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
import com.slash.youth.utils.SpUtils;

/**
 * Created by zss on 2016/10/31.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {
    private ActivityUserinfoBinding activityUserinfoBinding;
    private PopupWindow popupWindow;
    private ActivityUserInfoModel userInfoModel;
    private  long myId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        long uid = intent.getLongExtra("Uid", -1);//这是其他用的id
        activityUserinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo);
        userInfoModel = new ActivityUserInfoModel(activityUserinfoBinding,uid);
        activityUserinfoBinding.setActivityUserInfoModel(userInfoModel);

        back();

        title();

        listener();
    }

    private void title() {
        userInfoModel.setOnNameListener(new ActivityUserInfoModel.OnNameListener() {
            @Override
            public void OnNameListener(String name ,long myUid) {
                setTitle(userInfoModel.isOther,name);
                myId = myUid;

            }
        });
    }

    private void setTitle(boolean isOther,String name) {

        if(isOther){
            activityUserinfoBinding.tvUserinfoTitle.setText(name);
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.GONE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.VISIBLE);

        }else  {
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
                //LogKit.d("跳转到编辑页面");
                Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
                intentUserinfoEditorActivity.putExtra("myId",myId);
                intentUserinfoEditorActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentUserinfoEditorActivity);
                break;
            case R.id.iv_userinfo_menu:
               // LogKit.d("弹出弹框");
                showPopupWindow(v);
                break;
            case R.id.tv_reportTA:
                Intent intentReportTAActivity = new Intent(CommonUtils.getContext(), ReportTAActivity.class);
                intentReportTAActivity.putExtra("uid", userInfoModel.otherId);
                intentReportTAActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentReportTAActivity);
                popupWindow.dismiss();
                break;
            case R.id.tv_shieldTA:
               // LogKit.d("屏蔽他");
                popupWindow.dismiss();
                break;
        }
    }

    private void showPopupWindow(View v) {
        View contentView = LayoutInflater.from(UserInfoActivity.this).inflate(
                R.layout.pop_window, null);

        contentView.findViewById(R.id.tv_reportTA).setOnClickListener(this);
        contentView.findViewById(R.id.tv_shieldTA).setOnClickListener(this);

        popupWindow = new PopupWindow(contentView,
                RandomLayout.LayoutParams.WRAP_CONTENT, RandomLayout.LayoutParams.WRAP_CONTENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
              /*  if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }*/

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
