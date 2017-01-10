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

/**
 * Created by zss on 2016/9/20.
 */
public class DialogSearchCleanModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    private DialogSearchCleanBinding mDialogSearchCleanBinding;
    public AlertDialog currentDialog;//当前对话框
    private ArrayList<ItemSearchBean> searchHistoryList;
    private File file;
    private SearchHistoryListAdapter adapter;
    private String fileName ="data/data/com.slash.youth";
    private  SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();

    //构造函数
    public DialogSearchCleanModel(DialogSearchCleanBinding dialogSearchCleanBing) {
        this.mDialogSearchCleanBinding = dialogSearchCleanBing;
    }

    //取消搜索对话框(xml中取消按钮绑定方法)
    public void cancelSearchDialog(View v) {
        currentDialog.dismiss();
    }

    //ok搜索对话框
    public void okSearchDialog(View v) {
        try{
            file = new File(fileName, "SearchHistory.text");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write("");
            bw.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        initSearchHistoryData();
        currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(View.INVISIBLE);
        //删除文件
        deleteFile(file);
        //对话框消息
        currentDialog.dismiss();
    }

    public  void initSearchHistoryData() {
        //存储集合
        searchHistoryList = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line= br.readLine())!=null){
                if(TextUtils.isEmpty(line)){
                    continue;
                }
                searchHistoryList .add(new ItemSearchBean(line,true));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter= new SearchHistoryListAdapter(searchHistoryList);
        currentActivity.searchListviewAssociationBinding.lvLvSearchcontent.setAdapter(adapter);
    }


    public void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    this.deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            LogKit.d("文件不存在！");
        }
    }

}
