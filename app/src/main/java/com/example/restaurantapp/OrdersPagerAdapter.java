package com.example.restaurantapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class OrdersPagerAdapter extends FragmentStateAdapter {

    public OrdersPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0: return new ActiveOrdersFragment();
            case 1: return new CompletedOrdersFragment();
            case 2: return new CancelledOrdersFragment();
            default: return new ActiveOrdersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
