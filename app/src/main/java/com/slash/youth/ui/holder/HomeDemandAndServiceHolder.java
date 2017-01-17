package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemDemandLayoutBinding;
import com.slash.youth.databinding.ItemHomeDemandServiceBinding;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.ui.viewmodel.ItemDemandModel;
import com.slash.youth.ui.viewmodel.ItemHomeDemandServiceModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeDemandAndServiceHolder extends BaseHolder<FreeTimeMoreServiceBean.DataBean.ListBean> {

    private ItemHomeDemandServiceModel mItemHomeDemandServiceModel;

    @Override
    public View initView() {
        ItemHomeDemandServiceBinding itemHomeDemandServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_home_demand_service, null, false);
        mItemHomeDemandServiceModel = new ItemHomeDemandServiceModel(itemHomeDemandServiceBinding);
        itemHomeDemandServiceBinding.setItemHomeDemandServiceModel(mItemHomeDemandServiceModel);
        return itemHomeDemandServiceBinding.getRoot();
    }

    @Override
    public void refreshView(FreeTimeMoreServiceBean.DataBean.ListBean data) {
       /* if (data.isDemand) {
            mItemHomeDemandServiceModel.setDemandOrServiceTime("任务时间:9月18日 8:30");
            mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.VISIBLE);
        } else {
            mItemHomeDemandServiceModel.setDemandOrServiceTime("任务时间:每周(一 二 三 四 五 六 日) 8:30");
            mItemHomeDemandServiceModel.setDemandReplyTimeVisibility(View.INVISIBLE);
        }*/
    }
}
