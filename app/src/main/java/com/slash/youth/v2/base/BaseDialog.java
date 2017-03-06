package com.slash.youth.v2.base;

import android.databinding.ViewDataBinding;
import com.slash.youth.BR;
import com.core.op.lib.base.BDialog;
import com.core.op.lib.base.BViewModel;
import com.core.op.lib.weight.picker.view.DialogBuilder;



/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/12/21
 */
public class BaseDialog<V extends BViewModel, T extends ViewDataBinding> extends BDialog<V, T> {

    public BaseDialog(DialogBuilder builder, V viewModel) {
        super(builder, viewModel);
    }

    @Override
    protected void bindViewModel() {
        binding.setVariable(BR.viewModel, viewModel);
    }
}
