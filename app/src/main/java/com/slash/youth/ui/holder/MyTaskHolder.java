package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyTaskBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.ui.viewmodel.ItemMyTaskModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskHolder extends BaseHolder<MyTaskBean> {
    @Override
    public View initView() {
        ItemMyTaskBinding itemMyTaskBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_task, null, false);
        ItemMyTaskModel itemMyTaskModel = new ItemMyTaskModel(itemMyTaskBinding);
        itemMyTaskBinding.setItemMyTaskModel(itemMyTaskModel);
        return itemMyTaskBinding.getRoot();
    }

    @Override
    public void refreshView(MyTaskBean data) {

    }
}
