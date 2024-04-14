package com.example.fruitseason;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fruitseason.model.Fruit;
import com.example.fruitseason.model.Model;

import org.w3c.dom.Text;

public class SellerDetailsActivity extends AppCompatActivity {
    private TextView txtOpeningHours, txtAddress;
    private Model selectedSeller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_details);

        txtAddress = findViewById(R.id.txtAddressDetails);
        txtOpeningHours = findViewById(R.id.txtOpeningHours);

        selectedSeller = (Model) getIntent().getSerializableExtra("selectedSeller");

        if(selectedSeller != null){
            txtAddress.setText(selectedSeller.getAddress());

        }



    }
}