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

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;

    @Override
    public View initView() {
        ItemHomeDemandServiceBinding itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        mItemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(mItemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();
    }

    @Override
    public void refreshView(HomeDemandAndServiceBean data) {
        if (data.isDemand) {
            mItemHomeDemandServiceModel.setDemandOrServiceTime("任务时间:9月18日 8:30");
            mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.VISIBLE);
        } else {
            mItemHomeDemandServiceModel.setDemandOrServiceTime("任务时间:每周(一 二 三 四 五 六 日) 8:30");
            mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.INVISIBLE);
        }
    }
}
