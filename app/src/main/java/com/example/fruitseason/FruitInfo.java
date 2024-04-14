package com.example.fruitseason;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fruitseason.model.Fruit;
import com.example.fruitseason.model.SellerFruit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class FruitInfo extends AppCompatActivity{
    TextView textView;
    ImageView imgView;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    TextView editProfile, logout, price;
    Button btnSave;

    private Fruit fruitSelected;
    private DatabaseReference sellerReference;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_info);
        drawerLayout = findViewById(R.id.drawer_layout);

        editProfile = findViewById(R.id.editProfile);
        logout = findViewById(R.id.logout);
        menu= findViewById(R.id.menu);
        textView = findViewById(R.id.txtFruit);
        imgView = findViewById(R.id.imgFruit);
        price = findViewById(R.id.txtPrice);
        btnSave = findViewById(R.id.btnSave);

        mAuth = FirebaseAuth.getInstance();

        fruitSelected = (Fruit) getIntent().getSerializableExtra("selectedFruit");

        if(fruitSelected != null){
            textView.setText(fruitSelected.getName());
            Picasso.get().load(fruitSelected.getImage()).into(imgView);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = price.getText().toString().trim();

                if (!value.isEmpty() && !value.equals("0")){

                    SellerFruit fruit = new SellerFruit();
                    fruit.setName(fruitSelected.getName());
                    fruit.setPrice(value);
                    fruit.setFruitId(fruitSelected.getFruitId());

                    FirebaseUser firebaseUser = mAuth.getCurrentUser();

                    sellerReference = FirebaseDatabase.getInstance().getReference("users");

                    sellerReference.child(firebaseUser.getUid()).child("fruits").child(fruitSelected.getFruitId()).setValue(fruit).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(FruitInfo.this,
                                        "Fruit details saved! ", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FruitInfo.this,FruitsSale.class);
                                startActivity(intent);
                                finish();
                            }else{
                                Toast.makeText(FruitInfo.this, "Failed registration", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(FruitInfo.this,"Price can't be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(FruitInfo.this, EditProfile.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FruitInfo.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public static void openDrawer(DrawerLayout drawerLayout){
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public static void closeDrawer(DrawerLayout drawerLayout){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public static void redirectActivity(Activity activity, Class secondActivity){
        Intent intent = new Intent(activity,secondActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }


}
