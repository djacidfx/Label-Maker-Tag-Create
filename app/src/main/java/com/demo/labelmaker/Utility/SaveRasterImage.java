package com.demo.labelmaker.Utility;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class SaveRasterImage {
    static final  boolean $assertionsDisabled = false;
    private Context mContext;
    private OnSaveComplete onSaveComplete;
    private Enum shareState;

    
    public interface OnSaveComplete {
        void onSaveCompleted(Uri uri, Enum r2);
    }

    
    public enum SHARE_STATES {
        YES_SHARE,
        NOT_SHARE
    }

    public SaveRasterImage(Context context, Bitmap bitmap, Enum r3) {
        this.mContext = context;
        this.shareState = r3;
        try {
            saveImageInSdCard(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private void saveImageInSdCard(final Bitmap bitmap) throws IOException {
        new AsyncTask<Void, Void, Uri>() { 
            private ProgressDialog dialog;

            @Override 
            protected void onPreExecute() {
                ProgressDialog progressDialog = new ProgressDialog(SaveRasterImage.this.mContext);
                this.dialog = progressDialog;
                progressDialog.requestWindowFeature(1);
                this.dialog.setMessage("Saving In Progress");
                this.dialog.show();
            }

            
            @Override 
            public Uri doInBackground(Void... voidArr) {
                return Build.VERSION.SDK_INT >= 29 ? SaveRasterImage.this.getUriFromAndroidQ(bitmap) : SaveRasterImage.this.getUriFromAndroidQBelow(bitmap);
            }

            
            @Override 
            public void onPostExecute(Uri uri) {
                this.dialog.dismiss();
                SaveRasterImage.this.mContext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", uri));
                SaveRasterImage.this.onSaveComplete.onSaveCompleted(uri, SaveRasterImage.this.shareState);
            }
        }.execute(new Void[0]);
    }

    
    public Uri getUriFromAndroidQ(Bitmap bitmap) {
        ContentResolver contentResolver = this.mContext.getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", System.currentTimeMillis() + ".PNG");
        contentValues.put("mime_type", "image/png");
        contentValues.put("relative_path", Environment.DIRECTORY_PICTURES + File.separator + Constants.FOLDER_NAME);
        Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Log.e("file_uri", insert.toString());
        try {
            OutputStream openOutputStream = contentResolver.openOutputStream(insert);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, openOutputStream);
            if (openOutputStream != null) {
                openOutputStream.flush();
                openOutputStream.close();
            }
        } catch (FileNotFoundException unused) {
            Log.e("ERROR: ", " FIle Not Found");
        } catch (IOException e) {
            Log.e("ERROR: ", "Unknown ERRROR :(");
            e.printStackTrace();
        }
        return insert;
    }

    
    public Uri getUriFromAndroidQBelow(Bitmap bitmap) {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + Constants.FOLDER_NAME);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(file, System.currentTimeMillis() + Constants.PNG_EXE);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file2);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException unused) {
            Log.e("ERROR: ", " FIle Not Found");
        } catch (IOException e) {
            Log.e("ERROR: ", "Unknown ERRROR :(");
            e.printStackTrace();
        }
        return Uri.fromFile(file2);
    }

    public void setOnSaveComplete(OnSaveComplete onSaveComplete) {
        this.onSaveComplete = onSaveComplete;
    }
}
