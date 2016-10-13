package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeInfoBinding;
import com.slash.youth.domain.HomeInfoBean;
import com.slash.youth.ui.viewmodel.ItemHomeInfoModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class HomeInfoListHolder extends BaseHolder<HomeInfoBean> {

    private ItemHomeInfoModel mItemHomeInfoModel;

    @Override
    public View initView() {
        ItemListviewHomeInfoBinding itemListviewHomeInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_home_info, null, false);
        mItemHomeInfoModel = new ItemHomeInfoModel(itemListviewHomeInfoBinding);
        itemListviewHomeInfoBinding.setItemHomeInfoModel(mItemHomeInfoModel);
        return itemListviewHomeInfoBinding.getRoot();
    }

    @Override
    public void refreshView(HomeInfoBean data) {
        if (data.isSlashLittleHelper) {
            mItemHomeInfoModel.setUsername("斜杠小助手");
            mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.INVISIBLE);
            mItemHomeInfoModel.setAddVVisibility(View.GONE);
            mItemHomeInfoModel.setUserLabelsInfoVisibility(View.GONE);
        } else {
            mItemHomeInfoModel.setUsername(data.username);
            if (data.hasRelatedTasks) {
                mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.VISIBLE);
            } else {
                mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.INVISIBLE);
            }
            if (data.isAddV) {
                mItemHomeInfoModel.setAddVVisibility(View.VISIBLE);
            } else {
                mItemHomeInfoModel.setAddVVisibility(View.INVISIBLE);
            }
            mItemHomeInfoModel.setUserLabelsInfoVisibility(View.VISIBLE);
        }
    }
}
