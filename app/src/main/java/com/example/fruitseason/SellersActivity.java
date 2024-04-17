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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitseason.adapter.AdapterFruits;
import com.example.fruitseason.adapter.AdapterSellers;
import com.example.fruitseason.helper.RecyclerItemClickListener;
import com.example.fruitseason.model.Fruit;
import com.example.fruitseason.model.Model;
import com.example.fruitseason.model.SellerFruit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
    private TextView sellers, seasonalFruits, startingPage,selectedFruit;
    private Fruit fruitSelected;
    private String name, fruitIdSelected;
    private Query sellerQuery;
    SearchView searchFruits;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("info", "entrei na Seller");
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
        searchFruits = findViewById(R.id.txtSearchSeller);

        selectedFruit = findViewById(R.id.txtSelectedFruit);
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
            name = fruitSelected.getName();
            selectedFruit.setText(name);
            fruitIdSelected = fruitSelected.getFruitId();
        } else {
            Toast.makeText(SellersActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        Log.v("info", "antes da reference");
        sellersReference = FirebaseDatabase.getInstance().getReference("users");

        //sellerQuery = sellersReference.child("fruits").orderByChild("fruitId").equalTo("001");
        //sellerQuery = sellersReference.orderByChild("fruits/fruitId").equalTo(fruitId);
        Log.v("info", "criou a query");
        //teste();

        retrieveSelectedSellers();

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
    private void filterList(String newText) {
        List<Model>filteredList = new ArrayList<>();
        for (Model seller: sellersList){
            if(seller.getName().toLowerCase().contains(newText.toLowerCase())){
                filteredList.add(seller);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }else{
            adapterSellers.setFilteredList(filteredList);
        }
    }
    private void retrieveAllSellers(){

        sellersReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellersList.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    Log.i("Dados usuario: ", ds.getValue().toString() );
                    sellersList.add( ds.getValue(Model.class) );

                }

                adapterSellers.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void retrieveSelectedSellers(){

        sellersReference.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sellersList.clear();
                for ( DataSnapshot ds : snapshot.getChildren() ){
                    DataSnapshot fruitsSnapshot = ds.child("fruits");
                    for (DataSnapshot fruitSnapshot : fruitsSnapshot.getChildren()) {
                        String fruitId = fruitSnapshot.getKey();
                        if(fruitId.equals(fruitIdSelected)){
                            sellersList.add( ds.getValue(Model.class) );
                        }
                    }
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

    private void teste() {
        Log.v("info", "entrou no teste");
        //List<Model> users = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbUsers = database.getReference("users");
        Query q = dbUsers.orderByChild("name");
        Log.v("info", "criou a query");
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("info", "entrou no DataChange do metodo Teste");
                sellersList.clear();
                Log.v("info", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.v("info", "entrou no Child do snapshot");
                    Log.v("info", String.valueOf(postSnapshot.exists()));

                    Model user = postSnapshot.getValue(Model.class);
                    Log.v("info", "apos get value");
                    Log.v("info", String.valueOf(user.getFruits().size()));
                    if(user.getFruits().stream().filter(f -> "001".equals(f.getFruitId())).count() > 0){
                        Log.v("info", "pelo menos 1 seller com fruta 1");
                        sellersList.add(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
