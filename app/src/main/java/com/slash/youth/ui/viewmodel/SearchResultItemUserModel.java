package com.slash.youth.ui.viewmodel;

import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/12/6.
 */
public class SearchResultItemUserModel extends BaseHolder<SearchAllBean.DataBean.UserListBean> {

    private View userView;

    @Override
    public View initView() {
        userView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_person, null);

        return userView;

    }

    @Override
    public void refreshView(SearchAllBean.DataBean.UserListBean data) {

    }


}
