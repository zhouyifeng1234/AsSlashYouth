package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.ItemUserinfoBinding;
import com.slash.youth.domain.NewTaskUserInfoBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.viewmodel.ItemUserInfoModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/11/1.
 */
public class UserInfoHolder extends BaseHolder<NewTaskUserInfoBean> {

    private ItemUserinfoBinding itemUserinfoBinding;

    @Override
    public View initView() {
        itemUserinfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_userinfo, null, false);
        ItemUserInfoModel itemUserInfoModel = new ItemUserInfoModel(itemUserinfoBinding);
        itemUserinfoBinding.setItemUserInfoModel(itemUserInfoModel);
        return itemUserinfoBinding.getRoot();
    }

    @Override
    public void refreshView(NewTaskUserInfoBean data) {


    }



}
