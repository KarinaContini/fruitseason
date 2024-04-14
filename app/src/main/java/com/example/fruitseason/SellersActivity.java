package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

    private RecyclerView sellersRecyclerView;
    private List<Model> sellersList = new ArrayList<>();
    private AdapterSellers adapterSellers;
    private DatabaseReference sellersReference;
    TextView searchSeller;

    private Fruit fruitSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sellers);

        searchSeller = findViewById(R.id.txtSearchSeller);
        sellersRecyclerView = findViewById(R.id.recyclerViewSellersList);

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
                Intent intent = new Intent(SellersActivity.this, SellerDetailsActivity.class);
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

}