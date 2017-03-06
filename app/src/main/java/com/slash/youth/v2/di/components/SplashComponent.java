package com.slash.youth.v2.di.components;

import com.core.op.lib.di.PerActivity;
import com.slash.youth.v2.di.modules.ActivityModule;
import com.slash.youth.v2.di.modules.SplashModule;
import com.slash.youth.v2.feature.splash.SplashActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, SplashModule.class})
public interface SplashComponent extends ActivityComponent {
    void inject(SplashActivity activity);
}