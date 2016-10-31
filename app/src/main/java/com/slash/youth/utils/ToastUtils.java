package com.slash.youth.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.ui.viewmodel.DialogSearchCleanModel;

public class ToastUtils {
    public static Toast slashToast = new Toast(CommonUtils.getContext());
    public static Object obj = null;

    public static void longToast(String text) {
        Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void longCenterToast(String text) {
        Toast  toast = Toast.makeText(CommonUtils.getApplication(),
                text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void shortToast(String text) {
        Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void shortCenterToast(String text) {
        Toast  toast = Toast.makeText(CommonUtils.getApplication(),
                text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }




}


