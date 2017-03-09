package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommentResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 一、[需求]-需求方评价接口
 * <p/>
 * Created by zhouyifeng on 2016/11/14.
 */
public class CommentProtocol extends BaseProtocol<CommentResultBean> {

    String quality;
    String speed;
    String attitude;
    String remark;
    String type;
    String tid;
    String suid;

    public CommentProtocol(String quality, String speed, String attitude, String remark, String type, String tid, String suid) {
        this.quality = quality;
        this.speed = speed;
        this.attitude = attitude;
        this.remark = remark;
        this.type = type;
        this.tid = tid;
        this.suid = suid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.DEMAND_PARTY_COMMENT;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("quality", quality);
        params.addBodyParameter("speed", speed);
        params.addBodyParameter("attitude", attitude);
        params.addBodyParameter("remark", remark);
        params.addBodyParameter("type", type);
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("suid", suid);
    }

    @Override
    public CommentResultBean parseData(String result) {
        return commentResultBean;
    }

    CommentResultBean commentResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commentResultBean = gson.fromJson(result, CommentResultBean.class);
        try {
            if (commentResultBean.rescode == 0 && commentResultBean.data.evaluation.status == 1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
