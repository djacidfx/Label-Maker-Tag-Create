package com.demo.labelmaker.Utility;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.WebView;
import android.widget.Toast;
import java.io.IOException;


public class SetWallpaper extends AsyncTask<Void, Void, Bitmap> {
    Bitmap bitmap;
    boolean checker = true;
    Context context;
    String currentItem;
    ProgressDialog dialog;
    boolean screenChecker;
    WebView webView;

    @Override 
    protected void onPreExecute() {
    }

    public SetWallpaper(Context context, WebView webView, boolean z, ProgressDialog progressDialog) {
        this.context = context;
        this.webView = webView;
        this.screenChecker = z;
        this.dialog = progressDialog;
    }

    public SetWallpaper(Context context, String str, Boolean bool, ProgressDialog progressDialog) {
        this.context = context;
        this.currentItem = str;
        this.screenChecker = bool.booleanValue();
        this.dialog = progressDialog;
    }

    
    @Override 
    public Bitmap doInBackground(Void... voidArr) {
        if (this.checker) {
            this.webView.setDrawingCacheEnabled(true);
            this.webView.buildDrawingCache();
            this.bitmap = this.webView.getDrawingCache();
        } else {
            this.bitmap = BitmapFactory.decodeFile(this.currentItem);
        }
        Bitmap resizedBitmap = UtilityFunctions.getResizedBitmap(this.bitmap, Resources.getSystem().getDisplayMetrics().widthPixels, Resources.getSystem().getDisplayMetrics().heightPixels);
        this.bitmap = resizedBitmap;
        return resizedBitmap;
    }

    
    @Override 
    public void onPostExecute(Bitmap bitmap) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this.context);
        if (this.screenChecker) {
            homeScreen(wallpaperManager, bitmap);
            return;
        }
        try {
            lockScreen(wallpaperManager, bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void homeScreen(WallpaperManager wallpaperManager, Bitmap bitmap) {
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this.context, Constants.WallChanged, 1).show();
            this.dialog.dismiss();
            if (this.checker) {
                this.webView.destroyDrawingCache();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void lockScreen(WallpaperManager wallpaperManager, Bitmap bitmap) throws IOException {
        if (Build.VERSION.SDK_INT >= 24) {
            wallpaperManager.setBitmap(bitmap, null, true, 2);
            Toast.makeText(this.context, Constants.WallChanged, 1).show();
        } else {
            Toast.makeText(this.context, Constants.WallNotGetted, 0).show();
        }
        this.dialog.dismiss();
    }
}
