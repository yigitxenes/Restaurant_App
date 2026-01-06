package com.example.restaurantApp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class WelcomeActivity extends AppCompatActivity {

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {

                    Toast.makeText(WelcomeActivity.this, "Vazgeçildi", Toast.LENGTH_SHORT).show();
                } else {

                    String qrValue = result.getContents(); // E.g. "5" or "Table-5"

                    Toast.makeText(WelcomeActivity.this, "Masa Bulundu: " + qrValue, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    intent.putExtra("USER_TYPE", "CUSTOMER");
                    intent.putExtra("TABLE_ID", qrValue);
                    startActivity(intent);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button btnCustomer = findViewById(R.id.btnCustomer);
        Button btnStaff = findViewById(R.id.btnStaff);

        // Customer Login: Start QR Scanner
        btnCustomer.setOnClickListener(v -> {
            ScanOptions options = new ScanOptions();
            options.setPrompt("Lütfen Masadaki QR Kodu Okutun");
            options.setBeepEnabled(true);
            options.setOrientationLocked(true);
            options.setCaptureActivity(CaptureActivityPortrait.class);
            barcodeLauncher.launch(options);
        });

        // Staff Login: Go to Login Screen
        btnStaff.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}