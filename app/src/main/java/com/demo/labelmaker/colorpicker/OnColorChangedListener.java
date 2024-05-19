package com.demo.labelmaker.colorpicker;

import android.view.View;


public interface OnColorChangedListener {
    void onColorChanged(int i);

    void onColorChangedWithView(int i, View view);
}
