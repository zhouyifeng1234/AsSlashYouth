package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.sip.SipAudioCall;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.slash.youth.databinding.PagerHomeMyBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.MyFirstPageProtocol;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.MyAccountActivity;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.activity.MyHelpActivity;
import com.slash.youth.ui.activity.MySettingActivity;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.activity.ThridPartyActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class PagerHomeMyModel extends BaseObservable {
    PagerHomeMyBinding mPagerHomeMyBinding;
    Activity mActivity;
    //    float totalExpertMarks = 2000;
    float expertⅠMaxMarks = 1000;
    float expertⅡMaxMarks = 4000;
    float expertⅢMaxMarks = 10000;
    float expertⅣMaxMarks = 99999;
    float expertMarks;
    float expertMarksProgress;//0到360
    private int achievetaskcount;
    private int amount;
    private String avatar;
    private int averageservicepoint;
    private int userservicepoint;
    private int careertype;
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
    private int fansratio;
    private int totoltaskcount;
    private MyFirstPageBean.DataBean.MyinfoBean myinfo;

    public PagerHomeMyModel(PagerHomeMyBinding pagerHomeMyBinding, Activity activity) {
        this.mPagerHomeMyBinding = pagerHomeMyBinding;
        this.mActivity = activity;
        initData();
        initAnimation();
        initView();
    }

    RotateAnimation raExpertMarksMaker;

    private void initAnimation() {
        raExpertMarksMaker = new RotateAnimation(0, expertMarksProgress, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        raExpertMarksMaker.setDuration(120 * 16);
        raExpertMarksMaker.setInterpolator(new LinearInterpolator());
        raExpertMarksMaker.setFillAfter(true);
    }

    private void initView() {
        mPagerHomeMyBinding.svPagerHomeMy.setVerticalScrollBarEnabled(false);
        mPagerHomeMyBinding.flHomeMyExpertMarksMaker.startAnimation(raExpertMarksMaker);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.setTotalProgressAngle(expertMarksProgress);
        mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.post(new Runnable() {
            @Override
            public void run() {
                mPagerHomeMyBinding.rsvHomeMyExpertMarksProgress.initRingProgressDraw();
            }
        });
        initExpertMarksProgress();

    }

    private void initExpertMarksProgress() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 120; i++) {
                    try {
                        long startMill = System.currentTimeMillis();
                        final float displayMarks = expertMarks / 120 * (i + 1);
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                mPagerHomeMyBinding.tvHomeMyExpertMarks.setText((int) displayMarks + "");
                            }
                        });
                        long endMill = System.currentTimeMillis();
                        Thread.sleep(16 - (endMill - startMill));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void initData() {
        setExpertMarks(9999);

        //从网络获取我的首页的数据

        MyFirstPageProtocol myFirstPageProtocol = new MyFirstPageProtocol();
         myFirstPageProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<MyFirstPageBean>() {
             @Override
             public void execute(MyFirstPageBean dataBean) {
                 int rescode = dataBean.getRescode();

                 if(rescode == 0){
                     MyFirstPageBean.DataBean data = dataBean.getData();
                     myinfo = data.getMyinfo();


                      setMyInfoData();
                 }else {
                    LogKit.d("rescode : "+rescode);
                 }
             }

             @Override
             public void executeResultError(String result) {
                 LogKit.d("result:"+result);
             }
         });
    }

    //设置我的数据
    private void setMyInfoData() {

        int id = myinfo.getId();

        //姓名
        name = myinfo.getName();
        LogKit.d("name "+name );
        mPagerHomeMyBinding.tvName.setText(name);

        //完成任务的单数
        achievetaskcount = myinfo.getAchievetaskcount();
        totoltaskcount = myinfo.getTotoltaskcount();
        mPagerHomeMyBinding.tvAchieveTaskCount.setText("顺利成交"+achievetaskcount+"单");
        mPagerHomeMyBinding.tvAchieveTaskCount1.setText(achievetaskcount);
        mPagerHomeMyBinding.tvTotolTaskCount.setText("共"+totoltaskcount+"单任务");


        //数量,网络获取的分数
        amount = myinfo.getAmount();
        // setExpertMarks(amount);

        //头像
        avatar = myinfo.getAvatar();
        if(!avatar.isEmpty()){
            x.image().bind(mPagerHomeMyBinding.ivAssistantIcon,avatar);
        }

        //平均服务点
        averageservicepoint = myinfo.getAverageservicepoint();
        //用户服务指向
        userservicepoint = myinfo.getUserservicepoint();
        mPagerHomeMyBinding.tvServicePoint1.setText("服务力"+ userservicepoint +"星");
        mPagerHomeMyBinding.tvAverageServicePoint.setText(userservicepoint);
        mPagerHomeMyBinding.tvUserServicePoint.setText("---平台平均服务力为"+averageservicepoint+"星");

        //职业类型
        careertype = myinfo.getCareertype();
        if(careertype == 1){//固定职业者
            //公司
            company = myinfo.getCompany();
            //技术专家
            position = myinfo.getPosition();
            String companyAndPosition = company +"-"+ position;
            if(!company.isEmpty()){
                mPagerHomeMyBinding.tvMyUserInfoCompany.setText(companyAndPosition);
            }else {
                mPagerHomeMyBinding.tvMyUserInfoCompany.setText("暂未填写职务信息");
            }
        }else if(careertype == 2){//自由职业者
            mPagerHomeMyBinding.tvMyUserInfoCompany.setText("自雇者");
        }

        //城市
       // city = myinfo.getCity();
        //省份
     /*   province = myinfo.getProvince();
        if(!city.equals(province)){
            place = province +""+ city;
        }else {
            place = city;
        }*/



        //是否认证
        isauth = myinfo.getIsauth();
        if(isauth == 1){
            //认证过的
            mPagerHomeMyBinding.ivV.setVisibility(View.VISIBLE);
        }else if(isauth == 0){
            //非认证
            mPagerHomeMyBinding.ivV.setVisibility(View.GONE);
        }

        //行业 方向 技能标签
        industry = myinfo.getIndustry();
        direction = myinfo.getDirection();
        // tag = myinfo.getTag();
        mPagerHomeMyBinding.tvIndustry.setText(industry+" | "+direction);

        //粉丝数
        fanscount = myinfo.getFanscount();
        mPagerHomeMyBinding.tvFansCount.setText("粉丝数"+ fanscount);
        //粉丝比率
        fansratio = myinfo.getFansratio();
        mPagerHomeMyBinding.tvFansRadio.setText(fansratio);
        mPagerHomeMyBinding.tvFansCount.setText("超过平台"+fansratio+"的用户");
        //超出用户的百分比
        mPagerHomeMyBinding.tvOver.setText(fansratio);
    }


    private void setExpertMarks(float expertMarks) {
       // expertMarks = 9999;//这个数据实际应该从服务端获取
//        expertMarksProgress = expertMarks / totalExpertMarks * 360;
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



    //编辑点击事件
        public void editor(View view){
            Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
            intentUserInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentUserInfoActivity);
         }

         //我的账户
         public void  myAccount(View view){
         Intent intentMyAccountActivity = new Intent(CommonUtils.getContext(), MyAccountActivity.class);
         intentMyAccountActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         CommonUtils.getContext().startActivity(intentMyAccountActivity);
         }

        //技能管理
        public void skillManage(View view){
            LogKit.d("技能管理");
            Intent intentMySkillManageActivity = new Intent(CommonUtils.getContext(), MySkillManageActivity.class);
            intentMySkillManageActivity.putExtra("Title","技能管理");
            intentMySkillManageActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentMySkillManageActivity);
        }

        //设置
        public void mySetting(View view){
            LogKit.d("设置");
            Intent intentMySettingActivity = new Intent(CommonUtils.getContext(), MySettingActivity.class);
            intentMySettingActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentMySettingActivity);
        }

        //第三方
        public void myThirdParty(View view){
            LogKit.d("第三方");
            Intent intentThridPartyActivity = new Intent(CommonUtils.getContext(), ThridPartyActivity.class);
            intentThridPartyActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentThridPartyActivity);
        }

        //发布 managePublish
        public void managePublish(View view){
            LogKit.d("发布");
            Intent intentMySkillManageActivity = new Intent(CommonUtils.getContext(), MySkillManageActivity.class);
            intentMySkillManageActivity.putExtra("Title","管理我发布的任务");
            intentMySkillManageActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentMySkillManageActivity);
        }

        //帮助
        public void help(View view){
            LogKit.d("帮助");
        Intent intentMyHelpActivity = new Intent(CommonUtils.getContext(), MyHelpActivity.class);
        intentMyHelpActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentMyHelpActivity);

        }

        //常见问题
        public void collection(View view){
            LogKit.d("我的收藏");
            Intent intentMyCollectionActivity = new Intent(CommonUtils.getContext(), MyCollectionActivity.class);
            intentMyCollectionActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentMyCollectionActivity);
        }








}
