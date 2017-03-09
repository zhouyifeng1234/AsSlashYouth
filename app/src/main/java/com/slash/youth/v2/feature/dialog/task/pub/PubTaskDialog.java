package com.slash.youth.v2.feature.dialog.task.pub;

import android.content.Context;
import android.view.Gravity;

import com.core.op.lib.base.BDialog;
import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.databinding.DlgPubtaskBinding;
import com.slash.youth.v2.base.BaseDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@RootView(R.layout.dlg_pubtask)
public final class PubTaskDialog extends BaseDialog<PubTaskViewModel, DlgPubtaskBinding> {

    @Inject
    public PubTaskDialog(RxAppCompatActivity context, PubTaskViewModel viewModel) {
        super(BDialog.newDialog(context)
                        .setGravity(Gravity.CENTER)
                        .setMargin(100, 0, 100, 0)
                , viewModel);
    }

    @AfterViews
    void afterViews() {
    }
}
