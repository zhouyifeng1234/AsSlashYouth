package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.domain.FansBean;
import com.slash.youth.domain.FriendStatusBean;
import com.slash.youth.domain.OtherInfoBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.view.fly.RandomLayout;
import com.slash.youth.ui.viewmodel.ActivityUserInfoModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.socialize.UMShareAPI;

/**
 * Created by zss on 2016/10/31.
 */
public class UserInfoActivity extends Activity implements View.OnClickListener {
    private ActivityUserinfoBinding activityUserinfoBinding;
    private PopupWindow popupWindow;
    private ActivityUserInfoModel userInfoModel;
    private long myId;
    private String phone;
    private boolean isfriend;
    private long uid;
    private int anonymity;
    private int friendStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        uid = intent.getLongExtra("Uid", -1);
        String skillTag = intent.getStringExtra("skillTag");
        anonymity = intent.getIntExtra("anonymity", -1);
        activityUserinfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_userinfo);
        userInfoModel = new ActivityUserInfoModel(activityUserinfoBinding, uid, this, skillTag,anonymity,friendStatus);
        activityUserinfoBinding.setActivityUserInfoModel(userInfoModel);
        testIsFriend(uid);
        testisfollow(uid);
        back();
        title();
        listener();
    }

    private void title() {
        userInfoModel.setOnNameListener(new ActivityUserInfoModel.OnNameListener() {
            @Override
            public void OnNameListener(String name, long myUid) {
                setTitle(userInfoModel.isOther, name);
                myId = myUid;
            }
        });
    }

    private void setTitle(boolean isOther, String name) {

        if (isOther) {
            activityUserinfoBinding.tvUserinfoTitle.setText(name);
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.GONE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.VISIBLE);
            switch (anonymity){
                case 1://实名
                    break;
                case 0://匿名
                    activityUserinfoBinding.tvUserinfoTitle.setText(ContactsManager.ANONVMITY);
                    break;
            }
        } else {
            activityUserinfoBinding.tvUserinfoTitle.setText(ContactsManager.USER_INFO );
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
                Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
                intentUserinfoEditorActivity.putExtra("phone", phone);
                intentUserinfoEditorActivity.putExtra("myId", myId);
                UserInfoItemBean.DataBean.UinfoBean myUninfo = userInfoModel.myUninfo;
                intentUserinfoEditorActivity.putExtra("uifo",myUninfo);
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

    //先验证一下好友关系
    public void testIsFriend(long uid) {
        ContactsManager.onFriendApplicationStatus(new onFriendApplicationStatus(),uid);
    }


    //验证好友关系
    public class onTestFriendStatue implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status) {
                    case 1://好友
                        isfriend = true;
                        break;
                    case 0://非好友
                        isfriend = false;
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //验证我和某一个用户的加好友的状态
    public class onFriendApplicationStatus implements BaseProtocol.IResultExecutor<FriendStatusBean> {
        @Override
        public void execute(FriendStatusBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                FriendStatusBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 0:
                        LogKit.d("0表示陌生人");
                        friendStatus = 0;
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.ADD_FRIEND);
                        break;
                    case 1:
                        LogKit.d("表示我主动加了他，他还未回复");
                        friendStatus = 1;
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.ADD_FRIEND_APPLICATION);
                        break;
                    case 2:
                        LogKit.d("表示他主动加了我，我还未同意");
                        friendStatus = 2;
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.AFREEN_FRIEND_APPLICATION);
                        break;
                    case 3:
                        LogKit.d("表示是好友关系");
                        friendStatus = 3;
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.IS_FRIEND);
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //先验证我和某用户的关系
    public void testisfollow(long uid) {
        ContactsManager.onTestIsFollow(new onTestIsFollow(), uid);
    }

    public class onTestIsFollow implements BaseProtocol.IResultExecutor<FansBean> {
        @Override
        public void execute(FansBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                FansBean.DataBean data = dataBean.getData();
                int fans = data.getFans();
                switch (fans) {
                    case 1://1表示他是我的粉丝，0表示无关系  --他有没有关注过我
                        break;
                    case 0://无关系
                        break;
                }
                int follow = data.getFollow();
                switch (follow) {
                    case 1://1表示我是他的粉丝，0表示无关系 ==我关注他
                        activityUserinfoBinding.tvAttentionTA.setText(ContactsManager.CARE_TA_OK);
                        activityUserinfoBinding.ivCare.setImageResource(R.mipmap.attention_icon);
                        LogKit.d("==我关注过他=");
                        break;
                    case 0://无关系
                        activityUserinfoBinding.tvAttentionTA.setText(ContactsManager.CARE_TA);
                        activityUserinfoBinding.ivCare.setImageResource(R.mipmap.yi_attention_icon);
                        LogKit.d("==我没有关注过他=");
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }
}
