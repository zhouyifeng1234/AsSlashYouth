package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityReportTaskBinding;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2017/3/9.
 */
public class ReportTaskModel extends BaseObservable {

    public static final int FEED_CLAIMS_FOR_UNFIT = 1;
    public static final int FEED_CLAIMS_FOR_FRAUD = 2;
    public static final int FEED_CLAIMS_FOR_HARASS = 3;
    public static final int FEED_CLAIMS_FOR_TORT = 4;
    public static final int FEED_CLAIMS_FOR_OTHER = 5;

    ActivityReportTaskBinding mActivityReportTaskBinding;
    Activity mActivity;
    int currentCheckedReasonType = 1;//默认选中1
    String reasonDetail;//当选择FEED_CLAIMS_FOR_OTHER时为必填， 长度[0,2048]
    long tid;//需要举报的需求或者服务的id
    int type;//需求1 服务2

    public ReportTaskModel(ActivityReportTaskBinding activityReportTaskBinding, Activity activity) {
        this.mActivityReportTaskBinding = activityReportTaskBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        tid = mActivity.getIntent().getLongExtra("tid", -1);
        type = mActivity.getIntent().getIntExtra("type", -1);
        if (type == 1) {//需求
            setReportTitle("举报该需求");
            setReportText("请选择举报该需求的原因");
        } else if (type == 2) {//服务
            setReportTitle("举报该服务");
            setReportText("请选择举报该服务的原因");
        }
    }

    private void initView() {

    }

    private void initListener() {
        mActivityReportTaskBinding.etReportContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int textCount = s.length();
                mActivityReportTaskBinding.tvContentCount.setText(textCount + "/50");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**
     * 确定举报按钮
     *
     * @param v
     */
    public void sureReport(View v) {
        String reportContent = mActivityReportTaskBinding.etReportContent.getText().toString();
        if (currentCheckedReasonType == FEED_CLAIMS_FOR_OTHER && TextUtils.isEmpty(reportContent)) {//这种情况，以目前的UI设计来看，是不存在的
            ToastUtils.shortToast("请填写其它描述内容");
            return;
        }
        if (reportContent.length() > 50) {
            ToastUtils.shortToast("描述不能超过50个字");
            return;
        }
        MyTaskEngine.reportTask(new BaseProtocol.IResultExecutor<CommonResultBean>() {
            @Override
            public void execute(CommonResultBean dataBean) {
                ToastUtils.shortToast("举报成功");
                CommonUtils.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mActivity.finish();
                    }
                }, 2000);
            }

            @Override
            public void executeResultError(String result) {
                ToastUtils.shortToast("举报失败");
                LogKit.v("举报失败:" + result);
            }
        }, tid + "", type + "", currentCheckedReasonType + "", reportContent);
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void checkReportReason(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_report_reason_1:
                currentCheckedReasonType = FEED_CLAIMS_FOR_UNFIT;
                setCheckedReasonIcon(R.mipmap.pitchon_btn, R.mipmap.default_btn, R.mipmap.default_btn, R.mipmap.default_btn);
                break;
            case R.id.ll_report_reason_2:
                currentCheckedReasonType = FEED_CLAIMS_FOR_FRAUD;
                setCheckedReasonIcon(R.mipmap.default_btn, R.mipmap.pitchon_btn, R.mipmap.default_btn, R.mipmap.default_btn);
                break;
            case R.id.ll_report_reason_3:
                currentCheckedReasonType = FEED_CLAIMS_FOR_HARASS;
                setCheckedReasonIcon(R.mipmap.default_btn, R.mipmap.default_btn, R.mipmap.pitchon_btn, R.mipmap.default_btn);
                break;
            case R.id.ll_report_reason_4:
                currentCheckedReasonType = FEED_CLAIMS_FOR_TORT;
                setCheckedReasonIcon(R.mipmap.default_btn, R.mipmap.default_btn, R.mipmap.default_btn, R.mipmap.pitchon_btn);
                break;
        }
    }

    private void setCheckedReasonIcon(int reasonIconRes1, int reasonIconRes2, int reasonIconRes3, int reasonIconRes4) {
        mActivityReportTaskBinding.ivReasonIcon1.setImageResource(reasonIconRes1);
        mActivityReportTaskBinding.ivReasonIcon2.setImageResource(reasonIconRes2);
        mActivityReportTaskBinding.ivReasonIcon3.setImageResource(reasonIconRes3);
        mActivityReportTaskBinding.ivReasonIcon4.setImageResource(reasonIconRes4);
    }

    private String reportTitle;
    private String reportText;

    @Bindable
    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
        notifyPropertyChanged(BR.reportTitle);
    }

    @Bindable
    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
        notifyPropertyChanged(BR.reportText);
    }
}
