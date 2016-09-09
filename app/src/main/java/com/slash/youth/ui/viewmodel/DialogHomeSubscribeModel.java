package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.DialogHomeSubscribeBinding;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/9.
 */
public class DialogHomeSubscribeModel extends BaseObservable {

    private DialogHomeSubscribeBinding mDialogHomeSubscribeBinding;
    public AlertDialog currentDialog;

    public DialogHomeSubscribeModel(DialogHomeSubscribeBinding dialogHomeSubscribeBinding) {
        this.mDialogHomeSubscribeBinding = dialogHomeSubscribeBinding;
    }

    public void cancelSubscribeDialog(View v) {
        currentDialog.dismiss();
    }

    public void okSubscribeDialog(View v) {
        currentDialog.dismiss();
    }

    public void openSubscribeActivity(View v) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        intentSubscribeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentSubscribeActivity);
        currentDialog.dismiss();
    }

}
