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
import com.slash.youth.http.protocol.BaseProtocol;

import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import javax.net.ssl.SSLContext;

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
        bindImage(iv, url, null, -1);
    }

    public static void bindImage(ImageView iv, String url, int width, int height) {
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
                SSLContext sslContext = BaseProtocol.getSSLContext(CommonUtils.getApplication());
                params.setSslSocketFactory(sslContext.getSocketFactory());

                params.addHeader("uid", LoginManager.currentLoginUserId + "");
                params.addHeader("pass", "1");

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

        builder.setSize(width, height);
        builder.setSquare(true);
        builder.setLoadingDrawableId(R.mipmap.default_avatar);//设置加载过程中的图片
        builder.setFailureDrawableId(R.mipmap.default_avatar);//设置加载失败后的图片
        builder.setImageScaleType(ImageView.ScaleType.CENTER);
        x.image().bind(iv, url, imageOptions);
    }

    public static void bindImage(ImageView iv, String url, ImageView.ScaleType scaleType, int connerRadius) {
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
                SSLContext sslContext = BaseProtocol.getSSLContext(CommonUtils.getApplication());
                params.setSslSocketFactory(sslContext.getSocketFactory());

                params.addHeader("uid", LoginManager.currentLoginUserId + "");
                params.addHeader("pass", "1");

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
        if (connerRadius == -1) {
            //默认值
            builder.setRadius(DensityUtil.dip2px(5));
        } else {
            builder.setRadius(DensityUtil.dip2px(connerRadius));
        }
        builder.setSquare(true);
        builder.setLoadingDrawableId(R.mipmap.default_avatar);//设置加载过程中的图片
        builder.setFailureDrawableId(R.mipmap.default_avatar);//设置加载失败后的图片
        if (scaleType != null) {
            builder.setImageScaleType(scaleType);
        }
        x.image().bind(iv, url, imageOptions);
    }

    /**
     * 这个方法只用来加载融云的远程图片(融云保存在七牛云服务器上的原图链接，https的图片地址)
     */
    public static void bindImageFromRongClound(ImageView iv, String url) {
        ImageOptions.Builder builder = new ImageOptions.Builder();
        ImageOptions imageOptions = builder.build();
        builder.setLoadingDrawableId(R.mipmap.default_avatar);//设置加载过程中的图片
        builder.setFailureDrawableId(R.mipmap.default_avatar);//设置加载失败后的图片
        builder.setImageScaleType(ImageView.ScaleType.FIT_CENTER);
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
