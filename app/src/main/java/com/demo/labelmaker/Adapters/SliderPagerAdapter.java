package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;
import com.demo.labelmaker.Utility.GetSavedFiles;


public class SliderPagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<Uri> myTexts;

    @Override 
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    public SliderPagerAdapter(Context context) {
        this.myTexts = new GetSavedFiles(context).GetAllSavedFiles();
        this.mContext = context;
    }

    @Override 
    public int getCount() {
        try {
            return this.myTexts.size();
        } catch (Exception unused) {
            return 0;
        }
    }

    @Override 
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageURI(this.myTexts.get(i));
        viewGroup.addView(imageView, 0);
        return imageView;
    }

    @Override 
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView((View) obj);
    }

    public void resetAllItems() {
        this.myTexts = new GetSavedFiles(this.mContext).GetAllSavedFiles();
    }

    public Uri getImage(int i) {
        return this.myTexts.get(i);
    }
}
