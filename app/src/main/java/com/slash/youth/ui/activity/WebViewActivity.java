package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommonQuestionBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ActivityCommonQuestionModel;
import com.slash.youth.ui.viewmodel.ContactUsModel;
import com.slash.youth.ui.viewmodel.MyHelpModel;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/12/21.
 */
public class WebViewActivity extends Activity implements View.OnClickListener {
    private ActivityCommonQuestionBinding activityCommonQuestionBinding;
    private String webUrl;
    private TextView title;
    private String questionTitle  = "常见问题";
    private String influenceTitle  = "影响力";
    private FrameLayout fl;
    private String influence;
    private String commonQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        influence = intent.getStringExtra("influence");
        if(influence !=null){
            webUrl = GlobalConstants.WebPath.WEB_INFLUENCE;
        }
        commonQuestion = intent.getStringExtra("commonQuestion");
        if(commonQuestion !=null){
            webUrl = GlobalConstants.WebPath.WEB_COMMON_QUESTION;
        }

        activityCommonQuestionBinding = DataBindingUtil.setContentView(this, R.layout.activity_common_question);
        ActivityCommonQuestionModel activityCommonQuestionModel = new ActivityCommonQuestionModel(activityCommonQuestionBinding,webUrl,this);
        activityCommonQuestionBinding.setActivityCommonQuestionModel(activityCommonQuestionModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        if(influence!=null){
            title.setText(influenceTitle);
        }
        if(commonQuestion!=null){
            title.setText(questionTitle);
        }
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }
}
