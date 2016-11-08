package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.ui.viewmodel.ActivityUserInfoEditorModel;

/**
 * Created by zss on 2016/11/1.
 */
public class UserinfoEditorActivity extends Activity {
    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserinfoEditorBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo_editor);
        ActivityUserInfoEditorModel activityUserInfoEditorModel = new ActivityUserInfoEditorModel(activityUserinfoEditorBinding);
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


}