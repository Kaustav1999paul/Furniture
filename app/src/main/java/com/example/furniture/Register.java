package com.example.furniture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    ImageView back;
    TextView login;
    EditText name, phone, email, password;
    Button createAccount;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        back = findViewById(R.id.back);
        login = findViewById(R.id.login);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        createAccount = findViewById(R.id.createAccount);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = name.getText().toString().trim();
                String e = email.getText().toString().trim();
                String p = password.getText().toString().trim();
                String ph = phone.getText().toString().trim();


                if (n.isEmpty() || e.isEmpty()|| p.isEmpty() || ph.isEmpty()){
                    Toast.makeText(Register.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }else {
                    if (!e.contains("@") || !e.contains(".com")){
                        Toast.makeText(Register.this, "Not a valid emailID", Toast.LENGTH_SHORT).show();
                    } else if (p.length() <= 5){
                        Toast.makeText(Register.this, "Password too short", Toast.LENGTH_SHORT).show();
                    }else{
                        registerUser(n , e, p , ph);
                    }
                }
            }
        });

    }

    private void registerUser(String name, String email, String passwd, String phNo) {
        auth.createUserWithEmailAndPassword(email, passwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    reference = FirebaseDatabase.getInstance().getReference("User");
                    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    HashMap<String, Object> map = new HashMap<String, Object>();
                    map.put("id",firebaseUser.getUid());
                    map.put("Name",name);
                    map.put("Email",email);
                    map.put("PhoneNo", phNo);

                    reference.child(firebaseUser.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(Register.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}