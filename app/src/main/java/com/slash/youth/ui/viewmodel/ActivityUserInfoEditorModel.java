package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.ui.activity.EditorIdentityActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by acer on 2016/10/31.
 */
public class ActivityUserInfoEditorModel extends BaseObservable {
    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;

    public ActivityUserInfoEditorModel(ActivityUserinfoEditorBinding activityUserinfoEditorBinding) {
        this.activityUserinfoEditorBinding = activityUserinfoEditorBinding;
    }


    public void editorIdentity(View view){
        Intent editorIdentityActivity = new Intent(CommonUtils.getContext(),EditorIdentityActivity.class);
        editorIdentityActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(editorIdentityActivity);
    }

}
