package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogPasswordBinding;
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
    private int num =0;

    public DialogPassWorsModel(DialogPasswordBinding dialogPasswordBinding) {
        this.dialogPasswordBinding = dialogPasswordBinding;
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

    //确定
    public void sure(View view){

        if(num == 6){
            LogKit.d("密码是 :"+strPassword);
        }

        currentDialog.dismiss();
    }
}
