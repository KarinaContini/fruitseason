package com.example.fruitseason.helper;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissions {
    public static boolean validatePermissions (String[] permissions, Activity activity, int requestCode){

        if (Build.VERSION.SDK_INT >= 23 && Build.VERSION.SDK_INT <= 32 ){

            List<String> permissionList = new ArrayList<>();

            for ( String permission : permissions ){
                Boolean hasPermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if ( !hasPermission ) permissionList.add(permission);
            }

            if ( permissionList.isEmpty() ) return true;
            String[] newPermissions = new String[ permissionList.size() ];
            permissionList.toArray( newPermissions );

            ActivityCompat.requestPermissions(activity, newPermissions, requestCode );

        }

        return true;

    }

}
