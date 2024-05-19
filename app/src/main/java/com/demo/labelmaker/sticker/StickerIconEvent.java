package com.demo.labelmaker.sticker;

import android.view.MotionEvent;


public interface StickerIconEvent {
    void onActionDown(StickerView stickerView, MotionEvent motionEvent);

    void onActionMove(StickerView stickerView, MotionEvent motionEvent);

    void onActionUp(StickerView stickerView, MotionEvent motionEvent);
}
