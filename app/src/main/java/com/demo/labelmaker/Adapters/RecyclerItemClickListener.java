package com.demo.labelmaker.Adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    GestureDetector mGestureDetector;
    private OnItemClickListener mListener;

    
    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    @Override 
    public void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    @Override 
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    public RecyclerItemClickListener(Context context, OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
        this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() { 
            @Override 
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return true;
            }
        });
    }

    @Override 
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View findChildViewUnder = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
        if (findChildViewUnder == null || this.mListener == null || !this.mGestureDetector.onTouchEvent(motionEvent)) {
            return false;
        }
        this.mListener.onItemClick(findChildViewUnder, recyclerView.getChildPosition(findChildViewUnder));
        return false;
    }
}
