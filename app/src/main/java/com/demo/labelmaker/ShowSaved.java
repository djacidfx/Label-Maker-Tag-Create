package com.demo.labelmaker;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import com.demo.labelmaker.Utility.Constants;
import com.demo.labelmaker.Utility.UtilityFunctions;


public class ShowSaved extends AppCompatActivity implements View.OnClickListener {
    Uri fileUri;
    ImageView imageView;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_show_saved);

        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd_Counter(this);

        new UtilityFunctions().fullScreenActivity(this);
        this.imageView = (ImageView) findViewById(R.id.imageView);
        if (getIntent().getExtras() != null) {
            this.fileUri = Uri.parse(getIntent().getStringExtra(Constants.SHARE_KEY));
        }
        if (Build.VERSION.SDK_INT >= 29) {
            this.imageView.setImageURI(Uri.parse(this.fileUri.toString()));
        } else {
            this.imageView.setImageURI(this.fileUri);
        }
        initListener();
    }

    private void initListener() {
        findViewById(R.id.shareBtn).setOnClickListener(this);
        findViewById(R.id.delBtn).setOnClickListener(this);

    }

    @Override 
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.delBtn :
                try {
                    openDeleteConfirmationDialog();
                    return;
                } catch (Exception unused) {
                    Toast.makeText(getApplicationContext(), "Something Went Wrong :(", Toast.LENGTH_LONG).show();
                    return;
                }
            case R.id.shareBtn :
                if (Build.VERSION.SDK_INT >= 29) {
                    Uri parse = Uri.parse(this.fileUri.toString());
                    this.fileUri = parse;
                    shareImageWithQ(parse);
                    return;
                }
                shareImage(new File(this.fileUri.getPath()));
                return;
            default:
                return;
        }
    }

    private void openDeleteConfirmationDialog() throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Want to Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ShowSaved.this.fileUri != null) {
                    if (Build.VERSION.SDK_INT >= 29) {
                        ShowSaved.this.getContentResolver().delete(ShowSaved.this.fileUri, null, null);
                        ShowSaved.this.recreate();
                    } else if (UtilityFunctions.deleteLogoFile(ShowSaved.this.fileUri, ShowSaved.this.getApplicationContext())) {
                        ShowSaved.this.recreate();
                    }
                }
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

    private void shareImageWithQ(Uri uri) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.sharetext) + " " + getResources().getString(R.string.app_name) + ". " + getResources().getString(R.string.sharetext1) + "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra("android.intent.extra.STREAM", uri);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private void shareImage(File file) {
        Uri uriForFile = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("android.intent.extra.SUBJECT", getResources().getString(R.string.app_name));
        intent.putExtra("android.intent.extra.TEXT", getResources().getString(R.string.sharetext) + " " + getResources().getString(R.string.app_name) + ". " + getResources().getString(R.string.sharetext1) + "https://play.google.com/store/apps/details?id=" + getPackageName());
        intent.putExtra("android.intent.extra.STREAM", uriForFile);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private void rateFunc() {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
    }

    private void feedbackFunc() {
        Intent intent = new Intent("android.intent.action.SEND");
        if (isAppInstalled(this, "com.google.android.gm")) {
            intent.setType("message/rfc822");
            intent.putExtra("android.intent.extra.EMAIL", new String[]{Constants.REPORT_EMAIL});
            intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        } else {
            intent.setType("message/rfc822");
            intent.putExtra("android.intent.extra.EMAIL", new String[]{Constants.REPORT_EMAIL});
            intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.getStackTrace();
        }
    }

    private boolean isAppInstalled(Context context, String str) {
        try {
            context.getPackageManager().getApplicationInfo(str, 0);
            return true;
        } catch (PackageManager.NameNotFoundException unused) {
            return false;
        }
    }
}
