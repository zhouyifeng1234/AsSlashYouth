package com.slash.youth.utils;

import android.widget.Toast;

public class ToastUtils {
    public static Toast slashToast = new Toast(CommonUtils.getContext());

    static {
//        slashToast.setView();
    }

    public static void longToast(String text) {
        Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(String text) {
        Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
