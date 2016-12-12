package com.slash.youth.ui.adapter;

import com.slash.youth.domain.NewDemandAandServiceBean;
import com.slash.youth.domain.NewTaskUserInfoBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.UserInfoHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/1.
 */
public class UserInfoAdapter extends SlashBaseAdapter<NewDemandAandServiceBean.DataBean.ListBean>{
    public UserInfoAdapter(ArrayList<NewDemandAandServiceBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<NewDemandAandServiceBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new UserInfoHolder();
    }
}
