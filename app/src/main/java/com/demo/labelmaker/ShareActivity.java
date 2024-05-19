package com.demo.labelmaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import java.io.File;

import com.demo.labelmaker.Adapters.SliderPagerAdapter;
import com.demo.labelmaker.Utility.Constants;
import com.demo.labelmaker.Utility.UtilityFunctions;


public class ShareActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView errorView;
    ViewPager pager;
    SliderPagerAdapter pagerAdapter;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.share_activity);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd_Counter(this);


        new UtilityFunctions().fullScreenActivity(this);

        this.pagerAdapter = new SliderPagerAdapter(this);
        this.pager = (ViewPager) findViewById(R.id.viewPager);
        this.errorView = (ImageView) findViewById(R.id.error_view);
        int i = getIntent().getExtras().getInt(Constants.IMG_ID);
        this.pager.setAdapter(this.pagerAdapter);
        this.pager.setCurrentItem(i);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.delBtn).setOnClickListener(this);
        findViewById(R.id.shareBtn).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.delBtn) {
            try {
                openDeleteConfirmationDialog(this.pager.getCurrentItem());
            } catch (Exception unused) {
                Toast.makeText(getApplicationContext(), "Something Went Wrong :(", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.shareBtn && this.pagerAdapter.getCount() > 0) {
            if (Build.VERSION.SDK_INT >= 29) {
                shareImageWithQ(Uri.parse(this.pagerAdapter.getImage(this.pager.getCurrentItem()).toString()));
            } else {
                shareImage(new File(this.pagerAdapter.getImage(this.pager.getCurrentItem()).toString()));
            }

        } else {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse(Constants.DEV_NAME)));
        }
    }

    private void openDeleteConfirmationDialog(final int i) throws Exception {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Want to Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                if (ShareActivity.this.pagerAdapter.getCount() > 0) {
                    if (Build.VERSION.SDK_INT >= 29) {
                        ShareActivity.this.getContentResolver().delete(ShareActivity.this.pagerAdapter.getImage(i), null, null);
                        ShareActivity.this.refreshAdapter(i);
                    } else if (UtilityFunctions.deleteLogoFile(ShareActivity.this.pagerAdapter.getImage(i), ShareActivity.this.getApplicationContext())) {
                        ShareActivity.this.refreshAdapter(i);
                    }
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i2) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }


    public void refreshAdapter(int i) {
        this.pagerAdapter.resetAllItems();
        this.pager.setAdapter(this.pagerAdapter);
        this.pager.setCurrentItem(i);
        if (this.pagerAdapter.getCount() <= 0) {
            this.errorView.setVisibility(View.VISIBLE);
            this.errorView.setImageResource(R.drawable.error2);
        }
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

}
