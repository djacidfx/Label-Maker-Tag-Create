package com.demo.labelmaker.sticker;

import android.view.MotionEvent;


public class ZoomIconEvent implements StickerIconEvent {
    @Override 
    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
    }

    @Override 
    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
        stickerView.zoomAndRotateCurrentSticker(motionEvent);
    }

    @Override 
    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        if (stickerView.getOnStickerOperationListener() != null) {
            stickerView.getOnStickerOperationListener().onStickerZoomFinished(stickerView.getCurrentSticker());
        }
    }
}
