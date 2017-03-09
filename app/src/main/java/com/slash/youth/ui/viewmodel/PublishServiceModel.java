package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBinding;
import com.slash.youth.ui.activity.PublishServiceInfoActivity;
import com.slash.youth.ui.view.SlashDateTimePicker;
import com.slash.youth.ui.view.fly.SlashTimePicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishServiceModel extends BaseObservable {

    public static final int IDLETIME_TYPE_CYCLE = 0;//周期闲置时间
    public static final int IDLETIME_TYPE_FRAGMENT = 1;//碎片闲置时间


    ActivityPublishServiceBinding mActivityPublishServiceBinding;
    Activity mActivity;
    private int chooseDateTimeLayerVisibility = View.INVISIBLE;//选择时间浮层时候显示，默认为不显示
    private SlashDateTimePicker mChooseDateTimePicker;
    private SlashTimePicker mChooseTimePicker;
    int chooseIdleTimeType = IDLETIME_TYPE_CYCLE;//选择闲置时间的类型，默认为周期
    private int dateTimePickerVisibility;
    private int timePickerVisibility;
    boolean isChooseStartTime;//true为选择开始时间，false为选择结束时间
    private String startIdleTimeText;
    private String endIdleTimeText;

    private int mStartDisplayMonth;
    private int mStartDisplayDay;
    private int mStartDisplayHour;
    private int mStartDisplayMinute;
    private int mEndDisplayMonth;
    private int mEndDisplayDay;
    private int mEndDisplayHour;
    private int mEndDisplayMinute;

    boolean isRealName = true;//是否为实名发布服务，默认为true
    ArrayList<String> listChooseServiceCycle = new ArrayList<String>();//选择的服务周期，默认为都不选
    private boolean mIsPublishSecondService;
    private String nextOrPublishText;

    public PublishServiceModel(ActivityPublishServiceBinding activityPublishServiceBinding, Activity activity) {
        this.mActivityPublishServiceBinding = activityPublishServiceBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        mChooseDateTimePicker = mActivityPublishServiceBinding.sdtpPublishServiceChooseDatetime;
        mChooseTimePicker = mActivityPublishServiceBinding.stpPublishServiceChooseTime;
        mIsPublishSecondService = mActivity.getIntent().getBooleanExtra("isPublishSecondService", false);
        if (mIsPublishSecondService) {
            setNextOrPublishText("发布");
        } else {
            setNextOrPublishText("下一步");
        }
        initData();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        mStartDisplayMonth = calendar.get(Calendar.MONTH) + 1;
        mStartDisplayDay = calendar.get(Calendar.DAY_OF_MONTH);
        mStartDisplayHour = calendar.get(Calendar.HOUR_OF_DAY);
        mStartDisplayMinute = calendar.get(Calendar.MINUTE);
        mEndDisplayMonth = mStartDisplayMonth;
        mEndDisplayDay = mStartDisplayDay;
        mEndDisplayHour = mStartDisplayHour;
        mEndDisplayMinute = mStartDisplayMinute;
        //默认为周期时间显示
        setStartIdleTimeText(mStartDisplayHour + "时" + (mStartDisplayMinute < 10 ? "0" + mStartDisplayMinute : mStartDisplayMinute) + "分");
        setEndIdleTimeText(mEndDisplayHour + "时" + (mEndDisplayMinute < 10 ? "0" + mEndDisplayMinute : mEndDisplayMinute) + "分");
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        if (mIsPublishSecondService) {
            ToastUtils.shortToast("重新发布成功");
        } else {
            Intent intentPublishServiceInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceInfoActivity.class);

            Bundle publishServiceDataBundle = new Bundle();
            publishServiceDataBundle.putBoolean("isRealName", isRealName);
            publishServiceDataBundle.putInt("chooseIdleTimeType", chooseIdleTimeType);
            publishServiceDataBundle.putStringArrayList("listChooseServiceCycle", listChooseServiceCycle);
            publishServiceDataBundle.putBoolean("isAllDayChecked", isAllDayChecked);
            publishServiceDataBundle.putInt("mStartDisplayMonth", mStartDisplayMonth);
            publishServiceDataBundle.putInt("mStartDisplayDay", mStartDisplayDay);
            publishServiceDataBundle.putInt("mStartDisplayHour", mStartDisplayHour);
            publishServiceDataBundle.putInt("mStartDisplayMinute", mStartDisplayMinute);
            publishServiceDataBundle.putInt("mEndDisplayMonth", mEndDisplayMonth);
            publishServiceDataBundle.putInt("mEndDisplayDay", mEndDisplayDay);
            publishServiceDataBundle.putInt("mEndDisplayHour", mEndDisplayHour);
            publishServiceDataBundle.putInt("mEndDisplayMinute", mEndDisplayMinute);

            intentPublishServiceInfoActivity.putExtra("publishServiceDataBundle", publishServiceDataBundle);
            intentPublishServiceInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            CommonUtils.getContext().startActivity(intentPublishServiceInfoActivity);
        }
    }


    private boolean isAllDayChecked = true;

    public void checkAllDay(View v) {
        if (isAllDayChecked) {
            mActivityPublishServiceBinding.ivPublishServiceCheckAllday.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            mActivityPublishServiceBinding.ivPublishServiceCheckAllday.setImageResource(R.mipmap.dui_icon);

            mStartDisplayHour = 0;
            mStartDisplayMinute = 0;
            mEndDisplayHour = 23;
            mEndDisplayMinute = 59;
            if (chooseIdleTimeType == IDLETIME_TYPE_CYCLE) {
                setStartIdleTimeText(mStartDisplayHour + "时" + (mStartDisplayMinute < 10 ? "0" + mStartDisplayMinute : mStartDisplayMinute) + "分");
                setEndIdleTimeText(mEndDisplayHour + "时" + (mEndDisplayMinute < 10 ? "0" + mEndDisplayMinute : mEndDisplayMinute) + "分");
            } else {
                setStartIdleTimeText(mStartDisplayMonth + "月" + mStartDisplayDay + "日" + "-" + mStartDisplayHour + ":" + (mStartDisplayMinute < 10 ? "0" + mStartDisplayMinute : mStartDisplayMinute));
                setEndIdleTimeText(mEndDisplayMonth + "月" + mEndDisplayDay + "日" + "-" + mEndDisplayHour + ":" + (mEndDisplayMinute < 10 ? "0" + mEndDisplayMinute : mEndDisplayMinute));
            }
        }
        isAllDayChecked = !isAllDayChecked;
    }

    public void chooseIdleStartTime(View v) {
        setChooseDateTimeLayerVisibility(View.VISIBLE);
        if (chooseIdleTimeType == IDLETIME_TYPE_CYCLE) {
            setDateTimePickerVisibility(View.GONE);
            setTimePickerVisibility(View.VISIBLE);
        } else {
            setDateTimePickerVisibility(View.VISIBLE);
            setTimePickerVisibility(View.GONE);
        }

        isChooseStartTime = true;
    }

    public void chooseIdleEndTime(View v) {
        setChooseDateTimeLayerVisibility(View.VISIBLE);
        if (chooseIdleTimeType == IDLETIME_TYPE_CYCLE) {
            setDateTimePickerVisibility(View.GONE);
            setTimePickerVisibility(View.VISIBLE);
        } else {
            setDateTimePickerVisibility(View.VISIBLE);
            setTimePickerVisibility(View.GONE);
        }

        isChooseStartTime = false;
    }

    @Bindable
    public int getChooseDateTimeLayerVisibility() {
        return chooseDateTimeLayerVisibility;
    }

    public void setChooseDateTimeLayerVisibility(int chooseDateTimeLayerVisibility) {
        this.chooseDateTimeLayerVisibility = chooseDateTimeLayerVisibility;
        notifyPropertyChanged(BR.chooseDateTimeLayerVisibility);
    }

    public void okChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.INVISIBLE);
        if (chooseIdleTimeType == IDLETIME_TYPE_CYCLE) {
            //周期时间 只要小时和分钟
            int currentChooseHour = mChooseTimePicker.getCurrentChooseHour();
            int currentChooseMinute = mChooseTimePicker.getCurrentChooseMinute();
            if (isChooseStartTime) {
                setStartIdleTimeText(currentChooseHour + "时" + (currentChooseMinute < 10 ? "0" + currentChooseMinute : currentChooseMinute) + "分");
                mStartDisplayHour = currentChooseHour;
                mStartDisplayMinute = currentChooseMinute;
            } else {
                setEndIdleTimeText(currentChooseHour + "时" + (currentChooseMinute < 10 ? "0" + currentChooseMinute : currentChooseMinute) + "分");
                mEndDisplayHour = currentChooseHour;
                mEndDisplayMinute = currentChooseMinute;
            }
        } else {
            //碎片时间 需要 月、日、小时、分钟
            int currentChooseMonth = mChooseDateTimePicker.getCurrentChooseMonth();
            int currentChooseDay = mChooseDateTimePicker.getCurrentChooseDay();
            int currentChooseHour = mChooseDateTimePicker.getCurrentChooseHour();
            int currentChooseMinute = mChooseDateTimePicker.getCurrentChooseMinute();
            if (isChooseStartTime) {
                setStartIdleTimeText(currentChooseMonth + "月" + currentChooseDay + "日" + "-" + currentChooseHour + ":" + (currentChooseMinute < 10 ? "0" + currentChooseMinute : currentChooseMinute));
                mStartDisplayMonth = currentChooseMonth;
                mStartDisplayDay = currentChooseDay;
                mStartDisplayHour = currentChooseHour;
                mStartDisplayMinute = currentChooseMinute;
            } else {
                setEndIdleTimeText(currentChooseMonth + "月" + currentChooseDay + "日" + "-" + currentChooseHour + ":" + (currentChooseMinute < 10 ? "0" + currentChooseMinute : currentChooseMinute));
                mEndDisplayMonth = currentChooseMonth;
                mEndDisplayDay = currentChooseDay;
                mEndDisplayHour = currentChooseHour;
                mEndDisplayMinute = currentChooseMinute;
            }
        }
    }

    public void cancelChooseTime(View v) {
        setChooseDateTimeLayerVisibility(View.INVISIBLE);
    }

    /**
     * 切换到选择周期闲置时间
     *
     * @param v
     */
    public void tabCycleIdleTime(View v) {
        mActivityPublishServiceBinding.tvServiceCycleIdletimeText.setTextColor(0xff31C5E4);
        mActivityPublishServiceBinding.tvServiceFragmentIdletimeText.setTextColor(0xff999999);
        mActivityPublishServiceBinding.viewServiceCycleIdletimeBg.setBackgroundColor(0xff31C5E4);
        mActivityPublishServiceBinding.viewServiceFragmentIdletimeBg.setBackgroundColor(0xffE5E5E5);
        setStartIdleTimeText(mStartDisplayHour + "时" + mStartDisplayMinute + "分");
        setEndIdleTimeText(mEndDisplayHour + "时" + mEndDisplayMinute + "分");

        chooseIdleTimeType = IDLETIME_TYPE_CYCLE;
    }

    /**
     * 切换到选择碎片闲置时间
     *
     * @param v
     */
    public void tabFragmentIdleTime(View v) {
        mActivityPublishServiceBinding.tvServiceCycleIdletimeText.setTextColor(0xff999999);
        mActivityPublishServiceBinding.tvServiceFragmentIdletimeText.setTextColor(0xff31C5E4);
        mActivityPublishServiceBinding.viewServiceCycleIdletimeBg.setBackgroundColor(0xffE5E5E5);
        mActivityPublishServiceBinding.viewServiceFragmentIdletimeBg.setBackgroundColor(0xff31C5E4);
        setStartIdleTimeText(mStartDisplayMonth + "月" + mStartDisplayDay + "日" + "-" + mStartDisplayHour + ":" + mStartDisplayMinute);
        setEndIdleTimeText(mEndDisplayMonth + "月" + mEndDisplayDay + "日" + "-" + mEndDisplayHour + ":" + mEndDisplayMinute);

        chooseIdleTimeType = IDLETIME_TYPE_FRAGMENT;
    }

    @Bindable
    public int getDateTimePickerVisibility() {
        return dateTimePickerVisibility;
    }

    public void setDateTimePickerVisibility(int dateTimePickerVisibility) {
        this.dateTimePickerVisibility = dateTimePickerVisibility;
        notifyPropertyChanged(BR.dateTimePickerVisibility);
    }

    @Bindable
    public int getTimePickerVisibility() {
        return timePickerVisibility;
    }

    public void setTimePickerVisibility(int timePickerVisibility) {
        this.timePickerVisibility = timePickerVisibility;
        notifyPropertyChanged(BR.timePickerVisibility);
    }

    @Bindable
    public String getStartIdleTimeText() {
        return startIdleTimeText;
    }

    public void setStartIdleTimeText(String startIdleTimeText) {
        this.startIdleTimeText = startIdleTimeText;
        notifyPropertyChanged(BR.startIdleTimeText);
    }

    @Bindable
    public String getEndIdleTimeText() {
        return endIdleTimeText;
    }

    public void setEndIdleTimeText(String endIdleTimeText) {
        this.endIdleTimeText = endIdleTimeText;
        notifyPropertyChanged(BR.endIdleTimeText);
    }


    public void setRealName(View v) {
        mActivityPublishServiceBinding.ivbtnPublishDemandRealname.setImageResource(R.mipmap.btn_employer_treat);
        mActivityPublishServiceBinding.ivbtnPublishDemandAnonymous.setImageResource(R.mipmap.service_ptype_moren_icon);
        isRealName = true;
    }

    public void setAnonymous(View v) {
        mActivityPublishServiceBinding.ivbtnPublishDemandRealname.setImageResource(R.mipmap.service_ptype_moren_icon);
        mActivityPublishServiceBinding.ivbtnPublishDemandAnonymous.setImageResource(R.mipmap.btn_employer_treat);
        isRealName = false;
    }

    public void checkedServiceCycle(View v) {
        TextView tvCycleText = (TextView) ((FrameLayout) v).getChildAt(0);
        String chooseServiceCycle = tvCycleText.getText().toString();
        if (listChooseServiceCycle.contains(chooseServiceCycle)) {
            listChooseServiceCycle.remove(chooseServiceCycle);
            tvCycleText.setBackgroundResource(R.drawable.shape_round_choosecycle_bg);
            tvCycleText.setTextColor(0xffffffff);
        } else {
            listChooseServiceCycle.add(chooseServiceCycle);
            tvCycleText.setBackgroundResource(R.drawable.shape_round_choosecycle_bg_selected);
            tvCycleText.setTextColor(0xffCCCCCC);
        }
    }

    @Bindable
    public String getNextOrPublishText() {
        return nextOrPublishText;
    }

    public void setNextOrPublishText(String nextOrPublishText) {
        this.nextOrPublishText = nextOrPublishText;
        notifyPropertyChanged(BR.nextOrPublishText);
    }
}
