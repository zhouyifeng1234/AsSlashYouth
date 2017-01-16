package com.slash.youth.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.lang.reflect.Field;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class SlashNumberPicker extends NumberPicker {

    public SlashNumberPicker(Context context) {
        super(context);
    }

    public SlashNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlashNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index,
                        android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    public void updateView(View view) {
        if (view instanceof EditText) {
            //这里修改字体的属性
            EditText etData = (EditText) view;
//            etData.setEnabled(false);
            etData.setClickable(false);
            etData.setFocusable(false);
//            etData.setTextColor(Color.parseColor("#ff00ff00"));
//            ((EditText) view).setTextSize();
        }
    }

    @Override
    public void setDisplayedValues(String[] displayedValues) {
        super.setDisplayedValues(displayedValues);
        hideNumberPickerDivider();
    }
    public void hideNumberPickerDivider() {
        Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (Field pf : pickerFields) {
            try {
                if (pf.getName().equals("mSelectionDivider")) {
                    pf.setAccessible(true);
                    pf.set(this, new ColorDrawable(0x00000000));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
