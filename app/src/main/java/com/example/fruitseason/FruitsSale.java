package com.example.fruitseason;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class FruitsSale extends AppCompatActivity {
    ListView listView;
    private DrawerLayout drawerLayout;
    private ImageView menu;
    TextView editProfile, logout;
    SearchView searchFruits;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_sale);
        listView = findViewById(R.id.listv);
        searchFruits = findViewById(R.id.searchView);
        drawerLayout = findViewById(R.id.drawer_layout);
        logout= findViewById(R.id.logout);
        editProfile = findViewById(R.id.editProfile);
        menu= findViewById(R.id.menu);

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
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectActivity(FruitsSale.this, PasswordChange.class);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FruitsSale.this, LoginActivity.class);
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
