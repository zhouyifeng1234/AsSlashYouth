package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.VersionBean;
import com.slash.youth.global.GlobalConstants;
import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/12/27.
 */
public class CheckVersionProtocol  extends BaseProtocol<VersionBean>{
    private int type;
    private long code;

    public CheckVersionProtocol(int type, long code) {
        this.type = type;
        this.code = code;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.CHECK_VERSION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        if(type == 1||type == 2){
            params.addBodyParameter("type", String.valueOf(type));
        }
        if(code>0){
            params.addBodyParameter("code", String.valueOf(code));
        }
    }

    @Override
    public VersionBean parseData(String result) {
        Gson gson = new Gson();
        VersionBean versionBean = gson.fromJson(result, VersionBean.class);
        return versionBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
