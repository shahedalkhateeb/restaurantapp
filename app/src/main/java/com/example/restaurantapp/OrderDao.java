package com.example.restaurantapp;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insertOrder(Order order);
    @Update
    void updateOrder(Order order);

    @Query("SELECT * FROM orders ORDER BY id DESC")
    List<Order> getAllOrders();
    @Query("SELECT * FROM orders WHERE LOWER(status) = LOWER(:status) ORDER BY id DESC")
    List<Order> getOrdersByStatus(String status);

}
