package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.FloatViewBinding;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ChooseFriendActivtiy;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.ui.adapter.UserInfoAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/1.
 */
public class ActivityUserInfoModel extends BaseObservable {

    private ActivityUserinfoBinding activityUserinfoBinding;
    private  ArrayList<UserInfoItemBean> userInfoListView = new ArrayList<>();
    private UserInfoAdapter userInfoAdapter;
    private FloatViewBinding floatViewBinding;
    private String slashIdentity = "暂未填写";//默认
    private String defaultArea  = "暂未填写";

    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        initData();
        initView();
    }

    //加载界面
    private void initView() {
    //加载Listview布局
    userInfoAdapter = new UserInfoAdapter(userInfoListView);
    activityUserinfoBinding.lvUserinfo.setAdapter(userInfoAdapter);

    //手动ScrollView设置到顶部
    activityUserinfoBinding.sv.smoothScrollTo(0,0);

    //listview的设置不可滑动
    activityUserinfoBinding.lvUserinfo.setOnTouchListener(new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    });

    //tv_userInfo_company
        updateUserInfo();


    }



    //加载数据
    private void initData() {
        //网络获取数据
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
        userInfoListView.add(new UserInfoItemBean(true));
    }

    //加好友
    public void addFriend(View view) {
        LogKit.d("好友页面");
        Intent intentChooseFriendActivtiy = new Intent(CommonUtils.getContext(), ChooseFriendActivtiy.class);
        intentChooseFriendActivtiy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentChooseFriendActivtiy);
    }

    //点击打开技能标签页面
    public void openskilllabel(View view) {
        LogKit.d("打开技能标签页面，去选择标签");

    }

    //更新用户信息
    private void updateUserInfo() {

        //去认证,认证流程结束后，去认证yingc,V出现
        activityUserinfoBinding.tvUserinfoApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
                intentApprovalActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentApprovalActivity);

            }
        });







        //不是自雇者没有填写公司
        //activityUserinfoBinding.tvUserInfoCompany.setText("暂未填写职务信息");
        //如果是自雇者
        //activityUserinfoBinding.tvUserInfoCompany.setText("自雇者");


        //第一次进来，展示的是注册时的行业和职能，但是我可以修改，再次进来时，展示我修改的
        //String profession ="";
        //activityUserinfoBinding.tvUserInfoProfession.setText(profession);

        //用户姓名
        //activityUserinfoBinding.tvUserinfoUsername.setText("用户姓名啊");

        //用户头像
        //activityUserinfoBinding.ivUserinfoUsericon.setImageResource();

        //用户是否认证,默认不是认证的
        //activityUserinfoBinding.ivUserinfoV.setVisibility(View.VISIBLE);

        //用户身份，是否是专家，专家几级,默认是不显示
        //activityUserinfoBinding.tvUserinfoIdentity.setVisibility(View.GONE);
        //activityUserinfoBinding.tvUserinfoIdentity.setText("专家4级");

        //斜杠身份  没填写斜杠身份时，显示暂未填写 ,如果有的话，就填写
        //activityUserinfoBinding.tvSlashIdentity.setText("斜杠身份:"+slashIdentity);

        //地点,没有所在地时，显示用户的定位信息，没有定位信息时，显示暂未填写，
       // activityUserinfoBinding.tvUserinfoArea.setText("地点");
       // activityUserinfoBinding.tvUserinfoArea.setText(defaultArea);

        //技能描述,从编辑页面传过来
        //activityUserinfoBinding.tvUserinfoSkilldescribe.setText();

        //行业方向
        //activityUserinfoBinding.tvProfession.setText();

        //技能标签，一开始是注册时传过来的三个标签，点击到技能标签页面修改，点击后修改完的显示
        //标签的数量是(0.3],默认全不显示
      /*  boolean isShow1 = true;
        boolean isShow2 = true;
        boolean isShow3 = true;
        String skillLabel1 ="技能标签1";
        String skillLabel2 ="技能标签2";
        String skillLabel3 ="技能标签3";
        if(isShow1||isShow2||isShow3){
            activityUserinfoBinding.tvSkilllabel1.setVisibility(isShow1 ?View.VISIBLE:View.GONE);
            activityUserinfoBinding.tvSkilllabel2.setVisibility(isShow2 ?View.VISIBLE:View.GONE);
            activityUserinfoBinding.tvSkilllabel3.setVisibility(isShow3 ?View.VISIBLE:View.GONE);
            activityUserinfoBinding.tvSkilllabel1.setText(isShow1?skillLabel1:null);
            activityUserinfoBinding.tvSkilllabel2.setText(isShow2?skillLabel2:null);
            activityUserinfoBinding.tvSkilllabel3.setText(isShow3?skillLabel3:null);
        }*/

      









    }


}
