package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.DialogRecommendBinding;
import com.slash.youth.domain.FansBean;
import com.slash.youth.domain.FriendStatusBean;
import com.slash.youth.domain.NewDemandAandServiceBean;
import com.slash.youth.domain.OtherInfoBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ChatActivity;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.UserInfoAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2016/11/1.
 */
public class ActivityUserInfoModel extends BaseObservable {
    private ActivityUserinfoBinding activityUserinfoBinding;
    private  ArrayList<NewDemandAandServiceBean.DataBean.ListBean> userInfoListView = new ArrayList<>();
    private ArrayList<String> skillLabelList = new ArrayList<>();
    private UserInfoAdapter userInfoAdapter;
    private String slashIdentity = "暂未填写";//默认
    private String defaultArea  = "暂未填写";
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
    public long otherUid;
    public long myUid;
    private UserInfoActivity userInfoActivity;
    private String industry;
    private String desc;
    private TextView textViewTag;
    private View footView;
    private int listSize;
    private int footerHeight;
    private int startY;
    private int anonymity = 1;
    private OtherInfoBean.DataBean.UinfoBean uinfo;
    private  int friendStatus;
    private int attentionStatus;
    private int relationshipscount;
    private int  myAnonymity = 2;

    public ActivityUserInfoModel(ActivityUserinfoBinding activityUserinfoBinding, long otherUid,
                                 UserInfoActivity userInfoActivity,String tag,int anonymity
                                ) {
        this.activityUserinfoBinding = activityUserinfoBinding;
        this.tag = tag;
        this.otherUid = otherUid;
        this.anonymity = anonymity;
        this.userInfoActivity = userInfoActivity;
        testIsFriend();
        testisfollow();
        initData();
        initView();
        listener();
    }

    //先验证一下好友关系
    public void testIsFriend() {
        ContactsManager.onFriendApplicationStatus(new onFriendApplicationStatus(),otherUid);
    }

    //先验证我和某用户的关系
    public void testisfollow() {
        ContactsManager.onTestIsFollow(new onTestIsFollow(), otherUid);
    }

    //验证我和某用户的关系
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
                        activityUserinfoBinding.ivCare.setImageResource(R.mipmap.attention_icon);
                        attentionStatus = 1;
                        LogKit.d("我关注过他");
                        break;
                    case 0://无关系
                        activityUserinfoBinding.ivCare.setImageResource(R.mipmap.yi_attention_icon);
                        attentionStatus = 0;
                        LogKit.d("我没有关注过他");
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

    private void listener() {
        //最新任务点击事件
        activityUserinfoBinding.lvUserinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewDemandAandServiceBean.DataBean.ListBean userInfoBean = userInfoListView.get(position);
                int type = userInfoBean.getType();
                switch (type){
                    case 1:
                        long demandId  = userInfoBean.getUid();
                        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                        intentDemandDetailActivity.putExtra("demandId", demandId);
                        userInfoActivity.startActivity(intentDemandDetailActivity);
                        break;
                    case 2:
                        long serviceId = userInfoBean.getUid();
                        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                        intentServiceDetailActivity.putExtra("serviceId", serviceId);
                        userInfoActivity.startActivity(intentServiceDetailActivity);
                        break;
                }
            }
        });
    }

    private long uid;
    private int  offset = 0;
    private  int limit = 10;
    //加载数据
    private void initData() {
        if(otherUid == LoginManager.currentLoginUserId){//自己看自己
            MyManager.getMySelfPersonInfo(new OnGetSelfPersonInfo());
            MyManager.getOtherPersonInfo(new onGetMyUidPersonInfo(),otherUid,anonymity);
            UserInfoEngine.getNewDemandAndServiceList(new onGetNewDemandAndServiceList(),LoginManager.currentLoginUserId,offset,limit,myAnonymity);//自己看自己全部展示
            isOther = false;
            activityUserinfoBinding.tvUserinfoTitle.setText(ContactsManager.USER_INFO );
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.VISIBLE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.GONE);
        }else {//自己看其他人
            MyManager.getOtherPersonInfo(new onGetOtherPersonInfo(),otherUid,anonymity);
            UserInfoEngine.getNewDemandAndServiceList(new onGetNewDemandAndServiceList(),otherUid,offset,limit,anonymity);
            isOther = true;
            activityUserinfoBinding.ivUserinfoMenu.setVisibility(View.VISIBLE);
            activityUserinfoBinding.tvUserinfoSave.setVisibility(View.GONE);
            activityUserinfoBinding.llAddFriend.setVisibility(View.VISIBLE);

            //判断是否匿名
            switch (anonymity){
                case 1://实名
                    break;
                case 0://匿名
                    activityUserinfoBinding.lvUserinfo.setVisibility(View.GONE);
                    activityUserinfoBinding.llTaskTitle.setVisibility(View.GONE);
                    activityUserinfoBinding.ivUserinfoUsericon.setImageResource(R.mipmap.anonymity_avater);
                    break;
            }
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
                for (NewDemandAandServiceBean.DataBean.ListBean listBean : list) {
                    int anonymity = listBean.getAnonymity();
                    if(anonymity == 1){
                        userInfoListView.add(listBean);
                    }
                }

                listSize = userInfoListView.size();
               // userInfoListView.addAll(list);
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
        //手动ScrollView设置到顶部
        activityUserinfoBinding.sv.smoothScrollTo(0,0);

        activityUserinfoBinding.refreshView.setOnRefreshListener(new TaskListListener());

        //最新的任务没有
        if(listSize == 0){
            View rl = userInfoActivity.findViewById(R.id.rl_progress);
            rl.setVisibility(View.GONE);
            activityUserinfoBinding.tvNone.setVisibility(View.VISIBLE);
        }
    }

    public class TaskListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {

        }
        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //如果加载到最后一页，需要调用setLoadToLast()方法
                    if(listSize < limit){//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }else {//不是最后一页
                        offset += limit;
                        UserInfoEngine.getNewDemandAndServiceList(new onGetNewDemandAndServiceList(),uid,offset,limit,anonymity);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }
            }, 2000);
        }
    }

    //点击打开技能标签页面
    public void openskilllabel(View view) {
        if(!isOther){
            Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
            intentSubscribeActivity.putStringArrayListExtra("addedTagsName", skillLabelList);
            intentSubscribeActivity.putStringArrayListExtra("addedTags", skillLabelList);
            intentSubscribeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentSubscribeActivity);
            activityUserinfoBinding.ivInto.setVisibility(View.VISIBLE);
        }
    }

    public UserInfoItemBean.DataBean.UinfoBean myUninfo;
    private void updateUserInfo(UserInfoItemBean.DataBean.UinfoBean uinfo){
        myUninfo =  uinfo;
        //   技能标签
        String tag = uinfo.getTag();
        if(!TextUtils.isEmpty(tag)){
            String[] split = tag.split(",");
            for (String textTag : split) {
                skillLabelList.add(textTag);
                textViewTag = new TextView(CommonUtils.getContext());
                textViewTag.setText(textTag);
                textViewTag.setTextColor(Color.parseColor("#31C5E4"));
                textViewTag.setTextSize(CommonUtils.dip2px(4));
                textViewTag.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(6),CommonUtils.dip2px(8),CommonUtils.dip2px(6));
                textViewTag.setBackgroundColor(Color.parseColor("#d6f3fa"));
                activityUserinfoBinding.llSkilllabelContainer.addView(textViewTag);
            }
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

        //用户ID
        myUid = uinfo.getId();
        //用户头像
        avatar = uinfo.getAvatar();
        if(!TextUtils.isEmpty(avatar)){
            BitmapKit.bindImage(activityUserinfoBinding.ivUserinfoUsericon, GlobalConstants.HttpUrl.IMG_DOWNLOAD+"?fileId="+avatar);
        }
        //用户姓名
        name = uinfo.getName();
        activityUserinfoBinding.tvUserinfoUsername.setText(name);
        if(!TextUtils.isEmpty(name)){
            onNameListener.OnNameListener(name,myUid,company);
        }else {
            activityUserinfoBinding.tvUserinfoUsername.setText(ContactsManager.USER_INFO);
        }

        //是否认证
        isauth = uinfo.getIsauth();
        if(isauth == 1){
            //认证过的
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.VISIBLE);
        }else if(isauth == 0){
            //非认证
            if(!isOther){
                activityUserinfoBinding.ivUserinfoV.setVisibility(View.GONE);
            }
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
        if(!TextUtils.isEmpty(desc)){
            activityUserinfoBinding.tvUserinfoSkilldescribe.setText(desc);
        }

        //方向
        direction = uinfo.getDirection();
        industry = uinfo.getIndustry();
        if(!TextUtils.isEmpty(direction)){
            activityUserinfoBinding.tvProfession.setText(direction);
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
    }

    public   OtherInfoBean.DataBean.UinfoBean otherUinfo;

    //更新用户信息
    private void updateOtherUserInfo() {
       otherUinfo = uinfo;
        //   技能标签
        String tag = uinfo.getTag();
        if(!TextUtils.isEmpty(tag)){
            String[] split = tag.split(",");
            for (String textTag : split) {
                textViewTag = new TextView(CommonUtils.getContext());
                textViewTag.setText(textTag);
                textViewTag.setTextColor(Color.parseColor("#31C5E4"));
                textViewTag.setTextSize(CommonUtils.dip2px(4));
                textViewTag.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(6),CommonUtils.dip2px(8),CommonUtils.dip2px(6));
                textViewTag.setBackgroundColor(Color.parseColor("#d6f3fa"));
                activityUserinfoBinding.llSkilllabelContainer.addView(textViewTag);
            }
        }

        //粉丝数
        fanscount = uinfo.getFanscount();
         relationshipscount = uinfo.getRelationshipscount();
        activityUserinfoBinding.tvUserInfoFansCount.setText("粉丝数" + relationshipscount);
        activityUserinfoBinding.pbFans.setProgress(relationshipscount);
        //粉丝比率
        fansratio = uinfo.getFansratio();
        activityUserinfoBinding.tvUserInfoFansratio.setText(fansratio + "%");
        activityUserinfoBinding.tvFansCount.setText("超过平台" + fansratio + "的用户");
        //完成任务的单数
        achievetaskcount = uinfo.getAchievetaskcount();
        activityUserinfoBinding.pbTask.setProgress(achievetaskcount);
        activityUserinfoBinding.tvUserInfoAchieveTaskCount.setText("顺利成交" + achievetaskcount + "单");
        activityUserinfoBinding.tvAchieveTaskCount.setText(achievetaskcount + "");
        //任务总数
        totoltaskcount = uinfo.getTotoltaskcount();
        activityUserinfoBinding.tvTotolTaskCount.setText("共" + totoltaskcount + "单任务");
        //平均服务点
        averageservicepoint = uinfo.getAverageservicepoint();
        //用户服务指向
        userservicepoint = uinfo.getUserservicepoint();
        activityUserinfoBinding.pbService.setProgress((int)userservicepoint);
        activityUserinfoBinding.tvUserInfoServicePoint.setText("服务力" + userservicepoint + "星");
        activityUserinfoBinding.tvAverageServicePoint.setText(userservicepoint + "");
        activityUserinfoBinding.averageServicePoint.setText("平台平均服务力为" + averageservicepoint + "星");

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

        //用户ID
        myUid = uinfo.getId();
        //用户头像
        avatar = uinfo.getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            BitmapKit.bindImage(activityUserinfoBinding.ivUserinfoUsericon, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        //用户姓名
        name = uinfo.getName();
        activityUserinfoBinding.tvUserinfoUsername.setText(name);
        if (!TextUtils.isEmpty(name)) {
            onNameListener.OnNameListener(name, myUid,company);
        } else {
            activityUserinfoBinding.tvUserinfoUsername.setText(ContactsManager.USER_INFO);
        }

        //是否认证
        isauth = uinfo.getIsauth();
        if (isauth == 1) {
            //认证过的
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.VISIBLE);
        } else if (isauth == 0) {
            //非认证
            activityUserinfoBinding.ivUserinfoV.setVisibility(View.GONE);
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
        if (!TextUtils.isEmpty(desc)) {
            activityUserinfoBinding.tvDescTitle.setVisibility(View.GONE);
            activityUserinfoBinding.tvUserinfoSkilldescribe.setText(desc);
        }else{
            activityUserinfoBinding.tvDescTitle.setVisibility(View.VISIBLE);
        }

        //方向
        direction = uinfo.getDirection();
        industry = uinfo.getIndustry();
        if(!TextUtils.isEmpty(direction)&&!TextUtils.isEmpty(industry)){
            activityUserinfoBinding.tvProfession.setText(industry + "|" + direction);
        }

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
    }

    //接口回调
    public interface OnNameListener{
        void OnNameListener(String name ,long myId,String company);
    }

    private OnNameListener onNameListener;
    public void setOnNameListener(OnNameListener listener) {
        this.onNameListener = listener;
    }

    //获取个人信息的接口
    public class OnGetSelfPersonInfo implements BaseProtocol.IResultExecutor<UserInfoItemBean> {
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

    public class onGetMyUidPersonInfo implements BaseProtocol.IResultExecutor<OtherInfoBean> {
        @Override
        public void execute(OtherInfoBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                OtherInfoBean.DataBean data = dataBean.getData();
                uinfo = data.getUinfo();
                //粉丝数
                relationshipscount = uinfo.getRelationshipscount();
                fanscount = uinfo.getFanscount();
                activityUserinfoBinding.tvUserInfoFansCount.setText("粉丝数" + relationshipscount);
                activityUserinfoBinding.pbFans.setProgress(relationshipscount);
                //粉丝比率
                fansratio = uinfo.getFansratio();
                activityUserinfoBinding.tvUserInfoFansratio.setText(fansratio + "%");
                activityUserinfoBinding.tvFansCount.setText("超过平台" + fansratio + "的用户");
                //完成任务的单数
                achievetaskcount = uinfo.getAchievetaskcount();
                activityUserinfoBinding.pbTask.setProgress(achievetaskcount);
                activityUserinfoBinding.tvUserInfoAchieveTaskCount.setText("顺利成交" + achievetaskcount + "单");
                activityUserinfoBinding.tvAchieveTaskCount.setText(achievetaskcount + "");
                //任务总数
                totoltaskcount = uinfo.getTotoltaskcount();
                activityUserinfoBinding.tvTotolTaskCount.setText("共" + totoltaskcount + "单任务");
                //平均服务点
                averageservicepoint = uinfo.getAverageservicepoint();
                //用户服务指向
                userservicepoint = uinfo.getUserservicepoint();
                activityUserinfoBinding.pbService.setProgress((int)userservicepoint);
                activityUserinfoBinding.tvUserInfoServicePoint.setText("服务力" + userservicepoint + "星");
                activityUserinfoBinding.tvAverageServicePoint.setText(userservicepoint + "");
                activityUserinfoBinding.averageServicePoint.setText("---平台平均服务力为" + averageservicepoint + "星");
            }else {
                LogKit.d("rescode ="+rescode);
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
                uinfo = data.getUinfo();
                updateOtherUserInfo();
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
        switch (friendStatus){
            case 0://现在是陌生人，点击之后是加好友
                ContactsManager.onAddFriendRelationProtocol(new  onAddFriendRelationProtocol(),otherUid,"   ");
                break;
            case 1://现在是我已申请状态，点击之后没有效果
                activityUserinfoBinding.tvAddFriend.setText(ContactsManager.ADD_FRIEND_APPLICATION);
                ToastUtils.shortToast("您已申请加好友");
                break;
            case 2://人家向我发出好友申请,我显示同意，点击之后变成解除好友
                ContactsManager.onAgreeFriendProtocol(new onAgreeFriendProtocol(),otherUid,"  ");
                break;
            case 3://现在解除好友，点击一下,变成加好友
                ContactsManager.deleteFriendRelationProtocol(new onDeleteFriendRelationProtocol(),otherUid,"   ");
                break;
        }
    }

    //聊一聊
    public void chat(View view) {
        Intent intentChatActivity = new Intent(CommonUtils.getContext(), ChatActivity.class);
        intentChatActivity.putExtra("targetId",String.valueOf(myUid));
        intentChatActivity.putExtra("anonymity",String.valueOf(anonymity));
        userInfoActivity.startActivity(intentChatActivity);
    }

    //关注他
    public void attention(View view) {
        switch (attentionStatus){
            case 1://我关注过他
                ContactsManager.onCannelCareProtocol(new onCannelCareProtocol(),otherUid);
                break;
            case 0://我没关注他
                ContactsManager.onCareTAProtocol(new onCareTAProtocol(),otherUid);
                break;
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
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.ADD_FRIEND_APPLICATION);
                        friendStatus = 1;
                        break;
                    case 0:
                        ToastUtils.shortToast("加好友失败");
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
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.ADD_FRIEND);
                        friendStatus = 0;
                        break;
                    case 0:
                        ToastUtils.shortToast("删除好友失败");
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
                        activityUserinfoBinding.ivCare.setImageResource(R.mipmap.attention_icon);
                        attentionStatus = 1;
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
                        activityUserinfoBinding.ivCare.setImageResource(R.mipmap.yi_attention_icon);
                        attentionStatus = 0;
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

    //同意加好友申请的接口
    public class onAgreeFriendProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        activityUserinfoBinding.tvAddFriend.setText(ContactsManager.IS_FRIEND);
                        friendStatus = 3;
                        break;
                    case 0:
                        ToastUtils.shortToast("同意未成功");
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
