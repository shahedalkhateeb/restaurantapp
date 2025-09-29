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

public class CancelledOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cancelled_orders, container, false);
        recyclerView = view.findViewById(R.id.cancelledOrdersRecycler);
        db = AppDatabase.getInstance(requireContext());

        loadOrders();

        return view;
    }

    private void loadOrders() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                List<Order> cancelledOrders = db.orderDao().getOrdersByStatus("Cancelled");

                if (isAdded() && getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        OrdersAdapter adapter = new OrdersAdapter(cancelledOrders, "Cancelled", getContext(), new OnOrderStatusChangedListener() {
                            @Override
                            public void onOrderMarkedCompleted() {
                            }

                            @Override
                            public void onOrderMarkedCancelled() {
                                if (isAdded() && getActivity() != null) {
                                    ViewPager2 viewPager = getActivity().findViewById(R.id.viewPager);
                                    if (viewPager != null) {
                                        viewPager.setCurrentItem(0, true); // التاب رقم 0 = Active
                                    }
                                }
                            }
                        });

                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(adapter);
                    });
                }
            } catch (Exception e) {
                Log.e("CancelledOrdersFragment", "Error loading cancelled orders", e);
            }
        });
    }
}

