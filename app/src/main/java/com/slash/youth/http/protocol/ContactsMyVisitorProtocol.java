package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommentResultBean;
import com.slash.youth.domain.HomeContactsVisitorBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.antlr.v4.tool.ast.PredAST;
import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/21.
 */
public class ContactsMyVisitorProtocol extends BaseProtocol<HomeContactsVisitorBean> {
    private int  offset;
    private int  limit;

    public ContactsMyVisitorProtocol(int  offset, int  limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_VISITOR_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", String.valueOf(offset));
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public HomeContactsVisitorBean parseData(String result) {
        Gson gson = new Gson();
        HomeContactsVisitorBean homeContactsVisitorBean = gson.fromJson(result, HomeContactsVisitorBean.class);
        return homeContactsVisitorBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
