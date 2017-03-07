package com.slash.youth.v2.feature.main;

import android.content.Context;
import android.content.Intent;

import com.slash.youth.R;
import com.slash.youth.v2.base.BaseActivity;
import com.slash.youth.databinding.ActMainBinding;
import com.slash.youth.v2.di.components.DaggerMainComponent;
import com.slash.youth.v2.di.components.MainComponent;
import com.slash.youth.v2.di.modules.MainModule;

import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.BeforeViews;
import com.core.op.lib.utils.inject.RootView;

import javax.inject.Inject;

@RootView(R.layout.act_main)
public final class MainActivity extends BaseActivity<MainViewModel, ActMainBinding> {

    MainComponent component;

    public final static void instance(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @BeforeViews
    void beforViews() {
        component = DaggerMainComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .mainModule(new MainModule())
                .build();
        component.inject(this);
    }

    @AfterViews
    void afterViews() {
    }
}
