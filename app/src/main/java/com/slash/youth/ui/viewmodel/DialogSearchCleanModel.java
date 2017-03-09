package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.SearchHistoryEntity;
import com.slash.youth.gen.SearchHistoryEntityDao;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zss on 2016/9/20.
 */
public class DialogSearchCleanModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    private DialogSearchCleanBinding mDialogSearchCleanBinding;
    public AlertDialog currentDialog;//当前对话框
    private ArrayList<ItemSearchBean> searchHistoryList = new ArrayList<>();
    private SearchHistoryEntityDao searchHistoryEntityDao;
    private File file;
    private SearchHistoryListAdapter adapter;
   // private String fileName ="data/data/com.slash.youth";
    private  SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();

    //构造函数
    public DialogSearchCleanModel(DialogSearchCleanBinding dialogSearchCleanBing, SearchHistoryEntityDao searchHistoryEntityDao,SearchHistoryListAdapter adapter) {
        this.mDialogSearchCleanBinding = dialogSearchCleanBing;
        this.searchHistoryEntityDao = searchHistoryEntityDao;
        this.adapter =adapter;
    }

    //取消搜索对话框(xml中取消按钮绑定方法)
    public void cancelSearchDialog(View v) {
        currentDialog.dismiss();
    }

    //ok搜索对话框
    public void okSearchDialog(View v) {
        currentDialog.dismiss();
        //数据库清空
        searchHistoryEntityDao.deleteAll();
        searchHistoryList.clear();
        List<SearchHistoryEntity> searchHistoryEntities = searchHistoryEntityDao.loadAll();
        for (SearchHistoryEntity searchHistoryEntity : searchHistoryEntities) {
            searchHistoryList.add(new ItemSearchBean(searchHistoryEntity.getSearchHistory(),true));
        }

        adapter= new SearchHistoryListAdapter(searchHistoryList,searchHistoryEntityDao);
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
        currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(View.INVISIBLE);
    }

}
