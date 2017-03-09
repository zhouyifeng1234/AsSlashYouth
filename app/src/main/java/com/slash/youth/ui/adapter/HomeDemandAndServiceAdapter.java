package com.slash.youth.ui.adapter;

import com.slash.youth.domain.FreeTimeDemandBean;
import com.slash.youth.domain.FreeTimeMoreServiceBean;
import com.slash.youth.domain.FreeTimeServiceBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeServiceHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeDemandAndServiceAdapter extends SlashBaseAdapter<FreeTimeServiceBean.DataBean.ListBean> {
    public HomeDemandAndServiceAdapter(ArrayList<FreeTimeServiceBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<FreeTimeServiceBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        //new HomeServiceHolder()
        return null ;
    }
}
