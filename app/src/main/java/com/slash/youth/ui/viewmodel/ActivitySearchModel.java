package com.slash.youth.ui.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.blankj.utilcode.utils.RegexUtils;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SearchAssociativeProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
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

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    private SearchHistoryListAdapter adapter;
    private PagerSearchItemAdapter pagerSearchItemAdapter;
    private String searchContent1 = "";
    private ArrayList<String> list ;
    private ArrayList<String> search_contentlist = new ArrayList<>();
    private ArrayList<String> searchHistoryList= new ArrayList<>();;
    private File externalCacheDir = CommonUtils.getApplication().getExternalCacheDir();
    private String fileName = externalCacheDir.getAbsolutePath() + "/searchCacheDir";
    private File file = new File(fileName, "history.text");
    private String tvSearchright;
    private  SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private ListView listView;

    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding) {
        this.mActivitySearchBinding = activitySearchBinding;
        //搜索框的监听，zss
        searchListener();
    }

    //zss,点击直接搜索
    public void etSearch(View v) {
        currentActivity.changeView(4);
        SearchAssociationModel searchDialogModel = new SearchAssociationModel(currentActivity.searchListviewAssociationBinding);
        currentActivity.searchListviewAssociationBinding.setSearchAssociationModel(searchDialogModel);
        //加载搜索框历史的数据
        initSearchHistoryData();
    }

      //zss.监听框的搜索
    private void searchListener() {
        // 搜索框里面文字变化，出现联想搜索
        mActivitySearchBinding.etActivitySearchAssociation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //只要变化就会出现搜索历史记录的页面
              /*  currentActivity.changeView(4);
                SearchAssociationModel searchDialogModel = new SearchAssociationModel(currentActivity.searchListviewAssociationBinding);
                currentActivity.searchListviewAssociationBinding.setSearchAssociationModel(searchDialogModel);*/
            }

            //文本改变之后,输入的内容要保存到本地
            @Override
            public void afterTextChanged(Editable s) {
                //获取搜索内容
                //分三部分
                //长度为0：打开历史
                //长度为1并且非汉字；不动
                //长度大于2：搜索
                searchContent1 = s.toString();
                if(searchContent1.length()==0){
                    showHistory();
                }else if (searchContent1.length()==1){
                    if(RegexUtils.isChz(searchContent1)){
                        setSearchContentData();
                    }else {
                        //输入了一个非汉字的字符
                        ToastUtils.shortToast("输入信息太少，请重新输入");
                    }
                }else if(searchContent1.length()>=2){
                    setSearchContentData();
                }
                //当输入框里面不等于0就是有字，出现图片
                setDrawable(searchContent1);
            }
        });

        //设置搜索的数据，zss
        setSearchContentData();
        //点击搜索，显示搜索的结果 zss,二级页面是取消按钮
        setClickSearch();
        //叉叉图片点击事件
        mActivitySearchBinding.searchIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mActivitySearchBinding.etActivitySearchAssociation.setText(null);
            mActivitySearchBinding.searchIvDelete.setVisibility(View.INVISIBLE);
            }
        });
    }
    //设置搜索框的叉叉的图片
    private void setDrawable(String text) {
        if (text.length() < 1) {
            mActivitySearchBinding.searchIvDelete.setVisibility(View.INVISIBLE);
        }else {
            mActivitySearchBinding.searchIvDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setClickSearch() {
        mActivitySearchBinding.tvSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            tvSearchright = mActivitySearchBinding.tvSearchResult.getText().toString();
                    //显示结果页面
                String text=mActivitySearchBinding.etActivitySearchAssociation.getText().toString();
                if(text.length()==0){
                    ToastUtils.shortToast("请您输入搜索内容");
                    mActivitySearchBinding.etActivitySearchAssociation.setFocusable(true);
                    mActivitySearchBinding.etActivitySearchAssociation.setFocusableInTouchMode(true);
                    mActivitySearchBinding.etActivitySearchAssociation.requestFocus();
                    InputMethodManager inputManager = (InputMethodManager) CommonUtils.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(mActivitySearchBinding.etActivitySearchAssociation, 0);

                } else if (text.length()==1){
                    if(StringUtils.isSearchContent(text)) {
                        if (RegexUtils.isChz(searchContent1)) {
                            showSearchAllReasultView(text);
                        } else {
                            //输入了一个非汉字的字符
                            ToastUtils.shortToast("输入信息太少，请重新输入");
                        }
                    }
                } else if (text.length()>=2){
                    if(StringUtils.isSearchContent(text)) {
                        //联网搜索,并且根据字符搜索
                        saveHistory(text);
                        //TODO
                        // showSearchAllReasultView(text);
                    }else{
                        ToastUtils.shortToast("输入有效字符");
                    }
                }
            }
        });
    }

    private void saveHistory(String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showHistory() {
        initSearchHistoryData();
    }
    //zss 直接搜索结果
    private void showSearchAllReasultView(String text) {
        currentActivity.changeView(3);
        SearchResultAllModel searchResultAllModel = new SearchResultAllModel(currentActivity.activityResultAllBinding, text);
        currentActivity.activityResultAllBinding.setSearchResultAllModel(searchResultAllModel);
    }

    private   void initSearchHistoryData() {
        //存储集合
        list = new ArrayList<String>();
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
        searchHistoryList.clear();
        if(list.size()!=0){
            for (int i = 0; i < 5; i++) {
            searchHistoryList.add(list.get(i));
            }

        }else {
            LogKit.d("没有搜索历史记录");
        }
        adapter= new SearchHistoryListAdapter(searchHistoryList,true,mActivitySearchBinding);
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
    }

    private void setSearchContentData() {
         SearchAssociativeProtocol searchAssociativeProtocol = new SearchAssociativeProtocol(searchContent1);
        search_contentlist.clear();
        searchAssociativeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SearchAssociativeBean>() {
            @Override
            public void execute(SearchAssociativeBean dataBean) {
                ArrayList<SearchAssociativeBean.TagBean> list = dataBean.data.list;
                for (SearchAssociativeBean.TagBean tagBean : list) {
                    //遍历去重复
                    if(!search_contentlist.contains(tagBean.tag)){
                        search_contentlist.add(tagBean.tag);
                    }
                }
                adapter= new SearchHistoryListAdapter(search_contentlist,false,mActivitySearchBinding);
                currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("executeResultError:"+result);
            }
        });
    }
}
