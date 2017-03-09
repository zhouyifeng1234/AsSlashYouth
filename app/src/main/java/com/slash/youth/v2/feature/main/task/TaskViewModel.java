package com.slash.youth.v2.feature.main.task;


import android.databinding.ObservableField;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.core.op.lib.base.BFViewModel;
import com.core.op.lib.command.ReplyCommand;
import com.core.op.lib.di.PerActivity;
import com.core.op.lib.messenger.Messenger;
import com.slash.youth.R;
import com.slash.youth.databinding.FrgTaskBinding;
import com.slash.youth.v2.feature.dialog.task.pub.PubTaskDialog;
import com.slash.youth.v2.feature.dialog.task.select.SelectTaskDialog;
import com.slash.youth.v2.feature.main.task.list.TaskListFragment;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.trello.rxlifecycle.components.support.RxFragment;

import javax.inject.Inject;

import static com.slash.youth.v2.feature.main.MainViewModel.CHANG_POSITION;

@PerActivity
public class TaskViewModel extends BFViewModel<FrgTaskBinding> {

    public final static String SHOW_NODATA = "SHOW_NODATA";

    private SelectTaskDialog selectTaskDialog;

    private PubTaskDialog pubTaskDialog;

    public final ObservableField<Integer> noDataVisible = new ObservableField<>(View.GONE);

    public final ReplyCommand taskClick = new ReplyCommand(() -> {
        if (selectTaskDialog != null && !selectTaskDialog.isShowing()) {
            selectTaskDialog.show();
        }
    });

    public final ReplyCommand pubClick = new ReplyCommand(() -> {
        if (pubTaskDialog != null && !pubTaskDialog.isShowing()) {
            pubTaskDialog.show();
        }
    });

    public final ReplyCommand lookClick = new ReplyCommand(() -> {
        Messenger.getDefault().sendNoMsg(CHANG_POSITION);
    });

    @Inject
    public TaskViewModel(RxAppCompatActivity activity,
                         PubTaskDialog pubTaskDialog,
                         SelectTaskDialog selectTaskDialog) {
        super(activity);
        this.pubTaskDialog = pubTaskDialog;
        this.selectTaskDialog = selectTaskDialog;
    }

    @Override
    public void afterViews() {
        addFragment(R.id.fl_container, TaskListFragment.instance());
        Messenger.getDefault().register(activity, SHOW_NODATA, () -> {
            noDataVisible.set(View.VISIBLE);
        });
    }
}