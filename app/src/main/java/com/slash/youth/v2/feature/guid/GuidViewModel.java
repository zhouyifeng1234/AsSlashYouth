package com.slash.youth.v2.feature.guid;


import com.core.op.lib.base.BAViewModel;
import com.core.op.lib.di.PerActivity;
import com.slash.youth.databinding.ActGuidBinding;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@PerActivity
public class GuidViewModel extends BAViewModel<ActGuidBinding> {


    @Inject
    public GuidViewModel(RxAppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void afterViews() {

    }
}