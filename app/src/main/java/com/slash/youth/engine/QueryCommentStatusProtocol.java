package com.slash.youth.engine;

import com.google.gson.Gson;
import com.slash.youth.domain.CommentStatusBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/12/26.
 */
public class QueryCommentStatusProtocol extends BaseProtocol<CommentStatusBean> {

    private String tid;
    private String type;

    public QueryCommentStatusProtocol(String tid, String type) {
        this.tid = tid;
        this.type = type;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.QUERY_COMMENT_STATUS;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("type", type);
    }

    @Override
    public CommentStatusBean parseData(String result) {
        return commentStatusBean;
    }

    CommentStatusBean commentStatusBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commentStatusBean = gson.fromJson(result, CommentStatusBean.class);
        if (commentStatusBean.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
