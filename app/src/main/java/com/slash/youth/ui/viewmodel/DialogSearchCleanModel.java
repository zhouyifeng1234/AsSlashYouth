package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/9/20.
 */
public class DialogSearchCleanModel extends BaseObservable {

    private DialogSearchCleanBinding mDialogSearchCleanBinding;
    public AlertDialog currentDialog;//当前对话框
    //构造函数
    public DialogSearchCleanModel(DialogSearchCleanBinding dialogSearchCleanBing) {
        this.mDialogSearchCleanBinding = dialogSearchCleanBing;
    }

    //取消搜索对话框(xml中取消按钮绑定方法)
    public void cancelSearchDialog(View v) {
        currentDialog.dismiss();
    }

    //ok搜索对话框
    public void okSearchDialog(View v) {
        currentDialog.dismiss();
    }

   /* //打开搜索
    public void openSearchActivity(View v) {
        Intent intentSubSearchActivity = new Intent(CommonUtils.getContext(), SearchActivity.class);
        intentSubSearchActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSubSearchActivity);
        currentDialog.dismiss();
    }*/

}
