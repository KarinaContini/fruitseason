package com.example.fruitseason;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;


import androidx.appcompat.app.AppCompatActivity;

public class FruitsSale extends AppCompatActivity {
    ListView listView;

    SearchView searchFruits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_sale);
        listView = findViewById(R.id.listv);
        searchFruits = findViewById(R.id.searchView);
        String[] fruits = {"Pineapple", "Banana", "Strawberry"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.fruits_list_sale, R.id.txtFruits, fruits);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedItem = (String) adapterView.getItemAtPosition(position);
                Intent myIntent = new Intent(FruitsSale.this, FruitInfo.class);
                myIntent.putExtra("fruit", selectedItem);
                startActivity(myIntent);
            }
        } );
    }
}
