package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.ui.view.fly.RandomLayout;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/12/11.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView ivSplash = new ImageView(CommonUtils.getContext());
        ivSplash.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ViewGroup.LayoutParams ll = new RandomLayout.LayoutParams(-1, -1);
        ivSplash.setLayoutParams(ll);
        ivSplash.setImageResource(R.mipmap.splash);
        setContentView(ivSplash);

        CommonUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentLoginActivity = new Intent(CommonUtils.getContext(), LoginActivity.class);
                startActivity(intentLoginActivity);
                finish();
            }
        }, 3000);
    }
}
