package com.example.restaurantapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class Order {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String userId;
    public String date;
    public double totalPrice;
    public String itemsSummary;
    public String dishName;
    public String imageUrl;
    public String status;

    public Order(String userId, String date, double totalPrice, String itemsSummary,
                 String dishName, String imageUrl, String status) {
        this.userId = userId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.itemsSummary = itemsSummary;
        this.dishName = dishName;
        this.imageUrl = imageUrl;
        this.status = status;
    }
}



