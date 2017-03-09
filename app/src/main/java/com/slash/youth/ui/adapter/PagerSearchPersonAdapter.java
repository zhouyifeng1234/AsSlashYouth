package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SearchUserBean;
import com.slash.youth.domain.SearchUserItemBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerSearchPersonHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerSearchPersonAdapter extends SlashBaseAdapter<SearchUserItemBean.DataBean.ListBean> {


    public PagerSearchPersonAdapter(ArrayList<SearchUserItemBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchUserItemBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerSearchPersonHolder();
    }
}
