package com.example.furniture.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.furniture.Interface.ItmeClickListner;
import com.example.furniture.R;

public class BedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public ItmeClickListner listner;
    public ImageView itemImage;
    public TextView itemName, itemPrice;

    public BedViewHolder(@NonNull View itemView) {
        super(itemView);
        itemImage = itemView.findViewById(R.id.itemImage);
        itemName = itemView.findViewById(R.id.itemName);
        itemPrice = itemView.findViewById(R.id.itemPrice);
    }

    public void setItemClickListner(ItmeClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false );
    }
}
