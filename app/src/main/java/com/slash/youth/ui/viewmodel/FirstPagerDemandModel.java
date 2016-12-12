package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityFirstPagerMoreBinding;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.domain.SearchServiceItemBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.adapter.PagerHomeServiceAdapter;
import com.slash.youth.ui.adapter.PagerMoreDemandtAdapter;
import com.slash.youth.ui.adapter.PagerMoreServiceAdapter;
import com.slash.youth.ui.adapter.PagerSearchDemandtAdapter;
import com.slash.youth.ui.pager.ConstellationAdapter;
import com.slash.youth.ui.pager.GirdDropDownAdapter;
import com.slash.youth.ui.pager.ListDropDownAdapter;
import com.slash.youth.ui.view.fly.RandomLayout;
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
    private ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean>  arrayListDemand = new ArrayList<>();
    private ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean>  arrayListService = new ArrayList<>();
    private PagerMoreDemandtAdapter pagerHomeDemandtAdapter;
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private GirdDropDownAdapter demandAdapter;
    private ListDropDownAdapter userAdapter;
    private ListDropDownAdapter sexAdapter;
    private ListView userView;
    private ListView contentView;
    private  int offset = 0;
    private int limit = 20;
    private String tag = "";
    private int  pattern = -1;
    private  int isauth = -1;
    String city = null;
    private int sort = -1;
    double lat = 181;//-180 到 180
    double lng = 91;//-90 到 90
    private SearchActivityCityLocationBinding searchCityLocationBinding;
    private int constellationPosition = 0;
    private View constellationView;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;

    public FirstPagerDemandModel(ActivityFirstPagerMoreBinding activityFirstPagerMoreBinding, boolean isDemand,FirstPagerMoreActivity firstPagerMoreActivity) {
            this.activityFirstPagerMoreBinding = activityFirstPagerMoreBinding;
            this.isDemand = isDemand;
            this.firstPagerMoreActivity = firstPagerMoreActivity;
        initData();
        initView();
    }

    private void initData() {
        if(isDemand){
            demadHeaders =new String[]{"需求类型", "用户类型", "苏州", "排序"};
            FirstPagerManager.onFreeTimeMoreDemandList(new onFreeTimeMoreDemandList(),pattern,  isauth,  city,  sort, lng, lat, offset,limit);
        }else {
            demadHeaders =new String[]{"需求类型", "苏州", "排序"};
            FirstPagerManager.onFreeTimeMoreServiceList(new onFreeTimeMoreServiceList(),tag,  pattern,  isauth,  city,  sort, lng, lat, offset,limit);
        }
    }

    private void getData(boolean isDemand) {
        if(isDemand){
            FirstPagerManager.onFreeTimeMoreDemandList(new onFreeTimeMoreDemandList(),pattern,  isauth,  city,  sort, lng, lat, offset,limit);
        }else {
            FirstPagerManager.onFreeTimeMoreServiceList(new onFreeTimeMoreServiceList(),tag,  pattern,  isauth,  city,  sort, lng, lat, offset,limit);
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
       // setSearchArea(constellationView);

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

        //add item click event
        demandView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                demandAdapter.setCheckItem(position);
                activityFirstPagerMoreBinding.dropDownMenu.setTabText(position == 0 ? demadHeaders[0] : demands[position]);
                activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
                switch (position){
                    case 0:
                        pattern = -1;
                        break;
                    case 1:
                        pattern = 0;
                        break;
                    case 2:
                        pattern = 1;
                        break;
                }
                getData(isDemand);
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
                            isauth = -1;
                            break;
                        case 1:
                            isauth = 1;
                            break;
                    }
                    getData(isDemand);
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
                        sort = -1;
                        break;
                    case 1:
                        sort = 2;
                        break;
                    case 2:
                        sort = 1;
                        break;
                    case 3:
                        sort = 3;
                        break;
                }
                getData(isDemand);
            }
        });

        contentView = new ListView(firstPagerMoreActivity);
        contentView.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(10),CommonUtils.dip2px(10),0);
        contentView.setDividerHeight(0);

        //init dropdownview
        activityFirstPagerMoreBinding.dropDownMenu.setDropDownMenu(Arrays.asList(demadHeaders), popupViews, contentView);
    }

    //设置地区
    private void setSearchArea( View view) {
        SearchActivityCityLocationModel searchActivityCityLocationModel = new SearchActivityCityLocationModel(searchCityLocationBinding,firstPagerMoreActivity);
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
        if ( activityFirstPagerMoreBinding.dropDownMenu.isShowing()) {
            activityFirstPagerMoreBinding.dropDownMenu.closeMenu();
        }
        firstPagerMoreActivity.finish();
    }

    //服务
    public class onFreeTimeMoreServiceList implements BaseProtocol.IResultExecutor<FreeTimeMoreServiceBean> {
        @Override
        public void execute(FreeTimeMoreServiceBean data) {
            int rescode = data.getRescode();
            if(rescode == 0){
                FreeTimeMoreServiceBean.DataBean dataBean  = data.getData();
                List<FreeTimeMoreServiceBean.DataBean.ListBean> list = dataBean.getList();
                arrayListService.addAll(list);
                PagerMoreServiceAdapter pagerHomeServiceAdapter = new PagerMoreServiceAdapter(arrayListService,firstPagerMoreActivity);
                contentView.setAdapter(pagerHomeServiceAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //需求
    public class onFreeTimeMoreDemandList implements BaseProtocol.IResultExecutor<FreeTimeMoreDemandBean> {
        @Override
        public void execute(FreeTimeMoreDemandBean data) {
            int rescode = data.getRescode();
            if(rescode == 0){
                FreeTimeMoreDemandBean.DataBean dataBean = data.getData();
                List<FreeTimeMoreDemandBean.DataBean.ListBean> list = dataBean.getList();
                arrayListDemand.addAll(list);
                pagerHomeDemandtAdapter = new PagerMoreDemandtAdapter(arrayListDemand,firstPagerMoreActivity);
                contentView.setAdapter(pagerHomeDemandtAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
