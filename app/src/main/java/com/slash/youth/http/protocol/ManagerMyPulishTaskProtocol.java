package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.view.RichEditTextView;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

import java.util.List;

/**
 * Created by zss on 2016/11/30.
 */
public class ManagerMyPulishTaskProtocol extends BaseProtocol<ManagerMyPublishTaskBean> {
    private int offset;
    private int limit;

    public ManagerMyPulishTaskProtocol(int offset, int limit) {
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MANAGE_PUBLISH_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("offset", String.valueOf(offset));
        params.addBodyParameter("limit", String.valueOf(limit));
    }

    @Override
    public ManagerMyPublishTaskBean parseData(String result) {
        Gson gson = new Gson();
        ManagerMyPublishTaskBean managerMyPublishTaskBean = gson.fromJson(result, ManagerMyPublishTaskBean.class);

        return managerMyPublishTaskBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
