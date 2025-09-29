package com.example.restaurantapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

interface OnOrderStatusChangedListener {
    void onOrderMarkedCompleted();
    void onOrderMarkedCancelled();
}

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private String status;
    private Context context;
    private OnOrderStatusChangedListener statusChangedListener;

    public OrdersAdapter(List<Order> orderList, String status, Context context, OnOrderStatusChangedListener listener) {
        this.orderList = orderList;
        this.status = status;
        this.context = context;
        this.statusChangedListener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.name.setText(order.dishName);
        holder.summary.setText("Qty: " + order.itemsSummary + " pcs");
        holder.total.setText("₪" + String.format("%.2f", order.totalPrice));
        holder.statusText.setText("Status: " + order.status);

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            Date parsedDate = inputFormat.parse(order.date);
            holder.date.setText(outputFormat.format(parsedDate));
        } catch (Exception e) {
            holder.date.setText(order.date);
        }

        Glide.with(context)
                .load(order.imageUrl)
                .centerCrop()
                .into(holder.image);

        if (status.equals("Pending")) {
            holder.btnComplete.setVisibility(View.VISIBLE);
            holder.btnCancel.setVisibility(View.VISIBLE);
            holder.btnReorder.setVisibility(View.GONE);

            holder.btnComplete.setOnClickListener(v -> {
                order.status = "Completed";
                orderList.remove(position);
                notifyItemRemoved(position);

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        AppDatabase.getInstance(context).orderDao().updateOrder(order);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (statusChangedListener != null) {
                                statusChangedListener.onOrderMarkedCompleted();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("OrdersAdapter", "Error during update (Completed)", e);
                    }
                });

                Toast.makeText(context, "تم تأكيد الطلب", Toast.LENGTH_SHORT).show();
            });

            holder.btnCancel.setOnClickListener(v -> {
                order.status = "Cancelled";
                orderList.remove(position);
                notifyItemRemoved(position);

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        AppDatabase.getInstance(context).orderDao().updateOrder(order);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (statusChangedListener != null) {
                                statusChangedListener.onOrderMarkedCancelled();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("OrdersAdapter", "Error during update (Cancelled)", e);
                    }
                });

                Toast.makeText(context, "تم إلغاء الطلب", Toast.LENGTH_SHORT).show();
            });

        } else if (status.equals("Cancelled")) {
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
            holder.btnReorder.setVisibility(View.VISIBLE);

            holder.btnReorder.setOnClickListener(v -> {
                order.status = "Pending";
                orderList.remove(position);
                notifyItemRemoved(position);

                Executors.newSingleThreadExecutor().execute(() -> {
                    try {
                        AppDatabase.getInstance(context).orderDao().updateOrder(order);
                        new Handler(Looper.getMainLooper()).post(() -> {
                            if (statusChangedListener != null) {
                                statusChangedListener.onOrderMarkedCancelled(); // يرجع لتبويب Active
                            }
                        });
                    } catch (Exception e) {
                        Log.e("OrdersAdapter", "Error during update (Reorder)", e);
                    }
                });

                Toast.makeText(context, "تم إعادة الطلب", Toast.LENGTH_SHORT).show();
            });

        } else {
            holder.btnComplete.setVisibility(View.GONE);
            holder.btnCancel.setVisibility(View.GONE);
            holder.btnReorder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, summary, total, date, statusText;
        Button btnComplete, btnCancel, btnReorder;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.orderImage);
            name = itemView.findViewById(R.id.orderName);
            summary = itemView.findViewById(R.id.orderSummary);
            total = itemView.findViewById(R.id.orderTotal);
            date = itemView.findViewById(R.id.orderDate);
            statusText = itemView.findViewById(R.id.orderStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            btnReorder = itemView.findViewById(R.id.btnReorder);
        }
    }
}







