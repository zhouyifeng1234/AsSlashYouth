package com.slash.youth.v2.feature.splash;

import android.content.Context;
import android.content.Intent;

import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.BeforeViews;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActSplashBinding;
import com.slash.youth.v2.base.BaseActivity;
import com.slash.youth.v2.di.components.DaggerSplashComponent;
import com.slash.youth.v2.di.components.SplashComponent;
import com.slash.youth.v2.di.modules.SplashModule;

@RootView(R.layout.act_splash)
public final class SplashActivity extends BaseActivity<SplashViewModel, ActSplashBinding> {

    SplashComponent component;

    public final static void instance(Context context) {
        context.startActivity(new Intent(context, SplashActivity.class));
    }

    @BeforeViews
    void beforViews() {
        component = DaggerSplashComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .splashModule(new SplashModule())
                .build();
        component.inject(this);
    }

    @AfterViews
    void afterViews() {
    }
}
