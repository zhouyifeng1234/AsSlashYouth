package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.DeleteItemDialogBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2017/1/26.
 */
public class DeleteItemDialogModel extends BaseObservable {
    private DeleteItemDialogBinding deleteItemDialogBinding;
    private String itemTitle;
    public AlertDialog currentDialog;
    private int type;

    public DeleteItemDialogModel(DeleteItemDialogBinding deleteItemDialogBinding,int type) {
        this.deleteItemDialogBinding = deleteItemDialogBinding;
        this.type = type;
        initView();
    }

    private void initView() {
        switch (type){
            case 1:
                setItemTitle("确认删除该技能");
                break;
            case 2:
                setItemTitle("确定删除该信息");
                break;
            case 3:
                setItemTitle("确定删除该任务");
                break;
        }
    }

    public void itemDeleteCannel(View view){
        currentDialog.dismiss();
    }

    public void itemDeleteSure(View view){
        currentDialog.dismiss();
        listener.OnDeleteClick();
    }

    @Bindable
    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
        notifyPropertyChanged(BR.itemTitle);
    }

    public interface OnDeleteClickListener{
        void OnDeleteClick();
    }

    private OnDeleteClickListener listener;
    public void setOnDeleteCklickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }
}
