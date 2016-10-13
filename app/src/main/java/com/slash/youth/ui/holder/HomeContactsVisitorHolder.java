package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeContactsVisitorBinding;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.ui.viewmodel.HomeContactsVisitorModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeContactsVisitorHolder extends BaseHolder<HomeContactsVisitorBean> {

    private HomeContactsVisitorModel mHomeContactsVisitorModel;

    @Override
    public View initView() {
        ItemListviewHomeContactsVisitorBinding itemListviewHomeContactsVisitorBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_home_contacts_visitor, null, false);
        mHomeContactsVisitorModel = new HomeContactsVisitorModel(itemListviewHomeContactsVisitorBinding);
        itemListviewHomeContactsVisitorBinding.setHomeContactsVisitorModel(mHomeContactsVisitorModel);
        return itemListviewHomeContactsVisitorBinding.getRoot();
    }

    @Override
    public void refreshView(HomeContactsVisitorBean data) {

    }
}
