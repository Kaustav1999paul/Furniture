package com.example.furniture.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.furniture.Models.Cart;
import com.example.furniture.R;
import com.example.furniture.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderFragment extends Fragment {


    private RecyclerView orderList;
    private DatabaseReference ordersRef;
    FirebaseUser user;
    RelativeLayout loading, emptyCart;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        orderList = view.findViewById(R.id.orderList);
        loading = view.findViewById(R.id.loading);
        emptyCart = view.findViewById(R.id.emptyCart);
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin View").child(user.getUid()).child("Products");

        orderList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        orderList.setLayoutManager(linearLayoutManager);

        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    emptyCart.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                }else{
                    emptyCart.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(ordersRef, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, OrderViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, int position, @NonNull Cart model) {

                String state = model.getState();
                String s0= "Ordered";
                String s1= "Shipped";
                String s2 = "Out for Delivery";
                String s3 = "Delivered";


                loading.setVisibility(View.GONE);
                orderList.setVisibility(View.VISIBLE);

                if (state.equals(s0) || state.equals(s1) || state.equals(s2) || state.equals(s3)){

                    int qty = Integer.parseInt(model.getQty());
                    int price = Integer.parseInt(model.getPrice());
                    int totalCost = price*qty;
                    Glide.with(holder.itemImage).load(model.getImage()).into(holder.itemImage);

                    holder.itemName.setText(model.getName());
                    holder.noOfItem.setText(String.valueOf(qty));
                    holder.itemPrice.setText("₹"+String.valueOf(totalCost));

                    holder.cancelOrder.setVisibility(View.GONE);
                    holder.returnItem.setVisibility(View.GONE);

                    if (state.equals(s0)){
                        holder.progressBar.setProgress(5);
                        holder.cancelOrder.setVisibility(View.VISIBLE);
                    }
                    if (state.equals(s1)){
                        holder.progressBar.setProgress(35);
                        holder.cancelOrder.setVisibility(View.VISIBLE);
                    }
                    if (state.equals(s2)){
                        holder.progressBar.setProgress(60);
                        holder.cancelOrder.setVisibility(View.VISIBLE);
                    }
                    if (state.equals(s3)){
                        holder.progressBar.setProgress(100);
                        holder.returnItem.setVisibility(View.VISIBLE);
                        holder.cancelOrder.setVisibility(View.GONE);
                    }


                    holder.cancelOrder.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            new AlertDialog.Builder(getContext())
                                    .setTitle("Cancel Order")
                                    .setMessage("Are you sure you want to cancel the order?")

                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            ordersRef.child(model.getID()).removeValue();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setIcon(R.mipmap.ic_launcher_round)
                                    .show();
                        }
                    });
                }else {
                    holder.parent.setVisibility(View.GONE);
                    ViewGroup.LayoutParams params = holder.parent.getLayoutParams();
                    params.height = 0;
                    params.width = 0;
                    holder.parent.setLayoutParams(params);
                }
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent,false);
                OrderViewHolder holder = new OrderViewHolder(view);
                return holder;
            }
        };


        orderList.setAdapter(adapter);
        adapter.startListening();






        return view;
    }
}