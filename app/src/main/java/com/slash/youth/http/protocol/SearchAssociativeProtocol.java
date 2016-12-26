package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.UserSkillLabelBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PatternUtils;

import org.xutils.http.RequestParams;

/**
 * Created by zss on 2016/10/2.       搜索联想词
 */
public class SearchAssociativeProtocol extends BaseProtocol<SearchAssociativeBean> {
    String tag;
    public SearchAssociativeBean searchAssociativeBean;
    private int offset;
    private int limit;

    public SearchAssociativeProtocol(String tag,int offset,int limit) {
        this.tag = tag;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.SEARCH_ASSOCIATIVE;
    }

    @Override
    public void addRequestParams(RequestParams params) {
      if(tag.length() == 1){
          boolean match = PatternUtils.match(PatternUtils.chineseRegex, tag);
          if(match){//是中文
              params.addBodyParameter("tag",tag);
              params.addBodyParameter("offset", String.valueOf(offset));
              params.addBodyParameter("limit", String.valueOf(limit));
          }
      }else {
          params.addBodyParameter("tag",tag);
          params.addBodyParameter("offset", String.valueOf(offset));
          params.addBodyParameter("limit", String.valueOf(limit));
      }
    }

    @Override
    public SearchAssociativeBean parseData(String result) {
        Gson gson = new Gson();
        searchAssociativeBean = gson.fromJson(result, SearchAssociativeBean.class);
        return searchAssociativeBean;
    }


    @Override
    public boolean checkJsonResult(String result) {
        return true;
    }
}
