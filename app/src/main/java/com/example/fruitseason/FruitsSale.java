package com.example.fruitseason;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fruitseason.adapter.AdapterFruitsForSale;
import com.example.fruitseason.helper.RecyclerItemClickListener;
import com.example.fruitseason.model.Fruit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FruitsSale extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView menu;
    private RecyclerView fruitsRecyclerView;
    private List<Fruit> fruitNamesList = new ArrayList<>();
    private AdapterFruitsForSale adapterFruits;
    private DatabaseReference fruitsReference;
    TextView editProfile, logout, myFruits, addFruits;
    SearchView searchFruits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_sale);

        searchFruits = findViewById(R.id.searchView);
        searchFruits.clearFocus();
        searchFruits.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });
        drawerLayout = findViewById(R.id.drawer_layout);
        logout= findViewById(R.id.logout);
        editProfile = findViewById(R.id.editProfile);
        menu= findViewById(R.id.menu);
        myFruits = findViewById(R.id.myFruits);
        addFruits = findViewById(R.id.addFruits);

        fruitsRecyclerView = findViewById(R.id.recyclerViewFruitsForSale);

        fruitsReference = FirebaseDatabase.getInstance().getReference("fruits");

        //Configurar RecyclerView
        fruitsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        fruitsRecyclerView.setHasFixedSize(true);
        adapterFruits = new AdapterFruitsForSale(fruitNamesList, this);
        fruitsRecyclerView.setAdapter( adapterFruits );

        // Retrieve data from Firebase and populate the ListView
        retrieveFruits();


        fruitsRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, fruitsRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Fruit selectedFruit = fruitNamesList.get(position);
                Intent intent = new Intent(FruitsSale.this, FruitInfo.class);
                intent.putExtra("selectedFruit", selectedFruit);
                intent.putExtra("parentActivity", 1);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        myFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FruitsSale.this, MyFruitsActivity.class);
                startActivity(intent);
            }
        });
        addFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeDrawer(drawerLayout);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FruitsSale.this, EditProfile.class);
                startActivity(intent);
                finish();
            }
        });
        myFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FruitsSale.this, MyFruitsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FruitsSale.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void filterList(String newText) {
        List<Fruit>filteredList = new ArrayList<>();
        for (Fruit fruit: fruitNamesList){
            if(fruit.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(fruit);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapterFruits.setFilteredList(filteredList);
        }
    }

    private void retrieveFruits(){
        fruitsReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fruitNamesList.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    fruitNamesList.add( ds.getValue(Fruit.class) );
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
    @Override
    protected void onPause(){
        super.onPause();
        closeDrawer(drawerLayout);
    }


}
