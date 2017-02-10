package com.slash.youth.ui.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.PullToRefreshTabListviewBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.gen.SearchHistoryEntityDao;
import com.slash.youth.global.SlashApplication;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.GirdDropDownAdapter;
import com.slash.youth.ui.adapter.ListDropDownAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zss on 2016/9/23.
 */
public class SearchNeedResultTabModel extends BaseObservable  {
    public SearchNeedResultTabBinding mSearchNeedResultTabBinding;
    private View areaView;
    private String demandsList[] = {"不限","线上","线下"};
    private String servicesList[] = {"不限","线上服务","线下服务"};
    private String persionList[] = {"所用用户","认证用户"};
    private String users[] = {"全部用户","认证用户"};
    private String[] sorts;
    private String serviceSorts[] = {"综合评价最高（默认）","最新发布","离我最近"};
    private String demandSorts[] = {"最新发布","价格最高","离我最近"};
    private GirdDropDownAdapter  demandAdapter;
    private ListDropDownAdapter userAdapter;
    private ListView userListView;
    private String searchType;
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private ListDropDownAdapter sexAdapter;
    private TextView tvFirstLetter;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private SearchActivityCityLocationBinding searchCityLocationBinding;
    private  ArrayList<String> headerLists = new ArrayList<>();
    private String[] demadHeaders ={"需求方式", "用户类型", "全国", "排序"};
    private String[] serviceHeaders ={"服务方式", "全国", "排序"};
    private String[] personHeaders ={"所有用户"};
    private String[] demands;
    private String[] headers;
    private ListView demandView;
    private ListView sortListView;
    private PullToRefreshTabListviewBinding pullToRefreshTabListviewBinding;
    private PullToRefreshListTabViewModel pullToRefreshListTabViewModel;
    private SearchActivityCityLocationModel searchActivityCityLocationModel;
    private boolean isDemand;
    private String tag;
    private int heardHeight;
    private int height;
    private int itemHeight;
    private int startHeight;
    private int startY;
    private int startIndex;
    private HeaderLocationCityInfoModel headerLocationCityInfoModel;
    private CityHistoryEntityDao cityHistoryEntityDao;

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding,String tag,CityHistoryEntityDao cityHistoryEntityDao) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        this.tag = tag;
        this.cityHistoryEntityDao = cityHistoryEntityDao;
        initData();
        addView();
        back();
        listener();
    }

    private void initData() {
        searchType = SpUtils.getString("searchType", "");
        switch (searchType){
            case SearchManager.HOT_SEARCH_DEMEND:
                headers =demadHeaders;
                isDemand = true;
                demands =  demandsList;
                sorts = demandSorts;
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                headers =serviceHeaders;
                isDemand = false;
                demands =  servicesList;
                sorts = serviceSorts;
                break;
            case SearchManager.HOT_SEARCH_PERSON:
                headers =personHeaders;
                demands =  persionList;
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
        pullToRefreshListTabViewModel = new PullToRefreshListTabViewModel(pullToRefreshTabListviewBinding,searchType,currentActivity,tag);
        pullToRefreshTabListviewBinding.setPullToRefreshListTabViewModel(pullToRefreshListTabViewModel);
        contentView = pullToRefreshTabListviewBinding.getRoot();
        contentView.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(10),CommonUtils.dip2px(10),0);

        demandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                demandAdapter.setCheckItem(position);
                mSearchNeedResultTabBinding.dropDownMenu.setTabText(position == 0 ? headers[0] : demands[position]);
                mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
                if(!SpUtils.getString("searchType", "").equals(SearchManager.HOT_SEARCH_PERSON)){
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
                }else {
                    switch (position){
                        case 0:
                            pullToRefreshListTabViewModel.isauth =-1;
                            break;
                        case 1:
                            pullToRefreshListTabViewModel.isauth =1;
                            break;
                    }
                }
                pullToRefreshListTabViewModel.clear();
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
                    pullToRefreshListTabViewModel.clear();
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
                    if(isDemand){
                        switch (position){
                            case 0:
                                pullToRefreshListTabViewModel.sort = 1;
                                break;
                            case 1:
                                pullToRefreshListTabViewModel.sort = 3;
                                break;
                            case 2:
                                pullToRefreshListTabViewModel.sort = 4;
                                pullToRefreshListTabViewModel.lat= SlashApplication.getCurrentLatitude();
                                pullToRefreshListTabViewModel.lng= SlashApplication.getCurrentLongitude();
                                break;
                        }

                    }else {
                        switch (position){
                            case 0:
                                pullToRefreshListTabViewModel.sort = 1;
                                break;
                            case 1:
                                pullToRefreshListTabViewModel.sort = 2;
                                break;
                            case 2:
                                pullToRefreshListTabViewModel.sort = 3;
                                pullToRefreshListTabViewModel.lat= SlashApplication.getCurrentLatitude();
                                pullToRefreshListTabViewModel.lng= SlashApplication.getCurrentLongitude();
                                break;
                        }
                    }

                    pullToRefreshListTabViewModel.clear();
                    pullToRefreshListTabViewModel.getData(searchType);
                }
            });
        }

        headerLists.clear();
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
        searchActivityCityLocationModel = new SearchActivityCityLocationModel(searchCityLocationBinding,currentActivity);
        searchCityLocationBinding.setSearchActivityCityLocationModel(searchActivityCityLocationModel);

        Intent intent = currentActivity.getIntent();
        intent.putExtra("locationType",1);
        headerListviewLocationCityInfoListBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()),R.layout.header_listview_location_city_info_list,null,false);
        headerLocationCityInfoModel = new HeaderLocationCityInfoModel(headerListviewLocationCityInfoListBinding,intent,currentActivity,cityHistoryEntityDao);
        headerListviewLocationCityInfoListBinding.setHeaderLocationCityInfoModel(headerLocationCityInfoModel);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.addHeaderView(headerListviewLocationCityInfoListBinding.getRoot());

        ImageView ivLocationCityFirstLetterListHeader = new ImageView(CommonUtils.getContext());
        ivLocationCityFirstLetterListHeader.setImageResource(R.mipmap.search_letter_search_icon);
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.addHeaderView(ivLocationCityFirstLetterListHeader);
        ivLocationCityFirstLetterListHeader.measure(0,0);
        heardHeight = ivLocationCityFirstLetterListHeader.getMeasuredHeight();

        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setVerticalScrollBarEnabled(false);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.setVerticalScrollBarEnabled(false);

        setCityLinitListener();
    }

    private int cityPosition = 0;
    private Handler mHanler = new Handler();
    //设置城市监听事件
    private void setCityLinitListener() {
        //获取listview的高度
        ListAdapter adapter =  searchCityLocationBinding.lvActivityCityLocationCityFirstletter.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null,  searchCityLocationBinding.lvActivityCityLocationCityFirstletter);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        //总长
        height = totalHeight - heardHeight;
        //每一个字母的长度
        itemHeight = height / 26;

        //从A开的距离
        WindowManager wm = (WindowManager) CommonUtils.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int phoneHeight = wm.getDefaultDisplay().getHeight();

        int titleHeight = SearchActivity.titleHeight;

        startHeight = (phoneHeight - totalHeight-titleHeight-titleHeight) / 2+titleHeight+titleHeight;
        //触摸事件
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startY = (int)event.getRawY();
                         startIndex = (startY - startHeight) / itemHeight-1;
                        if(startIndex >=0&& startIndex <26){
                            String firstStartLetter = searchActivityCityLocationModel.listCityNameFirstLetter.get(startIndex).toString();
                            searchCityLocationBinding.tv.setText(firstStartLetter);
                            searchCityLocationBinding.tv.setVisibility(View.VISIBLE);
                            mHanler.removeCallbacksAndMessages(null);
                            mHanler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchCityLocationBinding.tv.setVisibility(View.GONE);
                                }
                            },1000);
                            //左边定位到具体的城市字母开头
                            for (int i = 0; i < searchActivityCityLocationModel.listCityInfo.size(); i++) {
                                LocationCityInfo locationCityInfo = searchActivityCityLocationModel.listCityInfo.get(i);
                                String firstCityLetter = locationCityInfo.getFirstLetter();
                                if(firstCityLetter.equals(firstStartLetter) ){
                                    searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i+ searchCityLocationBinding.lvActivityCityLocationCityFirstletter.getHeaderViewsCount());
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int  rawY = (int )event.getRawY();
                        int   moveY =  rawY - startY;
                        //对应的字母
                        int count = moveY / itemHeight;
                        int moveIndex =  startIndex +count;
                        if(moveIndex>=0&&moveIndex<26){
                            String firstMoveLetter = searchActivityCityLocationModel.listCityNameFirstLetter.get(moveIndex).toString();
                            searchCityLocationBinding.tv.setText(firstMoveLetter);
                            searchCityLocationBinding.tv.setVisibility(View.VISIBLE);
                            mHanler.removeCallbacksAndMessages(null);
                            mHanler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchCityLocationBinding.tv.setVisibility(View.GONE);
                                }
                            },1000);

                            //左边定位到具体的城市字母开头
                            for (int i = 0; i < searchActivityCityLocationModel.listCityInfo.size(); i++) {
                                LocationCityInfo locationCityInfo = searchActivityCityLocationModel.listCityInfo.get(i);
                                String firstCityLetter = locationCityInfo.getFirstLetter();
                                if(firstCityLetter.equals(firstMoveLetter) ){
                                    searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i+ searchCityLocationBinding.lvActivityCityLocationCityFirstletter.getHeaderViewsCount());
                                }
                            }
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        int endY = (int)event.getRawY();
                        break;
                }
                return true;
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
                headerLists.clear();
                popupViews.clear();
            }
        });
    }

    //关闭地点选择
    private void listener() {
        //点击列表
        if(searchActivityCityLocationModel!=null){
            searchActivityCityLocationModel.setOnClickListener(new SearchActivityCityLocationModel.onClickCListener() {
                @Override
                public void OnClick(String cityName) {
                    closeCity(cityName);
                    headerLocationCityInfoModel.saveCity(cityName);
                }
            });
        }

        //点击当前定位城市
        if(headerLocationCityInfoModel!=null){
            headerLocationCityInfoModel.setOnCityClickCListener(new HeaderLocationCityInfoModel.OnCityClickCListener() {
                @Override
                public void OnSearchCityClick(String city) {
                    closeCity(city);
                }

                @Override
                public void OnMoreCityClick(String city) {
                }
            });

            headerLocationCityInfoModel.setOnAllCityClickCListener(new HeaderLocationCityInfoModel.OnAllCityClickCListener() {
                @Override
                public void OnAllCityClick() {
                    closeCity("全国");
                }
            });
        }
    }

    private void closeCity(String cityName) {
        if(cityName.endsWith("市")){
            cityName = cityName.substring(0, cityName.length()-1);
        }

        if(cityName.endsWith("区")){
            if(!cityName.endsWith("地区")){
                cityName = cityName.substring(0, cityName.length()-1);
            }
        }

        if(cityName.endsWith("县")){
            if(cityName.endsWith("自治县")){
                cityName = cityName.substring(0, cityName.length()-1);
            }
        }
        pullToRefreshListTabViewModel.clear();
        pullToRefreshListTabViewModel.city = cityName;
        pullToRefreshListTabViewModel.getData(searchType);
        mSearchNeedResultTabBinding.dropDownMenu.setCurrentTabText(isDemand?4:2,cityName);
        mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
    }
}




