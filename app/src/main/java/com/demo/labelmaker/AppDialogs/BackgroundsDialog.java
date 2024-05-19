package com.demo.labelmaker.AppDialogs;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.demo.labelmaker.R;
import com.google.android.material.tabs.TabLayout;


public class BackgroundsDialog {

    
    public static class setBgDialog extends DialogFragment {
        int catPosition;
        String[] getItemTabs;
        int height;
        TabLayout tabLayout;
        ViewPager viewPager;
        int width;

        public setBgDialog(String[] strArr, int i) {
            this.getItemTabs = strArr;
            this.catPosition = i;
        }

        @Override 
        public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
            String[] strArr;
            this.width = getResources().getDimensionPixelSize(R.dimen._280sdp);
            this.height = getResources().getDimensionPixelSize(R.dimen._400sdp);
            getDialog().getWindow().setLayout(this.width, this.height);
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0));
            View inflate = layoutInflater.inflate(R.layout.backgrounds_dialog_layout, viewGroup, false);
            this.tabLayout = (TabLayout) inflate.findViewById(R.id.tab_layout);
            this.viewPager = (ViewPager) inflate.findViewById(R.id.pager);
            this.tabLayout.setInlineLabel(true);
            this.tabLayout.setHorizontalScrollBarEnabled(true);
            for (String str : this.getItemTabs) {
                TabLayout tabLayout = this.tabLayout;
                tabLayout.addTab(tabLayout.newTab().setText(str));
            }
            this.tabLayout.setTabGravity(0);
            this.viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
            this.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { 
                @Override 
                public void onTabReselected(TabLayout.Tab tab) {
                }

                @Override 
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override 
                public void onTabSelected(TabLayout.Tab tab) {
                    setBgDialog.this.viewPager.setCurrentItem(tab.getPosition());
                }
            });
            return inflate;
        }

        @Override 
        public void onResume() {
            super.onResume();
            Window window = getDialog().getWindow();
            if (window == null) {
                return;
            }
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.width = this.width;
            attributes.height = this.height;
            window.setAttributes(attributes);
        }

        @Override 
        public void onDestroy() {
            super.onDestroy();
            this.tabLayout = null;
            this.viewPager = null;
        }
    }
}
