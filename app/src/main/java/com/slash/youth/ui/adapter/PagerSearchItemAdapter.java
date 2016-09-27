package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SearchAllItem;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerSearchItemdHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerSearchItemAdapter extends SlashBaseAdapter<SearchAllItem> {

    private static final int searchTitle = 0;
    private static final int searchResult = 1;
    private static final int searchMore = 2;

    public PagerSearchItemAdapter(ArrayList<SearchAllItem> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchAllItem> onLoadMore() {
        return null;
    }



    @Override
    public BaseHolder getHolder(int position) {


        return new PagerSearchItemdHolder();
    }

    //获取种类的数量
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    //条目的种类
    @Override
    public int getItemViewType(int position) {
        int p = position % 5;
        if (p == 0)
            return searchTitle;
        else if (p < 4)
            return searchResult;
        else if (p < 5)
            return searchMore;
        else
            return searchTitle;
    }

}
