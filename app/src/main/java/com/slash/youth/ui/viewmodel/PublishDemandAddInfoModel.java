package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandAddinfoBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.DemandDetailBean;
import com.slash.youth.domain.PublishDemandResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishDemandBaseInfoActivity;
import com.slash.youth.ui.activity.PublishDemandSuccessActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class PublishDemandAddInfoModel extends BaseObservable {

    ActivityPublishDemandAddinfoBinding mActivityPublishDemandAddinfoBinding;
    Activity mActivity;
    boolean isOnline = true;//“线上”或者“线下”，默认为线上
    boolean isInstalment = true;//是否开启分期付，默认为true,开启
    public SlashAddLabelsLayout mSallSkillLabels;

    public int checkedDisputeHandingTypeIndex = 0;//选择的纠纷处理方式
    private double lng;
    private double lat;
    DemandDetailBean demandDetailBean;
    boolean isCheckedSlashProtocol = true;

    public PublishDemandAddInfoModel(ActivityPublishDemandAddinfoBinding activityPublishDemandAddinfoBinding, Activity activity) {
        this.mActivityPublishDemandAddinfoBinding = activityPublishDemandAddinfoBinding;
        this.mActivity = activity;
        //原来分期默认为开启的，现在需要改成默认为关闭，调用一次该方法就变成关闭了
        toggleInstalment(null);
        initData();
        initView();
    }

    private void initData() {
        mSallSkillLabels = mActivityPublishDemandAddinfoBinding.sallPublishDemandAddedSkilllabels;
        demandDetailBean = (DemandDetailBean) mActivity.getIntent().getSerializableExtra("demandDetailBean");
        if (demandDetailBean != null) {
            mActivityPublishDemandAddinfoBinding.tvPublishDemandText.setText("修改需求");
            mActivityPublishDemandAddinfoBinding.btnPublishText.setText("修改");
            mActivityPublishDemandAddinfoBinding.tvPublishSuccessText.setText("修改成功");
            loadDemandDetailData();
        }
    }

    private void initView() {
        mActivityPublishDemandAddinfoBinding.svPublishDemandLabels.setVerticalScrollBarEnabled(false);
        mSallSkillLabels.setActivity(mActivity);
        mSallSkillLabels.initSkillLabels();


    }

    /**
     * 修改需求时回填需求详情数据
     */
    private void loadDemandDetailData() {
        DemandDetailBean.Demand demand = demandDetailBean.data.demand;
        //回填技能标签
        String[] tags = demand.tag.split(",");
        ArrayList<String> reloadTagsName = new ArrayList<String>();
        ArrayList<String> reloadTags = new ArrayList<String>();
        for (String tag : tags) {
            reloadTags.add(tag);

            String[] tagInfo = tag.split("-");
            String tagName;
            if (tagInfo.length == 3) {
                tagName = tagInfo[2];
            } else {
                tagName = tag;
            }
            reloadTagsName.add(tagName);
        }
        mSallSkillLabels.reloadSkillLabels(reloadTagsName, reloadTags);
        //回填报价
        if (demand.quote > 0) {
            mActivityPublishDemandAddinfoBinding.etDemandQuote.setText(demand.quote + "");
        }
        //回填分期开关
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.getLayoutParams();
        if (demand.instalment == 0) {//分期关闭
            isInstalment = false;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else if (demand.instalment == 1) {//分期开启
            isInstalment = true;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.setLayoutParams(layoutParams);
        //回填线上、线下
        if (demand.pattern == 0) {//线上
            checkOnline(null);
        } else if (demand.pattern == 1) {//线下
            checkOffline(null);
        }
        //线下地址
        mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.setText(demand.place);
        lng = demand.lng;
        lat = demand.lat;
        //纠纷处理方式
        if (demand.bp == 0) {
            checkPlatformProcessing(null);
        } else {
            checkConsultProcessing(null);
        }
    }

    public void gotoBack(View v) {
        mActivity.finish();
    }

    boolean isClickPublish = false;

    public void publish(View v) {
        if (isClickPublish) {
            return;
        }
        isClickPublish = true;

        if (!isCheckedSlashProtocol) {
            ToastUtils.shortToast("请查阅零佣金活动，并勾选");
            isClickPublish = false;
            return;
        }

        Bundle bundleDemandData = mActivity.getIntent().getExtras();
        int anonymity = bundleDemandData.getInt("anonymity");
        String demandTitle = bundleDemandData.getString("demandTitle");
        String demandDesc = bundleDemandData.getString("demandDesc");
        long startTime = bundleDemandData.getLong("startTime");
        ArrayList<String> listPic = bundleDemandData.getStringArrayList("pic");


//        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedTagsName();
        ArrayList<String> addedSkillLabels = mSallSkillLabels.getAddedTags();
        if (addedSkillLabels.size() < 1) {
            ToastUtils.shortToast("请选择技能标签");
            isClickPublish = false;
            return;
        }
        if (addedSkillLabels.size() > 3) {
            ToastUtils.shortToast("技能标签不能超过3个");
            isClickPublish = false;
            return;
        }
        double quote = 0;//报价
        int offer;//报价类型 0 需求方报价    1 服务方报价
        String quoteStr = mActivityPublishDemandAddinfoBinding.etDemandQuote.getText().toString();
        if (TextUtils.isEmpty(quoteStr)) {
            offer = 1;//服务方报价
        } else {
            offer = 0;//需求方报价
            try {
                quote = Double.parseDouble(quoteStr);
                if (quote <= 0) {
                    ToastUtils.shortToast("报价必须大于0");
                    isClickPublish = false;
                    return;
                }
            } catch (Exception ex) {
                ToastUtils.shortToast("请正确填写报价");
                isClickPublish = false;
                return;
            }
        }

        int instalment = isInstalment == true ? 1 : 0;//1开启，0关闭
        int pattern = isOnline == true ? 0 : 1;//1线下 0线上
        int bp = checkedDisputeHandingTypeIndex + 1;//1平台 2协商
        String place = getLocationAddress();
        if (pattern == 1) {
            if (TextUtils.isEmpty(place)) {
                ToastUtils.shortToast("请输入线下见面地点");
                isClickPublish = false;
                return;
            }
        }

        if (demandDetailBean != null) {//修改需求
            DemandEngine.updateDemand(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    isClickPublish = false;
                    Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
                    intentPublishDemandSuccessActivity.putExtra("demandId", demandDetailBean.data.demand.id);
                    intentPublishDemandSuccessActivity.putExtra("isUpdate", true);
                    mActivity.startActivity(intentPublishDemandSuccessActivity);
                    mActivity.finish();
                    if (PublishDemandBaseInfoActivity.mActivity != null) {
                        PublishDemandBaseInfoActivity.mActivity.finish();
                        PublishDemandBaseInfoActivity.mActivity = null;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    isClickPublish = false;
                    ToastUtils.shortToast("修改需求失败:" + result);
                }
            }, demandDetailBean.data.demand.id + "", demandTitle, addedSkillLabels, startTime + "", anonymity + "", demandDesc, listPic, instalment + "", bp + "", pattern + "", place, place, lng + "", lat + "", offer + "", quote + "");
        } else {//发布需求
            DemandEngine.publishDemand(new BaseProtocol.IResultExecutor<PublishDemandResultBean>() {
                @Override
                public void execute(PublishDemandResultBean dataBean) {
                    isClickPublish = false;
                    Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
                    intentPublishDemandSuccessActivity.putExtra("demandId", dataBean.data.id);
                    mActivity.startActivity(intentPublishDemandSuccessActivity);
                    mActivity.finish();
                    if (PublishDemandBaseInfoActivity.mActivity != null) {
                        PublishDemandBaseInfoActivity.mActivity.finish();
                        PublishDemandBaseInfoActivity.mActivity = null;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    isClickPublish = false;
                    ToastUtils.shortToast("发布需求失败：" + result);
                }
            }, demandTitle, addedSkillLabels, startTime + "", anonymity + "", demandDesc, listPic, instalment + "", bp + "", pattern + "", place, place, lng + "", lat + "", offer + "", quote + "");
        }


//        Intent intentPublishDemandSuccessActivity = new Intent(CommonUtils.getContext(), PublishDemandSuccessActivity.class);
//        mActivity.startActivity(intentPublishDemandSuccessActivity);
//        mActivity.finish();
//        if (PublishDemandBaseInfoActivity.mActivity != null) {
//            PublishDemandBaseInfoActivity.mActivity.finish();
//            PublishDemandBaseInfoActivity.mActivity = null;
//        }
    }

    public void checkOnline(View v) {
        isOnline = true;
        setOfflineItemVisibility(View.GONE);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOnlineIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOfflineIcon.setImageResource(R.mipmap.default_btn);
    }

    public void checkOffline(View v) {
        isOnline = false;
        setOfflineItemVisibility(View.VISIBLE);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOnlineIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishDemandAddinfoBinding.ivPublishDemandOfflineIcon.setImageResource(R.mipmap.pitchon_btn);
    }

    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.getLayoutParams();
        if (isInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishDemandAddinfoBinding.ivPublishDemandInstalmentHandle.setLayoutParams(layoutParams);
        isInstalment = !isInstalment;
    }

    public void openLabelsActivity(View v) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        ArrayList<String> addedTagsName = mSallSkillLabels.getAddedTagsName();
        ArrayList<String> addedTags = mSallSkillLabels.getAddedTags();
        intentSubscribeActivity.putStringArrayListExtra("addedTagsName", addedTagsName);
        intentSubscribeActivity.putStringArrayListExtra("addedTags", addedTags);
        intentSubscribeActivity.putExtra("isPublish", true);
        mActivity.startActivityForResult(intentSubscribeActivity, 10);

        mSallSkillLabels.listTotalAddedTagsNames.clear();
        mSallSkillLabels.listTotalAddedTags.clear();
        //mSallSkillLabels.initSkillLabels();
    }

    public void openMapActivity(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
//        intentMapActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mActivity.startActivityForResult(intentMapActivity, 20);
    }

    /**
     * 选择平台处理方式
     *
     * @param v
     */
    public void checkPlatformProcessing(View v) {
        mActivityPublishDemandAddinfoBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishDemandAddinfoBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.default_btn);
        checkedDisputeHandingTypeIndex = 0;
    }

    /**
     * 选择协商处理方式
     *
     * @param v
     */
    public void checkConsultProcessing(View v) {
        mActivityPublishDemandAddinfoBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishDemandAddinfoBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        checkedDisputeHandingTypeIndex = 1;
    }

    /**
     * @param v
     */
    public void checkSlashProtocol(View v) {
        if (isCheckedSlashProtocol) {
            isCheckedSlashProtocol = false;
            mActivityPublishDemandAddinfoBinding.ivSlashProtocolCheckedIcon.setImageResource(R.mipmap.no_checked_icon);
        } else {
            isCheckedSlashProtocol = true;
            mActivityPublishDemandAddinfoBinding.ivSlashProtocolCheckedIcon.setImageResource(R.mipmap.checked_icon);
        }
    }

    private static final String securityRulesTitle = "斜杠青年顺利成交保障规则";
    private static final String securityRulesContent = "斜杠青年通过顺利成交保证金、预支付、分期到账等一系列规则来保障双方的顺利成交。\n" +
            "\n" +
            "1、顺利成交保证金规则\n" +
            "\n" +
            "本服务平台将实际交易金额的5%计提为“顺利成交保证金”，任务顺利完成并且服务、需求双方评价分享后，平台将以交易金额的2.5%奖励形式返还给双方。\n" +
            "\n" +
            "如果任务并未顺利成交，已经开始的“服务阶段”对应的“顺利成交保证金”将，不予退还，存放奖金池 用于活动基金；未开始“服务阶段”对应的“顺利成交保证金”将退还给需求方。上述“服务阶段”是指双方用户达成的“分期到账”后各期对应的服务阶段。\n" +
            "\n" +
            "\n" +
            "2、预支付与分期到账规则\n" +
            "\n" +
            "预支付：为了保障交易双方的权益，双方确认交易意向后，应支付的全部款项将会一次性预支付且托管在斜杠平台。\n" +
            "\n" +
            "分期到帐：基于任务的阶段性特征，若双方将任务划分成若干个阶段，预支付的资金也将会被划分成对应的若干个部分，在需求方确认服务方提交的某阶段的服务时，该阶段的资金将被划转给服务方。\n" +
            "\n" +
            "3、到账与冻结规则\n" +
            "到账：\n" +
            "在没有开启分期付功能的情况下，任务顺利完成后，需求方确认服务后款项立即到账，即可提现；\n" +
            "在开启分期付功能后，非尾期账款在需求方确认服务后款项立即到账，暂时处于冻结状态；尾期账款在需求方确认服务后款项立即到账，全部资金可提现。\n" +
            "\n" +
            "冻结：\n" +
            "为了保障交易双方的权益，服务期间，涉及到当前任务的已到账资金处于冻结状态。冻结是暂时的，在全部任务顺利完成后，资金将解冻，服务方可以用于提现或支付。如果没有顺利完成，将按照双方选择的平台处理规则或者双方协商的规则来处理，具体请见“帮助-常见问题”。";


    /**
     * 打开《斜杠青年顺利成交保障规则》
     *
     * @param v
     */
    public void openSecurityRules(View v) {
        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {
                LogKit.d("close SecurityRules");
            }
        }, securityRulesContent, securityRulesTitle);
    }

    private static final String instalmentInfoTitle = "分期到账";
    private static final String instalmentInfoContent = "基于任务的阶段性特征，若双方将任务划分成若干个阶段，预支付的资金也将会被划分成对应的若干个部分，在需求方确认服务方提交的某阶段的服务时，该阶段的资金将被划转给服务方。";

    /**
     * 开启分期付问号
     *
     * @param v
     */
    public void openInstalmentInfo(View v) {
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_REQUIREMENT_OPEN_INSTALL_ACCOUNT_QUESTION);

        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {

            }
        }, instalmentInfoContent, instalmentInfoTitle);
    }

    private static final String bpInfoTitle = "纠纷处理规则";
    private static final String bpInfoContent = "针对交易过程中出现的争议、纠纷等情况，本平台提供平台处理规则和双方协商规则的两种方式。\n" +
            "\n" +
            "平台处理规则：\n" +
            "若任务开启了分期到账，\n" +
            "1）未开始的分期阶段对应的资金，全额退给需求方\n" +
            "2）已开始未完成的或已完成未被需求方认可的分期阶段对应的资金，扣除顺利成交保证金（5%）后退款给需求方\n" +
            "3）已完成并被需求方认可的分期阶段对应的资金，扣除顺利成交保证金（5%）后划转给服务方。\n" +
            "若任务未开启分期到账，任务没有顺利完成的（需求方支付后，并没有认可对方的服务结果），扣除顺利成交保证金（5%）后退款给需求方。\n" +
            "\n" +
            "上述“开始”是指需求方支付后开始第一期服务或需求方确认某期服务后开始下期服务。\n" +
            "\n" +
            "双方协商规则：除平台处理方规则外，交易双方还可以选择“双方协商规则”方式处理纠纷。纠纷出现时，平台将依据双方提供的本客户端聊天截图、协议等资料来判断退款金额。对于处理结果双方不满意的，双方可以通过专业鉴定机构等第三方进行裁决，客服根据双方认可的裁决进行退款。\n" +
            "附则：本客户端之外的其他第三方聊天、通讯记录不具备法律效用。";

    /**
     * 纠纷处理问号
     *
     * @param v
     */
    public void openBpInfo(View v) {
        DialogUtils.showDialogOne(mActivity, new DialogUtils.DialogCallUnderStandBack() {
            @Override
            public void OkDown() {

            }
        }, bpInfoContent, bpInfoTitle);
    }

    private int offlineItemVisibility = View.GONE;

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

    public void setLocationAddress(String address, String name, double lng, double lat) {
//        ToastUtils.shortToast(address);
        if (!TextUtils.isEmpty(name)) {
            mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.setText(name);
        } else {
            mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.setText(address);
        }
        this.lng = lng;
        this.lat = lat;
    }

    public String getLocationAddress() {
        return mActivityPublishDemandAddinfoBinding.etPublishDemandAddress.getText().toString();
    }
}
