package com.example.restaurantapp;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "dishes")
public class Dish implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String category;
    public double price;

    @ColumnInfo(name = "image_url")
    public String imageUrl;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "rating")
    public float rating;

    @ColumnInfo(name = "order_count")
    public int orderCount;

    @Ignore
    public Dish(String name, String category, double price, String imageUrl,
                String description, float rating, int orderCount) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.rating = rating;
        this.orderCount = orderCount;
    }

    public Dish() {}
}


