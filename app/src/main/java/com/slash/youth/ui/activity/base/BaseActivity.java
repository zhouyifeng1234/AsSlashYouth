package com.slash.youth.ui.activity.base;

import android.app.Activity;
import android.view.KeyEvent;

import com.slash.youth.ui.dialog.offline.OfflineDialog;
import com.slash.youth.ui.dialog.offline.OfflineViewModel;

/**
 * Created by zhouyifeng on 2017/2/25.
 */
public class BaseActivity extends Activity {

    private OfflineDialog offlineDialog;

    private boolean isOffline = false;

    public void offline() {
        if (offlineDialog == null) {
            offlineDialog = new OfflineDialog(this, new OfflineViewModel(this));
        }
        if (!offlineDialog.isShowing()) {
            offlineDialog.show();
        }
        isOffline = true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && isOffline)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
