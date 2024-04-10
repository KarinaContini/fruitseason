package com.example.fruitseason;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.concurrent.ExecutionException;

public class PasswordChange extends AppCompatActivity {

    private Button buttonReset, cancel;
    private EditText emailRegistered;
    private FirebaseAuth mAuth;
    private final static String TAG = "PasswordChange";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_password_change);
        emailRegistered = findViewById(R.id.emailChangePass);

        buttonReset = findViewById(R.id.resetPassword);
        cancel = findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordChange.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailRegistered.getText().toString();
                if (email.isEmpty()) {
                    Toast.makeText(PasswordChange.this, "Please enter registered email", Toast.LENGTH_SHORT).show();
                    emailRegistered.setError("Email is required");
                    emailRegistered.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(PasswordChange.this, "Please enter valid email", Toast.LENGTH_SHORT).show();
                    emailRegistered.setError("Valid email is required");
                    emailRegistered.requestFocus();
                } else {
                    resetPassword(email);
                }
            }
        });
    }

    private void resetPassword(String email) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                if (task.isSuccessful()) {
                    SignInMethodQueryResult result = task.getResult();
                    if (result != null && result.getSignInMethods() != null && !result.getSignInMethods().isEmpty()) {
                        sendPasswordResetEmail(email);
                    } else {
                        Toast.makeText(PasswordChange.this, "User does not exist or is no longer valid. Please register again.", Toast.LENGTH_SHORT).show();
                        emailRegistered.setError("User does not exist or is no longer valid. Please register again.");
                    }
                } else {
                    Log.e(TAG, task.getException().getMessage());
                    Toast.makeText(PasswordChange.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PasswordChange.this, "Please check your inbox for password reset link", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PasswordChange.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e(TAG, task.getException().getMessage());
                    Toast.makeText(PasswordChange.this, "Please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}