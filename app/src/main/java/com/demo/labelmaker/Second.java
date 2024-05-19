package com.demo.labelmaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabLayout;
import com.soundcloud.android.crop.Crop;
import com.demo.labelmaker.sticker.CurveText;
import com.demo.labelmaker.sticker.Sticker;
import com.demo.labelmaker.sticker.StickerLayout;
import com.demo.labelmaker.sticker.StickerView;
import com.demo.labelmaker.sticker.TextSticker;

import java.io.File;

import com.demo.labelmaker.Adapters.GridRvAdapter;
import com.demo.labelmaker.Adapters.HorizontalRvAdapter;
import com.demo.labelmaker.AppDialogs.AppDialogs;
import com.demo.labelmaker.AppDialogs.BackgroundsDialog;
import com.demo.labelmaker.ItemTabs.ItemTab_1;
import com.demo.labelmaker.Utility.AppPermissions;
import com.demo.labelmaker.Utility.Constants;
import com.demo.labelmaker.Utility.SaveRasterImage;
import com.demo.labelmaker.Utility.UtilityFunctions;
import com.demo.labelmaker.colorpicker.LineColorPicker;
import com.demo.labelmaker.colorpicker.OnColorChangedListener;


public class Second extends AppCompatActivity implements View.OnClickListener, OnComponentAddedListener, StickerView.OnStickerOperationListener, OnComponentChangeListener, ItemTab_1.OnFragmentInteractionListener, SaveRasterImage.OnSaveComplete, OnColorChangedListener, HorizontalRvAdapter.HorizontalRvListener, GridRvAdapter.GridRvListener {
    private static final int SELECT_PICTURE_FROM_GALLERY = 907;

    BackgroundsDialog.setBgDialog backgroundDialog;
    RelativeLayout drawingCanvas;
    StickerLayout drawingSpace;
    TextView frameBtn;
    GridRvAdapter frameGridAdapter;
    TabLayout frameTabs;
    ImageView frameView;
    GridRvAdapter logoGridAdapter;
    TabLayout logoTabs;
    RecyclerView logosRv;
    CardView panelContainer;
    ViewFlipper panelHolder;
    TextView photoBtn;
    TextView saveBtn;
    Sticker selectedView;
    RecyclerView shapesRv;
    TextView tagsBtn;
    TextView textBtn;
    TextView textBtn1;
    TextView textBtn2;
    TextView textBtn3;
    TextView textBtn4;
    LineColorPicker textColorBar;
    ImageView textColorPicker;
    ViewFlipper textPanelHolder;
    int catPosition = 0;
    int itemPosition = 0;

    @Override 
    public void onFragmentInteraction(Uri uri) {
    }

    @Override 
    public void onStickerDeleted(Sticker sticker) {
    }

    @Override 
    public void onStickerDragFinished(Sticker sticker) {
    }

    @Override 
    public void onStickerFlipped(Sticker sticker) {
    }

    @Override 
    public void onStickerZoomFinished(Sticker sticker) {
    }

    
    @Override
    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_second);


        AdAdmob adAdmob = new AdAdmob( this);
        adAdmob.BannerAd((RelativeLayout) findViewById(R.id.banner), this);
        adAdmob.FullscreenAd_Counter(this);


        this.catPosition = getIntent().getIntExtra(Constants.CAT_KEY, 0);
        this.itemPosition = getIntent().getIntExtra(Constants.ITEM_KEY, 0);
        new UtilityFunctions().fullScreenActivity(this);
        initFunc();
    }

    public void initFunc() {
        this.panelContainer = (CardView) findViewById(R.id.panel_container);
        this.panelHolder = (ViewFlipper) findViewById(R.id.panel_holder);
        this.textPanelHolder = (ViewFlipper) findViewById(R.id.text_panel_holder);
        this.frameBtn = (TextView) findViewById(R.id.frame_btn);
        this.tagsBtn = (TextView) findViewById(R.id.tags_btn);
        this.textBtn = (TextView) findViewById(R.id.text_btn);
        this.saveBtn = (TextView) findViewById(R.id.save_btn);
        this.textBtn1 = (TextView) findViewById(R.id.text_btn_1);
        this.textBtn2 = (TextView) findViewById(R.id.text_btn_2);
        this.textBtn3 = (TextView) findViewById(R.id.text_btn_3);
        this.textBtn4 = (TextView) findViewById(R.id.text_btn_4);
        this.frameTabs = (TabLayout) findViewById(R.id.tab_layout);
        this.logoTabs = (TabLayout) findViewById(R.id.logo_tabs);
        this.textColorBar = (LineColorPicker) findViewById(R.id.text_color_bar);
        ImageView imageView = (ImageView) findViewById(R.id.text_color_picker);
        this.textColorPicker = imageView;
        imageView.setOnClickListener(this);
        this.textColorBar.setOnColorChangedListener(this);
        this.frameBtn.setOnClickListener(this);
        this.tagsBtn.setOnClickListener(this);
        this.textBtn.setOnClickListener(this);
        this.saveBtn.setOnClickListener(this);
        this.textBtn1.setOnClickListener(this);
        this.textBtn2.setOnClickListener(this);
        this.textBtn3.setOnClickListener(this);
        this.textBtn4.setOnClickListener(this);
        this.shapesRv = (RecyclerView) findViewById(R.id.shapes_rv);
        this.logosRv = (RecyclerView) findViewById(R.id.logos_rv);
        this.frameView = (ImageView) findViewById(R.id.frame_view);
        this.drawingSpace = (StickerLayout) findViewById(R.id.drawing_space);
        this.drawingCanvas = (RelativeLayout) findViewById(R.id.drawing_canvas);
        this.drawingSpace.setOnStickerOperationListener(this);
        this.drawingCanvas.setOnClickListener(new View.OnClickListener() { 
            @Override 
            public void onClick(View view) {
                Second.this.drawingSpace.setStickerIcons(null, null, null, null);
                Second.this.drawingSpace.invalidate();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {


        } else {
            AppPermissions appPermissions = new AppPermissions(this);
            appPermissions.checkForReadPermission();
            appPermissions.checkForWritePermission();
        }


        this.frameView.setImageBitmap(BitmapFactory.decodeResource(getResources(), Constants.ALL_FRAMES[this.catPosition][this.itemPosition]));
        runOnUiThread(new Runnable() { 
            @Override 
            public void run() {
                Second.this.setAllAdapters();
            }
        });
    }

    
    @Override 
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.frame_btn :
            case R.id.tags_btn :
                break;
            case R.id.save_btn :
                saveImage();
                return;
            case R.id.text_color_picker :
                AppDialogs.AddColorDialog addColorDialog = new AppDialogs.AddColorDialog(this);
                addColorDialog.setOnColorChangeListener(this);
                addColorDialog.show();
                return;
            default:
                switch (id) {
                    case R.id.text_btn :
                        break;
                    case R.id.text_btn_1 :
                    case R.id.text_btn_2 :
                        textPanelChanger(Integer.parseInt(view.getTag().toString()));
                        return;
                    case R.id.text_btn_3 :
                        AppDialogs.AddFontDialog addFontDialog = new AppDialogs.AddFontDialog(this);
                        addFontDialog.setOnFontAddedListener(this);
                        addFontDialog.show();
                        return;
                    case R.id.text_btn_4 :
                        AppDialogs.AddTextDialog addTextDialog = new AppDialogs.AddTextDialog(this, null);
                        addTextDialog.setOnTextAddedListener(this);
                        addTextDialog.show();
                        return;
                    default:
                        return;
                }
        }
        panelChanger(Integer.parseInt(view.getTag().toString()));
    }

    private void panelChanger(int i) {
        this.panelHolder.setDisplayedChild(i);
    }

    private void textPanelChanger(int i) {
        this.textPanelHolder.setDisplayedChild(i);
    }

    private void pickFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.PICK");
        startActivityForResult(Intent.createChooser(intent, "Choose Image From Gallery"), SELECT_PICTURE_FROM_GALLERY);
    }

    public void saveImage() {
        this.frameView.setBackgroundResource(0);
        this.drawingSpace.setLocked(true);
        new SaveRasterImage(this, UtilityFunctions.getBitmapFromView(this.drawingSpace), SaveRasterImage.SHARE_STATES.YES_SHARE).setOnSaveComplete(this);
    }

    
    @Override 
    public void onPause() {
        this.drawingSpace.setLocked(true);
        super.onPause();
    }

    
    @Override
    
    public void onStop() {
        this.drawingSpace.setLocked(true);
        super.onStop();
    }

    
    @Override 
    public void onResume() {
        this.drawingSpace.setLocked(false);
        super.onResume();
    }

    
    public void setAllAdapters() {
        new HorizontalRvAdapter(this, Constants.TEXT_PATTERN, (RecyclerView) findViewById(R.id.recycleViewPtr)).setHorizontalRvListener(this);
        GridRvAdapter gridRvAdapter = new GridRvAdapter(this, this.shapesRv);
        this.frameGridAdapter = gridRvAdapter;
        gridRvAdapter.setGridRvListener(this);
        this.frameGridAdapter.setNameList(Constants.ALL_FRAMES[this.catPosition]);
        this.frameGridAdapter.setAdapter();
        this.frameGridAdapter.setRecyclerViewItemListener();
        GridRvAdapter gridRvAdapter2 = new GridRvAdapter(this, this.logosRv);
        this.logoGridAdapter = gridRvAdapter2;
        gridRvAdapter2.setGridRvListener(this);
        this.logoGridAdapter.setNameList(Constants.ALL_LOGOS[this.catPosition]);
        this.logoGridAdapter.setAdapter();
        this.logoGridAdapter.setRecyclerViewItemListener();
        setCategoryTabs();
    }

    private void setCategoryTabs() {
        String[] strArr;
        String[] strArr2;
        this.frameTabs.setInlineLabel(true);
        this.frameTabs.setHorizontalScrollBarEnabled(true);
        for (String str : Constants.All_FRAMES_TITLE) {
            TabLayout tabLayout = this.frameTabs;
            tabLayout.addTab(tabLayout.newTab().setText(str));
        }
        this.frameTabs.setTabGravity(0);
        this.frameTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { 
            @Override 
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override 
            public void onTabSelected(TabLayout.Tab tab) {
                Second.this.frameGridAdapter.setNameList(Constants.ALL_FRAMES[tab.getPosition()]);
                Second.this.frameGridAdapter.setAdapter();
            }

            @Override 
            public void onTabReselected(TabLayout.Tab tab) {
                Second.this.frameGridAdapter.setNameList(Constants.ALL_FRAMES[tab.getPosition()]);
                Second.this.frameGridAdapter.setAdapter();
            }
        });
        this.logoTabs.setInlineLabel(true);
        this.logoTabs.setHorizontalScrollBarEnabled(true);
        for (String str2 : Constants.All_FRAMES_TITLE) {
            TabLayout tabLayout2 = this.logoTabs;
            tabLayout2.addTab(tabLayout2.newTab().setText(str2));
        }
        this.logoTabs.setTabGravity(0);
        this.logoTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() { 
            @Override 
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override 
            public void onTabSelected(TabLayout.Tab tab) {
                Second.this.logoGridAdapter.setNameList(Constants.ALL_LOGOS[tab.getPosition()]);
                Second.this.logoGridAdapter.setAdapter();
            }

            @Override 
            public void onTabReselected(TabLayout.Tab tab) {
                Second.this.logoGridAdapter.setNameList(Constants.ALL_LOGOS[tab.getPosition()]);
                Second.this.logoGridAdapter.setAdapter();
            }
        });
        TabLayout tabLayout3 = this.logoTabs;
        tabLayout3.selectTab(tabLayout3.getTabAt(this.catPosition));
    }

    
    @Override
    
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == SELECT_PICTURE_FROM_GALLERY) {
            beginCrop(intent.getData());
        }
        if (i == 9162 || i == 6709) {
            if (i == 9162 && i2 == -1) {
                beginCrop(intent.getData());
            } else if (i == 6709) {
                handleCrop(i2, intent);
            }
        }
    }

    private void beginCrop(Uri uri) {
        Crop.of(uri, Uri.fromFile(new File(getCacheDir(), "cropped"))).asSquare().start(this);
    }

    private void handleCrop(int i, Intent intent) {
        if (i == -1) {
            this.drawingSpace.addImageSticker(BitmapFactory.decodeFile(Crop.getOutput(intent).getPath()));
        } else if (i == 404) {
            Toast.makeText(this, Crop.getError(intent).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override 
    public void onSaveCompleted(Uri uri, Enum r3) {
        this.drawingSpace.setLocked(false);
        this.frameView.setBackgroundResource(R.drawable.trans_img);
        Intent intent = new Intent(this, ShowSaved.class);
        intent.putExtra(Constants.SHARE_KEY, uri.toString());
        startActivity(intent);
    }

    @Override 
    public void onAnyItemAdded(int i, int i2) {
        this.drawingSpace.addImageSticker(BitmapFactory.decodeResource(getResources(), Constants.ALL_FRAMES[i][i2]));
        this.backgroundDialog.dismiss();
    }

    @Override 
    public void onTextSelected(String str, Boolean bool) {
        if (bool.booleanValue()) {
            Sticker sticker = this.selectedView;
            if (sticker instanceof TextSticker) {
                ((TextSticker) sticker).replaceTextSticker(sticker, this.drawingSpace, str);
                return;
            }
            return;
        }
        this.drawingSpace.addTextSticker(str);
        Toast.makeText(this, "Double Tap To Edit", Toast.LENGTH_SHORT).show();
    }

    @Override 
    public void onCurveTextSelected(String str, Boolean bool) {
        if (bool.booleanValue()) {
            Sticker sticker = this.selectedView;
            if (sticker instanceof TextSticker) {
                ((TextSticker) sticker).replaceTextSticker(sticker, this.drawingSpace, str);
                return;
            }
            return;
        }
        this.drawingSpace.addCurveTextSticker(str);
        Toast.makeText(this, "Double Tap To Edit", Toast.LENGTH_SHORT).show();
    }

    @Override 
    public void onFontSelected(Typeface typeface) {
        Sticker sticker = this.selectedView;
        if (sticker != null && (sticker instanceof TextSticker)) {
            ((TextSticker) sticker).setTypeface(typeface);
            this.drawingSpace.replace(this.selectedView);
        } else if (sticker == null || !(sticker instanceof CurveText)) {
        } else {
            ((CurveText) sticker).setTypeface(typeface);
            this.drawingSpace.replace(this.selectedView);
        }
    }

    @Override 
    public void onStickerAdded(Sticker sticker) {
        this.selectedView = sticker;
    }

    @Override 
    public void onStickerClicked(Sticker sticker) {
        this.selectedView = sticker;
    }

    @Override 
    public void onStickerDoubleTapped(Sticker sticker) {
        Sticker sticker2 = this.selectedView;
        if (sticker2 == null || !(sticker2 instanceof TextSticker)) {
            return;
        }
        AppDialogs.AddTextDialog addTextDialog = new AppDialogs.AddTextDialog(this, ((TextSticker) sticker).getText());
        addTextDialog.setOnTextAddedListener(this);
        addTextDialog.show();
    }

    @Override
    
    public void onColorChanged(int i) {
        Sticker sticker = this.selectedView;
        if (sticker instanceof TextSticker) {
            ((TextSticker) sticker).setTextColor(i);
            this.drawingSpace.replace(this.selectedView);
        } else if (sticker instanceof CurveText) {
            ((CurveText) sticker).setTextColor(i);
            this.drawingSpace.replace(this.selectedView);
        } else {
            Toast.makeText(this, "Select Text First !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override 
    public void onColorChangedWithView(int i, View view) {
        Sticker sticker = this.selectedView;
        if (sticker instanceof TextSticker) {
            ((TextSticker) sticker).setTextColor(i);
        } else if (sticker instanceof CurveText) {
            ((CurveText) sticker).setTextColor(i);
        }
    }

    @Override 
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Unsaved Data Will Be Lost");
        builder.setNeutralButton("Rate Us", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                Second.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + Second.this.getPackageName())));
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                Second.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() { 
            @Override 
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override 
    public void onItemClicked(int i, RecyclerView recyclerView) {
        Sticker sticker = this.selectedView;
        if (sticker != null && (sticker instanceof TextSticker)) {
            ((TextSticker) this.selectedView).setTextImage(BitmapFactory.decodeResource(getResources(), i));
            this.drawingSpace.replace(this.selectedView);
        } else if (sticker != null && (sticker instanceof CurveText)) {
            ((CurveText) this.selectedView).setTextImage(BitmapFactory.decodeResource(getResources(), i));
            this.drawingSpace.replace(this.selectedView);
        } else {
            Toast.makeText(getApplicationContext(), "Select Text First !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override 
    public void onGirdItemClicked(int i, int i2, RecyclerView recyclerView) {
        int id = recyclerView.getId();
        if (id == R.id.logos_rv) {


            Log.e("cccccc", "" + BitmapFactory.decodeResource(getResources(), i));
            this.drawingSpace.addImageSticker(BitmapFactory.decodeResource(getResources(), i));
        } else if (id == R.id.shapes_rv) {
            this.frameView.setImageBitmap(BitmapFactory.decodeResource(getResources(), i));
        }

    }
}
