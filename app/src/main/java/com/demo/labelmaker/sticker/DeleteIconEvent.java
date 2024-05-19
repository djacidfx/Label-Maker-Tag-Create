package com.demo.labelmaker.sticker;

import android.view.MotionEvent;


public class DeleteIconEvent implements StickerIconEvent {
    @Override 
    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
    }

    @Override 
    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
    }

    @Override 
    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        stickerView.removeCurrentSticker();
    }
}
