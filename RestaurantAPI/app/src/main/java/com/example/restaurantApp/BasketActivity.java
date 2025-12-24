package com.example.restaurantApp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantApp.adapter.BasketAdapter;
import com.example.restaurantApp.dto.CreateOrderRequest;
import com.example.restaurantApp.dto.OrderLineRequest;
import com.example.restaurantApp.dto.OrderResponse;
import com.example.restaurantApp.entity.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView totalPriceText;
    private Button confirmButton;
    private BasketAdapter adapter;
    private String tableId; // Masa numarası lazım
    private Long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        // MainActivity'den gelen Masa Numarasını al
        tableId = getIntent().getStringExtra("TABLE_ID");
        long userIdParam = getIntent().getLongExtra("USER_ID", -1);
        if (userIdParam != -1) {
            currentUserId = userIdParam;
        }

        recyclerView = findViewById(R.id.recyclerViewBasket);
        totalPriceText = findViewById(R.id.textTotalPrice);
        confirmButton = findViewById(R.id.btnConfirmOrder);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Adaptörü kur ve "Liste değişirse fiyatı güncelle" emrini ver
        adapter = new BasketAdapter(this::updateTotalPrice);
        recyclerView.setAdapter(adapter);

        updateTotalPrice();

        confirmButton.setOnClickListener(v -> sendOrderToBackend());
    }

    private void updateTotalPrice() {
        double total = BasketManager.getInstance().getTotalPrice();
        totalPriceText.setText(total + " ₺");
    }

    private void sendOrderToBackend() {

        if (BasketManager.getInstance().getItems().isEmpty()) {
            Toast.makeText(this, "Sepetiniz boş!", Toast.LENGTH_SHORT).show();
            return;
        }

        CreateOrderRequest request = new CreateOrderRequest();
        if (currentUserId != null) {
            request.setCustomerId(currentUserId);
        } else {
            request.setCustomerId(1L); // <--- MİSAFİR ID'Sİ
        }
        // Backend'in beklediği formatı hazırla

        request.setCustomerId(currentUserId);

        // Eğer masa numarası yoksa varsayılan 1 yap (Test için)
        Long tId = (tableId != null) ? Long.parseLong(tableId) : 1L;
        request.setTableId(tId);

        List<OrderLineRequest> itemRequests = new ArrayList<>();
        for (Map.Entry<MenuItem, Integer> entry : BasketManager.getInstance().getItems().entrySet()) {
            OrderLineRequest itemReq = new OrderLineRequest();
            itemReq.setMenuItemId(entry.getKey().getId());
            itemReq.setQuantity(entry.getValue());
            itemRequests.add(itemReq);
        }
        request.setItems(itemRequests);

        // RETROFIT İLE GÖNDER
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.122:8080/") // <-- IP ADRESİNİ YAZMAYI UNUTMA
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestaurantApiService apiService = retrofit.create(RestaurantApiService.class);

        apiService.placeOrder(request).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(BasketActivity.this, "Siparişiniz Alındı! Afiyet Olsun 🍜", Toast.LENGTH_LONG).show();
                    BasketManager.getInstance().clearBasket(); // Sepeti boşalt
                    finish(); // Ana ekrana dön
                } else {
                    Toast.makeText(BasketActivity.this, "Hata: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(BasketActivity.this, "Bağlantı Hatası!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}