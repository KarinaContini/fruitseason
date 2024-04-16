package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.fruitseason.adapter.AdapterMyFruits;
import com.example.fruitseason.helper.RecyclerItemClickListener;
import com.example.fruitseason.model.SellerFruit;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyFruitsActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menu;
    TextView editProfile, logout, myFruits, addFruits;
    SearchView searchFruits;
    private RecyclerView myFruitsRecyclerView;
    private List<SellerFruit> fruitNamesList = new ArrayList<>();
    private AdapterMyFruits adapterFruits;
    private DatabaseReference sellerReference;
    private FirebaseAuth mAuth;
    Button btnAddFruit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fruits);
        searchFruits = findViewById(R.id.searchView);
        myFruits = findViewById(R.id.myFruits);
        addFruits = findViewById(R.id.addFruits);
        drawerLayout = findViewById(R.id.drawer_layout);
        logout= findViewById(R.id.logout);
        editProfile = findViewById(R.id.editProfile);
        menu= findViewById(R.id.menu);
        btnAddFruit = findViewById(R.id.btnAddFruit);

        myFruitsRecyclerView = findViewById(R.id.recyclerViewMyFruitsList);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        sellerReference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid()).child("fruits");

        //Configurar RecyclerView
        myFruitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myFruitsRecyclerView.setHasFixedSize(true);
        adapterFruits = new AdapterMyFruits(fruitNamesList, this);
        myFruitsRecyclerView.setAdapter( adapterFruits );

        // Retrieve data from Firebase and populate the ListView
        retrieveFruits();

        myFruitsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, myFruitsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SellerFruit selectedFruit = fruitNamesList.get(position);
                Intent intent = new Intent(MyFruitsActivity.this, FruitInfo.class);
                intent.putExtra("selectedFruit", selectedFruit);
                intent.putExtra("parentActivity", 2);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

        btnAddFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFruitsActivity.this, FruitsSale.class);
                startActivity(intent);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        myFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer(drawerLayout);
            }
        });
        addFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFruitsActivity.this, FruitsSale.class);
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFruitsActivity.this, EditProfile.class);
                startActivity(intent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFruitsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void retrieveFruits(){
        sellerReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fruitNamesList.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    fruitNamesList.add( ds.getValue(SellerFruit.class) );
                }
                //Collections.reverse( fruits );
                adapterFruits.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        activity.startActivity(intent);
        activity.finish();
    }
    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }


}
