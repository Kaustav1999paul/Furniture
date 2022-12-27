package com.example.furniture.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.furniture.Models.Bed;
import com.example.furniture.ProductDetails;
import com.example.furniture.R;
import com.example.furniture.SliderAdapter;
import com.example.furniture.SliderItem;
import com.example.furniture.ViewHolder.BedViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    public RecyclerView bedList, sofaList, tableList;
    private ViewPager2 viewPager2;
    private Handler handler = new Handler();
    private DatabaseReference Ref;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        carosoleSettings(view);
        bedList = view.findViewById(R.id.bedList);
        sofaList = view.findViewById(R.id.sofaList);
        tableList = view.findViewById(R.id.tableList);







        Ref = FirebaseDatabase.getInstance().getReference().child("Product");

        beds();
        sofa();
        table();

        return view;
    }

    private void beds(){

        bedList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        bedList.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<Bed> options = new FirebaseRecyclerOptions.Builder<Bed>()
                .setQuery(Ref.child("Bed"), Bed.class)
                .build();

        FirebaseRecyclerAdapter<Bed, BedViewHolder> adapter = new FirebaseRecyclerAdapter<Bed, BedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BedViewHolder holder, int position, @NonNull Bed model) {
                String price = String.valueOf(model.getPrice());
                holder.itemPrice.setText("₹"+price);
                holder.itemName.setText(model.getName());
                Glide.with(holder.itemImage).load(model.getImage()).into(holder.itemImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ProductDetails.class);
                        intent.putExtra("productID", model.getID());
                        intent.putExtra("imagePath", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public BedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
                BedViewHolder holder = new BedViewHolder(view);
                return holder;
            }
        };
        bedList.setAdapter(adapter);
        adapter.startListening();
    }

    private void sofa(){

        sofaList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        sofaList.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<Bed> options = new FirebaseRecyclerOptions.Builder<Bed>()
                .setQuery(Ref.child("Sofa"), Bed.class)
                .build();

        FirebaseRecyclerAdapter<Bed, BedViewHolder> adapter = new FirebaseRecyclerAdapter<Bed, BedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BedViewHolder holder, int position, @NonNull Bed model) {
                String price = String.valueOf(model.getPrice());
                holder.itemPrice.setText("₹"+price);
                holder.itemName.setText(model.getName());
                Glide.with(holder.itemImage).load(model.getImage()).into(holder.itemImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ProductDetails.class);
                        intent.putExtra("productID", model.getID());
                        intent.putExtra("imagePath", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public BedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
                BedViewHolder holder = new BedViewHolder(view);
                return holder;
            }
        };
        sofaList.setAdapter(adapter);
        adapter.startListening();
    }

    private void table(){

        tableList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        tableList.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<Bed> options = new FirebaseRecyclerOptions.Builder<Bed>()
                .setQuery(Ref.child("Tables"), Bed.class)
                .build();

        FirebaseRecyclerAdapter<Bed, BedViewHolder> adapter = new FirebaseRecyclerAdapter<Bed, BedViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BedViewHolder holder, int position, @NonNull Bed model) {
                String price = String.valueOf(model.getPrice());
                holder.itemPrice.setText("₹"+price);
                holder.itemName.setText(model.getName());
                Glide.with(holder.itemImage).load(model.getImage()).into(holder.itemImage);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ProductDetails.class);
                        intent.putExtra("productID", model.getID());
                        intent.putExtra("imagePath", model.getImage());
                        intent.putExtra("category", model.getCategory());
                        startActivity(intent);
                    }
                });
            }

            @NonNull
            @Override
            public BedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent,false);
                BedViewHolder holder = new BedViewHolder(view);
                return holder;
            }
        };
        tableList.setAdapter(adapter);
        adapter.startListening();
    }


    private void carosoleSettings(View view) {
        viewPager2 = view.findViewById(R.id.viewPager2);
        List<SliderItem> list = new ArrayList<>();
        list.add(new SliderItem(R.drawable.banner1, "30% off", "This diwali get min 30% off"));
        list.add(new SliderItem(R.drawable.banner2, "New Collections", "Get all the new Collections at your home"));
        list.add(new SliderItem(R.drawable.banner3, "Happy New Year", "This year bring new things to your home."));

        viewPager2.setAdapter(new SliderAdapter(list, viewPager2));
        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + 0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                handler.removeCallbacks(slideRunnable);
                handler.postDelayed(slideRunnable, 3000);
            }
        });
    }

    private Runnable slideRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(slideRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(slideRunnable, 3000);
    }
}