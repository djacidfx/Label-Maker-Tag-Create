package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.demo.labelmaker.R;


public class GridRvAdapter {
    Context context;
    MyGridAdapter gridAdapter;
    GridRvListener gridRvListener;
    int[] nameList;
    RecyclerView recyclerView;

    
    public interface GridRvListener {
        void onGirdItemClicked(int i, int i2, RecyclerView recyclerView);
    }

    public GridRvAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.gridAdapter = new MyGridAdapter(context);
        setAdapter();
    }

    public void setNameList(int[] iArr) {
        this.nameList = iArr;
        this.gridAdapter.setNameList(iArr);
    }

    public void setAdapter() {
        this.recyclerView.setLayoutManager(new GridLayoutManager(this.context, 5));
        this.recyclerView.setAdapter(this.gridAdapter);
    }

    public void setRecyclerViewItemListener() {
        this.recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this.context, new RecyclerItemClickListener.OnItemClickListener() { 
            @Override 
            public void onItemClick(View view, int i) {
                GridRvAdapter.this.gridRvListener.onGirdItemClicked(GridRvAdapter.this.nameList[i], i, GridRvAdapter.this.recyclerView);
            }
        }));
    }

    public void setGridRvListener(GridRvListener gridRvListener) {
        this.gridRvListener = gridRvListener;
    }

    
    
    public static class MyGridAdapter extends RecyclerView.Adapter<MyGridAdapter.MyViewHolder> {
        Context context;
        int[] nameList;

        public MyGridAdapter(Context context) {
            this.context = context;
        }

        public void setNameList(int[] iArr) {
            this.nameList = iArr;
        }

        @Override 
        public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_rv_item, (ViewGroup) null));
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
