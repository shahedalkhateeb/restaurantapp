package com.example.restaurantapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

public class CartFragment extends Fragment {

    private RecyclerView cartRecycler;
    private Button btnConfirmOrder;
    private TextView totalPriceText;
    private CartAdapter cartAdapter;
    private AppDatabase db;
    private List<CartItem> cartItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        cartRecycler = view.findViewById(R.id.cartRecycler);
        totalPriceText = view.findViewById(R.id.totalPriceText);
        btnConfirmOrder = view.findViewById(R.id.btnConfirmOrder);
        db = AppDatabase.getInstance(requireContext());

        loadCartItems();
        setupPlaceOrder(view);

        return view;
    }

    private void loadCartItems() {
        Executors.newSingleThreadExecutor().execute(() -> {
            cartItems = db.cartDao().getAllItems();
            double total = 0;
            for (CartItem item : cartItems) {
                total += item.price * item.quantity;
            }

            double finalTotal = total;
            requireActivity().runOnUiThread(() -> {
                cartAdapter = new CartAdapter(requireContext(), cartItems, new CartAdapter.CartActionListener() {
                    @Override
                    public void onQuantityChanged(CartItem item, int newQuantity) {
                        item.quantity = newQuantity;
                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.cartDao().insert(item);
                            loadCartItems();
                        });
                    }

                    @Override
                    public void onItemDeleted(CartItem item) {
                        Executors.newSingleThreadExecutor().execute(() -> {
                            db.cartDao().delete(item);
                            loadCartItems();
                        });
                    }
                });

                cartRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                cartRecycler.setAdapter(cartAdapter);
                totalPriceText.setText("Total: ₪" + finalTotal);
            });
        });
    }

    private void setupPlaceOrder(View view) {
        btnConfirmOrder.setOnClickListener(v -> {
            Executors.newSingleThreadExecutor().execute(() -> {

                SharedPreferences prefs = requireContext().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE);
                int userIdInt = prefs.getInt("user_id", -1);
                String userId = String.valueOf(userIdInt);

                List<CartItem> items = db.cartDao().getAllItems();
                if (items.isEmpty()) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Cart is empty", Toast.LENGTH_SHORT).show()
                    );
                    return;
                }

                double total = 0;
                StringBuilder summary = new StringBuilder();

                for (CartItem item : items) {
                    total += item.price * item.quantity;
                    summary.append(item.name).append(" x").append(item.quantity).append(", ");
                }

                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
                String dishName = cartItems.size() == 1 ? cartItems.get(0).name : "Multiple Items";
                String imageUrl = cartItems.get(0).imageUrl;

                Order order = new Order(userId, date, total, summary.toString(), dishName, imageUrl, "Pending");

                db.orderDao().insertOrder(order);
                db.cartDao().clearCart();

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getContext(), "Order placed!", Toast.LENGTH_SHORT).show();
                    loadCartItems();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("cart_items", (Serializable) items);

                    NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.ordersFragment, bundle);
                });
            });
        });
    }
}

