package com.slash.youth.ui.dialog.offline;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.icu.text.SimpleDateFormat;
import android.view.View;

import com.slash.youth.databinding.DialogOfflineBinding;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.dialog.base.BViewModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DateUtil;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by acer on 2017/3/1.
 */

public class OfflineViewModel extends BViewModel<DialogOfflineBinding> {

    public static final ObservableField<String> info = new ObservableField<>();

    public OfflineViewModel(Activity activity) {
        super(activity);
        info.set("您的账号于" + DateUtil.getCurrentTimeCn() + "在其他设备上登录。如非本人操作，请重新登录或联系客服。");
    }

    public void toLogin(View view) {
        ArrayList<Activity> listActivities = ((SlashApplication) SlashApplication.getApplication()).listActivities;
        for (Activity activity : listActivities) {
            if (activity != null) {
                activity.finish();
                listActivities.remove(activity);
                activity = null;
            }
        }

        Intent intentLoginActivity = new Intent(CommonUtils.getContext(), LoginActivity.class);
        activity.startActivity(intentLoginActivity);

        LoginManager.token = "";
        LoginManager.currentLoginUserAvatar = "";
        LoginManager.currentLoginUserId = -1;
        LoginManager.currentLoginUserName = "";
        LoginManager.currentLoginUserPhone = "";

        SpUtils.setString("token", "");

    }
}
