package com.slash.youth.ui.adapter;

import com.slash.youth.domain.ContactsBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ContactsCareHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareAdapter extends SlashBaseAdapter<ContactsBean.DataBean.ListBean> {

    private ContactsCareHolder contactsCareHolder;

    public ContactsCareAdapter(ArrayList<ContactsBean.DataBean.ListBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<ContactsBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        contactsCareHolder = new ContactsCareHolder();
        return contactsCareHolder;
    }
}
