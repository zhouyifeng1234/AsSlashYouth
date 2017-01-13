package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewSubscribeSecondSkilllabelBinding;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.viewmodel.ItemSubscribeSecondSkilllabelModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class SubscribeSecondSkilllabelHolder extends BaseHolder<SkillLabelBean> {

    public static int clickItemPosition = 0;
    private ItemListviewSubscribeSecondSkilllabelBinding mItemListviewSubscribeSecondSkilllabelBinding;
    public ItemSubscribeSecondSkilllabelModel mItemSubscribeSecondSkilllabelModel;

    @Override
    public View initView() {
        mItemListviewSubscribeSecondSkilllabelBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_subscribe_second_skilllabel, null, false);
        mItemSubscribeSecondSkilllabelModel = new ItemSubscribeSecondSkilllabelModel(mItemListviewSubscribeSecondSkilllabelBinding);
        mItemListviewSubscribeSecondSkilllabelBinding.setTtemSubscribeSecondSkilllabelModel(mItemSubscribeSecondSkilllabelModel);
        return mItemListviewSubscribeSecondSkilllabelBinding.getRoot();
    }

    @Override
    public void refreshView(SkillLabelBean data) {
        mItemSubscribeSecondSkilllabelModel.setSecondSkilllabelName(data.tag);
        if (getCurrentPosition() == clickItemPosition) {
            mItemSubscribeSecondSkilllabelModel.setSecondSkilllabelColor(0xff31c5e4);
        } else {
            mItemSubscribeSecondSkilllabelModel.setSecondSkilllabelColor(0xff333333);
        }
    }
}
