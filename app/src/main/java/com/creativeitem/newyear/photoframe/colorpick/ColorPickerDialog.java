package com.creativeitem.newyear.photoframe.colorpick;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


/**
 * Created by SelfCoderLab
 */
public class ColorPickerDialog extends AlertDialog {



    private final OnColorSelectedListener onColorSelectedListener;
    private ColorPicker colorPickerView;
    private OnClickListener onClickListener = new OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case BUTTON_POSITIVE:
                    int selectedColor = colorPickerView.getColor();
                    onColorSelectedListener.onColorSelected(selectedColor);
                    break;
                case BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
            }
        }
    };

    public ColorPickerDialog(Context context, int initialColor, OnColorSelectedListener onColorSelectedListener) {
        super(context);

        this.onColorSelectedListener = onColorSelectedListener;

        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        colorPickerView = new ColorPicker(context);
        colorPickerView.setColor(initialColor);

       // relativeLayout.setAlpha(Float.parseFloat("0.5"));
        relativeLayout.addView(colorPickerView, layoutParams);

        setButton(BUTTON_POSITIVE, context.getString(android.R.string.ok), onClickListener);
        setButton(BUTTON_NEGATIVE, context.getString(android.R.string.cancel), onClickListener);

        setView(relativeLayout);

    }

    public interface OnColorSelectedListener {
        public void onColorSelected(int color);
    }
}
