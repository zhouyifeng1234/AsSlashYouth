package com.slash.youth.ui.viewmodel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.HeaderListviewLocationCityInfoListBinding;
import com.slash.youth.databinding.SearchActivityCityLocationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.LocationCityInfo;
import com.slash.youth.domain.SearchPersonBean;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.ListViewAdapter;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.ui.adapter.LocationCityInfoAdapter;
import com.slash.youth.ui.adapter.LocationCitySearchListAdapter;
import com.slash.youth.ui.adapter.PagerHomeDemandtAdapter;
import com.slash.youth.ui.adapter.PagerSearchPersonAdapter;
import com.slash.youth.ui.adapter.SearchCityAdapter;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.LocationCityFirstLetterHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.EventListener;
import java.util.HashMap;

/**
 * Created by zss on 2016/9/23.
 */
public class SearchNeedResultTabModel extends BaseObservable implements View.OnClickListener, AdapterView.OnItemClickListener {

    public SearchNeedResultTabBinding mSearchNeedResultTabBinding;
    //zss 指示器的点击事件
    private View lineView;
    private View userView;
    private View sortView;
    private View areaView;
    private View sureView;
    private boolean isClickStar = false;
    private boolean isClickHot = false;
    private PagerHomeDemandtAdapter pagerHomeDemandtAdapter;
    private ArrayList<DemandBean> listDemand;
    private ArrayList<SearchPersonBean> listPerson;
    private PagerSearchPersonAdapter pagerSearchPersonAdapter;
    private ListView lv;
    private String cityName = "苏州";
    private ArrayList<String> arrayList = new ArrayList<String>();
    private ArrayList<String> items = new ArrayList<>();
    private String[] lineText = {"不限","线上","线下"};
    private String[] userText = {"全部用户","认证用户"};
    private String[] sureText = {"所有用户","认证用户","非认证用户"};
    private String[] sortText = {"发布时间最近（默认）","回复时间最近","价格最高","距离最近"};
    private String[] searchSortText = {"综合评价最高（默认）","发布时间最近","距离最近"};
    private String[] areaText = { "全苏州", "工业园区", "吴中区","姑苏区","相城区","高新区",
            "姑苏区", "工业园区", "张家港市", "昆山市" };
    public static  int TYPE;
    private ListViewAdapter listViewAdapter;
    private String title;
    private TextView currentCity;
    private TextView switchCity;
    private GridView gridViewv;
    private SearchCityAdapter searchCityAdapter;
    private String locale;
    private String searchType;
    private View userTabView;
    private View searchTabView;
    private SearchActivityCityLocationBinding searchCityLocationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.search_activity_city_location, null, false);
    private HashMap<String, Integer> mHashFirstLetterIndex;
    private HeaderListviewLocationCityInfoListBinding headerListviewLocationCityInfoListBinding;
    private Activity currentActivity = CommonUtils.getCurrentActivity();
    private View publicView;
    private ArrayList<Character> listCityNameFirstLetter;
    private TextView tvFirstLetter;
    private LocationCityFirstLetterAdapter locationCityFirstLetterAdapter;
    private TextView textView;
    private ArrayList<LocationCityInfo> listCityInfo;

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        initData();
        initView();
    }

    private void initView() {
       searchType = SpUtils.getString("searchType", "");
        mSearchNeedResultTabBinding.FlTab.removeAllViews();

        if (searchType.equals("搜人")) {
            LayoutInflater inflater = LayoutInflater.from(CommonUtils.getApplication());
            userTabView = inflater.inflate(R.layout.search_tab_user, null);
            mSearchNeedResultTabBinding.FlTab.addView(userTabView);
            userTabView.findViewById(R.id.rl_tab_huoyuedu).setOnClickListener(this);
            userTabView.findViewById(R.id.rl_tab_sure).setOnClickListener(this);
            userTabView.findViewById(R.id.rl_tab_xingji).setOnClickListener(this);
            setAdapter(1);

        }else{
            LayoutInflater inflater = LayoutInflater.from(CommonUtils.getApplication());
            searchTabView = inflater.inflate(R.layout.search_tab_demand, null);
            mSearchNeedResultTabBinding.FlTab.addView(searchTabView);

            searchTabView.findViewById(R.id.rl_tab_line).setOnClickListener(this);
            searchTabView.findViewById(R.id.rl_tab_sort).setOnClickListener(this);
            searchTabView.findViewById(R.id.rl_tab_area).setOnClickListener(this);

            if(searchType.equals("热搜服务")){
                searchTabView.findViewById(R.id.rl_tab_user).setVisibility(View.GONE);
                setAdapter(2);
            }else if(searchType.equals("热搜需求")){
                searchTabView.findViewById(R.id.rl_tab_user).setVisibility(View.VISIBLE);
                searchTabView.findViewById(R.id.rl_tab_user).setOnClickListener(this);
                setAdapter(3);
            }
        }
    }

    private void setAdapter(int type) {
        switch (type){
            case 1:
                pagerSearchPersonAdapter = new PagerSearchPersonAdapter(listPerson);
                mSearchNeedResultTabBinding.lvSearchTab.setAdapter(pagerSearchPersonAdapter);
                break;
            case 2:
                pagerHomeDemandtAdapter = new PagerHomeDemandtAdapter(listDemand);
                mSearchNeedResultTabBinding.lvSearchTab.setAdapter(pagerHomeDemandtAdapter);
                break;
            case 3:
                pagerHomeDemandtAdapter = new PagerHomeDemandtAdapter(listDemand);
                mSearchNeedResultTabBinding.lvSearchTab.setAdapter(pagerHomeDemandtAdapter);
                break;
        }
    }

    //加载数据
    private void initData() {
        //设置搜索人的数据
        setSearchPersonData();
        //获取listview要展示的数据
        setSearchResultData();
    }

    //展示搜索人的数据
    private void setSearchPersonData() {
        listPerson = new ArrayList<>();
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
    }

    //获取listview要展示的数据
    private void setSearchResultData() {
        //假数据，真实数据从服务端接口获取
        listDemand = new ArrayList<DemandBean>();
        //集合里对象信息也要重新设置
        listDemand.add(new DemandBean(148123183129L));
        listDemand.add(new DemandBean(148123193130L));
        listDemand.add(new DemandBean(148123193130L));
        listDemand.add(new DemandBean(148123193130L));
        listDemand.add(new DemandBean(148123193130L));
        listDemand.add(new DemandBean(148123193130L));
        listDemand.add(new DemandBean(148123193130L));
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
                    updateUpData();
                } else {
                    updateDownData();
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据
                break;
            //活跃度
           case R.id.rl_tab_huoyuedu:
                isClickHot = !isClickHot;
                setImageView(isClickHot, v, R.id.iv_hot, R.mipmap.shang_icon, R.mipmap.xia);
               ((ImageView)userTabView.findViewById(R.id.iv_star)).setImageResource(R.mipmap.xia);
                isClickStar = false;
                if (isClickHot) {
                    updateUpData();
                } else {
                    updateDownData();
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据

                break;
        }
        clickSearchTab(v.getId());
    }

    private int searchTabId =-1;//当前id
    private boolean isSearchTabOn = false;//当前searchTab 打开

    private void
    clickSearchTab(int id) {
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
                if(searchType.equals("热搜服务")){
                    setSearchSelector(sortView ,searchSortText,sortPostion);
                }else {
                    setSearchSelector(sortView ,sortText,sortPostion);
                }
                TYPE = 3;
                return sortView;
            //地区
            case R.id.rl_tab_area:
                if(areaView==null){
                   // areaView=View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_area, null);
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
        setRemoveView();
    }

    //返回按钮监听，去除Tab的下来选项
    private void setRemoveView() {
        ((SearchActivity)currentActivity).setOnCBacklickListener(new SearchActivity.OnCBacklickListener() {
            @Override
            public void OnBackClick() {
                mSearchNeedResultTabBinding.flShowSearchResult.removeView(publicView);
            }
        });
    }

    private void setImageView(boolean isclick, View v, int v1, int v2, int v3) {
        if (isclick) {
            ((ImageView) v.findViewById(v1)).setImageResource(v2);
        } else {
            ((ImageView) v.findViewById(v1)).setImageResource(v3);
        }
    }

    private void sortByStar(boolean isUp){

    }
    private void sortByHot(boolean isUp){

    }
    private void sortByTime(final  boolean isUp){
        Collections.sort(listDemand,new Comparator<DemandBean>(){
            @Override
            public int compare(DemandBean o1, DemandBean o2) {
                return (int)(o1.lasttime-o2.lasttime)*(isUp?1:-1);
            }
        });
    }

    private int clickPostion = 0;//初始化
    private int linePostion = 0;//初始化
    private int userPostion = 0;//初始化
    private int sortPostion = 0;//初始化
    private int surePostion = 0;//初始化

    //设置地区
    private void setSearchArea( View view) {
        SearchActivityCityLocationModel searchActivityCityLocationModel = new SearchActivityCityLocationModel(searchCityLocationBinding);
        searchCityLocationBinding.setSearchActivityCityLocationModel(searchActivityCityLocationModel);

        headerListviewLocationCityInfoListBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()),R.layout.header_listview_location_city_info_list,null,false);
        HeaderLocationCityInfoModel headerLocationCityInfoModel = new HeaderLocationCityInfoModel();
        headerListviewLocationCityInfoListBinding.setHeaderLocationCityInfoModel(headerLocationCityInfoModel);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.addHeaderView(headerListviewLocationCityInfoListBinding.getRoot());

        ImageView ivLocationCityFirstLetterListHeader = new ImageView(CommonUtils.getContext());
        ivLocationCityFirstLetterListHeader.setImageResource(R.mipmap.search_letter_search_icon);
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.addHeaderView(ivLocationCityFirstLetterListHeader);

        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setVerticalScrollBarEnabled(false);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.setVerticalScrollBarEnabled(false);
        setCityData();
        setCityLinitListener();
        setRemoveView();
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
                        //TODO 可以做，但是没有好的思路，先做技能标签，在峰哥的代码中找更好的思路



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

    //设置城市数据
    private void setCityData() {
        //城市名称模拟数据
        listCityInfo = new ArrayList<LocationCityInfo>();
        /*for (char cha = 'A'; cha <= 'Z'; cha++) {
            listCityInfo.add(new LocationCityInfo(true, cha+"", ""));
            for (int i = 0; i <5 ; i++) {
                listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"+cha+"==="+i));
            }
        }*/

        listCityInfo.add(new LocationCityInfo(true, "A", ""));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(false, "", "安徽"));
        listCityInfo.add(new LocationCityInfo(false, "", "鞍钢"));
        listCityInfo.add(new LocationCityInfo(true, "S", ""));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(false, "", "上海"));
        listCityInfo.add(new LocationCityInfo(false, "", "苏州"));
        listCityInfo.add(new LocationCityInfo(true, "X", ""));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        listCityInfo.add(new LocationCityInfo(false, "", "徐州"));
        //城市名称首字母模拟数据
        listCityNameFirstLetter = new ArrayList<Character>();
        for (char cha = 'A'; cha <= 'Z'; cha++) {
            listCityNameFirstLetter.add(cha);
        }

       mHashFirstLetterIndex = new HashMap<String, Integer>();
       /* for (char cha = 'A' ; cha <= 'Z'; cha++) {
            for (int i = 0; i <26 ; i++) {
                mHashFirstLetterIndex.put(cha+"", i*6);
            }
        }*/
        mHashFirstLetterIndex.put("A", 0);
        mHashFirstLetterIndex.put("S", 13);
        mHashFirstLetterIndex.put("X", 26);
        searchCityLocationBinding.lvActivityCityLocationCityinfo.setAdapter(new LocationCityInfoAdapter(listCityInfo));
        locationCityFirstLetterAdapter = new LocationCityFirstLetterAdapter(listCityNameFirstLetter);
        searchCityLocationBinding.lvActivityCityLocationCityFirstletter.setAdapter(locationCityFirstLetterAdapter);
        //搜索的自动提示数据，实际应该由服务端返回
        //搜索自动提示测试数据
        ArrayList<String> listSearchCity = new ArrayList<String>();
        listSearchCity.add("苏州");
        listSearchCity.add("上海");
        listSearchCity.add("北京");
        searchCityLocationBinding.lvActivityCityLocationSearchList.setAdapter(new LocationCitySearchListAdapter(listSearchCity));
    }

   /* private void setSearchArea( View view) {
        currentCity = (TextView) view.findViewById(R.id.tv_currentCity);
        currentCity.setText("当前城市:" + cityName);//TODO具体的数据看返回值

        switchCity = (TextView) view.findViewById(R.id.tv_switchCity);
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转搜索城市地址的页面
                Intent intentCityLocationActivity = new Intent(CommonUtils.getContext(),  CityLocationActivity.class);
                intentCityLocationActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                CommonUtils.getContext().startActivity(intentCityLocationActivity);
            }
        });

        gridViewv = (GridView) view.findViewById(R.id.gv_city);
        getData();
        searchCityAdapter = new SearchCityAdapter(items,clickPostion);
        //配置适配器
        gridViewv.setAdapter(searchCityAdapter);
        gridViewv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                locale = items.get(position);
                ((TextView)searchTabView.findViewById(R.id.tv_area)).setText(locale);
                switchSearchTab(R.id.rl_tab_area,false);
                TextView local = (TextView) view.findViewById(R.id.tv_city);
                clickPostion = position;
                clickSearchTab(R.id.fl_showSearchResult);
            }
        });
    }*/

    //获取数据
    private void getData() {
    items.clear();
    Collections.addAll(items,areaText);
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
                break;
            case 2:
                ((TextView)searchTabView.findViewById(R.id.tv_user)).setText(title);
                userPostion = position;
                switchSearchTab(R.id.rl_tab_user,false);
                break;
            case 3:
                ((TextView)searchTabView.findViewById(R.id.tv_sort)).setText(title);
                sortPostion = position;
                switchSearchTab(R.id.rl_tab_sort,false);
                break;
            case 4:
                ((TextView)searchTabView.findViewById(R.id.tv_sure)).setText(title);
                surePostion = position;
                switchSearchTab(R.id.rl_tab_sure,false);
                break;
        }
        clickSearchTab(R.id.fl_showSearchResult);
    }


    //箭头向下排序
    private void updateDownData() {
        listDemand.clear();//添加向下的排列的数据
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
    }

    //箭头向上排序
    private void updateUpData() {
        listDemand.clear();//添加向上的排列数据
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
    }
}





