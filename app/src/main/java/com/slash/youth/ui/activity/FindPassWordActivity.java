package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFindPasswordBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.engine.AccountManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.viewmodel.FindPassWordModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class FindPassWordActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private TextView save;
    private ActivityFindPasswordBinding activityFindPasswordBinding;
    private FindPassWordModel findPassWordModel;
    private String createPassWord;
    private String surePassWord;
    private Intent intent;
    private String findPassWord ="找回密码";
    private String setPassWord ="设置密码";
    private String findPassWordText ="请上传手持身份证正面照, 用于找回交易密码";
    private String setPassWordText ="请上传手持身份证正面照, 用于设置交易密码";
    private String titleRight ="提交";
    private Bitmap bitmap;
    private Bitmap photo;
    private String toastText = "两次输入的密码不一致";
    private String toastTextString = "请上传手持身份证正面照";
    private String toastString = "请填写密码";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        activityFindPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        findPassWordModel = new FindPassWordModel(activityFindPasswordBinding,this, type);
        activityFindPasswordBinding.setFindPassWordModel(findPassWordModel);
        this.intent = new Intent();
        listener();
        initView();
    }
    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText(titleRight);
        save.setOnClickListener(this);
    }

    private void initView() {
        switch (type){
            case 1://设置了交易密码
                title.setText(findPassWord);
                activityFindPasswordBinding.tvDesc.setText(findPassWordText);
                break;
            case 2://表示当前没有交易密码
                title.setText(setPassWord);
                activityFindPasswordBinding.tvDesc.setText(setPassWordText);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
            case R.id.tv_userinfo_save://提交
                createPassWord = findPassWordModel.createPassWordMap.get("createPassWord");
                surePassWord = findPassWordModel.surePassWordMap.get("surePassWord");
                if(createPassWord!=null&&surePassWord!=null){
                    if(!createPassWord.equals(surePassWord)){
                        ToastUtils.shortToast(toastText);
                    }

                    if(TextUtils.isEmpty(findPassWordModel.fileId)){
                        ToastUtils.shortToast(toastTextString);
                    }

                    if(createPassWord.equals(surePassWord)&&!(findPassWordModel.fileId).isEmpty()){
                        switch (type){
                            case 1://设置了交易密码,找回密码
                                findPassWordModel.findPassWord(surePassWord,findPassWordModel.fileId);
                                break;
                            case 2://没有设置密码，创建密码
                                findPassWordModel.createPassWord(surePassWord,findPassWordModel.fileId);
                                break;
                        }
                    }
                }else {
                    ToastUtils.shortToast(toastString);
                }
                break;
        }
    }
}
