package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.FloatViewBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.NewTaskUserInfoBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.GetUserInfoProtocol;
import com.slash.youth.http.protocol.MyUserInfoProtocol;
import com.slash.youth.http.protocol.ServicePartyRejectProtocol;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ChooseFriendActivtiy;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.UserInfoAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.StringUtils;
import com.tencent.connect.UserInfo;

import org.xutils.x;

import java.util.ArrayList;
import java.util.UnknownFormatConversionException;

/**
 * Created by acer on 2016/11/1.
 */
public class ActivityUserInfoModel extends BaseObservable {
    private ActivityUserinfoBinding activityUserinfoBinding;
    private  ArrayList<NewTaskUserInfoBean> userInfoListView = new ArrayList<>();
    private UserInfoAdapter userInfoAdapter;
    private FloatViewBinding floatViewBinding;
    private String slashIdentity = "暂未填写";//默认
    private String defaultArea  = "暂未填写";
    public UserInfoItemBean.DataBean.UinfoBean uinfo;//从网络获取的个人信息
    public long  userUid = -1;//-1是自己看自己的
    public String name;
    private int expert;
    private int isauth;
    private String position;
    private String province;
    private String avatar;
    public boolean isOther;
    private String city;
    private String place;
    private String company;
    private int careertype;
    private String tag;
    private String identity;
    private StringBuffer stringBuffer = new StringBuffer();
    private int fanscount;
    private int achievetaskcount;
    private int fansratio;
    private int averageservicepoint;
    private String direction;
    private int totoltaskcount;
    private int userservicepoint;
    private long otherUid;
    public long myUid;
    public long otherId;
    private UserInfoActivity userInfoActivity;
    private String industry;
    private String desc;

    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding, long otherUid,
                                 UserInfoActivity userInfoActivity,String tag) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        this.userInfoActivity = userInfoActivity;
        this.tag = tag;
        this.otherUid = otherUid;
        initData();
        initView();

    }
    //加载数据
    private void initData() {
        if(otherUid == -1){
            //我的id
            MyManager.getMySelfPersonInfo(new OnGetMyUserinfo());
            isOther = false; //我的id
        }else {
            //其他的id
            //这是从其他页面里面传过来的，我看其他人的资料数据
            //uid是从登陆页面第一次获得uid
            MyManager.getOtherPersonInfo(new onGetOtherPersonInfo(),10001);
            isOther = true;
        }

        //网络获取数据
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
    }


    //加载界面
    private void initView() {
        //加载Listview布局
        userInfoAdapter = new UserInfoAdapter(userInfoListView);
        activityUserinfoBinding.lvUserinfo.setAdapter(userInfoAdapter);

        //让listview不获取焦点
        activityUserinfoBinding.lvUserinfo.setFocusable(false);

        //手动ScrollView设置到顶部
        activityUserinfoBinding.sv.smoothScrollTo(0,0);

        //listview的设置不可滑动
        activityUserinfoBinding.lvUserinfo.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    //加好友
    public void addFriend(View view) {
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
        //用户ID
        myUid = uinfo.getId();

        //用户头像
        avatar = uinfo.getAvatar();
        if(!avatar.isEmpty()){
            x.image().bind(activityUserinfoBinding.ivUserinfoUsericon,avatar);
        }

        //用户姓名
        name = uinfo.getName();
        activityUserinfoBinding.tvUserinfoUsername.setText(name);
        if(!name.isEmpty()){
            onNameListener.OnNameListener(name,myUid);
        }else {
            activityUserinfoBinding.tvUserinfoUsername.setText("个人信息中心");
        }

        //专家  用户身份，是否是专家，专家几级,默认是不显示
        expert = uinfo.getExpert();
        activityUserinfoBinding.tvUserinfoIdentity.setText("专家"+expert+"级");

        //城市  //省份
        city = uinfo.getCity();
        province = uinfo.getProvince();
        if(!city.equals(province)){
            place = province+""+city;
        }else {
            place = city;
        }
        activityUserinfoBinding.tvPlace.setText(place);

        //职业类型
        careertype = uinfo.getCareertype();
        if(careertype == 1){//固定职业者
            company = uinfo.getCompany();//公司
            position = uinfo.getPosition();//技术专家
            String companyAndPosition = company+"-"+position;
            if(!company.isEmpty()){
                activityUserinfoBinding.tvUserInfoCompany.setText(companyAndPosition);
            }else {
                activityUserinfoBinding.tvUserInfoCompany.setText("暂未填写职务信息");
            }
        }else if(careertype == 2){//自由职业者
            activityUserinfoBinding.tvUserInfoCompany.setText("自雇者");
        }

        //是否认证
        isauth = uinfo.getIsauth();
        if(isauth == 1){
            //认证过的
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.GONE);
            activityUserinfoBinding.ivUserinfoV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isOther){
                        OpenApprovalActivtity();
                    }
                }
            });

        }else if(isauth == 0){
            //非认证
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.VISIBLE);
            // TODO  如果没有确定职业类型，还要去编辑页面去判断



            activityUserinfoBinding.tvUserinfoApproval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isOther){
                        OpenApprovalActivtity();
                    }
                }
            });
        }


        //身份,斜杠 斜杠身份  没填写斜杠身份时，显示暂未填写 ,如果有的话，就填写
        identity = uinfo.getIdentity();
        if(!identity.contains(",")){
            if(identity.isEmpty()){
                activityUserinfoBinding.tvSlashIdentity.setText("斜杠身份: "+slashIdentity);
            }else {
                activityUserinfoBinding.tvSlashIdentity.setText("斜杠身份: "+identity);
            }
        }else {
            String[] splitIdentity = identity.split(",");
            stringBuffer.append("斜杠身份: ");
            for (int i = 0; i < splitIdentity.length; i++) {
                if(i == splitIdentity.length-1){
                    stringBuffer.append(splitIdentity[i]);
                }else {
                    stringBuffer.append(splitIdentity[i]+"/");
                }
            }
            activityUserinfoBinding.tvSlashIdentity.setText(stringBuffer.toString());
        }

        //粉丝数
        fanscount = uinfo.getFanscount();
        activityUserinfoBinding.tvUserInfoFansCount.setText("粉丝数"+fanscount);
        //粉丝比率
        fansratio = uinfo.getFansratio();
        activityUserinfoBinding.tvUserInfoFansratio.setText(fansratio+"%");
        activityUserinfoBinding.tvFansCount.setText("超过平台"+fansratio+"的用户");
        //完成任务的单数
        achievetaskcount = uinfo.getAchievetaskcount();
        activityUserinfoBinding.tvUserInfoAchieveTaskCount.setText("顺利成交"+achievetaskcount+"单");
        activityUserinfoBinding.tvAchieveTaskCount.setText(achievetaskcount+"");
        //任务总数
        totoltaskcount = uinfo.getTotoltaskcount();
        activityUserinfoBinding.tvTotolTaskCount.setText("共"+totoltaskcount+"单任务");
        //平均服务点
        averageservicepoint = uinfo.getAverageservicepoint();
        //用户服务指向
        userservicepoint = uinfo.getUserservicepoint();
        activityUserinfoBinding.tvUserInfoServicePoint.setText("服务力"+userservicepoint+"星");
        activityUserinfoBinding.tvAverageServicePoint.setText(userservicepoint+"");
        activityUserinfoBinding.averageServicePoint.setText("---平台平均服务力为"+averageservicepoint+"星");

        //技能描述
        desc = uinfo.getDesc();
        if(!desc.isEmpty()){
        activityUserinfoBinding.tvUserinfoSkilldescribe.setText(desc);
        }

        //方向
        direction = uinfo.getDirection();
        industry = uinfo.getIndustry();
        activityUserinfoBinding.tvProfession.setText(industry+"|"+direction);

        //   技能标签
        tag = uinfo.getTag();
        if(!tag.contains(" ")){
            activityUserinfoBinding.tvUserInfoTag.setText(tag);
            activityUserinfoBinding.tvSkilllabel1.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvSkilllabel1.setText(tag);

        }else {
            String[] split = tag.split(" ");
            for (int i = 0; i < split.length; i++) {
                if(i == split.length-1){
                    activityUserinfoBinding.tvUserInfoTag.setText(split[i]);
                }else {
                    activityUserinfoBinding.tvUserInfoTag.setText(split[i]+"|");
                }
         //最多三个，分情况,技能标签
                switch (split.length){
                    case 1:
                        activityUserinfoBinding.tvSkilllabel1.setVisibility(View.VISIBLE);
                        activityUserinfoBinding.tvSkilllabel1.setText(split[0]);
                        break;
                    case 2:
                        activityUserinfoBinding.tvSkilllabel1.setVisibility(View.VISIBLE);
                        activityUserinfoBinding.tvSkilllabel1.setText(split[0]);
                        activityUserinfoBinding.tvSkilllabel2.setVisibility(View.VISIBLE);
                        activityUserinfoBinding.tvSkilllabel2.setText(split[1]);
                        break;
                    case 3:
                        activityUserinfoBinding.tvSkilllabel1.setVisibility(View.VISIBLE);
                        activityUserinfoBinding.tvSkilllabel1.setText(split[0]);
                        activityUserinfoBinding.tvSkilllabel2.setVisibility(View.VISIBLE);
                        activityUserinfoBinding.tvSkilllabel2.setText(split[1]);
                        activityUserinfoBinding.tvSkilllabel3.setVisibility(View.VISIBLE);
                        activityUserinfoBinding.tvSkilllabel3.setText(split[2]);
                        break;
                }
            }
        }

    }
    //去认证
    private void OpenApprovalActivtity() {
        Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
        intentApprovalActivity.putExtra("careertype",careertype);
        intentApprovalActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentApprovalActivity);
    }

    //接口回调
    public interface OnNameListener{
        void OnNameListener(String name ,long myId);
    }

    private OnNameListener onNameListener;
    public void setOnNameListener(OnNameListener listener) {
        this.onNameListener = listener;
    }

    //获取个人信息的接口
    public class OnGetMyUserinfo implements BaseProtocol.IResultExecutor<UserInfoItemBean> {
        @Override
        public void execute(UserInfoItemBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                UserInfoItemBean.DataBean data = dataBean.getData();
                uinfo = data.getUinfo();
                updateUserInfo();
            }else {
                LogKit.d("rescode="+rescode);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //获取其他用户信息
    public class onGetOtherPersonInfo implements BaseProtocol.IResultExecutor<UserInfoItemBean> {
        @Override
        public void execute(UserInfoItemBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                UserInfoItemBean.DataBean data = dataBean.getData();
                uinfo = data.getUinfo();
                updateUserInfo();
            }else {
                LogKit.d("rescode ="+rescode);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
