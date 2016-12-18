package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemRecommendDemandBinding;
import com.slash.youth.ui.adapter.RecommendDemandAdapter;

/**
 * Created by zhouyifeng on 2016/11/9.
 */
public class ItemRecommendDemandModel extends BaseObservable {
    ItemRecommendDemandBinding mItemRecommendDemandBinding;

    private int mCurrentPosition;

    public ItemRecommendDemandModel(ItemRecommendDemandBinding itemRecommendDemandBinding) {
        this.mItemRecommendDemandBinding = itemRecommendDemandBinding;

        initData();
        initView();
    }

    private void initData() {

    }

    private void initView() {

    }


    public void checkRecommendDemand(View v) {
        if (RecommendDemandAdapter.listCheckedItemId.contains(mCurrentPosition)) {
            RecommendDemandAdapter.listCheckedItemId.remove(new Integer(mCurrentPosition));
            mItemRecommendDemandBinding.ivRecommendDemandChecked.setImageResource(R.mipmap.default_btn);
        } else {
            RecommendDemandAdapter.listCheckedItemId.add(mCurrentPosition);
            mItemRecommendDemandBinding.ivRecommendDemandChecked.setImageResource(R.mipmap.pitchon_btn);
        }
    }

    public void setCurrentPosition(int currentPosition) {
        this.mCurrentPosition = currentPosition;
    }

    private int authVisibility;
    private String demandQuote = "300元";//300元
    private String demandTitle="设计全套官方网站和微信端的首页";
    private String demandUsername;

    @Bindable
    public String getDemandUsername() {
        return demandUsername;
    }

    public void setDemandUsername(String demandUsername) {
        this.demandUsername = demandUsername;
        notifyPropertyChanged(BR.demandUsername);
    }

    @Bindable
    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
        notifyPropertyChanged(BR.demandTitle);
    }

    @Bindable
    public String getDemandQuote() {
        return demandQuote;
    }

    public void setDemandQuote(String demandQuote) {
        this.demandQuote = demandQuote;
        notifyPropertyChanged(BR.demandQuote);
    }

    @Bindable
    public int getAuthVisibility() {
        return authVisibility;
    }

    public void setAuthVisibility(int authVisibility) {
        this.authVisibility = authVisibility;
        notifyPropertyChanged(BR.authVisibility);
    }
}
