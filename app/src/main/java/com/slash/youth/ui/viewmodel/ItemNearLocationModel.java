package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemListviewNearLocationBinding;

/**
 * Created by zhouyifeng on 2016/9/13.
 */
public class ItemNearLocationModel extends BaseObservable {
    ItemListviewNearLocationBinding mItemListviewNearLocationBinding;
    private String nearName;
    private String nearAddress;
    private String nearDistance;

    public ItemNearLocationModel(ItemListviewNearLocationBinding itemListviewNearLocationBinding) {
        this.mItemListviewNearLocationBinding = itemListviewNearLocationBinding;
        initView();
    }

    private void initView() {
    }

    @Bindable
    public String getNearName() {
        return nearName;
    }

    public void setNearName(String nearName) {
        this.nearName = nearName;
        notifyPropertyChanged(BR.nearName);
    }

    @Bindable
    public String getNearAddress() {
        return nearAddress;
    }

    public void setNearAddress(String nearAddress) {
        this.nearAddress = nearAddress;
        notifyPropertyChanged(BR.nearAddress);
    }

    @Bindable
    public String getNearDistance() {
        return nearDistance;
    }

    public void setNearDistance(String nearDistance) {
        this.nearDistance = nearDistance;
        notifyPropertyChanged(BR.nearDistance);
    }
}
