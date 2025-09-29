package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Executors;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private final List<Dish> dishList;

    public DishAdapter(List<Dish> dishList) {
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dish, parent, false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder, int position) {
        Dish dish = dishList.get(position);
        holder.dishName.setText(dish.name);
        holder.dishPrice.setText("₪" + dish.price);

        Glide.with(holder.itemView.getContext())
                .load(dish.imageUrl)
                .centerCrop()
                .into(holder.dishImage);

        //  التنقل إلى شاشة التفاصيل
        holder.itemView.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("dish", dish);
            Navigation.findNavController(holder.itemView).navigate(R.id.dishDetailsFragment, bundle);
        });

        // إضافة إلى السلة
        holder.btnAddToCart.setOnClickListener(v -> {
            CartItem item = new CartItem(
                    dish.name,
                    dish.price,
                    1,
                    dish.imageUrl
            );

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(holder.itemView.getContext()).cartDao().insert(item);
            });

            Toast.makeText(holder.itemView.getContext(), "تمت إضافة الطبق إلى السلة", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }

    static class DishViewHolder extends RecyclerView.ViewHolder {
        TextView dishName, dishPrice;
        ImageView dishImage;
        Button btnAddToCart;

        public DishViewHolder(@NonNull View itemView) {
            super(itemView);
            dishName = itemView.findViewById(R.id.dishName);
            dishPrice = itemView.findViewById(R.id.dishPrice);
            dishImage = itemView.findViewById(R.id.dishImage);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}



