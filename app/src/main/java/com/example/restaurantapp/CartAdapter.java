package com.example.restaurantapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final List<CartItem> cartList;
    private final CartActionListener listener;

    public CartAdapter(Context context, List<CartItem> cartList, CartActionListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        holder.name.setText(item.name);
        holder.quantity.setText("الكمية: " + item.quantity);
        holder.price.setText("₪" + (item.price * item.quantity));
        Glide.with(context)
                .load(item.imageUrl)
                .circleCrop()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.image_error)
                .into(holder.image);
        holder.btnIncrease.setOnClickListener(v -> {
            listener.onQuantityChanged(item, item.quantity + 1);
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (item.quantity > 1) {
                listener.onQuantityChanged(item, item.quantity - 1);
            }
        });

        holder.btnDelete.setOnClickListener(v -> listener.onItemDeleted(item));
        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle("حذف العنصر")
                    .setMessage("هل أنت متأكد أنك تريد حذف هذا الطبق من السلة؟")
                    .setPositiveButton("نعم", (dialog, which) -> {
                        listener.onItemDeleted(item);
                    })
                    .setNegativeButton("إلغاء", null)
                    .show();
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name, quantity, price;
        Button btnIncrease, btnDecrease, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cartImage);
            name = itemView.findViewById(R.id.cartName);
            quantity = itemView.findViewById(R.id.cartQuantity);
            price = itemView.findViewById(R.id.cartPrice);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface CartActionListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemDeleted(CartItem item);
    }
}

