package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SeasonalFruits extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private RecyclerView fruitsList;
    private List<Fruit> fruits = new ArrayList<>();
    private List<Fruit> startFruits = new ArrayList<>();
    private List<Fruit> endFruits = new ArrayList<>();
    private AdapterFruits adapterFruits;
    private DatabaseReference fruitsReference;
    TextView sellers, startingPage, seasonalFruits;
    private Query startingMonth, endingMonth;

    private double currentMonth;

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



        // Get the current month
        /* //Using java.time package (for API level 26 and higher):
        LocalDate currentDate = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            currentDate = LocalDate.now();
        }
        Month currentMonth = currentDate.getMonth(); // Enum representing the month
        int currentMonthValue = currentDate.getMonthValue(); // Month value (1 = January)*/

        Calendar calendar = Calendar.getInstance();
        currentMonth = (double) calendar.get(Calendar.MONTH); // Month is zero-based (0 = January)

        startingMonth = fruitsReference.orderByChild("start").endAt(currentMonth);

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

        //.orderByChild("name")
        startingMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fruits.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    double end =  ds.child("end").getValue(Double.class);
                    if( end >= currentMonth){
                        fruits.add( ds.getValue(Fruit.class) );
                    }

                }

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


/*    private void retrieveFruits(){

        //.orderByChild("name")
        fruitsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fruits.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    fruits.add( ds.getValue(Fruit.class) );
                }

                startingMonth = fruitsReference.orderByChild("start").endAt(currentMonth);


                adapterFruits.notifyDataSetChanged();

                        }

@Override
public void onCancelled(@NonNull DatabaseError error) {

        }
        });
        }*/