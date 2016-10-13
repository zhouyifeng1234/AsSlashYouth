package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.TextUtils;
import android.view.View;

import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by zss on 2016/9/20.
 */
public class DialogSearchCleanModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    private DialogSearchCleanBinding mDialogSearchCleanBinding;
    public AlertDialog currentDialog;//当前对话框
    private ArrayList<String> searchHistoryList;
    private File file;
    private SearchHistoryListAdapter adapter;
    //构造函数
    public DialogSearchCleanModel(DialogSearchCleanBinding dialogSearchCleanBing,ActivitySearchBinding mActivitySearchBinding) {
        this.mDialogSearchCleanBinding = dialogSearchCleanBing;
        this.mActivitySearchBinding = mActivitySearchBinding;
    }

    //取消搜索对话框(xml中取消按钮绑定方法)
    public void cancelSearchDialog(View v) {
        currentDialog.dismiss();
    }

    //ok搜索对话框
    public void okSearchDialog(View v) {
        try{
            File externalCacheDir = CommonUtils.getApplication().getExternalCacheDir();
            file = new File(externalCacheDir,"history.text");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("");
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        initSearchHistoryData();

        mActivitySearchBinding.tvCleanAll.setVisibility(View.INVISIBLE);
        currentDialog.dismiss();
    }

    public  void initSearchHistoryData() {
        //存储集合
        searchHistoryList = new ArrayList<String>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line= br.readLine())!=null){
                if(TextUtils.isEmpty(line)){
                    continue;
                }
                searchHistoryList .add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        adapter= new SearchHistoryListAdapter(searchHistoryList,true);
        mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);


    }

}
