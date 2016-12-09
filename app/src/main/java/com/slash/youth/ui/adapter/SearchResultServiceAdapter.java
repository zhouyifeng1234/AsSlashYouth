package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.viewmodel.SearchResultItemDemandModel;
import com.slash.youth.ui.viewmodel.SearchResultItemServiceModel;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/6.
 */
public class SearchResultServiceAdapter extends SlashBaseAdapter<SearchAllBean.DataBean.ServiceListBean> {


    public SearchResultServiceAdapter(ArrayList<SearchAllBean.DataBean.ServiceListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchAllBean.DataBean.ServiceListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SearchResultItemServiceModel();
    }
}
