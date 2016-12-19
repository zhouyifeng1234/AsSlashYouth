package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityUserinfoHeardListviewBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;

import java.util.Observable;

/**
 * Created by zss on 2016/12/16.
 */
public class UserInfoHeardModel extends BaseObservable {
    private ActivityUserinfoHeardListviewBinding activityUserinfoHeardListviewBinding;

    public UserInfoHeardModel(ActivityUserinfoHeardListviewBinding activityUserinfoHeardListviewBinding) {
        this.activityUserinfoHeardListviewBinding = activityUserinfoHeardListviewBinding;
    }


}
