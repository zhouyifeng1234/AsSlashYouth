package com.slash.youth.ui.adapter;

import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class SubscribeSecondSkilllabelAdapter extends  SlashBaseAdapter<SkillLabelBean> {
    public SubscribeSecondSkilllabelAdapter(ArrayList<SkillLabelBean> listData) {
        super(listData);
    }

    @Override
    public ArrayList<SkillLabelBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SubscribeSecondSkilllabelHolder();
    }
}
