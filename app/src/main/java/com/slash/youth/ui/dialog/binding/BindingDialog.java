package com.slash.youth.ui.dialog.binding;

import android.app.Activity;
import android.view.Gravity;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogBindingBinding;
import com.slash.youth.ui.dialog.base.BDialog;
import com.slash.youth.ui.view.picker.view.DialogBuilder;
import com.slash.youth.utils.inject.RootView;

/**
 * Created by acer on 2017/3/2.
 */
@RootView(R.layout.dialog_binding)
public class BindingDialog extends BDialog<BindingViewModel, DialogBindingBinding> {

    public BindingDialog(Activity activity, BindingViewModel viewModel) {
        super(BDialog.newDialog(activity)
                .setGravity(Gravity.CENTER)
                .setMargin(100, 0, 100, 0), viewModel);
    }
}
