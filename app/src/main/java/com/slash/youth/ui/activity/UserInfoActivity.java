package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.databinding.FloatViewBinding;
import com.slash.youth.ui.view.fly.RandomLayout;
import com.slash.youth.ui.viewmodel.ActivityUserInfoModel;
import com.slash.youth.ui.viewmodel.FloatViewModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/10/31.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {


    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;
    private ActivityUserinfoBinding activityUserinfoBinding;
    private FloatViewBinding floatViewBinding;
    private ImageView menu;
    private TextView save;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //这是我的视角编辑按钮界面
      /*  activityUserinfoEditorBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo_editor);
        ActivityUserInfoEditorModel activityUserInfoEditorModel = new ActivityUserInfoEditorModel(activityUserinfoEditorBinding);
        activityUserinfoEditorBinding.setActivityUserInfoEditorModel(activityUserInfoEditorModel);*/


        activityUserinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo);
        ActivityUserInfoModel userInfoModel = new ActivityUserInfoModel(activityUserinfoBinding);
        activityUserinfoBinding.setActivityUserInfoModel(userInfoModel);

        back();

        title();

        listener();
    }

    private void title() {
        //setTitle("李小四");
    }

    private void setTitle(String name) {

        if(name!=null){
            activityUserinfoBinding.tvUserinfoTitle.setText(name);
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.GONE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.VISIBLE);

        }else {
            activityUserinfoBinding.tvUserinfoTitle.setText("个人信息");
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.VISIBLE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.GONE);
        }
    }

    private void back() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void listener() {
        activityUserinfoBinding.ivUserinfoMenu.setOnClickListener(this);
        activityUserinfoBinding.tvUserinfoSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_userinfo_save:
                LogKit.d("跳转到编辑页面");
                Intent UserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
                UserinfoEditorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(UserinfoEditorActivity);
                break;
            case R.id.iv_userinfo_menu:
                LogKit.d("弹出弹框");
                showPopupWindow(v);
                break;
        }

    }

    private void showPopupWindow(View v) {
        View contentView = LayoutInflater.from(UserInfoActivity.this).inflate(
                R.layout.pop_window, null);

        popupWindow = new PopupWindow(contentView,
                RandomLayout.LayoutParams.WRAP_CONTENT, RandomLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.mipmap.dropdown_box));

        popupWindow.showAsDropDown(v);

    }
}
