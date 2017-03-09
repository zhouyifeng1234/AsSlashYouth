package com.slash.youth.ui.adapter;

import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.HomeInfoListHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class HomeInfoListAdapter extends SlashBaseAdapter<ConversationListBean.ConversationInfo> {

    public HomeInfoListAdapter(ArrayList<ConversationListBean.ConversationInfo> listData) {
        super(listData);
    }

    @Override
    public ArrayList<ConversationListBean.ConversationInfo> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new HomeInfoListHolder();
    }
}
