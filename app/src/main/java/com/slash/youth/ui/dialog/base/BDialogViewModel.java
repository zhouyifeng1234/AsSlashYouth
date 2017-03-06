package com.slash.youth.ui.dialog.base;

import android.app.Activity;

import com.core.op.lib.base.BViewModel;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/7/21
 */
public class BDialogViewModel<T> extends BViewModel<T> {
    protected OnDialogLisetener onDialogLisetener;

    public BDialogViewModel(RxAppCompatActivity activity) {
        super(activity);
    }

    public BDialogViewModel(RxAppCompatActivity activity, OnDialogLisetener onDialogLisetener) {
        super(activity);
        this.onDialogLisetener = onDialogLisetener;
    }
}
