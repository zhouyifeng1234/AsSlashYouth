package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.SearchActivityHotServiceBinding;

/**
 * Created by zss on 2016/9/26.
 */
public class SearchActivityHotServiceModel extends BaseObservable {

    private SearchActivityHotServiceBinding searchActivityHotServiceBinding;

    public SearchActivityHotServiceModel(SearchActivityHotServiceBinding searchActivityHotServiceBinding) {
        this.searchActivityHotServiceBinding = searchActivityHotServiceBinding;
    }
}
