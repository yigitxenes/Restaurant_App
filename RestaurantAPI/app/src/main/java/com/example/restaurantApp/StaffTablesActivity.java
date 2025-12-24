package com.example.restaurantApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantApp.entity.RestaurantTable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StaffTablesActivity extends AppCompatActivity {

    private ListView listViewTables;
    private Long currentStaffId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Basit bir layout oluşturalım (XML dosyası oluşturmaya gerek kalmadan kodla halledelim veya basit bir listview kullan)
        setContentView(R.layout.activity_staff_tables); // XML'i aşağıda vereceğim

        listViewTables = findViewById(R.id.listViewTables);
        currentStaffId = getIntent().getLongExtra("USER_ID", -1);

        fetchTables();
    }

    private void fetchTables() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.122:8080/") // IP ADRESİNİ KONTROL ET
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestaurantApiService apiService = retrofit.create(RestaurantApiService.class);

        apiService.getTables().enqueue(new Callback<List<RestaurantTable>>() {
            @Override
            public void onResponse(Call<List<RestaurantTable>> call, Response<List<RestaurantTable>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    displayTables(response.body());
                } else {
                    Toast.makeText(StaffTablesActivity.this, "Masa listesi alınamadı", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RestaurantTable>> call, Throwable t) {
                Toast.makeText(StaffTablesActivity.this, "Hata: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayTables(List<RestaurantTable> tables) {
        List<String> tableNames = new ArrayList<>();
        for (RestaurantTable t : tables) {
            tableNames.add("Masa " + t.getTableNumber());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableNames);
        listViewTables.setAdapter(adapter);

        listViewTables.setOnItemClickListener((parent, view, position, id) -> {
            RestaurantTable selectedTable = tables.get(position);

            // Sipariş Detayına Git
            Intent intent = new Intent(StaffTablesActivity.this, StaffOrderActivity.class);
            intent.putExtra("TABLE_ID", selectedTable.getId());
            intent.putExtra("TABLE_NUM", selectedTable.getTableNumber());
            intent.putExtra("STAFF_ID", currentStaffId);
            startActivity(intent);
        });
    }
}