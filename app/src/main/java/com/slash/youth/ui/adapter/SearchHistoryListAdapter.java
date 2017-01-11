package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.SearchHistoryEntity;
import com.slash.youth.gen.SearchHistoryEntityDao;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.SearchContentHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by zss on 2016/9/19.搜索listview的适配器
 */
public class SearchHistoryListAdapter extends SlashBaseAdapter<ItemSearchBean> {

    private ArrayList<ItemSearchBean> arrayList;
    private SearchContentHolder searchContentHolder;
    private SearchHistoryEntityDao searchHistoryEntityDao;
    private  SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();

    public SearchHistoryListAdapter(ArrayList<ItemSearchBean> listData, SearchHistoryEntityDao searchHistoryEntityDao) {
        super(listData);
        this.arrayList = listData;
        this.searchHistoryEntityDao = searchHistoryEntityDao;
    }
    @Override
    public ArrayList<ItemSearchBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        searchContentHolder = new SearchContentHolder(arrayList);
        deleteItem();
        return searchContentHolder;
    }

    //删除列表的条目
    private void deleteItem() {
        searchContentHolder.setItemRemoveListener(new SearchContentHolder.onItemRemoveListener() {
            @Override
            public void onItemRemove(int index) {
                if(index == (arrayList.size()-1)){
                    currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(View.GONE);
                }

                arrayList.remove(index);
                notifyDataSetChanged();

                searchHistoryEntityDao.deleteAll();
                for (int i = 0; i < arrayList.size(); i++) {
                    String item = arrayList.get(i).getItem();
                    searchHistoryEntityDao.insert(new SearchHistoryEntity(i,item));
                }
            }
        });
    }
}
