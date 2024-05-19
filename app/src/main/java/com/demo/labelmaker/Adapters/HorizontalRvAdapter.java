package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.labelmaker.R;


public class HorizontalRvAdapter {
    Context context;
    HorizontalRvListener horizontalRvListener;
    int[] nameList;
    RecyclerView recyclerView;

    
    public interface HorizontalRvListener {
        void onItemClicked(int i, RecyclerView recyclerView);
    }

    public HorizontalRvAdapter(Context context, int[] iArr, RecyclerView recyclerView) {
        this.context = context;
        this.nameList = iArr;
        this.recyclerView = recyclerView;
        setAdapter();
    }

    public void setAdapter() {
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false));
        this.recyclerView.setAdapter(new MyHorizontalAdapter(this.context, this.nameList));
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.context, new RecyclerItemClickListener.OnItemClickListener() { 
            @Override 
            public void onItemClick(View view, int i) {
                HorizontalRvAdapter.this.horizontalRvListener.onItemClicked(HorizontalRvAdapter.this.nameList[i], HorizontalRvAdapter.this.recyclerView);
            }
        }));
    }

    public void setHorizontalRvListener(HorizontalRvListener horizontalRvListener) {
        this.horizontalRvListener = horizontalRvListener;
    }

    
    
    public static class MyHorizontalAdapter extends RecyclerView.Adapter<MyHorizontalAdapter.MyViewHolder> {
        Context context;
        int[] nameList;

        public MyHorizontalAdapter(Context context, int[] iArr) {
            this.context = context;
            this.nameList = iArr;
        }

        @Override 
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pattern_grid_item, (ViewGroup) null));
        }

        @Override 
        public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
            Glide.with(this.context).load(Integer.valueOf(this.nameList[i])).thumbnail(0.9f).into(myViewHolder.imageView);
        }

        @Override 
        public int getItemCount() {
            return this.nameList.length;
        }

        
        public static class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public MyViewHolder(View view) {
                super(view);
                this.imageView = (ImageView) view.findViewById(R.id.imageView);
            }
        }
    }
}
