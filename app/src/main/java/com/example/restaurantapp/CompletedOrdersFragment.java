package com.example.restaurantapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.Executors;

public class CompletedOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_completed_orders, container, false);
        recyclerView = view.findViewById(R.id.completedOrdersRecycler);
        db = AppDatabase.getInstance(requireContext());

        loadOrders();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadOrders();
    }

    private void loadOrders() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<Order> completedOrders = db.orderDao().getOrdersByStatus("Completed");

            requireActivity().runOnUiThread(() -> {
                OrdersAdapter adapter = new OrdersAdapter(completedOrders, "Completed", getContext(), null);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            });
        });
    }
}

