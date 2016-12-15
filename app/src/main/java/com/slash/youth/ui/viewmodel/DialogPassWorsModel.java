package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogPasswordBinding;
import com.slash.youth.databinding.LayoutWithdrawalsBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/6.
 */
public class DialogPassWorsModel extends BaseObservable {

    private DialogPasswordBinding dialogPasswordBinding;
    public AlertDialog currentDialog;//当前对话框
    private ImageView[] imageViews = new ImageView[6];//用来存小圆点的
    private String strPassword;//密码字符串
    private LayoutWithdrawalsBinding layoutWithdrawalsBinding;
    private int num =0;
    private double amount;
    private String number;

    public DialogPassWorsModel(DialogPasswordBinding dialogPasswordBinding,double amount,String number,LayoutWithdrawalsBinding layoutWithdrawalsBinding) {
        this.dialogPasswordBinding = dialogPasswordBinding;
        this.layoutWithdrawalsBinding = layoutWithdrawalsBinding;
        this.amount = amount;
        this.number = number;
        initData();
        initView();
    }

    private void initData() {
        imageViews[0]=  dialogPasswordBinding.password1;
        imageViews[1]=  dialogPasswordBinding.password2;
        imageViews[2]=  dialogPasswordBinding.password3;
        imageViews[3]=  dialogPasswordBinding.password4;
        imageViews[4]=  dialogPasswordBinding.password5;
        imageViews[5]=  dialogPasswordBinding.password6;
    }

    private void initView() {
        dialogPasswordBinding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                num =num+count;
            }
            @Override
            public void afterTextChanged(Editable s) {
            String text = s.toString();
            if (num == 6){
                strPassword = text;
            }

            if(num >0&& num <=6){
            imageViews[num -1].setVisibility(View.VISIBLE);
            }

            }
        });

        dialogPasswordBinding.etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL
                        && event.getAction() == KeyEvent.ACTION_DOWN) {

                    if(num == 0){
                        strPassword = null;
                    }

                    if(num>0&&num<=6){
                    num--;
                    imageViews[num].setVisibility(View.INVISIBLE);
                    }

                    return false;
                }
                return false;
            }
        });

    }

    //取消
    public void cannel(View view){
        currentDialog.dismiss();
    }

    private  int  type = 1;//支付宝 ，2微信

    //确定
    public void sure(View view){
        if(num == 6){
            String password = String.valueOf(strPassword);
            MyManager.enchashmentApplication(new onEnchashmentApplication(),amount,number,1,password);
        }
        currentDialog.dismiss();
    }

    public class onEnchashmentApplication implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        layoutWithdrawalsBinding.flHint.setVisibility(View.VISIBLE);
                        layoutWithdrawalsBinding.tvHint.setText(MyManager.WITHDRAWALS_SUCCESS);
                        break;
                    case 2:
                        layoutWithdrawalsBinding.flHint.setVisibility(View.VISIBLE);
                        layoutWithdrawalsBinding.tvHint.setText(MyManager.WITHDRAWALS_FAIL_BALANCE_LEASE);
                        break;
                    case 3:
                        layoutWithdrawalsBinding.flHint.setVisibility(View.VISIBLE);
                        layoutWithdrawalsBinding.tvHint.setText(MyManager.WITHDRAWALS_FAIL_PASSWORD_ERROR);
                        break;
                    case 4:
                        LogKit.d("WITHDRAWALS_SERVICE_ERROR:"+MyManager.WITHDRAWALS_SERVICE_ERROR);
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
