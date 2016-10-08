package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十、[需求]-我发布的历史需求列表
 * Created by zhouyifeng on 2016/10/8.
 */
public class MyPublishHistoryDemandListProtocol extends BaseProtocol<String> {

    private String offset;//分页 >=0
    private String limit;//分页 >=0 最大20 如果设置=0 则系统默认10

    public MyPublishHistoryDemandListProtocol(String offset, String limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_PUBLISH_HOSTORY_DEMAND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", offset);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public String parseData(String result) {
        return result;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
