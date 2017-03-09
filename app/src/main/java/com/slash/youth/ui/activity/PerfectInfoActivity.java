package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityPerfectInfoBinding;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.viewmodel.PerfectInfoModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class PerfectInfoActivity extends BaseActivity {

    private ActivityPerfectInfoBinding mActivityPerfectInfoBinding;
    private PerfectInfoModel mPerfectInfoModel;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        activity = this;
        mActivityPerfectInfoBinding = DataBindingUtil.setContentView(this, R.layout.activity_perfect_info);
        mPerfectInfoModel = new PerfectInfoModel(mActivityPerfectInfoBinding, this);
        mActivityPerfectInfoBinding.setPerfectInfoModel(mPerfectInfoModel);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogKit.v("resultCode:" + resultCode);
        if (resultCode != -1) {
            return;
        }

        mPerfectInfoModel.setCameraIconVisibility(View.GONE);//隐藏头像中间的摄像机icon
        Bitmap bmpSource = (Bitmap) data.getExtras().get("data");
        // 创建缩略图变换矩阵。
        Matrix m = new Matrix();
        m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, CommonUtils.dip2px(100), CommonUtils.dip2px(100)), Matrix.ScaleToFit.CENTER);
        // 生成缩略图。
        Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);
        File cacheDir = getCacheDir();
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        final File fileThumb = new File(cacheDir, "thumb" + System.currentTimeMillis());
        FileOutputStream fosThumb = null;
        try {
            fosThumb = new FileOutputStream(fileThumb);
            bmpThumb.compress(Bitmap.CompressFormat.JPEG, 100, fosThumb);
            //调用上传图片接口获取fileId
            DemandEngine.uploadFile(new BaseProtocol.IResultExecutor<UploadFileResultBean>() {
                @Override
                public void execute(UploadFileResultBean dataBean) {
                    String fileId = dataBean.data.fileId;
                    mPerfectInfoModel.setUploadAvatarFileId(fileId);
                    mPerfectInfoModel.setIsUploadAvatar(true);
                    //页面上显示头像，直接加载本地缓存的图片
                    ImageOptions.Builder builder = new ImageOptions.Builder();
                    ImageOptions imageOptions = builder.build();
                    builder.setCircular(true);
                    x.image().bind(mActivityPerfectInfoBinding.ivUserAvatar, fileThumb.toURI().toString(), imageOptions);
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.v("上传头像失败：" + result);
                    ToastUtils.shortToast("上传头像失败：" + result);
                }
            }, fileThumb.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fosThumb);
        }


    }
}
