package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.slash.youth.BR;
import com.slash.youth.databinding.PagerHomeMyBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.OtherInfoBean;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.activity.BindThridPartyActivity;
import com.slash.youth.ui.activity.MyAccountActivity;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.MyHelpActivity;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.ui.activity.VisitorsActivity;
import com.slash.youth.ui.activity.WebViewActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.CountUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeMyModel extends BaseObservable {
    private PagerHomeMyBinding mPagerHomeMyBinding;
    Activity mActivity;
    //   float totalExpertMarks = 2000;
    float expertⅠMaxMarks = 1000;
    float expertⅡMaxMarks = 4000;
    float expertⅢMaxMarks = 30000;
    float expertⅣMaxMarks = 99999;
    float expertMarks;
    float expertMarksProgress;//0到360
    private int achievetaskcount;
    private String avatar;
    private double averageservicepoint;
    private double userservicepoint;
    private int careertype = 1;
    private String company;
    private String position;
    private String city;
    private String province;
    private String place;
    private String name;
    private int isauth;
    private String industry;
    private String direction;
    private String tag;
    private int fanscount;
    private double fansratio;
    private long totoltaskcount;
    private long id;
    public String phone;
    private String unit = "元";
    private MyFirstPageBean.DataBean.MyinfoBean myinfo;
    private String desc;
    private RotateAnimation raExpertMarksMaker;
    private int loadLayerVisibility = View.GONE;
    private int approvalDialog = View.GONE;
    private int taskProgress;
    private String[] grades = {"少侠", "大侠", "宗师", "至尊"};//4000  10000 请等待客服审核
    private String grade;
    int addMeFriendCount;
    int myAddFriendCount;

    private int myAddFriendLocalCount;
    private int addMeFriendLocalCount;

    public PagerHomeMyModel(PagerHomeMyBinding pagerHomeMyBinding, Activity activity) {
        this.mPagerHomeMyBinding = pagerHomeMyBinding;
        this.mActivity = activity;
        //  displayLoadLayer();
        initData();
        initView();
        listener();
    }

    //数据加载完毕后
    private void hideLoadLayer() {
        setLoadLayerVisibility(View.GONE);
    }

    @Bindable
    public int getLoadLayerVisibility() {
        return loadLayerVisibility;
    }

    public void setLoadLayerVisibility(int loadLayerVisibility) {
        this.loadLayerVisibility = loadLayerVisibility;
        notifyPropertyChanged(BR.loadLayerVisibility);
    }

    @Bindable
    public int getApprovalDialog() {
        return approvalDialog;
    }

    public void setApprovalDialog(int approvalDialog) {
        this.approvalDialog = approvalDialog;
        notifyPropertyChanged(BR.approvalDialog);
    }

    public void makeCannel(View view) {
        setApprovalDialog(View.GONE);
    }

    //确认
    public void makeSure(View view) {
        setApprovalDialog(View.GONE);
        Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
        intentUserinfoEditorActivity.putExtra("phone", phone);
        intentUserinfoEditorActivity.putExtra("myUinfo", myinfo);
        intentUserinfoEditorActivity.putExtra("myId", id);
        mActivity.startActivityForResult(intentUserinfoEditorActivity, UserInfoEngine.MY_USER_EDITOR);
    }

    private void initAnimation() {
        raExpertMarksMaker = new RotateAnimation(0, expertMarksProgress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raExpertMarksMaker.setDuration(120 * 16);
        raExpertMarksMaker.setInterpolator(new LinearInterpolator());
        raExpertMarksMaker.setFillAfter(true);
    }

    private void initView() {
        mPagerHomeMyBinding.svPagerHomeMy.setVerticalScrollBarEnabled(false);


        ContactsManager.getPersonRelationFirstPage(new onPersonRelationFirstPage());
    }

    private void initScoreView() {
        mPagerHomeMyBinding.flHomeMyExpertMarksMaker.startAnimation(raExpertMarksMaker);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.setStartProgressAngle(0);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.setTotalProgressAngle(expertMarksProgress);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.post(new Runnable() {
            @Override
            public void run() {
                mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.initRingProgressDraw();
            }
        });
    }

    private void initExpertMarksProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 120; i++) {
                    try {
                        long startMill = System.currentTimeMillis();
                        final float displayMarks = expertMarks * (i + 1) / 120;
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mPagerHomeMyBinding.tvHomeMyExpertMarks.setText((int) displayMarks + "");
                            }
                        });
                        long endMill = System.currentTimeMillis();
                        if (16 - (endMill - startMill) > 0) {
                            Thread.sleep(16 - (endMill - startMill));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void initData() {

        myAddFriendLocalCount = SpUtils.getInt("myAddFriendCount", 0);
        addMeFriendLocalCount = SpUtils.getInt("addMeFriendCount", 0);

        MyManager.getMyUserinfo(new OnGetMyUserinfo());
        MyManager.getOtherPersonInfo(new onGetOtherPersonInfo(), LoginManager.currentLoginUserId, 1);
    }

    private String careertypeString = "自雇者";

    //首页展示的数据
    public class onPersonRelationFirstPage implements BaseProtocol.IResultExecutor<PersonRelationBean> {
        @Override
        public void execute(PersonRelationBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                PersonRelationBean.DataBean data = dataBean.getData();
                PersonRelationBean.DataBean.InfoBean info = data.getInfo();
                addMeFriendCount = info.getAddMeFriendCount();
                myAddFriendCount = info.getMyAddFriendCount();

                if (addMeFriendLocalCount != addMeFriendCount) {
                    mPagerHomeMyBinding.ivMineContacts.setVisibility(View.VISIBLE);
                }
                //访客接口还没有
//                if (myAddFriendLocalCount != myAddFriendCount) {
//                    mPagerHomeMyBinding.ivMineVisitors.setVisibility(View.VISIBLE);
//                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //获取其他用户信息
    public class onGetOtherPersonInfo implements BaseProtocol.IResultExecutor<OtherInfoBean> {
        @Override
        public void execute(OtherInfoBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                OtherInfoBean.DataBean data = dataBean.getData();//myinfo
                OtherInfoBean.DataBean.UinfoBean uinfo = data.getUinfo();
                int relationshipscount = uinfo.getRelationshipscount();
                mPagerHomeMyBinding.tvFansCount.setText("" + relationshipscount);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //设置我的数据
    private void setMyInfoData() {
        //描述详情
        desc = myinfo.getDesc();
        //电话号码
        phone = myinfo.getPhone();
        //金额
        double amount = myinfo.getAmount();
        String totalMoney = CountUtils.DecimalFormat(amount);
        mPagerHomeMyBinding.tvAmount.setText(totalMoney + unit);
        //用户的id
        id = myinfo.getId();
        //姓名
        name = myinfo.getName();
        mPagerHomeMyBinding.tvName.setText(name);
        //是否认证
        isauth = myinfo.getIsauth();
        if (isauth == 1) {  //认证过的
            mPagerHomeMyBinding.ivV.setVisibility(View.VISIBLE);
            mPagerHomeMyBinding.tvMyApproval.setVisibility(View.GONE);
        } else if (isauth == 0) {    //非认证
            mPagerHomeMyBinding.ivV.setVisibility(View.GONE);
            mPagerHomeMyBinding.tvMyApproval.setVisibility(View.VISIBLE);
        }
        //头像
        avatar = myinfo.getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            BitmapKit.bindImage(mPagerHomeMyBinding.ivAssistantIcon, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        //行业 方向 技能标签
        industry = myinfo.getIndustry();
        direction = myinfo.getDirection();
        tag = myinfo.getTag();
        if (!TextUtils.isEmpty(direction)) {
//            mPagerHomeMyBinding.tvIndustry.setText(direction);
        }

        //职业类型
        careertype = myinfo.getCareertype();
        if (careertype == 1) {//固定职业者
            //公司
            company = myinfo.getCompany();
            //技术专家
            position = myinfo.getPosition();
            String companyAndPosition = company + "-" + position;
            if (!company.isEmpty()) {
//                mPagerHomeMyBinding.tvMyUserInfoCompany.setText(companyAndPosition);
            } else {
//                mPagerHomeMyBinding.tvMyUserInfoCompany.setText("暂未填写职务信息");
            }
        } else if (careertype == 2) {//自由职业者
//            mPagerHomeMyBinding.tvMyUserInfoCompany.setText(careertypeString);
        }

        //城市    //省份
        city = myinfo.getCity();
        province = myinfo.getProvince();
        if (!city.equals(province)) {
            place = province + "" + city;
        } else {
            place = city;
        }

        //粉丝数
        fansratio = myinfo.getFansratio();
//        if (fansratio > 0 && fansratio <= 1) {
//            fansratio = 1;
//        }
//        mPagerHomeMyBinding.pbProgressbarFans.setProgress((int) (fansratio * 100));
//        //粉丝比率
//        mPagerHomeMyBinding.tvFansRadio.setText((int) (fansratio * 100) + "%");
//        mPagerHomeMyBinding.tvOverFansCount.setText("超过" + (int) (fansratio * 100) + "%" + "的用户");
        //完成任务的单数
        achievetaskcount = myinfo.getAchievetaskcount();
        totoltaskcount = myinfo.getTotoltaskcount();
        if (totoltaskcount != 0) {
            taskProgress = (int) (achievetaskcount * 100 / totoltaskcount);
        } else {
            taskProgress = 0;
        }
//        mPagerHomeMyBinding.pbProgressbarTask.setProgress((int) taskProgress);
        mPagerHomeMyBinding.tvMyAchieveTaskCount.setText("" + achievetaskcount + "");
//        mPagerHomeMyBinding.tvMyTask.setText(String.valueOf(achievetaskcount));
//        mPagerHomeMyBinding.tvMyTotolTaskCount.setText("共" + totoltaskcount + "单任务");
        //平均服务点
        averageservicepoint = myinfo.getAverageservicepoint();

        DecimalFormat df = new DecimalFormat("######0.0");
        //用户服务指向
        userservicepoint = myinfo.getUserservicepoint();
        int servicepoint = (int) ((averageservicepoint * 100) / 5);
//        mPagerHomeMyBinding.pbProgressbarService.setProgress((int) servicepoint);
        mPagerHomeMyBinding.tvServicePoint1.setText("" + userservicepoint + "星");
////        mPagerHomeMyBinding.tvAverageServicePoint.setText(String.valueOf(averageservicepoint));
//        mPagerHomeMyBinding.tvAverageServicePoint.setText(df.format(averageservicepoint));
//        mPagerHomeMyBinding.tvUserServicePoint.setText("平台平均服务力为" + df.format(averageservicepoint) + "星");

        //数量,网络获取的分数
       /* int expertscore = myinfo.getExpertscore();
        mPagerHomeMyBinding.tvLeastMark.setText(expertscore + "");*/
        //超出用户的百分比
        double expertratio = myinfo.getExpertratio();
        int v = (int) (expertratio * 100);
        mPagerHomeMyBinding.tvOver.setText(v + "%");

        List<Integer> expertlevels = myinfo.getExpertlevels();//每个等级对应的分数
        if (expertlevels.size() != 0) {
            expertMarks = myinfo.getExpertscore();
            int expertlevel = myinfo.getExpertlevel();//当前对应的等级
            if (expertlevel > 0 && expertlevel <= 4) {
                grade = grades[expertlevel];
                mPagerHomeMyBinding.tvGrade.setText(grade);
                int expertscore = expertlevels.get(expertlevel - 1);
                int mark = (int) (expertscore - expertMarks);
                mPagerHomeMyBinding.tvLeastMark.setText(mark + "");
            }
        } else {
            mPagerHomeMyBinding.tvLeastMark.setText("0");
        }

        if (expertlevels.size() != 0) {
            for (int i = 0; i < expertlevels.size(); i++) {
                if (i == 0) {
                    expertⅠMaxMarks = (float) expertlevels.get(0);
                }
                if (i == 1) {
                    expertⅡMaxMarks = (float) expertlevels.get(1);
                }

                if (i == 2) {
                    expertⅢMaxMarks = (float) expertlevels.get(2);
                }
                if (i == 3) {
                    expertⅣMaxMarks = (float) expertlevels.get(3);
                }
            }
        }

        setExpertMarks();
        initAnimation();
//        initScoreView();
//        initExpertMarksProgress();
        isLoadDataFinished = true;
    }

    boolean isLoadDataFinished = false;

    public void doMarksAnimation() {
        if (isLoadDataFinished) {
            initScoreView();
            initExpertMarksProgress();
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        LogKit.v("HomeMyPager waiting load data...");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (isLoadDataFinished) {
                            CommonUtils.getHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    initScoreView();
                                    initExpertMarksProgress();
                                }
                            });
                            return;
                        }
                    }
                }
            }).start();
        }
    }

    private void setExpertMarks() {
//        expertMarks = 2000;//这个数据实际应该从服务端获取
//             expertMarksProgress = expertMarks / totalExpertMarks * 360;
        if (expertMarks >= 0 && expertMarks <= expertⅠMaxMarks) {
            expertMarksProgress = expertMarks / expertⅠMaxMarks * 90;
        } else if (expertMarks <= expertⅡMaxMarks) {
            expertMarksProgress = 90 + (expertMarks - expertⅠMaxMarks) / (expertⅡMaxMarks - expertⅠMaxMarks) * 90;
        } else if (expertMarks <= expertⅢMaxMarks) {
            expertMarksProgress = 180 + (expertMarks - expertⅡMaxMarks) / (expertⅢMaxMarks - expertⅡMaxMarks) * 90;
        } else if (expertMarks <= expertⅣMaxMarks) {
            expertMarksProgress = 270 + (expertMarks - expertⅢMaxMarks) / (expertⅣMaxMarks - expertⅢMaxMarks) * 90;
        }
    }

    //认证
    public void identificate(View view) {
        Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
        intentApprovalActivity.putExtra("careertype", careertype);
        intentApprovalActivity.putExtra("Uid", LoginManager.currentLoginUserId);
        mActivity.startActivityForResult(intentApprovalActivity, UserInfoEngine.MY_USER_EDITOR);
    }

    //我的好友
    public void MyFriend(View view) {
        Intent intentChooseFriendActivtiy = new Intent(CommonUtils.getContext(), MyFriendActivtiy.class);
        mActivity.startActivity(intentChooseFriendActivtiy);
    }

    //最近访客
    public void visitor(View view) {
        Intent intentChooseFriendActivtiy = new Intent(CommonUtils.getContext(), VisitorsActivity.class);
        mActivity.startActivity(intentChooseFriendActivtiy);

    }

    //去认证
    private void listener() {
        mPagerHomeMyBinding.tvMyApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_APPROVE);

                switch (careertype) {
                    case 1:
                        if (TextUtils.isEmpty(myinfo.getCompany()) || TextUtils.isEmpty(myinfo.getName()) || TextUtils.isEmpty(myinfo.getPosition())) {
                            setApprovalDialog(View.VISIBLE);
                        } else {
                            jumpApprovalActivity();
                        }
                        break;
                    case 2:
                        if (TextUtils.isEmpty(myinfo.getName())) {
                            setApprovalDialog(View.VISIBLE);
                        } else {
                            jumpApprovalActivity();
                        }
                        break;
                    case 0:
                        setApprovalDialog(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void jumpApprovalActivity() {
        Intent intentApprovalActivity = new Intent(CommonUtils.getContext(), ApprovalActivity.class);
        intentApprovalActivity.putExtra("careertype", careertype);
        intentApprovalActivity.putExtra("Uid", LoginManager.currentLoginUserId);
        mActivity.startActivityForResult(intentApprovalActivity, Constants.APPROVAL);
    }

    //编辑点击事件
    public void editor(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_EDIT_PROFILE);

        Intent intentUserinfoEditorActivity = new Intent(CommonUtils.getContext(), UserinfoEditorActivity.class);
        intentUserinfoEditorActivity.putExtra("phone", phone);
        intentUserinfoEditorActivity.putExtra("myUinfo", myinfo);
        intentUserinfoEditorActivity.putExtra("myId", id);
        mActivity.startActivityForResult(intentUserinfoEditorActivity, UserInfoEngine.MY_USER_EDITOR);
    }

    //个人信息页面
    public void personInfo(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_PERSON_MESSAGE);

        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        intentUserInfoActivity.putExtra("phone", phone);
        intentUserInfoActivity.putExtra("skillTag", tag);
        intentUserInfoActivity.putExtra("Uid", LoginManager.currentLoginUserId);
        mActivity.startActivity(intentUserInfoActivity);
    }

    //我的账户
    public void myAccount(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_ACCOUNT);

        Intent intentMyAccountActivity = new Intent(CommonUtils.getContext(), MyAccountActivity.class);
        intentMyAccountActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMyAccountActivity);
    }

    //技能管理
    public void skillManage(View view) {
        Intent intentMySkillManageActivity = new Intent(CommonUtils.getContext(), MySkillManageActivity.class);
        intentMySkillManageActivity.putExtra("Title", Constants.MY_TITLE_SKILL_MANAGER);
        intentMySkillManageActivity.putExtra("name", name);
        intentMySkillManageActivity.putExtra("avater", avatar);
        intentMySkillManageActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMySkillManageActivity);
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SKILL_AGREEMENT);
    }

    //设置
    public void mySetting(View view) {
        Intent intentMySettingActivity = new Intent(CommonUtils.getContext(), MySettingActivity.class);
        intentMySettingActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMySettingActivity);
        //设置的埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SET);
    }

    //第三方
    public void myThirdParty(View view) {
        Intent intentThridPartyActivity = new Intent(CommonUtils.getContext(), BindThridPartyActivity.class);
        mActivity.startActivity(intentThridPartyActivity);
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_THIRD_PARTY_ACCOUNT);
    }

    //我的发布
    public void managePublish(View view) {
        Intent intentMySkillManageActivity = new Intent(CommonUtils.getContext(), MySkillManageActivity.class);
        intentMySkillManageActivity.putExtra("Title", Constants.MY_TITLE_MANAGER_MY_PUBLISH);
        intentMySkillManageActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMySkillManageActivity);
    }

    //帮助
    public void help(View view) {
        Intent intentMyHelpActivity = new Intent(CommonUtils.getContext(), MyHelpActivity.class);
        intentMyHelpActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMyHelpActivity);
        //帮助的埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_HELP);
    }

    //我的收藏
    public void collection(View view) {
        Intent intentMyCollectionActivity = new Intent(CommonUtils.getContext(), MyCollectionActivity.class);
        intentMyCollectionActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMyCollectionActivity);
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_COLLECT);
    }

    //获取我的用户信息数据
    public class OnGetMyUserinfo implements BaseProtocol.IResultExecutor<MyFirstPageBean> {
        @Override
        public void execute(MyFirstPageBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                hideLoadLayer();
                MyFirstPageBean.DataBean data = dataBean.getData();
                myinfo = data.getMyinfo();
                setMyInfoData();
            } else {
                LogKit.d("rescode : " + rescode);
            }
        }

        @Override
        public void executeResultError(String result) {
        }
    }

    //点击问号，影响力
    public void influence(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_INFLUENCE);

        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), WebViewActivity.class);
        intentCommonQuestionActivity.putExtra("influence", "influence");
        mActivity.startActivity(intentCommonQuestionActivity);
    }

    public void updateMessage() {
        mPagerHomeMyBinding.ivMineContacts.setVisibility(View.GONE);
    }

//    private int myloadLayerVisibility = View.GONE;
//
//    @Bindable
//    public int getMyloadLayerVisibility() {
//        return myloadLayerVisibility;
//    }
//
//    public void setMyloadLayerVisibility(int myloadLayerVisibility) {
//        this.myloadLayerVisibility = myloadLayerVisibility;
//        notifyPropertyChanged(BR.myloadLayerVisibility);
//    }
}