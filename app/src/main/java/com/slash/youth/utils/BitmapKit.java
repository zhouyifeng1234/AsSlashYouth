package com.slash.youth.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.widget.ImageView;

import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;

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
}
