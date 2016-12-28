package com.slash.youth.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.slash.youth.R;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.TokenLoginResultBean;
import com.slash.youth.domain.VersionBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckVersionProtocol;
import com.slash.youth.ui.adapter.PagerMoreDemandtAdapter;
import com.slash.youth.ui.view.fly.RandomLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PackageUtil;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.TimeUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/12/11.
 */
public class SplashActivity extends Activity {
    private int type = 1;//1表示android,2表示ios
    private int versionCode;
    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView ivSplash = new ImageView(CommonUtils.getContext());
        ivSplash.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams ll = new RandomLayout.LayoutParams(-1, -1);
        ivSplash.setLayoutParams(ll);
        ivSplash.setImageResource(R.mipmap.splash);
        setContentView(ivSplash);

        CommonUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tokenLogin();
                //checkVersion();
            }
        }, 3000);
    }

    /**
     * token自动登录
     */
    private void tokenLogin() {
        final String rongToken = SpUtils.getString("rongToken", "");//通过融云connect方法去检测是否还有效，如果有效，就直接连接，如果无效，重新从服务端接口获取
        final long uid = SpUtils.getLong("uid", -1);
        final String token = SpUtils.getString("token", "");
        LoginManager.token = token;
        LogKit.v("token:" + token + " uid:" + uid);
        if (!TextUtils.isEmpty(token)) {
            //如果本地保存的token不为空，尝试用token自动登录
            LoginManager.tokenLogin(new BaseProtocol.IResultExecutor<TokenLoginResultBean>() {
                @Override
                public void execute(TokenLoginResultBean dataBean) {
                    //页面跳转
                    if (uid != -1) {
                        LogKit.v("token登录成功，直接跳转到首页");
                        LoginManager.currentLoginUserId = uid;
                        LoginManager.token = token;
                        LoginManager.rongToken = rongToken;
                        //链接融云
                        MsgManager.connectRongCloud(LoginManager.rongToken);

                        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
                        startActivity(intentHomeActivity);
                        finish();
                    } else {
                        gotoLoginActivity();
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.v("token登录失败");
                    gotoLoginActivity();
                }
            });
        } else {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {
        Intent intentLoginActivity = new Intent(CommonUtils.getContext(), LoginActivity.class);
        startActivity(intentLoginActivity);
        finish();
    }

    //检测版本
    private void checkVersion() {
        // 获取当前的版本号
        versionCode = PackageUtil.getVersionCode(this);
        //获取网络的版本号
        LoginManager.checkVersion(new onCheckVersion(),type, versionCode);
    }

    //检测版本
    public class onCheckVersion implements BaseProtocol.IResultExecutor<VersionBean> {
        @Override
        public void execute(VersionBean data) {
            int rescode = data.rescode;
            if(rescode == 0){
                VersionBean.DataBean versionData = data.getData();
                long NetVersionCode = versionData.getCode();//服务端更新的版本
                long cts = versionData.getCts();
                String versionTime = TimeUtils.getTimeData(cts);//发版本时间
                String version = versionData.getVersion();//展示给用户的新版本信息
                String content = versionData.getContent();////更新文案
                int forceupdate = versionData.getForceupdate();//是否强制更新 0表示不强制，1表示强制
                //int type = versionData.getType();//1表示android,2表示ios
                String url = versionData.getUrl(); //下载app的url

                if (NetVersionCode > versionCode) {
                    // 检测版本更新
                    versionUpdate(forceupdate,version);
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }

        //检测版本更新
        private void versionUpdate(int forceupdate,String version) {
            switch (forceupdate){
                case 0:// 0表示不强制

                    break;
                case 1://1表示强制

                    break;
            }

            //弹出对话框
            //showUpdateDialog();
        }

      /*  private void showUpdateDialog() {
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
                Response response = HttpUtil.get(mUrl);
                is = response.body().byteStream();

                    // 找到本地的文件存储位置 mnt/sdcard/...
                    File rootFile = Environment.getExternalStorageDirectory();
                    File file = new File(rootFile, "SlashYouth.apk");
                    fos = new FileOutputStream(file);

                    // 设置下载进度条的最大值
                long contentLength = response.body().contentLength();
                mDialog.setMax((int) contentLength);
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
                    IOUtils.close(is);
                    IOUtils.close(fos);
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
        }*/
    }
}
