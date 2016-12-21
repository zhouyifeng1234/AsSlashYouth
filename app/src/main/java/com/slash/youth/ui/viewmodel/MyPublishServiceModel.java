package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyPublishServiceBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.domain.MyTaskItemBean;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.ServiceFlowComplainResultBean;
import com.slash.youth.domain.ServiceInstalmentListBean;
import com.slash.youth.domain.ServiceOrderInfoBean;
import com.slash.youth.domain.UserInfoBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.engine.ServiceEngine;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.view.RefreshScrollView;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/12/6.
 */
public class MyPublishServiceModel extends BaseObservable {

    ActivityMyPublishServiceBinding mActivityMyPublishServiceBinding;
    Activity mActivity;
    private SlashDateTimePicker mChooseDateTimePicker;

    private long tid;
    private long soid;
    private long suid;
    private int fid;//当前是第几期
    private double orderQuote = -1;//必须是服务订单信息接口返回的报价才是准确的
    private int quoteunit = -1;
    private long duid;//服务订单中的需求方ID
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
    private boolean isUpdateInstalment = true;//修改条件按钮中的分期是否开启，默认为true开启

    public MyPublishServiceModel(ActivityMyPublishServiceBinding activityMyPublishServiceBinding, Activity activity) {
        this.mActivity = activity;
        this.mActivityMyPublishServiceBinding = activityMyPublishServiceBinding;
        displayLoadLayer();
        initData();
        initView();
        initListener();
    }

    MyTaskBean myTaskBean;

    private void initData() {
        myTaskBean = (MyTaskBean) mActivity.getIntent().getSerializableExtra("myTaskBean");
        tid = myTaskBean.tid;//tid就是soid
        soid = tid;//tid（任务id）就是soid(服务订单id)
        fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0

        getDataFromServer();
    }

    private int loadDataTimes = 0;//getTaskItemData、getServiceDetailFromServer、getServiceOrderInfoData、getDemandUserInfo、getServiceUserInfo五次都加载完毕，则数据加载完毕

    private void getDataFromServer() {
        getTaskItemData();
        getServiceDetailFromServer();//通过tid获取服务详情信息
        getServiceOrderInfoData();//根据soid(即tid)获取服务订单状态信息
    }

    private void initView() {
        mChooseDateTimePicker = mActivityMyPublishServiceBinding.sdtpPublishServiceChooseDatetime;
    }

    private void initListener() {
        mActivityMyPublishServiceBinding.scRefresh.setRefreshTask(new RefreshScrollView.IRefreshTask() {
            @Override
            public void refresh() {
                displayLoadLayer();
                getDataFromServer();
            }
        });
        mActivityMyPublishServiceBinding.etUpdateInstalmentRatio1.addTextChangedListener(new InstalmentRatioTextChangeListener(1));
        mActivityMyPublishServiceBinding.etUpdateInstalmentRatio2.addTextChangedListener(new InstalmentRatioTextChangeListener(2));
        mActivityMyPublishServiceBinding.etUpdateInstalmentRatio3.addTextChangedListener(new InstalmentRatioTextChangeListener(3));
        mActivityMyPublishServiceBinding.etUpdateInstalmentRatio4.addTextChangedListener(new InstalmentRatioTextChangeListener(4));
        mActivityMyPublishServiceBinding.etServiceUpdateQuote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String updateQuoteStr = s.toString();
                String ratioStr1 = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio1.getText().toString();
                String ratioStr2 = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio2.getText().toString();
                String ratioStr3 = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio3.getText().toString();
                String ratioStr4 = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio4.getText().toString();
                try {
                    double updateQuote = Double.parseDouble(updateQuoteStr);
                    double ratio1 = Double.parseDouble(ratioStr1);
                    int ratioQuote1 = (int) (updateQuote * ratio1 / 100);
                    mActivityMyPublishServiceBinding.tvInstalment1Amount.setText("￥" + ratioQuote1);
                    double ratio2 = Double.parseDouble(ratioStr2);
                    int ratioQuote2 = (int) (updateQuote * ratio2 / 100);
                    mActivityMyPublishServiceBinding.tvInstalment2Amount.setText("￥" + ratioQuote2);
                    double ratio3 = Double.parseDouble(ratioStr3);
                    int ratioQuote3 = (int) (updateQuote * ratio3 / 100);
                    mActivityMyPublishServiceBinding.tvInstalment3Amount.setText("￥" + ratioQuote3);
                    double ratio4 = Double.parseDouble(ratioStr4);
                    int ratioQuote4 = (int) (updateQuote * ratio4 / 100);
                    mActivityMyPublishServiceBinding.tvInstalment4Amount.setText("￥" + ratioQuote4);
                } catch (Exception ex) {
//                    ToastUtils.shortToast("填写数据有误");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private class InstalmentRatioTextChangeListener implements TextWatcher {

        private int instalmentNo;

        public InstalmentRatioTextChangeListener(int instalmentNo) {
            this.instalmentNo = instalmentNo;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String ratioStr = s.toString();
            String updateQuoteStr = mActivityMyPublishServiceBinding.etServiceUpdateQuote.getText().toString();
            String ratioQuoteStr = "";
            try {
                double ratio = Double.parseDouble(ratioStr);
                double updateQuote = Double.parseDouble(updateQuoteStr);
                int ratioQuote = (int) (updateQuote * ratio / 100);
                ratioQuoteStr = "￥" + ratioQuote;
            } catch (Exception ex) {

            }
            switch (instalmentNo) {
                case 1:
                    mActivityMyPublishServiceBinding.tvInstalment1Amount.setText(ratioQuoteStr);
                    break;
                case 2:
                    mActivityMyPublishServiceBinding.tvInstalment2Amount.setText(ratioQuoteStr);
                    break;
                case 3:
                    mActivityMyPublishServiceBinding.tvInstalment3Amount.setText(ratioQuoteStr);
                    break;
                case 4:
                    mActivityMyPublishServiceBinding.tvInstalment4Amount.setText(ratioQuoteStr);
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    /**
     * 刚进入页面时，显示加载层
     */
    private void displayLoadLayer() {
        setLoadLayerVisibility(View.VISIBLE);
    }

    /**
     * 数据加载完毕后,隐藏加载层
     */
    private void hideLoadLayer() {
        setLoadLayerVisibility(View.GONE);
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void gotoUserInfoPage(View v) {
        Intent intentUserInfoActivity = new Intent(CommonUtils.getContext(), UserInfoActivity.class);
        switch (v.getId()) {
            case R.id.ll_demand_userinfo:
                //获取需求方uid
                intentUserInfoActivity.putExtra("Uid", duid);
                break;
            case R.id.ll_service_userinfo:
                //我发的服务，我就是服务方，所以这里不需要传uid
                break;
        }
        mActivity.startActivity(intentUserInfoActivity);
    }

    /**
     * 打开聊天界面
     *
     * @param v
     */
    public void havaAChat(View v) {

    }

    /**
     * 服务方申诉（不同意退款）按钮
     *
     * @param v
     */
    public void complain(View v) {
        ServiceEngine.serviceComplain(new BaseProtocol.IResultExecutor<ServiceFlowComplainResultBean>() {
            @Override
            public void execute(ServiceFlowComplainResultBean dataBean) {
                ToastUtils.shortToast("申诉完成");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("申诉失败：" + result);
            }
        }, soid + "", "");
    }

    /**
     * 服务方同意退款
     *
     * @param v
     */
    public void agreeRefund(View v) {
        ServiceEngine.serviceAgreeRefund(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("同意退款成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("同意退款失败：" + result);
            }
        }, soid + "");
    }

    /**
     * 服务方完成任务
     *
     * @param v
     */
    public void complete(View v) {
        ServiceEngine.complete(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("完成任务成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("完成任务失败:" + result);
            }
        }, soid + "", fid + "");
    }

    /**
     * 服务方拒绝需求方
     *
     * @param v
     */
    public void noAccept(View v) {
        ServiceEngine.noAccept(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("拒绝成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("拒绝失败:" + result);
            }
        }, soid + "", duid + "");
    }

    /**
     * 服务方接受需求方,效果和修改条件中的抢单按钮是一样的
     *
     * @param v
     */
    public void accept(View v) {
        selectedDemandUser();
    }

    /**
     * 修改条件 中的 抢单按钮， 效果和接受是一样的
     */
    public void bidDemand(View v) {
        selectedDemandUser();
    }

    /**
     * 服务方选定需求方
     */
    private void selectedDemandUser() {
        //一开始没有分期比例，所以修改条件中的分期比例不需要回填
        double updateQuote;
        String updateQuoteStr = mActivityMyPublishServiceBinding.etServiceUpdateQuote.getText().toString();
        try {
            updateQuote = Double.parseDouble(updateQuoteStr);
            if (updateQuote <= 0) {
                ToastUtils.shortToast("报价必须大于0元");
                return;
            }
        } catch (Exception ex) {
            ToastUtils.shortToast("请正确填写报价");
            return;
        }

        if (starttime <= 0 || endtime <= 0) {
            ToastUtils.shortToast("请先完善开始时间和结束时间");
            return;
        }
        if (starttime <= System.currentTimeMillis()) {
            ToastUtils.shortToast("开始时间必须大于当前时间");
            return;
        }
        if (endtime <= starttime) {
            ToastUtils.shortToast("结束时间必须大于开始时间");
            return;
        }

        getInputInstalmentRatio();
        if (isUpdateInstalment == false) {
            updateInstalmentRatioList.clear();
            updateInstalmentRatioList.add(1d);
        } else if (updateInstalmentRatioList.size() <= 0) {
            ToastUtils.shortToast("请先完善分期比例信息");
            return;
        } else {
            double totalRatio = 0;
            for (double ratio : updateInstalmentRatioList) {
                totalRatio += ratio;
            }
            if (totalRatio < 100) {
                ToastUtils.shortToast("分期比例总和必须是100%");
                return;
            }
        }
        ServiceEngine.selected(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("服务方选定成功");
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("服务方选定失败:" + result);
            }
        }, soid + "", duid + "", updateQuote + "", starttime + "", endtime + "", updateInstalmentRatioList, updateBp + "");
    }

    private void getInputInstalmentRatio() {
        String ratioStr;
        if (instalmentCount >= 1) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio1.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第一期分期比率");
                return;
            }
            updateInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 2) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio2.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第二期分期比率");
                return;
            }
            updateInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 3) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio3.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第三期分期比率");
                return;
            }
            updateInstalmentRatioList.add(ratio);
        }
        if (instalmentCount >= 4) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio4.getText().toString();
            double ratio = convertStrToRatio(ratioStr);
            if (ratio == -1) {
                ToastUtils.shortToast("请正确填写第四期分期比率");
                return;
            }
            updateInstalmentRatioList.add(ratio);
        }
    }

    /**
     * @param ratioStr
     * @return
     */
    private double convertStrToRatio(String ratioStr) {
        double ratio;
        if (ratioStr.endsWith("%")) {
            ratioStr = ratioStr.substring(0, ratioStr.length() - 1);
        }
        try {
            ratio = Double.parseDouble(ratioStr);
            if (ratio <= 0) {
                return -1;
            }
        } catch (Exception ex) {
            return -1;
        }
        return ratio;
    }

    /**
     * 服务方修改条件
     *
     * @param v
     */
    public void updateCondition(View v) {
        setUpdateLayerVisibility(View.VISIBLE);
    }

    ArrayList<Double> updateInstalmentRatioList = new ArrayList<Double>();

    private boolean mIsChooseStartTime;

    /**
     * @param v
     */
    public void openUpdateTaskTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.VISIBLE);
    }

    public void closeUpdateTaskTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
    }

    public void okChooseIdleStartTimeAndEndTime(View v) {
        if (TextUtils.isEmpty(starttimeStr) || TextUtils.isEmpty(endtimeStr)) {
            ToastUtils.shortToast("请填写开始时间和结束时间");
            return;
        }
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
        mActivityMyPublishServiceBinding.tvServiceStartEndTime.setText(starttimeStr + "-" + endtimeStr);
    }

    public void closeStartTimeAndEndTimeLayer(View v) {
        setSetStartTimeAndEndTimeLayerVisibility(View.GONE);
    }

    public void chooseStartTime(View v) {
        mIsChooseStartTime = true;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void chooseEndTime(View v) {
        mIsChooseStartTime = false;
        setChooseDateTimeLayerVisibility(View.VISIBLE);
    }

    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
    }

    private int mCurrentChooseMonth;
    private int mCurrentChooseDay;
    private int mCurrentChooseHour;
    private int mCurrentChooseMinute;
    long starttime;
    long endtime;
    String starttimeStr;
    String endtimeStr;

    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.GONE);
        mCurrentChooseMonth = mChooseDateTimePicker.getCurrentChooseMonth();
        mCurrentChooseDay = mChooseDateTimePicker.getCurrentChooseDay();
        mCurrentChooseHour = mChooseDateTimePicker.getCurrentChooseHour();
        mCurrentChooseMinute = mChooseDateTimePicker.getCurrentChooseMinute();
        String dateTimeStr = mCurrentChooseMonth + "月" + mCurrentChooseDay + "日" + " " + mCurrentChooseHour + ":" + (mCurrentChooseMinute < 10 ? "0" + mCurrentChooseMinute : mCurrentChooseMinute);
        if (mIsChooseStartTime) {
            mActivityMyPublishServiceBinding.tvStartTime.setText(dateTimeStr);
            starttime = convertTimeToMillis();
            starttimeStr = dateTimeStr;
        } else {
            mActivityMyPublishServiceBinding.tvEndTime.setText(dateTimeStr);
            endtime = convertTimeToMillis();
            endtimeStr = dateTimeStr;
        }

    }

    public long convertTimeToMillis() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MONTH, mCurrentChooseMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, mCurrentChooseDay);
        calendar.set(Calendar.HOUR_OF_DAY, mCurrentChooseHour);
        calendar.set(Calendar.MINUTE, mCurrentChooseMinute);
        return calendar.getTimeInMillis();
    }

    private int updateBp = 1;

    /**
     * 选择平台方式
     *
     * @param v
     */
    public void checkPlatformProcessing(View v) {
        mActivityMyPublishServiceBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        mActivityMyPublishServiceBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.default_btn);
        updateBp = 1;
    }

    /**
     * 选择协商方式
     *
     * @param v
     */
    public void checkConsultProcessing(View v) {
        mActivityMyPublishServiceBinding.ivPlatformProcessingIcon.setImageResource(R.mipmap.default_btn);
        mActivityMyPublishServiceBinding.ivConsultProcessingIcon.setImageResource(R.mipmap.pitchon_btn);
        updateBp = 2;
    }

    /**
     * 修改条件 按钮中的 开启或者关闭分期
     *
     * @param v
     */
    public void toggleInstalment(View v) {
        RelativeLayout.LayoutParams layoutParams
                = (RelativeLayout.LayoutParams) mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentHandle.getLayoutParams();
        if (isUpdateInstalment) {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle_weijihuo);
            //关闭分期 隐藏分期比率
            hideInstalmentRatio();
        } else {
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentBg.setImageResource(R.mipmap.background_safebox_toggle);
            //开启分期 显示已输入的分期比率
            displayInstalmentRatio();
        }
        mActivityMyPublishServiceBinding.ivMyPublishServiceInstalmentHandle.setLayoutParams(layoutParams);
        isUpdateInstalment = !isUpdateInstalment;
    }

    /**
     * 关闭分期时，隐藏分期比例
     */
    private void hideInstalmentRatio() {
        setInstalmentRatioVisibility(View.GONE);
    }

    /**
     * 开启分期时，显示已填写的分期比例
     */
    private void displayInstalmentRatio() {
        setInstalmentRatioVisibility(View.VISIBLE);
    }

    private int instalmentCount = 0;

    /**
     * 删除分期
     *
     * @param v
     */
    public void deleteInstalment(View v) {
        if (isUpdateInstalment) {
            if (instalmentCount > 0) {
                instalmentCount--;
                if (instalmentCount == 0) {
                    setUpdateInstalmentLine1Visibility(View.GONE);
                } else if (instalmentCount == 1) {
                    setUpdateInstalmentLine2Visibility(View.GONE);
                } else if (instalmentCount == 2) {
                    setUpdateInstalmentLine3Visibility(View.GONE);
                } else {//instalmentCount=3
                    setUpdateInstalmentLine4Visibility(View.GONE);
                }
            }
        }
    }

    /**
     * 添加分期
     *
     * @param v
     */
    public void addInstalment(View v) {
        if (isUpdateInstalment) {
            if (instalmentCount < 4) {
                boolean isAddable = checkIsAddedable();
                if (isAddable) {
                    instalmentCount++;
                    if (instalmentCount == 1) {
                        setUpdateInstalmentLine1Visibility(View.VISIBLE);
                    } else if (instalmentCount == 2) {
                        setUpdateInstalmentLine2Visibility(View.VISIBLE);
                    } else if (instalmentCount == 3) {
                        setUpdateInstalmentLine3Visibility(View.VISIBLE);
                    } else {//instalmentCount=4
                        setUpdateInstalmentLine4Visibility(View.VISIBLE);
                    }
                } else {
                    ToastUtils.shortToast("请正确填写分期比率");
                }
            }
        }
    }

    /**
     * 判断是否可以添加下一期，如果已经填写了分期比率，就可以添加，如果未填写，或者填写有误，就不能添加下一期
     */
    private boolean checkIsAddedable() {
        String ratioStr;
        if (instalmentCount == 0) {
            return true;
        } else if (instalmentCount == 1) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio1.getText().toString();
        } else if (instalmentCount == 2) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio2.getText().toString();
        } else if (instalmentCount == 3) {
            ratioStr = mActivityMyPublishServiceBinding.etUpdateInstalmentRatio3.getText().toString();
        } else {
            return false;
        }
        if (ratioStr.endsWith("%")) {
            ratioStr = ratioStr.substring(0, ratioStr.length() - 1);
        }
        try {
            double ratio = Double.parseDouble(ratioStr);
            if (ratio <= 0) {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    /**
     * 获取对应的单个任务条目信息（从任务列表穿过的myTaskBean可能不是最新的数据）
     */
    private void getTaskItemData() {
        MyTaskEngine.getMyTaskItem(new BaseProtocol.IResultExecutor<MyTaskItemBean>() {
            @Override
            public void execute(MyTaskItemBean dataBean) {
                myTaskBean = dataBean.data.taskinfo;
                tid = myTaskBean.tid;//tid就是soid
                soid = tid;//tid（任务id）就是soid(服务订单id)
                fid = myTaskBean.instalmentcurr;//通过调试接口发现，这个字段当type=2为服务的时候，好像不准，一直都是0

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "2", "1");
    }

    /**
     * 通过tid获取服务详情信息
     */
    private void getServiceDetailFromServer() {
        MyTaskEngine.getServiceDetailByTid(new BaseProtocol.IResultExecutor<ServiceDetailBean>() {
            @Override
            public void execute(ServiceDetailBean dataBean) {
                ServiceDetailBean.Service service = dataBean.data.service;
                //服务标题，布局文件中有两个地方需要设置
                setServiceTitle(service.title);
                //闲置时间
                int timetype = service.timetype;
                if (timetype == 0) {
                    SimpleDateFormat sdfIdleTime = new SimpleDateFormat("MM月dd日 hh:mm");
                    String starttimeStr = sdfIdleTime.format(service.starttime);
                    String endtimeStr = sdfIdleTime.format(service.endtime);
                    setIdleTime("闲置时间:" + starttimeStr + "-" + endtimeStr);
                    //回填修改条件中的开始时间和结束时间
                    starttime = service.starttime;
                    endtime = service.endtime;
                    mActivityMyPublishServiceBinding.tvServiceStartEndTime.setText(starttimeStr + "-" + endtimeStr);
                } else if (timetype == 1) {
                    setIdleTime("闲置时间:下班后");
                    //回填修改条件中的开始时间和结束时间
                    starttime = 0;
                    endtime = 0;
                } else if (timetype == 2) {
                    setIdleTime("闲置时间:周末");
                    //回填修改条件中的开始时间和结束时间
                    starttime = 0;
                    endtime = 0;
                } else if (timetype == 3) {
                    setIdleTime("闲置时间:下班后及周末");
                    //回填修改条件中的开始时间和结束时间
                    starttime = 0;
                    endtime = 0;
                } else if (timetype == 4) {
                    setIdleTime("闲置时间:随时");
                    //回填修改条件中的开始时间和结束时间
                    starttime = 0;
                    endtime = 0;
                }

                //报价 这里不能使用服务详情接口返回的报价
                quoteunit = service.quoteunit;
//                CommonUtils.getHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
                if (orderQuote != -1) {
                    if (quoteunit == 9) {
                        setQuote(orderQuote + "元");
                    } else if (quoteunit > 0 && quoteunit < 9) {
                        setQuote(orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                    }
                }
//                    }
//                });
                //分期
                //这里不能用service详情的instalment，要用任务列表item的instalment
                //但是 目前任务列表item中的分期信息（分期比例）也不对，"instalmentcurr": 0, "instalmentcurrfinish": 0, "instalmentratio": "",
                if (myTaskBean.instalment == 1) {//开启分期
                    setInstalmentVisibility(View.VISIBLE);
                    String instalmentRatioStr = "";
                    String[] ratios = myTaskBean.instalmentratio.split(",");
                    for (int i = 0; i < ratios.length; i++) {
                        String ratio = ratios[i];
                        if (TextUtils.isEmpty(ratio)) {
                            continue;
                        }
                        double ratioDouble = Double.parseDouble(ratio);

                        if (i < ratios.length - 1) {
                            instalmentRatioStr += ratioDouble * 100 + "%/";
                        } else {
                            instalmentRatioStr += ratioDouble * 100 + "%";
                        }
                    }
                    setInstalmentRatio(instalmentRatioStr);
                    //回填修改条件中是否开始分期
                    isUpdateInstalment = false;//这里需要设置true，设置为false，调用toggleInstalment方法后就变为true了
                    toggleInstalment(null);
                } else {//未开启分期
                    setInstalmentVisibility(View.INVISIBLE);
                    //回填修改条件中是否开始分期
                    isUpdateInstalment = true;//这里需要设置false，设置为true，调用toggleInstalment方法后就变为false了
                    toggleInstalment(null);
                }
                //纠纷处理方式（似乎协商处理就显示）
                if (service.bp == 2) {//协商
                    setBpConsultVisibility(View.VISIBLE);
                    //回填修改条件中的纠纷处理方式
                    checkConsultProcessing(null);
                } else if (service.bp == 1) {//平台
                    setBpConsultVisibility(View.INVISIBLE);
                    //回填修改条件中的纠纷处理方式
                    checkPlatformProcessing(null);
                }

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {

            }
        }, tid + "", "1");
    }


    /**
     * 根据soid(即tid)获取服务订单状态信息
     */
    private void getServiceOrderInfoData() {

        ServiceEngine.getServiceOrderInfo(new BaseProtocol.IResultExecutor<ServiceOrderInfoBean>() {
            @Override
            public void execute(ServiceOrderInfoBean dataBean) {
                orderQuote = dataBean.data.order.quote;
                //回填修改条件中的报价
                mActivityMyPublishServiceBinding.etServiceUpdateQuote.setText(orderQuote + "");
//                CommonUtils.getHandler().post(new Runnable() {
//                    @Override
//                    public void run() {
                if (quoteunit != -1) {
                    if (quoteunit == 9) {
                        setQuote(orderQuote + "元");
                    } else if (quoteunit > 0 && quoteunit < 9) {
                        setQuote(orderQuote + "元/" + optionalPriceUnit[quoteunit - 1]);
                    }
                }
//                    }
//                });

                int status = dataBean.data.order.status;
                displayStatusCycle(status);
                displayStatusButton(dataBean);//显示对应不同状态的操作按钮
                suid = dataBean.data.order.suid;
                duid = dataBean.data.order.uid;
                getDemandUserInfo();
                getServiceUserInfo();

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {

            }
        }, soid + "");
    }

    /**
     * 获取需求者信息
     */
    private void getDemandUserInfo() {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                BitmapKit.bindImage(mActivityMyPublishServiceBinding.ivDemandUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar);
                if (uinfo.isauth == 0) {//未认证
                    setDemandUserIsAuthVisibility(View.GONE);
                } else {
                    setDemandUserIsAuthVisibility(View.VISIBLE);
                }
                setDemandUsername("需求方:" + uinfo.name);

                LogKit.v("需求方信息 uinfo.id:" + uinfo.id);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取需求方信息失败:" + result);
            }
        }, duid + "", "0");
    }

    /**
     * 获取服务者信息
     */
    private void getServiceUserInfo() {
        UserInfoEngine.getOtherUserInfo(new BaseProtocol.IResultExecutor<UserInfoBean>() {
            @Override
            public void execute(UserInfoBean dataBean) {
                UserInfoBean.UInfo uinfo = dataBean.data.uinfo;
                BitmapKit.bindImage(mActivityMyPublishServiceBinding.ivServiceUserAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + uinfo.avatar);
                if (uinfo.isauth == 0) {//未认证
                    setServiceUserIsAuthVisibility(View.GONE);
                } else {
                    setServiceUserIsAuthVisibility(View.VISIBLE);
                }
                setServiceUsername("服务方:" + uinfo.name);

                LogKit.v("服务方信息 uinfo.id:" + uinfo.id);

                loadDataTimes++;
                if (loadDataTimes >= 5) {
                    hideLoadLayer();
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取服务方信息失败:" + result);
            }
        }, suid + "", "0");
    }

    ArrayList<ServiceInstalmentListBean.InstalmentInfo> instalmentInfoList;

    private void displayStatusButton(ServiceOrderInfoBean dataBean) {
        ServiceOrderInfoBean.Order order = dataBean.data.order;

        switch (order.status) {
            case 1:/*初始化订单*/
                setAcceptItemVisibility(View.VISIBLE);
                break;
            case 5:/*订单进行中*/
                //获取分期情况 来判断是否需要显示完成按钮
                ServiceEngine.getServiceInstalmentList(new BaseProtocol.IResultExecutor<ServiceInstalmentListBean>() {
                    @Override
                    public void execute(ServiceInstalmentListBean dataBean) {
                        instalmentInfoList = dataBean.data.list;
                        int totalInstalment = instalmentInfoList.size();//总的分期数
                        for (int i = 0; i < totalInstalment; i++) {
                            ServiceInstalmentListBean.InstalmentInfo instalmentInfo = instalmentInfoList.get(i);
                            if (instalmentInfo.status != 2) {
                                fid = instalmentInfo.id;
                                if (instalmentInfo.status == 0) {
                                    setFinishItemVisibility(View.VISIBLE);
                                }
                                break;
                            }
                        }
                    }

                    @Override
                    public void executeResultError(String result) {
                        ToastUtils.shortToast("获取服务订单的分期信息失败");
                    }
                }, soid + "");
                break;
            case 8:/*申请退款*/
                setRefundItemVisibility(View.VISIBLE);
                break;
            case 7:/*订单确认完成*/
            case 2:/*服务者确认*/
            case 3:/*需求方支付中*/
            case 6:/*订单完成*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                setFinishItemVisibility(View.GONE);
                setAcceptItemVisibility(View.GONE);
                setRefundItemVisibility(View.GONE);
                break;
        }
    }

    private void displayStatusCycle(int status) {
        switch (status) {
            case 1:/*初始化订单*/
            case 2:/*服务者确认*/
                //预约中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 3:/*需求方支付中*/
                //预支付 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 5:/*订单进行中*/
            case 6:/*订单完成*/
            case 8:/*申请退款*/
            case 9:/*同意退款*/
            case 10:/*平台申诉处理*/
                //服务中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
            case 7:/*订单确认完成*/
                //评价中 大状态
                setStatusProgress(R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4, R.mipmap.flowpoint_act, 0xff31C5E4);
                break;
            case 4:/*订单已经取消*/
            case 11:/*服务方拒绝*/
            default:
                //失效 过期 状态 四个圈全都是灰色
                setStatusProgress(R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC, R.mipmap.flowpoint_nor, 0xffCCCCCC);
                break;
        }
    }

    /**
     * 设置 4个圈 表示的大状态进度
     */
    private void setStatusProgress(int bigStateReservationBg, int bigStateReservationTextColor, int bigStatePaymentBg, int bigStatePaymentTextColor, int bigStateServiceBg, int bigStateServiceTextColor, int bigStateCommentBg, int bigStateCommentTextColor) {
        mActivityMyPublishServiceBinding.tvServiceReservationing.setBackgroundResource(bigStateReservationBg);
        mActivityMyPublishServiceBinding.tvServiceReservationing.setTextColor(bigStateReservationTextColor);
        mActivityMyPublishServiceBinding.tvServicePayment.setBackgroundResource(bigStatePaymentBg);
        mActivityMyPublishServiceBinding.tvServicePayment.setTextColor(bigStatePaymentTextColor);
        mActivityMyPublishServiceBinding.tvServiceServiceing.setBackgroundResource(bigStateServiceBg);
        mActivityMyPublishServiceBinding.tvServiceServiceing.setTextColor(bigStateServiceTextColor);
        mActivityMyPublishServiceBinding.tvServiceComment.setBackgroundResource(bigStateCommentBg);
        mActivityMyPublishServiceBinding.tvServiceComment.setTextColor(bigStateCommentTextColor);
    }

    private String serviceTitle;
    private String idleTime;
    private String quote;
    private int instalmentVisibility;
    private String instalmentRatio;
    private int bpConsultVisibility;

    private int refundItemVisibility = View.GONE;//申诉、同意退款 条目是否可见
    private int finishItemVisibility = View.GONE;//完成 条目是否可见
    private int acceptItemVisibility = View.GONE;//不接受、接受、修改条件 条目是否可见

    private int updateLayerVisibility = View.GONE;//修改服务订单信息蒙层是否可见
    private int updateInstalmentLine1Visibility = View.GONE;
    private int updateInstalmentLine2Visibility = View.GONE;
    private int updateInstalmentLine3Visibility = View.GONE;
    private int updateInstalmentLine4Visibility = View.GONE;
    private int instalmentRatioVisibility;

    private int setStartTimeAndEndTimeLayerVisibility = View.GONE;
    private int chooseDateTimeLayerVisibility = View.GONE;

    private int demandUserIsAuthVisibility = View.GONE;
    private String demandUsername;
    private int serviceUserIsAuthVisibility = View.GONE;
    private String serviceUsername;

    private int loadLayerVisibility = View.GONE;

    @Bindable
    public int getInstalmentRatioVisibility() {
        return instalmentRatioVisibility;
    }

    public void setInstalmentRatioVisibility(int instalmentRatioVisibility) {
        this.instalmentRatioVisibility = instalmentRatioVisibility;
        notifyPropertyChanged(BR.instalmentRatioVisibility);
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
    public int getDemandUserIsAuthVisibility() {
        return demandUserIsAuthVisibility;
    }

    public void setDemandUserIsAuthVisibility(int demandUserIsAuthVisibility) {
        this.demandUserIsAuthVisibility = demandUserIsAuthVisibility;
        notifyPropertyChanged(BR.demandUserIsAuthVisibility);
    }

    @Bindable
    public String getDemandUsername() {
        return demandUsername;
    }

    public void setDemandUsername(String demandUsername) {
        this.demandUsername = demandUsername;
        notifyPropertyChanged(BR.demandUsername);
    }

    @Bindable
    public int getServiceUserIsAuthVisibility() {
        return serviceUserIsAuthVisibility;
    }

    public void setServiceUserIsAuthVisibility(int serviceUserIsAuthVisibility) {
        this.serviceUserIsAuthVisibility = serviceUserIsAuthVisibility;
        notifyPropertyChanged(BR.serviceUserIsAuthVisibility);
    }

    @Bindable
    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
        notifyPropertyChanged(BR.serviceUsername);
    }

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
    }

    @Bindable
    public int getSetStartTimeAndEndTimeLayerVisibility() {
        return setStartTimeAndEndTimeLayerVisibility;
    }

    public void setSetStartTimeAndEndTimeLayerVisibility(int setStartTimeAndEndTimeLayerVisibility) {
        this.setStartTimeAndEndTimeLayerVisibility = setStartTimeAndEndTimeLayerVisibility;
        notifyPropertyChanged(BR.setStartTimeAndEndTimeLayerVisibility);
    }

    @Bindable
    public int getUpdateInstalmentLine1Visibility() {
        return updateInstalmentLine1Visibility;
    }

    public void setUpdateInstalmentLine1Visibility(int updateInstalmentLine1Visibility) {
        this.updateInstalmentLine1Visibility = updateInstalmentLine1Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine1Visibility);
    }

    @Bindable
    public int getUpdateInstalmentLine2Visibility() {
        return updateInstalmentLine2Visibility;
    }

    public void setUpdateInstalmentLine2Visibility(int updateInstalmentLine2Visibility) {
        this.updateInstalmentLine2Visibility = updateInstalmentLine2Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine2Visibility);
    }

    @Bindable
    public int getUpdateInstalmentLine3Visibility() {
        return updateInstalmentLine3Visibility;
    }

    public void setUpdateInstalmentLine3Visibility(int updateInstalmentLine3Visibility) {
        this.updateInstalmentLine3Visibility = updateInstalmentLine3Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine3Visibility);
    }

    @Bindable
    public int getUpdateInstalmentLine4Visibility() {
        return updateInstalmentLine4Visibility;
    }

    public void setUpdateInstalmentLine4Visibility(int updateInstalmentLine4Visibility) {
        this.updateInstalmentLine4Visibility = updateInstalmentLine4Visibility;
        notifyPropertyChanged(BR.updateInstalmentLine4Visibility);
    }

    @Bindable
    public int getUpdateLayerVisibility() {
        return updateLayerVisibility;
    }

    public void setUpdateLayerVisibility(int updateLayerVisibility) {
        this.updateLayerVisibility = updateLayerVisibility;
        notifyPropertyChanged(BR.updateLayerVisibility);
    }

    @Bindable
    public int getAcceptItemVisibility() {
        return acceptItemVisibility;
    }

    public void setAcceptItemVisibility(int acceptItemVisibility) {
        this.acceptItemVisibility = acceptItemVisibility;
        notifyPropertyChanged(BR.acceptItemVisibility);
    }

    @Bindable
    public int getFinishItemVisibility() {
        return finishItemVisibility;
    }

    public void setFinishItemVisibility(int finishItemVisibility) {
        this.finishItemVisibility = finishItemVisibility;
        notifyPropertyChanged(BR.finishItemVisibility);
    }

    @Bindable
    public int getRefundItemVisibility() {
        return refundItemVisibility;
    }

    public void setRefundItemVisibility(int refundItemVisibility) {
        this.refundItemVisibility = refundItemVisibility;
        notifyPropertyChanged(BR.refundItemVisibility);
    }

    @Bindable
    public String getInstalmentRatio() {
        return instalmentRatio;
    }

    public void setInstalmentRatio(String instalmentRatio) {
        this.instalmentRatio = instalmentRatio;
        notifyPropertyChanged(BR.instalmentRatio);
    }

    @Bindable
    public int getBpConsultVisibility() {
        return bpConsultVisibility;
    }

    public void setBpConsultVisibility(int bpConsultVisibility) {
        this.bpConsultVisibility = bpConsultVisibility;
        notifyPropertyChanged(BR.bpConsultVisibility);
    }

    @Bindable
    public int getInstalmentVisibility() {
        return instalmentVisibility;
    }

    public void setInstalmentVisibility(int instalmentVisibility) {
        this.instalmentVisibility = instalmentVisibility;
        notifyPropertyChanged(BR.instalmentVisibility);
    }

    @Bindable
    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
        notifyPropertyChanged(BR.quote);
    }

    @Bindable
    public String getIdleTime() {
        return idleTime;
    }

    public void setIdleTime(String idleTime) {
        this.idleTime = idleTime;
        notifyPropertyChanged(BR.idleTime);
    }

    @Bindable
    public String getServiceTitle() {
        return serviceTitle;
    }

    public void setServiceTitle(String serviceTitle) {
        this.serviceTitle = serviceTitle;
        notifyPropertyChanged(BR.serviceTitle);
    }
}
