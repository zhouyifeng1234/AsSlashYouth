package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.slash.youth.BR;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class ItemPublishedServiceModel extends BaseObservable {

    private String title;
    private String price;
    private String type;
    private String buyinfo;
    private int chooseBgColor;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
        notifyPropertyChanged(BR.price);
    }

    @Bindable
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getBuyinfo() {
        return buyinfo;
    }

    public void setBuyinfo(String buyinfo) {
        this.buyinfo = buyinfo;
        notifyPropertyChanged(BR.buyinfo);
    }

    @Bindable
    public int getChooseBgColor() {
        return chooseBgColor;
    }

    public void setChooseBgColor(int chooseBgColor) {
        this.chooseBgColor = chooseBgColor;
        notifyPropertyChanged(BR.chooseBgColor);
    }
}
