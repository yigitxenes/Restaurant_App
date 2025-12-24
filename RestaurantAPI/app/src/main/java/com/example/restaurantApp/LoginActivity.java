package com.example.restaurantApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.restaurantApp.dto.LoginRequest;
import com.example.restaurantApp.entity.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private EditText emailInput;
    private EditText passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(v -> login());
    }

    private void login() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show();
            return;
        }

        // RETROFIT KURULUMU (IP Adresini Kontrol Et!)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.122:8080/") // <-- BURAYA KENDİ IP ADRESİNİ YAZ
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestaurantApiService apiService = retrofit.create(RestaurantApiService.class);

        // İstek Hazırlama
        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        // Sunucuya Gönderme
        apiService.login(request).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    Intent intent;
                    if (user.getRole() == com.example.restaurantApp.enums.Role.STAFF) {
                        intent = new Intent(LoginActivity.this, StaffTablesActivity.class);
                    } else {
                        intent = new Intent(LoginActivity.this, MainActivity.class);
                    }

                    intent.putExtra("USER_ID", user.getId());
                    intent.putExtra("USER_NAME", user.getName());
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Giriş Başarısız! Bilgileri kontrol et.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Bağlantı Hatası: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}