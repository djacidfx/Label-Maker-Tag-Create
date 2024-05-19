package com.demo.labelmaker.AppDialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.demo.labelmaker.R;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.demo.labelmaker.Adapters.listAdapter;
import com.demo.labelmaker.OnComponentAddedListener;
import com.demo.labelmaker.OnComponentChangeListener;
import com.demo.labelmaker.Second;
import com.demo.labelmaker.Utility.Constants;


public class AppDialogs {

    
    public static class AddTextDialog extends Dialog {
        EditText editText;
        Boolean editedText;
        OnComponentAddedListener onTextAddedListener;

        public AddTextDialog(final Context context, String str) {
            super(context);
            this.editedText = false;
            requestWindowFeature(1);
            setContentView(R.layout.text_input_layout);
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            this.editText = (EditText) findViewById(R.id.input);
            if (str != null) {
                this.editedText = true;
                this.editText.setText(str);
            }
            ((ImageView) findViewById(R.id.cancelBtn)).setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    AddTextDialog.this.dismiss();
                }
            });
            ((ImageView) findViewById(R.id.curveTextBtn)).setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    String obj = AddTextDialog.this.editText.getText().toString();
                    if (obj.length() < 1) {
                        Toast.makeText(context, "Enter Text .....", Toast.LENGTH_SHORT).show();
                    } else if (obj.length() >= 1) {
                        AddTextDialog.this.onTextAddedListener.onCurveTextSelected(obj, AddTextDialog.this.editedText);
                        AddTextDialog.this.dismiss();
                    }
                }
            });
            ((ImageView) findViewById(R.id.enterTextBtn)).setOnClickListener(new View.OnClickListener() { 
                @Override 
                public void onClick(View view) {
                    String obj = AddTextDialog.this.editText.getText().toString();
                    if (obj.length() < 1) {
                        Toast.makeText(context, "Enter Text .....", Toast.LENGTH_SHORT).show();
                    } else if (obj.length() >= 1) {
                        AddTextDialog.this.onTextAddedListener.onTextSelected(obj, AddTextDialog.this.editedText);
                        AddTextDialog.this.dismiss();
                    }
                }
            });
        }

        public void setOnTextAddedListener(Second second) {
            this.onTextAddedListener = second;
        }
    }

    
    public static class AddFontDialog extends Dialog {
        ListView listView;
        OnComponentAddedListener onFontAddedListener;

        public AddFontDialog(Context context) {
            super(context);
            requestWindowFeature(1);
            setContentView(R.layout.font_style_lay);
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            ListView listView = (ListView) findViewById(R.id.listView);
            this.listView = listView;
            listView.setAdapter((ListAdapter) new listAdapter(getContext(), Constants.FONTS));
            this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { 
                @Override 
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                    AddFontDialog.this.onFontAddedListener.onFontSelected(Typeface.createFromAsset(AddFontDialog.this.getContext().getAssets(), Constants.FONTS[i]));
                    AddFontDialog.this.dismiss();
                }
            });
        }

        public void setOnFontAddedListener(Second second) {
            this.onFontAddedListener = second;
        }
    }

    
    public static class AddColorDialog extends Dialog implements ColorPicker.OnColorChangedListener, View.OnClickListener {
        OnComponentChangeListener onComponentChangeListener;

        public AddColorDialog(Context context) {
            super(context);
            requestWindowFeature(1);
            setContentView(R.layout.color_style_lay);
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
            ColorPicker colorPicker = (ColorPicker) findViewById(R.id.color_picker);
            colorPicker.addSVBar((SVBar) findViewById(R.id.svbar));
            colorPicker.addOpacityBar((OpacityBar) findViewById(R.id.opacitybar));
            colorPicker.getColor();
            colorPicker.setOnColorChangedListener(this);
            findViewById(R.id.okBtn).setOnClickListener(this);
        }

        public void setOnColorChangeListener(OnComponentChangeListener onComponentChangeListener) {
            this.onComponentChangeListener = onComponentChangeListener;
        }

        @Override 
        public void onColorChanged(int i) {
            this.onComponentChangeListener.onColorChanged(i);
        }

        @Override 
        public void onClick(View view) {
            dismiss();
        }
    }
}
