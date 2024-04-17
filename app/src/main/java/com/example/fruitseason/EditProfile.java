package com.example.fruitseason;

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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fruitseason.model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfile extends AppCompatActivity {

    EditText editName, editPhone, editAddress, editCity, editProvince;
    Button cancel, save;
    String name, phone, address, city, province, profilePicture;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    TextView editProfilePicture;
    private CircleImageView circleImageViewProfile;

    StorageReference storage;

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editCity = findViewById(R.id.editCity);
        editProvince = findViewById(R.id.editProvince);
        editProfilePicture = findViewById(R.id.txtChangePhoto);
        circleImageViewProfile = findViewById(R.id.circleImageView_editImgProfile);

        cancel = findViewById(R.id.cancelEditButton);
        save = findViewById(R.id.editProfileButton);
        mAuth = FirebaseAuth.getInstance();
        //reference = FirebaseDatabase.getInstance().getReference("users");

        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        retrieveSeller();
        editName.setText(name);
        editPhone.setText(phone);
        editAddress.setText(address);
        editCity.setText(city);
        editProvince.setText(province);

        //Uri url = firebaseUser.getPhotoUrl();

        if ( profilePicture != null ){
            Picasso.get().load(profilePicture).into( circleImageViewProfile );
        }else {
            circleImageViewProfile.setImageResource(R.drawable.pattern);
        }

        editProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                if ( i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, 200 );
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfile.this, FruitsSale.class);
                startActivity(intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });

    }

    private void retrieveSeller(){

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for ( DataSnapshot ds : snapshot.getChildren() ){
                    name = ds.child("name").getValue(String.class);
                    phone = ds.child("phone").getValue(String.class);
                    address = ds.child("address").getValue(String.class);
                    city = ds.child("city").getValue(String.class);
                    province = ds.child("province").getValue(String.class);
                    profilePicture = ds.child("image").getValue(String.class);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void updateProfile(FirebaseUser firebaseUser) {
        name  = editName.getText().toString().trim();
        phone = editPhone.getText().toString().trim();
        address = editAddress.getText().toString().trim();
        city = editCity.getText().toString().trim();
        province = editProvince.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || city.isEmpty()
                || province.isEmpty()){
            Toast.makeText(this, "All the fields should be filled!", Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.PHONE.matcher(phone).matches()){
            editPhone.setError("Please enter a valid phone number");
            editPhone.requestFocus();
        } else{

            Model model = new Model(name,phone,address,city,province,profilePicture);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            String userId= firebaseUser.getUid();
            reference.child(userId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(EditProfile.this, "Update Successful!", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditProfile.this, FruitsSale.class);
                        startActivity(intent);
                    } else{
                        try{
                            throw Objects.requireNonNull(task.getException());
                        }catch(Exception e){
                            Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK ){
            Bitmap image = null;
            try {
                Uri locationSelectedImage = data.getData();
                image = MediaStore.Images.Media.getBitmap(getContentResolver(), locationSelectedImage );

                if ( image != null ) {
                    circleImageViewProfile.setImageBitmap(image);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 80, baos );
                    byte[] imageData = baos.toByteArray();

                    String fileName = UUID.randomUUID().toString();
                    StorageReference imageRef = storage.child("sellerImages").child(fileName + ".jpeg");

                    UploadTask uploadTask = imageRef.putBytes( imageData );

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EditProfile.this,"Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(EditProfile.this,"Success uploading image", Toast.LENGTH_SHORT).show();
                            imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    //updatePhotoUser( url );
                                    profilePicture = url.toString();
                                }
                            });
                        }
                    });


                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updatePhotoUser(Uri url){
        try {

            FirebaseUser user = mAuth.getCurrentUser();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri( url )
                    .build();

            user.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Profile", "Error updating profile picture.");
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for( int permissionResult : grantResults ){
            if( permissionResult == PackageManager.PERMISSION_DENIED){
                permissionValidationAlert();
            }
        }
    }

    private void permissionValidationAlert(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permission Denied");
        builder.setMessage("To use the app, you need to accept the permissions");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}