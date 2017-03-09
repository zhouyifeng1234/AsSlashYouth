package com.slash.youth.ui.adapter;

import android.app.Activity;

import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeServiceHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeServiceAdapter extends SlashBaseAdapter<FreeTimeServiceBean.DataBean.ListBean> {
    private Activity mActivity;

    public HomeServiceAdapter(ArrayList<FreeTimeServiceBean.DataBean.ListBean> listData, Activity mActivity) {
        super(listData);
        this.mActivity = mActivity;
    }

    @Override
    public ArrayList<FreeTimeServiceBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeServiceHolder(mActivity);
    }
}
