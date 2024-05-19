package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.labelmaker.R;

import com.demo.labelmaker.Utility.Constants;


public class listAdapter extends ArrayAdapter {
    private String[] fonts;
    Context mContext;

    public listAdapter(Context context, String[] strArr) {
        super(context, R.layout.list_sample);
        this.mContext = context;
        this.fonts = strArr;
    }

    @Override 
    public int getCount() {
        return Constants.FONTS.length;
    }

    @Override 
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.list_sample, viewGroup, false);
            ((TextView) linearLayout.findViewById(R.id.text_v)).setTypeface(Typeface.createFromAsset(this.mContext.getAssets(), Constants.FONTS[i]));
            return linearLayout;
        }
        ((TextView) view.findViewById(R.id.text_v)).setTypeface(Typeface.createFromAsset(this.mContext.getAssets(), Constants.FONTS[i]));
        return view;
    }
}
