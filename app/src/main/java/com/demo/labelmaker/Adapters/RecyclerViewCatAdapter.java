package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.labelmaker.R;


public class RecyclerViewCatAdapter extends RecyclerView.Adapter<RecyclerViewCatAdapter.MyViewHolder> {
    private int[] bitmapArrayList;
    Context context;

    public RecyclerViewCatAdapter(int[] iArr, Context context) {
        this.context = context;
        this.bitmapArrayList = iArr;
    }

    @Override 
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        ImageView imageView = myViewHolder.imageView;
        Glide.with(this.context).load(Integer.valueOf(this.bitmapArrayList[i])).thumbnail(0.9f).into(imageView);

    }

    @Override 
    public int getItemCount() {
        return this.bitmapArrayList.length;
    }

    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView adIcon;
        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) this.itemView.findViewById(R.id.imageView);

        }
    }
}
