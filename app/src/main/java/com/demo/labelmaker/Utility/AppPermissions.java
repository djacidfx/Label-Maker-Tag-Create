package com.demo.labelmaker.Utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;


public class AppPermissions {
    Context context;

    public AppPermissions(Context context) {
        this.context = context;
    }

    public void checkForReadPermission() {
        if (Build.VERSION.SDK_INT < 23 || this.context.checkSelfPermission("android.permission.READ_EXTERNAL_STORAGE") == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ActivityCompat.requestPermissions((Activity) this.context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
        Toast.makeText(this.context, "Permission Granted", Toast.LENGTH_LONG).show();
    }

    public void checkForWritePermission() {
        if (Build.VERSION.SDK_INT < 23 || this.context.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
        builder.setTitle("Allow Storage Permission");
        builder.setMessage("Application Needs to Store Your Data,Gives Storage Permission.Click Yes to Allow !");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                ActivityCompat.requestPermissions((Activity) AppPermissions.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
                Toast.makeText(AppPermissions.this.context, "Permission Granted", Toast.LENGTH_LONG).show();
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    
    public void openSettings() {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", this.context.getPackageName(), null));
        ((Activity) this.context).startActivityForResult(intent, 101);
    }
}
