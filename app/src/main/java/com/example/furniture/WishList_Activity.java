package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.example.furniture.Models.Cart;
import com.example.furniture.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WishList_Activity extends AppCompatActivity {

    RecyclerView cartList;
    RelativeLayout loading, emptyCart;
    private DatabaseReference reference, userRef;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        cartList = findViewById(R.id.cartList);
        loading = findViewById(R.id.loading);
        emptyCart = findViewById(R.id.emptyCart);
        user = FirebaseAuth.getInstance().getCurrentUser();

        cartList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        cartList.setLayoutManager(linearLayoutManager);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("User")
                .child(user.getUid())
                .child("Wishlist");

        reference.addValueEventListener(new ValueEventListener() {
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
                .setQuery(reference, Cart.class)
                .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull Cart model) {

                loading.setVisibility(View.GONE);
                cartList.setVisibility(View.VISIBLE);

                holder.itemName.setText(model.getName());
                holder.itemPrice.setText(model.getPrice());
                Glide.with(holder.itemImage).load(model.getImage()).into(holder.itemImage);

                holder.count.setVisibility(View.GONE);

                holder.shiftPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(WishList_Activity.this, ProductDetails.class);
                        intent.putExtra("productID", model.getID());
                        intent.putExtra("imagePath", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_order_item_layout, parent,false);
                CartViewHolder holer = new CartViewHolder(view);
                return holer;
            }
        };

        cartList.setAdapter(adapter);
        adapter.startListening();

    }
}