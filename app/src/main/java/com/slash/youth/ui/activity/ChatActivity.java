package com.slash.youth.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.core.op.lib.utils.AndroidBug5497Workaround;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChatBinding;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ChatModel;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatActivity extends BaseActivity {

    private ChatModel mChatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityChatBinding activityChatBinding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        mChatModel = new ChatModel(activityChatBinding, this);
        activityChatBinding.setChatModel(mChatModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AndroidBug5497Workaround.assistActivity(this, getApplicationContext());
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mChatModel.onActivityDestory();

        MsgManager.removeChatTextListener();
        MsgManager.removeChatPicListener();
        MsgManager.removeChatVoiceListener();
        MsgManager.removeChatOtherCmdListener();
        MsgManager.removeHistoryListener();
        MsgManager.removeRelatedTaskListener();
        MsgManager.removeSlashMessageListener();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 100:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    mChatModel.soundRecord();
                } else {
                    // Permission Denied
                    ToastUtils.shortToast("RECORD_AUDIO Denied");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == this.RESULT_OK) {
            if (requestCode == 10) {//从相册选择图片发送
//                这里是用了系统自带的选择图片方式
                if (data != null) {
                    try {
                        Bitmap bitmap = data.getParcelableExtra("data");
                        String picCachePath = this.getCacheDir().getAbsoluteFile() + "/picache/";
                        File cacheDir = new File(picCachePath);
                        if (!cacheDir.exists()) {
                            cacheDir.mkdir();
                        }
                        File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));
                        mChatModel.sendPic(tempFile.getAbsolutePath());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else if (requestCode == 20) {//拍摄图片发送
                //这是手机自带的拍照功能
                if (data != null) {
                    Bitmap bmpSource = (Bitmap) data.getExtras().get("data");
                    File cacheDir = getCacheDir();
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs();
                    }
                    final File fileBmpSource = new File(cacheDir, "thumb" + System.currentTimeMillis());
                    FileOutputStream fosBmpSource = null;
                    try {
                        fosBmpSource = new FileOutputStream(fileBmpSource);
                        bmpSource.compress(Bitmap.CompressFormat.JPEG, 100, fosBmpSource);
                        mChatModel.sendPic(fileBmpSource.getAbsolutePath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.close(fosBmpSource);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        int viewPicVisibility = mChatModel.getViewPicVisibility();
        if (viewPicVisibility == View.VISIBLE) {
            mChatModel.setViewPicVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}