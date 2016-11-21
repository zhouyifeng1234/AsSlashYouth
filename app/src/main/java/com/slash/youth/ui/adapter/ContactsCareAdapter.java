package com.slash.youth.ui.adapter;

import com.slash.youth.domain.ContactsBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ContactsCareHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareAdapter extends SlashBaseAdapter<ContactsBean> {

    private ContactsCareHolder contactsCareHolder;

    public ContactsCareAdapter(ArrayList<ContactsBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<ContactsBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        contactsCareHolder = new ContactsCareHolder();
        return contactsCareHolder;
    }
}
