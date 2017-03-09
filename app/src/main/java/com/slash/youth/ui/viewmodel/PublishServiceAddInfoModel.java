package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceAddinfoBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.PublishServiceResultBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MapActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.activity.PublishServiceSucceddActivity;
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
 * Created by zhouyifeng on 2016/11/9.
 */
public class PublishServiceAddInfoModel extends BaseObservable {

    ActivityPublishServiceAddinfoBinding mActivityPublishServiceAddinfoBinding;
    Activity mActivity;

    boolean isOnline = true;//“线上”或者“线下”，默认为线上
    boolean isInstalment = true;//是否开启分期付，默认为true,开启
    public SlashAddLabelsLayout mSallSkillLabels;

    public int checkedDisputeHandingTypeIndex = 0;//选择的纠纷处理方式
    private NumberPicker mNpChoosePriceUnit;
    String[] optionalPriceUnit;
    private String mChoosePriceUnit;
    private int quoteunit = -1;
    private double lng = 0;
    private double lat = 0;
    ServiceDetailBean serviceDetailBean;
    boolean isFromSkillManager;

    public PublishServiceAddInfoModel(ActivityPublishServiceAddinfoBinding activityPublishServiceAddinfoBinding, Activity activity) {
        this.mActivityPublishServiceAddinfoBinding = activityPublishServiceAddinfoBinding;
        this.mActivity = activity;
        //原来分期默认为开启的，现在需要改成默认为关闭，调用一次该方法就变成关闭了
        toggleInstalment(null);
        initData();
        initView();
    }

    private void initData() {
        mSallSkillLabels = mActivityPublishServiceAddinfoBinding.sallPublishServiceAddedSkilllabels;//在 loadOriginServiceData()中会使用，所以必须在这里初始化
        optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

        serviceDetailBean = (ServiceDetailBean) mActivity.getIntent().getSerializableExtra("serviceDetailBean");
        isFromSkillManager = mActivity.getIntent().getBooleanExtra("isFromSkillManager", false);
        if (serviceDetailBean != null) {//表示是修改服务，首先需要把服务的数据填充
            if (!isFromSkillManager) {
                mActivityPublishServiceAddinfoBinding.tvPublishServiceText.setText("修改服务");
                mActivityPublishServiceAddinfoBinding.btnPublishText.setText("修改");
                mActivityPublishServiceAddinfoBinding.tvPublishSuccessText.setText("修改成功");
            }
            loadOriginServiceData();
        }
    }

    private void initView() {
        mNpChoosePriceUnit = mActivityPublishServiceAddinfoBinding.npChoosePriceUnit;

        mActivityPublishServiceAddinfoBinding.svPublishServiceLabels.setVerticalScrollBarEnabled(false);

        mSallSkillLabels.setActivity(mActivity);
        mSallSkillLabels.initSkillLabels();
    }

    /**
     * 修改服务，首先回填服务数据
     */
    private void loadOriginServiceData() {
        ServiceDetailBean.Service service = serviceDetailBean.data.service;
        //填报价
        mActivityPublishServiceAddinfoBinding.etServiceQuote.setText(service.quote + "");
        //报价单位
        quoteunit = service.quoteunit;
        mChoosePriceUnit = optionalPriceUnit[service.quoteunit - 1];
//        if (quoteunit < 9) {
//            setPriceUnit("元/" + mChoosePriceUnit);
//        } else {
//            setPriceUnit("元");
//        }
        setPriceUnit("元");
        mActivityPublishServiceAddinfoBinding.tvChooseQuoteunit.setText(mChoosePriceUnit);
        //分期
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentHandle.getLayoutParams();
        if (service.instalment == 0) {//分期关闭
            isInstalment = false;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else if (service.instalment == 1) {//分期开启
            isInstalment = true;
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentHandle.setLayoutParams(layoutParams);
        //线上、线下
        if (service.pattern == 0) {//线上
            checkOnline(null);
        } else if (service.pattern == 1) {//线下
            checkOffline(null);
        }
        //线下地址
        mActivityPublishServiceAddinfoBinding.etPublishServiceAddress.setText(service.place);
        lng = service.lng;
        lat = service.lat;
        //纠纷处理方式
        if (service.bp == 0) {
            checkPlatformProcessing(null);
        } else {
            checkConsultProcessing(null);
        }
        //技能标签
        String[] tags = service.tag.split(",");
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
    }

    public void gotoBack(View v) {
        mActivity.finish();
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
        mSallSkillLabels.initSkillLabels();
    }

    //打开或者关闭分期
    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentHandle.getLayoutParams();
        if (isInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
        }
        mActivityPublishServiceAddinfoBinding.ivPublishServiceInstalmentHandle.setLayoutParams(layoutParams);
        isInstalment = !isInstalment;
    }

    public void checkOnline(View v) {
        isOnline = true;
        setOfflineItemVisibility(View.GONE);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOnlineIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOfflineIcon.setImageResource(R.mipmap.default_btn);
    }

    public void checkOffline(View v) {
        isOnline = false;
        setOfflineItemVisibility(View.VISIBLE);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOnlineIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishServiceAddinfoBinding.ivPublishServiceOfflineIcon.setImageResource(R.mipmap.pitchon_btn);
    }

    /**
     * 选择平台处理方式
     *
     * @param v
     */
    public void checkPlatformProcessing(View v) {
        mActivityPublishServiceAddinfoBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityPublishServiceAddinfoBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.default_btn);
        checkedDisputeHandingTypeIndex = 0;
    }

    /**
     * 选择协商处理方式
     *
     * @param v
     */
    public void checkConsultProcessing(View v) {
        mActivityPublishServiceAddinfoBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.default_btn);
        mActivityPublishServiceAddinfoBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        checkedDisputeHandingTypeIndex = 1;
    }

    boolean isClickPublish = false;

    public void publish(View v) {
        //发布成功以后才能跳转到成功页面,这里只是为了方便测试直接跳转
//        Intent intentPublishServiceSuccessActivity = new Intent(CommonUtils.getContext(), PublishServiceSucceddActivity.class);
//        intentPublishServiceSuccessActivity.putExtra("serviceId", 88l);
//        mActivity.startActivity(intentPublishServiceSuccessActivity);
//        mActivity.finish();
//        if (PublishServiceBaseInfoActivity.activity != null) {
//            PublishServiceBaseInfoActivity.activity.finish();
//            PublishServiceBaseInfoActivity.activity = null;
//        }

        if (isClickPublish) {
            return;
        }
        isClickPublish = true;

        if (!isCheckedSlashProtocol) {
            ToastUtils.shortToast("请查阅零佣金活动，并勾选");
            isClickPublish = false;
            return;
        }

        Bundle bundleServiceData = mActivity.getIntent().getExtras();
        String title = bundleServiceData.getString("title");
        String desc = bundleServiceData.getString("desc");
        int anonymity = bundleServiceData.getInt("anonymity");
        int timetype = bundleServiceData.getInt("timetype");
        long starttime = bundleServiceData.getLong("starttime");
        long endtime = bundleServiceData.getLong("endtime");
        ArrayList<String> listPic = bundleServiceData.getStringArrayList("pic");


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
        double quote = 0;
        String quoteStr = mActivityPublishServiceAddinfoBinding.etServiceQuote.getText().toString();
        if (TextUtils.isEmpty(quoteStr)) {
            ToastUtils.shortToast("请填写报价");
            isClickPublish = false;
            return;
        } else {
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
        if (quoteunit == -1) {
            ToastUtils.shortToast("请选择价格单位");
            isClickPublish = false;
            return;
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

        if (serviceDetailBean != null && isFromSkillManager == false) {//修改服务
            ServiceEngine.updateService(new BaseProtocol.IResultExecutor<CommonResultBean>() {
                @Override
                public void execute(CommonResultBean dataBean) {
                    isClickPublish = false;
                    //这里是修改服务成功后跳转
                    Intent intentPublishServiceSuccessActivity = new Intent(CommonUtils.getContext(), PublishServiceSucceddActivity.class);
                    intentPublishServiceSuccessActivity.putExtra("serviceId", serviceDetailBean.data.service.id);
                    intentPublishServiceSuccessActivity.putExtra("isUpdate", true);
                    mActivity.startActivity(intentPublishServiceSuccessActivity);
                    mActivity.finish();
                    if (PublishServiceBaseInfoActivity.activity != null) {
                        PublishServiceBaseInfoActivity.activity.finish();
                        PublishServiceBaseInfoActivity.activity = null;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    isClickPublish = false;
                    ToastUtils.shortToast("修改失败：" + result);
                }
            }, serviceDetailBean.data.service.id + "", title, addedSkillLabels, starttime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        } else {//发布服务
            ServiceEngine.publishService(new BaseProtocol.IResultExecutor<PublishServiceResultBean>() {
                @Override
                public void execute(PublishServiceResultBean dataBean) {
                    isClickPublish = false;
                    LogKit.v("发布成功，id:" + dataBean.data.id);
                    //发布成功以后才能跳转到成功页面
                    Intent intentPublishServiceSuccessActivity = new Intent(CommonUtils.getContext(), PublishServiceSucceddActivity.class);
                    intentPublishServiceSuccessActivity.putExtra("serviceId", dataBean.data.id);
                    mActivity.startActivity(intentPublishServiceSuccessActivity);
                    mActivity.finish();
                    if (PublishServiceBaseInfoActivity.activity != null) {
                        PublishServiceBaseInfoActivity.activity.finish();
                        PublishServiceBaseInfoActivity.activity = null;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    isClickPublish = false;
                    ToastUtils.shortToast("发布服务失败：" + result);
                }
            }, title, addedSkillLabels, starttime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        }
    }

    public void openMapActivity(View v) {
        Intent intentMapActivity = new Intent(CommonUtils.getContext(), MapActivity.class);
        mActivity.startActivityForResult(intentMapActivity, 20);
    }

    public void setLocationInfo(String address, String name, double lng, double lat) {
        if (!TextUtils.isEmpty(name)) {
            mActivityPublishServiceAddinfoBinding.etPublishServiceAddress.setText(name);
        } else {
            mActivityPublishServiceAddinfoBinding.etPublishServiceAddress.setText(address);
        }
        this.lng = lng;
        this.lat = lat;
    }

    public String getLocationAddress() {
        return mActivityPublishServiceAddinfoBinding.etPublishServiceAddress.getText().toString();
    }

    public void openChoosePriceUnit(View v) {
        setChoosePriceUnitLayerVisibility(View.VISIBLE);
        mNpChoosePriceUnit.setDisplayedValues(optionalPriceUnit);
        mNpChoosePriceUnit.setMaxValue(optionalPriceUnit.length - 1);
        mNpChoosePriceUnit.setMinValue(0);
        mNpChoosePriceUnit.setValue(1);
    }

    public void okChoosePriceUnit(View v) {
        setChoosePriceUnitLayerVisibility(View.INVISIBLE);
        int value = mNpChoosePriceUnit.getValue();
        mChoosePriceUnit = optionalPriceUnit[value];
//        if (value < 8) {
//            setPriceUnit("元/" + mChoosePriceUnit);
//        } else {
//            setPriceUnit("元");
//        }
        setPriceUnit("元");
        quoteunit = value + 1;
        mActivityPublishServiceAddinfoBinding.tvChooseQuoteunit.setText(mChoosePriceUnit);
    }

    boolean isCheckedSlashProtocol = true;

    /**
     * @param v
     */
    public void checkSlashProtocol(View v) {
        if (isCheckedSlashProtocol) {
            isCheckedSlashProtocol = false;
            mActivityPublishServiceAddinfoBinding.ivSlashProtocolCheckedIcon.setImageResource(R.mipmap.no_checked_icon);
        } else {
            isCheckedSlashProtocol = true;
            mActivityPublishServiceAddinfoBinding.ivSlashProtocolCheckedIcon.setImageResource(R.mipmap.checked_icon);
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
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.PUBLISH_SERVICE_PUBLISH_OPEN_INSTALL_ACCOUNT_QUESTION);

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
    private int choosePriceUnitLayerVisibility = View.GONE;
    private String priceUnit = "元";

    @Bindable
    public int getOfflineItemVisibility() {
        return offlineItemVisibility;
    }

    public void setOfflineItemVisibility(int offlineItemVisibility) {
        this.offlineItemVisibility = offlineItemVisibility;
        notifyPropertyChanged(BR.offlineItemVisibility);
    }

    @Bindable
    public int getChoosePriceUnitLayerVisibility() {
        return choosePriceUnitLayerVisibility;
    }

    public void setChoosePriceUnitLayerVisibility(int choosePriceUnitLayerVisibility) {
        this.choosePriceUnitLayerVisibility = choosePriceUnitLayerVisibility;
        notifyPropertyChanged(BR.choosePriceUnitLayerVisibility);
    }

    @Bindable
    public String getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(String priceUnit) {
        this.priceUnit = priceUnit;
        notifyPropertyChanged(BR.priceUnit);
    }
}
