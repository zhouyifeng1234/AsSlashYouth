package com.slash.youth.ui.dialog.binding;

import android.view.View;

import com.slash.youth.databinding.DialogBindingBinding;
import com.slash.youth.ui.dialog.base.BDialogViewModel;
import com.slash.youth.ui.dialog.base.OnDialogLisetener;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

/**
 * Created by acer on 2017/3/1.
 */

public class BindingViewModel extends BDialogViewModel<DialogBindingBinding> {
    public BindingViewModel(RxAppCompatActivity activity, OnDialogLisetener onDialogLisetener) {
        super(activity, onDialogLisetener);
    }

    public void cancel(View view) {
        if (onDialogLisetener != null) {
            onDialogLisetener.onCancel();
        }
    }

    public void consult(View view) {
        if (onDialogLisetener != null) {
            onDialogLisetener.onConfirm();
        }
    }
}
