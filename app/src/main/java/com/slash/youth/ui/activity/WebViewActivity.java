package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommonQuestionBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.ActivityCommonQuestionModel;

/**
 * Created by zss on 2016/12/21.
 */
public class WebViewActivity extends BaseActivity implements View.OnClickListener {
    private ActivityCommonQuestionBinding activityCommonQuestionBinding;
    private String webUrl;
    private TextView title;
    private String questionTitle = "常见问题";
    private String influenceTitle = "影响力";
    /* private String bannerTitle_1  = "只有你能决定你有多优秀";
     private String bannerTitle_2  = "斜杠青年创业故事";
     private String bannerTitle_3  = "互联网行业岗位故事全纪录";*/
    private FrameLayout fl;
    private String influence;
    private String commonQuestion;
    private int bannerIndex;
    private String bannerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        bannerTitle = intent.getStringExtra("title");
        if (bannerTitle != null) {
            webUrl = intent.getStringExtra("bannerUrl");
            webUrl = webUrl.trim().replace(" ", "");
        }

        influence = intent.getStringExtra("influence");
        if (influence != null) {
            webUrl = GlobalConstants.WebPath.WEB_INFLUENCE;
        }
        commonQuestion = intent.getStringExtra("commonQuestion");
        if (commonQuestion != null) {
            webUrl = GlobalConstants.WebPath.WEB_COMMON_QUESTION;
        }

       /* bannerIndex = intent.getIntExtra("bannerIndex", -1);
        if(bannerIndex !=-1){
            switch (bannerIndex){
                case 0:
                    webUrl =GlobalConstants.WebPath.WEB_BANNER_1;
                    break;
                case 1:
                    webUrl =GlobalConstants.WebPath.WEB_BANNER_2;
                    break;
                case 2:
                    webUrl =GlobalConstants.WebPath.WEB_BANNER_3;
                    break;
            }
        }*/

        activityCommonQuestionBinding = DataBindingUtil.setContentView(this, R.layout.activity_common_question);
        ActivityCommonQuestionModel activityCommonQuestionModel = new ActivityCommonQuestionModel(activityCommonQuestionBinding, webUrl, this);
        activityCommonQuestionBinding.setActivityCommonQuestionModel(activityCommonQuestionModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);

        if (bannerTitle != null) {
            title.setText(bannerTitle);
        }

        if (influence != null) {
            title.setText(influenceTitle);
        }
        if (commonQuestion != null) {
            title.setText(questionTitle);
        }

        /*if(bannerIndex !=-1){
            switch (bannerIndex){
                case 0:
                    title.setText(bannerTitle_1);
                    break;
                case 1:
                    title.setText(bannerTitle_2);
                    break;
                case 2:
                    title.setText(bannerTitle_3);
                    break;
            }
        }*/
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }
}
