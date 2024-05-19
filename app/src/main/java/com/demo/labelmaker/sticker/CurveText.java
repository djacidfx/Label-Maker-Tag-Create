package com.demo.labelmaker.sticker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import androidx.core.content.ContextCompat;

import com.demo.labelmaker.R;


public class CurveText extends Sticker {
    private static final String mEllipsis = "…";
    private Layout.Alignment alignment;
    private int centerX;
    private int centerY;
    private Path circle;
    private final Context context;
    TextPaint curveBase;
    TextPaint curvePaint;
    private Drawable drawable;
    private float lineSpacingExtra;
    private float lineSpacingMultiplier;
    private int mTextHeight;
    private int mTextWidth;
    private float maxTextSizePixels;
    private float minTextSizePixels;
    private float radius;
    private final Rect realBounds;
    private StaticLayout staticLayout;
    private String text;
    private final TextPaint textPaint;
    private final Rect textRect;

    public CurveText(Context context) {
        this(context, null);
        this.circle = new Path();
    }

    public CurveText(Context context, Drawable drawable) {
        this.lineSpacingMultiplier = 1.0f;
        this.lineSpacingExtra = 0.0f;
        this.context = context;
        this.drawable = drawable;
        if (drawable == null) {
            this.drawable = ContextCompat.getDrawable(context, R.drawable.sticker_transparent_background);
        }
        TextPaint textPaint = new TextPaint(1);
        this.textPaint = textPaint;
        textPaint.setColor(0);
        TextPaint textPaint2 = new TextPaint(1);
        this.curveBase = textPaint2;
        textPaint2.setColor(0);
        this.curvePaint = new TextPaint(1);
        this.realBounds = new Rect(0, 0, getWidth(), getHeight());
        this.textRect = new Rect(0, 0, getWidth(), getHeight());
        this.minTextSizePixels = convertSpToPx(6.0f);
        this.maxTextSizePixels = convertSpToPx(22.0f);
        this.alignment = Layout.Alignment.ALIGN_CENTER;
        textPaint.setTextSize(this.minTextSizePixels);
        this.curvePaint.setTextSize(this.maxTextSizePixels);
    }

    @Override 
    public void draw(Canvas canvas) {
        Matrix matrix = getMatrix();
        canvas.save();
        canvas.concat(matrix);
        Drawable drawable = this.drawable;
        if (drawable != null) {
            drawable.setBounds(this.realBounds);
            this.drawable.draw(canvas);
        }
        canvas.restore();
        canvas.save();
        canvas.concat(matrix);
        if (this.textRect.width() == getWidth()) {
            canvas.translate(0.0f, (getHeight() / 2) - (this.staticLayout.getHeight() / 2));
        } else {
            canvas.translate(this.textRect.left, (this.textRect.top + (this.textRect.height() / 2)) - (this.staticLayout.getHeight() / 2));
        }
        canvas.rotate(180.0f, this.textRect.width() / 2, this.textRect.height() / 2);
        canvas.drawCircle(this.textRect.centerX(), this.textRect.centerY(), this.radius, this.curveBase);
        this.circle.addCircle(this.textRect.centerX(), this.textRect.centerY(), this.radius, Path.Direction.CW);
        canvas.drawTextOnPath(this.text, this.circle, 0.0f, 0.0f, this.curvePaint);
        this.staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override 
    public int getWidth() {
        return this.drawable.getIntrinsicWidth();
    }

    @Override 
    public int getHeight() {
        return this.drawable.getIntrinsicHeight();
    }

    @Override 
    public void release() {
        super.release();
        if (this.drawable != null) {
            this.drawable = null;
        }
    }

    @Override 
    public CurveText setAlpha(int i) {
        this.curvePaint.setAlpha(i);
        return this;
    }

    @Override 
    public Drawable getDrawable() {
        return this.drawable;
    }

    @Override 
    public CurveText setDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.realBounds.set(0, 0, getWidth(), getHeight());
        this.textRect.set(0, 0, getWidth(), getHeight());
        return this;
    }

    public CurveText setDrawable(Drawable drawable, Rect rect) {
        this.drawable = drawable;
        this.realBounds.set(0, 0, getWidth(), getHeight());
        if (rect == null) {
            this.textRect.set(0, 0, getWidth(), getHeight());
        } else {
            this.textRect.set(rect.left, rect.top, rect.right, rect.bottom);
        }
        return this;
    }

    public CurveText setTypeface(Typeface typeface) {
        this.curvePaint.setTypeface(typeface);
        return this;
    }

    public Typeface getTypeFace() {
        return this.curvePaint.getTypeface();
    }

    public CurveText setTextColor(int i) {
        this.curvePaint.setShader(null);
        this.curvePaint.setColor(i);
        return this;
    }

    public int getTextColor() {
        return this.curvePaint.getColor();
    }

    public CurveText setTextAlign(Layout.Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public CurveText setTextImage(Bitmap bitmap) {
        this.curvePaint.setShader(new BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        return this;
    }

    public CurveText borderStroke() {
        this.curvePaint.setStyle(Paint.Style.STROKE);
        this.curvePaint.setStrokeWidth(2.0f);
        return this;
    }

    public CurveText setMaxTextSize(float f) {
        this.curvePaint.setTextSize(convertSpToPx(f));
        this.maxTextSizePixels = this.curvePaint.getTextSize();
        return this;
    }

    public CurveText setMinTextSize(float f) {
        this.minTextSizePixels = convertSpToPx(f);
        return this;
    }

    public CurveText setLineSpacing(float f, float f2) {
        this.lineSpacingMultiplier = f2;
        this.lineSpacingExtra = f;
        return this;
    }

    public CurveText setText(String str) {
        this.text = str;
        this.curvePaint.getTextBounds(str, 0, str.length(), this.realBounds);
        this.mTextWidth = Math.round(this.curvePaint.measureText(str.toString()));
        this.mTextHeight = this.realBounds.height();
        double d = this.mTextWidth;
        Double.isNaN(d);
        this.radius = (float) (d / 3.141592653589793d);
        return this;
    }

    public CurveText setText(String str, int i) {
        this.text = str;
        this.curvePaint.getTextBounds(str, 0, str.length(), this.realBounds);
        this.mTextWidth = Math.round(this.curvePaint.measureText(str.toString()));
        this.mTextHeight = this.realBounds.height() - i;
        double d = this.mTextWidth;
        Double.isNaN(d);
        this.radius = (float) (d / 3.141592653589793d);
        return this;
    }

    public String getText() {
        return this.text;
    }

    public CurveText resizeText() {
        int height = this.textRect.height();
        int width = this.textRect.width();
        String text = getText();
        if (text != null && text.length() > 0 && height > 0 && width > 0) {
            float f = this.maxTextSizePixels;
            if (f > 0.0f) {
                int textHeightPixels = getTextHeightPixels(text, width, f);
                float f2 = f;
                while (textHeightPixels > height) {
                    float f3 = this.minTextSizePixels;
                    if (f2 <= f3) {
                        break;
                    }
                    f2 = Math.max(f2 - 2.0f, f3);
                    textHeightPixels = getTextHeightPixels(text, width, f2);
                }
                if (f2 == this.minTextSizePixels && textHeightPixels > height) {
                    TextPaint textPaint = new TextPaint(this.curvePaint);
                    textPaint.setTextSize(f2);
                    StaticLayout staticLayout = new StaticLayout(text, textPaint, width, Layout.Alignment.ALIGN_NORMAL, this.lineSpacingMultiplier, this.lineSpacingExtra, false);
                    if (staticLayout.getLineCount() > 0 && staticLayout.getLineForVertical(height) - 1 >= 0) {

                        int lastLine = staticLayout.getLineForVertical(height) - 1;
                        int lineStart = staticLayout.getLineStart(lastLine);
                        int lineEnd = staticLayout.getLineEnd(lastLine);
                        float lineWidth = staticLayout.getLineWidth(lastLine);
                        float measureText = textPaint.measureText(mEllipsis);
                        while (width < lineWidth + measureText) {
                            lineEnd--;
                            lineWidth = textPaint.measureText(text.subSequence(lineStart, lineEnd + 1).toString());
                        }
                        setText(((Object) text.subSequence(0, lineEnd)) + mEllipsis);
                    }
                }
                this.curvePaint.setTextSize(f2);
                this.staticLayout = new StaticLayout(this.text, this.textPaint, this.textRect.width(), this.alignment, this.lineSpacingMultiplier, this.lineSpacingExtra, true);
            }
        }
        return this;
    }

    public float getMinTextSizePixels() {
        return this.minTextSizePixels;
    }

    protected int getTextHeightPixels(CharSequence charSequence, int i, float f) {
        this.curvePaint.setTextSize(f);
        return new StaticLayout(charSequence, this.curvePaint, i, Layout.Alignment.ALIGN_NORMAL, this.lineSpacingMultiplier, this.lineSpacingExtra, true).getHeight();
    }

    private float convertSpToPx(float f) {
        return f * this.context.getResources().getDisplayMetrics().scaledDensity;
    }
}
