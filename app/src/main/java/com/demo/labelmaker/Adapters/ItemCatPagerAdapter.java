package com.demo.labelmaker.Adapters;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.demo.labelmaker.ItemTabs.ItemTab_1;


public class ItemCatPagerAdapter extends FragmentStatePagerAdapter {
    private int cat_position;
    Context context;
    private String[] itemTabs;
    private int mNoOfTabs;

    public ItemCatPagerAdapter(FragmentManager fragmentManager, int i, Context context, String[] strArr, int i2) {
        super(fragmentManager);
        this.mNoOfTabs = i;
        this.context = context;
        this.itemTabs = strArr;
        this.cat_position = i2;
    }

    @Override 
    public Fragment getItem(int i) {
        for (int i2 = 0; i2 <= this.itemTabs.length; i2++) {
            if (i == i2) {
                return new ItemTab_1(i);
            }
        }
        return null;
    }

    @Override 
    public int getCount() {
        return this.mNoOfTabs;
    }
}
