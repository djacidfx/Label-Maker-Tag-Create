package com.demo.labelmaker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.demo.labelmaker.Adapters.LibraryAdapter;
import com.demo.labelmaker.Adapters.RecyclerItemClickListener;
import com.demo.labelmaker.Utility.Constants;
import com.demo.labelmaker.Utility.GetSavedFiles;
import com.demo.labelmaker.Utility.UtilityFunctions;


public class MyText extends AppCompatActivity {
    ArrayList<Uri> libraryFiles;
    RecyclerView mRecyclerView;
    TextView textView;

    
    @Override 
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_my_text);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd_Counter(this);


        new UtilityFunctions().fullScreenActivity(this);
        this.textView = (TextView) findViewById(R.id.no_image);
        ArrayList<Uri> GetAllSavedFiles = new GetSavedFiles(this).GetAllSavedFiles();
        this.libraryFiles = GetAllSavedFiles;
        if (GetAllSavedFiles != null && GetAllSavedFiles.size() > 0) {
            this.textView.setVisibility(View.GONE);
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        this.mRecyclerView = recyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        this.mRecyclerView.setAdapter(new LibraryAdapter(this.libraryFiles, this));
        this.mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() { 
            @Override 
            public void onItemClick(View view, int i) {
                MyText.this.openShareActivity(i);
            }
        }));
    }

    
    @Override 
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ArrayList<Uri> GetAllSavedFiles = new GetSavedFiles(this).GetAllSavedFiles();
        this.libraryFiles = GetAllSavedFiles;
        this.mRecyclerView.setAdapter(new LibraryAdapter(GetAllSavedFiles, this));
        if (this.libraryFiles.size() < 1) {
            this.textView.setVisibility(View.VISIBLE);
        }
    }

    public void openShareActivity(int i) {
        Intent intent = new Intent(this, ShareActivity.class);
        intent.putExtra(Constants.IMG_ID, i);
        startActivityForResult(intent, 444);
    }
}
