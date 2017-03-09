package com.slash.youth.v2.feature.dialog.task.select;


import com.core.op.lib.base.BViewModel;
import com.core.op.lib.di.PerActivity;
import com.slash.youth.databinding.DlgSelecttaskBinding;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@PerActivity
public class SelectTaskViewModel extends BViewModel<DlgSelecttaskBinding> {

    private final RxAppCompatActivity activity;

    @Inject
    public SelectTaskViewModel(RxAppCompatActivity activity) {
        this.activity = activity;
    }

}