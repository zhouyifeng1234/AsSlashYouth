package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.TagRecommendList;
import com.slash.youth.ui.viewmodel.DemandDetailModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2017/3/2.
 */
public class TagRecommendHolder extends BaseHolder<TagRecommendList.TagRecommendInfo> {

    @Override
    public View initView() {
        DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_tag_recommend, null, false);


        DemandDetailModel
    }

    @Override
    public void refreshView(TagRecommendList.TagRecommendInfo data) {
        //data.type为2是服务，其它的为需求
        if (data.type == 2) {//服务

        } else {//需求，这里希求的type可能是0

        }
    }
}
