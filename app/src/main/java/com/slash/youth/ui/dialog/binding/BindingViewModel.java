package com.slash.youth.ui.dialog.binding;

import android.app.Activity;
import android.content.Intent;
import android.databinding.ObservableField;
import android.view.View;

import com.slash.youth.databinding.DialogBindingBinding;
import com.slash.youth.databinding.DialogOfflineBinding;
import com.slash.youth.domain.LoginBindBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.LoginUnBindProtocol;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.dialog.base.BDialogViewModel;
import com.slash.youth.ui.dialog.base.BViewModel;
import com.slash.youth.ui.dialog.base.OnDialogLisetener;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DateUtil;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by acer on 2017/3/1.
 */

public class BindingViewModel extends BDialogViewModel<DialogBindingBinding> {
    public BindingViewModel(Activity activity, OnDialogLisetener onDialogLisetener) {
        super(activity, onDialogLisetener);
    }

    public void cancel(View view) {
        if (onDialogLisetener != null) {
            onDialogLisetener.onCancel();
        }
    }

    public void consult(View view) {
        if (onDialogLisetener != null) {
            onDialogLisetener.onConfirm();
        }
    }
}
