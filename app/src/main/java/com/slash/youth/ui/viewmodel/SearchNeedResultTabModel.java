package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.net.sip.SipSession;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.PullToRefreshTabListviewBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.ListCityBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.GirdDropDownAdapter;
import com.slash.youth.ui.pager.ListDropDownAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.ToastUtils;

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
    private TextView tvFirstLetter;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
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
    private SearchActivityCityLocationModel searchActivityCityLocationModel;
    private boolean isDemand;
    private String tag;

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding,String tag) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        this.tag = tag;
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
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                headers =serviceHeaders;
                isDemand = false;
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
        searchActivityCityLocationModel = new SearchActivityCityLocationModel(searchCityLocationBinding,currentActivity);
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
    private void setCityLinitListener() {
        //点击字母定位城市
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    tvFirstLetter = (TextView) view;
                    String firstLetter = tvFirstLetter.getText().toString();

                    for (int i = 0; i < searchActivityCityLocationModel.listCityInfo.size(); i++) {
                        LocationCityInfo locationCityInfo = searchActivityCityLocationModel.listCityInfo.get(i);
                        String firstCityLetter = locationCityInfo.getFirstLetter();
                        if(firstCityLetter.equals(firstLetter) ){
                           // ToastUtils.shortCenterToast(" "+firstLetter);
                            searchCityLocationBinding.tv.setText(firstLetter);
                            searchCityLocationBinding.tv.setVisibility(View.VISIBLE);
                            CommonUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    searchCityLocationBinding.tv.setVisibility(View.GONE);
                                }
                            }, 2000);
                            searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i +searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
                        }
                    }
                }
            }
        });

        //触摸事件监听
       /* searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnTouchListener(new View.OnTouchListener() {
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
        });*/
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

    //关闭地点选择
    private void listener() {
        if(searchActivityCityLocationModel!=null){
            searchActivityCityLocationModel.setOnClickListener(new SearchActivityCityLocationModel.onClickCListener() {
                @Override
                public void OnClick(String cityName) {
                    pullToRefreshListTabViewModel.city = cityName;
                    pullToRefreshListTabViewModel.getData(searchType);
                    mSearchNeedResultTabBinding.dropDownMenu.setCurrentTabText(isDemand?4:2,cityName);
                    mSearchNeedResultTabBinding.dropDownMenu.closeMenu();
                }
            });
        }
    }
}




