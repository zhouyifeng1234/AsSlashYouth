package com.slash.youth.ui.adapter;

import com.slash.youth.domain.HomeDemandAndServiceBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeDemandAndServiceHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/12.
 */
public class HomeDemandAndServiceAdapter extends SlashBaseAdapter<HomeDemandAndServiceBean> {
    public HomeDemandAndServiceAdapter(ArrayList<HomeDemandAndServiceBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<HomeDemandAndServiceBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeDemandAndServiceHolder();
    }
}
