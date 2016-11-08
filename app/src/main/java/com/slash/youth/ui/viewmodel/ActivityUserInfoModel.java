package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.MotionEvent;
import android.view.View;

import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.FloatViewBinding;
import com.slash.youth.domain.NewTaskUserInfoBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.GetUserInfoProtocol;
import com.slash.youth.http.protocol.MyUserInfoProtocol;
import com.slash.youth.http.protocol.ServicePartyRejectProtocol;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ChooseFriendActivtiy;
import com.slash.youth.ui.adapter.UserInfoAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
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
    private UserInfoItemBean.DataBean.UinfoBean uinfo;
    public long  userUid = -11;//-1是自己看自己的
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

    //自己
    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding ) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        initData();
        initView();

    }

    //看其他用户
    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding ,long uid) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        this.userUid = uid;
        initData();
        initView();
    }

    //加载数据
    private void initData() {

        if(userUid == -1){
            getUserOwnInfoData();
            isOther = false;
        }else {
            getOtherUserInfoData();
            isOther = true;
        }

        //网络获取数据
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
        userInfoListView.add(new NewTaskUserInfoBean(true));
    }

    private void getOtherUserInfoData() {
        //这是从其他页面里面传过来的，我看其他人的资料数据
        //uid是从登陆页面第一次获得uid

        //目前 默认是10001
        userUid = 10001;
        MyUserInfoProtocol myUserInfoProtocol = new MyUserInfoProtocol(userUid);
        myUserInfoProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<UserInfoItemBean>() {
            @Override
            public void execute(UserInfoItemBean dataBean) {
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    UserInfoItemBean.DataBean data = dataBean.getData();
                    uinfo = data.getUinfo();
                    //这是我看别人的信息
                    updateUserInfo();
                }else {
                    LogKit.d("rescode ="+rescode);
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
    }

    private void getUserOwnInfoData() {
        //这是看我自己的个人信息，是直接从我的里面点击进来的
        GetUserInfoProtocol getUserInfoProtocol = new GetUserInfoProtocol();
        getUserInfoProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<UserInfoItemBean>() {
            @Override
            public void execute(UserInfoItemBean dataBean) {
                int rescode = dataBean.getRescode();
                if(rescode == 0){
                    UserInfoItemBean.DataBean data = dataBean.getData();
                    uinfo = data.getUinfo();
                    //这是我自己看自己的
                    updateUserInfo();
                }else {
                    LogKit.d("rescode="+rescode);
                }
            }

            @Override
            public void executeResultError(String result) {
               LogKit.d("result:"+result);
            }
        });
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
        //用户头像
        avatar = uinfo.getAvatar();
        if(!avatar.isEmpty()){
           // x.image().bind(activityUserinfoBinding.ivUserinfoUsericon,avatar);
        }

        //用户姓名
        name = uinfo.getName();
        activityUserinfoBinding.tvUserinfoUsername.setText(name);
        if(!name.isEmpty()){
            onNameListener.OnNameListener(name);
        }else {
            activityUserinfoBinding.tvUserinfoUsername.setText("个人信息中心");
        }



        //专家  用户身份，是否是专家，专家几级,默认是不显示
        expert = uinfo.getExpert();
        activityUserinfoBinding.tvUserinfoIdentity.setText("专家"+expert+"级");

        //是否认证
        isauth = uinfo.getIsauth();
        if(isauth == 1){
            //认证过的
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.GONE);
            activityUserinfoBinding.ivUserinfoV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenApprovalActivtity();
                }
            });

        }else if(isauth == 0){
            //非认证
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoApproval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenApprovalActivtity();
                }
            });
        }

        //地点,没有所在地时，显示用户的定位信息，没有定位信息时，显示暂未填写，
        //城市
        city = uinfo.getCity();
        //省份
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

        //ios android
        tag = uinfo.getTag();
        if(!tag.contains("-")){
            activityUserinfoBinding.tvUserInfoTag.setText(tag);
        }else {
            String[] split = tag.split("-");
            for (int i = 0; i < split.length; i++) {
                if(i == split.length-1){
                    activityUserinfoBinding.tvUserInfoTag.setText(split[i]);
                }else {
                    activityUserinfoBinding.tvUserInfoTag.setText(split[i]+"|");
                }
            }
        }

        //身份,斜杠 斜杠身份  没填写斜杠身份时，显示暂未填写 ,如果有的话，就填写
        identity = uinfo.getIdentity();
        if(!identity.contains(",")){
            activityUserinfoBinding.tvSlashIdentity.setText("斜杠身份:"+slashIdentity);
        }else {
            String[] splitIdentity = identity.split(",");
            stringBuffer.append("斜杠身份:");
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
        activityUserinfoBinding.tvUserInfoFansratio.setText(fansratio);
        activityUserinfoBinding.tvFansCount.setText("超过平台"+fansratio+"的用户");

        //完成任务的单数
        achievetaskcount = uinfo.getAchievetaskcount();
        activityUserinfoBinding.tvUserInfoAchieveTaskCount.setText("顺利成交"+achievetaskcount+"单");
        activityUserinfoBinding.tvAchieveTaskCount.setText(achievetaskcount);
        //任务总数
        totoltaskcount = uinfo.getTotoltaskcount();
        activityUserinfoBinding.tvTotolTaskCount.setText("共"+totoltaskcount+"单任务");


        //平均服务点
        averageservicepoint = uinfo.getAverageservicepoint();
        //用户服务指向
        userservicepoint = uinfo.getUserservicepoint();
        activityUserinfoBinding.tvUserInfoServicePoint.setText("服务力"+userservicepoint+"星");
        activityUserinfoBinding.tvAverageServicePoint.setText(averageservicepoint);
        activityUserinfoBinding.averageServicePoint.setText("---平台平均服务力为"+averageservicepoint+"星");

        //方向
        direction = uinfo.getDirection();
        activityUserinfoBinding.tvProfession.setText(direction);
        //用户id
        int id = uinfo.getId();



        //第一次进来，展示的是注册时的行业和职能，但是我可以修改，再次进来时，展示我修改的
        //String profession ="";
       // activityUserinfoBinding.tvUserInfoProfession.setText(profession);


        //技能描述,从编辑页面传过来
        //activityUserinfoBinding.tvUserinfoSkilldescribe.setText();



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

    private void OpenApprovalActivtity() {
        Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
        intentApprovalActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentApprovalActivity);
    }

    public interface OnNameListener{
        void OnNameListener(String name);
    }

    private OnNameListener onNameListener;
    public void setOnNameListener(OnNameListener listener) {
        this.onNameListener = listener;
    }

}
