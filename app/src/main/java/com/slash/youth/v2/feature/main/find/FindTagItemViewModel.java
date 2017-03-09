package com.slash.youth.v2.feature.main.find;

import android.content.Intent;

import com.core.op.lib.base.BViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.domain.bean.HomeTagInfoBean;
import com.slash.youth.ui.activity.TagRecommendActivity;
import com.slash.youth.utils.CommonUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by acer on 2017/3/7.
 */

public class FindTagItemViewModel extends BViewModel {

    public HomeTagInfoBean.TagInfo tagInfo;

    public final ReplyCommand click = new ReplyCommand(() -> {
        Intent intentTagRecommendActivity = new Intent(CommonUtils.getContext(), TagRecommendActivity.class);
        intentTagRecommendActivity.putExtra("tagId", tagInfo.id);
        intentTagRecommendActivity.putExtra("tagName", tagInfo.name);
        activity.startActivity(intentTagRecommendActivity);
    });

    public FindTagItemViewModel(RxAppCompatActivity activity, HomeTagInfoBean.TagInfo tagInfo) {
        super(activity);
        this.tagInfo = tagInfo;
    }
}
