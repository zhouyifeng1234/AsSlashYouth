package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.viewmodel.ActivityUserInfoEditorModel;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/1.
 */
public class UserinfoEditorActivity extends Activity {

    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;
    private ActivityUserInfoEditorModel activityUserInfoEditorModel;
    private StringBuffer sb = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        activityUserinfoEditorBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo_editor);
        long myId = intent.getLongExtra("myId", -1);
        if(myId == -1){
            MyFirstPageBean.DataBean.MyinfoBean myinfo = (MyFirstPageBean.DataBean.MyinfoBean) intent.getSerializableExtra("myUinfo");
            activityUserInfoEditorModel = new ActivityUserInfoEditorModel(activityUserinfoEditorBinding,myId,this,myinfo);
        }else {
            String phone = intent.getStringExtra("phone");
            UserInfoItemBean.DataBean.UinfoBean uifo = (UserInfoItemBean.DataBean.UinfoBean) intent.getSerializableExtra("uifo");
            activityUserInfoEditorModel = new ActivityUserInfoEditorModel(activityUserinfoEditorBinding,myId,this,uifo,phone);
        }
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
                    if (identity != null) {
                        activityUserinfoEditorBinding.tvIdentity.setText(identity);
                    }
                }
                break;
            case Constants.USERINFO_IMAGVIEW:
                //1.图片裁剪小图片，2传递uri,用bitmapFactory解析出来(intent不适合传递大量数据)
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    try {
                        Bitmap photo = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        activityUserinfoEditorBinding.ivHead.setImageBitmap(photo);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
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
                            switch (size) {
                                case 1:
                                    activityUserinfoEditorBinding.tvLine.setText(listCheckedLabelName.get(0));
                                    activityUserinfoEditorBinding.tvLine.setVisibility(View.VISIBLE);
                                    break;
                                case 2:
                                    activityUserinfoEditorBinding.tvLine.setText(listCheckedLabelName.get(0));
                                    activityUserinfoEditorBinding.tvLine.setVisibility(View.VISIBLE);
                                    activityUserinfoEditorBinding.tvLine2.setText(listCheckedLabelName.get(1));
                                    activityUserinfoEditorBinding.tvLine2.setVisibility(View.VISIBLE);
                                    break;
                                case 3:
                                    activityUserinfoEditorBinding.tvLine.setText(listCheckedLabelName.get(0));
                                    activityUserinfoEditorBinding.tvLine.setVisibility(View.VISIBLE);
                                    activityUserinfoEditorBinding.tvLine2.setText(listCheckedLabelName.get(1));
                                    activityUserinfoEditorBinding.tvLine2.setVisibility(View.VISIBLE);
                                    activityUserinfoEditorBinding.tvLine3.setText(listCheckedLabelName.get(2));
                                    activityUserinfoEditorBinding.tvLine3.setVisibility(View.VISIBLE);
                                    break;
                                default:
                                    activityUserinfoEditorBinding.llEditorSkillContainer.removeAllViews();
                                    break;
                            }
                        }
                        //第一级技能标签 //第二级技能标签
                        String checkFirstLabel = bundleCheckedLabelsData.getString("checkedFirstLabel", "未选择");
                        String checkedSecondLabel = bundleCheckedLabelsData.getString("checkedSecondLabel", "未选择");
                        if (checkFirstLabel != null && checkedSecondLabel != null) {
                            activityUserinfoEditorBinding.tvDirection.setText(checkFirstLabel + "|" + checkedSecondLabel);
                        }
                    }
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }




   /* @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // savedInstanceState.putString("msg_con", htmlsource);
        // savedInstanceState.putString("msg_camera_picname", camera_picname);
        super.onSaveInstanceState(savedInstanceState); //实现父类方法 放在最后 防止拍照后无法返回当前activity
    }*/


}