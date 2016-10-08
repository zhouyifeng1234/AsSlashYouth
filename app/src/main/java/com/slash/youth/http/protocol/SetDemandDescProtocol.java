package com.slash.youth.http.protocol;

import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 十五、[需求]-设置需求描述信息
 * Created by zhouyifeng on 2016/10/8.
 */
public class SetDemandDescProtocol extends BaseProtocol<String> {
    private String id;// 需求ID	>=0
    private String desc;//需求描述信息	长度小于4096，该接口只能在服务方未确认前修改，服务方一旦确认该接口设置失效

    public SetDemandDescProtocol(String id, String desc) {
        this.id = id;
        this.desc = desc;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SET_DEMAND_DESC;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("id", id);
        params.addBodyParameter("desc", desc);
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
