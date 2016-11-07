package com.slash.youth.ui.adapter;

import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.MyCollectionHolder;
import com.slash.youth.ui.holder.MySkillManageHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class MyCollectionAdapter extends SlashBaseAdapter<MyCollectionBean> {

    public MyCollectionAdapter(ArrayList<MyCollectionBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<MyCollectionBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {

        return new MyCollectionHolder();
    }
}
