package com.example.restaurantapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import java.util.List;
import java.util.concurrent.Executors;

public class ActiveOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_orders, container, false);
        recyclerView = view.findViewById(R.id.activeOrdersRecycler);
        db = AppDatabase.getInstance(requireContext());

        loadOrders();

        return view;
    }

    private void loadOrders() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Order> activeOrders = db.orderDao().getOrdersByStatus("Pending");

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        OrdersAdapter adapter = new OrdersAdapter(activeOrders, "Pending", getContext(), new OnOrderStatusChangedListener() {
                            @Override
                            public void onOrderMarkedCompleted() {
                                if (isAdded() && getActivity() != null) {
                                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                                    if (viewPager != null) {
                                        viewPager.setCurrentItem(1, true); // التاب رقم 1 = Completed
                                    }
                                }
                            }

                            @Override
                            public void onOrderMarkedCancelled() {
                                if (isAdded() && getActivity() != null) {
                                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                                    if (viewPager != null) {
                                        viewPager.setCurrentItem(2, true); // التاب رقم 2 = Cancelled
                                    }
                                }
                            }
                        });

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    });
                }
            } catch (Exception e) {
                Log.e("ActiveOrdersFragment", "Error loading orders", e);
            }
        });
    }
}



