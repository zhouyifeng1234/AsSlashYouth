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
import android.widget.TextView;
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
        if(text!=null&&text!=""){
            Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    public static void shortCenterToast(String text) {
        Toast  toast = Toast.makeText(CommonUtils.getApplication(),
                text, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    public static void shortCenterToast(String text,int messageColor, int duration) {
        Toast  toast = Toast.makeText(CommonUtils.getApplication(),
                text, duration);
        toast.setGravity(Gravity.CENTER, 0, 0);
        View view = toast.getView();
        if(view!=null){
            TextView message=((TextView) view.findViewById(android.R.id.message));
           // message.setBackgroundResource(background);
            message.setTextColor(messageColor);
        }
      //  toast.setDuration(duration);
        toast.show();

    }

    //可以避免出现toast叠加
    private Context context;
    private Toast toast = null;
    public ToastUtils(Context context) {
        this.context = context;
    }
    public void toastShow(String text) {
        if(toast == null)
        {
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        else {
            toast.setText(text);
        }
        toast.show();
    }

}


