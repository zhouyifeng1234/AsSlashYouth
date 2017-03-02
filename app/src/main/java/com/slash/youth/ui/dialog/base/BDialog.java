package com.slash.youth.ui.dialog.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;

import com.slash.youth.BR;
import com.slash.youth.ui.view.picker.view.BasePickerView;
import com.slash.youth.ui.view.picker.view.DialogBuilder;
import com.slash.youth.utils.inject.InjectUtil;

/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/12/21
 */
public abstract class BDialog<V extends BViewModel, T extends ViewDataBinding> extends BasePickerView {

    protected LayoutInflater inflater;

    protected V viewModel;

    protected T binding;

    public BDialog(DialogBuilder builder, V viewModel) {
        super(builder);
        this.viewModel = viewModel;
        binding = DataBindingUtil.
                inflate(LayoutInflater.from(context), InjectUtil.injectFrgRootView(this), contentContainer, true);
        viewModel.setBinding(binding);
        bindViewModel();
        InjectUtil.injectAfterView(this);
    }

    public static DialogBuilder newDialog(Context context) {
        return new DialogBuilder(context);
    }

    protected void bindViewModel() {
        binding.setVariable(BR.viewModel, viewModel);
    }

}
