package com.slash.youth.ui.adapter;

import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.UserInfoHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/1.
 */
public class UserInfoAdapter extends SlashBaseAdapter<UserInfoItemBean>{
    public UserInfoAdapter(ArrayList<UserInfoItemBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<UserInfoItemBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new UserInfoHolder();
    }
}
