package com.example.restaurantapp;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItem item);

    @Delete
    void delete(CartItem item);

    @Query("SELECT * FROM cart_items")
    List<CartItem> getAllItems();

    @Query("DELETE FROM cart_items")
    void clearCart();
}

