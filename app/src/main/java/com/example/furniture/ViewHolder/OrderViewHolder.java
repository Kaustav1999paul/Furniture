package com.example.furniture.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.R;

public class OrderViewHolder extends RecyclerView.ViewHolder{

    public ImageView itemImage;
    public TextView itemName, itemPrice, noOfItem, cancel_state;
    public LinearLayout shiftPage, parent, ll;
    public ProgressBar progressBar;
    public TextView returnItem, cancelOrder;



    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);

        cancel_state = itemView.findViewById(R.id.cancel_state);
        ll = itemView.findViewById(R.id.ll);
        parent = itemView.findViewById(R.id.parent);
        returnItem = itemView.findViewById(R.id.returnItem);
        cancelOrder = itemView.findViewById(R.id.cancelOrder);
        itemImage = itemView.findViewById(R.id.itemImage);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        noOfItem = itemView.findViewById(R.id.noOfItem);
        shiftPage = itemView.findViewById(R.id.shiftPage);
        progressBar = itemView.findViewById(R.id.progressBar);
    }
}
