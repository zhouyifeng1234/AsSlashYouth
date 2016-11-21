package com.slash.youth.ui.holder;

import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareHolder extends BaseHolder<ContactsBean> {
    @Override
    public View initView() {
        View itemView = View.inflate(CommonUtils.getContext(), R.layout.item_contacts_care, null);

        return itemView;
    }

    @Override
    public void refreshView(ContactsBean data) {

    }
}
