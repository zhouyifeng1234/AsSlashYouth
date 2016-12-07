package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/11/28.
 */
public class ConversationListProtocol extends BaseProtocol<ConversationListBean> {
    String offset;
    String limit;

    public ConversationListProtocol(String offset, String limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_CONVERSATION_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", offset);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public ConversationListBean parseData(String result) {
        return conversationListBean;
    }

    ConversationListBean conversationListBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        conversationListBean = gson.fromJson(result, ConversationListBean.class);
        if (conversationListBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
