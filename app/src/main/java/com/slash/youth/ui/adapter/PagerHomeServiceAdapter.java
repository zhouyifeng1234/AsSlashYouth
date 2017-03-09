package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SearchServiceItemBean;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.PagerHomeDemandHolder;
import com.slash.youth.ui.holder.PagerHomeServiceHolder;
import com.slash.youth.ui.holder.PagerMorwServiceHolder;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerHomeServiceAdapter extends SlashBaseAdapter<SearchServiceItemBean.DataBean.ListBean> {

    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    public PagerHomeServiceAdapter(ArrayList<SearchServiceItemBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SearchServiceItemBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new PagerHomeServiceHolder();
    }
}
