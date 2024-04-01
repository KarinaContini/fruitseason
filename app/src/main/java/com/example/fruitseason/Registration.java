package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {

    private EditText emailtxt,passtxt,confirmPasstxt,fullnametxt,confirmEmailtxt,addresstxt,provincetxt,citytxt, phonetxt;
    private TextView buttonLogin;
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
        addresstxt = findViewById(R.id.txtAddress);
        provincetxt = findViewById(R.id.txtProvince);
        citytxt = findViewById(R.id.txtCity);
        phonetxt = findViewById(R.id.txtPhone);
        buttonLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();
        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void registerUser(){
        String name  = fullnametxt.getText().toString().trim();
        String email = emailtxt.getText().toString().trim();
        String password = passtxt.getText().toString().trim();
        String confirmPassword = confirmPasstxt.getText().toString().trim();
        String confirmEmail = confirmEmailtxt.getText().toString().trim();
        String phone = phonetxt.getText().toString().trim();
        String address = addresstxt.getText().toString().trim();
        String city = citytxt.getText().toString().trim();
        String province = provincetxt.getText().toString().trim();



        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || confirmEmail.isEmpty() || phone.isEmpty() ||address.isEmpty()||city.isEmpty()||province.isEmpty() ){
            Toast.makeText(this, "All the fields should be filled!!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<8){
            passtxt.setError("Password should be more than 8 char");
            passtxt.requestFocus();
            return;
        }
        if(!password.equals(confirmPassword)){
            confirmPasstxt.setError("Password and confirmation does not match");
            confirmPasstxt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtxt.setError("Please enter a valid email address");
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
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(Registration.this, "Failed registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
