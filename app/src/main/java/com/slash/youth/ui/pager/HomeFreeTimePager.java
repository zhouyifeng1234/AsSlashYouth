package com.slash.youth.ui.pager;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.PagerHomeFreetimeBinding;
import com.slash.youth.ui.viewmodel.PagerHomeFreeTimeModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class HomeFreeTimePager extends BaseHomePager {

    public HomeFreeTimePager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
//        TextView tv = new TextView(CommonUtils.getContext());
//        tv.setText("HomeFreeTimePager");
//        return tv;

        PagerHomeFreetimeBinding pagerHomeFreetimeBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.pager_home_freetime, null, false);
        PagerHomeFreeTimeModel pagerHomeFreeTimeModel = new PagerHomeFreeTimeModel(pagerHomeFreetimeBinding, mActivity);
        pagerHomeFreetimeBinding.setPagerHomeFreeTimeModel(pagerHomeFreeTimeModel);
        return pagerHomeFreetimeBinding.getRoot();
    }

    @Override
    public void initListener() {

    }

    @Override
    public void setData() {

    }
}
