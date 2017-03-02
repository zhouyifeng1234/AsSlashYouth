package com.slash.youth.http.protocol;

import com.google.gson.Gson;
import com.slash.youth.domain.TagRecommendList;
import com.slash.youth.global.GlobalConstants;

import org.xutils.http.RequestParams;

/**
 * 一、[标签]-标签查询 首页分级标签查询接口
 * <p/>
 * Created by zhouyifeng on 2017/3/1.
 */
public class TagRecommendProtocol extends BaseProtocol<TagRecommendList> {

    private String tag;//tag对应的唯一ID
    private String level;//tag的level 1表示一级标签，2表示二级标签
    private String offset;
    private String limit;

    public TagRecommendProtocol(String tag, String level, String offset, String limit) {
        this.tag = tag;
        this.level = level;
        this.offset = offset;
        this.limit = limit;
    }

    @Override
    public String getUrlString() {
        return GlobalConstants.HttpUrl.GET_TAG_RECOMMEND_LIST;
    }

    @Override
    public void addRequestParams(RequestParams params) {
        params.addBodyParameter("tag", tag);
        params.addBodyParameter("level", level);
        params.addBodyParameter("offset", offset);
        params.addBodyParameter("limit", limit);
    }

    @Override
    public TagRecommendList parseData(String result) {
        return tagRecommendList;
    }

    TagRecommendList tagRecommendList;

    @Override
    public boolean checkJsonResult(String result) {
        Gson gson = new Gson();
        tagRecommendList = gson.fromJson(result, TagRecommendList.class);
        if (tagRecommendList.rescode == 0) {
            return true;
        } else {
            return false;
        }
    }
}
