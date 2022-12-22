package com.example.furniture.Fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Models.Cart;
import com.example.furniture.ProductDetails;
import com.example.furniture.R;
import com.example.furniture.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class CartFragment extends Fragment {


    RecyclerView cartList;
    RelativeLayout loading, emptyCart;
    private DatabaseReference reference;
    FirebaseUser user;
    FloatingActionButton proceed;
    ArrayList<Integer> list = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartList = view.findViewById(R.id.cartList);
        loading = view.findViewById(R.id.loading);
        emptyCart = view.findViewById(R.id.emptyCart);
        proceed = view.findViewById(R.id.proceed);

        cartList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        cartList.setLayoutManager(linearLayoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid()).child("Cart");

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

                int qty = Integer.parseInt(model.getQty());
                int price = Integer.parseInt(model.getPrice());
                int totalCost = price*qty;
                Glide.with(holder.itemImage).load(model.getImage()).into(holder.itemImage);

                list.add(totalCost);

                holder.itemName.setText(model.getName());
                holder.noOfItem.setText(String.valueOf(qty));
                holder.itemPrice.setText("₹"+String.valueOf(totalCost));

                holder.shiftPage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ProductDetails.class);
                        intent.putExtra("productID", model.getID());
                        intent.putExtra("imagePath", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        startActivity(intent);
                    }
                });

                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int qtyNew = Integer.parseInt(model.getQty());
                        qtyNew += 1;
                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                        Date date = new Date();
                        String d = formatter.format(date);
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("ID", model.getID());
                        map.put("Date", d.toString());
                        map.put("image", model.getImage());
                        map.put("name", model.getName());
                        map.put("price", String.valueOf(model.getPrice()));
                        map.put("Qty", String.valueOf(qtyNew));
                        map.put("category", model.getCategory());
                        map.put("cartID", model.getCartID());

                        reference.child(model.getCartID()).setValue(map);
                    }
                });

                holder.substract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int qtyNew = Integer.parseInt(model.getQty());

                        if(qtyNew == 1){
//                            Delete item from Cart
                            reference.child(model.getCartID()).removeValue();
                        }else{
//                            Reduce Quantity by 1
                            qtyNew -= 1;
                            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                            Date date = new Date();
                            String d = formatter.format(date);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("ID", model.getID());
                            map.put("Date", d.toString());
                            map.put("image", model.getImage());
                            map.put("name", model.getName());
                            map.put("price", String.valueOf(model.getPrice()));
                            map.put("Qty", String.valueOf(qtyNew));
                            map.put("category", model.getCategory());
                            map.put("cartID", model.getCartID());

                            reference.child(model.getCartID()).setValue(map);
                        }
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



        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long finalAmount = 0;
                for(long i : list){
                    finalAmount += i;
                }

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setTitle("Final Amount");
                alert.setMessage(String.valueOf(finalAmount));
                alert.setIcon(R.mipmap.ic_launcher_round);

                AlertDialog al = alert.create();
                al.show();
            }
        });

        return view;
    }
}