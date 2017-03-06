package com.slash.youth.v2.feature.login;


import com.core.op.lib.base.BAViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.core.op.lib.di.PerActivity;
import com.slash.youth.databinding.ActLoginBinding;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;

@PerActivity
public class LoginViewModel extends BAViewModel<ActLoginBinding> {

    public final ReplyCommand help = new ReplyCommand(() -> {

    });

    public final ReplyCommand qqLogin = new ReplyCommand(() -> {

    });

    public final ReplyCommand weixinLogin = new ReplyCommand(() -> {

    });

    public final ReplyCommand protocol = new ReplyCommand(() -> {

    });

    public final ReplyCommand agreeProtocol = new ReplyCommand(() -> {

    });

    public final ReplyCommand login = new ReplyCommand(() -> {

    });

    public final ReplyCommand sendVerify = new ReplyCommand(() -> {

    });


    @Inject
    public LoginViewModel(RxAppCompatActivity activity) {
        super(activity);
    }

    @Override
    public void afterViews() {

    }
}