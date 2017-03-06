package com.slash.youth.v2.di.components;

import com.core.op.lib.di.PerActivity;
import com.slash.youth.v2.di.modules.ActivityModule;
import com.slash.youth.v2.di.modules.LoginModule;
import com.slash.youth.v2.feature.login.LoginActivity;

import dagger.Component;


@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, LoginModule.class})
public interface LoginComponent extends ActivityComponent {
    void inject(LoginActivity activity);
}