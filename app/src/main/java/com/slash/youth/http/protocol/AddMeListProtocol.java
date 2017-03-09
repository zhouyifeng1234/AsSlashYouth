package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/27.
 */
public class AddMeListProtocol extends BaseProtocol<ContactsBean> {
    private int offset;
    private int limit;
    private String url;

    public AddMeListProtocol(int offset, int limit, String url ) {
        this.offset = offset;
        this.limit = limit;
        this.url = url;
    }

    @Override
    public String getUrlString() {
        return url;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", String.valueOf(offset));
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public ContactsBean parseData(String result) {
        Gson gson = new Gson();
        ContactsBean contactsBean = gson.fromJson(result, ContactsBean.class);
        return contactsBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
