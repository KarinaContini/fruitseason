package com.example.fruitseason;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fruitseason.helper.Permissions;
import com.example.fruitseason.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration extends AppCompatActivity {

    private EditText emailtxt,passtxt,confirmPasstxt,fullnametxt,confirmEmailtxt,addresstxt,provincetxt,citytxt, phonetxt;
    private TextView buttonLogin;
    private FirebaseAuth mAuth;
    private static final String TAG= "Registration";
    DatabaseReference reference;
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
            Toast.makeText(this, "All the fields should be filled!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailtxt.setError("Please enter a valid email address");
            emailtxt.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(phone).matches()){
            phonetxt.setError("Please enter a valid phone number");
            phonetxt.requestFocus();
            return;
        }
        if(!email.equals(confirmEmail)){
            confirmEmailtxt.setError("Email and confirmation does not match");
            confirmEmailtxt.requestFocus();
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

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            Model model = new Model(name,phone,address,city,province, null);
                            FirebaseDatabase.getInstance().setPersistenceEnabled(true);

                            reference = FirebaseDatabase.getInstance().getReference("users");
                            reference.child(firebaseUser.getUid()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                    Toast.makeText(Registration.this,
                                            "User registered! ", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Registration.this,FruitsSale.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("address", address);
                                    intent.putExtra("city", city);
                                    intent.putExtra("province", province);
                                    startActivity(intent);
                                    finish();
                                    }else{
                                        Toast.makeText(Registration.this, "Failed registration", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{
                            try{
                                throw task.getException();
                            }catch(FirebaseAuthUserCollisionException e){
                                emailtxt.setError("User is already registered with this email.");
                                emailtxt.requestFocus();
                            }
                            catch(Exception e){
                                Log.e(TAG, e.getMessage());
                                Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }


}
