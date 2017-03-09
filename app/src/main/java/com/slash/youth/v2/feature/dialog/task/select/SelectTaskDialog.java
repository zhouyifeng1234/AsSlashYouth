package com.slash.youth.v2.feature.dialog.task.select;

import android.content.Context;
import android.view.Gravity;

import com.core.op.lib.base.BDialog;
import com.slash.youth.R;
import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.databinding.DlgSelecttaskBinding;
import com.slash.youth.v2.base.BaseDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@RootView(R.layout.dlg_selecttask)
public final class SelectTaskDialog extends BaseDialog<SelectTaskViewModel, DlgSelecttaskBinding> {

    @Inject
    public SelectTaskDialog(RxAppCompatActivity activity, SelectTaskViewModel viewModel) {
        super(BDialog.newDialog(activity)
                .setGravity(Gravity.TOP | Gravity.BOTTOM)
                .setMargin(0, 200, 50, 0)
                .setBackgroud(false)
                .setInAnimation(R.anim.anim_scan_in)
                .setOutAnimation(R.anim.anim_scan_out), viewModel);
    }

    @AfterViews
    void afterViews() {
    }
}
