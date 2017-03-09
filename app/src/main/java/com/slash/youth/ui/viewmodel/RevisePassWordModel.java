package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.slash.youth.databinding.ActivityRevisePasswordBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zss on 2016/11/3.
 */
public class RevisePassWordModel extends BaseObservable {
    private ActivityRevisePasswordBinding activityRevisePasswordBinding;
    public Map<String, String> oldPassWordMap = new HashMap<>();
    public Map<String, String> newPassWordMap = new HashMap<>();
    public Map<String, String> surePassWordMap = new HashMap<>();
    private Activity mActivity;

    public RevisePassWordModel(ActivityRevisePasswordBinding activityRevisePasswordBinding, Activity activity) {
        this.activityRevisePasswordBinding = activityRevisePasswordBinding;
        this.mActivity = activity;
        initData();
        listener();
    }

    private void initData() {
    }

    private void listener() {
        getOldPassWord(activityRevisePasswordBinding.etOldPassword);
        getNewPassWord(activityRevisePasswordBinding.etNewPassword);
        getSurePassWord(activityRevisePasswordBinding.etCheckNewPassword);
    }

    //原来的密码
    private void getOldPassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passWord = s.toString();
                oldPassWordMap.put("oldpass", passWord);
            }
        });
    }

    //新的密码
    private void getNewPassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passWord = s.toString();
                newPassWordMap.put("newpass", passWord);
            }
        });
    }

    //确认密码
    private void getSurePassWord(EditText v) {
        v.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String passWord = s.toString();
                surePassWordMap.put("surepass", passWord);
            }
        });
    }

    //修改新的密码
    public void setNewPassWord(String oldpass, String newpass) {
        ContactsManager.onSetNewPassWord(new onSetNewPassWord(), oldpass, newpass);
    }

    //创建交易密码
    public class onSetNewPassWord implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status) {
                    case 1:
                        LogKit.d("设置成功");
                        ToastUtils.shortCenterToast("修改成功");
                        CommonUtils.getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mActivity.finish();
                            }
                        }, 1000);
                        break;
                    case 0:
                        LogKit.d("设置失败");
                        ToastUtils.shortCenterToast("修改失败");
                        break;
                    case 2:
                        ToastUtils.shortCenterToast("修改失败,新密码和原密码一样");
                        break;
                }
            } else {
                ToastUtils.shortToast("修改交易密码失败");
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }
}
