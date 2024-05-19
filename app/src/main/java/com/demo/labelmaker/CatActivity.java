package com.demo.labelmaker;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.demo.labelmaker.Adapters.ItemCatPagerAdapter;
import com.demo.labelmaker.ItemTabs.ItemTab_1;
import com.demo.labelmaker.Utility.Constants;


public class CatActivity extends AppCompatActivity implements OnComponentAddedListener, ItemTab_1.OnFragmentInteractionListener {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override 
    public void onCurveTextSelected(String str, Boolean bool) {
    }

    @Override 
    public void onFontSelected(Typeface typeface) {
    }

    @Override 
    public void onFragmentInteraction(Uri uri) {
    }

    @Override 
    public void onTextSelected(String str, Boolean bool) {
    }

    
    @Override 
    public void onCreate(Bundle bundle) {
        String[] strArr;
        super.onCreate(bundle);
        setContentView(R.layout.activity_cat);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd_Counter(this);


        this.tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        this.viewPager = (ViewPager) findViewById(R.id.pager);
        this.tabLayout.setInlineLabel(true);
        this.tabLayout.setHorizontalScrollBarEnabled(true);
        for (String str : Constants.All_FRAMES_TITLE) {
            TabLayout tabLayout = this.tabLayout;
            tabLayout.addTab(tabLayout.newTab().setText(str));
        }
        this.tabLayout.setTabGravity(0);
        this.viewPager.setAdapter(new ItemCatPagerAdapter(getSupportFragmentManager(), this.tabLayout.getTabCount(), this, Constants.All_FRAMES_TITLE, 0));
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { 
            @Override 
            public void onTabReselected(TabLayout.Tab tab) {
            }

            @Override 
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override 
            public void onTabSelected(TabLayout.Tab tab) {
                CatActivity.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
    }

    @Override 
    public void onAnyItemAdded(int i, int i2) {
        Intent intent = new Intent(this, Second.class);
        intent.putExtra(Constants.CAT_KEY, i);
        intent.putExtra(Constants.ITEM_KEY, i2);
        startActivity(intent);

    }
}
