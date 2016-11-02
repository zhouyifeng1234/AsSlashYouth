package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;

import com.slash.youth.databinding.ActivityEditorIdentityBinding;

/**
 * Created by acer on 2016/11/1.
 */
public class EditorIdentityModel extends BaseObservable {

    private ActivityEditorIdentityBinding activityEditorIdentityBinding;

    public EditorIdentityModel(ActivityEditorIdentityBinding activityEditorIdentityBinding) {
        this.activityEditorIdentityBinding = activityEditorIdentityBinding;
        initData();
        initView();

    }

    private void initData() {

    }

    private void initView() {
        //先做添加标签
        //View.inflate()

        //点击，动态穿件标签

        //新穿件的在添加在里面



    }
}
