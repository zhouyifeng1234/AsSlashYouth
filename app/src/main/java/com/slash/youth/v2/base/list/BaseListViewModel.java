package com.slash.youth.v2.base.list;


import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;

import com.core.op.bindingadapter.common.ItemViewSelector;
import com.core.op.lib.base.BFViewModel;
import com.core.op.lib.base.BViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.core.op.lib.weight.EmptyLayout;
import com.slash.youth.R;
import com.slash.youth.databinding.FrgBaselistBinding;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListViewModel<T extends BViewModel> extends BFViewModel<FrgBaselistBinding> {

    public int currentPage = 0;

    public final List<T> itemViewModels = new ArrayList<>();

    public final ItemViewSelector<T> itemView = itemView();

    public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

    public final ObservableField<Integer> errorVisible = new ObservableField(View.GONE);

    public final ObservableField<Integer> errorType = new ObservableField(EmptyLayout.HIDE_LAYOUT);

    public final ReplyCommand errorClick = new ReplyCommand<>(() -> {
    });

    public final ReplyCommand<Integer> onLoadMoreCommand = new ReplyCommand<>((p) -> {
        loadMore();
    });

    public final ReplyCommand onRefreshCommand = new ReplyCommand<>(() -> {
        refresh();
    });

    public BaseListViewModel(RxAppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void afterViews() {
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.app_theme_colorPrimary);
    }

    public abstract void loadMore();

    public abstract void refresh();

    public abstract ItemViewSelector<T> itemView();

}