package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.UserSkillLabelBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zhouyifeng on 2016/9/4.
 * 获取用户技能标签
 */
public class GetUserSkillLabelProtocol extends BaseProtocol<UserSkillLabelBean> {

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_USER_SKILL_LABEL;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("uid", "20");
    }

    @Override
    public UserSkillLabelBean parseData(String result) {
        Gson gson = new Gson();
        UserSkillLabelBean userSkillLabelBean = gson.fromJson(result, UserSkillLabelBean.class);
        return userSkillLabelBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return false;
    }
}
