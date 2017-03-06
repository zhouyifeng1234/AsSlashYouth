package com.slash.youth.ui.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.view.fly.RandomLayout;
import com.slash.youth.ui.viewmodel.ActivityUserInfoModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by zss on 2016/10/31.
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    private ActivityUserinfoBinding activityUserinfoBinding;
    private PopupWindow popupWindow;
    private ActivityUserInfoModel userInfoModel;
    private long myId;
    private String phone;
    private long uid;
    private int anonymity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        uid = intent.getLongExtra("Uid", -1);
        String skillTag = intent.getStringExtra("skillTag");
        anonymity = intent.getIntExtra("anonymity", 1);
        activityUserinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo);
        userInfoModel = new ActivityUserInfoModel(activityUserinfoBinding, uid, this, skillTag, anonymity);
        activityUserinfoBinding.setActivityUserInfoModel(userInfoModel);
        back();
        title();
        listener();
    }

    private void title() {
        userInfoModel.setOnNameListener(new ActivityUserInfoModel.OnNameListener() {
            @Override
            public void OnNameListener(String name, long myUid, String company) {
                setTitle(userInfoModel.isOther, name, company);
                myId = myUid;
            }
        });
    }

    private void setTitle(boolean isOther, String name, String company) {
        if (isOther) {//我看其他人
            activityUserinfoBinding.tvUserinfoTitle.setText(name);
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.GONE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.VISIBLE);
            switch (anonymity) {
                case 1://实名
                    break;
                case 0://匿名
                    activityUserinfoBinding.ivUserinfoUsericon.setImageResource(R.mipmap.anonymity_avater);
                    break;
            }
        } else {
            activityUserinfoBinding.tvUserinfoTitle.setText(ContactsManager.USER_INFO);
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.VISIBLE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.GONE);
        }
    }

    private void back() {
        activityUserinfoBinding.ivUserinfoBack.setOnClickListener(new View.OnClickListener() {
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
        switch (v.getId()) {
            case R.id.tv_userinfo_save:
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_PERSON_MESSAGE_CLICK_EDIT);

                Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
                intentUserinfoEditorActivity.putExtra("phone", phone);
                intentUserinfoEditorActivity.putExtra("myId", myId);
                UserInfoActivity.this.startActivity(intentUserinfoEditorActivity);
                break;
            case R.id.iv_userinfo_menu:
                showPopupWindow(v);
                break;
            case R.id.tv_reportTA:
                Intent intentReportTAActivity = new Intent(CommonUtils.getContext(), ReportTAActivity.class);
                intentReportTAActivity.putExtra("uid", userInfoModel.otherUid);
                intentReportTAActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentReportTAActivity);
                popupWindow.dismiss();
                break;
            case R.id.tv_shieldTA:
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
                return false;
            }
        });
        popupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.mipmap.dropdown_box));

        popupWindow.showAsDropDown(v);
    }

    //添加返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        int visibility = activityUserinfoBinding.flSourceAvatarLayer.getVisibility();
        if (visibility == View.VISIBLE) {
            activityUserinfoBinding.flSourceAvatarLayer.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }
}
