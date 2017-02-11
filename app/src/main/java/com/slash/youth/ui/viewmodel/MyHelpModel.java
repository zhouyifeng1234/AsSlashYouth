package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyHelpBinding;
import com.slash.youth.databinding.DialogVersionUpdateBinding;
import com.slash.youth.ui.activity.SplashActivity;
import com.slash.youth.ui.activity.WebViewActivity;
import com.slash.youth.ui.activity.ContactUsActivity;
import com.slash.youth.ui.activity.MyHelpActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.IOException;

/**
 * Created by zss on 2016/11/4.
 */
public class MyHelpModel extends BaseObservable {

    private ActivityMyHelpBinding activityMyHelpBinding;
    private DialogVersionUpdateBinding dialogVersionUpdateBinding;
    private AlertDialog dialogVersion;
    private ProgressDialog mDialog;
    private ProgressDialog pDialog;
    private MyHelpActivity myHelpActivity;
    private String text1 = " 我们为深耕于互联网行业各岗位的专业人才，提供更便捷的变现机会和更广阔的学习舞台，并获得物质、价值的双重回报。";
    private String text2 ="同时为中小互联网科技企业,提供发展规划、产品设计、开发测试、运营市场、法律财务、管理人事等定制化服务,降低用工成本,提高企业市场竞争力。";
    private String downloadurl;
    private String version;


    public MyHelpModel(ActivityMyHelpBinding activityMyHelpBinding,MyHelpActivity myHelpActivity) {
        this.activityMyHelpBinding = activityMyHelpBinding;
        this.myHelpActivity = myHelpActivity;
        initView();
    }

    private void initView() {
        version = SpUtils.getString("version", "v1.0");
        if(TextUtils.isEmpty(version)||version.equals("")){
            version = "v1.0" ;
        }
        activityMyHelpBinding.tvVersion.setText("斜杠青年"+ version);
        activityMyHelpBinding.tvLineText1.setText(text1);
        activityMyHelpBinding.tvLineText2.setText(text2);
    }

    // 常见问题
    public void commonQuestion(View view){
        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), WebViewActivity.class);
        intentCommonQuestionActivity.putExtra("commonQuestion","commonQuestion");
        myHelpActivity.startActivity(intentCommonQuestionActivity);
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_HELP_COMMON_PROBLEM);
    }

    // 联系我们
    public void contactUs(View view){
       Intent intentContactUsActivity = new Intent(CommonUtils.getContext(), ContactUsActivity.class);
        myHelpActivity.startActivity(intentContactUsActivity);

        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_HELP_CONTACT_US);
    }

     // 版本更新
    public void updateVersion(View view){
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_HELP_VERSION_UPDATE);

        proDialogShow(myHelpActivity,"正在检测......");
        CommonUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                PDialogHide();

                downloadurl = SpUtils.getString("downloadurl", "none");
                if(downloadurl.equals("none")){
                    ToastUtils.shortToast("目前最新版本");
                }else {
                    //downloadurl = "http://dldir1.qq.com/weixin/android/weixin653android980.apk";//用来测试
                    showVersionUpdateDialog(1, downloadurl);
                }
            }
        }, 2000);
    }

    private void showVersionUpdateDialog(int forceupdate, String url) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myHelpActivity);
        dialogVersionUpdateBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_version_update, null, false);
        DialogVersionUpdateModel dialogVersionUpdateModel = new DialogVersionUpdateModel(dialogVersionUpdateBinding, forceupdate, url);
        dialogVersionUpdateBinding.setDialogVersionUpdateModel(dialogVersionUpdateModel);
        dialogBuilder.setView(dialogVersionUpdateBinding.getRoot());
        dialogVersion = dialogBuilder.create();
        dialogVersion.show();
        dialogVersion.setCanceledOnTouchOutside(false);
        Window dialogSubscribeWindow = dialogVersion.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(280);//宽度
        dialogParams.height = CommonUtils.dip2px(169);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
       // cannel();
        update(url);
    }

    private void update(final String url) {
        dialogVersionUpdateBinding.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_HELP_VERSION_UPDATE_CONFIRM_UPDATE);

                dialogVersion.dismiss();
                //下载apk
                downLoadApk(url);
            }
        });
    }

    // 下载APK方法
    protected void downLoadApk(String url) {
        //校验是否有SD卡
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            ToastUtils.shortToast("没有SD卡！");
            return;
        }

        //在后端下载APK,xUtuils
        RequestParams params = new RequestParams(url);
        params.setAutoRename(true);//断点下载
        File file = new File("/mnt/sdcard/demo.apk");
        if(!file.exists()){
            try {
                boolean newFile = file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        params.setSaveFilePath(file.getAbsolutePath());

        x.http().get(params,  new Callback.ProgressCallback<File>(){
                    @Override
                    public void onSuccess(File result) {
                        if(mDialog!=null && mDialog.isShowing()){
                            mDialog.dismiss();
                        }

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Uri data = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "SlashYouth.apk"));
                        intent.setDataAndType(data, "application/vnd.android.package-archive");
                        myHelpActivity.startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        if(mDialog!=null && mDialog.isShowing()){
                            mDialog.dismiss();
                            ToastUtils.shortToast("更新失败");
                        }
                        LogKit.d("更新失败");
                        ex.printStackTrace();
                    }

                    @Override
                    public void onCancelled(CancelledException cex) {
                    }

                    @Override
                    public void onFinished() {
                    }

                    @Override
                    public void onWaiting() {
                    }

                    @Override
                    public void onStarted() {
                        mDialog = new ProgressDialog(myHelpActivity);
                        mDialog.setCancelable(false);
                        mDialog.setMessage("正在下载中...");
                        mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        mDialog.setProgress(0);
                        mDialog.show();
                    }

                    @Override
                    public void onLoading(long total, long current, boolean isDownloading) {
                        mDialog.setMax(100);
                        int progress = (int) (current*100 / total);
                        mDialog.setProgress(progress);
                    }
                }
        );
    }

    private void proDialogShow(Context context, String msg) {
                pDialog = new ProgressDialog(context);
                 pDialog.setMessage(msg);
                 // pDialog.setCancelable(false);
                 pDialog.show();
             }

    private void PDialogHide() {
        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
