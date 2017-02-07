package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.sip.SipAudioCall;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.blankj.utilcode.utils.RegexUtils;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchPagerFirstBinding;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SearchAllProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.StringUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by zss on 2016/10/16.
 */
public class SearchPagerFirstModel extends BaseObservable {
    private SearchPagerFirstBinding searchPagerFirstBinding;
    private  SearchActivity searchActivity;
    private PagerSearchItemAdapter pagerSearchItemAdapter;
    private String searchContent1 = "";
    private File externalCacheDir = CommonUtils.getApplication().getExternalCacheDir();
    private File file = new File(externalCacheDir, "history.text");
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private ListView listView;
    private ActivitySearchBinding activitySearchBinding;
    private String demadHint = "搜需求";
    private String serviceHint = "搜服务";
    private String userHint = "搜人";
    private CityHistoryEntityDao cityHistoryEntityDao;

    public SearchPagerFirstModel(SearchPagerFirstBinding searchPagerFirstBinding,ActivitySearchBinding activitySearchBinding,CityHistoryEntityDao cityHistoryEntityDao) {
        this.searchPagerFirstBinding = searchPagerFirstBinding;
        this.activitySearchBinding = activitySearchBinding;
        this.cityHistoryEntityDao = cityHistoryEntityDao;
    }

    //搜索需求
    public void searchDemand(View v) {
        showView(SearchManager.HOT_SEARCH_DEMEND);
        SpUtils.setString("searchType",SearchManager.HOT_SEARCH_DEMEND);
        activitySearchBinding.etActivitySearchAssociation.setHint(demadHint);
        //搜索需求埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SEARCH_CLICKC_REQUIREMENT_SEARCH);
    }

    //搜索服务
    public void searchService(View v) {
        showView(SearchManager.HOT_SEARCH_SERVICE);
        SpUtils.setString("searchType",SearchManager.HOT_SEARCH_SERVICE);
        activitySearchBinding.etActivitySearchAssociation.setHint(serviceHint);
        //搜索服务埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SEARCH_CLICK_SERVICE_SEARCH);
    }

    //搜索人
    public void searchUser(View v){
        showView(SearchManager.HOT_SEARCH_PERSON);
        SpUtils.setString("searchType",SearchManager.HOT_SEARCH_PERSON);
        activitySearchBinding.etActivitySearchAssociation.setHint(userHint);
        //搜索人的埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SEARCH_CLICK_PERSON_SEARCH);
    }

    public void showView(String title) {
        currentActivity.changeView(1);
        currentActivity.searchActivityHotServiceBinding.tvSearchTitle.setText(title);
        SearchActivityHotServiceModel searchActivityHotServiceModel = new SearchActivityHotServiceModel(currentActivity.searchActivityHotServiceBinding,currentActivity.activitySearchBinding,cityHistoryEntityDao);
        currentActivity.searchActivityHotServiceBinding.setSearchActivityHotServiceModel(searchActivityHotServiceModel);
    }
}
