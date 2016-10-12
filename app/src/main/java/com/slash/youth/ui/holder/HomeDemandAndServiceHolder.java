package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.HomeDemandAndServiceBean;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeDemandAndServiceHolder extends BaseHolder<HomeDemandAndServiceBean> {
    @Override
    public View initView() {
        ItemHomeDemandServiceBinding itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        ItemHomeDemandServiceModel itemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(itemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();
    }

    @Override
    public void refreshView(HomeDemandAndServiceBean data) {

    }
}
