package com.slash.youth.ui.adapter;

import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ChooseSlashFriendHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseFriendAdapter extends SlashBaseAdapter<MyFriendListBean.DataBean.ListBean> {

    public ChooseFriendAdapter(ArrayList<MyFriendListBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<MyFriendListBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new ChooseSlashFriendHolder();
    }
}
