package com.slash.youth.ui.adapter;

import com.slash.youth.domain.TagRecommendList;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.TagRecommendHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2017/3/2.
 */
public class TagRecommendAdapter extends SlashBaseAdapter<TagRecommendList.TagRecommendInfo> {


    public TagRecommendAdapter(ArrayList<TagRecommendList.TagRecommendInfo> listData) {
        super(listData);
    }

    @Override
    public ArrayList<TagRecommendList.TagRecommendInfo> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new TagRecommendHolder();
    }
}
