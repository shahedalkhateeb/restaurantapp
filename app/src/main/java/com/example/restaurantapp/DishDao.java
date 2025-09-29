package com.example.restaurantapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DishDao {

    @Insert
    void insert(Dish dish);

    @Insert
    void insertAll(List<Dish> dishes);

    @Query("SELECT * FROM dishes")
    List<Dish> getAllDishes();

    @Query("SELECT * FROM dishes WHERE category = :category")
    List<Dish> getDishesByCategory(String category);
}
