package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.global.GlobalConstants;

import org.antlr.v4.tool.ast.PredAST;
import org.xutils.http.RequestParams;

/**
 * Created by  on 2016/11/20.
 */
public class AddMyCollectionItemProtocol extends BaseProtocol<SetBean> {
    private int type;
    private long tid;

    public AddMyCollectionItemProtocol(int type, long tid) {
        this.type = type;
        this.tid = tid;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.MY_ADD_COLLECTION_ITEM;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("type", String.valueOf(type));
        params.addBodyParameter("tid", String.valueOf(tid));

    }

    @Override
    public SetBean parseData(String result) {
        Gson gson = new Gson();
        SetBean setBean = gson.fromJson(result, SetBean.class);
        return setBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
