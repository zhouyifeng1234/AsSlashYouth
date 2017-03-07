package com.slash.youth.v2.feature.main.find;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.core.op.bindingadapter.common.ItemView;
import com.core.op.lib.base.BViewModel;
import com.core.op.lib.weight.BannerView;
import com.slash.youth.R;
import com.slash.youth.BR;
import com.slash.youth.domain.bean.BannerConfigBean;
import com.slash.youth.domain.bean.HomeTagInfoBean;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by acer on 2017/3/7.
 */

public class FindItemViewModel extends BViewModel {

    public BannerConfigBean bannerConfigBean;

    public HomeTagInfoBean homeTagInfoBean;


    public final ItemView itemView = ItemView.of(BR.viewModel, R.layout.item_main_find_banner);
    public final ObservableList<FindBannerItemViewModel> viewModels = new ObservableArrayList<>();


    public FindItemViewModel(RxAppCompatActivity activity, BannerConfigBean bannerConfigBean) {
        super(activity);
        this.bannerConfigBean = bannerConfigBean;
    }

    public FindItemViewModel(RxAppCompatActivity activity, HomeTagInfoBean homeTagInfoBean) {
        super(activity);
        this.homeTagInfoBean = homeTagInfoBean;
    }
}
