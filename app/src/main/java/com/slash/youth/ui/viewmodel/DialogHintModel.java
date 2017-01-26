package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.DeleteItemDialogBinding;
import com.slash.youth.databinding.DialogHintBinding;

/**
 * Created by acer on 2017/1/26.
 */
public class DialogHintModel extends BaseObservable {
    private DialogHintBinding dialogHintBinding;
    public AlertDialog currentDialog;

    public DialogHintModel(DialogHintBinding dialogHintBinding) {
        this.dialogHintBinding = dialogHintBinding;
    }

    public void hintCannel(View view){
        currentDialog.dismiss();
    }

    public void hintSure(View view){
        currentDialog.dismiss();
        listener.OnHintClick();
    }

    public interface OnHintClickListener{
        void OnHintClick();
    }

    private OnHintClickListener listener;
    public void setOnHintCklickListener(OnHintClickListener listener) {
        this.listener = listener;
    }

}
