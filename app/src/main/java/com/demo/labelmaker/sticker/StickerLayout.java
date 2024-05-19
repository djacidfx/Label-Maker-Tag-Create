package com.demo.labelmaker.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import androidx.core.content.ContextCompat;

import com.demo.labelmaker.R;

import java.util.Arrays;


public class StickerLayout extends StickerView {
    public void setStickerIcons(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
    }

    public StickerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setStickerIcons();
    }

    private void setStickerIcons() {
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_close_white_18dp), 0);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_scale_white_18dp), 3);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_flip_white_18dp), 1);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        BitmapStickerIcon bitmapStickerIcon4 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_flip_white_18dp), 2);
        bitmapStickerIcon4.setIconEvent(new StickerButtonsClickListener());
        setIcons(Arrays.asList(bitmapStickerIcon, bitmapStickerIcon2, bitmapStickerIcon3, bitmapStickerIcon4));
        setBackgroundColor(-1);
        setLocked(false);
        setConstrained(true);
    }

    public void addImageSticker(int i) {
        addSticker(new DrawableSticker(ContextCompat.getDrawable(getContext(), i)));
    }

    public void addImageSticker(Bitmap bitmap) {
        addSticker(new DrawableSticker(new BitmapDrawable(getContext().getResources(), bitmap)));
    }

    public void addTextSticker(String str) {
        TextSticker textSticker = new TextSticker(getContext());
        textSticker.setText(str);
        textSticker.resizeText();
        addSticker(textSticker);
    }

    public void addTextSticker(String str, int i) {
        TextSticker textSticker = new TextSticker(getContext());
        textSticker.setText(str);
        textSticker.setDrawable(ContextCompat.getDrawable(getContext(), i));
        textSticker.resizeText();
        addSticker(textSticker);
    }

    public void addCurveTextSticker(String str) {
        CurveText curveText = new CurveText(getContext());
        curveText.setText(str);
        curveText.resizeText();
        addSticker(curveText);
    }

    public void addTextSticker(String str, Bitmap bitmap) {
        TextSticker textSticker = new TextSticker(getContext());
        textSticker.setText(str);
        textSticker.setDrawable((Drawable) new BitmapDrawable(getResources(), bitmap));
        textSticker.resizeText();
        addSticker(textSticker);
    }

    
    
    public class StickerButtonsClickListener implements StickerIconEvent {
        @Override 
        public void onActionDown(StickerView stickerView, MotionEvent motionEvent) {
        }

        @Override 
        public void onActionMove(StickerView stickerView, MotionEvent motionEvent) {
        }

        private StickerButtonsClickListener() {
        }

        @Override 
        public void onActionUp(StickerView stickerView, MotionEvent motionEvent) {


            Log.e("StickerLayout","onActionUp");
            Sticker currentSticker = stickerView.getCurrentSticker();
            if (currentSticker instanceof TextSticker) {
                Drawable drawable = currentSticker.getDrawable();
                TextSticker textSticker = (TextSticker) currentSticker;
                String text = textSticker.getText();
                int textColor = textSticker.getTextColor();
                Typeface typeFace = textSticker.getTypeFace();
                StickerLayout.this.remove(currentSticker);
                Matrix matrix = currentSticker.getMatrix();
                TextSticker textSticker2 = new TextSticker(StickerLayout.this.getContext());
                textSticker2.setDrawable(drawable);
                textSticker2.setText(text);
                textSticker2.setTypeface(typeFace);
                textSticker2.setTextColor(textColor);
                StickerLayout.this.addSticker(textSticker2);
                textSticker2.resizeText();
                textSticker2.setMatrix(matrix);
            } else if (currentSticker instanceof DrawableSticker) {
                Drawable drawable2 = currentSticker.getDrawable();
                StickerLayout.this.remove(currentSticker);
                Matrix matrix2 = currentSticker.getMatrix();
                DrawableSticker drawableSticker = new DrawableSticker(drawable2);
                StickerLayout.this.addSticker(drawableSticker);
                drawableSticker.setMatrix(matrix2);
            }
        }
    }
}
