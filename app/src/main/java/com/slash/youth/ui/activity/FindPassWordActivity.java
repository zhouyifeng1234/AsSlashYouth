package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFindPasswordBinding;
import com.slash.youth.ui.viewmodel.FindPassWordModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Constants;
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
    private File file = new File(Environment.getExternalStorageDirectory()+"/000.jpg");
    private String filename;
    private FindPassWordModel findPassWordModel;
    private  ArrayList<String> path = new ArrayList<>();
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
            title.setText(findPassWord);
            activityFindPasswordBinding.tvDesc.setText(findPassWordText);
        }else {
            title.setText(setPassWord);
            activityFindPasswordBinding.tvDesc.setText(setPassWordText);
        }
        save = (TextView) findViewById(R.id.tv_userinfo_save);
        save.setText(titleRight);
        save.setOnClickListener(this);
    }

    private String toastText = "两次输入的密码不一致";
    private String toastTextString = "请上传手持身份证正面照";
    private String toastString = "请填写密码";
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

                    if(path.isEmpty()){
                        ToastUtils.shortToast(toastTextString);
                    }
                    if(createPassWord.equals(surePassWord)&&!(path).isEmpty()){
                        //上传照片的地址
                        //findPassWordModel.uploadPhoto(path.get(0));
                        //创建密码
                        findPassWordModel.createPassWord(surePassWord);
                        if(findPassWordModel.setPhoto1&&findPassWordModel.createPassWord1){
                            //FindPassWordActivity.this.setResult(RESULT_OK);
                        }
                      //  finish();
                    }
                }else {
                    ToastUtils.shortToast(toastString);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            path.clear();

            switch (requestCode){
                case  Constants.MY_SETTING_TAKE_PHOTO:
                    Bundle bundle = data.getExtras();
                    bitmap = (Bitmap) bundle.get("data");
                    break;
                case Constants.MY_SETTING_TAKE_ABLEM:

                    Uri uri = data.getData();
                    try {
                        bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        photo  = BitmapKit.zoomBitmap(bitmap, 250, 200);
                        activityFindPasswordBinding.ivPhoto.setImageBitmap(photo);
                        WriteFile(bitmap);
                        filename = file.getAbsolutePath(); //文件的绝对路径
                        path.add(filename);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            photo  = BitmapKit.zoomBitmap(bitmap, 250, 200);
            activityFindPasswordBinding.ivPhoto.setImageBitmap(photo);
            WriteFile(bitmap);
            filename = file.getAbsolutePath(); //文件的绝对路径
            path.add(filename);
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
