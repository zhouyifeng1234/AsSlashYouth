package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFirstPagerMoreBinding;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.PullToRefreshListviewBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.adapter.GirdDropDownAdapter;
import com.slash.youth.ui.pager.ListDropDownAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

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
    private String sorts[] = {"发布时间最近（默认）","回复时间最近","价格最高","距离最近"};
    private String[] demadHeaders;
    private List<View> popupViews = new ArrayList<>();
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private GirdDropDownAdapter demandAdapter;
    private ListDropDownAdapter userAdapter;
    private ListDropDownAdapter sexAdapter;
    private ListView userView;
    private View contentView;
    private String tag = "";
    private SearchActivityCityLocationBinding searchCityLocationBinding;
    private int constellationPosition = 0;
    private View constellationView;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private PullToRefreshListviewBinding pullToRefreshListviewBinding;
    private PullToRefreshListViewModel pullToRefreshListViewModel;
    private SearchActivityCityLocationModel searchActivityCityLocationModel;

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
            demadHeaders =new String[]{"需求类型", "用户类型", "苏州", "排序"};
        }else {
            demadHeaders =new String[]{"需求类型", "苏州", "排序"};
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
                            pullToRefreshListViewModel.isauth = -1;
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
                activityFirstPagerMoreBinding.dropDownMenu.setTabText(position == 0 ? demadHeaders[2] : sorts[position]);
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

    private TextView tvFirstLetter;
    private void setCityLinitListener() {
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv= (TextView) view;
//                ToastUtils.shortToast(position + " "+tv.getText());
//                int clickIndex = position - mActivityCityLocationBinding.lvActivityCityLocationCityFirstletter.getHeaderViewsCount();
                if (position > 0) {
                    tvFirstLetter = (TextView) view;
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
        searchActivityCityLocationModel.setOnClickListener(new SearchActivityCityLocationModel.onClickCListener() {
            @Override
            public void OnClick(String cityName) {
                pullToRefreshListViewModel.city = cityName;
                pullToRefreshListViewModel.getData(isDemand);
                activityFirstPagerMoreBinding.dropDownMenu.setCurrentTabText(isDemand?4:2,cityName);
                activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
            }
        });
    }

}
