package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.graphics.Color;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.SearchDemandTabBinding;

/**
 * Created by acer on 2016/12/7.
 */
public class SearchDemandTabModel extends BaseObservable {

    private SearchDemandTabBinding searchDemandTabBinding;
    private boolean isdemand = true;
    private boolean isUser = true;
    private boolean isArea = true;
    private boolean isSort = true;

    public SearchDemandTabModel(SearchDemandTabBinding searchDemandTabBinding) {
        this.searchDemandTabBinding = searchDemandTabBinding;
    }


    //点击需求类型
    public void demandType(View view){
        setDemandTabChange(isdemand);
        isdemand = !isdemand;
        setUserTabChange(false);
        setAreaTabChange(false);
        setSortTabChange(false);
       // setDemandTabChange(false);
    }


    //用户类型
    public void userType(View view){
        setUserTabChange(isUser);
        isUser = !isUser;
       //setUserTabChange(false);
        setAreaTabChange(false);
        setSortTabChange(false);
        setDemandTabChange(false);
    }


    //地区
    public void area(View view){
        setAreaTabChange(isArea);
        isArea = !isArea;
        setUserTabChange(false);
       // setAreaTabChange(false);
        setSortTabChange(false);
        setDemandTabChange(false);
    }

    //排序
    public void sort(View view){
        setSortTabChange(isSort);
        isSort = !isSort;
        setUserTabChange(false);
        setAreaTabChange(false);
        //setSortTabChange(false);
        setDemandTabChange(false);
    }

    private void setSortTabChange(boolean isSort) {
        searchDemandTabBinding.ivTimeIcon.setImageResource(isSort? R.mipmap.free_pay_jihuo: R.mipmap.free_play);
        searchDemandTabBinding.tvSort.setTextColor(isSort? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
    }

    private void setAreaTabChange(boolean isArea) {
        searchDemandTabBinding.ivAreaIcon.setImageResource(isArea? R.mipmap.free_pay_jihuo: R.mipmap.free_play);
        searchDemandTabBinding.tvArea.setTextColor(isArea? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
    }

    private void setUserTabChange(boolean isUser) {
        searchDemandTabBinding.tvUser.setTextColor(isUser? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
        searchDemandTabBinding.ivUserIcon.setImageResource(isUser? R.mipmap.free_pay_jihuo: R.mipmap.free_play);
    }

    private void setDemandTabChange(boolean isdemand) {
        searchDemandTabBinding.ivLineIcon.setImageResource(isdemand? R.mipmap.free_pay_jihuo: R.mipmap.free_play);
        searchDemandTabBinding.tvLine.setTextColor(isdemand? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
    }


}
