package com.slash.youth.ui.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.os.Handler;
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
import com.slash.youth.databinding.ActivityFirstPagerMoreBinding;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.PullToRefreshListviewBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.adapter.GirdDropDownAdapter;
import com.slash.youth.ui.adapter.ListDropDownAdapter;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ZSS on 2016/12/9.
 */
public class FirstPagerDemandModel extends BaseObservable {
    private ActivityFirstPagerMoreBinding activityFirstPagerMoreBinding;
    private FirstPagerMoreActivity firstPagerMoreActivity;
    private boolean isDemand = true;
    private String demands[] = {"不限","线上","线下"};
    private String users[] = {"全部用户","认证用户"};
   // private String sorts[] = {"综合评价最高（默认）","最新发布","离我最近"};
    private String sorts[];
   // private String demadsorts[] = {"最新发布(默认)","回复时间最近","价格最高","离我最近"};
    //private String servicesorts[] = {"综合性评价最高（默认）","最新发布","离我最近"};
    private String[] demadHeaders;
    private List<View> popupViews = new ArrayList<>();
    private GirdDropDownAdapter demandAdapter;
    private ListDropDownAdapter userAdapter;
    private ListDropDownAdapter sexAdapter;
    private ListView userView;
    private View contentView;
    private String tag = "";
    public  SearchActivityCityLocationBinding searchCityLocationBinding;
    private View constellationView;
    private PullToRefreshListviewBinding pullToRefreshListviewBinding;
    private PullToRefreshListViewModel pullToRefreshListViewModel;
    private SearchActivityCityLocationModel searchActivityCityLocationModel;
    private int heardHeight;
    private int height;
    private int itemHeight;
    private int startHeight;
    private int startY;
    private int index;
    private int startIndex;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private HeaderLocationCityInfoModel headerLocationCityInfoModel;

    public FirstPagerDemandModel(ActivityFirstPagerMoreBinding activityFirstPagerMoreBinding, boolean isDemand,FirstPagerMoreActivity firstPagerMoreActivity) {
            this.activityFirstPagerMoreBinding = activityFirstPagerMoreBinding;
            this.isDemand = isDemand;
            this.firstPagerMoreActivity = firstPagerMoreActivity;
        initData();
        initView();
        listener();
    }

    private void initData() {
        if(isDemand){
            demadHeaders =new String[]{"需求方式", "用户类型", "全国", "排序"};
            sorts = new String[]{"最新发布(默认)","回复时间最近","价格最高","离我最近"};
        }else {
            demadHeaders =new String[]{"服务方式", "全国", "排序"};
            sorts = new String[]{"综合性评价最高（默认）","最新发布","离我最近"};
        }
    }

    private void initView() {
        final ListView demandView = new ListView(firstPagerMoreActivity);
        demandAdapter = new GirdDropDownAdapter(firstPagerMoreActivity, Arrays.asList(demands));
        demandView.setDividerHeight(0);
        demandView.setAdapter(demandAdapter);

        if(isDemand){
            userView = new ListView(firstPagerMoreActivity);
            userView.setDividerHeight(0);
            userAdapter = new ListDropDownAdapter(firstPagerMoreActivity, Arrays.asList(users));
            userView.setAdapter(userAdapter);
        }

       searchCityLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.search_activity_city_location, null, false);
        constellationView = searchCityLocationBinding.getRoot();
        setSearchArea(constellationView);

        final ListView sortView = new ListView(firstPagerMoreActivity);
        sortView.setDividerHeight(0);
        sexAdapter = new ListDropDownAdapter(firstPagerMoreActivity, Arrays.asList(sorts));
        sortView.setAdapter(sexAdapter);

        //init popupViews
        popupViews.add(demandView);
        if(isDemand){
            popupViews.add(userView);
        }
        popupViews.add(constellationView);
        popupViews.add(sortView);

        pullToRefreshListviewBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pull_to_refresh_listview, null, false);
        pullToRefreshListViewModel = new PullToRefreshListViewModel(pullToRefreshListviewBinding ,isDemand,firstPagerMoreActivity);
        pullToRefreshListviewBinding.setPullToRefreshListViewModel(pullToRefreshListViewModel);
        contentView = pullToRefreshListviewBinding.getRoot();
        contentView.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(10),CommonUtils.dip2px(10),0);

        //add item click event
        demandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                demandAdapter.setCheckItem(position);
                activityFirstPagerMoreBinding.dropDownMenu.setTabText(position == 0 ? demadHeaders[0] : demands[position]);
                activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
                switch (position){
                    case 0:
                        pullToRefreshListViewModel.pattern = -1;
                        break;
                    case 1:
                        pullToRefreshListViewModel.pattern = 0;
                        break;
                    case 2:
                        pullToRefreshListViewModel.pattern = 1;
                        break;
                }
                pullToRefreshListViewModel.getData(isDemand);
            }
        });

        if(isDemand){
            userView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    userAdapter.setCheckItem(position);
                    activityFirstPagerMoreBinding.dropDownMenu.setTabText(position == 0 ? demadHeaders[1] : users[position]);
                    activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
                    switch (position){
                        case 0:
                            pullToRefreshListViewModel.isauth = 0;
                            break;
                        case 1:
                            pullToRefreshListViewModel.isauth = 1;
                            break;
                    }
                    pullToRefreshListViewModel. getData(isDemand);
                }
            });
        }

        sortView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sexAdapter.setCheckItem(position);
                if(isDemand){
                    activityFirstPagerMoreBinding.dropDownMenu.setTabText(position == 0 ? demadHeaders[3] : sorts[position]);
                }else {
                    activityFirstPagerMoreBinding.dropDownMenu.setTabText(position == 0 ? demadHeaders[2] : sorts[position]);
                }

                activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
                switch (position){
                    case 0:
                        pullToRefreshListViewModel.sort = -1;
                        break;
                    case 1:
                        pullToRefreshListViewModel.sort = 2;
                        break;
                    case 2:
                        pullToRefreshListViewModel.sort = 1;
                        break;
                    case 3:
                        pullToRefreshListViewModel.sort = 3;
                        break;
                }
                pullToRefreshListViewModel.getData(isDemand);
            }
        });

        //init dropdownview
        activityFirstPagerMoreBinding.dropDownMenu.setDropDownMenu(Arrays.asList(demadHeaders), popupViews, contentView);
    }

    //设置地区
    private void setSearchArea( View view) {
        searchActivityCityLocationModel = new SearchActivityCityLocationModel(searchCityLocationBinding,firstPagerMoreActivity);
        searchCityLocationBinding.setSearchActivityCityLocationModel(searchActivityCityLocationModel);

        Intent intent = firstPagerMoreActivity.getIntent();
        intent.putExtra("locationType",0);
        headerListviewLocationCityInfoListBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.header_listview_location_city_info_list, null, false);
        headerLocationCityInfoModel = new HeaderLocationCityInfoModel(headerListviewLocationCityInfoListBinding,intent,firstPagerMoreActivity);
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

    private TextView tvFirstLetter;
    private Handler mHanler = new Handler();
    private void setCityLinitListener() {
        //触摸事件
        ListAdapter adapter =   searchCityLocationBinding.lvActivityCityLocationCityFirstletter.getAdapter();
        if (adapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = adapter.getView(i, null,  searchCityLocationBinding.lvActivityCityLocationCityFirstletter);
            if(listItem!=null){
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }
        //总长
        height = totalHeight - heardHeight;
        //每一个字母的长度
        itemHeight = height / 26;

        //从A开的距离
        WindowManager wm = (WindowManager) CommonUtils.getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        int phoneHeight = wm.getDefaultDisplay().getHeight();

        int titleHeight = FirstPagerMoreActivity.barHeight;

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
                                    searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i+ searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
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
                            for (int i = 0; i <  searchActivityCityLocationModel.listCityInfo.size(); i++) {
                                LocationCityInfo locationCityInfo =  searchActivityCityLocationModel.listCityInfo.get(i);
                                String firstCityLetter = locationCityInfo.getFirstLetter();
                                if(firstCityLetter.equals(firstMoveLetter) ){
                                    searchCityLocationBinding.lvActivityCityLocationCityinfo.setSelection(i+ searchCityLocationBinding.lvActivityCityLocationCityinfo.getHeaderViewsCount());
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

    //返回
    public void back(View view){
        //退出activity前关闭菜单
        close();
    }

    private void close() {
        if ( activityFirstPagerMoreBinding.dropDownMenu.isShowing()) {
            activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
        }
        firstPagerMoreActivity.finish();
    }

    private void listener() {
        //点击条目，获取的城市
        searchActivityCityLocationModel.setOnClickListener(new SearchActivityCityLocationModel.onClickCListener() {
            @Override
            public void OnClick(String cityName) {
                setCurrentyCity(cityName);
                headerLocationCityInfoModel.saveCity(cityName);
            }
        });
        //点击定位当前城市
        headerLocationCityInfoModel.setOnCityClickCListener(new HeaderLocationCityInfoModel.OnCityClickCListener() {
            @Override
            public void OnSearchCityClick(String city) {

            }

            @Override
            public void OnMoreCityClick(String city) {
                setCurrentyCity(city);
            }
        });
    }

    public void setCurrentyCity(String cityName) {
        pullToRefreshListViewModel.city = cityName;
        pullToRefreshListViewModel.getData(isDemand);
        activityFirstPagerMoreBinding.dropDownMenu.setCurrentTabText(isDemand?4:2,cityName);
        activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
    }

}
