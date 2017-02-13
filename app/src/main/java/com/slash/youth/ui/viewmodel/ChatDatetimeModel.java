package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemChatDatetimeBinding;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatDatetimeModel extends BaseObservable {

    ItemChatDatetimeBinding mItemChatDatetimeBinding;
    Activity mActivity;
    long mDatetime;


    public ChatDatetimeModel(ItemChatDatetimeBinding itemChatDatetimeBinding, Activity activity, long datetime) {
        this.mItemChatDatetimeBinding = itemChatDatetimeBinding;
        this.mActivity = activity;
        this.mDatetime = datetime;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {
//这个Locale.CHINA好像不是我加的 2017年2月11号
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm", Locale.CHINA);
        String datetimeStr = sdf.format(mDatetime);
        setDatetimeText(datetimeStr);
    }

    private String datetimeText;

    @Bindable
    public String getDatetimeText() {
        return datetimeText;
    }

    public void setDatetimeText(String datetimeText) {
        this.datetimeText = datetimeText;
        notifyPropertyChanged(BR.datetimeText);
    }
}
