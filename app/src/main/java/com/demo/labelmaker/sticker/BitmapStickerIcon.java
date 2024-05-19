package com.demo.labelmaker.sticker;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


public class BitmapStickerIcon extends DrawableSticker implements StickerIconEvent {
    public static final float DEFAULT_ICON_EXTRA_RADIUS = 10.0f;
    public static final float DEFAULT_ICON_RADIUS = 30.0f;
    public static final int LEFT_BOTTOM = 2;
    public static final int LEFT_TOP = 0;
    public static final int RIGHT_BOTOM = 3;
    public static final int RIGHT_TOP = 1;
    private StickerIconEvent iconEvent;
    private float iconExtraRadius;
    private float iconRadius;
    private int position;
    private float x;
    private float y;

    @Retention(RetentionPolicy.SOURCE)
    
    public @interface Gravity {
    }

    public BitmapStickerIcon(Drawable drawable, int i) {
        super(drawable);
        this.iconRadius = 30.0f;
        this.iconExtraRadius = 10.0f;
        this.position = 0;
        this.position = i;
    }

    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(this.x, this.y, this.iconRadius, paint);
        super.draw(canvas);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float f) {
        this.x = f;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float f) {
        this.y = f;
    }

    public float getIconRadius() {
        return this.iconRadius;
    }

    public void setIconRadius(float f) {
        this.iconRadius = f;
    }

    public float getIconExtraRadius() {
        return this.iconExtraRadius;
    }

    public void setIconExtraRadius(float f) {
        this.iconExtraRadius = f;
    }

    @Override 
    public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionDown(stickerView, motionEvent);
        }
    }

    @Override 
    public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionMove(stickerView, motionEvent);
        }
    }

    @Override 
    public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {
        StickerIconEvent stickerIconEvent = this.iconEvent;
        if (stickerIconEvent != null) {
            stickerIconEvent.onActionUp(stickerView, motionEvent);
        }
    }

    public StickerIconEvent getIconEvent() {
        return this.iconEvent;
    }

    public void setIconEvent(StickerIconEvent stickerIconEvent) {
        this.iconEvent = stickerIconEvent;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }
}
