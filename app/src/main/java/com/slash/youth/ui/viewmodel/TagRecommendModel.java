package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityTagRecommendBinding;
import com.slash.youth.domain.AllSkillLablesBean;
import com.slash.youth.domain.LoginTagBean;
import com.slash.youth.domain.TagRecommendList;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.DemandDetailActivity;
import com.slash.youth.ui.activity.ServiceDetailActivity;
import com.slash.youth.ui.adapter.TagRecommendAdapter;
import com.slash.youth.ui.view.RefreshListView;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class TagRecommendModel extends BaseObservable {

    public static final int LOAD_DATA_TYPE_FIRST = 0;//第一次进入页面的加载
    public static final int LOAD_DATA_TYPE_TAB = 1;//tab页切换的加载方式
    public static final int LOAD_DATA_TYPE_REFRESH = 2;//下拉刷新的加载方式
    public static final int LOAD_DATA_TYPE_MORE = 3;//上拉加载更多的加载方式

    private int currentLoadDataType = LOAD_DATA_TYPE_FIRST;//当前加载数据的方式，默认为第一次页面进入的加载方式
    ActivityTagRecommendBinding mActivityTagRecommendBinding;
    Activity mActivity;
    long tagId;
    String tagName;
    RefreshListView lvTagRecommend;

    public TagRecommendModel(ActivityTagRecommendBinding activityTagRecommendBinding, Activity activity) {
        this.mActivityTagRecommendBinding = activityTagRecommendBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }

    private void initData() {
        tagId = mActivity.getIntent().getLongExtra("tagId", -1);//这里应该是一级标签的ID
        tagName = mActivity.getIntent().getStringExtra("tagName");
        LogKit.v("tagId:" + tagId + "    tagName:" + tagName);
        getDataFromServer();
    }

    private void initView() {
        setFirstTagName(tagName);
        mActivityTagRecommendBinding.lvTagRecommend.setVerticalScrollBarEnabled(false);
        lvTagRecommend = mActivityTagRecommendBinding.lvTagRecommend;
    }

    private void initListener() {
        mActivityTagRecommendBinding.lvTagRecommend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listTagRecommend != null && position < listTagRecommend.size()) {
                    TagRecommendList.TagRecommendInfo tagRecommendInfo = listTagRecommend.get(position);
                    if (tagRecommendInfo.type == 2) {//服务
                        Intent intentServiceDetailActivity = new Intent(CommonUtils.getContext(), ServiceDetailActivity.class);
                        intentServiceDetailActivity.putExtra("serviceId", tagRecommendInfo.id);
                        mActivity.startActivity(intentServiceDetailActivity);
                    } else {//需求
                        Intent intentDemandDetailActivity = new Intent(CommonUtils.getContext(), DemandDetailActivity.class);
                        intentDemandDetailActivity.putExtra("demandId", tagRecommendInfo.id);
                        mActivity.startActivity(intentDemandDetailActivity);
                    }
                }
            }
        });
        //刷新和加载更多的操作
        lvTagRecommend.setRefreshDataTask(new RefreshDataTask());
        lvTagRecommend.setLoadMoreNewsTast(new LoadMoreNewsTask());
    }

    private ArrayList<Long> listTag2Id = new ArrayList<Long>();
    private ArrayList<String> listTag2Name = new ArrayList<String>();

    private void getDataFromServer() {
        initTagsInfo();
        if (allSkillLablesBean != null) {
            HashMap<Long, AllSkillLablesBean.Tag_1> mapTag_1 = allSkillLablesBean.mapTag_1;
            AllSkillLablesBean.Tag_1 tag_1 = mapTag_1.get(tagId);//根据一级标签ID获取一级标签对象
            //获取到这个一级标签下所有二级标签的ID和名字信息
            for (long tag_2_id : tag_1.mapTag_2.keySet()) {
                AllSkillLablesBean.Tag_2 tag_2 = tag_1.mapTag_2.get(tag_2_id);
                String tag_2_name = tag_2.tag;
                LogKit.v("tag_2_id:" + tag_2_id + "  tag_2_name:" + tag_2_name);
                listTag2Id.add(tag_2_id);
                listTag2Name.add(tag_2_name);
            }
            //把二级标签根据id来排序
            for (int a = 0; a < listTag2Id.size(); a++) {
                for (int b = a + 1; b < listTag2Id.size(); b++) {
                    if (listTag2Id.get(a) > listTag2Id.get(b)) {
                        long tmpId;
                        String tmpName;
                        tmpId = listTag2Id.get(a);
                        tmpName = listTag2Name.get(a);
                        listTag2Id.set(a, listTag2Id.get(b));
                        listTag2Name.set(a, listTag2Name.get(b));
                        listTag2Id.set(b, tmpId);
                        listTag2Name.set(b, tmpName);
                    }
                }
            }
            if (listTag2Id.size() > 0) {
                setTag2Tab();
                //默认显示集合中的第一个二级标签的推荐列表
                loadTag2RecommendList(listTag2Id.get(0));
            } else {
                LogKit.v("没有对应二级标签的数据");
                mActivityTagRecommendBinding.llTag2Tab.setVisibility(View.GONE);
            }
        } else {
            LogKit.v("加载标签数据失败,allSkillLablesBean:" + allSkillLablesBean);
        }
    }

    private ArrayList<TextView> listTabTextView = new ArrayList<TextView>();

    /**
     * 显示二级标签的tab
     */
    private void setTag2Tab() {
        if (listTag2Name.size() <= 1) {//只有一个二级标签，“其它”就是如此，直接隐藏二级标签tab栏
            mActivityTagRecommendBinding.llTag2Tab.setVisibility(View.GONE);
        } else {//目前二级标签的数量只有3个或者4个,如果以后会出现跟多的数量，再做判断
            for (int i = 0; i < listTag2Name.size(); i++) {
                String tag2Name = listTag2Name.get(i);
                long tag2Id = listTag2Id.get(i);

                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, -2);
                tvParams.gravity = Gravity.CENTER_VERTICAL;
                tvParams.weight = 1;
                TextView tvTab = new TextView(CommonUtils.getContext());
                tvTab.setLayoutParams(tvParams);
                tvTab.setGravity(Gravity.CENTER);
                tvTab.setText(tag2Name);
                tvTab.setTextSize(15);
                //这里默认选中的是第一个，所以默认第一个字体颜色是31C5E4
                if (i == 0) {
                    tvTab.setTextColor(0xff31C5E4);
                } else {
                    tvTab.setTextColor(0xff333333);
                }
                tvTab.setOnClickListener(new Tag2TabOnClickListener(i));
                mActivityTagRecommendBinding.llTag2Tab.addView(tvTab);
                listTabTextView.add(tvTab);

                if (i < listTag2Name.size() - 1) {//如果不是最后一个tab，需要加入分割竖线
                    LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(CommonUtils.dip2px(1), CommonUtils.dip2px(20));
                    viewParams.gravity = Gravity.CENTER_VERTICAL;
                    View viewLine = new View(CommonUtils.getContext());
                    viewLine.setBackgroundColor(0xffE5E5E5);
                    viewLine.setLayoutParams(viewParams);
                    mActivityTagRecommendBinding.llTag2Tab.addView(viewLine);
                }
            }
        }
    }

    private int currentCheckedTabIndex = 0;//默认选中的是第一个tab

    private class Tag2TabOnClickListener implements View.OnClickListener {

        private int tabIndex;

        public Tag2TabOnClickListener(int tabIndex) {
            this.tabIndex = tabIndex;
        }

        @Override
        public void onClick(View v) {
            if (tabIndex != currentCheckedTabIndex) {
                currentCheckedTabIndex = tabIndex;
                for (int i = 0; i < listTabTextView.size(); i++) {
                    TextView tvTab = listTabTextView.get(i);
                    if (i == tabIndex) {
                        tvTab.setTextColor(0xff31C5E4);
                    } else {
                        tvTab.setTextColor(0xff333333);
                    }
                }
                Long tag2Id = listTag2Id.get(tabIndex);
                offset = 0;
                currentLoadDataType = LOAD_DATA_TYPE_TAB;
                loadTag2RecommendList(tag2Id);
            }
        }
    }

    private int offset = 0;
    private int limit = 20;

    ArrayList<TagRecommendList.TagRecommendInfo> listTagRecommend;
    TagRecommendAdapter tagRecommendAdapter;

    /**
     * 根据二级标签的ID,从接口获取推荐的需求服务列表
     */
    private void loadTag2RecommendList(long tag2Id) {
        LogKit.v("load tag2 recommend list,tag2Id:" + tag2Id);
        MyTaskEngine.getTagRecommendList(new BaseProtocol.IResultExecutor<TagRecommendList>() {
            @Override
            public void execute(TagRecommendList dataBean) {
                if ((currentLoadDataType == LOAD_DATA_TYPE_FIRST || currentLoadDataType == LOAD_DATA_TYPE_TAB || currentLoadDataType == LOAD_DATA_TYPE_REFRESH) && (dataBean.data.list == null || dataBean.data.list.size() <= 0)) {
                    mActivityTagRecommendBinding.flNodataLayer.setVisibility(View.VISIBLE);
                    mActivityTagRecommendBinding.lvTagRecommend.setVisibility(View.GONE);
                } else {
                    mActivityTagRecommendBinding.flNodataLayer.setVisibility(View.GONE);
                    mActivityTagRecommendBinding.lvTagRecommend.setVisibility(View.VISIBLE);
                    if (listTagRecommend == null) {
                        listTagRecommend = new ArrayList<TagRecommendList.TagRecommendInfo>();
                    }
                    if (offset == 0) {// 首次进入页面，或者是刷新操作,或者是切换tab的时候，offset都会为0
                        listTagRecommend.clear();
                    }
                    listTagRecommend.addAll(dataBean.data.list);
                    if (tagRecommendAdapter == null) {
                        tagRecommendAdapter = new TagRecommendAdapter(listTagRecommend);
                        mActivityTagRecommendBinding.lvTagRecommend.setAdapter(tagRecommendAdapter);
                    } else {
                        tagRecommendAdapter.notifyDataSetChanged();
                    }
                    if (currentLoadDataType == LOAD_DATA_TYPE_REFRESH) {//下拉刷新完成
                        mActivityTagRecommendBinding.lvTagRecommend.refreshDataFinish();
                        mActivityTagRecommendBinding.lvTagRecommend.setNotLoadToLast();
                    } else if (currentLoadDataType == LOAD_DATA_TYPE_MORE) {//上拉加载更多完成
                        mActivityTagRecommendBinding.lvTagRecommend.loadMoreNewsFinished();
                        if (dataBean.data.list.size() < limit) {
                            mActivityTagRecommendBinding.lvTagRecommend.setLoadToLast();
                        }
                    }
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.v("获取二级标签推荐的服务需求失败,result:" + result);
            }
        }, tag2Id + "", "2", offset + "", limit + "");
    }

    AllSkillLablesBean allSkillLablesBean;

    private void initTagsInfo() {
        //先获取本地的标签缓存
        allSkillLablesBean = getTagCache();
        if (allSkillLablesBean != null) {

        } else {
            LoginManager.loginGetTag(new BaseProtocol.IResultExecutor<String>() {
                @Override
                public void execute(String dataBean) {
                    setTagCache(dataBean);
                    allSkillLablesBean = getTagCache();
                }

                @Override
                public void executeResultError(String result) {
                    //这里不会执行
                }
            });
        }
    }

    private AllSkillLablesBean getTagCache() {
        File fileCache = CommonUtils.getContext().getCacheDir();
        File fileTagJson = new File(fileCache, "sys_tag.json");
        if (!fileTagJson.exists()) {
            return null;
        } else {
            FileInputStream fisTagJson = null;
            ObjectInputStream oisTagJson = null;
            try {
                fisTagJson = new FileInputStream(fileTagJson);
                oisTagJson = new ObjectInputStream(fisTagJson);
                AllSkillLablesBean allSkillLablesBean = (AllSkillLablesBean) oisTagJson.readObject();
                return allSkillLablesBean;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(oisTagJson);
                IOUtils.close(fisTagJson);
            }
        }
        return null;
    }

    private void setTagCache(String tagJson) {
        File fileCache = CommonUtils.getContext().getCacheDir();
        if (!fileCache.exists()) {
            fileCache.mkdirs();
        }
        File fileTagJson = new File(fileCache, "sys_tag.json");
        FileOutputStream fosTagJson = null;
        ObjectOutputStream oosTagJson = null;
        try {
            fosTagJson = new FileOutputStream(fileTagJson);
            oosTagJson = new ObjectOutputStream(fosTagJson);
            Gson gson = new Gson();

            Type listTagsType = new TypeToken<ArrayList<LoginTagBean>>() {
            }.getType();
            ArrayList<LoginTagBean> listTags = gson.fromJson(tagJson, listTagsType);


            AllSkillLablesBean allSkillLablesBean = new AllSkillLablesBean();
            allSkillLablesBean.addListTags(listTags);
            oosTagJson.writeObject(allSkillLablesBean);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oosTagJson);
            IOUtils.close(fosTagJson);
        }
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    /**
     * 下拉刷新执行的回调，执行结束后需要调用refreshDataFinish()方法，用来更新状态
     */
    public class RefreshDataTask implements RefreshListView.IRefreshDataTask {

        @Override
        public void refresh() {
            offset = 0;
            currentLoadDataType = LOAD_DATA_TYPE_REFRESH;
            Long tag2Id = listTag2Id.get(currentCheckedTabIndex);
            loadTag2RecommendList(tag2Id);
            //避免数据加载错误的情况
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActivityTagRecommendBinding.lvTagRecommend.refreshDataFinish();
                    mActivityTagRecommendBinding.lvTagRecommend.setNotLoadToLast();
                }
            }, 15000);
        }
    }

    /**
     * 上拉加载更多执行的回调，执行完毕后需要调用loadMoreNewsFinished()方法，用来更新状态,如果加载到最后一页，则需要调用setLoadToLast()方法
     */
    public class LoadMoreNewsTask implements RefreshListView.ILoadMoreNewsTask {

        @Override
        public void loadMore() {
            offset += limit;
            currentLoadDataType = LOAD_DATA_TYPE_MORE;
            Long tag2Id = listTag2Id.get(currentCheckedTabIndex);
            loadTag2RecommendList(tag2Id);
            //避免数据加载错误的情况
            CommonUtils.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mActivityTagRecommendBinding.lvTagRecommend.loadMoreNewsFinished();
                }
            }, 15000);
        }
    }

    private String firstTagName;

    @Bindable
    public String getFirstTagName() {
        return firstTagName;
    }

    public void setFirstTagName(String firstTagName) {
        this.firstTagName = firstTagName;
        notifyPropertyChanged(BR.firstTagName);
    }
}
