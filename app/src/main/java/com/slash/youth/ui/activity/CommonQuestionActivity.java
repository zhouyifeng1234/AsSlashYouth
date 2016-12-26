package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommonQuestionBinding;
import com.slash.youth.ui.viewmodel.ActivityCommonQuestionModel;
import com.slash.youth.ui.viewmodel.ContactUsModel;
import com.slash.youth.ui.viewmodel.MyHelpModel;

/**
 * Created by zss on 2016/12/21.
 */
public class CommonQuestionActivity extends Activity  {
    private TextView title;
    private String titleString  = "常见问题";
    private FrameLayout fl;
    private ActivityCommonQuestionBinding activityCommonQuestionBinding;
    private WebView wv;
    private WebSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCommonQuestionBinding = DataBindingUtil.setContentView(this, R.layout.activity_common_question);
        ActivityCommonQuestionModel activityCommonQuestionModel = new ActivityCommonQuestionModel(activityCommonQuestionBinding);
        activityCommonQuestionBinding.setActivityCommonQuestionModel(activityCommonQuestionModel);
        listener();
    }

    private void listener() {
      /*  findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(titleString);
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);*/

        wv = (WebView) findViewById(R.id.wv_oauth);
        settings = wv.getSettings();
        // 浏览器不支持多窗口显示
        settings.setSupportMultipleWindows(false);
        // 页面是否可以进行缩放
        settings.setSupportZoom(false);
        // 使用WebViewClient的特性处理html页面
       // wv.setWebViewClient(new MyWebViewClient());
        settings.setDomStorageEnabled(true);
        wv.loadUrl("http://121.42.145.178/#!/program?type=program");


     /*   Uri uri = Uri.parse("http://121.42.145.178/#!/program?type=program");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);*/

    }

 /*   @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }*/

}
