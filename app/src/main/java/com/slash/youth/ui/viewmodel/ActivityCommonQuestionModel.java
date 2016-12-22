package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.slash.youth.databinding.ActivityCommonQuestionBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import java.net.URL;

/**
 * Created by zss on 2016/12/21.
 */
public class ActivityCommonQuestionModel extends BaseObservable {
    private ActivityCommonQuestionBinding activityCommonQuestionBinding;

    public ActivityCommonQuestionModel(ActivityCommonQuestionBinding activityCommonQuestionBinding) {
        this.activityCommonQuestionBinding = activityCommonQuestionBinding;
      //  initView();
    }

    private void initView() {
        activityCommonQuestionBinding.wvOauth.loadUrl("http://121.42.145.178/#!/program?type=program");

        activityCommonQuestionBinding.wvOauth.setWebViewClient(new WebViewClient(){
            //网页加载开始时调用，显示加载提示旋转进度条
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
                activityCommonQuestionBinding.progressBar.setVisibility(android.view.View.VISIBLE);
//                Toast.makeText(ElecHall.this, "onPageStarted", 2).show();
            }

            //网页加载完成时调用，隐藏加载提示旋转进度条
            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onPageFinished(view, url);
                activityCommonQuestionBinding.progressBar.setVisibility(android.view.View.GONE);
//                Toast.makeText(ElecHall.this, "onPageFinished", 2).show();
            }
            //网页加载失败时调用，隐藏加载提示旋转进度条
            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                super.onReceivedError(view, errorCode, description, failingUrl);
                activityCommonQuestionBinding.progressBar.setVisibility(android.view.View.GONE);
//                Toast.makeText(ElecHall.this, "onReceivedError", 2).show();
            }

        });
    }
}
