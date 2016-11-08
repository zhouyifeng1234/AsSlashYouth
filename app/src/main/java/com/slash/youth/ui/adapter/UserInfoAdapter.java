package com.slash.youth.ui.adapter;

import com.slash.youth.domain.NewTaskUserInfoBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.UserInfoHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/1.
 */
public class UserInfoAdapter extends SlashBaseAdapter<NewTaskUserInfoBean>{
    public UserInfoAdapter(ArrayList<NewTaskUserInfoBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<NewTaskUserInfoBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new UserInfoHolder();
    }
}
