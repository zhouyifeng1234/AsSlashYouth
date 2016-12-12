package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.media.MediaCodec;
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
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.AgreeRefundBean;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.domain.SearchItemDemandBean;
import com.slash.youth.domain.SearchServiceItemBean;
import com.slash.youth.domain.SearchUserItemBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.SearchActivity;
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
public class SearchNeedResultTabModel extends BaseObservable implements View.OnClickListener, AdapterView.OnItemClickListener {
    public SearchNeedResultTabBinding mSearchNeedResultTabBinding;
    private View lineView;
    private View userView;
    private View sortView;
    private View areaView;
    private View sureView;
    private boolean isClickStar = false;
    private boolean isClickHot = false;
    private PagerSearchDemandtAdapter pagerHomeDemandtAdapter;
    private ArrayList<SearchItemDemandBean.DataBean.ListBean>  arrayListDemand = new ArrayList<>();
    private ArrayList<SearchServiceItemBean.DataBean.ListBean>  arrayListService = new ArrayList<>();
    private ArrayList<SearchUserItemBean.DataBean.ListBean> arryListUser  = new ArrayList<>();
    private PagerSearchPersonAdapter pagerSearchPersonAdapter;
    private ListView lv;
    private String cityName = "苏州";
    private ArrayList<String> arrayList = new ArrayList<String>();
    private String[] lineText = {"不限","线上","线下"};
    private String[] userText = {"全部用户","认证用户"};
    private String[] sureText = {"所有用户","认证用户","非认证用户"};
    private String[] sortText = {"发布时间最近（默认）","回复时间最近","价格最高","距离最近"};
    private String[] searchSortText = {"综合评价最高（默认）","发布时间最近","距离最近"};
    private String[] areaText = { "全苏州", "工业园区", "吴中区","姑苏区","相城区","高新区",
            "姑苏区", "工业园区", "张家港市", "昆山市" };

    private String demands[] = {"不限","线上","线下"};
    private String users[] = {"全部用户","认证用户"};
    private String sorts[] = {"发布时间最近（默认）","回复时间最近","价格最高","距离最近"};
    private GirdDropDownAdapter  demandAdapter;
    public static  int TYPE;
    private ListViewAdapter listViewAdapter;
    private ListDropDownAdapter userAdapter;
    private ListView userListView;
    private String title;
    private String searchType;
    private View userTabView;
    private View searchTabView;
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private View publicView;
    private ArrayList<Character> listCityNameFirstLetter;
    private TextView tvFirstLetter;
    private LocationCityFirstLetterAdapter locationCityFirstLetterAdapter;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private ArrayList<LocationCityInfo> listCityInfo = new ArrayList<>();
    private SearchActivityCityLocationBinding searchCityLocationBinding;
    private String[] headers;
    private  int offset = 0;
    private int limit = 20;
    String tag = "微信";
    private int  pattern = -1;
   private  int isauth = -1;
    String city = null;
    private int sort = -1;
    private int hotSort = 0;
    double lat = 181;//-180 到 180
    double lng = 91;//-90 到 90

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        initView();
        back();
        //initData();
        //addView();
    }

    private void initData() {
        searchType = SpUtils.getString("searchType", "");

        switch (searchType){
            case SearchManager.HOT_SEARCH_DEMEND:
                headers =new String[]{"需求类型", "用户类型", "苏州", "排序"};
                SearchManager.getSearchDemandList(new onGetSearchDemandList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                headers =new String[]{"需求类型", "苏州", "排序"};
                SearchManager.getSearchServiceList(new onGetSearchServiceList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case SearchManager.HOT_SEARCH_PERSON:
                headers =new String[]{"认证",  "影响力"};
                SearchManager.getSearchUserList(new onGetSearchUserList(),tag,isauth,sort, offset,limit);
                break;
        }
    }

    private void addView() {
        final ListView demandView = new ListView(currentActivity);
        demandAdapter = new GirdDropDownAdapter(currentActivity, Arrays.asList(demands));
        demandView.setDividerHeight(0);
        demandView.setAdapter(demandAdapter);

        if(SpUtils.getString("searchType", "") == SearchManager.HOT_SEARCH_DEMEND){
            userListView = new ListView(currentActivity);
            userListView.setDividerHeight(0);
            userAdapter = new ListDropDownAdapter(currentActivity, Arrays.asList(users));
            userListView.setAdapter(userAdapter);
        }
    }

    private void initView() {
       searchType = SpUtils.getString("searchType", "");
        mSearchNeedResultTabBinding.FlTab.removeAllViews();

        switch (searchType){
            case SearchManager.HOT_SEARCH_DEMEND:
                LayoutInflater demandInflater = LayoutInflater.from(CommonUtils.getApplication());
                searchTabView = demandInflater.inflate(R.layout.search_tab_demand, null);
                mSearchNeedResultTabBinding.FlTab.addView(searchTabView);

                searchTabView.findViewById(R.id.rl_tab_line).setOnClickListener(this);
                searchTabView.findViewById(R.id.rl_tab_sort).setOnClickListener(this);
                searchTabView.findViewById(R.id.rl_tab_area).setOnClickListener(this);
                searchTabView.findViewById(R.id.rl_tab_user).setOnClickListener(this);
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                LayoutInflater serviceInflater = LayoutInflater.from(CommonUtils.getApplication());
                searchTabView = serviceInflater.inflate(R.layout.search_tab_demand, null);
                searchTabView.findViewById(R.id.rl_tab_user).setVisibility(View.GONE);
                searchTabView.findViewById(R.id.view_user).setVisibility(View.GONE);
                mSearchNeedResultTabBinding.FlTab.addView(searchTabView);
                searchTabView.findViewById(R.id.rl_tab_line).setOnClickListener(this);
                searchTabView.findViewById(R.id.rl_tab_sort).setOnClickListener(this);
                searchTabView.findViewById(R.id.rl_tab_area).setOnClickListener(this);

                break;
            case SearchManager.HOT_SEARCH_PERSON:
                LayoutInflater userInflater = LayoutInflater.from(CommonUtils.getApplication());
                userTabView = userInflater.inflate(R.layout.search_tab_user, null);
                mSearchNeedResultTabBinding.FlTab.addView(userTabView);
                userTabView.findViewById(R.id.rl_tab_huoyuedu).setOnClickListener(this);
                userTabView.findViewById(R.id.rl_tab_sure).setOnClickListener(this);
                break;
        }

        getDataFromService();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //认证
            case R.id.rl_tab_sure:
            //线上用户
            case R.id.rl_tab_line:
            //认证用户
            case R.id.rl_tab_user:
            //排序
            case R.id.rl_tab_sort:
            //地区
            case R.id.rl_tab_area:
                break;
            case R.id.rl_tab_xingji:
                isClickStar = !isClickStar;
                setImageView(isClickStar, v, R.id.iv_star, R.mipmap.shang_icon, R.mipmap.xia);
                ((ImageView)userTabView.findViewById(R.id.iv_hot)).setImageResource(R.mipmap.xia);
                isClickHot = false;
            //判断上下箭头
                if (isClickStar) {
                    sort =1;
                } else {
                    sort =0;
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据

                break;
            //活跃度
           case R.id.rl_tab_huoyuedu:
               /* isClickHot = !isClickHot;
                setImageView(isClickHot, v, R.id.iv_hot, R.mipmap.shang_icon, R.mipmap.xia);
               ((ImageView)userTabView.findViewById(R.id.iv_star)).setImageResource(R.mipmap.xia);
                isClickStar = false;
                if (isClickHot) {
                    hotSort = 1;
                } else {
                    hotSort = 0;
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据*/
                break;
        }
        clickSearchTab(v.getId());
    }

    private int searchTabId =-1;//当前id
    private boolean isSearchTabOn = false;//当前searchTab 打开

    private void clickSearchTab(int id) {
        //关闭旧的
        if (isSearchTabOn) {
            mSearchNeedResultTabBinding.flShowSearchResult.removeView(getShowView(searchTabId));
            switchSearchTab(searchTabId,false);
            isSearchTabOn=false;
            if(id==searchTabId){
                return;
            }
        }
        switch (id){
            case R.id.rl_tab_sure:
            case R.id.rl_tab_line:
            case R.id.rl_tab_user:
            case R.id.rl_tab_area:
            case R.id.rl_tab_sort:
                break;
            default:
                return;//点击了其他按钮,直接结束，不打开新的
        }
        //打开新的
        mSearchNeedResultTabBinding.flShowSearchResult.addView(getShowView(id));
        switchSearchTab(id,true);
        searchTabId=id;
        isSearchTabOn=true;
        return;
    }

        //指示器
    private void  switchSearchTab( int id ,boolean isOn){
        switch (id) {
            case R.id.rl_tab_line:
                ((ImageView)searchTabView.findViewById(R.id.iv_line_icon)).setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                ((TextView)searchTabView.findViewById(R.id.tv_line)).setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_user:
               ((ImageView)searchTabView.findViewById(R.id.iv_user_icon)).setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                ((TextView)searchTabView.findViewById(R.id.tv_user)).setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_area:
                ((ImageView)searchTabView.findViewById(R.id.iv_area_icon)).setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                ((TextView)searchTabView.findViewById(R.id.tv_area)).setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_sort:
                ((ImageView)searchTabView.findViewById(R.id.iv_time_icon)).setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                ((TextView)searchTabView.findViewById(R.id.tv_sort)).setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_sure:
                ((ImageView)userTabView.findViewById(R.id.iv_sure)).setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                ((TextView)userTabView.findViewById(R.id.tv_sure)).setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            default:
        }
    }
    //下部视图
    private View getShowView(int id) {
        switch (id){
            case R.id.rl_tab_line:
                if (lineView == null) {
                    lineView = View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_line, null);
                }
                setSearchSelector(lineView ,lineText,linePostion);
                TYPE = 1;
                return lineView;
            case R.id.rl_tab_user:
                if (userView == null) {
                    userView = View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_line, null);
                }
                setSearchSelector(userView ,userText,userPostion);
                TYPE = 2;
                return userView;
            //排序
            case R.id.rl_tab_sort:
                if(sortView==null){
                    sortView=View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_line, null);
                }
                if(searchType.equals(SearchManager.HOT_SEARCH_SERVICE)){
                    setSearchSelector(sortView ,searchSortText,sortPostion);
                }else {
                    setSearchSelector(sortView ,sortText,sortPostion);
                }
                TYPE = 3;
                return sortView;
            //地区
            case R.id.rl_tab_area:
                if(areaView==null){
                    searchCityLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.search_activity_city_location, null, false);
                    areaView = searchCityLocationBinding.getRoot();
                    setSearchArea(areaView);
                }
                return areaView;
            case R.id.rl_tab_sure:
                if(sureView==null){
                    sureView=View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_line, null);
                }
                setSearchSelector(sureView ,sureText,surePostion);
                TYPE = 4;
                return sureView;
            default:
                return null;
        }
    }

    //填充布局
    private void setSearchSelector( View lineView , String[] str, int position) {
        lv = (ListView) lineView.findViewById(R.id.lv_search_selector);
        arrayList.clear();
        Collections.addAll(arrayList,str);
        listViewAdapter = new ListViewAdapter(arrayList,position);
       lv.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
       lv.setOnItemClickListener(this);
        publicView = lineView;
    }

    private void setImageView(boolean isclick, View v, int v1, int v2, int v3) {
        if (isclick) {
            ((ImageView) v.findViewById(v1)).setImageResource(v2);
        } else {
            ((ImageView) v.findViewById(v1)).setImageResource(v3);
        }
    }

    private int clickPostion = 0;//初始化
    private int linePostion = 0;//初始化
    private int userPostion = 0;//初始化
    private int sortPostion = 0;//初始化
    private int surePostion = 0;//初始化

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
                ((TextView)searchTabView.findViewById(R.id.tv_area)).setText(curentyCityText);
                switchSearchTab(R.id.rl_tab_area,false);
                clickSearchTab(R.id.fl_showSearchResult);
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

    //每个条目的点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        title = arrayList.get(position);
        switch(TYPE){
            case 1:
                ((TextView)searchTabView.findViewById(R.id.tv_line)).setText(title);
                linePostion = position;
                switchSearchTab(R.id.rl_tab_line,false);
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
                SearchManager.getSearchDemandList(new onGetSearchDemandList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case 2:
                ((TextView)searchTabView.findViewById(R.id.tv_user)).setText(title);
                userPostion = position;
                switchSearchTab(R.id.rl_tab_user,false);
                switch (position){
                    case 0:
                        isauth = -1;
                        break;
                    case 1:
                        isauth = 1;
                        break;
                }
                SearchManager.getSearchDemandList(new onGetSearchDemandList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case 3:
                ((TextView)searchTabView.findViewById(R.id.tv_sort)).setText(title);
                sortPostion = position;
                switchSearchTab(R.id.rl_tab_sort,false);
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
                SearchManager.getSearchDemandList(new onGetSearchDemandList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case 4:
                ((TextView)searchTabView.findViewById(R.id.tv_sure)).setText(title);
                surePostion = position;
                switchSearchTab(R.id.rl_tab_sure,false);
                switch (position){
                    case 0:
                        isauth = -1;
                        break;
                    case 1:
                        isauth = 1;
                        break;
                    case 2:
                        isauth = 0;
                        break;
                }
                break;
        }

        getDataFromService();
        clickSearchTab(R.id.fl_showSearchResult);
    }

    //请求数据
    private void getDataFromService(){
        switch (searchType){
            case SearchManager.HOT_SEARCH_DEMEND:
                SearchManager.getSearchDemandList(new onGetSearchDemandList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case SearchManager.HOT_SEARCH_SERVICE:
                SearchManager.getSearchServiceList(new onGetSearchServiceList(),tag,pattern,isauth,  city, sort,  lat,  lng,  offset,  limit);
                break;
            case SearchManager.HOT_SEARCH_PERSON:
                SearchManager.getSearchUserList(new onGetSearchUserList(),tag,isauth,sort, offset,limit);
                break;
        }
    }

    //返回
    private void back() {
        currentActivity.setOnBackClickListener(new SearchActivity.OnBacklickCListener() {
            @Override
            public void OnBackClick() {
                if(searchTabId!=0){
                mSearchNeedResultTabBinding.flShowSearchResult.removeView(getShowView(searchTabId));
                }
            }
        });
    }

    //需求
    public class onGetSearchDemandList implements BaseProtocol.IResultExecutor<SearchItemDemandBean> {
        @Override
        public void execute(SearchItemDemandBean dataBean) {
            arrayListDemand.clear();
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchItemDemandBean.DataBean data = dataBean.getData();
                List<SearchItemDemandBean.DataBean.ListBean> list = data.getList();
                arrayListDemand.addAll(list);
                pagerHomeDemandtAdapter = new PagerSearchDemandtAdapter(arrayListDemand);
                mSearchNeedResultTabBinding.lvSearchTab.setAdapter(pagerHomeDemandtAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //服务
    public class onGetSearchServiceList implements BaseProtocol.IResultExecutor<SearchServiceItemBean> {
        @Override
        public void execute(SearchServiceItemBean dataBean) {
            arrayListService.clear();
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchServiceItemBean.DataBean data = dataBean.getData();
                List<SearchServiceItemBean.DataBean.ListBean> list = data.getList();
                arrayListService.addAll(list);
                PagerHomeServiceAdapter pagerHomeServiceAdapter = new PagerHomeServiceAdapter(arrayListService);
                mSearchNeedResultTabBinding.lvSearchTab.setAdapter(pagerHomeServiceAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //搜人
    public class onGetSearchUserList implements BaseProtocol.IResultExecutor<SearchUserItemBean> {
        @Override
        public void execute(SearchUserItemBean dataBean) {
            arryListUser.clear();
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                SearchUserItemBean.DataBean data = dataBean.getData();
                List<SearchUserItemBean.DataBean.ListBean> list = data.getList();
                arryListUser.addAll(list);
                pagerSearchPersonAdapter = new PagerSearchPersonAdapter(arryListUser);
                mSearchNeedResultTabBinding.lvSearchTab.setAdapter(pagerSearchPersonAdapter);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}





