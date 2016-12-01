package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.PersonRelationBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/11/21.
 */
public class PersonRelationProtocol extends BaseProtocol<PersonRelationBean> {
    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.PERSON_RELATION_FIRST_PAGER;
    }

    @Override
    public void addRequestParams(RequestParams params) {

    }

    @Override
    public PersonRelationBean parseData(String result) {
        Gson gson = new Gson();
        PersonRelationBean personRelationBean = gson.fromJson(result, PersonRelationBean.class);
        return personRelationBean;
    }

    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
