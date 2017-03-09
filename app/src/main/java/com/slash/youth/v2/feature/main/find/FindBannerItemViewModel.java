package com.slash.youth.v2.feature.main.find;

import android.content.Intent;

import com.core.op.lib.base.BViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.ui.activity.WebViewActivity;
import com.slash.youth.utils.CommonUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by acer on 2017/3/7.
 */

public class FindBannerItemViewModel extends BViewModel {

    public BannerConfigBean.BannerBean bannerBean;

    public final ReplyCommand click = new ReplyCommand(() -> {
        Intent intentCommonQuestionActivity = new Intent(CommonUtils.getContext(), WebViewActivity.class);
        intentCommonQuestionActivity.putExtra("title", bannerBean.getTitle());
        intentCommonQuestionActivity.putExtra("bannerUrl", bannerBean.getUrl());
        activity.startActivity(intentCommonQuestionActivity);
    });


    public FindBannerItemViewModel(RxAppCompatActivity activity, BannerConfigBean.BannerBean bannerBean) {
        super(activity);
        this.bannerBean = bannerBean;
    }
}
