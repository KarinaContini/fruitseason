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
import com.example.fruitseason.adapter.AdapterSellers;
import com.example.fruitseason.helper.RecyclerItemClickListener;
import com.example.fruitseason.model.Fruit;
import com.example.fruitseason.model.Model;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SellersActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ImageView menu;
    private RecyclerView sellersRecyclerView;
    private List<Model> sellersList = new ArrayList<>();
    private AdapterSellers adapterSellers;
    private DatabaseReference sellersReference;
    TextView sellers, seasonalFruits, startingPage;
    private Fruit fruitSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers);
        drawerLayout = findViewById(R.id.drawer_layout_buyer);
        sellers = findViewById(R.id.sellers);
        seasonalFruits = findViewById(R.id.searchFruits);
        menu= findViewById(R.id.menu_buyer);
        seasonalFruits = findViewById(R.id.searchFruits);
        sellers = findViewById(R.id.sellers);
        startingPage = findViewById(R.id.home);
        sellersRecyclerView = findViewById(R.id.recyclerViewSellersList);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        seasonalFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SellersActivity.this, SeasonalFruits.class);
            }
        });
        sellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer(drawerLayout);
            }
        });
        startingPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(SellersActivity.this, MainActivity.class);
            }
        });

        //Configurar RecyclerView
        sellersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellersRecyclerView.setHasFixedSize(true);
        adapterSellers = new AdapterSellers(sellersList, this);
        sellersRecyclerView.setAdapter( adapterSellers );

        fruitSelected = (Fruit) getIntent().getSerializableExtra("selectedFruit");

        if(fruitSelected != null){

        }

        sellersReference = FirebaseDatabase.getInstance().getReference("users");

        retrieveSellers();

        sellersRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, sellersRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Model selectedSeller = sellersList.get(position);
                Intent intent = new Intent(SellersActivity.this, SellerMapsActivity.class);
                intent.putExtra("selectedSeller", selectedSeller);
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

    private void retrieveSellers(){
        sellersReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellersList.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    sellersList.add( ds.getValue(Model.class) );
                }

                adapterSellers.notifyDataSetChanged();
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
