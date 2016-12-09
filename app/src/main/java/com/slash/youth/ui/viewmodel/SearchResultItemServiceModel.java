package com.slash.youth.ui.viewmodel;

import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/12/6.
 */
public class SearchResultItemServiceModel extends BaseHolder<SearchAllBean.DataBean.ServiceListBean> {

    private View serviceView;

    @Override
    public View initView() {
        serviceView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_needandservice, null);
        return serviceView;

    }

    @Override
    public void refreshView(SearchAllBean.DataBean.ServiceListBean data) {

    }


}
