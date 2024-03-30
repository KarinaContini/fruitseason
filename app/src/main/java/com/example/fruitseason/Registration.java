package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private EditText emailtxt,passtxt,confirmPasstxt,fullnametxt,confirmEmailtxt,addressText,provinceText,cityText;
    private FirebaseAuth mAuth;
    Button buttonReg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        buttonReg = findViewById(R.id.btnRegister);
        emailtxt = findViewById(R.id.txtEmail);
        passtxt= findViewById(R.id.txtPassword);
        confirmPasstxt = findViewById(R.id.txtConfirmPassword);
        fullnametxt = findViewById(R.id.txtName);
        confirmEmailtxt = findViewById(R.id.txtConfirmEmail);
        addressText = findViewById(R.id.txtAddress);
        provinceText = findViewById(R.id.txtProvince);
        cityText = findViewById(R.id.txtCity);

        mAuth = FirebaseAuth.getInstance();
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, FruitsSale.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(){
        String name  = fullnametxt.getText().toString().trim();
        String email = emailtxt.getText().toString().trim();
        String password = passtxt.getText().toString().trim();
        String confirmPassword = confirmPasstxt.getText().toString().trim();
        String age = confirmEmailtxt.getText().toString().trim();


        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || age.isEmpty()){
            Toast.makeText(this, "All the field should be filled!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<8){
            passtxt.setError("Password should be more than 8 char");
            passtxt.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            confirmPasstxt.setError("Password and confirmation is not match");
            confirmPasstxt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtxt.setError("Please Put the valid Email address");
            emailtxt.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Registration.this,
                                    "Registered! ", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Registration.this,MainActivity.class);
                            intent.putExtra("name",name);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Registration.this, "Failed registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
