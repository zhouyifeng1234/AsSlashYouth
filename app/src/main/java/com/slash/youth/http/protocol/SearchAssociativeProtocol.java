package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.UserSkillLabelBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/2.       搜索联想词
 */
public class SearchAssociativeProtocol extends BaseProtocol<SearchAssociativeBean> {
    String tag;
    public SearchAssociativeBean searchAssociativeBean;

    public SearchAssociativeProtocol(String tag) {
        this.tag = tag;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_ASSOCIATIVE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        LogKit.v(tag);
      //params.addBodyParameter("tag",tag );
        params.addBodyParameter("tag","张" );
    }

    @Override
    public SearchAssociativeBean parseData(String result) {
        Gson gson = new Gson();
        searchAssociativeBean = gson.fromJson(result, SearchAssociativeBean.class);
        LogKit.d("sssssssssssssssssssssssssssss"+searchAssociativeBean);

        return searchAssociativeBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
