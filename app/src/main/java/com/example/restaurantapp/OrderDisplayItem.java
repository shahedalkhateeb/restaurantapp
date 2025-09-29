package com.example.restaurantapp;
public class OrderDisplayItem {
    public String date;
    public double total;
    public String summary;

    public OrderDisplayItem(String date, double total, String summary) {
        this.date = date;
        this.total = total;
        this.summary = summary;
    }
}
