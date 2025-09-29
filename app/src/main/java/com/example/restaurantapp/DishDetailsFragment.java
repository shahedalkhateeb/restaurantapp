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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executors;

public class DishDetailsFragment extends Fragment {

    private ImageView dishImage;
    private TextView dishName, dishPrice, dishCategory;
    private Button btnAddToCart;
    private Dish selectedDish;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dish_details, container, false);

        dishImage = view.findViewById(R.id.detailDishImage);
        dishName = view.findViewById(R.id.detailDishName);
        dishPrice = view.findViewById(R.id.detailDishPrice);
        dishCategory = view.findViewById(R.id.detailDishCategory);
        btnAddToCart = view.findViewById(R.id.btnAddToCartDetail);

        if (getArguments() != null) {
            selectedDish = (Dish) getArguments().getSerializable("dish");

            dishName.setText(selectedDish.name);
            dishPrice.setText("₪" + selectedDish.price);
            dishCategory.setText("الفئة: " + selectedDish.category);

            Glide.with(requireContext())
                    .load(selectedDish.imageUrl)
                    .centerCrop()
                    .into(dishImage);
        }

        btnAddToCart.setOnClickListener(v -> {
            CartItem item = new CartItem(
                    selectedDish.name,
                    selectedDish.price,
                    1,
                    selectedDish.imageUrl
            );

            Executors.newSingleThreadExecutor().execute(() -> {
                AppDatabase.getInstance(requireContext()).cartDao().insert(item);
            });

            Toast.makeText(getContext(), "تمت إضافة الطبق إلى السلة", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}
