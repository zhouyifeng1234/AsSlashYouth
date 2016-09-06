package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeDemandBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeDemandHolder extends BaseHolder<DemandBean> {
    @Override
    public View initView() {
        ItemListviewHomeDemandBinding itemListviewHomeDemandBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_home_demand, null, false);

        return itemListviewHomeDemandBinding.getRoot();
    }

    @Override
    public void refreshView(DemandBean data) {

    }
}
