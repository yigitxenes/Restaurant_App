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
        // Tasarım dosyasını (item_menu.xml) bağladığımız yer
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        MenuItem item = menuList.get(position);

        holder.nameText.setText(item.getName());
        holder.categoryText.setText(item.getCategory());
        holder.priceText.setText(item.getPrice() + " ₺");
        holder.foodImage.setImageResource(R.mipmap.ic_launcher);

        // GÜNCELLENEN KISIM:
        holder.addButton.setOnClickListener(v -> {
            // 1. Sepete Ekle
            BasketManager.getInstance().addItem(item);

            // 2. Kullanıcıya Bilgi Ver
            Toast.makeText(v.getContext(), item.getName() + " sepete eklendi!", Toast.LENGTH_SHORT).show();

            // (İsteğe bağlı) Burada ana ekrandaki "Sepete Git" butonunu güncelleyebiliriz ileride.
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    // İŞTE SORDUĞUN "ViewHolder" SINIFI BURASI
    public static class MenuViewHolder extends RecyclerView.ViewHolder {

        ImageView foodImage;
        TextView nameText, categoryText, priceText;
        Button addButton;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            // item_menu.xml içindeki ID'leri burada bulup eşleştiriyoruz
            foodImage = itemView.findViewById(R.id.imageFood);
            nameText = itemView.findViewById(R.id.textName);
            categoryText = itemView.findViewById(R.id.textCategory);
            priceText = itemView.findViewById(R.id.textPrice);
            addButton = itemView.findViewById(R.id.btnAdd);
        }
    }
}