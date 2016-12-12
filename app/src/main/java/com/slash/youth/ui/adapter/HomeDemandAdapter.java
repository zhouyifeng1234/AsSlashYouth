package com.slash.youth.ui.adapter;

import android.app.Activity;

import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeMoreDemandBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeDemandHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeDemandAdapter extends SlashBaseAdapter<FreeTimeDemandBean.DataBean.ListBean> {
    private Activity mActivity;

    public HomeDemandAdapter(ArrayList<FreeTimeDemandBean.DataBean.ListBean> listData,Activity mActivity) {
        super(listData);
        this.mActivity = mActivity;
    }

    @Override
    public ArrayList<FreeTimeDemandBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeDemandHolder(mActivity);
    }
}
