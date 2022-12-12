package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.furniture.Fragment.CartFragment;
import com.example.furniture.Fragment.HomeFragment;
import com.example.furniture.Fragment.OrderFragment;
import com.example.furniture.Fragment.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    DatabaseReference reference;
    FirebaseUser user;
    public static final String MY_PREFS_NAME = "Dock";
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = FirebaseAuth.getInstance().getCurrentUser();

        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if (savedInstanceState == null){

            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            int idName = prefs.getInt("idName", 1);

            if (idName == 1){
                bottomNavigationView.setSelectedItemId(R.id.home);
                handleFrames(new HomeFragment() );
            }
            if (idName == 2){
                bottomNavigationView.setSelectedItemId(R.id.cart);
                handleFrames(new CartFragment() );
            }
            if (idName == 3){
                bottomNavigationView.setSelectedItemId(R.id.orders);
                handleFrames(new OrderFragment() );
            }
            if (idName == 4){
                bottomNavigationView.setSelectedItemId(R.id.setting);
                handleFrames(new SettingsFragment() );
            }
        }

        handleOnClickListner();
    }


    private void handleFrames(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_holder, fragment);
        fragmentTransaction.commit();
    }

    private void handleOnClickListner(){

        // ********** For colorful bottom nav icons ************
        // bottomNavigationView.setItemIconTintList(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        handleFrames(new HomeFragment());
                        editor.putInt("idName", 1);
                        editor.apply();
                        break;
                    case R.id.cart:
                        handleFrames(new CartFragment());
                        editor.putInt("idName", 2);
                        editor.apply();
                        break;
                    case R.id.orders:
                        handleFrames(new OrderFragment());
                        editor.putInt("idName", 3);
                        editor.apply();
                        break;
                    case R.id.setting:
                        handleFrames(new SettingsFragment());
                        editor.putInt("idName", 4);
                        editor.apply();
                        break;
                }
                return true;
            }
        });
    }
}