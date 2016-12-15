package com.slash.youth.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityLoginBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ActivityLoginModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PackageUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zhouyifeng on 2016/9/5.
 */
public class LoginActivity extends Activity {

    public QQLoginUiListener qqLoginUiListener;
    public AuthInfo mAuthInfo;
    public SsoHandler mSsoHandler;
    private ProgressDialog mDialog;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        qqLoginUiListener = new QQLoginUiListener();

        mAuthInfo = new AuthInfo(CommonUtils.getContext(), GlobalConstants.ThirdAppId.APPID_WEIBO, "www.slashyouth.com", "");
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

        // zss
        int versionCode = PackageUtil.getVersionCode(this);
        //获取网络的版本号,xUtils异步获取，后端接口
        int NetVersionCode = -1;
        if (NetVersionCode > versionCode) {
            // 检测版本更新
            //     versionUpdate();
        } else {
            //不需要版本更新
            ActivityLoginBinding activityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
            ActivityLoginModel activityLoginModel = new ActivityLoginModel(activityLoginBinding, qqLoginUiListener, this, mSsoHandler);
            activityLoginBinding.setActivityLoginModel(activityLoginModel);
        }
    }

    //    应用调用Andriod_SDK接口时，如果要成功接收到回调，需要在调用接口的Activity的onActivityResult方法中增加如下代码：
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, qqLoginUiListener);

        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public class QQLoginUiListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
//            final JSONObject joResultData = (JSONObject) o;
//            LogKit.v(joResultData.toString());
//            try {
//                final String openid = joResultData.getString("openid");
//                final String access_token = joResultData.getString("access_token");
//
//                String phone = "18915521461";
//                String pin = "123456";
//                String _3pToken = access_token + "&" + openid + "&" + GlobalConstants.LoginPlatformType.QQ;
//                String userInfo = " & & & & ";
//                LoginManager.loginPhoneNum(phone, pin, _3pToken, userInfo);
//
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        JSONObject joUserInfo = null;
//                        try {
////                            joUserInfo = LoginManager.mTencent.request("get_user_info", null, Constants.HTTP_GET);
//                            joUserInfo = LoginManager.mTencent.request("get_user_info", null, "GET");
//                            LogKit.v(joUserInfo.toString());
//
////                            QQToken qqToken = new QQToken(access_token);
////                            qqToken.setOpenId(openid);
//                            UserInfo userinfo = new UserInfo(LoginActivity.this, LoginManager.mTencent.getQQToken());
//                            userinfo.getUserInfo(new IUiListener() {
//                                @Override
//                                public void onComplete(Object o) {
//                                    JSONObject jj = (JSONObject) o;
//                                    LogKit.v(jj.toString());
//                                }
//
//                                @Override
//                                public void onError(UiError uiError) {
//
//                                }
//
//                                @Override
//                                public void onCancel() {
//
//                                }
//                            });
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//
//            } catch (JSONException ex) {
//
//            }
        }

        @Override
        public void onError(UiError uiError) {

        }

        @Override
        public void onCancel() {

        }
    }

    //检测版本更新
    private void versionUpdate() {
        //弹出对话框
        showUpdateDialog();
    }

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);//点击旁边不会消失
        builder.setTitle("版本更新");
        builder.setMessage("请判断是否进行版本跟新？");
        builder.setPositiveButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 去下载APK
                downLoadApk();
            }
        });

        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 直接进入主页
                //  enterHome();
            }
        });
        builder.show();
    }


    // 下载APK方法
    protected void downLoadApk() {
        //在后端下载APK,xUtuils

        //校验是否有SD卡
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "没有SD卡！", Toast.LENGTH_SHORT).show();
            // enterHome();
            return;
        }

        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(false);
        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDialog.show();
        // 提示用户安装
        new Thread(new DownLoadTask()).start();

    }

    private class DownLoadTask implements Runnable {
        @Override
        public void run() {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
               /* Response response = HttpUtil.get(mUrl);
                is = response.body().byteStream();*/

                // 找到本地的文件存储位置 mnt/sdcard/...
                File rootFile = Environment.getExternalStorageDirectory();
                File file = new File(rootFile, "SlashYouth.apk");
                fos = new FileOutputStream(file);

                // 设置下载进度条的最大值
               /* long contentLength = response.body().contentLength();
                mDialog.setMax((int) contentLength);
*/
                byte[] buffer = new byte[1024];
                int len = -1;
                int progress = 0;
                while ((len = is.read(buffer)) != -1) {

                    fos.write(buffer, 0, len);
                    // 设置进度条当前进度
                    progress += len;
                    mDialog.setProgress(progress);
                }

                fos.flush();
                mDialog.dismiss();
                LogKit.d("download is successful");
                // 是否安装
                installApk(file);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // 关流
                closeIo(is);
                closeIo(fos);
            }
        }
    }

    // 安装APK的方法
    public void installApk(File file) {
        // <intent-filter>
        // <action android:name="android.intent.action.VIEW" />
        // <category android:name="android.intent.category.DEFAULT" />
        // <data android:scheme="content" />
        // <data android:scheme="file" />
        // <data android:mimeType="application/vnd.android.package-archive" />
        // </intent-filter>
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri data = Uri.fromFile(file);
        //   file:///mnt/sdcard/safe.apk
        //  Log.d(TAG, "uri:" + data);
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        // startActivityForResult(intent, REQUEST_CODE);
    }

    // 关流
    public void closeIo(Closeable io) {
        if (io != null) {
            try {
                io.close();
                io = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
