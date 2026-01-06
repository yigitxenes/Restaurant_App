package com.example.restaurantApp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class BasketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        // Note: activity_basket.xml is used by the Fragment.
        // We use a programmatic FrameLayout as a container.

        // Setup container
        if (savedInstanceState == null) {
            BasketFragment fragment = new BasketFragment();
            Bundle args = new Bundle();
            args.putString("TABLE_ID", getIntent().getStringExtra("TABLE_ID"));
            args.putLong("USER_ID", getIntent().getLongExtra("USER_ID", -1));
            fragment.setArguments(args);

            // Create dynamic FrameLayout
            android.widget.FrameLayout container = new android.widget.FrameLayout(this);
            container.setId(android.view.View.generateViewId());
            setContentView(container);

            getSupportFragmentManager().beginTransaction()
                    .add(container.getId(), fragment)
                    .commit();
        }
    }
}