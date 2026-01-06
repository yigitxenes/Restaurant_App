package com.example.restaurantApp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantApp.adapter.BasketAdapter;
import com.example.restaurantApp.dto.CreateOrderRequest;
import com.example.restaurantApp.dto.OrderLineRequest;
import com.example.restaurantApp.dto.OrderResponse;
import com.example.restaurantApp.entity.MenuItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BasketFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView totalPriceText;
    private Button confirmButton;
    private BasketAdapter adapter;
    private String tableId;
    private Long currentUserId;

    public BasketFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_basket, container, false); // activity_basket xml'ini
                                                                                  // kullanıyoruz

        // Argümanları al
        if (getArguments() != null) {
            tableId = getArguments().getString("TABLE_ID");
            currentUserId = getArguments().getLong("USER_ID", -1);
            if (currentUserId == -1)
                currentUserId = null;
        }

        recyclerView = view.findViewById(R.id.recyclerViewBasket);
        totalPriceText = view.findViewById(R.id.textTotalPrice);
        confirmButton = view.findViewById(R.id.btnConfirmOrder);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new BasketAdapter(this::updateTotalPrice);
        recyclerView.setAdapter(adapter);

        updateTotalPrice();

        confirmButton.setOnClickListener(v -> sendOrderWithBackgroundThread());

        return view;
    }

    private void updateTotalPrice() {
        double total = BasketManager.getInstance().getTotalPrice();
        totalPriceText.setText(total + " ₺");
    }

    // Background Thread Impl
    private void sendOrderWithBackgroundThread() {
        if (BasketManager.getInstance().getItems().isEmpty()) {
            Toast.makeText(getContext(), "Sepetiniz boş!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare request
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomerId(currentUserId != null ? currentUserId : 1L);
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

        RestaurantApiService apiService = RetrofitClient.getApiService();
        Call<OrderResponse> call = apiService.placeOrder(request);

        // Start Background Thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Background Thread
            try {
                // Synchronous request
                Response<OrderResponse> response = call.execute();

                // Return to Main Thread
                handler.post(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Siparişiniz Alındı! Afiyet Olsun 🍜", Toast.LENGTH_LONG).show();
                        BasketManager.getInstance().clearBasket();
                        if (getActivity() != null)
                            getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), "Hata: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                handler.post(() -> Toast
                        .makeText(getContext(), "Bağlantı Hatası: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}