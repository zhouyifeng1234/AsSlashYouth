package com.slash.youth.http.protocol;

import com.slash.youth.domain.CommentResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/11/14.
 */
public class CommentProtocol extends  BaseProtocol<CommentResultBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public CommentResultBean parseData(String result) {
        return null;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return false;
    }
}
