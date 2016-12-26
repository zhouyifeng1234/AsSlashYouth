package com.slash.youth.ui.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.domain.ContactsBean;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ContactsCareHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareAdapter extends SlashBaseAdapter<ContactsBean.DataBean.ListBean> {

    private ContactsCareHolder contactsCareHolder;
    private int type;

    public ContactsCareAdapter(ArrayList<ContactsBean.DataBean.ListBean> listData, int type) {
        super(listData);
        this.type = type;
    }

    @Override
    public ArrayList<ContactsBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        contactsCareHolder = new ContactsCareHolder(type);
        return contactsCareHolder;
    }

}
