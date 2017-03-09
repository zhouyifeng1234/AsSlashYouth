package com.slash.youth.v2.feature.login;

import android.content.Context;
import android.content.Intent;

import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.BeforeViews;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActLoginBinding;
import com.slash.youth.v2.base.BaseActivity;
import com.slash.youth.v2.di.components.DaggerLoginComponent;
import com.slash.youth.v2.di.components.LoginComponent;
import com.slash.youth.v2.di.modules.LoginModule;

@RootView(R.layout.act_login)
public final class LoginActivity extends BaseActivity<LoginViewModel, ActLoginBinding> {

    LoginComponent component;

    public final static void instance(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @BeforeViews
    void beforViews() {
        component = DaggerLoginComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .loginModule(new LoginModule())
                .build();
        component.inject(this);
    }

    @AfterViews
    void afterViews() {
    }
}
