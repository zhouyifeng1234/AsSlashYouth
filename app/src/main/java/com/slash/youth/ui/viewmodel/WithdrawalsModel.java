package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.sip.SipSession;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
        listener();
    }

    private void listener() {
        layoutWithdrawalsBinding.etDollar.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        layoutWithdrawalsBinding.etDollar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s == null || s.length() == 0){
                    layoutWithdrawalsBinding.etDollar.setText("0.00");
                    return;
                }

                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        layoutWithdrawalsBinding.etDollar.setText(s);
                        layoutWithdrawalsBinding.etDollar.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    layoutWithdrawalsBinding.etDollar.setText(s);
                    layoutWithdrawalsBinding.etDollar.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        layoutWithdrawalsBinding.etDollar.setText(s.subSequence(0, 1));
                        layoutWithdrawalsBinding.etDollar.setSelection(1);
                        return;
                    }
                }

                if(s.toString().trim().substring(0,1).equals(".") || s.toString().trim().substring(s.toString().trim().length()-1,s.toString().trim().length()).equals(".")){
                    return;
                }

            }

        @Override
        public void afterTextChanged(Editable s) {

        }
    });

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
