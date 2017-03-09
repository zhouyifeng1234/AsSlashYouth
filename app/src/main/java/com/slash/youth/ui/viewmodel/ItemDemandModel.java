package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ItemDemandLayoutBinding;

/**
 * Created by zss on 2017/1/17.
 */
public class ItemDemandModel extends BaseObservable {
    private ItemDemandLayoutBinding itemDemandLayoutBinding;
    private int isAuthVisivity = View.GONE;
    private int timeVisibility = View.GONE;
    private int instalmentVisibility = View.GONE;
    private String title;
    private String quote;
    private String freeTime;
    private String name;
    private String pattern;
    private String instalment;
    private String place;
    private String distance;
    private int radhintVisibility = View.GONE;

    @Bindable
    public int getRadhintVisibility() {
        return radhintVisibility;
    }

    public void setRadhintVisibility(int radhintVisibility) {
        this.radhintVisibility = radhintVisibility;
        notifyPropertyChanged(BR.radhintVisibility);
    }

    public ItemDemandModel(ItemDemandLayoutBinding itemDemandLayoutBinding) {
        this.itemDemandLayoutBinding = itemDemandLayoutBinding;
    }

    @Bindable
    public int getIsAuthVisivity() {
        return isAuthVisivity;
    }

    public void setIsAuthVisivity(int isAuthVisivity) {
        this.isAuthVisivity = isAuthVisivity;
        notifyPropertyChanged(BR.isAuthVisivity);
    }

    @Bindable
    public int getTimeVisibility() {
        return timeVisibility;
    }

    public void setTimeVisibility(int timeVisibility) {
        this.timeVisibility = timeVisibility;
        notifyPropertyChanged(BR.timeVisibility);
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
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
    public String getFreeTime() {
        return freeTime;
    }

    public void setFreeTime(String freeTime) {
        this.freeTime = freeTime;
        notifyPropertyChanged(BR.freeTime);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
        notifyPropertyChanged(BR.pattern);
    }

    @Bindable
    public String getInstalment() {
        return instalment;
    }

    public void setInstalment(String instalment) {
        this.instalment = instalment;
        notifyPropertyChanged(BR.instalment);
    }

    @Bindable
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
        notifyPropertyChanged(BR.place);
    }

    @Bindable
    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
        notifyPropertyChanged(BR.distance);
    }

    @Bindable
    public int getInstalmentVisibility() {
        return instalmentVisibility;
    }

    public void setInstalmentVisibility(int instalmentVisibility) {
        this.instalmentVisibility = instalmentVisibility;
        notifyPropertyChanged(BR.instalmentVisibility);
    }
}
