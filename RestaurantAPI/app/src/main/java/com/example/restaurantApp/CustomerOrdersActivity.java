package com.example.restaurantApp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantApp.dto.OrderResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerOrdersActivity extends AppCompatActivity {

    private ListView listViewOrders;
    private TextView textTitle;
    private Long tableId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_orders);

        listViewOrders = findViewById(R.id.listViewOrders);
        textTitle = findViewById(R.id.textTitle);

        tableId = getIntent().getLongExtra("TABLE_ID", -1);
        int tableNum = getIntent().getIntExtra("TABLE_NUM", 0);

        if (tableNum > 0) {
            textTitle.setText("Masa " + tableNum + " - Siparişlerim");
        }

        fetchTableOrders();
    }

    private void fetchTableOrders() {
        if (tableId == -1) {
            Toast.makeText(this, "Masa bilgisi bulunamadı", Toast.LENGTH_SHORT).show();
            return;
        }

        RestaurantApiService apiService = RetrofitClient.getApiService();

        apiService.getTableOrders(tableId).enqueue(new Callback<List<OrderResponse>>() {
            @Override
            public void onResponse(Call<List<OrderResponse>> call, Response<List<OrderResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayOrders(response.body());
                } else {
                    Toast.makeText(CustomerOrdersActivity.this, "Siparişler yüklenemedi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderResponse>> call, Throwable t) {
                Toast.makeText(CustomerOrdersActivity.this, "Bağlantı hatası: " + t.getMessage(), Toast.LENGTH_LONG)
                        .show();
            }
        });
    }

    private List<OrderResponse> currentOrders = new ArrayList<>();

    private void displayOrders(List<OrderResponse> orders) {
        this.currentOrders = orders;

        if (orders.isEmpty()) {
            List<String> emptyList = new ArrayList<>();
            emptyList.add("Aktif siparişiniz bulunmamaktadır.");
            listViewOrders.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emptyList));
            return;
        }

        List<String> orderDisplayList = new ArrayList<>();
        // SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm",
        // Locale.getDefault()); // Unused logic

        for (OrderResponse order : orders) {
            String statusEmoji = getStatusEmoji(order.getStatus());
            String statusText = getStatusText(order.getStatus());

            int itemCount = (order.getItems() != null) ? order.getItems().size() : 0;

            String displayText = String.format(
                    "%s Sipariş #%d\n%s\n%d ürün (Detay için dokunun)",
                    statusEmoji,
                    order.getId(),
                    statusText,
                    itemCount);

            orderDisplayList.add(displayText);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderDisplayList);
        listViewOrders.setAdapter(adapter);

        listViewOrders.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < currentOrders.size()) {
                showOrderDetails(currentOrders.get(position));
            }
        });
    }

    private void showOrderDetails(OrderResponse order) {
        StringBuilder details = new StringBuilder();
        details.append("Sipariş Durumu: ").append(getStatusText(order.getStatus())).append("\n\n");

        if (order.getItems() != null) {
            for (OrderResponse.OrderItemResponse item : order.getItems()) {
                String itemName = (item.getMenuItem() != null) ? item.getMenuItem().getName() : "Bilinmeyen Ürün";
                Double price = (item.getMenuItem() != null) ? item.getMenuItem().getPrice() : 0.0;
                details.append("• ").append(itemName)
                        .append(" - ").append(price).append(" TL\n");
            }
        }

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Sipariş #" + order.getId() + " Detayı")
                .setMessage(details.toString())
                .setPositiveButton("Tamam", null)
                .show();
    }

    private String getStatusEmoji(String status) {
        switch (status) {
            case "RECEIVED":
                return "🔵";
            case "PREPARING":
                return "🟠";
            case "READY":
                return "🟢";
            case "DELIVERED":
                return "⚪";
            default:
                return "❓";
        }
    }

    private String getStatusText(String status) {
        switch (status) {
            case "RECEIVED":
                return "Alındı";
            case "PREPARING":
                return "Hazırlanıyor";
            case "READY":
                return "Hazır";
            case "DELIVERED":
                return "Teslim Edildi";
            default:
                return status;
        }
    }
}
