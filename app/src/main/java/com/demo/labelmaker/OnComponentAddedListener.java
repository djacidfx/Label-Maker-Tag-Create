package com.demo.labelmaker;

import android.graphics.Typeface;


public interface OnComponentAddedListener {
    void onAnyItemAdded(int i, int i2);

    void onCurveTextSelected(String str, Boolean bool);

    void onFontSelected(Typeface typeface);

    void onTextSelected(String str, Boolean bool);
}
