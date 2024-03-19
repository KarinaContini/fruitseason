package com.example.fruitseason;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FruitInfo extends AppCompatActivity {
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit_info);
        textView = findViewById(R.id.txtFruit);
        Intent intent = getIntent();
        String fruit = intent.getStringExtra("fruit");
        textView.setText(fruit);

    }
}
