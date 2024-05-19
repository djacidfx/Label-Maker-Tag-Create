package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.labelmaker.R;

import java.util.ArrayList;


public class LibraryAdapter extends RecyclerView.Adapter<LibraryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Uri> mWallpaper;

    public LibraryAdapter(ArrayList<Uri> arrayList, Context context) {
        this.mWallpaper = arrayList;
        this.context = context;
    }

    @Override 
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.library_grid_item, viewGroup, false));
    }

    @Override 
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        if (Build.VERSION.SDK_INT >= 29) {
            Glide.with(this.context).load(this.mWallpaper.get(i)).thumbnail(0.1f).into(myViewHolder.imageView);
        } else {
            Glide.with(this.context).load(this.mWallpaper.get(i).getPath()).thumbnail(0.1f).into(myViewHolder.imageView);
        }
    }

    @Override 
    public int getItemCount() {
        ArrayList<Uri> arrayList = this.mWallpaper;
        if (arrayList == null) {
            return 0;
        }
        return arrayList.size();
    }

    
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        private MyViewHolder(View view) {
            super(view);
            this.imageView = (ImageView) this.itemView.findViewById(R.id.imageView);
        }
    }
}
