package com.example.restaurantapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(bottomNavigation, navController);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_map) {
                try {
                    startActivity(new Intent(this, GoogleMapsActivity.class));
                } catch (Exception e) {
                    Log.e("MainActivity", "Error opening map", e);
                }
                return false;
            }
            NavigationUI.onNavDestinationSelected(item, navController);
            return true;
        });
    }
}


