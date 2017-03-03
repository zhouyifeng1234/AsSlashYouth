package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.PullToRefreshListviewBinding;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.HomeRecommendList2;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.FirstPagerMoreActivity;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.adapter.PagerMoreDemandtAdapter;
import com.slash.youth.ui.adapter.PagerMoreServiceAdapter;
import com.slash.youth.ui.view.PullableListView.PullToRefreshLayout;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/12/15.
 */
public class PullToRefreshListViewModel extends BaseObservable {
    private PullToRefreshListviewBinding pullToRefreshListviewBinding;
    private ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean> arrayListDemand = new ArrayList<>();
    private ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean> arrayListService = new ArrayList<>();
    private PagerMoreDemandtAdapter pagerHomeDemandtAdapter;
    private boolean isDemand;
    private int offset = 0;
    private int limit = 20;
    public String tag = "";
    public int pattern = -1;
    public int isauth = -1;
    public String city = null;
    public int sort = 0;
    public double lat = 91;
    public double lng = 181;
    private FirstPagerMoreActivity firstPagerMoreActivity;
    private int listsize;
    private SearchActivity currentActivity;
    private String searchType;
    private PagerMoreServiceAdapter pagerHomeServiceAdapter;
    public boolean isFirstInPager;

    public PullToRefreshListViewModel(PullToRefreshListviewBinding pullToRefreshListviewBinding, boolean isDemand, FirstPagerMoreActivity firstPagerMoreActivity) {
        this.pullToRefreshListviewBinding = pullToRefreshListviewBinding;
        this.firstPagerMoreActivity = firstPagerMoreActivity;
        this.isDemand = isDemand;
        isFirstInPager = true;
        initListView();
        initData();
        listener();
    }

    //加载listView
    private void initListView() {
        PullToRefreshLayout ptrl = pullToRefreshListviewBinding.refreshView;
        ptrl.setOnRefreshListener(new FreeTimeListListener());
    }

    private void initData() {
        getData(isDemand);
    }

    public void clear() {
        if (arrayListDemand != null) {
            arrayListDemand.clear();
        }
        if (arrayListService != null) {
            arrayListService.clear();
        }
    }

    public void getData(boolean isDemand) {
        if (isDemand) {
            if (isFirstInPager) {
                //首次进入页面时，调用新的接口，区分精准和模糊
                getRecommendDemandMore2();
            } else {
                FirstPagerManager.onFreeTimeMoreDemandList(new onFreeTimeMoreDemandList(), pattern, isauth, city, sort, lng, lat, offset, limit);
            }
        } else {
            if (isFirstInPager) {
                //首次进入页面时，调用新的接口，区分精准和模糊
                getRecommendServiceMore2();
            } else {
                FirstPagerManager.onFreeTimeMoreServiceList(new onFreeTimeMoreServiceList(), tag, pattern, isauth, city, sort, lng, lat, offset, limit);
            }
        }
    }

    int rec_offset = 0;//推荐分页offset
    int rec_limit = limit;//推荐分页limit
    int rad_offset = 0;//随机分页offset
    int rad_limit = limit;//随机分页limit

    private void getRecommendDemandMore2() {
        FirstPagerManager.getRecommendDemandMore2(new BaseProtocol.IResultExecutor<HomeRecommendList2>() {
            @Override
            public void execute(HomeRecommendList2 dataBean) {
                //把HomeRecommendList2 转成  FreeTimeMoreDemandBean(字段赋值)
                FreeTimeMoreDemandBean freeTimeMoreDemandBean = new FreeTimeMoreDemandBean();
                freeTimeMoreDemandBean.setRescode(dataBean.rescode);
                freeTimeMoreDemandBean.setData(new FreeTimeMoreDemandBean.DataBean());
                freeTimeMoreDemandBean.getData().setList(new ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean>());
                List<FreeTimeMoreDemandBean.DataBean.ListBean> listMoreDemand = freeTimeMoreDemandBean.getData().getList();

                boolean isInsertRadHint = false;
                ArrayList<HomeRecommendList2.RecommendInfo> reclist = dataBean.data.reclist;
                for (HomeRecommendList2.RecommendInfo recommendInfo : reclist) {
                    FreeTimeMoreDemandBean.DataBean.ListBean listBean = new FreeTimeMoreDemandBean.DataBean.ListBean();
                    listBean.isInsertRadHint = isInsertRadHint;

                    listBean.setAnonymity(recommendInfo.anonymity);
//                    listBean.setIsonline();
                    listBean.setPlace(recommendInfo.place);
//                    listBean.setTag();
                    listBean.setAvatar(recommendInfo.avatar);
//                    listBean.setCity();
//                    listBean.setCts();
                    listBean.setId(recommendInfo.id);
                    listBean.setInstalment(recommendInfo.instalment);
                    listBean.setIsauth(recommendInfo.isauth);
                    listBean.setLat(recommendInfo.lat);
                    listBean.setLng(recommendInfo.lng);
//                    listBean.setLocation();
                    listBean.setName(recommendInfo.name);
                    listBean.setPattern(recommendInfo.pattern);
                    listBean.setQuote(recommendInfo.quote);
                    listBean.setStarttime(recommendInfo.starttime);
                    listBean.setTitle(recommendInfo.title);
                    listBean.setUid(recommendInfo.uid);
//                    listBean.setUts();

                    listMoreDemand.add(listBean);
                }

                isInsertRadHint = true;
                ArrayList<HomeRecommendList2.RecommendInfo> radlist = dataBean.data.radlist;
                for (HomeRecommendList2.RecommendInfo recommendInfo : radlist) {
                    FreeTimeMoreDemandBean.DataBean.ListBean listBean = new FreeTimeMoreDemandBean.DataBean.ListBean();
                    listBean.isInsertRadHint = isInsertRadHint;
                    isInsertRadHint = false;

                    listBean.setAnonymity(recommendInfo.anonymity);
//                    listBean.setIsonline();
                    listBean.setPlace(recommendInfo.place);
//                    listBean.setTag();
                    listBean.setAvatar(recommendInfo.avatar);
//                    listBean.setCity();
//                    listBean.setCts();
                    listBean.setId(recommendInfo.id);
                    listBean.setInstalment(recommendInfo.instalment);
                    listBean.setIsauth(recommendInfo.isauth);
                    listBean.setLat(recommendInfo.lat);
                    listBean.setLng(recommendInfo.lng);
//                    listBean.setLocation();
                    listBean.setName(recommendInfo.name);
                    listBean.setPattern(recommendInfo.pattern);
                    listBean.setQuote(recommendInfo.quote);
                    listBean.setStarttime(recommendInfo.starttime);
                    listBean.setTitle(recommendInfo.title);
                    listBean.setUid(recommendInfo.uid);
//                    listBean.setUts();

                    listMoreDemand.add(listBean);
                }

                int recSize = reclist.size();
                int radSize = radlist.size();
                if (recSize >= rec_limit) {
                    rec_offset += rec_limit;
                } else {
                    rad_offset += rad_limit;
                }

                setMoreDemandData(freeTimeMoreDemandBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, rec_offset + "", rec_limit + "", rad_offset + "", rad_limit + "");
    }

    private void getRecommendServiceMore2() {
        FirstPagerManager.getRecommendServiceMore2(new BaseProtocol.IResultExecutor<HomeRecommendList2>() {
            @Override
            public void execute(HomeRecommendList2 dataBean) {
                //把HomeRecommendList2 转成  FreeTimeMoreServiceBean(字段赋值)
                FreeTimeMoreServiceBean freeTimeMoreServiceBean = new FreeTimeMoreServiceBean();
                freeTimeMoreServiceBean.setRescode(dataBean.rescode);
                freeTimeMoreServiceBean.setData(new FreeTimeMoreServiceBean.DataBean());
                freeTimeMoreServiceBean.getData().setList(new ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean>());
                List<FreeTimeMoreServiceBean.DataBean.ListBean> listMoreDemand = freeTimeMoreServiceBean.getData().getList();

                boolean isInsertRadHint = false;
                ArrayList<HomeRecommendList2.RecommendInfo> reclist = dataBean.data.reclist;
                for (HomeRecommendList2.RecommendInfo recommendInfo : reclist) {
                    FreeTimeMoreServiceBean.DataBean.ListBean listBean = new FreeTimeMoreServiceBean.DataBean.ListBean();
                    listBean.isInsertRadHint = isInsertRadHint;

                    listBean.setAnonymity(recommendInfo.anonymity);
//                    listBean.setIsonline();
                    listBean.setPlace(recommendInfo.place);
//                    listBean.setTag();
                    listBean.setAvatar(recommendInfo.avatar);
//                    listBean.setCity();
//                    listBean.setCts();
                    listBean.setEndtime(recommendInfo.endtime);
                    listBean.setId(recommendInfo.id);
                    listBean.setInstalment(recommendInfo.instalment);
                    listBean.setIsauth(recommendInfo.isauth);
                    listBean.setLat(recommendInfo.lat);
                    listBean.setLng(recommendInfo.lng);
//                    listBean.setLocation();
                    listBean.setName(recommendInfo.name);
                    listBean.setPattern(recommendInfo.pattern);
                    listBean.setQuote(recommendInfo.quote);
                    listBean.setQuoteunit(recommendInfo.quoteunit);
                    listBean.setStarttime(recommendInfo.starttime);
                    listBean.setTimetype(recommendInfo.timetype);
                    listBean.setTitle(recommendInfo.title);
                    listBean.setUid(recommendInfo.uid);
//                    listBean.setUserservicepoint();
//                    listBean.setUts();

                    listMoreDemand.add(listBean);
                }

                isInsertRadHint = true;
                ArrayList<HomeRecommendList2.RecommendInfo> radlist = dataBean.data.radlist;
                for (HomeRecommendList2.RecommendInfo recommendInfo : radlist) {
                    FreeTimeMoreServiceBean.DataBean.ListBean listBean = new FreeTimeMoreServiceBean.DataBean.ListBean();
                    listBean.isInsertRadHint = isInsertRadHint;
                    isInsertRadHint = false;

                    listBean.setAnonymity(recommendInfo.anonymity);
//                    listBean.setIsonline();
                    listBean.setPlace(recommendInfo.place);
//                    listBean.setTag();
                    listBean.setAvatar(recommendInfo.avatar);
//                    listBean.setCity();
//                    listBean.setCts();
                    listBean.setEndtime(recommendInfo.endtime);
                    listBean.setId(recommendInfo.id);
                    listBean.setInstalment(recommendInfo.instalment);
                    listBean.setIsauth(recommendInfo.isauth);
                    listBean.setLat(recommendInfo.lat);
                    listBean.setLng(recommendInfo.lng);
//                    listBean.setLocation();
                    listBean.setName(recommendInfo.name);
                    listBean.setPattern(recommendInfo.pattern);
                    listBean.setQuote(recommendInfo.quote);
                    listBean.setQuoteunit(recommendInfo.quoteunit);
                    listBean.setStarttime(recommendInfo.starttime);
                    listBean.setTimetype(recommendInfo.timetype);
                    listBean.setTitle(recommendInfo.title);
                    listBean.setUid(recommendInfo.uid);
//                    listBean.setUserservicepoint();
//                    listBean.setUts();


                    listMoreDemand.add(listBean);
                }

                int recSize = reclist.size();
                int radSize = radlist.size();
                if (recSize >= rec_limit) {
                    rec_offset += rec_limit;
                } else {
                    rad_offset += rad_limit;
                }

                setMoreServiceData(freeTimeMoreServiceBean);
            }

            @Override
            public void executeResultError(String result) {

            }
        }, rec_offset + "", rec_limit + "", rad_offset + "", rad_limit + "");
    }

    //不同的条目点击事件
    private void listener() {
        pullToRefreshListviewBinding.lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isDemand) {
                    FreeTimeMoreDemandBean.DataBean.ListBean listBean = arrayListDemand.get(position);
                    long demandId = listBean.getId();
                    Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                    intentDemandDetailActivity.putExtra("demandId", demandId);
                    firstPagerMoreActivity.startActivity(intentDemandDetailActivity);

                } else {
                    FreeTimeMoreServiceBean.DataBean.ListBean listBean = arrayListService.get(position);
                    long serviceId = listBean.getId();
                    Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                    intentServiceDetailActivity.putExtra("serviceId", serviceId);
                    firstPagerMoreActivity.startActivity(intentServiceDetailActivity);
                }
            }
        });
    }

    public class FreeTimeListListener implements PullToRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isFirstInPager) {
                        rec_offset = 0;
                        rad_offset = 0;
                    } else {
                        offset = 0;
                    }
                    arrayListDemand.clear();
                    arrayListService.clear();
                    getData(isDemand);
                    if (pagerHomeDemandtAdapter != null) {
                        pagerHomeDemandtAdapter.notifyDataSetChanged();
                    }

                    if (pagerHomeServiceAdapter != null) {
                        pagerHomeServiceAdapter.notifyDataSetChanged();
                    }
                    pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                }
            }, 2000);
        }

        @Override
        public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //如果加载到最后一页，需要调用setLoadToLast()方法
                    if (listsize < limit) {//说明到最后一页啦
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    } else {//不是最后一页
                        if (!isFirstInPager) {
                            offset += limit;
                        }
                        getData(isDemand);
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                    if (pagerHomeDemandtAdapter != null) {
                        pagerHomeDemandtAdapter.notifyDataSetChanged();
                    }

                    if (pagerHomeServiceAdapter != null) {
                        pagerHomeServiceAdapter.notifyDataSetChanged();
                    }
                }
            }, 2000);
        }
    }

    //服务
    public class onFreeTimeMoreServiceList implements BaseProtocol.IResultExecutor<FreeTimeMoreServiceBean> {
        @Override
        public void execute(FreeTimeMoreServiceBean data) {
            setMoreServiceData(data);
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private ArrayList<FreeTimeMoreServiceBean.DataBean.ListBean> thisLastServiceData = new ArrayList<>();

    private void setMoreServiceData(FreeTimeMoreServiceBean data) {
        int rescode = data.getRescode();
        if (rescode == 0) {
            FreeTimeMoreServiceBean.DataBean dataBean = data.getData();
            List<FreeTimeMoreServiceBean.DataBean.ListBean> list = dataBean.getList();
            listsize = list.size();

            boolean isNoData = false;
            if (listsize <= 0) {
                if (isFirstInPager) {//根据rec_offset和rad_offset来判断
                    if (rec_offset == 0 && rad_offset == 0) {
                        isNoData = true;
                    }
                } else {//根据offset判断
                    if (offset == 0) {
                        isNoData = true;
                    }
                }
            }
            if (isNoData) {
                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                pullToRefreshListviewBinding.refreshView.setVisibility(View.GONE);
            } else {
                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                pullToRefreshListviewBinding.refreshView.setVisibility(View.VISIBLE);
                arrayListService.addAll(list);
                thisLastServiceData.clear();
                thisLastServiceData.addAll(arrayListService);
                if (pagerHomeServiceAdapter != null) {
                    pagerHomeServiceAdapter.notifyDataSetChanged();
                } else {
                    pagerHomeServiceAdapter = new PagerMoreServiceAdapter(thisLastServiceData, firstPagerMoreActivity);
                    pullToRefreshListviewBinding.lv.setAdapter(pagerHomeServiceAdapter);
                }
            }

//            if (listsize == 0) {
//                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
//                pullToRefreshListviewBinding.tvContent.setVisibility(View.GONE);
//                arrayListService.clear();
//                if (pagerHomeServiceAdapter != null) {
//                    pagerHomeServiceAdapter.notifyDataSetChanged();
//                }
//            } else {
//                arrayListService.addAll(list);
//                pagerHomeServiceAdapter = new PagerMoreServiceAdapter(arrayListService, firstPagerMoreActivity);
//                pullToRefreshListviewBinding.lv.setAdapter(pagerHomeServiceAdapter);
//                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
//                pagerHomeServiceAdapter.notifyDataSetChanged();
//            }
        }
    }

    //需求
    public class onFreeTimeMoreDemandList implements BaseProtocol.IResultExecutor<FreeTimeMoreDemandBean> {
        @Override
        public void execute(FreeTimeMoreDemandBean data) {
            setMoreDemandData(data);
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private ArrayList<FreeTimeMoreDemandBean.DataBean.ListBean> theLastDemandData = new ArrayList<>();

    private void setMoreDemandData(FreeTimeMoreDemandBean data) {
        int rescode = data.getRescode();
        if (rescode == 0) {
            FreeTimeMoreDemandBean.DataBean dataBean = data.getData();
            List<FreeTimeMoreDemandBean.DataBean.ListBean> list = dataBean.getList();
            listsize = list.size();

            boolean isNoData = false;
            if (listsize <= 0) {
                if (isFirstInPager) {//根据rec_offset和rad_offset来判断
                    if (rec_offset == 0 && rad_offset == 0) {
                        isNoData = true;
                    }
                } else {//根据offset判断
                    if (offset == 0) {
                        isNoData = true;
                    }
                }
            }

            if (isNoData) {//没有数据
                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
                pullToRefreshListviewBinding.refreshView.setVisibility(View.GONE);
            } else {
                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
                pullToRefreshListviewBinding.refreshView.setVisibility(View.VISIBLE);
                arrayListDemand.addAll(list);
                theLastDemandData.clear();
                theLastDemandData.addAll(arrayListDemand);
                if (pagerHomeDemandtAdapter != null) {
                    pagerHomeDemandtAdapter.notifyDataSetChanged();
                } else {
                    pagerHomeDemandtAdapter = new PagerMoreDemandtAdapter(theLastDemandData, firstPagerMoreActivity);
                    pullToRefreshListviewBinding.lv.setAdapter(pagerHomeDemandtAdapter);
                }
            }

//            if (listsize == 0) {
//                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.VISIBLE);
//                arrayListDemand.clear();
//                if (pagerHomeDemandtAdapter != null) {
//                    pagerHomeDemandtAdapter.notifyDataSetChanged();
//                }
//            } else {
//                pullToRefreshListviewBinding.rlHomeDefaultImage.setVisibility(View.GONE);
//                arrayListDemand.addAll(list);
//                pagerHomeDemandtAdapter = new PagerMoreDemandtAdapter(arrayListDemand, firstPagerMoreActivity);
//                pullToRefreshListviewBinding.lv.setAdapter(pagerHomeDemandtAdapter);
//                pagerHomeDemandtAdapter.notifyDataSetChanged();
//            }
        }
    }
}
