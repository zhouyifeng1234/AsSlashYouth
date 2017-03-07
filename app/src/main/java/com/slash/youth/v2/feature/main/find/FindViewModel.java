package com.slash.youth.v2.feature.main.find;


import com.core.op.bindingadapter.common.BaseItemViewSelector;
import com.core.op.bindingadapter.common.ItemView;
import com.core.op.bindingadapter.common.ItemViewSelector;
import com.core.op.lib.di.PerActivity;
import com.slash.youth.R;
import com.slash.youth.BR;
import com.slash.youth.v2.base.list.BaseListViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@PerActivity
public class FindViewModel extends BaseListViewModel<FindItemViewModel> {

    @Inject
    public FindViewModel(RxAppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void afterViews() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void refresh() {

    }

    @Override
    public ItemViewSelector<FindItemViewModel> itemView() {
        return new BaseItemViewSelector<FindItemViewModel>() {
            @Override
            public void select(ItemView itemView, int position, FindItemViewModel item) {
                if (position == 0) {
                    itemView.set(BR.viewModel, R.layout.item_main_find_banner);
                } else {
//                    itemView.set(BR.viewModel, R.layout.item_main_find_tags);
                }
            }

            @Override
            public int viewTypeCount() {
                return 4;
            }
        };
    }
}