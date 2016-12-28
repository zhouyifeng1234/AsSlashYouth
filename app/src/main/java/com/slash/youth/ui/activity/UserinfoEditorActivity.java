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
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.ui.viewmodel.ActivityUserInfoEditorModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;

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
    private File file = new File(Environment.getExternalStorageDirectory()+"/001.jpg");
    private File fileName = new File(Environment.getExternalStorageDirectory()+"/002.jpg");
    private  ArrayList<String> path = new ArrayList<>();
    private  ArrayList<String> pathFile = new ArrayList<>();
    private String filename;
    private StringBuffer sb = new StringBuffer();

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
            case Constants.USERINFO_IMAGVIEW_TAKE_PHOTO:
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                Bitmap photo = BitmapKit.zoomBitmap(bitmap, 48, 48);
                activityUserinfoEditorBinding.ivHead.setImageBitmap(photo);
                WriteFile(photo, fileName);
                filename = fileName.getAbsolutePath();
                pathFile.add(filename);
                String takePhotoFilePath = pathFile.get(0);
                activityUserInfoEditorModel.avatar = takePhotoFilePath;
            }
                break;
            case Constants.USERINFO_SKILLLABEL_ALBUM:
            if(resultCode == RESULT_OK){
                Uri uri = data.getData();
                try {
                    Bitmap bitmap_album= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    Bitmap  photo_album  = BitmapKit.zoomBitmap(bitmap_album, 48, 48);
                    activityUserinfoEditorBinding.ivHead.setImageBitmap(photo_album);
                    WriteFile(photo_album,file);
                    filename = file.getAbsolutePath();
                    path.add(filename);
                    String filePath = path.get(0);
                    activityUserInfoEditorModel.avatar = filePath;

                } catch (Exception e) {
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
                            if(size!=0){
                                activityUserinfoEditorBinding.llSkilllabelContainer.removeAllViews();
                                for (String skillTagText : listCheckedLabelName) {
                                    TextView skillTag = activityUserInfoEditorModel.createSkillTag(skillTagText);
                                    activityUserinfoEditorBinding.llSkilllabelContainer.addView(skillTag);
                                }
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

        //最近访问的城市
        if(resultCode == Constants.CURRENT_ACCESS_CITY){
            String currentCityAccess = data.getStringExtra("currentCityAccess");
            activityUserinfoEditorBinding.tvLocation.setText(currentCityAccess);
        }
        super.onActivityResult(requestCode, resultCode, data);
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