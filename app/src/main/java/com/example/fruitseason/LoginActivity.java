package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {


    private EditText emailtxt, passtxt;
    private Button signupbtn, loginbtn;

    public void loginUser() {
        String email = emailtxt.getText().toString().trim();
        String password = passtxt.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "No empty field", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}