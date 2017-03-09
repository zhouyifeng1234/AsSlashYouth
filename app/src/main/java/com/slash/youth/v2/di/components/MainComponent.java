package com.slash.youth.v2.di.components;

import dagger.Component;


import com.slash.youth.v2.di.modules.ActivityModule;
import com.slash.youth.v2.di.modules.MainModule;
import com.slash.youth.v2.feature.main.MainActivity;
import com.core.op.lib.di.PerActivity;
import com.slash.youth.v2.feature.main.find.FindFragment;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class, MainModule.class})
public interface MainComponent extends ActivityComponent {

    void inject(MainActivity activity);

    void inject(FindFragment fragment);
}