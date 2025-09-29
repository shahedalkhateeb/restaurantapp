package com.example.restaurantapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.regex.MatchResult;

public class HomeFragment extends Fragment {

    private RecyclerView categoriesRecycler, popularRecycler;
    private TextInputEditText searchInput;
    private ImageView filterIcon;
    private DishAdapter dishAdapter;
    private CategoryAdapter categoryAdapter;
    private AppDatabase db;

    private List<Dish> allDishes = new ArrayList<>();
    private List<Dish> filteredDishes = new ArrayList<>();
    private List<String> categories = Arrays.asList("Starters", "Main", "Deserts", "Drinks");

    private String selectedCategory = "";
    private String sortOrder = "none";
    private double maxPrice = 100;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        categoriesRecycler = view.findViewById(R.id.categoriesRecycler);
        popularRecycler = view.findViewById(R.id.popularRecycler);
        searchInput = view.findViewById(R.id.searchInput);
        filterIcon = view.findViewById(R.id.filterIcon);
        db = AppDatabase.getInstance(requireContext());
        setupCategories();
        seedDataIfNeeded();
        loadDishes();
        filterIcon.setOnClickListener(v -> {
            FilterBottomSheet sheet = new FilterBottomSheet();
            sheet.setOnFilterAppliedListener((category, maxPrice, sortOrder) -> {
                selectedCategory = category.equals("All") ? "" : category;
                this.maxPrice = maxPrice;
                this.sortOrder = sortOrder;
                applyFilters(searchInput.getText().toString());
            });
            sheet.show(getParentFragmentManager(), "FilterSheet");
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        return view;
    }

    private void setupCategories() {
        categoryAdapter = new CategoryAdapter(categories, category -> {
            selectedCategory = category.trim();
            applyFilters(searchInput.getText().toString());
        });
        categoriesRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoriesRecycler.setAdapter(categoryAdapter);
    }

    private void seedDataIfNeeded() {
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        boolean isFirstRun = prefs.getBoolean("is_first_run", true);

        if (isFirstRun) {
            List<Dish> sampleDishes = Arrays.asList(
                    new Dish("Pizza", "Main", 12.99,
                            "https://cdn.pixabay.com/photo/2017/12/09/08/18/pizza-3007395_1280.jpg",
                            "بيتزا إيطالية بالجبنة والمكونات الطازجة", 4.5f, 120),

                    new Dish("Soup", "Starters", 6.50,
                            "https://cdn.pixabay.com/photo/2016/09/21/20/18/pumpkin-soup-1685574_1280.jpg",
                            "شوربة اليقطين الدافئة مع التوابل", 4.2f, 80),

                    new Dish("Cake", "Desserts", 8.75,
                            "https://cdn.pixabay.com/photo/2016/02/29/00/19/cake-1227842_1280.jpg",
                            "كيك الشوكولاتة الغني والمميز", 4.8f, 150),

                    new Dish("Juice", "Drinks", 4.25,
                            "https://cdn.pixabay.com/photo/2025/07/15/04/01/grapefruit-9715105_640.jpg",
                            "عصير الجريب فروت الطازج والمنعش", 4.0f, 60)
            );

            Executors.newSingleThreadExecutor().execute(() -> {
                for (Dish dish : sampleDishes) {
                    db.dishDao().insert(dish);
                }
                prefs.edit().putBoolean("is_first_run", false).apply();
            });
        }
    }

    private void loadDishes() {
        Executors.newSingleThreadExecutor().execute(() -> {
            allDishes = db.dishDao().getAllDishes();
            filteredDishes.clear();
            filteredDishes.addAll(allDishes);

            requireActivity().runOnUiThread(() -> {
                dishAdapter = new DishAdapter(filteredDishes);
                popularRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
                popularRecycler.setAdapter(dishAdapter);
            });
        });
    }

    private void applyFilters(String query) {
        filteredDishes.clear();
        for (Dish dish : allDishes) {
            boolean matchesCategory = selectedCategory.isEmpty() || dish.category.equalsIgnoreCase(selectedCategory.trim());
            boolean matchesQuery = dish.name.toLowerCase().contains(query.toLowerCase());
            boolean matchesPrice = dish.price <= maxPrice;

            if (matchesCategory && matchesQuery && matchesPrice) {
                filteredDishes.add(dish);
            }
        }

        if (sortOrder.equals("asc")) {
            Collections.sort(filteredDishes, Comparator.comparingDouble(d -> d.price));
        } else if (sortOrder.equals("desc")) {
            Collections.sort(filteredDishes, (d1, d2) -> Double.compare(d2.price, d1.price));
        }

        requireActivity().runOnUiThread(() -> {
            dishAdapter.notifyDataSetChanged();
            if (filteredDishes.isEmpty()) {
                Toast.makeText(getContext(), "لا يوجد أطباق مطابقة", Toast.LENGTH_SHORT).show();
            }
        });

    }
}






