package com.slash.youth.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;

/**
 * Created by zss on 2016/10/28.
 */
public class DialogUtils {

    private static AlertDialog dialog;


    public static void showDialogOne(Context context, final DialogCallUnderStandBack dialogCallUnderStandBack, String text, String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout, null);
        LinearLayout llTextContent = (LinearLayout) view.findViewById(R.id.ll_text_content);
        TextView tvTextContent = (TextView) view.findViewById(R.id.tv_text_content);
        if (!TextUtils.isEmpty(text)) {
            tvTextContent.setText(text);
            llTextContent.setVisibility(View.GONE);
            tvTextContent.setVisibility(View.VISIBLE);
        } else {
            llTextContent.setVisibility(View.VISIBLE);
            tvTextContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            TextView tvToastTitle = (TextView) view.findViewById(R.id.tv_toast_title);
            tvToastTitle.setText(title);
        }
        ImageButton understand = (ImageButton) view.findViewById(R.id.ib_close);
      /*  TextView text1 = (TextView) view.findViewById(R.id.tv_content);
        TextView text2 = (TextView) view.findViewById(R.id.tv_time_title);
        TextView text3 = (TextView) view.findViewById(R.id.tv_data);*/
       /* text2.setText(title);
        text1.setText(content);
        text3.setText(time);*/
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallUnderStandBack.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();
    }

    public static void showDialogOne(Context context, final DialogCallUnderStandBack dialogCallUnderStandBack, Spanned text, String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout, null);
        LinearLayout llTextContent = (LinearLayout) view.findViewById(R.id.ll_text_content);
        TextView tvTextContent = (TextView) view.findViewById(R.id.tv_text_content);
        if (!TextUtils.isEmpty(text)) {
            tvTextContent.setText(text);
            llTextContent.setVisibility(View.GONE);
            tvTextContent.setVisibility(View.VISIBLE);
        } else {
            llTextContent.setVisibility(View.VISIBLE);
            tvTextContent.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(title)) {
            TextView tvToastTitle = (TextView) view.findViewById(R.id.tv_toast_title);
            tvToastTitle.setText(title);
        }
        ImageButton understand = (ImageButton) view.findViewById(R.id.ib_close);
      /*  TextView text1 = (TextView) view.findViewById(R.id.tv_content);
        TextView text2 = (TextView) view.findViewById(R.id.tv_time_title);
        TextView text3 = (TextView) view.findViewById(R.id.tv_data);*/
       /* text2.setText(title);
        text1.setText(content);
        text3.setText(time);*/
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallUnderStandBack.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();
    }


    public static void showDialogSecond(Context context, String title, String content, final DialogCallUnderStandBack dialogCallUnderStandBack) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_second, null);
        View understand = view.findViewById(R.id.understand);
        TextView text1 = (TextView) view.findViewById(R.id.tv_content);
        TextView text2 = (TextView) view.findViewById(R.id.tv_title);
        text2.setText(title);
        text1.setText(content);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallUnderStandBack.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();
    }

    public static void showDialogThree(Context context, String title, String content, final DialogCallUnderStandBack dialogCallUnderStandBack) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_third, null);
        View understand = view.findViewById(R.id.iv_chacha);
        TextView text1 = (TextView) view.findViewById(R.id.tv_content);
        TextView text2 = (TextView) view.findViewById(R.id.tv_title);
        text2.setText(title);
        text1.setText(content);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallUnderStandBack.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();
    }

    public static void showDialogFour(Context context, final DialogCallUnderStandBack dialogCallUnderStandBack) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_fouth, null);
        View understand = view.findViewById(R.id.understand);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        understand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCallUnderStandBack.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();
    }

    public static void showDialogFive(Context context, String title, String content, final DialogCallBack dialogcallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_five, null);
        View ok = view.findViewById(R.id.ok);
        View cancel = view.findViewById(R.id.cancel);
        TextView text1 = (TextView) view.findViewById(R.id.tv_content);
        TextView text2 = (TextView) view.findViewById(R.id.tv_title);
        text2.setText(title);
        text1.setText(content);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//左右位子变化了
                dialogcallback.CancleDown();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();

    }

    public static void showDialogHint(Context context, String title, String content, final DialogCallBack dialogcallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_five, null);
        View ok = view.findViewById(R.id.ok);
        View cancel = view.findViewById(R.id.cancel);
        TextView text1 = (TextView) view.findViewById(R.id.tv_content);
        TextView text2 = (TextView) view.findViewById(R.id.tv_title);
        text2.setText(title);
        text1.setText(content);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.OkDown();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.CancleDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();

    }

    public static void showDialogLogout(Context context, String title, String content, final DialogCallBack dialogcallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_logout, null);
        View ok = view.findViewById(R.id.ok);
        View cancel = view.findViewById(R.id.cancel);
        TextView text1 = (TextView) view.findViewById(R.id.tv_hint);
        TextView text2 = (TextView) view.findViewById(R.id.tv_title);
        text2.setText(title);
        text1.setText(content);
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.CancleDown();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();

    }

    public static void showDialogVersionUpdate(Context context, boolean isForce, final DialogCallBack dialogcallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.toast_layout_version_update, null);
        View ok = view.findViewById(R.id.ok);
        View cancel = view.findViewById(R.id.cancel);
        if (isForce) {
            ok.setVisibility(View.GONE);
        }
        dialogBuilder.setView(view);
        dialog = dialogBuilder.create();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.CancleDown();
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogcallback.OkDown();
                dialog.dismiss();
            }
        });
        setDialog();
        dialog.show();
    }

    private static void setDialog() {
        //  Window dialogSubscribeWindow = dialog.getWindow();
        // WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        //定义显示的dialog的宽和高
        // dialogParams.width = CommonUtils.dip2px(width);//宽度
        // dialogParams.height = CommonUtils.dip2px(height);//高度
        //dialogSubscribeWindow.setAttributes(dialogParams);
        //dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.setCanceledOnTouchOutside(false);
    }


    public interface DialogCallBack {
        abstract void OkDown();

        abstract void CancleDown();
    }

    public interface DialogCallUnderStandBack {
        abstract void OkDown();
    }


}
