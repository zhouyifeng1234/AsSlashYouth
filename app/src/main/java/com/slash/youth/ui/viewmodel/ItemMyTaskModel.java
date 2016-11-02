package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemMyTaskBinding;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class ItemMyTaskModel extends BaseObservable {
    ItemMyTaskBinding mItemMyTaskBinding;

    public ItemMyTaskModel(ItemMyTaskBinding itemMyTaskBinding) {
        this.mItemMyTaskBinding = itemMyTaskBinding;
        initView();
        initData();
    }

    private void initView() {

    }

    private void initData() {

    }

    private String taskTitle;//任务标题
    private String startTime;//任务开始时间
    private int addVvisibility;//是否显示加V认证
    private String username;//用户名
    private String instalmentText;//分期显示的文本
    private String quote;//报价
    private int publishDemandStatusPointVisibility;//我发的需求状态小圆点是否可见（里面有数字，新增的抢单者数量）
    private int bidDemandStatusPointVisibility;//我抢的需求状态小圆点是否可见（没有需求，有状态变化，只需要显示圆点）
    private String bidnum;//抢单数量，服务端返回的是所有的抢单数量，这里需要新增的抢单数量


    @Bindable
    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
        notifyPropertyChanged(BR.taskTitle);
    }

    @Bindable
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
        notifyPropertyChanged(BR.startTime);
    }

    @Bindable
    public int getAddVvisibility() {
        return addVvisibility;
    }

    public void setAddVvisibility(int addVvisibility) {
        this.addVvisibility = addVvisibility;
        notifyPropertyChanged(BR.addVvisibility);
    }

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getInstalmentText() {
        return instalmentText;
    }

    public void setInstalmentText(String instalmentText) {
        this.instalmentText = instalmentText;
        notifyPropertyChanged(BR.instalmentText);
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
    public int getPublishDemandStatusPointVisibility() {
        return publishDemandStatusPointVisibility;
    }

    public void setPublishDemandStatusPointVisibility(int publishDemandStatusPointVisibility) {
        this.publishDemandStatusPointVisibility = publishDemandStatusPointVisibility;
        notifyPropertyChanged(BR.publishDemandStatusPointVisibility);
    }

    @Bindable
    public int getBidDemandStatusPointVisibility() {
        return bidDemandStatusPointVisibility;
    }

    public void setBidDemandStatusPointVisibility(int bidDemandStatusPointVisibility) {
        this.bidDemandStatusPointVisibility = bidDemandStatusPointVisibility;
        notifyPropertyChanged(BR.bidDemandStatusPointVisibility);
    }

    @Bindable
    public String getBidnum() {
        return bidnum;
    }

    public void setBidnum(String bidnum) {
        this.bidnum = bidnum;
        notifyPropertyChanged(BR.bidnum);
    }
}
