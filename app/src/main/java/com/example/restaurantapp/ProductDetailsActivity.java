package com.example.restaurantapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProductDetailsActivity extends AppCompatActivity {

    ImageView productImage;
    TextView productName, productPrice, productDescription;
    Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        productName.setText("Burger With Meat");
        productPrice.setText("$12.23 - Free Delivery");
        productDescription.setText("Burger With Meat is a typical food for our area...");

        btnAddToCart.setOnClickListener(v -> {
            // إضافة المنتج للسلة
            CartItem item = new CartItem("Burger With Meat", 12.23, 1, "String imageUrl =https://upload.wikimedia.org/wikipedia/commons/0/0b/Burger_King_Burger.jpg");
            AppDatabase.getInstance(this).cartDao().insert(item);
            Toast.makeText(this, "تمت إضافة المنتج للسلة", Toast.LENGTH_SHORT).show();
        });
    }
}
