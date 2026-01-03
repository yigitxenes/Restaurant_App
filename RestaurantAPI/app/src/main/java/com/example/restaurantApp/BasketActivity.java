package com.example.restaurantApp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fragment container olarak kullanacağımız boş bir FrameLayout
        // veya direkt content view set edip dinamik ekleyebiliriz.
        // Basitlik için activity_basket.xml yerine programatik ekliyoruz veya
        // boş bir layout kullanabilirsin. Burada dinamik ekleme yapıyoruz:

        setContentView(R.layout.activity_basket);
        // Not: activity_basket.xml içindeki view'ları Fragment kullanacak.
        // Ancak Activity'nin kendi layoutu olarak boş bir container (FrameLayout) olması daha doğrudur.
        // Eğer activity_basket.xml içinde id'si "fragment_container" olan bir FrameLayout varsa şöyle yap:
        /*
        if (savedInstanceState == null) {
            BasketFragment fragment = new BasketFragment();
            Bundle args = new Bundle();
            args.putString("TABLE_ID", getIntent().getStringExtra("TABLE_ID"));
            args.putLong("USER_ID", getIntent().getLongExtra("USER_ID", -1));
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment) // XML'de bu ID olmalı
                    .commit();
        }
        */

        // Hızlı çözüm (XML değiştirmeden):
        // Fragment'ı activity_basket.xml'in root view'ı üzerine bindiremeyiz.
        // Bu yüzden Activity için basit bir XML oluşturup (activity_host.xml) içine FrameLayout koymalısın.
        // Ama şimdilik mevcut yapıyı bozmadan Fragment kullanımını göstermek için
        // BasketActivity mantığını tamamen Fragment'a taşıdım (Yukarıdaki kod).

        // Activity sadece host olacaksa:
        if (savedInstanceState == null) {
            BasketFragment fragment = new BasketFragment();
            Bundle args = new Bundle();
            args.putString("TABLE_ID", getIntent().getStringExtra("TABLE_ID"));
            args.putLong("USER_ID", getIntent().getLongExtra("USER_ID", -1));
            fragment.setArguments(args);

            // Layoutu dinamik olarak değiştirelim (id çakışması olmasın diye)
            android.widget.FrameLayout container = new android.widget.FrameLayout(this);
            container.setId(android.view.View.generateViewId());
            setContentView(container);

            getSupportFragmentManager().beginTransaction()
                    .add(container.getId(), fragment)
                    .commit();
        }
    }
}