package com.slash.youth.ui.dialog.offline;

import android.app.Activity;
import android.view.Gravity;

import com.core.op.lib.base.BDialog;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.databinding.DialogOfflineBinding;
import com.slash.youth.v2.base.BaseDialog;

@RootView(R.layout.dialog_offline)
public class OfflineDialog extends BaseDialog<OfflineViewModel, DialogOfflineBinding> {
    public OfflineDialog(Activity activity, OfflineViewModel viewModel) {
        super(BDialog.newDialog(activity)
                .setGravity(Gravity.CENTER)
                .setMargin(100, 0, 100, 0)
                .setCancelable(false), viewModel);
    }
}
