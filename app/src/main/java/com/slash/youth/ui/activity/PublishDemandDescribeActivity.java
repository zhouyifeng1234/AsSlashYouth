package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.viewmodel.PublishDemandDescModel;
import com.slash.youth.utils.CommonUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by zhouyifeng on 2016/9/21.
 */
public class PublishDemandDescribeActivity extends Activity {

    private ActivityPublishDemandDescribeBinding mActivityPublishDemandDescribeBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle publishDemandDataBundle = getIntent().getBundleExtra("publishDemandDataBundle");

        mActivityPublishDemandDescribeBinding = DataBindingUtil.setContentView(this, R.layout.activity_publish_demand_describe);
        PublishDemandDescModel publishDemandDescModel = new PublishDemandDescModel(mActivityPublishDemandDescribeBinding, this, publishDemandDataBundle);
        mActivityPublishDemandDescribeBinding.setPublishDemandDescModel(publishDemandDescModel);

        initListener();
    }

    private File mTempFile;

    private void initListener() {
        mActivityPublishDemandDescribeBinding.ivPublishServiceDescribeAddpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String picCachePath = getCacheDir().getAbsoluteFile() + "/picache/";
//                String picCachePath = "/storage/emulated/0/SlashYouth/";
                File cacheDir = new File(picCachePath);
                if (!cacheDir.exists()) {
                    cacheDir.mkdir();
                }
                mTempFile = new File(picCachePath + "tempfile.jpeg");
                Intent intentAddPicture = new Intent();
                intentAddPicture.setType("image/*");
                intentAddPicture.setAction(Intent.ACTION_GET_CONTENT);
                intentAddPicture.putExtra("crop", "true");
//                intentAddPicture.putExtra("output", Uri.fromFile(mTempFile));
                intentAddPicture.putExtra("outputX", CommonUtils.dip2px(91));
                intentAddPicture.putExtra("outputY", CommonUtils.dip2px(91));
                intentAddPicture.putExtra("outputFormat", "JPEG");
                intentAddPicture.putExtra("aspectX", 1);
                intentAddPicture.putExtra("aspectY", 1);
                intentAddPicture.putExtra("return-data", true);
                PublishDemandDescribeActivity.this.startActivityForResult(intentAddPicture, 10);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Uri uri = data.getData();
//        LogKit.v("add Picture:" + uri.toString());
//        ContentResolver contentResolver = getContentResolver();
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
////            x.image().bind(mActivityPublishDemandDescribeBinding.ivAddpic, uri.toString());
//            mActivityPublishDemandDescribeBinding.ivAddpic.setImageBitmap(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (resultCode == RESULT_OK) {
            if (requestCode == 10) {
                try {
//                if (mTempFile.exists()) {

                    Bitmap bitmap = data.getParcelableExtra("data");
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(mTempFile));
                    mActivityPublishDemandDescribeBinding.ivAddpic.setImageBitmap(bitmap);
//                mActivityPublishDemandDescribeBinding.ivAddpic.setImageDrawable(Drawable.createFromPath(mTempFile.getAbsolutePath()));
//                }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
