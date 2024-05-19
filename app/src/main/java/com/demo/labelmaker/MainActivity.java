package com.demo.labelmaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.labelmaker.Utility.UtilityFunctions;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView createBtn;
    ImageView policyBtn;
    ImageView rateBtn;
    ImageView savedBtn;
    LinearLayout splashLayout;

    public void splashClickHandler(View view) {
    }

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd_Counter(this);



        new UtilityFunctions().fullScreenActivity(this);
        this.splashLayout = (LinearLayout) findViewById(R.id.splash);

        splashFunc();
        initFunc();

    }

    public void initFunc() {
        this.createBtn = (ImageView) findViewById(R.id.create_btn);
        this.savedBtn = (ImageView) findViewById(R.id.save_btn);
        this.rateBtn = (ImageView) findViewById(R.id.rate_btn);
        this.policyBtn = (ImageView) findViewById(R.id.policy_btn);
        this.createBtn.setOnClickListener(this);
        this.savedBtn.setOnClickListener(this);
        this.rateBtn.setOnClickListener(this);
        this.policyBtn.setOnClickListener(this);
    }

    public void splashFunc() {
        new Handler().postDelayed(new Runnable() { 
            @Override 
            public void run() {
                MainActivity.this.splashLayout.setVisibility(View.GONE);
            }
        }, 4000L);
    }

    @Override 
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_btn :
                startActivity(new Intent(this, CatActivity.class));
                return;

            case R.id.policy_btn :
                uriFunc("https://www.google.com");
                return;
            case R.id.rate_btn :
                uriFunc("https://play.google.com/store/apps/details?id=" + getPackageName());
                return;
            case R.id.save_btn :
                startActivity(new Intent(this, MyText.class));
                return;
            default:
                return;
        }
    }

    public void uriFunc(String str) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }
}
