package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPublishDemandDescribeBinding;
import com.slash.youth.ui.viewmodel.PublishDemandDescModel;
import com.slash.youth.utils.LogKit;

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

    private void initListener() {
        mActivityPublishDemandDescribeBinding.ivPublishServiceDescribeAddpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAddPicture = new Intent();
                intentAddPicture.setType("image/*");
                intentAddPicture.setAction(Intent.ACTION_GET_CONTENT);
                intentAddPicture.putExtra("outputX", 50);
                intentAddPicture.putExtra("outputY", 50);
                PublishDemandDescribeActivity.this.startActivityForResult(intentAddPicture, 0);

//        private File tempFile;
//        this.tempFile = new File(FilePathUtility.GetReAudioFilePath(
//                PersonalEditActivity.this, "head.jpg", true));
//                Intent intent = new Intent();
///* 开启Pictures画面Type设定为image */
//                intent.setType("image/*");
///* 使用Intent.ACTION_GET_CONTENT这个Action */
//                intent.setAction(Intent.ACTION_GET_CONTENT);
///* 出现截取界面 */
//                intent.putExtra("crop", "true");
///*保存到SD*/
////        intent.putExtra("output", Uri.fromFile(tempFile));
///*设置图片像素*/
//                intent.putExtra("outputX", 200);
//                intent.putExtra("outputY", 200);
///*设置图片格式*/
//                intent.putExtra("outputFormat", "JPEG");
///* 设置比例 1:1 */
//                intent.putExtra("aspectX", 1);
//                intent.putExtra("aspectY", 1);
///* 取得相片后返回本画面 */
//                PublishDemandDescribeActivity.this.startActivityForResult(intent, 0);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = data.getData();
        LogKit.v("add Picture:" + uri.toString());
        ContentResolver contentResolver = getContentResolver();
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri));
//            x.image().bind(mActivityPublishDemandDescribeBinding.ivAddpic, uri.toString());
            mActivityPublishDemandDescribeBinding.ivAddpic.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
