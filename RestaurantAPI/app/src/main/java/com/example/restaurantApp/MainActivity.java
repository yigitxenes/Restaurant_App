package com.example.restaurantApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantApp.adapter.MenuAdapter;
import com.example.restaurantApp.entity.MenuItem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView textWelcome, textTableInfo;
    private MenuAdapter adapter;
    private Long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerViewMenu);
        textWelcome = findViewById(R.id.textWelcome);
        textTableInfo = findViewById(R.id.textTableInfo);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get intent data
        String userType = getIntent().getStringExtra("USER_TYPE");
        String userName = getIntent().getStringExtra("USER_NAME");
        String tableId = getIntent().getStringExtra("TABLE_ID");

        long userIdParam = getIntent().getLongExtra("USER_ID", -1);
        if (userIdParam != -1) {
            currentUserId = userIdParam;
        }

        // Display info
        if ("CUSTOMER".equals(userType)) {
            textWelcome.setText("Hoşgeldiniz Misafirimiz");
            if (tableId != null) {
                textTableInfo.setText("Masa No: " + tableId);
            }
        } else if (userName != null) {
            textWelcome.setText("Merhaba, " + userName);
            textTableInfo.setText("Personel Girişi");
        }

        // Go to Basket Button
        findViewById(R.id.btnGoToBasket).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BasketActivity.class);
            // Pass table ID
            intent.putExtra("TABLE_ID", getIntent().getStringExtra("TABLE_ID"));
            if (currentUserId != null) {
                intent.putExtra("USER_ID", currentUserId);
            }
            startActivity(intent);
        });

        // Siparişlerim Butonu
        findViewById(R.id.btnMyOrders).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CustomerOrdersActivity.class);
            String tableIdStr = getIntent().getStringExtra("TABLE_ID");
            if (tableIdStr != null) {
                try {
                    intent.putExtra("TABLE_ID", Long.parseLong(tableIdStr));
                    intent.putExtra("TABLE_NUM", Integer.parseInt(tableIdStr));
                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Masa bilgisi geçersiz", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            startActivity(intent);
        });

        // Fetch Menu
        fetchMenu();
    }

    private void fetchMenu() {
        // Check IP Address
        RestaurantApiService apiService = RetrofitClient.getApiService();

        apiService.getActiveMenu().enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MenuItem> menuList = response.body();

                    // Adaptörü Kur ve Listeyi Ver
                    adapter = new MenuAdapter(menuList);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(MainActivity.this, "Menü yüklenemedi!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Bağlantı Hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}