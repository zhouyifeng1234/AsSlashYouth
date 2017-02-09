package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.viewmodel.ActivityUserInfoEditorModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/1.
 */
public class UserinfoEditorActivity extends Activity {
    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;
    private ActivityUserInfoEditorModel activityUserInfoEditorModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        activityUserinfoEditorBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo_editor);
        long myId = intent.getLongExtra("myId", -1);
        activityUserInfoEditorModel = new ActivityUserInfoEditorModel(activityUserinfoEditorBinding,myId,this);
        activityUserinfoEditorBinding.setActivityUserInfoEditorModel(activityUserInfoEditorModel);
        back();
    }

    private void back() {
        activityUserinfoEditorBinding.ivUserinfoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.USERINFO_PHONE:
                if (resultCode == RESULT_OK) {
                    String phone = data.getStringExtra("phone");
                    activityUserinfoEditorBinding.tvUserphone.setText(phone);
                }
                break;
            case Constants.USERINFO_LOCATION:
                if (resultCode == RESULT_OK) {
                    String city = data.getStringExtra("city");
                    String province = data.getStringExtra("province");
                    if (city != null && province != null) {
                        activityUserInfoEditorModel.city = city;
                        activityUserInfoEditorModel.province = province;
                        activityUserinfoEditorBinding.tvLocation.setText(province + "" + city);
                    }
                }
                break;
            case Constants.USERINFO_IDENTITY:
                if (resultCode == RESULT_OK) {
                    String identity = data.getStringExtra("identity");
                    if(identity.equals("null")){
                        activityUserinfoEditorBinding.tvIdentity.setText("");
                    }else {
                        activityUserinfoEditorBinding.tvIdentity.setText(identity);
                    }
                }
                break;
            case Constants.USERINFO_SKILLLABEL:
                if (resultCode == RESULT_OK) {
                    Bundle bundleCheckedLabelsData = data.getBundleExtra("bundleCheckedLabelsData");
                    if (bundleCheckedLabelsData != null) {
                        String firstSkillLabelTitle = null;
                        String secondSkillLabelTitle = null;
                        //第三级技能标签集合
                        ArrayList<String> listCheckedLabelName = bundleCheckedLabelsData.getStringArrayList("listCheckedLabelName");
                        if (listCheckedLabelName != null) {
                            activityUserInfoEditorModel.skillLabelList.clear();
                            activityUserInfoEditorModel.skillLabelList.addAll(listCheckedLabelName);
                            int size = listCheckedLabelName.size();
                            if(size!=0){
                                activityUserinfoEditorBinding.llSkilllabelContainer.removeAllViews();
                                for (String skillTagText : listCheckedLabelName) {
                                    TextView skillTag = activityUserInfoEditorModel.createSkillTag(skillTagText);
                                    activityUserinfoEditorBinding.llSkilllabelContainer.addView(skillTag);
                                }
                            }
                        }
                        int size = listCheckedLabelName.size();
                        if(size!=0){
                            //第一级技能标签 //第二级技能标签
                            String checkFirstLabel = bundleCheckedLabelsData.getString("checkedFirstLabel", "未选择");
                            String checkedSecondLabel = bundleCheckedLabelsData.getString("checkedSecondLabel", "未选择");
                            if ( checkedSecondLabel != null) {
                                activityUserinfoEditorBinding.tvDirection.setText(checkedSecondLabel);
                                activityUserInfoEditorModel.industry = checkFirstLabel;
                                activityUserInfoEditorModel.direction =  checkedSecondLabel;
                            }
                        }else {
                            activityUserinfoEditorBinding.tvDirection.setText("");
                            activityUserinfoEditorBinding.llSkilllabelContainer.removeAllViews();
                        }
                    }
                }
                break;
        }

        //最近访问的城市
        if(resultCode == Constants.CURRENT_ACCESS_CITY){
            String currentCityAccess = data.getStringExtra("currentCityAccess");
            String currentyProvince = data.getStringExtra("currentyProvince");
            activityUserInfoEditorModel.city =  currentCityAccess;
            activityUserInfoEditorModel.province =  currentyProvince;
            activityUserinfoEditorBinding.tvLocation.setText(currentyProvince+""+currentCityAccess);
        }
    }

    private void WriteFile(Bitmap bitmap,File file) {
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