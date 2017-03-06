package com.slash.youth.v2.di.modules;

import com.core.op.lib.di.PerActivity;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public final class SplashModule {

    public SplashModule() {

    }

    @Provides
    @PerActivity
    RxPermissions getPermissions(RxAppCompatActivity activity) {
        return new RxPermissions(activity);
    }
}
