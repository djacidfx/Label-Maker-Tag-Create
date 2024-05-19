package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.demo.labelmaker.R;

import java.io.File;

import com.demo.labelmaker.Utility.Constants;
import com.demo.labelmaker.Utility.UtilityFunctions;


public class MyTextsGrid extends BaseAdapter {
    LayoutInflater layoutInflator;
    Context mContext;
    File[] myTexts = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + Constants.FOLDER_NAME).listFiles();

    @Override 
    public Object getItem(int i) {
        return null;
    }

    @Override 
    public long getItemId(int i) {
        return 0L;
    }

    public MyTextsGrid(Context context) {
        this.mContext = context;
        this.layoutInflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override 
    public int getCount() {
        try {
            return this.myTexts.length;
        } catch (Exception unused) {
            return 0;
        }
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        this.layoutInflator.inflate(R.layout.library_grid_item, viewGroup);
        ImageView imageView = new ImageView(this.mContext);
        imageView.setImageBitmap(UtilityFunctions.getScaledBitmap(this.myTexts[i]));
        return imageView;
    }
}
