package com.slash.youth.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import com.sina.weibo.sdk.utils.ResourceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
        ImageOptions.Builder builder = new ImageOptions.Builder();
        ImageOptions imageOptions = builder.build();
        builder.setParamsBuilder(new ImageOptions.ParamsBuilder() {
            @Override
            public RequestParams buildParams(RequestParams params, ImageOptions options) {
                params.addHeader("uid", "10000");
                params.addHeader("pass", "1");
                return params;
            }
        });
        x.image().bind(iv, url, imageOptions);
    }

    //获取手机相片和手机相册中的图片处理
    public static Bitmap zoomBitmap(Bitmap srcBitmap,int width,int height) {
        int w = CommonUtils.dip2px(width);
        int h = CommonUtils.dip2px(height);
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(output);
       Paint paint = new Paint();
       paint.setAntiAlias(true);//抗锯齿
        canvas.drawBitmap(srcBitmap, null, new Rect(0, 0, w, h),paint );
        return output;
    }


    /**
     * 保存图片文件
     * @param bmp
     * @return 返回true如果图片保存失败或图片已存在，否则返回false
     */
    public static String  saveBitmap(Bitmap bmp,String fileName) {
        FileOutputStream fos = null;
        File file = new File(CommonUtils.getApplication().getCacheDir(),fileName);
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
