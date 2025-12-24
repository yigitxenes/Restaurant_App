package com.example.restaurantApp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantApp.dto.OrderResponse;
import com.example.restaurantApp.dto.UpdateStatusRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StaffOrderActivity extends AppCompatActivity {

    private TextView textTableTitle, textStatus, textOrderDetails;
    private Button btnNextStatus;

    private Long tableId;
    private Long staffId;
    private Long currentOrderId;
    private String currentStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_order); // Layout aşağıda

        textTableTitle = findViewById(R.id.textTableTitle);
        textStatus = findViewById(R.id.textCurrentStatus);
        textOrderDetails = findViewById(R.id.textOrderDetails);
        btnNextStatus = findViewById(R.id.btnNextStatus);

        tableId = getIntent().getLongExtra("TABLE_ID", -1);
        int tableNum = getIntent().getIntExtra("TABLE_NUM", 0);
        staffId = getIntent().getLongExtra("STAFF_ID", -1);

        textTableTitle.setText("Masa " + tableNum + " Siparişi");

        fetchActiveOrder();

        btnNextStatus.setOnClickListener(v -> updateStatus());
    }

    private void fetchActiveOrder() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.122:8080/") // IP KONTROL
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestaurantApiService apiService = retrofit.create(RestaurantApiService.class);

        apiService.getTableActiveOrder(tableId).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showOrder(response.body());
                } else {
                    textOrderDetails.setText("Bu masada aktif sipariş yok.");
                    btnNextStatus.setVisibility(View.GONE);
                    textStatus.setText("-");
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                textOrderDetails.setText("Hata: " + t.getMessage());
            }
        });
    }

    private void showOrder(OrderResponse order) {
        currentOrderId = order.getId();
        currentStatus = order.getStatus(); // Örn: RECEIVED

        textStatus.setText("Durum: " + currentStatus);

        StringBuilder details = new StringBuilder();
        // Items listesini döngüye al (OrderResponse içinde items Listesi olmalı)
        // Eğer OrderResponse içinde items yoksa eklememiz gerekebilir, şimdilik basit yazıyorum
        details.append("Sipariş ID: ").append(order.getId()).append("\n\n");

        // Eğer items listesine erişimin varsa:
        /*
        for(OrderResponse.OrderItemResponse item : order.getItems()) {
             details.append("- ").append(item.getMenuItemName()).append(" x").append(item.getQuantity()).append("\n");
        }
        */

        textOrderDetails.setText(details.toString());

        // Buton Metnini Ayarla
        configureNextButton(currentStatus);
    }

    private void configureNextButton(String status) {
        btnNextStatus.setVisibility(View.VISIBLE);
        switch (status) {
            case "RECEIVED":
                btnNextStatus.setText("Hazırlanıyor'a Geç");
                btnNextStatus.setTag("PREPARING");
                break;
            case "PREPARING":
                btnNextStatus.setText("Hazır'a Geç");
                btnNextStatus.setTag("READY");
                break;
            case "READY":
                btnNextStatus.setText("Teslim Et");
                btnNextStatus.setTag("DELIVERED");
                break;
            default:
                btnNextStatus.setVisibility(View.GONE);
        }
    }

    private void updateStatus() {
        String nextStatus = (String) btnNextStatus.getTag();
        if (nextStatus == null) return;

        UpdateStatusRequest req = new UpdateStatusRequest();
        req.setNewStatus(nextStatus);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.122:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestaurantApiService api = retrofit.create(RestaurantApiService.class);

        api.updateOrderStatus(currentOrderId, staffId, req).enqueue(new Callback<OrderResponse>() {
            @Override
            public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StaffOrderActivity.this, "Güncellendi!", Toast.LENGTH_SHORT).show();
                    fetchActiveOrder(); // Ekranı yenile
                } else {
                    Toast.makeText(StaffOrderActivity.this, "Güncelleme Başarısız", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrderResponse> call, Throwable t) {
                Toast.makeText(StaffOrderActivity.this, "Hata", Toast.LENGTH_SHORT).show();
            }
        });
    }
}