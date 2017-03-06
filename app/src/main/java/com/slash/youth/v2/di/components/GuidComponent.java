package com.slash.youth.v2.di.components;

import com.core.op.lib.di.PerActivity;
import com.slash.youth.v2.di.modules.ActivityModule;
import com.slash.youth.v2.di.modules.GuidModule;
import com.slash.youth.v2.feature.guid.GuidActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, GuidModule.class})
public interface GuidComponent extends ActivityComponent {
    void inject(GuidActivity activity);
}