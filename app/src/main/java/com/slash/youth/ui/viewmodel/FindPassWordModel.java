package com.slash.youth.ui.viewmodel;

import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.Uri;
import android.net.sip.SipSession;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.slash.youth.databinding.ActivityFindPasswordBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CreatePassWordProtovol;
import com.slash.youth.http.protocol.SetPassWordProtocol;
import com.slash.youth.http.protocol.UploadPhotoProtocol;
import com.slash.youth.ui.activity.FindPassWordActivity;
import com.slash.youth.utils.LogKit;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zss on 2016/11/3.
 */
public class FindPassWordModel extends BaseObservable {
    private ActivityFindPasswordBinding activityFindPasswordBinding;
    private FindPassWordActivity findPassWordActivity;
    final static int CAMERA_RESULT = 0;//声明一个常量，代表结果码
    public Map<String, String> createPassWordMap = new HashMap<>();
    public Map<String, String> surePassWordMap = new HashMap<>();
    private String createPassWord;
    private String surePassWord;
    public boolean createPassWord1=true;
    public boolean setPhoto1 = true;

    public FindPassWordModel(ActivityFindPasswordBinding activityFindPasswordBinding,FindPassWordActivity findPassWordActivity) {
        this.activityFindPasswordBinding = activityFindPasswordBinding;
        this.findPassWordActivity = findPassWordActivity;
        listener();
    }

    private void listener() {
        getCtretePassWord(activityFindPasswordBinding.etSetNewPassword);
         getSurePassWord(activityFindPasswordBinding.etSureNewPassword);

    }

    private void getCtretePassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
           @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String  setPassWord =  s.toString();
                createPassWordMap.put("createPassWord",setPassWord);
            }

        });
    }

    private void getSurePassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String  setPassWord =  s.toString();
                surePassWordMap.put("surePassWord",setPassWord);
            }
        });
    }

    //点击拍照
    public  void photo(View view){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            findPassWordActivity.startActivityForResult(intent, CAMERA_RESULT);
    }


    //上传图片
    public void uploadPhoto(String filename){
        UploadPhotoProtocol uploadPhotoProtocol = new UploadPhotoProtocol(filename);
        uploadPhotoProtocol.getDataFromServer(new BaseProtocol.IResultExecutor() {
            @Override
            public void execute(Object dataBean) {
                LogKit.d("返回值"+dataBean.toString());
                setPhoto1 = true;
            }

            @Override
            public void executeResultError(String result) {
            LogKit.d("result:"+result);
            }
        });
    }

    //设置密码
    public void setPassWord(String oldpass,String newpass){
        SetPassWordProtocol setPassWordProtocol = new SetPassWordProtocol(oldpass,newpass);
        setPassWordProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                if(rescode == 0){
                    SetBean.DataBean data = dataBean.getData();
                    int status = data.getStatus();
                    switch (status){
                        case 1:
                            LogKit.d("设置成功");

                            break;
                        case 2:
                            LogKit.d("设置失败");
                            break;
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                 LogKit.d("result:"+result);
            }
        });
    }

    //创建密码
    public void createPassWord(String pass){
        CreatePassWordProtovol createPassWordProtovol = new CreatePassWordProtovol(pass);
        createPassWordProtovol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                if(rescode == 0){
                    SetBean.DataBean data = dataBean.getData();
                    int status = data.getStatus();
                    switch (status){
                        case 1:
                            LogKit.d("设置成功");
                            createPassWord1 = true;
                            break;
                        case 2:
                            LogKit.d("设置失败");
                            createPassWord1 = false;
                            break;
                    }
                }
            }
            @Override
            public void executeResultError(String result) {
            LogKit.d("result:"+result);
            }
        });
    }



}
