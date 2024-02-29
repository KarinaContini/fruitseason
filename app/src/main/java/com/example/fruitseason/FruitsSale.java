package com.example.fruitseason;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class FruitsSale extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fruit_sale);
        listView = findViewById(R.id.listv);
        String[] fruits = {"Pineapple", "Banana", "Strawberry"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.fruits_list_sale, R.id.textFruits, fruits);
        listView.setAdapter(adapter);
    }
}
