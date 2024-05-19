package com.demo.labelmaker.colorpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.demo.labelmaker.R;


public class LineColorPicker extends View {
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    private int cellSize;
    int[] colors;
    private boolean isClick;
    boolean isColorSelected;
    private int mOrientation;
    private OnColorChangedListener onColorChanged;
    private Paint paint;
    private Rect rect;
    private int screenH;
    private int screenW;
    private int selectedColor;

    public LineColorPicker(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        int resourceId;
        if (isInEditMode()) {
            this.colors = Palette.DEFAULT;
        } else {
            this.colors = new int[1];
        }
        this.rect = new Rect();
        this.isColorSelected = false;
        this.selectedColor = this.colors[0];
        this.mOrientation = 0;
        this.isClick = false;
        Paint paint = new Paint();
        this.paint = paint;
        paint.setStyle(Paint.Style.FILL);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.LineColorPicker, 0, 0);
        try {
            this.mOrientation = obtainStyledAttributes.getInteger(R.styleable.LineColorPicker_orientation, 0);
            if (!isInEditMode() && (resourceId = obtainStyledAttributes.getResourceId(R.styleable.LineColorPicker_colors, -1)) > 0) {
                setColors(context.getResources().getIntArray(resourceId));
            }
            int integer = obtainStyledAttributes.getInteger(R.styleable.LineColorPicker_selectedColorIndex, -1);
            if (integer != -1) {
                int[] colors = getColors();
                if (integer < (colors != null ? colors.length : 0)) {
                    setSelectedColorPosition(integer);
                }
            }
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    @Override 
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mOrientation == 0) {
            drawHorizontalPicker(canvas);
        } else {
            drawVerticalPicker(canvas);
        }
    }

    private void drawVerticalPicker(Canvas canvas) {
        this.rect.left = 0;
        this.rect.top = 0;
        this.rect.right = canvas.getWidth();
        this.rect.bottom = 0;
        int round = Math.round(canvas.getWidth() * 0.08f);
        int i = 0;
        while (true) {
            int[] iArr = this.colors;
            if (i >= iArr.length) {
                return;
            }
            this.paint.setColor(iArr[i]);
            Rect rect = this.rect;
            rect.top = rect.bottom;
            this.rect.bottom += this.cellSize;
            if (this.isColorSelected && this.colors[i] == this.selectedColor) {
                this.rect.left = 0;
                this.rect.right = canvas.getWidth();
            } else {
                this.rect.left = round;
                this.rect.right = canvas.getWidth() - round;
            }
            canvas.drawRect(this.rect, this.paint);
            i++;
        }
    }

    private void drawHorizontalPicker(Canvas canvas) {
        this.rect.left = 0;
        this.rect.top = 0;
        this.rect.right = 0;
        this.rect.bottom = canvas.getHeight();
        int round = Math.round(canvas.getHeight() * 0.08f);
        int i = 0;
        while (true) {
            int[] iArr = this.colors;
            if (i >= iArr.length) {
                return;
            }
            this.paint.setColor(iArr[i]);
            Rect rect = this.rect;
            rect.left = rect.right;
            this.rect.right += this.cellSize;
            if (this.isColorSelected && this.colors[i] == this.selectedColor) {
                this.rect.top = 0;
                this.rect.bottom = canvas.getHeight();
            } else {
                this.rect.top = round;
                this.rect.bottom = canvas.getHeight() - round;
            }
            canvas.drawRect(this.rect, this.paint);
            i++;
        }
    }

    private void onColorChanged(int i) {
        OnColorChangedListener onColorChangedListener = this.onColorChanged;
        if (onColorChangedListener != null) {
            onColorChangedListener.onColorChanged(i);
        }
    }

    private void onColorChangedWithView(int i, View view) {
        OnColorChangedListener onColorChangedListener = this.onColorChanged;
        if (onColorChangedListener != null) {
            onColorChangedListener.onColorChangedWithView(i, view);
        }
    }

    @Override 
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.isClick = true;
        } else if (action == 1) {
            setSelectedColor(getColorAtXY(motionEvent.getX(), motionEvent.getY()));
            if (this.isClick) {
                performClick();
            }
        } else if (action == 2) {
            setSelectedColor(getColorAtXY(motionEvent.getX(), motionEvent.getY()));
        } else if (action == 3) {
            this.isClick = false;
        } else if (action == 4) {
            this.isClick = false;
        }
        return true;
    }

    private int getColorAtXY(float f, float f2) {
        int i = 0;
        if (this.mOrientation != 0) {
            int i2 = 0;
            while (true) {
                int[] iArr = this.colors;
                if (i >= iArr.length) {
                    break;
                }
                int i3 = this.cellSize + i2;
                if (f2 >= i2 && f2 <= i3) {
                    return iArr[i];
                }
                i++;
                i2 = i3;
            }
        } else {
            int i4 = 0;
            while (true) {
                int[] iArr2 = this.colors;
                if (i >= iArr2.length) {
                    break;
                }
                int i5 = this.cellSize + i4;
                if (i4 <= f && i5 >= f) {
                    return iArr2[i];
                }
                i++;
                i4 = i5;
            }
        }
        return this.selectedColor;
    }

    @Override 
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.selectedColor = this.selectedColor;
        savedState.isColorSelected = this.isColorSelected;
        return savedState;
    }

    @Override 
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.selectedColor = savedState.selectedColor;
        this.isColorSelected = savedState.isColorSelected;
    }

    
    
    public static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() { 
            @Override 
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override 
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean isColorSelected;
        int selectedColor;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.selectedColor = parcel.readInt();
            this.isColorSelected = parcel.readInt() == 1;
        }

        @Override 
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.selectedColor);
            parcel.writeInt(this.isColorSelected ? 1 : 0);
        }
    }

    @Override 
    public boolean performClick() {
        return super.performClick();
    }

    @Override 
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        this.screenW = i;
        this.screenH = i2;
        recalcCellSize();
        super.onSizeChanged(i, i2, i3, i4);
    }

    public int getColor() {
        return this.selectedColor;
    }

    public void setSelectedColor(int i) {
        if (containsColor(this.colors, i)) {
            if (this.isColorSelected && this.selectedColor == i) {
                return;
            }
            this.selectedColor = i;
            this.isColorSelected = true;
            invalidate();
            onColorChanged(i);
            onColorChangedWithView(i, this);
        }
    }

    public void setSelectedColorPosition(int i) {
        setSelectedColor(this.colors[i]);
    }

    public void setColors(int[] iArr) {
        this.colors = iArr;
        if (!containsColor(iArr, this.selectedColor)) {
            this.selectedColor = iArr[0];
        }
        recalcCellSize();
        invalidate();
    }

    private int recalcCellSize() {
        if (this.mOrientation == 0) {
            this.cellSize = Math.round(this.screenW / (this.colors.length * 1.0f));
        } else {
            this.cellSize = Math.round(this.screenH / (this.colors.length * 1.0f));
        }
        return this.cellSize;
    }

    public int[] getColors() {
        return this.colors;
    }

    private boolean containsColor(int[] iArr, int i) {
        for (int i2 : iArr) {
            if (i2 == i) {
                return true;
            }
        }
        return false;
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.onColorChanged = onColorChangedListener;
    }
}
