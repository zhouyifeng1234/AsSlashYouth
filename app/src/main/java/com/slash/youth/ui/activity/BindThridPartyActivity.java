package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityMyThridPartyBinding;
import com.slash.youth.databinding.PagerHomeMyBinding;
import com.slash.youth.ui.viewmodel.PagerHomeMyModel;
import com.slash.youth.ui.viewmodel.SheildModel;
import com.slash.youth.ui.viewmodel.ThirdPartyModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/11/4.
 */
public class BindThridPartyActivity extends Activity implements View.OnClickListener {

    private TextView title;
    private FrameLayout fl;
    private ActivityMyThridPartyBinding activityMyThridPartyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMyThridPartyBinding = DataBindingUtil.setContentView(this, R.layout.activity_my_thrid_party);
        ThirdPartyModel thirdPartyModel = new ThirdPartyModel(activityMyThridPartyBinding,this);
        activityMyThridPartyBinding.setThirdPartyModel(thirdPartyModel);

        listener();
    }

    private void listener() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(this);
        title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("第三方账号");
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
