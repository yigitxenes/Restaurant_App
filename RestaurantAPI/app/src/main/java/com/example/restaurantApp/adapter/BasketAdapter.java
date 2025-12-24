package com.example.restaurantApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantApp.BasketManager;
import com.example.restaurantApp.R;
import com.example.restaurantApp.entity.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    private List<MenuItem> cartItems;
    private Runnable onListUpdatedListener; // Listeden eleman silinince Activity'e haber vermek için

    public BasketAdapter(Runnable onListUpdatedListener) {
        this.onListUpdatedListener = onListUpdatedListener;
        updateData();
    }

    public void updateData() {
        // Map yapısını Listeye çeviriyoruz ki RecyclerView gösterebilsin
        this.cartItems = new ArrayList<>(BasketManager.getInstance().getItems().keySet());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BasketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket, parent, false);
        return new BasketViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BasketViewHolder holder, int position) {
        MenuItem item = cartItems.get(position);
        int quantity = BasketManager.getInstance().getItems().get(item);
        double totalPrice = item.getPrice().doubleValue() * quantity;

        holder.nameText.setText(item.getName());
        holder.quantityText.setText(quantity + " Adet");
        holder.priceText.setText(totalPrice + " ₺");

        // Silme Butonu
        holder.deleteButton.setOnClickListener(v -> {
            BasketManager.getInstance().removeItem(item);
            updateData(); // Listeyi yenile
            onListUpdatedListener.run(); // Activity'e "Fiyatı güncelle" de
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class BasketViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, quantityText, priceText;
        ImageButton deleteButton;

        public BasketViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.textBasketFoodName);
            quantityText = itemView.findViewById(R.id.textBasketQuantity);
            priceText = itemView.findViewById(R.id.textBasketPrice);
            deleteButton = itemView.findViewById(R.id.btnDelete);
        }
    }
}