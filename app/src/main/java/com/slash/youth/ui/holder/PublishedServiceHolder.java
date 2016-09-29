package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewPublishedServiceBinding;
import com.slash.youth.domain.PublishedServiceBean;
import com.slash.youth.ui.viewmodel.ItemPublishedServiceModel;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/19.
 */
public class PublishedServiceHolder extends BaseHolder<PublishedServiceBean> {

    public ItemPublishedServiceModel mItemPublishedServiceModel;
    public static ArrayList<Integer> listChoosedItemIndex = new ArrayList<Integer>();
    public ItemListviewPublishedServiceBinding mItemListviewPublishedServiceBinding;

    @Override
    public View initView() {
        mItemListviewPublishedServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_published_service, null, false);
        mItemPublishedServiceModel = new ItemPublishedServiceModel();
        mItemListviewPublishedServiceBinding.setItemPublishedServiceModel(mItemPublishedServiceModel);
        return mItemListviewPublishedServiceBinding.getRoot();
    }

    @Override
    public void refreshView(PublishedServiceBean data) {
        mItemPublishedServiceModel.setTitle(data.title);
        mItemPublishedServiceModel.setPrice(data.price);
        mItemPublishedServiceModel.setType(data.type);
        mItemPublishedServiceModel.setBuyinfo(data.buyCount + "人购买过");

        if (listChoosedItemIndex.contains(getCurrentPosition())) {
            mItemPublishedServiceModel.setChooseBgColor(0xff31C5E4);//选中状态的背景色
            mItemListviewPublishedServiceBinding.ivPublishedServiceChecked.setImageResource(R.mipmap.icon_check);
        } else {
            mItemPublishedServiceModel.setChooseBgColor(0xffF8F8F9);//未选中状态的背景色
            mItemListviewPublishedServiceBinding.ivPublishedServiceChecked.setImageResource(R.mipmap.icon_moren);
        }
    }
}
