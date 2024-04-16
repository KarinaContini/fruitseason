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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fruitseason.adapter.AdapterFruits;
import com.example.fruitseason.helper.RecyclerItemClickListener;
import com.example.fruitseason.model.Fruit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeasonalFruits extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private RecyclerView fruitsList;
    private List<Fruit> fruits = new ArrayList<>();
    private AdapterFruits adapterFruits;
    private DatabaseReference fruitsReference;
    TextView sellers, startingPage, seasonalFruits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seasonal_fruits);
        drawerLayout = findViewById(R.id.drawer_layout_buyer);
        sellers = findViewById(R.id.sellers);
        startingPage = findViewById(R.id.home);
        seasonalFruits = findViewById(R.id.searchFruits);

        menu= findViewById(R.id.menu_buyer);
        fruitsList = findViewById(R.id.recyclerViewFruitsList);
        fruitsReference = FirebaseDatabase.getInstance().getReference("fruits");

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        seasonalFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer(drawerLayout);
            }
        });
        sellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SeasonalFruits.this, SellersActivity.class);
            }
        });
        startingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SeasonalFruits.this, MainActivity.class);
            }
        });
        //Configurar RecyclerView
        fruitsList.setLayoutManager(new LinearLayoutManager(this));
        fruitsList.setHasFixedSize(true);
        adapterFruits = new AdapterFruits(fruits, this);
        fruitsList.setAdapter( adapterFruits );

        retrieveFruits();

        fruitsList.addOnItemTouchListener(new RecyclerItemClickListener(this, fruitsList, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fruit selectedFruit = fruits.get(position);
                Intent intent = new Intent(SeasonalFruits.this, SellersActivity.class);
                intent.putExtra("selectedFruit", selectedFruit);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    private void retrieveFruits(){
        fruitsReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fruits.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    fruits.add( ds.getValue(Fruit.class) );
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
