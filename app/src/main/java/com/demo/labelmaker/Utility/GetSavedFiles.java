package com.demo.labelmaker.Utility;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;


public class GetSavedFiles {
    private Context context;
    private ArrayList<Uri> libraryFiles = new ArrayList<>();

    public GetSavedFiles(Context context) {
        this.context = context;
    }

    public ArrayList<Uri> GetAllSavedFiles() {
        if (Build.VERSION.SDK_INT >= 29) {
            ArrayList<Uri> imagesFromDevice = getImagesFromDevice(this.context);
            this.libraryFiles = imagesFromDevice;
            return imagesFromDevice;
        }
        File[] listFiles = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath() + File.separator + Constants.FOLDER_NAME).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                this.libraryFiles.add(Uri.parse(file.getAbsolutePath()));
            }
        }
        return this.libraryFiles;
    }

    private ArrayList<Uri> getImagesFromDevice(Context context) {
        ArrayList<Uri> arrayList = new ArrayList<>();
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_id"}, "_data like ? ", new String[]{"%LABEL_MAKER_SAVED%"}, null);
        if (query != null) {
            while (query.moveToNext()) {
                query.getString(0);
                Uri withAppendedId = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, query.getInt(1));
                Log.e("Path :", "" + withAppendedId);
                arrayList.add(withAppendedId);
            }
            query.close();
        }
        return arrayList;
    }
}
