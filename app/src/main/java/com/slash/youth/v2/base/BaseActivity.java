package com.slash.youth.v2.base;

import android.databinding.ViewDataBinding;

import com.core.op.lib.base.BAViewModel;
import com.core.op.lib.base.BActivity;
import com.slash.youth.BR;
import com.slash.youth.v2.MainApplication;
import com.slash.youth.v2.di.components.AppComponent;
import com.slash.youth.v2.di.modules.ActivityModule;


/**
 * @author op
 * @version 1.0
 * @description
 * @createDate 2016/8/4
 */
public class BaseActivity<V extends BAViewModel, T extends ViewDataBinding> extends BActivity<V, T> {

    @Override
    protected void initBeforeView() {
        super.initBeforeView();
        getApplicationComponent().inject(this);
    }

    @Override
    protected void bindViewModel() {
        binding.setVariable(BR.viewModel, viewModel);
    }

    protected AppComponent getApplicationComponent() {
        return ((MainApplication) getApplication()).getAppComponent();
    }

    /**
     * Get an Activity module for dependency injection.
     */
    protected ActivityModule getActivityModule() {
        return new ActivityModule(this);
    }
}
