package com.slash.youth.v2.feature.main.task.list;

import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.BeforeViews;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.v2.base.list.BaseListFragment;
import com.slash.youth.v2.di.components.MainComponent;

public final class TaskListFragment extends BaseListFragment<TaskListViewModel> {

    public static TaskListFragment instance() {
        return new TaskListFragment();
    }

    @BeforeViews
    void beforViews() {
        getComponent(MainComponent.class).inject(this);
    }

    @AfterViews
    void afterViews() {
    }
}
