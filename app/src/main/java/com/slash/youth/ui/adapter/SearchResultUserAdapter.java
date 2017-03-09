package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.viewmodel.SearchResultItemDemandModel;
import com.slash.youth.ui.viewmodel.SearchResultItemUserModel;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/6.
 */
public class SearchResultUserAdapter extends SlashBaseAdapter<SearchAllBean.DataBean.UserListBean> {
    public SearchResultUserAdapter(ArrayList<SearchAllBean.DataBean.UserListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchAllBean.DataBean.UserListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SearchResultItemUserModel();
    }
}
