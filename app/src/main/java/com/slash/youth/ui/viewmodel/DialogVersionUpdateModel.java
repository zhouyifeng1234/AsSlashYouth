package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.DialogVersionUpdateBinding;

/**
 * Created by zss on 2017/1/5.
 */
public class DialogVersionUpdateModel  extends BaseObservable {
    private DialogVersionUpdateBinding dialogVersionUpdateBinding;
    private int forceupdate;
    private String url;

    public DialogVersionUpdateModel(DialogVersionUpdateBinding dialogVersionUpdateBinding,int forceupdate,String url) {
        this.dialogVersionUpdateBinding = dialogVersionUpdateBinding;
        this.forceupdate = forceupdate;
        this.url = url;
        initView();
    }

    private void initView() {
       if(forceupdate == 1){
           dialogVersionUpdateBinding.cannel.setVisibility(View.GONE);
       }
    }

}
