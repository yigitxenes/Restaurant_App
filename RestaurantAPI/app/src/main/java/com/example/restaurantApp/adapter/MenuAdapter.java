package com.example.restaurantApp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantApp.BasketManager;
import com.example.restaurantApp.R;
import com.example.restaurantApp.entity.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private List<MenuItem> menuList;

    public MenuAdapter(List<MenuItem> menuList) {
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout (item_menu.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        holder.nameText.setText(item.getName());
        holder.categoryText.setText(item.getCategory());
        holder.priceText.setText(item.getPrice() + " ₺");
        // Dynamic image loading: Match item name to drawable resource
        // E.g. "Cheese Burger" -> "cheese_burger"
        Context context = holder.itemView.getContext();
        String imageName = item.getName().toLowerCase(java.util.Locale.ENGLISH)
                .replace(" ", "_")
                .replace("ç", "c")
                .replace("ğ", "g")
                .replace("ı", "i")
                .replace("ö", "o")
                .replace("ş", "s")
                .replace("ü", "u");

        int resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        if (resourceId != 0) {
            holder.foodImage.setImageResource(resourceId);
        } else {
            holder.foodImage.setImageResource(R.mipmap.ic_launcher);
        }

        holder.addButton.setOnClickListener(v -> {
            // 1. Add to Basket
            BasketManager.getInstance().addItem(item);

            // 2. Notify User
            Toast.makeText(v.getContext(), item.getName() + " sepete eklendi!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    // ViewHolder Class
    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView nameText, categoryText, priceText;
        Button addButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind views
            foodImage = itemView.findViewById(R.id.imageFood);
            nameText = itemView.findViewById(R.id.textName);
            categoryText = itemView.findViewById(R.id.textCategory);
            priceText = itemView.findViewById(R.id.textPrice);
            addButton = itemView.findViewById(R.id.btnAdd);
        }
    }
}