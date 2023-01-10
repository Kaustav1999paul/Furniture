package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.furniture.Models.Bed;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductDetails extends AppCompatActivity {

    ImageView back,itemImage;
    Intent intent;
    String productID, imagePath, category;
    TextView itemPrice, itemName, details;
    DatabaseReference reference, cartRef;
    FloatingActionButton itemWishList, itemAddCart;
    FirebaseUser firebaseUser;
    int isListed = 0;
    int cartExist = 0;
    Bed product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        intent = getIntent();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        productID = intent.getStringExtra("productID");
        imagePath = intent.getStringExtra("imagePath");
        category = intent.getStringExtra("category");
        back = findViewById(R.id.back);
        itemName = findViewById(R.id.itemName);
        details = findViewById(R.id.details);
        itemAddCart = findViewById(R.id.itemAddCart);
        itemWishList = findViewById(R.id.itemWishList);
        itemPrice = findViewById(R.id.itemPrice);
        itemImage = findViewById(R.id.itemImage);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Glide.with(itemImage).load(imagePath).into(itemImage);

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Product").child(category);
        reference = FirebaseDatabase.getInstance().getReference().child("User").child(firebaseUser.getUid());
        cartRef = FirebaseDatabase.getInstance().getReference()
                .child("Cart List")
                .child("User View")
                .child(firebaseUser.getUid())
                .child("Products");

        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    product = snapshot.getValue(Bed.class);
                    itemName.setText(product.getName());
                    itemPrice.setText("₹ "+String.valueOf(product.getPrice()));
                    details.setText(product.getDescription());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



//        Wishlist Button State Handling
        reference.child("Wishlist").orderByChild("ID").equalTo(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    itemWishList.setImageResource(R.drawable.ic_baseline_favorite_24);
                    isListed = 1;
                }else{
                    itemWishList.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    isListed = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Cart Button State Handling
        cartRef.orderByChild("ID").equalTo(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    itemAddCart.setImageResource(R.drawable.item_added);
                    cartExist = 1;
                }else{
                    itemAddCart.setImageResource(R.drawable.ic_round_add_shopping_cart_24);
                    cartExist = 0;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




//        WishList Click
        itemWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isListed == 0){
//                    If item not exist Add item to wishList
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();
                    String d = formatter.format(date);

                    String listID = firebaseUser.getUid()+productID;

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ID", productID);
                    map.put("Date", d.toString());
                    map.put("image", product.getImage());
                    map.put("name", product.getName());
                    map.put("price", String.valueOf(product.getPrice()));
                    map.put("Qty", "1");
                    map.put("category", category);
                    map.put("cartID", listID);
                    map.put("state", "inCart");


                    reference.child("Wishlist").child(listID).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemWishList.setImageResource(R.drawable.ic_baseline_favorite_24);
                            }
                        }
                    });
                }else{
//                    If item not exist Remove item from WishList
                    reference.child("Wishlist").orderByChild("ID").equalTo(productID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot data: snapshot.getChildren()) {
                                data.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

//        CartButton Click
        itemAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cartExist == 0){

//                    Add item to Cart
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                    Date date = new Date();
                    String d = formatter.format(date);
                    String idD = formatter1.format(date);
                    String listID = firebaseUser.getUid()+productID+ idD;

                    final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ID", productID);
                    map.put("Date", d.toString());
                    map.put("image", product.getImage());
                    map.put("name", product.getName());
                    map.put("price", String.valueOf(product.getPrice()));
                    map.put("Qty", "1");
                    map.put("category", category);
                    map.put("cartID", listID);
                    map.put("state", "inCart");


                    cartListRef.child("User View")
                            .child(firebaseUser.getUid())
                            .child("Products")
                            .child(productID).updateChildren(map)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        cartListRef.child("Admin View")
                                                .child(firebaseUser.getUid()).child("Products")
                                                .child(productID).updateChildren(map)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        reference.child("Cart").child(listID).setValue(map);
                                                        itemAddCart.setImageResource(R.drawable.item_added);
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });

    }
}