package com.slash.youth.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyCollectionBinding;
import com.slash.youth.engine.MyManager;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.MyCollectionModel;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private FrameLayout fl;
    private ActivityMyCollectionBinding activityMyCollectionBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMyCollectionBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_collection);
        MyCollectionModel myCollectionModel = new MyCollectionModel(activityMyCollectionBinding,this);
        activityMyCollectionBinding.setMyCollectionModel(myCollectionModel);
        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText(MyManager.MY_COLLECTION);
        fl = (FrameLayout) findViewById(R.id.fl_title_right);
        fl.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_userinfo_back:
                finish();
                break;
        }
    }
}
