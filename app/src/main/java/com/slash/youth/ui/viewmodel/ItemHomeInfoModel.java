package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemListviewHomeInfoBinding;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class ItemHomeInfoModel extends BaseObservable {
    ItemListviewHomeInfoBinding mItemListviewHomeInfoBinding;

    public ItemHomeInfoModel(ItemListviewHomeInfoBinding itemListviewHomeInfoBinding) {
        this.mItemListviewHomeInfoBinding = itemListviewHomeInfoBinding;
        initView();
    }

    private void initView() {

    }

    private String username;
    private int relatedTasksInfoVisibility;
    private int addVVisibility;
    private int userLabelsInfoVisibility;
    private String companyAndPosition;
    private String lastMsg;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }


    @Bindable
    public int getRelatedTasksInfoVisibility() {
        return relatedTasksInfoVisibility;
    }

    public void setRelatedTasksInfoVisibility(int relatedTasksInfoVisibility) {
        this.relatedTasksInfoVisibility = relatedTasksInfoVisibility;
        notifyPropertyChanged(BR.relatedTasksInfoVisibility);
    }

    @Bindable
    public int getAddVVisibility() {
        return addVVisibility;
    }

    public void setAddVVisibility(int addVVisibility) {
        this.addVVisibility = addVVisibility;
        notifyPropertyChanged(BR.addVVisibility);
    }

    @Bindable
    public int getUserLabelsInfoVisibility() {
        return userLabelsInfoVisibility;
    }

    public void setUserLabelsInfoVisibility(int userLabelsInfoVisibility) {
        this.userLabelsInfoVisibility = userLabelsInfoVisibility;
        notifyPropertyChanged(BR.userLabelsInfoVisibility);
    }

    @Bindable
    public String getCompanyAndPosition() {
        return companyAndPosition;
    }

    public void setCompanyAndPosition(String companyAndPosition) {
        this.companyAndPosition = companyAndPosition;
        notifyPropertyChanged(BR.companyAndPosition);
    }

    @Bindable
    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
        notifyPropertyChanged(BR.lastMsg);
    }
}
