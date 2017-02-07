package com.slash.youth.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.blankj.utilcode.utils.RegexUtils;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.SearchHistoryEntity;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.gen.SearchHistoryEntityDao;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.StringUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    private SearchHistoryListAdapter adapter;
    private String searchText = "";
    public static ArrayList<ItemSearchBean> search_contentlist = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private int offset = 0;
    private int limit = 5;
    private SearchActivity searchActivity;
    private SearchHistoryEntityDao searchHistoryEntityDao;
    private CityHistoryEntityDao cityHistoryEntityDao;

    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding, SearchActivity searchActivity, SearchHistoryEntityDao searchHistoryEntityDao, CityHistoryEntityDao cityHistoryEntityDao) {
        this.mActivitySearchBinding = activitySearchBinding;
        this.searchActivity = searchActivity;
        this.searchHistoryEntityDao = searchHistoryEntityDao;
        this.cityHistoryEntityDao = cityHistoryEntityDao;

        etSearchListener();
        getSearchContentData(searchText, offset, limit);
    }

    //点击直接搜索框,搜索历史
    public void etSearch(View v) {
        currentActivity.changeView(4);
        SearchAssociationModel searchDialogModel = new SearchAssociationModel(currentActivity.searchListviewAssociationBinding, searchHistoryEntityDao, adapter);
        currentActivity.searchListviewAssociationBinding.setSearchAssociationModel(searchDialogModel);
        showHistoryData();
    }

    //点击搜索
    public void searchBtn(View view) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.IDLE_TIME_SEARCH_DIRECT_SEARCH);

        int searchTextlength = searchText.length();
        switch (searchTextlength) {
            case 0:
                ToastUtils.shortToast("请您输入搜索内容");
                mActivitySearchBinding.etActivitySearchAssociation.setFocusable(true);
                mActivitySearchBinding.etActivitySearchAssociation.setFocusableInTouchMode(true);
                mActivitySearchBinding.etActivitySearchAssociation.requestFocus();
                InputMethodManager inputManager = (InputMethodManager) CommonUtils.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(mActivitySearchBinding.etActivitySearchAssociation, 0);
                break;
            case 1:
                if (RegexUtils.isChz(searchText)) {
                    showSearchAllReasultView(searchText);
                } else {
                    ToastUtils.shortToast("输入信息太少，请重新输入");
                }
                break;
            default:
                if (StringUtils.isSearchContent(searchText)) {
                    //联网搜索,并且根据字符搜索
                    //saveHistory(searchText);
                    //保存在数据库
                    saveHistoryData(searchText);
                    showSearchAllReasultView(searchText);
                } else if (!StringUtils.isSearchNumberContent(searchText)) {
                    saveHistoryData(searchText);
                    showSearchAllReasultView(searchText);
                } else {
                    ToastUtils.shortToast("输入有效字符");
                }
                break;
        }
    }

    private void etSearchListener() {
        mActivitySearchBinding.etActivitySearchAssociation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchText = s.toString();
                showSearchHistroy(searchText);
            }
        });
    }

    //显示搜索的历史记录
    private void showSearchHistroy(String searchText) {
        int textLength = searchText.length();
        switch (textLength) {
            case 0:
                showHistoryData();
                break;
            case 1:
                if (RegexUtils.isChz(searchText)) {
                    getSearchContentData(searchText, offset, limit);
                }
                break;
            default:
                getSearchContentData(searchText, offset, limit);
                break;
        }
        setDeleteDrawable(searchText);
    }

    //设置搜索框的叉叉的图片
    private void setDeleteDrawable(String text) {
        if (text.length() < 1) {
            mActivitySearchBinding.searchIvDelete.setVisibility(View.INVISIBLE);
        } else {
            mActivitySearchBinding.searchIvDelete.setVisibility(View.VISIBLE);
        }

        mActivitySearchBinding.searchIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivitySearchBinding.etActivitySearchAssociation.setText(null);
                mActivitySearchBinding.searchIvDelete.setVisibility(View.INVISIBLE);
            }
        });
    }

    //保存数据在数据库
    private void saveHistoryData(String text) {
        long count = searchHistoryEntityDao.count();
        SearchHistoryEntity searchHistoryEntity = new SearchHistoryEntity(count + 1, text);
        searchHistoryEntityDao.insert(searchHistoryEntity);
    }

    //读取数据库
    private void showHistoryData() {
        search_contentlist.clear();
        List<SearchHistoryEntity> SearchHistoryList = searchHistoryEntityDao.loadAll();
        Collections.reverse(SearchHistoryList);
        if (SearchHistoryList.size() != 0) {
            if (SearchHistoryList.size() < 5 && SearchHistoryList.size() >= 0) {
                for (SearchHistoryEntity searchHistoryEntity : SearchHistoryList) {
                    String searchHistory = searchHistoryEntity.getSearchHistory();
                    search_contentlist.add(new ItemSearchBean(searchHistory, true));
                }
            } else {
                for (int i = 0; i < 5; i++) {
                    SearchHistoryEntity searchHistoryEntity = SearchHistoryList.get(i);
                    String searchHistory = searchHistoryEntity.getSearchHistory();
                    search_contentlist.add(new ItemSearchBean(searchHistory, true));
                }
            }
        } else {
            LogKit.d("没有搜索历史记录");
        }

        adapter = new SearchHistoryListAdapter(search_contentlist, searchHistoryEntityDao);
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
        onItemClick();
    }

    //直接搜索结果
    private void showSearchAllReasultView(String text) {
        currentActivity.changeView(3);
        SearchResultAllModel searchResultAllModel = new SearchResultAllModel(currentActivity.activityResultAllBinding, text, cityHistoryEntityDao);
        currentActivity.activityResultAllBinding.setSearchResultAllModel(searchResultAllModel);
    }

    //获取搜索联想词
    private void getSearchContentData(String text, int offset, int limit) {
        String etText = mActivitySearchBinding.etActivitySearchAssociation.getText().toString();
        if (!TextUtils.isEmpty(etText)) {
            SearchManager.getSearchAssociativeTag(new onGetSearchAssociativeTag(), etText, offset, limit);
        }
    }

    //获取搜索联想词
    public class onGetSearchAssociativeTag implements BaseProtocol.IResultExecutor<SearchAssociativeBean> {
        @Override
        public void execute(SearchAssociativeBean dataBean) {
            search_contentlist.clear();
            ArrayList<SearchAssociativeBean.TagBean> list = dataBean.data.list;
            for (SearchAssociativeBean.TagBean tagBean : list) {
                //遍历去重复
                if (!search_contentlist.contains(tagBean.tag)) {
                    search_contentlist.add(new ItemSearchBean(tagBean.tag, false));
                }
            }
            if (list.size() == 0) {
                currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(View.GONE);
            }
            adapter = new SearchHistoryListAdapter(search_contentlist, searchHistoryEntityDao);
            currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
            onItemClick();
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //点击条目搜索
    private void onItemClick() {
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemSearchBean itemSearchBean = search_contentlist.get(position);
                String item = itemSearchBean.getItem();
                mActivitySearchBinding.etActivitySearchAssociation.setText(item);
                showSearchAllReasultView(item);
            }
        });
    }
}
