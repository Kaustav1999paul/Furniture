package com.example.furniture.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.furniture.Models.Cart;
import com.example.furniture.OrderConfirmedActivity;
import com.example.furniture.ProductDetails;
import com.example.furniture.R;
import com.example.furniture.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CartFragment extends Fragment {


    RecyclerView cartList;
    RelativeLayout loading, emptyCart;
    private DatabaseReference reference,reference1, orderRef, userRef;
    FirebaseUser user;
    FloatingActionButton proceed;
    long finalAmount = 0;
    BottomSheetDialog confirmDialog;

    public CartFragment() {

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

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(user.getUid())
                .child("Products");

        reference1 = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("Admin View")
                .child(user.getUid())
                .child("Products");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    emptyCart.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    proceed.setVisibility(View.VISIBLE);
                }else{
                    emptyCart.setVisibility(View.VISIBLE);
                    loading.setVisibility(View.GONE);
                    proceed.setVisibility(View.INVISIBLE);
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

                finalAmount += totalCost;

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
                        map.put("state", "inCart");

                        reference.child(model.getID()).setValue(map);
                        reference1.child(model.getID()).setValue(map);
                    }
                });

                holder.substract.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int qtyNew = Integer.parseInt(model.getQty());

                        if(qtyNew == 1){
//                            Delete item from Cart
                            reference.child(model.getID()).removeValue();
                            reference1.child(model.getID()).removeValue();
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
                            map.put("state", "inCart");

                            reference.child(model.getID()).setValue(map);
                            reference1.child(model.getID()).setValue(map);
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
                showDialog();
            }
        });

        return view;
    }

    private void showDialog() {
//        Snackbar snackbar = Snackbar.make(getView(), "Cart should not be empty to proceed further.",Snackbar.LENGTH_LONG);
//        snackbar.show();

        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.confirm_order_dialog, null);
        confirmDialog = new BottomSheetDialog(getContext());

        TextView cartTotal = sheetView.findViewById(R.id.cartTotal);
        cartTotal.setText("Cart Total: "+String.valueOf(finalAmount));

        TextView totalAmount = sheetView.findViewById(R.id.totalAmount);
        totalAmount.setText("Final Amount: "+String.valueOf(finalAmount+150));

        confirmDialog.setContentView(sheetView);
        confirmDialog.show();

        FloatingActionButton placeOrder = sheetView.findViewById(R.id.placeOrder);
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmOrder(confirmDialog);
            }
        });
    }

    private void confirmOrder(BottomSheetDialog finalAmount) {
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(user.getUid());
        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(user.getUid());

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        String date = currentDate.format(calForDate.getTime());
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        String time = currentTime.format(calForDate.getTime());

        String randomKey = date+time+user.getUid();

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child("Name").getValue().toString();
                    String Email = snapshot.child("Email").getValue().toString();
                    String Phone = snapshot.child("PhoneNo").getValue().toString();

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("Name",name);
                    map.put("Email",Email);
                    map.put("PhoneNo", Phone);
                    map.put("state", "not shipped");
                    map.put("date", date);
                    map.put("time", time);
                    map.put("address", "3B/5 Sepco");
                    map.put("id", randomKey);

                    orderRef.child(randomKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                FirebaseDatabase.getInstance().getReference().child("Cart List").child("User View")
                                        .child(user.getUid()).removeValue()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
//                                                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
//                                                        @Override
//                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                            for (DataSnapshot sn : snapshot.getChildren()){
//                                                                sn.child("state")
//                                                            }
//                                                        }
//
//                                                        @Override
//                                                        public void onCancelled(@NonNull DatabaseError error) {
//
//                                                        }
//                                                    });


                                                    ValueEventListener eventListener = new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            String trueValue = "Ordered";
                                                            for(DataSnapshot ds : dataSnapshot.getChildren()) {
                                                                ds.child("state").getRef().setValue(trueValue);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError error) {
                                                            Log.d("TAG", error.getMessage()); //Never ignore potential errors!
                                                        }
                                                    };
                                                    reference1.addListenerForSingleValueEvent(eventListener);


                                                    HashMap<String, Object> om = new HashMap<>();
                                                    om.put("Name",name );
                                                    om.put("phone", Phone);
                                                    om.put("state", randomKey);
                                                    orderRef.updateChildren(om);
                                                    confirmDialog.dismiss();
                                                    Intent intent = new Intent(getContext(), OrderConfirmedActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }
                    });


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}