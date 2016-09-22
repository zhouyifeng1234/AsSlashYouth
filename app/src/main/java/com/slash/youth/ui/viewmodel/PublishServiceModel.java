package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.NumberPicker;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishServiceBinding;
import com.slash.youth.ui.activity.PublishServiceInfoActivity;
import com.slash.youth.utils.CommonUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishServiceModel extends BaseObservable {

    ActivityPublishServiceBinding mActivityPublishServiceBinding;
    Activity mActivity;
    String[] monthAndDay = {"", "", "", "", ""};
    int[] monthIntArr = new int[5];
    int[] dayIntArr = new int[5];
    String[] hours = {"", "", "", "", ""};
    int[] hourIntArr = new int[5];
    String[] minutes = {"", "", "", "", ""};
    int[] minuteIntArr = new int[5];
    NumberPicker npMonthDay;
    NumberPicker npHour;
    NumberPicker npMinute;
    private Calendar mCalendar;

    public PublishServiceModel(ActivityPublishServiceBinding activityPublishServiceBinding, Activity activity) {
        this.mActivityPublishServiceBinding = activityPublishServiceBinding;
        initView();
    }

    private void initView() {
        npMonthDay = mActivityPublishServiceBinding.npPublishServiceMonthDay;
        npHour = mActivityPublishServiceBinding.npPublishServiceHour;
        npMinute = mActivityPublishServiceBinding.npPublishServiceMinute;
        initData();
        initListener();
    }

    private void initData() {
        mCalendar = Calendar.getInstance();
        Date date = new Date();
        mCalendar.setTime(date);
        initTime();

        npMonthDay.setDisplayedValues(monthAndDay);
        npHour.setDisplayedValues(hours);
        npMinute.setDisplayedValues(minutes);

        npMonthDay.setMinValue(0);
        npMonthDay.setMaxValue(4);
        npMonthDay.setValue(2);

        npHour.setMinValue(0);
        npHour.setMaxValue(4);
        npHour.setValue(2);

        npMinute.setMinValue(0);
        npMinute.setMaxValue(4);
        npMinute.setValue(2);
    }

    private void initTime() {
        mCalendar.add(Calendar.DAY_OF_MONTH, -3);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY) - 3;
        int minute = mCalendar.get(Calendar.MINUTE) - 3;
        for (int i = 0; i < 5; i++) {
            mCalendar.add(Calendar.DAY_OF_MONTH, 1);
            int month = mCalendar.get(Calendar.MONTH);
            monthIntArr[i] = month;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            dayIntArr[i] = day;
            monthAndDay[i] = month + "月" + day + "日";
            hour++;
            if (hour > 23) {
                hour = 0;
            }
            hourIntArr[i] = hour;
            if (hour < 10) {
                hours[i] = "0" + hour + "时";
            } else {
                hours[i] = hour + "时";
            }
            minute++;
            if (minute > 59) {
                minute = 0;
            }
            minuteIntArr[i] = minute;
            if (minute < 10) {
                minutes[i] = "0" + minute + "分";
            } else {
                minutes[i] = minute + "分";
            }
        }


        //恢复到当前的时间
        mCalendar.setTime(new Date());
    }

    private void initListener() {
        npMonthDay.setOnValueChangedListener(new ChooseTimeListener());
        npHour.setOnValueChangedListener(new ChooseTimeListener());
        npMinute.setOnValueChangedListener(new ChooseTimeListener());
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    public void nextStep(View v) {
        Intent intentPublishServiceInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceInfoActivity.class);
        intentPublishServiceInfoActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentPublishServiceInfoActivity);
    }


    private boolean isAllDayChecked = true;

    public void checkAllDay(View v) {
        if (isAllDayChecked) {
            mActivityPublishServiceBinding.ivPublishServiceCheckAllday.setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else {
            mActivityPublishServiceBinding.ivPublishServiceCheckAllday.setImageResource(R.mipmap.dui_icon);
        }
        isAllDayChecked = !isAllDayChecked;
    }

    public void chooseIdleTime(View v) {

    }

    public class ChooseTimeListener implements NumberPicker.OnValueChangeListener {

        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            int length = monthAndDay.length;
            int upVal = newVal - 2;
            int downVal = newVal + 2;
            if (upVal < 0) {
                upVal = upVal + length;
            }
            if (downVal > length - 1) {
                downVal = downVal - length;
            }

            switch (picker.getId()) {
                case R.id.np_publish_service_month_day:

                    boolean isUp;

//                    if (isUp) {
//                        //向上滚
//                    } else {
//                        //向下滚
//                    }


                    break;
                case R.id.np_publish_service_hour:
                    int chooseHour = hourIntArr[newVal];
                    hourIntArr[upVal] = chooseHour - 2;
                    if (hourIntArr[upVal] < 0) {
                        hourIntArr[upVal] = hourIntArr[upVal] + 24;
                    }
                    if (hourIntArr[upVal] < 10) {
                        hours[upVal] = "0" + hourIntArr[upVal] + "时";
                    } else {
                        hours[upVal] = hourIntArr[upVal] + "时";
                    }
                    hourIntArr[downVal] = chooseHour + 2;
                    if (hourIntArr[downVal] > 23) {
                        hourIntArr[downVal] = hourIntArr[downVal] - 24;
                    }
                    if (hourIntArr[downVal] < 10) {
                        hours[downVal] = "0" + hourIntArr[downVal] + "时";
                    } else {
                        hours[downVal] = hourIntArr[downVal] + "时";
                    }
                    break;
                case R.id.np_publish_service_minute:
                    int chooseMinute = minuteIntArr[newVal];
                    minuteIntArr[upVal] = chooseMinute - 2;
                    if (minuteIntArr[upVal] < 0) {
                        minuteIntArr[upVal] = minuteIntArr[upVal] + 60;
                    }
                    if (minuteIntArr[upVal] < 10) {
                        minutes[upVal] = "0" + minuteIntArr[upVal] + "分";
                    } else {
                        minutes[upVal] = minuteIntArr[upVal] + "分";
                    }
                    minuteIntArr[downVal] = chooseMinute + 2;
                    if (minuteIntArr[downVal] > 59) {
                        minuteIntArr[downVal] = minuteIntArr[downVal] - 60;
                    }
                    if (minuteIntArr[downVal] < 10) {
                        minutes[downVal] = "0" + minuteIntArr[downVal] + "分";
                    } else {
                        minutes[downVal] = minuteIntArr[downVal] + "分";
                    }
                    break;
            }
        }
    }


//    EditText mInputText;
//    Field[] pickerFields;
//    NumberPicker np;
//    Field pfInputText;

//    private void initView() {
////        Calendar calendar = Calendar.getInstance();
////        Date date = new Date();
////        LogKit.v(date.toGMTString());
////        calendar.setTime(date);
////        LogKit.v(calendar.getTime().toString());
////        calendar.add(Calendar.DAY_OF_MONTH, 1);
////        LogKit.v(calendar.getTime().toString());
////        calendar.add(Calendar.DAY_OF_MONTH, 1);
////        LogKit.v(calendar.getTime().toString());
//
//
//        np = mActivityPublishServiceBinding.np;
//
//        pickerFields = NumberPicker.class.getDeclaredFields();
//
//        for (Field pf : pickerFields) {
//            try {
//                if (pf.getName().equals("mSelectionDivider")) {
//                    pf.setAccessible(true);
//                    pf.set(np, new ColorDrawable(0x00000000));
//                } else if (pf.getName().equals("mInputText")) {
//                    pf.setAccessible(true);
//                    pfInputText = pf;
//                    mInputText = (EditText) pf.get(np);
////                    mInputText.setTextColor(0xffff0000);
////                    mInputText.setTextSize(CommonUtils.dip2px(30));
////                    mInputText.setTextColor(0xffff0000);
//                    mInputText.setTextAppearance(CommonUtils.getContext(),R.color.selector_datepicker_textcolor);
//                }
//            } catch (IllegalArgumentException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//
//        final String[] strings = {"a", "b", "c", "d", "e", "f", "g", "h"};
//        np.setDisplayedValues(strings);
//        np.setMinValue(0);
//        np.setMaxValue(7);
//        np.setValue(3);
//        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                LogKit.v("oldVal" + oldVal);
//                LogKit.v("newVal" + newVal);
//                strings[newVal] = "change";
//                mInputText.setTextColor(0xffff0000);
//                mInputText.invalidate();
//            }
//        });
//    }
}
