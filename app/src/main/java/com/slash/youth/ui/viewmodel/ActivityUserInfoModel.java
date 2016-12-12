package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.DialogRecommendBinding;
import com.slash.youth.databinding.FloatViewBinding;
import com.slash.youth.domain.NewDemandAandServiceBean;
import com.slash.youth.domain.NewTaskUserInfoBean;
import com.slash.youth.domain.OtherInfoBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.UserInfoAdapter;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2016/11/1.
 */
public class ActivityUserInfoModel extends BaseObservable {
    private ActivityUserinfoBinding activityUserinfoBinding;
    private  ArrayList<NewDemandAandServiceBean.DataBean.ListBean> userInfoListView = new ArrayList<>();
    private UserInfoAdapter userInfoAdapter;
    private FloatViewBinding floatViewBinding;
    private String slashIdentity = "暂未填写";//默认
    private String defaultArea  = "暂未填写";
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
    private double userservicepoint;
    private long otherUid;
    public long myUid;
    private UserInfoActivity userInfoActivity;
    private String industry;
    private String desc;
    public int otherId;
    private boolean isSuccessful;
    private boolean isDelete;

    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding, long otherUid,
                                 UserInfoActivity userInfoActivity,String tag) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        this.userInfoActivity = userInfoActivity;
        this.tag = tag;
        this.otherUid = otherUid;
        initData();
        initView();
        listener();

    }

    private void listener() {
        if(isauth == 0){
            activityUserinfoBinding.tvUserinfoApproval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
                    intentApprovalActivity.putExtra("careertype",careertype);
                    intentApprovalActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    CommonUtils.getContext().startActivity(intentApprovalActivity);
                }
            });
        }
    }

    private long uid;
    private int  offset = 0;
    private  int limit = 20;
    //加载数据
    private void initData() {
        if(otherUid == -1){
            MyManager.getMySelfPersonInfo(new OnGetMyUserinfo());
            uid =  LoginManager.currentLoginUserId;
            UserInfoEngine.getNewDemandAndServiceList(new onGetNewDemandAndServiceList(),uid,offset,limit);
            isOther = false;
        }else {
            MyManager.getOtherPersonInfo(new onGetOtherPersonInfo(),otherUid);
            uid = otherUid;
            UserInfoEngine.getNewDemandAndServiceList(new onGetNewDemandAndServiceList(),uid,offset,limit);
            isOther = true;
        }
    }

    //最新任务
    public class onGetNewDemandAndServiceList implements BaseProtocol.IResultExecutor<NewDemandAandServiceBean> {
        @Override
        public void execute(NewDemandAandServiceBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                NewDemandAandServiceBean.DataBean data = dataBean.getData();
                List<NewDemandAandServiceBean.DataBean.ListBean> list = data.getList();
                userInfoListView.addAll(list);
                userInfoAdapter = new UserInfoAdapter(userInfoListView);
                activityUserinfoBinding.lvUserinfo.setAdapter(userInfoAdapter);

            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }


    //加载界面
    private void initView() {


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

    //点击打开技能标签页面
    public void openskilllabel(View view) {
        LogKit.d("打开技能标签页面，去选择标签");
    }

    private void updateUserInfo(UserInfoItemBean.DataBean.UinfoBean uinfo){
        //用户ID
        myUid = uinfo.getId();
        //用户头像
        avatar = uinfo.getAvatar();
        if(!avatar.isEmpty()){
            BitmapKit.bindImage(activityUserinfoBinding.ivUserinfoUsericon, GlobalConstants.HttpUrl.IMG_DOWNLOAD+"?fileId="+avatar);
        }
        //用户姓名
        name = uinfo.getName();
        activityUserinfoBinding.tvUserinfoUsername.setText(name);
        if(!name.isEmpty()){
            onNameListener.OnNameListener(name,myUid);
        }else {
            activityUserinfoBinding.tvUserinfoUsername.setText("个人信息中心");
        }

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
        }else if(isauth == 0){
            //非认证
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.VISIBLE);
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

        //技能描述
        desc = uinfo.getDesc();
        if(desc!=null){
            activityUserinfoBinding.tvUserinfoSkilldescribe.setText(desc);
        }

        //方向
        direction = uinfo.getDirection();
        industry = uinfo.getIndustry();
        activityUserinfoBinding.tvProfession.setText(industry+"|"+direction);

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
    }

    //更新用户信息
    private void updateOtherUserInfo( OtherInfoBean.DataBean.UinfoBean uinfo) {

        //用户ID
        myUid = uinfo.getId();
        //用户头像
        avatar = uinfo.getAvatar();
        if (!avatar.isEmpty()) {
            BitmapKit.bindImage(activityUserinfoBinding.ivUserinfoUsericon, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        //用户姓名
        name = uinfo.getName();
        activityUserinfoBinding.tvUserinfoUsername.setText(name);
        if (!name.isEmpty()) {
            onNameListener.OnNameListener(name, myUid);
        } else {
            activityUserinfoBinding.tvUserinfoUsername.setText("个人信息中心");
        }

        //职业类型
        careertype = uinfo.getCareertype();
        if (careertype == 1) {//固定职业者
            company = uinfo.getCompany();//公司
            position = uinfo.getPosition();//技术专家
            String companyAndPosition = company + "-" + position;
            if (!company.isEmpty()) {
                activityUserinfoBinding.tvUserInfoCompany.setText(companyAndPosition);
            } else {
                activityUserinfoBinding.tvUserInfoCompany.setText("暂未填写职务信息");
            }
        } else if (careertype == 2) {//自由职业者
            activityUserinfoBinding.tvUserInfoCompany.setText("自雇者");
        }

        //是否认证
        isauth = uinfo.getIsauth();
        if (isauth == 1) {
            //认证过的
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.GONE);
        } else if (isauth == 0) {
            //非认证
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoApproval.setVisibility(View.VISIBLE);
        }

        //身份,斜杠 斜杠身份  没填写斜杠身份时，显示暂未填写 ,如果有的话，就填写
        identity = uinfo.getIdentity();
        if (!identity.contains(",")) {
            if (identity.isEmpty()) {
                activityUserinfoBinding.tvSlashIdentity.setText("斜杠身份: " + slashIdentity);
            } else {
                activityUserinfoBinding.tvSlashIdentity.setText("斜杠身份: " + identity);
            }
        } else {
            String[] splitIdentity = identity.split(",");
            stringBuffer.append("斜杠身份: ");
            for (int i = 0; i < splitIdentity.length; i++) {
                if (i == splitIdentity.length - 1) {
                    stringBuffer.append(splitIdentity[i]);
                } else {
                    stringBuffer.append(splitIdentity[i] + "/");
                }
            }
            activityUserinfoBinding.tvSlashIdentity.setText(stringBuffer.toString());
        }

        //技能描述
        desc = uinfo.getDesc();
        if (desc != null) {
            activityUserinfoBinding.tvUserinfoSkilldescribe.setText(desc);
        }

        //方向
        direction = uinfo.getDirection();
        industry = uinfo.getIndustry();
        activityUserinfoBinding.tvProfession.setText(industry + "|" + direction);

        //专家  用户身份，是否是专家，专家几级,默认是不显示
        expert = uinfo.getExpert();
        activityUserinfoBinding.tvUserinfoIdentity.setText("专家" + expert + "级");

        //城市  //省份
        city = uinfo.getCity();
        province = uinfo.getProvince();
        if (!city.equals(province)) {
            place = province + "" + city;
        } else {
            place = city;
        }
        activityUserinfoBinding.tvPlace.setText(place);

        //粉丝数
        fanscount = uinfo.getFanscount();
        activityUserinfoBinding.tvUserInfoFansCount.setText("粉丝数" + fanscount);
        //粉丝比率
        fansratio = uinfo.getFansratio();
        activityUserinfoBinding.tvUserInfoFansratio.setText(fansratio + "%");
        activityUserinfoBinding.tvFansCount.setText("超过平台" + fansratio + "的用户");
        //完成任务的单数
        achievetaskcount = uinfo.getAchievetaskcount();
        activityUserinfoBinding.tvUserInfoAchieveTaskCount.setText("顺利成交" + achievetaskcount + "单");
        activityUserinfoBinding.tvAchieveTaskCount.setText(achievetaskcount + "");
        //任务总数
        totoltaskcount = uinfo.getTotoltaskcount();
        activityUserinfoBinding.tvTotolTaskCount.setText("共" + totoltaskcount + "单任务");
        //平均服务点
        averageservicepoint = uinfo.getAverageservicepoint();
        //用户服务指向
        userservicepoint = uinfo.getUserservicepoint();
        activityUserinfoBinding.tvUserInfoServicePoint.setText("服务力" + userservicepoint + "星");
        activityUserinfoBinding.tvAverageServicePoint.setText(userservicepoint + "");
        activityUserinfoBinding.averageServicePoint.setText("---平台平均服务力为" + averageservicepoint + "星");

        //   技能标签
     /*   tag = uinfo.getTag();
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
            }*/


    }
    //去认证
    public void OpenApprovalActivtity(View view) {
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
                UserInfoItemBean.DataBean.UinfoBean uinfo = data.getUinfo();
                  updateUserInfo(uinfo);
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
    public class onGetOtherPersonInfo implements BaseProtocol.IResultExecutor<OtherInfoBean> {
        @Override
        public void execute(OtherInfoBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                OtherInfoBean.DataBean data = dataBean.getData();
                OtherInfoBean.DataBean.UinfoBean uinfo = data.getUinfo();
                updateOtherUserInfo(uinfo);
            }else {
                LogKit.d("rescode ="+rescode);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //加好友
    public void addFriend(View view) {
        String text = activityUserinfoBinding.tvAddFriend.getText().toString();
        if(text.equals("申请加好友")){
            ContactsManager.onAddFriendRelationProtocol(new onAddFriendRelationProtocol(),otherUid,"userInfo");
            if(isSuccessful){
                ToastUtils.shortCenterToast("已申请加好友成功");
                activityUserinfoBinding.tvAddFriend.setText("已申请加好友");
            }else {
                ToastUtils.shortCenterToast("申请加好友失败");
                activityUserinfoBinding.tvAddFriend.setText("申请加好友");
            }
        }

        if(text.equals("解除好友")){
        ContactsManager.deleteFriendRelationProtocol(new onDeleteFriendRelationProtocol(),otherUid,"unBindUserInfo");
            if (isDelete) {
                ToastUtils.shortCenterToast("已解除好友成功");
                activityUserinfoBinding.tvAddFriend.setText("申请加好友");
            }else {
                ToastUtils.shortCenterToast("解除好友失败");
                activityUserinfoBinding.tvAddFriend.setText("解除好友");
            }
        }
    }

    //聊一聊
    public void chat(View view) {
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        userInfoActivity.startActivity(intentChatActivity);
    }

    //关注他
    public void attention(View view) {
        String text = activityUserinfoBinding.tvAttentionTA.getText().toString();
        if(text.equals("关注TA")){
            ContactsManager.onCareTAProtocol(new onCareTAProtocol(),otherUid);
        }

        //取消关注
        if(text.equals("取消关注")){
            ContactsManager.onCannelCareProtocol(new onCannelCareProtocol(),otherUid);
        }
    }

    //推荐
    public void recommend(View view) {
        DialogRecommendBinding dialogRecommendBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_recommend, null, false);
        DialogRecommendModel dialogRecommendModel = new DialogRecommendModel(dialogRecommendBinding,userInfoActivity,activityUserinfoBinding);
        dialogRecommendBinding.setDialogRecommendModel(dialogRecommendModel);
        activityUserinfoBinding.flDialogContainer.addView(dialogRecommendBinding.getRoot());
    }

    //加好友关系
    public class onAddFriendRelationProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        isSuccessful = true;
                        break;
                    case 0:
                        isSuccessful = false;
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //删除好友关系
    public class onDeleteFriendRelationProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        isDelete = true;
                        break;
                    case 0:
                        isDelete = false;
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //关注TA
    public class onCareTAProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case ContactsManager.FOLLOW_STATUS_SUCCESS://关注or取消关注成功
                        ToastUtils.shortCenterToast("已关注TA");
                        activityUserinfoBinding.tvAttentionTA.setText("取消关注");
                        break;
                    case ContactsManager.FOLLOW_STATUS_ALREADY_ERROR:
                        LogKit.d("已经关注过错误");
                        break;
                    case ContactsManager.FOLLOW_STATUS_NOT_EXIST_ERROR:
                        LogKit.d("关注关系不存在错误");
                        break;
                    case ContactsManager.FOLLOW_STATUS_UNKNOWN_ERROR:
                        LogKit.d("服务端错误");
                        break;
                    case ContactsManager.FOLLOW_USER_NOT_EXIST_ERROR:
                        LogKit.d("用户不存在错误");
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //取消关注
    public class onCannelCareProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case ContactsManager.FOLLOW_STATUS_SUCCESS://关注or取消关注成功
                        ToastUtils.shortCenterToast("已取消关注TA");
                        activityUserinfoBinding.tvAttentionTA.setText("关注TA");
                        break;
                    case ContactsManager.FOLLOW_STATUS_ALREADY_ERROR:
                        LogKit.d("已经关注过错误");
                        break;
                    case ContactsManager.FOLLOW_STATUS_NOT_EXIST_ERROR:
                        LogKit.d("关注关系不存在错误");
                        break;
                    case ContactsManager.FOLLOW_STATUS_UNKNOWN_ERROR:
                        LogKit.d("服务端错误");
                        break;
                    case ContactsManager.FOLLOW_USER_NOT_EXIST_ERROR:
                        LogKit.d("用户不存在错误");
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
