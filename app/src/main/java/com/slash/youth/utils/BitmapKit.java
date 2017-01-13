package com.slash.youth.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by zhouyifeng on 2016/10/15.
 */
public class BitmapKit {

    /**
     * 创建圆角图片
     *
     * @param srcBitmap 原图
     * @param radius    圆角半径，单位为“dp”
     * @return
     */
    public static Bitmap createRoundedBitmap(Bitmap srcBitmap, int radius) {
//        LogKit.v("createRoundedBitmap");
        Bitmap roundedBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), srcBitmap.getConfig());
        Canvas canvas = new Canvas(roundedBitmap);
        RectF rectF = new RectF(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
        Paint paintRectF = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBitmap.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRoundRect(rectF, CommonUtils.dip2px(radius), CommonUtils.dip2px(radius), paintRectF);
        canvas.drawBitmap(srcBitmap, null, rectF, paintBitmap);
        return roundedBitmap;
    }


    public static void bindImage(ImageView iv, String url) {
        //这里对url做个容错
        if (url.contains("?fileId=http")) {
            String[] urlInfo = url.split("\\?fileId=");
            url = urlInfo[urlInfo.length - 1];
        }

        ImageOptions.Builder builder = new ImageOptions.Builder();
        ImageOptions imageOptions = builder.build();
        builder.setParamsBuilder(new ImageOptions.ParamsBuilder() {
            @Override
            public RequestParams buildParams(RequestParams params, ImageOptions options) {
                params.addHeader("uid", LoginManager.currentLoginUserId + "");
//                params.addHeader("pass", "1");

                params.addHeader("token", LoginManager.token);
                String url = GlobalConstants.HttpUrl.IMG_DOWNLOAD;
                Map headerMap = AuthHeaderUtils.getBasicAuthHeader("POST", url);
                String date = (String) headerMap.get("Date");
                String authorizationStr = (String) headerMap.get("Authorization");
                params.addHeader("Date", date);
                params.addHeader("Authorization", authorizationStr);
                return params;
            }
        });
//        builder.setCircular(true);//设置显示圆形图片
        builder.setRadius(DensityUtil.dip2px(5));
        // builder.setSize(CommonUtils.dip2px(59),CommonUtils.dip2px(59));//zss
        builder.setSquare(true);
        builder.setLoadingDrawableId(R.mipmap.default_avatar);//设置加载过程中的图片
        builder.setFailureDrawableId(R.mipmap.default_avatar);//设置加载失败后的图片
        x.image().bind(iv, url, imageOptions);
    }

    //获取手机相片和手机相册中的图片处理
    public static Bitmap zoomBitmap(Bitmap srcBitmap, int width, int height) {
        int w = CommonUtils.dip2px(width);
        int h = CommonUtils.dip2px(height);
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);//抗锯齿
        canvas.drawBitmap(srcBitmap, null, new Rect(0, 0, w, h), paint);
        return output;
    }


    /**
     * 保存图片文件
     *
     * @param bmp
     * @return 返回true如果图片保存失败或图片已存在，否则返回false
     */
    public static String saveBitmap(Bitmap bmp, String fileName) {
        FileOutputStream fos = null;
        File file = new File(CommonUtils.getApplication().getCacheDir(), fileName);
        try {
            fos = new FileOutputStream(file);
            if (null != fos) {
                bmp.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (IOException e) {

        }
        return file.getAbsolutePath();
    }
}
