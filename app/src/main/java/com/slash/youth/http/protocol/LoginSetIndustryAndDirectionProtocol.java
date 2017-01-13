package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.CommonResultBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 登录后 完善技能标签,设置行业和方向（一级和二级标签）
 * <p/>
 * Created by zhouyifeng on 2017/1/13.
 */
public class LoginSetIndustryAndDirectionProtocol extends BaseProtocol<CommonResultBean> {

    String industry;
    String direction;

    public LoginSetIndustryAndDirectionProtocol(String industry, String direction) {
        this.industry = industry;
        this.direction = direction;
    }


    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.LOGIN_SET_INDUSTRY_DIRECTION;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("industry", industry);
        params.addBodyParameter("direction", direction);
    }

    @Override
    public CommonResultBean parseData(String result) {
        return commonResultBean;
    }

    CommonResultBean commonResultBean;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        commonResultBean = gson.fromJson(result, CommonResultBean.class);
        if (commonResultBean.rescode == 0 && commonResultBean.data.status == 1) {
            return true;
        } else {
            return false;
        }
    }
}
