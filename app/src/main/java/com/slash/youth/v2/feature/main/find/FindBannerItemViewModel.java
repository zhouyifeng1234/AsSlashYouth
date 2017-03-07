package com.slash.youth.v2.feature.main.find;

import com.core.op.lib.base.BViewModel;
import com.slash.youth.domain.bean.BannerConfigBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by acer on 2017/3/7.
 */

public class FindBannerItemViewModel extends BViewModel {

    public BannerConfigBean.BannerBean bannerBean;

    public FindBannerItemViewModel(RxAppCompatActivity activity, BannerConfigBean.BannerBean bannerBean) {
        super(activity);
        this.bannerBean = bannerBean;
    }
}
