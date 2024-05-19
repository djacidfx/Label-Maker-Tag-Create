package com.demo.labelmaker.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class UtilityFunctions {
    public static Drawable getDrawableFromAssets(Context context, String str) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(str);
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = null;
        }
        return Drawable.createFromStream(inputStream, null);
    }

    public static Bitmap loadBitmapFromAssets(Context context, String str, int i) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            int min = Math.min(i / 25, i / 25);
            options.inJustDecodeBounds = false;
            options.inSampleSize = min;
            options.inPurgeable = true;
            return BitmapFactory.decodeStream(context.getAssets().open(str), null, options);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap justLoadBitmap(Context context, String str) {
        try {
            return BitmapFactory.decodeStream(context.getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fullScreenActivity(Context context) {
        ((Activity) context).getWindow().setFlags(1024, 1024);
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap getScaledBitmap(int i, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), i, options);
        int min = Math.min(options.outWidth / 50, options.outHeight / 50);
        options.inJustDecodeBounds = false;
        options.inSampleSize = min;
        options.inPurgeable = true;
        return BitmapFactory.decodeResource(context.getResources(), i, options);
    }

    public static Bitmap getScaledBitmap(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int min = Math.min(options.outWidth / 80, options.outHeight / 80);
        options.inJustDecodeBounds = false;
        options.inSampleSize = min;
        options.inPurgeable = true;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static Bitmap getResizedBitmap(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(i / width, i2 / height);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
    }

    public static boolean deleteLogoFile(Uri uri, Context context) {
        boolean z;
        try {
            File file = new File(uri.getPath());
            z = file.delete();
            try {
                if (file.exists()) {
                    try {
                        z = file.getCanonicalFile().delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (file.exists()) {
                        z = context.deleteFile(file.getName());
                    }
                }
                context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file)));
            } catch (Exception unused) {
                Toast.makeText(context, "Something Went Wrong ):", 0).show();
                return z;
            }
        } catch (Exception unused2) {
            z = false;
        }
        return z;
    }
}
