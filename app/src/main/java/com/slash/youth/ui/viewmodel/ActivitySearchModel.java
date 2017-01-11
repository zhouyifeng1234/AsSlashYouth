package com.slash.youth.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.blankj.utilcode.utils.RegexUtils;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.DemandFlowLogList;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.SearchHistoryEntity;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.gen.SearchHistoryEntityDao;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SearchAssociativeProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.StringUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    private SearchHistoryListAdapter adapter;
    private String searchText = "";
    public static ArrayList<ItemSearchBean> search_contentlist = new ArrayList<>();
    private ArrayList<String> list = new ArrayList<>();
  /*  private String fileName ="data/data/com.slash.youth";
    private File file = new File(fileName, "SearchHistory.text");*/
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private int offset = 0;
    private int limit = 5;
    private SearchActivity searchActivity;
    private SearchHistoryEntityDao searchHistoryEntityDao;

    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding,SearchActivity searchActivity,SearchHistoryEntityDao searchHistoryEntityDao) {
        this.mActivitySearchBinding = activitySearchBinding;
        this.searchActivity = searchActivity;
        this.searchHistoryEntityDao = searchHistoryEntityDao;

        etSearchListener();
        getSearchContentData(searchText, offset, limit);

    }

    //点击直接搜索框,搜索历史
    public void etSearch(View v) {
        currentActivity.changeView(4);
        SearchAssociationModel searchDialogModel = new SearchAssociationModel(currentActivity.searchListviewAssociationBinding,searchHistoryEntityDao,adapter);
        currentActivity.searchListviewAssociationBinding.setSearchAssociationModel(searchDialogModel);
        //加载搜索框历史的数据
        //initSearchHistoryData();
        showHistoryData();
    }

    //点击搜索
    public void searchBtn(View view) {
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
               // getSearchContentData(searchText, offset, limit);
               // showSearchHistroy(searchText);
            }
        });
    }

    //显示搜索的历史记录
    private void showSearchHistroy(String searchText) {
        int textLength = searchText.length();
        switch (textLength) {
            case 0:
                //showHistory();
                showHistoryData();
                break;
            case 1:
                if (RegexUtils.isChz(searchText)) {
                    getSearchContentData(searchText, offset, limit);
                } else {
                    ToastUtils.shortToast("输入信息太少，请重新输入");
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

   /* //保存搜索的记录
    private void saveHistory(String text) {
        try {
            //BufferedWriter  bw = new BufferedWriter(new FileWriter(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    //保存数据在数据库
    private void saveHistoryData(String text) {
        long count = searchHistoryEntityDao.count();
        SearchHistoryEntity searchHistoryEntity = new SearchHistoryEntity(count+1,text);
        searchHistoryEntityDao.insert(searchHistoryEntity);
    }

    //读取数据库
    private void showHistoryData() {
    search_contentlist.clear();
    List<SearchHistoryEntity> SearchHistoryList = searchHistoryEntityDao.loadAll();
        Collections.reverse(SearchHistoryList);
        if(SearchHistoryList.size()!=0){
            if(SearchHistoryList.size()<5&&SearchHistoryList.size()>=0){
                for (SearchHistoryEntity searchHistoryEntity : SearchHistoryList) {
                    String searchHistory = searchHistoryEntity.getSearchHistory();
                    search_contentlist.add(new ItemSearchBean(searchHistory,true));
                }
            }else {
                for (int i = 0; i < 5; i++) {
                    SearchHistoryEntity searchHistoryEntity = SearchHistoryList.get(i);
                    String searchHistory = searchHistoryEntity.getSearchHistory();
                    search_contentlist.add(new ItemSearchBean(searchHistory,true));
                }
            }
        }else {
            LogKit.d("没有搜索历史记录");
        }

        adapter = new SearchHistoryListAdapter(search_contentlist,searchHistoryEntityDao);
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
        onItemClick();
    }

    //显示历史数据
  /*  private void showHistory() {
        initSearchHistoryData();
    }*/

    //直接搜索结果
    private void showSearchAllReasultView(String text) {
        currentActivity.changeView(3);
        SearchResultAllModel searchResultAllModel = new SearchResultAllModel(currentActivity.activityResultAllBinding, text);
        currentActivity.activityResultAllBinding.setSearchResultAllModel(searchResultAllModel);
    }

    //加载搜索历史的数据
   /* private void initSearchHistoryData() {
        //存储集合
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line= br.readLine())!=null){
                if(TextUtils.isEmpty(line)){
                    continue;
                }
                list.add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //倒序，最多取5个
        Collections.reverse(list);
        search_contentlist.clear();
        if(list.size()!=0){
            if(list.size()<5&&list.size()>=0){
                for (String text : list) {
                    search_contentlist.add(new ItemSearchBean(text,true));
                }
            }else {
                for (int i = 0; i < 5; i++) {
                    String text = list.get(i);
                    search_contentlist.add(new ItemSearchBean(text,true));
                }
            }
        }else {
            LogKit.d("没有搜索历史记录");
        }

        adapter = new SearchHistoryListAdapter(search_contentlist);
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
        onItemClick();
    }*/

    //获取搜索联想词
    private void getSearchContentData(String text, int offset, int limit) {
        String etText = mActivitySearchBinding.etActivitySearchAssociation.getText().toString();
        if(text!=null&&text!=""&&etText!=null){
            SearchManager.getSearchAssociativeTag(new onGetSearchAssociativeTag(), text, offset, limit);
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
            if(list.size() == 0){
                currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(View.GONE);
            }
            adapter = new SearchHistoryListAdapter(search_contentlist,searchHistoryEntityDao);
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
