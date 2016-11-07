package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ChooseSlashFriendHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseFriendAdapter extends SlashBaseAdapter<SlashFriendBean> {

    public ChooseFriendAdapter(ArrayList<SlashFriendBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SlashFriendBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new ChooseSlashFriendHolder();
    }
}
