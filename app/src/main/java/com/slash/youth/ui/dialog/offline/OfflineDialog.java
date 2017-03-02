package com.slash.youth.ui.dialog.offline;

import android.app.Activity;
import android.view.Gravity;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogOfflineBinding;
import com.slash.youth.ui.dialog.base.BDialog;
import com.slash.youth.ui.view.picker.view.DialogBuilder;
import com.slash.youth.utils.inject.RootView;

@RootView(R.layout.dialog_offline)
public class OfflineDialog extends BDialog<OfflineViewModel, DialogOfflineBinding> {
    public OfflineDialog(Activity activity, OfflineViewModel viewModel) {
        super(BDialog.newDialog(activity)
                .setGravity(Gravity.CENTER)
                .setMargin(30, 0, 30, 0)
                .setCancelable(false), viewModel);
    }
}
