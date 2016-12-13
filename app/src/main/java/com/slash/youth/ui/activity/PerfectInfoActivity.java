package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.ui.viewmodel.PerfectInfoModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoActivity extends Activity {

    private ActivityPerfectInfoBinding mActivityPerfectInfoBinding;
    private PerfectInfoModel mPerfectInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        mActivityPerfectInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_info);
        mPerfectInfoModel = new PerfectInfoModel(mActivityPerfectInfoBinding, this);
        mActivityPerfectInfoBinding.setPerfectInfoModel(mPerfectInfoModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bitmap bp = (Bitmap) data.getExtras().get("data");
        mPerfectInfoModel.setCameraIconVisibility(View.GONE);
        BitmapKit.bindImage(mActivityPerfectInfoBinding.ivUserAvatar, );
    }
}
