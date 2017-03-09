package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityCommentBinding;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.CommentModel;

/**
 * 当服务方顺利完成需求方的任务之后，需求方对服务方评价
 * <p/>
 * Created by zhouyifeng on 2016/11/11.
 */
public class CommentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityCommentBinding activityCommentBinding = DataBindingUtil.setContentView(this, R.layout.activity_comment);
        CommentModel commentModel = new CommentModel(activityCommentBinding, this);
        activityCommentBinding.setCommentModel(commentModel);
    }
}
