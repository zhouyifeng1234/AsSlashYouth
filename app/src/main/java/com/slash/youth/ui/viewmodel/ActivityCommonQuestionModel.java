package com.slash.youth.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommonQuestionBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.WebViewActivity;
import com.slash.youth.utils.LogKit;

import java.net.URL;

/**
 * Created by zss on 2016/12/21.
 */
public class ActivityCommonQuestionModel extends BaseObservable {
    private ActivityCommonQuestionBinding activityCommonQuestionBinding;
    private WebView wv;
    private WebSettings settings;
    private String webUrl;
    private WebViewActivity webViewActivity;

    public ActivityCommonQuestionModel(ActivityCommonQuestionBinding activityCommonQuestionBinding,String webUrl,WebViewActivity webViewActivity) {
        this.activityCommonQuestionBinding = activityCommonQuestionBinding;
        this.webUrl = webUrl;
        this.webViewActivity =webViewActivity;
        initView();
    }

    private void initView() {
        wv = activityCommonQuestionBinding.wvOauth;

        settings = wv.getSettings();  //获取设置
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false//这一句一定要加上去
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        settings.setUseWideViewPort(true);//设置此属性，可任意比例缩放。大视图模式
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setDomStorageEnabled(true);//DOM Storage

        //设置加载的进度条
        activityCommonQuestionBinding.wvOauth.setWebViewClient(new WebViewClient(){
            //网页加载开始时调用，显示加载提示旋转进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                activityCommonQuestionBinding.progressBar.setVisibility(android.view.View.VISIBLE);
            }

            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                activityCommonQuestionBinding.progressBar.setVisibility(android.view.View.GONE);
            }
            //网页加载失败时调用，隐藏加载提示旋转进度条
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                activityCommonQuestionBinding.progressBar.setVisibility(android.view.View.GONE);
            }

        });

        //Android6.0动态开启权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wv.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //nav = 1是没有头布局
        wv.loadUrl(webUrl);

    }

}
