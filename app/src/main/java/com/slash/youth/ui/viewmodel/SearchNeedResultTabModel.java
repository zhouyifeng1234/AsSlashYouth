package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.media.MediaCodec;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.PagerHomeContactsBinding;
import com.slash.youth.databinding.PullToRefreshListviewBinding;
import com.slash.youth.databinding.PullToRefreshTabListviewBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.AgreeRefundBean;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.domain.SearchServiceItemBean;
import com.slash.youth.domain.SearchUserItemBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.ui.adapter.ListViewAdapter;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.PagerSearchDemandtAdapter;
import com.slash.youth.ui.adapter.PagerHomeServiceAdapter;
import com.slash.youth.ui.adapter.PagerSearchPersonAdapter;
import com.slash.youth.ui.pager.GirdDropDownAdapter;
import com.slash.youth.ui.pager.ListDropDownAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zss on 2016/9/23.
 */
public class SearchNeedResultTabModel extends BaseObservable  {
    public SearchNeedResultTabBinding mSearchNeedResultTabBinding;
    private View areaView;
    private String demands[] = {"不限","线上","线下"};
    private String users[] = {"全部用户","认证用户"};
    private String sorts[] = {"发布时间最近（默认）","回复时间最近","价格最高","距离最近"};
    private GirdDropDownAdapter  demandAdapter;
    private ListDropDownAdapter userAdapter;
    private ListView userListView;
    private String searchType;
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private ListDropDownAdapter sexAdapter;
    private ArrayList<Character> listCityNameFirstLetter;
    private TextView tvFirstLetter;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private ArrayList<LocationCityInfo> listCityInfo = new ArrayList<>();
    private SearchActivityCityLocationBinding searchCityLocationBinding;
    private  ArrayList<String> headerLists = new ArrayList<>();
    private String[] demadHeaders ={"需求类型", "用户类型", "苏州", "排序"};
    private String[] serviceHeaders ={"需求类型", "苏州", "排序"};
    private String[] personHeaders ={"认证"};
    private String[] headers;
    private ListView demandView;
    private ListView sortListView;
    private PullToRefreshTabListviewBinding pullToRefreshTabListviewBinding;
    private PullToRefreshListTabViewModel pullToRefreshListTabViewModel;

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        initData();
        addView();
        back();
    }

    private void initData() {
        searchType = SpUtils.getString("searchType", "");
        switch (searchType){
            case SearchManager.HOT_SEARCH_DEMEND:
                headers =demadHeaders;
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                headers =serviceHeaders;
                break;
            case SearchManager.HOT_SEARCH_PERSON:
                headers =personHeaders;
                break;
        }
    }

    private List<View> popupViews = new ArrayList<>();
    private View contentView;
    private void addView() {
        if (demandView == null) {
            demandView = new ListView(currentActivity);
            demandAdapter = new GirdDropDownAdapter(currentActivity, Arrays.asList(demands));
            demandView.setDividerHeight(0);
            demandView.setAdapter(demandAdapter);
        }

        if(SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_DEMEND) ){
            if(userListView == null){
                userListView = new ListView(currentActivity);
                userListView.setDividerHeight(0);
                userAdapter = new ListDropDownAdapter(currentActivity, Arrays.asList(users));
                userListView.setAdapter(userAdapter);
            }
        }

        if(!SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_PERSON)){
            //地区
            if(searchCityLocationBinding==null){
                searchCityLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.search_activity_city_location, null, false);
                areaView = searchCityLocationBinding.getRoot();
                setSearchArea(areaView);
            }
        }

        if(!SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_PERSON)) {
            if (sortListView == null) {
                sortListView = new ListView(currentActivity);
                sortListView.setDividerHeight(0);
                sexAdapter = new ListDropDownAdapter(currentActivity, Arrays.asList(sorts));
                sortListView.setAdapter(sexAdapter);
            }
        }

        popupViews.add(demandView);
        if(SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_DEMEND)){
            popupViews.add(userListView);
        }
        if(!SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_PERSON)){
            popupViews.add(areaView);
            popupViews.add(sortListView);
        }

        pullToRefreshTabListviewBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pull_to_refresh_tab_listview, null, false);
        pullToRefreshListTabViewModel = new PullToRefreshListTabViewModel(pullToRefreshTabListviewBinding,searchType,currentActivity);
        pullToRefreshTabListviewBinding.setPullToRefreshListTabViewModel(pullToRefreshListTabViewModel);
        contentView = pullToRefreshTabListviewBinding.getRoot();
        contentView.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(10),CommonUtils.dip2px(10),0);

        demandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                demandAdapter.setCheckItem(position);
                mSearchNeedResultTabBinding.dropDownMenu.setTabText(position == 0 ? headers[0] : demands[position]);
                mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
                switch (position){
                    case 0:
                        pullToRefreshListTabViewModel.pattern = -1;
                        break;
                    case 1:
                        pullToRefreshListTabViewModel.pattern = 0;
                        break;
                    case 2:
                        pullToRefreshListTabViewModel.pattern = 1;
                        break;
                }
                pullToRefreshListTabViewModel.getData(searchType);
            }
        });

        if(SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_DEMEND)){
            userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    userAdapter.setCheckItem(position);
                    mSearchNeedResultTabBinding.dropDownMenu.setTabText(position == 0 ? headers[1] : users[position]);
                    mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
                    switch (position){
                        case 0:
                            pullToRefreshListTabViewModel.isauth = -1;
                            break;
                        case 1:
                            pullToRefreshListTabViewModel.isauth = 1;
                            break;
                    }
                    pullToRefreshListTabViewModel.getData(searchType);
                }
            });
        }

        if(!SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_PERSON)){
            sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    sexAdapter.setCheckItem(position);
                    mSearchNeedResultTabBinding.dropDownMenu.setTabText(position == 0 ? headers[2] : sorts[position]);
                    mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
                    switch (position){
                        case 0:
                            pullToRefreshListTabViewModel.sort = -1;
                            break;
                        case 1:
                            pullToRefreshListTabViewModel.sort = 2;
                            break;
                        case 2:
                            pullToRefreshListTabViewModel.sort = 1;
                            break;
                        case 3:
                            pullToRefreshListTabViewModel.sort = 3;
                            break;
                    }
                    pullToRefreshListTabViewModel.getData(searchType);
                }
            });
        }

        for (String header : headers) {
            headerLists.add(header);
        }
        mSearchNeedResultTabBinding.dropDownMenu.setDropDownMenu(headerLists, popupViews, contentView);


        if(SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_PERSON)){
            mSearchNeedResultTabBinding.dropDownMenu.addTabView("影响力");
            mSearchNeedResultTabBinding.dropDownMenu.isAdd = true;
        }

    }


    //设置地区
    private void setSearchArea( View view) {
        SearchActivityCityLocationModel searchActivityCityLocationModel = new SearchActivityCityLocationModel(searchCityLocationBinding,currentActivity);
        searchCityLocationBinding.setSearchActivityCityLocationModel(searchActivityCityLocationModel);

        headerListviewLocationCityInfoListBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()),R.layout.header_listview_location_city_info_list,null,false);
        // HeaderLocationCityInfoModel headerLocationCityInfoModel = new HeaderLocationCityInfoModel(headerListviewLocationCityInfoListBinding);
        //headerListviewLocationCityInfoListBinding.setHeaderLocationCityInfoModel(headerLocationCityInfoModel);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.addHeaderView(headerListviewLocationCityInfoListBinding.getRoot());

        ImageView ivLocationCityFirstLetterListHeader = new ImageView(CommonUtils.getContext());
        ivLocationCityFirstLetterListHeader.setImageResource(R.mipmap.search_letter_search_icon);
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.addHeaderView(ivLocationCityFirstLetterListHeader);

        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setVerticalScrollBarEnabled(false);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.setVerticalScrollBarEnabled(false);

        setCityLinitListener();
    }

    private int cityPosition = 0;
    private Handler mHanler = new Handler();
    //设置城市监听事件
    @TargetApi(Build.VERSION_CODES.M)
    private void setCityLinitListener() {
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv= (TextView) view;
//                ToastUtils.shortToast(position + " "+tv.getText());
//                int clickIndex = position - mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.getHeaderViewsCount();
                if (position > 0) {
                    tvFirstLetter = (TextView) view;
                    //TODO 一点击字就会变成一种颜色，滑动触摸也会变化
                    // cityPosition = position;
                    // tvFirstLetter.setTextColor(Color.BLUE);
                    // tvFirstLetter.setBackgroundResource(R.drawable.shape_home_freetime_searchbox_bg);
                    String firstLetter = tvFirstLetter.getText().toString();
                    if (mHashFirstLetterIndex.containsKey(firstLetter)) {
                        Integer firstLetterIndex = mHashFirstLetterIndex.get(firstLetter);
//                    ToastUtils.shortToast(firstLetterIndex + "");
                        searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(firstLetterIndex + searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                    }
                }
            }
        });

        //触摸事件监听
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnTouchListener(new View.OnTouchListener() {
            private String key;
            private float y = 0;
            private int index = -1;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                TextView childAt = (TextView) ((ListView) v).getChildAt(2);
                int height = childAt.getMeasuredHeight();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y = event.getY();
                        index = (int) (y /height);
                        if (index >= 0 && index < listCityNameFirstLetter.size()) {
                            String firstDownLetter = listCityNameFirstLetter.get(index).toString();
                            LogKit.d("firstDownLetter = " + firstDownLetter);
                            if (mHashFirstLetterIndex.containsKey(firstDownLetter)) {
                                showTextView(firstDownLetter);
                                Integer firstLetterIndex = mHashFirstLetterIndex.get(firstDownLetter);
                                searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(firstLetterIndex + searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        y = event.getY();
                        index = (int) (y / height);
                        if (index >= 0 && index < listCityNameFirstLetter.size()) {
                            String firstMoveLetter = listCityNameFirstLetter.get(index).toString();
                            LogKit.d("firstMoveLetter = "+firstMoveLetter);
                            if (mHashFirstLetterIndex.containsKey(firstMoveLetter)) {
                                showTextView(firstMoveLetter);
                                Integer firstLetterIndex = mHashFirstLetterIndex.get(firstMoveLetter);
                                searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(firstLetterIndex + searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        y = event.getY();
                        index = (int) (y / height);
                        if (index >= 0 && index < listCityNameFirstLetter.size()) {
                            String firstUpLetter = listCityNameFirstLetter.get(index).toString();
                            LogKit.d("firstUpLetter = " + firstUpLetter);
                            if (mHashFirstLetterIndex.containsKey(firstUpLetter)) {
                                showTextView(firstUpLetter);
                                Integer firstLetterIndex = mHashFirstLetterIndex.get(firstUpLetter);
                                searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(firstLetterIndex + searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                            }
                        }
                        break;
                }
                return true;
            }
        });

        //实现点击当前城市事件
        headerListviewLocationCityInfoListBinding.tvCurrentCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curentyCityText = (String) headerListviewLocationCityInfoListBinding.tvCurrentCity.getText();
               // ((TextView)searchTabView.findViewById(R.id.tv_area)).setText(curentyCityText);
                //switchSearchTab(R.id.rl_tab_area,false);
                //  clickSearchTab(R.id.fl_showSearchResult);
            }
        });
        //点击下面的地址，赋值给当前定位，并且保存在最近访问，最多3个
        searchCityLocationBinding.lvActivityCityLocationCityinfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    LocationCityInfo locationCityInfo = listCityInfo.get(position-1);
                    String cityName = locationCityInfo.getCityName();
                    if(!cityName.isEmpty()){
                        headerListviewLocationCityInfoListBinding.tvCurrentCity.setText(cityName);
                        //保存到最近访问
                        //TODO 可以做,先做技能标签


                    }
                }
            }
        });
    }
    //显示中间的字
    private void showTextView(String text ) {
        searchCityLocationBinding.tv.setVisibility(View.VISIBLE);
        searchCityLocationBinding.tv.setText(text);
        mHanler.removeCallbacksAndMessages(null);
        mHanler.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchCityLocationBinding.tv.setVisibility(View.GONE);
            }
        },1000);

    }

    //返回
    private void back() {
        currentActivity.setOnBackClickListener(new SearchActivity.OnBacklickCListener() {
            @Override
            public void OnBackClick() {
                if ( mSearchNeedResultTabBinding.dropDownMenu.isShowing()) {
                    mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
                }
                mSearchNeedResultTabBinding.dropDownMenu.removeView();
            }
        });
    }

}




