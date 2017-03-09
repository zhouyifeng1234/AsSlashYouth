package com.slash.youth.v2.feature.dialog.task.pub;


import com.core.op.lib.base.BViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.core.op.lib.di.PerActivity;
import com.slash.youth.databinding.DlgPubtaskBinding;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@PerActivity
public class PubTaskViewModel extends BViewModel<DlgPubtaskBinding> {

    private final RxAppCompatActivity activity;

    public final ReplyCommand close = new ReplyCommand(() -> {

    });

    public final ReplyCommand demandClick = new ReplyCommand(() -> {

    });

    public final ReplyCommand serviceClick = new ReplyCommand(() -> {

    });


    @Inject
    public PubTaskViewModel(RxAppCompatActivity activity) {
        this.activity = activity;
    }

}