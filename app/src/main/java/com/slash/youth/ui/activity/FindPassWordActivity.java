package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFindPasswordBinding;
import com.slash.youth.ui.viewmodel.FindPassWordModel;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by zss on 2016/11/3.
 */
public class FindPassWordActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private TextView save;
    private ActivityFindPasswordBinding activityFindPasswordBinding;
    private File file = new File(Environment.getExternalStorageDirectory()+"/000.jpg");
    private String filename;
    private FindPassWordModel findPassWordModel;
    private  StringBuffer sb = new StringBuffer();
    private String createPassWord;
    private String surePassWord;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityFindPasswordBinding = DataBindingUtil.setContentView(this, R.layout.activity_find_password);
        findPassWordModel = new FindPassWordModel(activityFindPasswordBinding,this);
        activityFindPasswordBinding.setFindPassWordModel(findPassWordModel);
        intent = new Intent();
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        if (SpUtils.getBoolean("create_ok",false)) {
            title.setText("找回密码");
            activityFindPasswordBinding.tvDesc.setText("请上传手持身份证正面照, 用于找回交易密码");
        }else {
            title.setText("设置密码");
            activityFindPasswordBinding.tvDesc.setText("请上传手持身份证正面照, 用于设置交易密码");
        }
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText("提交");
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;

            case R.id.tv_userinfo_save:
                createPassWord = findPassWordModel.createPassWordMap.get("createPassWord");
                surePassWord = findPassWordModel.surePassWordMap.get("surePassWord");
                if(createPassWord!=null&&surePassWord!=null){
                    if(!createPassWord.equals(surePassWord)){
                        ToastUtils.shortToast("两次输入的密码不一致");
                    }

                    if((sb.toString()).isEmpty()){
                        ToastUtils.shortToast("请上传手持身份证正面照");
                    }
                    if(createPassWord.equals(surePassWord)&&!(sb.toString()).isEmpty()){
                        findPassWordModel.uploadPhoto(sb.toString());
                        findPassWordModel.createPassWord(surePassWord);
                        if(findPassWordModel.setPhoto1&&findPassWordModel.createPassWord1){
                            FindPassWordActivity.this.setResult(RESULT_OK);
                        }
                        finish();
                    }
                }else {
                    ToastUtils.shortToast("请填写密码");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
          if(requestCode == 0){
          Bundle bundle = data.getExtras();
           Bitmap bitmap = (Bitmap) bundle.get("data");
            WriteFile(bitmap);
              //文件的绝对路径
              filename = file.getAbsolutePath();
              sb.append(file);
          }
        }
    }

    private void WriteFile(Bitmap bitmap) {
        FileOutputStream b = null;
        try {
            b = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
