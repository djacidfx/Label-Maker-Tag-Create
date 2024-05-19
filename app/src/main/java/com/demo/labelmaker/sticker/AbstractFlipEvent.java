package com.demo.labelmaker.sticker;

import android.view.MotionEvent;


public abstract class AbstractFlipEvent implements StickerIconEvent {
    protected abstract int getFlipDirection();

    @Override 
    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
    }

    @Override 
    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
    }

    @Override 
    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        stickerView.flipCurrentSticker(getFlipDirection());
    }
}
