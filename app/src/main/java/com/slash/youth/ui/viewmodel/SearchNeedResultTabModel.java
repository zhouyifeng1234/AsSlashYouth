package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.view.View;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SearchPersonBean;
import com.slash.youth.ui.adapter.PagerHomeDemandtAdapter;
import com.slash.youth.ui.adapter.PagerSearchPersonAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zss on 2016/9/23.
 */
public class SearchNeedResultTabModel extends BaseObservable implements View.OnClickListener {

    public SearchNeedResultTabBinding mSearchNeedResultTabBinding;
    //zss 指示器的点击事件
    private boolean isClickTime = false;
    private boolean isClickStar = false;
    private boolean isClickHot = false;
    private View lineView;
    private View userView;
    private View sureView;

    private PagerHomeDemandtAdapter pagerHomeDemandtAdapter;
    private ArrayList<DemandBean> listDemand;
    private ArrayList<SearchPersonBean> listPerson;
    private PagerSearchPersonAdapter pagerSearchPersonAdapter;

    public SearchNeedResultTabModel(SearchNeedResultTabBinding mSearchNeedResultTabBinding) {
        this.mSearchNeedResultTabBinding = mSearchNeedResultTabBinding;
        initView();
        initListener();
        initData();
    }

    private void initView() {
        String searchType = SpUtils.getString("searchType", "");
        if (searchType.equals("searchPerson")) {
            mSearchNeedResultTabBinding.llSearchTab2.setVisibility(View.VISIBLE);
            mSearchNeedResultTabBinding.llSearchTabHead.setVisibility(View.GONE);
            mSearchNeedResultTabBinding.lvShowSearchResult.setVisibility(View.GONE);
            mSearchNeedResultTabBinding.lvShowSearchPerson.setVisibility(View.VISIBLE);
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
        mSearchNeedResultTabBinding.rlTabTime.setOnClickListener(this);
        mSearchNeedResultTabBinding.rlTabHuoyuedu.setOnClickListener(this);
        mSearchNeedResultTabBinding.rlTabRenzheng.setOnClickListener(this);
        mSearchNeedResultTabBinding.rlTabXingji.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //认证
            case R.id.rl_tab_renzheng:
            //线上用户
            case R.id.rl_tab_line:
            //认证用户
            case R.id.rl_tab_user:
                clickSearchTab(v.getId());
                break;
            //星级
            case R.id.rl_tab_xingji:
                isClickStar = !isClickStar;
                setImageView(isClickStar, v, R.id.iv_star, R.mipmap.shang_icon, R.mipmap.xia);

                mSearchNeedResultTabBinding.ivHot.setImageResource(R.mipmap.xia);
                isClickHot = false;
                //判断上下箭头
                if (isClickStar) {//
                   // LogKit.d("向上排序");//上
                    updateUpData();
                } else {
                   // LogKit.d("向下排序");//下
                    updateDownData();
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据
                clickSearchTab(v.getId());
                break;

            //活跃度
            case R.id.rl_tab_huoyuedu:
                isClickHot = !isClickHot;
                setImageView(isClickHot, v, R.id.iv_hot, R.mipmap.shang_icon, R.mipmap.xia);
                mSearchNeedResultTabBinding.ivStar.setImageResource(R.mipmap.xia);
                isClickStar = false;
                if (isClickHot) {//
                  //  LogKit.d("向上排序");//上
                    updateUpData();
                } else {
                  //  LogKit.d("向下排序");//下
                    updateDownData();
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据
                clickSearchTab(v.getId());
                break;
            //时间
            case R.id.rl_tab_time:
                isClickTime = !isClickTime;
                setImageView(isClickTime, v, R.id.iv_time_icon, R.mipmap.shang_icon, R.mipmap.xia);
                //判断箭头向上和向下，数据不同,布局一样的，根据接口文档不同的排序方式，更新数据

                if (isClickTime) {//
                   // LogKit.d("向上排序");//上
//                    updateUpData();
                    sortByTime(true);
                } else {
                  //  LogKit.d("向下排序");//下
                    sortByTime(false);
//                    updateDownData();
                }
                pagerHomeDemandtAdapter.notifyDataSetChanged();//更新数据
                clickSearchTab(v.getId());
                break;
        }
    }

    private int searchTabId =-1;//当前id
    private boolean isSearchTabOn = false;//当前searchTab 打开

    private void clickSearchTab(int id ) {
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
            case R.id.rl_tab_renzheng:
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
                break;
            case R.id.rl_tab_user:
                mSearchNeedResultTabBinding.ivUserIcon.setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
                break;
            case R.id.rl_tab_renzheng:
                mSearchNeedResultTabBinding.ivSure.setImageResource(isOn?R.mipmap.free_pay_jihuo: R.mipmap.free_play);
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
                return lineView;
            case R.id.rl_tab_user:
                if (userView == null) {
                    userView = View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_user, null);
                }
                return userView;
            case R.id.rl_tab_renzheng:
                if(sureView==null){
                    sureView=View.inflate(CommonUtils.getContext(), R.layout.search_result_tab_sure, null);
                }
                return sureView;
            default:
                return null;
        }
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
    }

}
