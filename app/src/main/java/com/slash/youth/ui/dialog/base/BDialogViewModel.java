package com.slash.youth.ui.dialog.base;

import android.app.Activity;


/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/7/21
 */
public class BDialogViewModel<T> extends BViewModel<T> {
    protected OnDialogLisetener onDialogLisetener;

    public BDialogViewModel(Activity activity) {
        super(activity);
    }

    public BDialogViewModel(Activity activity, OnDialogLisetener onDialogLisetener) {
        super(activity);
        this.onDialogLisetener = onDialogLisetener;
    }
}
