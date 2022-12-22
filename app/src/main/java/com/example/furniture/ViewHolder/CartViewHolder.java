package com.example.furniture.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.Interface.ItmeClickListner;
import com.example.furniture.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ItmeClickListner listner;
    public ImageView itemImage;
    public TextView itemName, itemPrice, noOfItem;
    public ImageView substract, add;
    public LinearLayout shiftPage;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.itemImage);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
        noOfItem = itemView.findViewById(R.id.noOfItem);
        substract = itemView.findViewById(R.id.substract);
        shiftPage = itemView.findViewById(R.id.shiftPage);
        add = itemView.findViewById(R.id.add);
    }

    public void setItemClickListner(ItmeClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false );
    }
}
