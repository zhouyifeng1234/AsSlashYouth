package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendServicePartBinding;
import com.slash.youth.ui.adapter.RecommendServicePartAdapter;

/**
 * Created by zhouyifeng on 2016/10/18.
 */
public class ItemRecommendServicePartModel extends BaseObservable {
    ItemRecommendServicePartBinding mItemRecommendServicePartBinding;
    private int mCurrentPosition;

    public ItemRecommendServicePartModel(ItemRecommendServicePartBinding itemRecommendServicePartBinding) {
        this.mItemRecommendServicePartBinding = itemRecommendServicePartBinding;
        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }

    public void checkRecommendServicePart(View v) {
        if (RecommendServicePartAdapter.listCheckedItemId.contains(mCurrentPosition)) {
            RecommendServicePartAdapter.listCheckedItemId.remove(new Integer(mCurrentPosition));
            mItemRecommendServicePartBinding.ivRecommendServicePartChecked.setImageResource(R.mipmap.default_btn);
        } else {
            RecommendServicePartAdapter.listCheckedItemId.add(mCurrentPosition);
            mItemRecommendServicePartBinding.ivRecommendServicePartChecked.setImageResource(R.mipmap.pitchon_btn);
        }
    }


    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }


    private String serviceUsername;
    private int authVisibility;
    private String companyAndPosition;
    private String industryAndDirection;

    @Bindable
    public String getIndustryAndDirection() {
        return industryAndDirection;
    }

    public void setIndustryAndDirection(String industryAndDirection) {
        this.industryAndDirection = industryAndDirection;
        notifyPropertyChanged(BR.industryAndDirection);
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
    public int getAuthVisibility() {
        return authVisibility;
    }

    public void setAuthVisibility(int authVisibility) {
        this.authVisibility = authVisibility;
        notifyPropertyChanged(BR.authVisibility);
    }

    @Bindable
    public String getServiceUsername() {
        return serviceUsername;
    }

    public void setServiceUsername(String serviceUsername) {
        this.serviceUsername = serviceUsername;
        notifyPropertyChanged(BR.serviceUsername);
    }
}
