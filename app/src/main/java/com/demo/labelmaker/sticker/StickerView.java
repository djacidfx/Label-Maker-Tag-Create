package com.demo.labelmaker.sticker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;

import com.demo.labelmaker.R;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class StickerView extends FrameLayout {
    private static final int DEFAULT_MIN_CLICK_DELAY_TIME = 200;
    public static final int FLIP_HORIZONTALLY = 1;
    public static final int FLIP_VERTICALLY = 2;
    private static final String TAG = "StickerView";
    private final float[] bitmapPoints;
    private final Paint borderPaint;
    private final float[] bounds;
    private final boolean bringToFrontCurrentSticker;
    private boolean constrained;
    private final PointF currentCenterPoint;
    private BitmapStickerIcon currentIcon;
    private int currentMode;
    private final Matrix downMatrix;
    private float downX;
    private float downY;
    private Sticker handlingSticker;
    private final List<BitmapStickerIcon> icons;
    private long lastClickTime;
    private boolean locked;
    private PointF midPoint;
    private int minClickDelayTime;
    private final Matrix moveMatrix;
    private float oldDistance;
    private float oldRotation;
    private OnStickerOperationListener onStickerOperationListener;
    private final float[] point;
    private final boolean showBorder;
    private final boolean showIcons;
    private final Matrix sizeMatrix;
    private final RectF stickerRect;
    private final List<Sticker> stickers;
    private final float[] tmp;
    private final int touchSlop;

    @Retention(RetentionPolicy.SOURCE)
    
    protected @interface ActionMode {
        public static final int CLICK = 4;
        public static final int DRAG = 1;
        public static final int ICON = 3;
        public static final int NONE = 0;
        public static final int ZOOM_WITH_TWO_FINGER = 2;
    }

    @Retention(RetentionPolicy.SOURCE)
    
    protected @interface Flip {
    }

    
    public interface OnStickerOperationListener {
        void onStickerAdded(Sticker sticker);

        void onStickerClicked(Sticker sticker);

        void onStickerDeleted(Sticker sticker);

        void onStickerDoubleTapped(Sticker sticker);

        void onStickerDragFinished(Sticker sticker);

        void onStickerFlipped(Sticker sticker);

        void onStickerZoomFinished(Sticker sticker);
    }

    public StickerView(Context context) {
        this(context, null);
    }

    public StickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public StickerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.stickers = new ArrayList();
        this.icons = new ArrayList(4);
        Paint paint = new Paint();
        this.borderPaint = paint;
        this.stickerRect = new RectF();
        this.sizeMatrix = new Matrix();
        this.downMatrix = new Matrix();
        this.moveMatrix = new Matrix();
        this.bitmapPoints = new float[8];
        this.bounds = new float[8];
        this.point = new float[2];
        this.currentCenterPoint = new PointF();
        this.tmp = new float[2];
        this.midPoint = new PointF();
        this.oldDistance = 0.0f;
        this.oldRotation = 0.0f;
        this.currentMode = 0;
        this.lastClickTime = 0L;
        this.minClickDelayTime = 200;
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.StickerView);
            this.showIcons = typedArray.getBoolean(R.styleable.StickerView_showIcons, false);
            this.showBorder = typedArray.getBoolean(R.styleable.StickerView_showBorder, false);
            this.bringToFrontCurrentSticker = typedArray.getBoolean(R.styleable.StickerView_bringToFrontCurrentSticker, false);
            paint.setAntiAlias(true);
            paint.setColor(typedArray.getColor(R.styleable.StickerView_borderColor, ViewCompat.MEASURED_STATE_MASK));
            paint.setAlpha(typedArray.getInteger(R.styleable.StickerView_borderAlpha, 128));
            configDefaultIcons();
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }
    }

    public void configDefaultIcons() {
        BitmapStickerIcon bitmapStickerIcon = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_close_white_18dp), 0);
        bitmapStickerIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon bitmapStickerIcon2 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_scale_white_18dp), 3);
        bitmapStickerIcon2.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon bitmapStickerIcon3 = new BitmapStickerIcon(ContextCompat.getDrawable(getContext(), R.drawable.sticker_ic_flip_white_18dp), 1);
        bitmapStickerIcon3.setIconEvent(new FlipHorizontallyEvent());
        this.icons.clear();
        this.icons.add(bitmapStickerIcon);
        this.icons.add(bitmapStickerIcon2);
        this.icons.add(bitmapStickerIcon3);
    }

    public void swapLayers(int i, int i2) {
        if (this.stickers.size() < i || this.stickers.size() < i2) {
            return;
        }
        Collections.swap(this.stickers, i, i2);
        invalidate();
    }

    public void sendToLayer(int i, int i2) {
        if (this.stickers.size() < i || this.stickers.size() < i2) {
            return;
        }
        this.stickers.remove(i);
        this.stickers.add(i2, this.stickers.get(i));
        invalidate();
    }

    @Override 
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.stickerRect.left = i;
            this.stickerRect.top = i2;
            this.stickerRect.right = i3;
            this.stickerRect.bottom = i4;
        }
    }

    @Override 
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawStickers(canvas);
    }

    protected void drawStickers(Canvas canvas) {
        float f;
        float f2;
        float f3;
        float f4;
        int i = 0;
        for (int i2 = 0; i2 < this.stickers.size(); i2++) {
            Sticker sticker = this.stickers.get(i2);
            if (sticker != null) {
                sticker.draw(canvas);
            }
        }
        Sticker sticker2 = this.handlingSticker;
        if (sticker2 == null || this.locked) {
            return;
        }
        if (this.showBorder || this.showIcons) {
            getStickerPoints(sticker2, this.bitmapPoints);
            float[] fArr = this.bitmapPoints;
            float f5 = fArr[0];
            int i3 = 1;
            float f6 = fArr[1];
            float f7 = fArr[2];
            float f8 = fArr[3];
            float f9 = fArr[4];
            float f10 = fArr[5];
            float f11 = fArr[6];
            float f12 = fArr[7];
            if (this.showBorder) {
                f = f12;
                f2 = f11;
                f3 = f10;
                f4 = f9;
                canvas.drawLine(f5, f6, f7, f8, this.borderPaint);
                canvas.drawLine(f5, f6, f4, f3, this.borderPaint);
                canvas.drawLine(f7, f8, f2, f, this.borderPaint);
                canvas.drawLine(f2, f, f4, f3, this.borderPaint);
            } else {
                f = f12;
                f2 = f11;
                f3 = f10;
                f4 = f9;
            }
            if (this.showIcons) {
                float f13 = f;
                float f14 = f2;
                float f15 = f3;
                float f16 = f4;
                float calculateRotation = calculateRotation(f14, f13, f16, f15);
                while (i < this.icons.size()) {
                    BitmapStickerIcon bitmapStickerIcon = this.icons.get(i);
                    int position = bitmapStickerIcon.getPosition();
                    if (position == 0) {
                        configIconMatrix(bitmapStickerIcon, f5, f6, calculateRotation);
                    } else if (position == i3) {
                        configIconMatrix(bitmapStickerIcon, f7, f8, calculateRotation);
                    } else if (position == 2) {
                        configIconMatrix(bitmapStickerIcon, f16, f15, calculateRotation);
                    } else if (position == 3) {
                        configIconMatrix(bitmapStickerIcon, f14, f13, calculateRotation);
                    }
                    bitmapStickerIcon.draw(canvas, this.borderPaint);
                    i++;
                    i3 = 1;
                }
            }
        }
    }

    protected void configIconMatrix(BitmapStickerIcon bitmapStickerIcon, float f, float f2, float f3) {
        bitmapStickerIcon.setX(f);
        bitmapStickerIcon.setY(f2);
        bitmapStickerIcon.getMatrix().reset();
        bitmapStickerIcon.getMatrix().postRotate(f3, bitmapStickerIcon.getWidth() / 2, bitmapStickerIcon.getHeight() / 2);
        bitmapStickerIcon.getMatrix().postTranslate(f - (bitmapStickerIcon.getWidth() / 2), f2 - (bitmapStickerIcon.getHeight() / 2));
    }

    @Override 
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.locked) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        if (motionEvent.getAction() == 0) {
            this.downX = motionEvent.getX();
            this.downY = motionEvent.getY();
            return (findCurrentIconTouched() == null && findHandlingSticker() == null) ? false : true;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override 
    public boolean onTouchEvent(MotionEvent motionEvent) {
        Sticker sticker;
        OnStickerOperationListener onStickerOperationListener;
        if (this.locked) {
            return super.onTouchEvent(motionEvent);
        }
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                onTouchUp(motionEvent);
            } else if (actionMasked == 2) {
                handleCurrentMode(motionEvent);
                invalidate();
            } else if (actionMasked == 5) {
                this.oldDistance = calculateDistance(motionEvent);
                this.oldRotation = calculateRotation(motionEvent);
                this.midPoint = calculateMidPoint(motionEvent);
                Sticker sticker2 = this.handlingSticker;
                if (sticker2 != null && isInStickerArea(sticker2, motionEvent.getX(1), motionEvent.getY(1)) && findCurrentIconTouched() == null) {
                    this.currentMode = 2;
                }
            } else if (actionMasked == 6) {
                if (this.currentMode == 2 && (sticker = this.handlingSticker) != null && (onStickerOperationListener = this.onStickerOperationListener) != null) {
                    onStickerOperationListener.onStickerZoomFinished(sticker);
                }
                this.currentMode = 0;
            }
        } else if (!onTouchDown(motionEvent)) {
            return false;
        }
        return true;
    }

    protected boolean onTouchDown(MotionEvent motionEvent) {
        this.currentMode = 1;
        this.downX = motionEvent.getX();
        this.downY = motionEvent.getY();
        PointF calculateMidPoint = calculateMidPoint();
        this.midPoint = calculateMidPoint;
        this.oldDistance = calculateDistance(calculateMidPoint.x, this.midPoint.y, this.downX, this.downY);
        this.oldRotation = calculateRotation(this.midPoint.x, this.midPoint.y, this.downX, this.downY);
        BitmapStickerIcon findCurrentIconTouched = findCurrentIconTouched();
        this.currentIcon = findCurrentIconTouched;
        if (findCurrentIconTouched != null) {
            this.currentMode = 3;
            findCurrentIconTouched.onActionDown(this, motionEvent);
        } else {
            this.handlingSticker = findHandlingSticker();
        }
        Sticker sticker = this.handlingSticker;
        if (sticker != null) {
            this.downMatrix.set(sticker.getMatrix());
            if (this.bringToFrontCurrentSticker) {
                this.stickers.remove(this.handlingSticker);
                this.stickers.add(this.handlingSticker);
            }
        }
        if (this.currentIcon == null && this.handlingSticker == null) {
            return false;
        }
        invalidate();
        return true;
    }

    protected void onTouchUp(MotionEvent motionEvent) {
        Sticker sticker;
        OnStickerOperationListener onStickerOperationListener;
        Sticker sticker2;
        OnStickerOperationListener onStickerOperationListener2;
        BitmapStickerIcon bitmapStickerIcon;
        long uptimeMillis = SystemClock.uptimeMillis();
        if (this.currentMode == 3 && (bitmapStickerIcon = this.currentIcon) != null && this.handlingSticker != null) {
            bitmapStickerIcon.onActionUp(this, motionEvent);
        }
        if (this.currentMode == 1 && Math.abs(motionEvent.getX() - this.downX) < this.touchSlop && Math.abs(motionEvent.getY() - this.downY) < this.touchSlop && (sticker2 = this.handlingSticker) != null) {
            this.currentMode = 4;
            OnStickerOperationListener onStickerOperationListener3 = this.onStickerOperationListener;
            if (onStickerOperationListener3 != null) {
                onStickerOperationListener3.onStickerClicked(sticker2);
            }
            if (uptimeMillis - this.lastClickTime < this.minClickDelayTime && (onStickerOperationListener2 = this.onStickerOperationListener) != null) {
                onStickerOperationListener2.onStickerDoubleTapped(this.handlingSticker);
            }
        }
        if (this.currentMode == 1 && (sticker = this.handlingSticker) != null && (onStickerOperationListener = this.onStickerOperationListener) != null) {
            onStickerOperationListener.onStickerDragFinished(sticker);
        }
        this.currentMode = 0;
        this.lastClickTime = uptimeMillis;
    }

    protected void handleCurrentMode(MotionEvent motionEvent) {
        BitmapStickerIcon bitmapStickerIcon;
        int i = this.currentMode;
        if (i == 1) {
            if (this.handlingSticker != null) {
                this.moveMatrix.set(this.downMatrix);
                this.moveMatrix.postTranslate(motionEvent.getX() - this.downX, motionEvent.getY() - this.downY);
                this.handlingSticker.setMatrix(this.moveMatrix);
                if (this.constrained) {
                    constrainSticker(this.handlingSticker);
                }
            }
        } else if (i == 2) {
            if (this.handlingSticker != null) {
                float calculateDistance = calculateDistance(motionEvent);
                float calculateRotation = calculateRotation(motionEvent);
                this.moveMatrix.set(this.downMatrix);
                Matrix matrix = this.moveMatrix;
                float f = this.oldDistance;
                matrix.postScale(calculateDistance / f, calculateDistance / f, this.midPoint.x, this.midPoint.y);
                this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
                this.handlingSticker.setMatrix(this.moveMatrix);
            }
        } else if (i != 3 || this.handlingSticker == null || (bitmapStickerIcon = this.currentIcon) == null) {
        } else {
            bitmapStickerIcon.onActionMove(this, motionEvent);
        }
    }

    public void zoomAndRotateCurrentSticker(MotionEvent motionEvent) {
        zoomAndRotateSticker(this.handlingSticker, motionEvent);
    }

    public void zoomAndRotateSticker(Sticker sticker, MotionEvent motionEvent) {
        if (sticker != null) {
            float calculateDistance = calculateDistance(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            float calculateRotation = calculateRotation(this.midPoint.x, this.midPoint.y, motionEvent.getX(), motionEvent.getY());
            this.moveMatrix.set(this.downMatrix);
            Matrix matrix = this.moveMatrix;
            float f = this.oldDistance;
            matrix.postScale(calculateDistance / f, calculateDistance / f, this.midPoint.x, this.midPoint.y);
            this.moveMatrix.postRotate(calculateRotation - this.oldRotation, this.midPoint.x, this.midPoint.y);
            this.handlingSticker.setMatrix(this.moveMatrix);
        }
    }

    protected void constrainSticker(Sticker sticker) {
        int width = getWidth();
        int height = getHeight();
        sticker.getMappedCenterPoint(this.currentCenterPoint, this.point, this.tmp);
        float f = this.currentCenterPoint.x < 0.0f ? -this.currentCenterPoint.x : 0.0f;
        float f2 = width;
        if (this.currentCenterPoint.x > f2) {
            f = f2 - this.currentCenterPoint.x;
        }
        float f3 = this.currentCenterPoint.y < 0.0f ? -this.currentCenterPoint.y : 0.0f;
        float f4 = height;
        if (this.currentCenterPoint.y > f4) {
            f3 = f4 - this.currentCenterPoint.y;
        }
        sticker.getMatrix().postTranslate(f, f3);
    }


    @Nullable
    protected BitmapStickerIcon findCurrentIconTouched() {
        for (BitmapStickerIcon icon : icons) {
            float x = icon.getX() - downX;
            float y = icon.getY() - downY;
            float distance_pow_2 = x * x + y * y;
            if (distance_pow_2 <= Math.pow(icon.getIconRadius() + icon.getIconRadius(), 2)) {
                return icon;
            }
        }

        return null;
    }


    protected Sticker findHandlingSticker() {
        for (int size = this.stickers.size() - 1; size >= 0; size--) {
            if (isInStickerArea(this.stickers.get(size), this.downX, this.downY)) {
                return this.stickers.get(size);
            }
        }
        return null;
    }

    protected boolean isInStickerArea(Sticker sticker, float f, float f2) {
        float[] fArr = this.tmp;
        fArr[0] = f;
        fArr[1] = f2;
        return sticker.contains(fArr);
    }

    protected PointF calculateMidPoint(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        this.midPoint.set((motionEvent.getX(0) + motionEvent.getX(1)) / 2.0f, (motionEvent.getY(0) + motionEvent.getY(1)) / 2.0f);
        return this.midPoint;
    }

    protected PointF calculateMidPoint() {
        Sticker sticker = this.handlingSticker;
        if (sticker == null) {
            this.midPoint.set(0.0f, 0.0f);
            return this.midPoint;
        }
        sticker.getMappedCenterPoint(this.midPoint, this.point, this.tmp);
        return this.midPoint;
    }

    protected float calculateRotation(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateRotation(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    protected float calculateRotation(float f, float f2, float f3, float f4) {
        return (float) Math.toDegrees(Math.atan2(f2 - f4, f - f3));
    }

    protected float calculateDistance(MotionEvent motionEvent) {
        if (motionEvent == null || motionEvent.getPointerCount() < 2) {
            return 0.0f;
        }
        return calculateDistance(motionEvent.getX(0), motionEvent.getY(0), motionEvent.getX(1), motionEvent.getY(1));
    }

    protected float calculateDistance(float f, float f2, float f3, float f4) {
        double d = f - f3;
        double d2 = f2 - f4;
        Double.isNaN(d);
        Double.isNaN(d);
        Double.isNaN(d2);
        Double.isNaN(d2);
        return (float) Math.sqrt((d * d) + (d2 * d2));
    }

    @Override 
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        for (int i5 = 0; i5 < this.stickers.size(); i5++) {
            Sticker sticker = this.stickers.get(i5);
            if (sticker != null) {
                transformSticker(sticker);
            }
        }
    }

    protected void transformSticker(Sticker sticker) {
        if (sticker == null) {
            Log.e(TAG, "transformSticker: the bitmapSticker is null or the bitmapSticker bitmap is null");
            return;
        }
        this.sizeMatrix.reset();
        float width = getWidth();
        float height = getHeight();
        float width2 = sticker.getWidth();
        float height2 = sticker.getHeight();
        this.sizeMatrix.postTranslate((width - width2) / 2.0f, (height - height2) / 2.0f);
        float f = (width < height ? width / width2 : height / height2) / 2.0f;
        this.sizeMatrix.postScale(f, f, width / 2.0f, height / 2.0f);
        sticker.getMatrix().reset();
        sticker.setMatrix(this.sizeMatrix);
        invalidate();
    }

    public void flipCurrentSticker(int i) {
        flip(this.handlingSticker, i);
    }

    public void flip(Sticker sticker, int i) {
        if (sticker != null) {
            sticker.getCenterPoint(this.midPoint);
            if ((i & 1) > 0) {
                sticker.getMatrix().preScale(-1.0f, 1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedHorizontally(!sticker.isFlippedHorizontally());
            }
            if ((i & 2) > 0) {
                sticker.getMatrix().preScale(1.0f, -1.0f, this.midPoint.x, this.midPoint.y);
                sticker.setFlippedVertically(!sticker.isFlippedVertically());
            }
            OnStickerOperationListener onStickerOperationListener = this.onStickerOperationListener;
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerFlipped(sticker);
            }
            invalidate();
        }
    }

    public boolean replace(Sticker sticker) {
        return replace(sticker, true);
    }

    public boolean replace(Sticker sticker, boolean z) {
        float intrinsicHeight;
        if (this.handlingSticker == null || sticker == null) {
            return false;
        }
        float width = getWidth();
        float height = getHeight();
        if (z) {
            sticker.setMatrix(this.handlingSticker.getMatrix());
            sticker.setFlippedVertically(this.handlingSticker.isFlippedVertically());
            sticker.setFlippedHorizontally(this.handlingSticker.isFlippedHorizontally());
        } else {
            this.handlingSticker.getMatrix().reset();
            sticker.getMatrix().postTranslate((width - this.handlingSticker.getWidth()) / 2.0f, (height - this.handlingSticker.getHeight()) / 2.0f);
            if (width < height) {
                intrinsicHeight = width / this.handlingSticker.getDrawable().getIntrinsicWidth();
            } else {
                intrinsicHeight = height / this.handlingSticker.getDrawable().getIntrinsicHeight();
            }
            float f = intrinsicHeight / 2.0f;
            sticker.getMatrix().postScale(f, f, width / 2.0f, height / 2.0f);
        }
        this.stickers.set(this.stickers.indexOf(this.handlingSticker), sticker);
        this.handlingSticker = sticker;
        invalidate();
        return true;
    }

    public boolean remove(Sticker sticker) {
        if (this.stickers.contains(sticker)) {
            this.stickers.remove(sticker);
            OnStickerOperationListener onStickerOperationListener = this.onStickerOperationListener;
            if (onStickerOperationListener != null) {
                onStickerOperationListener.onStickerDeleted(sticker);
            }
            if (this.handlingSticker == sticker) {
                this.handlingSticker = null;
            }
            invalidate();
            return true;
        }
        Log.d(TAG, "remove: the sticker is not in this StickerView");
        return false;
    }

    public boolean removeCurrentSticker() {
        return remove(this.handlingSticker);
    }

    public void removeAllStickers() {
        this.stickers.clear();
        Sticker sticker = this.handlingSticker;
        if (sticker != null) {
            sticker.release();
            this.handlingSticker = null;
        }
        invalidate();
    }

    public StickerView addSticker(Sticker sticker) {
        return addSticker(sticker, 1);
    }

    public StickerView addSticker(final Sticker sticker, final int i) {
        if (ViewCompat.isLaidOut(this)) {
            addStickerImmediately(sticker, i);
        } else {
            post(new Runnable() { 
                @Override 
                public void run() {
                    StickerView.this.addStickerImmediately(sticker, i);
                }
            });
        }
        return this;
    }

    protected void addStickerImmediately(@NonNull Sticker sticker, @Sticker.Position int position) {
        setStickerPosition(sticker, position);


        float scaleFactor, widthScaleFactor, heightScaleFactor;

        widthScaleFactor = (float) getWidth() / sticker.getDrawable().getIntrinsicWidth();
        heightScaleFactor = (float) getHeight() / sticker.getDrawable().getIntrinsicHeight();
        scaleFactor = widthScaleFactor > heightScaleFactor ? heightScaleFactor : widthScaleFactor;

        sticker.getMatrix()
                .postScale(scaleFactor / 2, scaleFactor / 2, getWidth() / 2, getHeight() / 2);

        handlingSticker = sticker;
        stickers.add(sticker);
        if (onStickerOperationListener != null) {
            onStickerOperationListener.onStickerAdded(sticker);
        }
        invalidate();
    }



    protected void setStickerPosition(Sticker sticker, int i) {
        float width = getWidth() - sticker.getWidth();
        float height = getHeight() - sticker.getHeight();
        sticker.getMatrix().postTranslate((i & 4) > 0 ? width / 4.0f : (i & 8) > 0 ? width * 0.75f : width / 2.0f, (i & 2) > 0 ? height / 4.0f : (i & 16) > 0 ? height * 0.75f : height / 2.0f);
    }

    public float[] getStickerPoints(Sticker sticker) {
        float[] fArr = new float[8];
        getStickerPoints(sticker, fArr);
        return fArr;
    }

    public void getStickerPoints(Sticker sticker, float[] fArr) {
        if (sticker == null) {
            Arrays.fill(fArr, 0.0f);
            return;
        }
        sticker.getBoundPoints(this.bounds);
        sticker.getMappedPoints(fArr, this.bounds);
    }

    public void save(File file) {
        try {
            StickerUtils.saveImageToGallery(file, createBitmap());
            StickerUtils.notifySystemGallery(getContext(), file);
        } catch (IllegalArgumentException | IllegalStateException unused) {
        }
    }

    public Bitmap createBitmap() throws OutOfMemoryError {
        this.handlingSticker = null;
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public int getStickerCount() {
        return this.stickers.size();
    }

    public boolean isNoneSticker() {
        return getStickerCount() == 0;
    }

    public boolean isLocked() {
        return this.locked;
    }

    public StickerView setLocked(boolean z) {
        this.locked = z;
        invalidate();
        return this;
    }

    public StickerView setMinClickDelayTime(int i) {
        this.minClickDelayTime = i;
        return this;
    }

    public int getMinClickDelayTime() {
        return this.minClickDelayTime;
    }

    public boolean isConstrained() {
        return this.constrained;
    }

    public StickerView setConstrained(boolean z) {
        this.constrained = z;
        postInvalidate();
        return this;
    }

    public StickerView setOnStickerOperationListener(OnStickerOperationListener onStickerOperationListener) {
        this.onStickerOperationListener = onStickerOperationListener;
        return this;
    }

    public OnStickerOperationListener getOnStickerOperationListener() {
        return this.onStickerOperationListener;
    }

    public Sticker getCurrentSticker() {
        return this.handlingSticker;
    }

    public List<BitmapStickerIcon> getIcons() {
        return this.icons;
    }

    public void setIcons(List<BitmapStickerIcon> list) {
        this.icons.clear();
        this.icons.addAll(list);
        invalidate();
    }
}
