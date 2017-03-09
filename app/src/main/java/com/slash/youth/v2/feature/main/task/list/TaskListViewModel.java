package com.slash.youth.v2.feature.main.task.list;


import com.core.op.bindingadapter.common.BaseItemViewSelector;
import com.core.op.bindingadapter.common.ItemView;
import com.core.op.bindingadapter.common.ItemViewSelector;
import com.core.op.lib.di.PerActivity;
import com.core.op.lib.messenger.Messenger;
import com.core.op.lib.weight.picker.lib.WheelView;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.domain.interactor.main.TaskListUseCase;
import com.slash.youth.v2.base.list.BaseListViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

import rx.Observable;

import static com.slash.youth.v2.feature.main.task.TaskViewModel.SHOW_NODATA;

@PerActivity
public class TaskListViewModel extends BaseListViewModel<TaskListItemViewModel> {
    TaskListUseCase useCase;

    @Inject
    public TaskListViewModel(RxAppCompatActivity activity,
                             TaskListUseCase useCase) {
        super(activity);
        this.useCase = useCase;
    }

    @Override
    public void afterViews() {
        super.afterViews();
        loadData();
    }

    @Override
    public void loadMore() {
    }

    @Override
    public void refresh() {
        loadData();
    }

    public void loadData() {
        isRefreshing.set(true);
        useCase.setParams("{\"limit\":\"20\"}");
        useCase.execute().compose(activity.bindToLifecycle())
                .flatMap(data -> {
                    if (data.getList() != null && data.getList().size() == 0) {
                        Messenger.getDefault().sendNoMsg(SHOW_NODATA);
                        return null;
                    }
                    itemViewModels.clear();
                    itemViewModels.add(new TaskListItemViewModel());
                    return Observable.from(data.getList());
                })
                .subscribe(d -> {
                    itemViewModels.add(new TaskListItemViewModel(activity, d));
                }, error -> {
                    isRefreshing.set(false);
                }, () -> {
                    binding.recyclerView.getAdapter().notifyDataSetChanged();
                    isRefreshing.set(false);
                });
    }

    @Override
    public ItemViewSelector<TaskListItemViewModel> itemView() {
        return new BaseItemViewSelector<TaskListItemViewModel>() {
            @Override
            public void select(ItemView itemView, int position, TaskListItemViewModel item) {
                if (position == 0) {
                    itemView.set(BR.viewModel, R.layout.item_main_task_title);
                } else {
                    itemView.set(BR.viewModel, R.layout.item_main_task);
                }
            }
        };
    }
}