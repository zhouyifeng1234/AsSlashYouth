package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeDemandBinding;
import com.slash.youth.databinding.ItemListviewSearchPersonBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SearchPersonBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerSearchPersonHolder extends BaseHolder<SearchPersonBean> {
    @Override
    public View initView() {
        ItemListviewSearchPersonBinding itemListviewSearchPersonBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_search_person, null, false);

        return itemListviewSearchPersonBinding.getRoot();
    }

    @Override
    public void refreshView(SearchPersonBean data) {

    }
}
