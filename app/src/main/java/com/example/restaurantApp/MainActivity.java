package com.example.restaurantApp;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView textViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewStatus = findViewById(R.id.textViewStatus);
        textViewStatus.setText("Menü yükleniyor...");

        // 1. Retrofit Kurulumu
        // DİKKAT: Emulator için localhost "10.0.2.2" adresidir!
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // 2. Servisi Oluştur
        RestaurantApiService apiService = retrofit.create(RestaurantApiService.class);

        // 3. İsteği Gönder (Asenkron)
        apiService.getActiveMenu().enqueue(new Callback<List<MenuItem>>() {
            @Override
            public void onResponse(Call<List<MenuItem>> call, Response<List<MenuItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<MenuItem> menu = response.body();

                    // Gelen veriyi ekrana basalım
                    StringBuilder builder = new StringBuilder();
                    builder.append("BAĞLANTI BAŞARILI! 🎉\n\n");

                    for (MenuItem item : menu) {
                        builder.append("🍔 ").append(item.toString()).append("\n");
                    }
                    textViewStatus.setText(builder.toString());
                } else {
                    textViewStatus.setText("Hata: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MenuItem>> call, Throwable t) {
                textViewStatus.setText("Bağlantı Koptu: " + t.getMessage());
                t.printStackTrace(); // Logcat'e hatayı basar
            }
        });
    }
}