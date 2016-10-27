package com.slash.youth.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.ui.viewmodel.DialogSearchCleanModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToastUtils {
    public static Toast slashToast = new Toast(CommonUtils.getContext());
    public static Object obj = null;


    static {
//        slashToast.setView();
    }

    public static void longToast(String text) {
        Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void shortToast(String text) {
        Toast.makeText(CommonUtils.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showDialog() {
        /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommonUtils.getContext());
        //数据绑定填充视图
        DialogSearchCleanBinding dialogSearchCleanBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_search_clean, null, false);
        //创建数据模型
        DialogSearchCleanModel dialogSearchCleanModel = new DialogSearchCleanModel(dialogSearchCleanBinding);
        //绑定数据模型
        dialogSearchCleanBinding.setDialogSearchCleanModel(dialogSearchCleanModel);
        //设置布局
        dialogBuilder.setView(dialogSearchCleanBinding.getRoot());//getRoot返回根布局
        //创建dialogSearch
        AlertDialog dialogSearch = dialogBuilder.create();
        dialogSearch.show();
        dialogSearchCleanModel.currentDialog = dialogSearch;
        dialogSearch.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        Window dialogSubscribeWindow = dialogSearch.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        //定义显示的dialog的宽和高
        dialogParams.width = CommonUtils.dip2px(280);//宽度
        dialogParams.height = CommonUtils.dip2px(200);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);*/
        //显示全局的
        // dialogSearch.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);


       /*  AlertDialog.Builder builder = new AlertDialog.Builder(CommonUtils.getContext());
        builder.setIcon(R.drawable.guanbi_icon);
        builder.setTitle("标题");
        builder.setMessage("提示文字");
        *//*builder.setPositiveButton("quxiao ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //增加按钮,回调事件
                    }
                    );*//*
                   // builder.setCancelable(false);//弹出框不可以换返回键取消
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);//将弹出框设置为全局
                    dialog.setCanceledOnTouchOutside(false);//失去焦点不会消失
                    dialog.show();*/


    }

}


