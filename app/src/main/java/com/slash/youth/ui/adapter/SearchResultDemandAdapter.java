package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.viewmodel.SearchResultItemDemandModel;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/6.
 */
public class SearchResultDemandAdapter extends SlashBaseAdapter<SearchAllBean.DataBean.DemandListBean> {
    public SearchResultDemandAdapter(ArrayList<SearchAllBean.DataBean.DemandListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchAllBean.DataBean.DemandListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SearchResultItemDemandModel();
    }
}
