package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogPasswordBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.databinding.LayoutWithdrawalsBinding;
import com.slash.youth.ui.activity.WithdrawalsActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;

import java.util.Observable;

/**
 * Created by zss on 2016/11/6.
 */
public class WithdrawalsModel extends BaseObservable {
    private LayoutWithdrawalsBinding layoutWithdrawalsBinding;
    private WithdrawalsActivity withdrawalsActivity;

    public WithdrawalsModel(LayoutWithdrawalsBinding layoutWithdrawalsBinding, WithdrawalsActivity withdrawalsActivity) {
        this.layoutWithdrawalsBinding = layoutWithdrawalsBinding;
        this.withdrawalsActivity = withdrawalsActivity;
    }

    //next
    public void next(View view){
    layoutWithdrawalsBinding.flHint.setVisibility(View.VISIBLE);
    }

    //确定
    public void hintSure(View view){
        layoutWithdrawalsBinding.flHint.setVisibility(View.GONE);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(withdrawalsActivity);
        DialogPasswordBinding dialogPasswordBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_password, null, false);
        DialogPassWorsModel dialogPassWorsModel = new DialogPassWorsModel(dialogPasswordBinding);
        dialogPasswordBinding.setDialogPasWorsModel(dialogPassWorsModel);
        dialogBuilder.setView(dialogPasswordBinding.getRoot());//getRoot返回根布局
        AlertDialog dialogPassWord = dialogBuilder.create();
        dialogPassWord.show();
        dialogPassWorsModel.currentDialog = dialogPassWord;
        dialogPassWord.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        Window dialogSubscribeWindow = dialogPassWord.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(280);//宽度
        dialogParams.height = CommonUtils.dip2px(200);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);//dialog的灰度
//        dialogBuilder.show();
    }

    //取消
    public void hintCannel(View view){
    layoutWithdrawalsBinding.flHint.setVisibility(View.GONE);
    }
}
