package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ShareReportResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 二、[分享]-服务者分享上报接口
 * <p/>
 * Created by zhouyifeng on 2017/2/21.
 */
public class ShareReportProtocol extends BaseProtocol<ShareReportResultBean> {

    private String type;//1需求 2服务
    private String tid;//需求or服务ID
    private String rsslink;//1QQ好友、2QQ空间、4微信好友、8微信朋友圈


    public ShareReportProtocol(String type, String tid, String rsslink) {
        this.type = type;
        this.tid = tid;
        this.rsslink = rsslink;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SHARE_REPORT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("type", type);
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("rsslink", rsslink);
    }

    @Override
    public ShareReportResultBean parseData(String result) {
        return shareReportResultBean;
    }

    ShareReportResultBean shareReportResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        shareReportResultBean = gson.fromJson(result, ShareReportResultBean.class);
        if (shareReportResultBean.rescode == 0 && shareReportResultBean.evaluation != null && shareReportResultBean.evaluation.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
