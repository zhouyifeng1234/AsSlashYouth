package com.slash.youth.ui.viewmodel;

import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/12/6.
 */
public class SearchResultItemDemandModel extends BaseHolder<SearchAllBean.DataBean.DemandListBean> {

    private View demandView;

    @Override
    public View initView() {
        demandView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_needandservice, null);
        return demandView;

    }

    @Override
    public void refreshView(SearchAllBean.DataBean.DemandListBean data) {

    }


}
