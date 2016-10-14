package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SearchPersonBean;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.ListViewAdapter;
import com.slash.youth.ui.adapter.PagerHomeDemandtAdapter;
import com.slash.youth.ui.adapter.PagerSearchPersonAdapter;
import com.slash.youth.ui.adapter.SearchCityAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    private String[] sortText = {"发布时间最近（默认）","回复时间最近","价格最高","距离最近"};
    private String[] searchSortText = {"综合性评价最高（默认）","发布时间最近","距离最近"};
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
    private TextView local;

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        initView();
        initListener();
        initData();
    }

    public SearchNeedResultTabBinding getmSearchNeedResultTabBinding() {
        return mSearchNeedResultTabBinding;
    }

    private void initView() {
       searchType = SpUtils.getString("searchType", "");

        if (searchType.equals("搜人")) {
            mSearchNeedResultTabBinding.llSearchTabHead.setVisibility(View.GONE);
            mSearchNeedResultTabBinding.lvShowSearchResult.setVisibility(View.GONE);
            mSearchNeedResultTabBinding.lvShowSearchPerson.setVisibility(View.VISIBLE);
        }

        if(searchType.equals("热搜服务")){
            mSearchNeedResultTabBinding.rlTabUser.setVisibility(View.GONE);
            mSearchNeedResultTabBinding.view.setVisibility(View.GONE);
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
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        listPerson.add(new SearchPersonBean());
        pagerSearchPersonAdapter = new PagerSearchPersonAdapter(listPerson);
        mSearchNeedResultTabBinding.lvShowSearchPerson.setAdapter(pagerSearchPersonAdapter);
    }

    //获取listview要展示的数据
    private void setSearchResultData() {
        //假数据，真实数据从服务端接口获取
        listDemand = new ArrayList<DemandBean>();
        //集合里对象信息也要重新设置
        listDemand.add(new DemandBean(148123123123L));
        listDemand.add(new DemandBean(148123133124L));
        listDemand.add(new DemandBean(148123143125L));
        listDemand.add(new DemandBean(148123153126L));
        listDemand.add(new DemandBean(148123163127L));
        listDemand.add(new DemandBean(148123173128L));
        listDemand.add(new DemandBean(148123183129L));
        listDemand.add(new DemandBean(148123193130L));
        pagerHomeDemandtAdapter = new PagerHomeDemandtAdapter(listDemand);
        mSearchNeedResultTabBinding.lvShowSearchResult.setAdapter(pagerHomeDemandtAdapter);
    }

    //监听事件
    private void initListener() {
        //设置指示器的点击事件,三个不同的指示器的监听事件
        mSearchNeedResultTabBinding.rlTabLine.setOnClickListener(this);
        mSearchNeedResultTabBinding.rlTabUser.setOnClickListener(this);
        mSearchNeedResultTabBinding.rlTabSort.setOnClickListener(this);
        mSearchNeedResultTabBinding.rlTabArea.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //认证
            //线上用户
            case R.id.rl_tab_line:
            //认证用户
            case R.id.rl_tab_user:
            //排序
            case R.id.rl_tab_sort:
            //地区
            case R.id.rl_tab_area:
                clickSearchTab(v.getId());
                break;
        }
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
                mSearchNeedResultTabBinding.ivLineIcon.setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                mSearchNeedResultTabBinding.tvLine.setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_user:
                mSearchNeedResultTabBinding.ivUserIcon.setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                mSearchNeedResultTabBinding.tvUser.setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_area:
                mSearchNeedResultTabBinding.ivAreaIcon.setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                mSearchNeedResultTabBinding.tvArea.setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
                break;
            case R.id.rl_tab_sort:
                mSearchNeedResultTabBinding.ivTimeIcon.setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                mSearchNeedResultTabBinding.tvSort.setTextColor(isOn? Color.parseColor("#31c5e4"):Color.parseColor("#333333"));
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
                    areaView=View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_area, null);
                }
                setSearchArea(areaView);
                return areaView;
            default:
                return null;
        }
    }

    //填充布局
    private void setSearchSelector(View lineView ,String[] str,int position) {
        lv = (ListView) lineView.findViewById(R.id.lv_search_selector);
        arrayList.clear();
        Collections.addAll(arrayList,str);
        listViewAdapter = new ListViewAdapter(arrayList,position);
       lv.setAdapter(listViewAdapter);
        listViewAdapter.notifyDataSetChanged();
       lv.setOnItemClickListener(this);
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

    //设置地区
    private void setSearchArea( View view) {
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
            mSearchNeedResultTabBinding.tvArea.setText(locale);
            switchSearchTab(R.id.rl_tab_area,false);
            local = (TextView) view.findViewById(R.id.tv_city);
            clickPostion = position;
            clickSearchTab(R.id.fl_showSearchResult);

            }
        });
    }

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
                mSearchNeedResultTabBinding.tvLine.setText(title);
                linePostion = position;
                switchSearchTab(R.id.rl_tab_line,false);
                break;
            case 2:
                mSearchNeedResultTabBinding.tvUser.setText(title);
                userPostion = position;
                switchSearchTab(R.id.rl_tab_user,false);
                break;
            case 3:
                mSearchNeedResultTabBinding.tvSort.setText(title);
                sortPostion = position;
                switchSearchTab(R.id.rl_tab_sort,false);
                break;
        }
        clickSearchTab(R.id.fl_showSearchResult);
    }
}
