package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityResultAllBinding;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.databinding.SearchPagerFirstBinding;
import com.slash.youth.ui.viewmodel.ActivitySearchModel;
import com.slash.youth.ui.viewmodel.SearchActivityHotServiceModel;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.ui.viewmodel.SearchPagerFirstModel;
import com.slash.youth.ui.viewmodel.SearchResultAllModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class SearchActivity extends Activity {

    private SearchNeedResultTabBinding searchNeedResultTabBinding;
    private SearchActivityHotServiceBinding searchActivityHotServiceBinding;
    private ActivitySearchBinding activitySearchBinding;
    private ActivityResultAllBinding activityResultAllBinding;
    private SearchPagerFirstBinding searchPagerFirstBinding;
    public String checkedFirstLabel = "未选择";
    public int page = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        //加载搜索框页面
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        ActivitySearchModel activitySearchModel = new ActivitySearchModel(activitySearchBinding);
        activitySearchBinding.setActivitySearchModel(activitySearchModel);

      //  initView();

      //  removeView();

    }

    //加载页面
    private void initView() {

        //加载首页
        searchPagerFirstBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_pager_first, null, false);
        SearchPagerFirstModel searchPagerFirstModel = new SearchPagerFirstModel(searchPagerFirstBinding);
        searchPagerFirstBinding.setSearchPagerFirstModel(searchPagerFirstModel);

        //加载搜索技能页面
        searchActivityHotServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_activity_hot_service, null, false);
        SearchActivityHotServiceModel searchActivityHotServiceModel = new SearchActivityHotServiceModel(searchActivityHotServiceBinding);
        searchActivityHotServiceBinding.setSearchActivityHotServiceModel(searchActivityHotServiceModel);

        //加载搜索结果页面
        searchNeedResultTabBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_need_result_tab, null, false);
        SearchNeedResultTabModel searchResultTabModel = new SearchNeedResultTabModel(searchNeedResultTabBinding);
        searchNeedResultTabBinding.setSearchNeedResultTabModel(searchResultTabModel);

        //加载搜索所有结果页面
        activityResultAllBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.activity_result_all, null, false);
        SearchResultAllModel searchResultAllModel = new SearchResultAllModel(activityResultAllBinding);
        activityResultAllBinding.setSearchResultAllModel(searchResultAllModel);


        changeView(0);

    }
    //切换页面
    public void changeView(int page) {
        activitySearchBinding.flSearchFirst.removeAllViews();
        switch (page){
            case 0:
                activitySearchBinding.flSearchFirst.addView(searchPagerFirstBinding.getRoot());
                break;
            case 1:
                activitySearchBinding.flSearchFirst.addView(searchActivityHotServiceBinding.getRoot());
                break;
            case 2:
                activitySearchBinding.flSearchFirst.addView(searchNeedResultTabBinding.getRoot());
                break;
            case 3:
                activitySearchBinding.flSearchFirst.addView(activityResultAllBinding.getRoot());
                break;
        }

    }


    //删除页面
    private void removeView() {
        activitySearchBinding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeView(page);


            }
        });
    }



}
