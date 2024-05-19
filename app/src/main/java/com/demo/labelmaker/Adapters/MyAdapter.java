package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.labelmaker.R;

import com.demo.labelmaker.Utility.UtilityFunctions;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    Context context;
    int[] mWallpaper;

    public MyAdapter(int[] iArr, Context context) {
        this.mWallpaper = iArr;
        this.context = context;
    }

    @Override 
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.imageView.setImageBitmap(UtilityFunctions.getScaledBitmap(this.mWallpaper[i], this.context));
    }

    @Override 
    public int getItemCount() {
        return this.mWallpaper.length;
    }

    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) this.itemView.findViewById(R.id.imageView);
        }
    }
}
