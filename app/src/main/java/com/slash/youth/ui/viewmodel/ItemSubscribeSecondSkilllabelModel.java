package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemListviewSubscribeSecondSkilllabelBinding;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class ItemSubscribeSecondSkilllabelModel extends BaseObservable {

    ItemListviewSubscribeSecondSkilllabelBinding mItemListviewSubscribeSecondSkilllabelBinding;

    public ItemSubscribeSecondSkilllabelModel(ItemListviewSubscribeSecondSkilllabelBinding itemListviewSubscribeSecondSkilllabelBinding) {
        this.mItemListviewSubscribeSecondSkilllabelBinding = itemListviewSubscribeSecondSkilllabelBinding;
    }

    private String secondSkilllabelName;
    private int secondSkilllabelColor=0xff333333;

    @Bindable
    public int getSecondSkilllabelColor() {
        return secondSkilllabelColor;
    }

    public void setSecondSkilllabelColor(int secondSkilllabelColor) {
        this.secondSkilllabelColor = secondSkilllabelColor;
        notifyPropertyChanged(BR.secondSkilllabelColor);
    }

    @Bindable
    public String getSecondSkilllabelName() {
        return secondSkilllabelName;
    }

    public void setSecondSkilllabelName(String secondSkilllabelName) {
        this.secondSkilllabelName = secondSkilllabelName;
        notifyPropertyChanged(BR.secondSkilllabelName);
    }
}
