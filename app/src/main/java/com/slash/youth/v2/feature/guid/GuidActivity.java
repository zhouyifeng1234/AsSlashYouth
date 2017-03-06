package com.slash.youth.v2.feature.guid;

import android.content.Context;
import android.content.Intent;

import com.core.op.lib.utils.inject.AfterViews;
import com.core.op.lib.utils.inject.BeforeViews;
import com.core.op.lib.utils.inject.RootView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActGuidBinding;
import com.slash.youth.v2.base.BaseActivity;
import com.slash.youth.v2.di.components.DaggerGuidComponent;
import com.slash.youth.v2.di.components.GuidComponent;
import com.slash.youth.v2.di.modules.GuidModule;

@RootView(R.layout.act_guid)
public final class GuidActivity extends BaseActivity<GuidViewModel, ActGuidBinding> {

    GuidComponent component;

    public final static void instance(Context context) {
        context.startActivity(new Intent(context, GuidActivity.class));
    }

    @BeforeViews
    void beforViews() {
        component = DaggerGuidComponent.builder()
                .appComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .guidModule(new GuidModule())
                .build();
        component.inject(this);
    }

    @AfterViews
    void afterViews() {
    }
}
