package com.slash.youth.ui.activity.test;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/10/15.
 */
public class RoundedImageTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView ivTest = new ImageView(CommonUtils.getContext());
        Bitmap resBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.banner);
        Bitmap roundedBitmap = Bitmap.createBitmap(resBitmap.getWidth(), resBitmap.getHeight(), resBitmap.getConfig());
        Canvas canvas = new Canvas(roundedBitmap);
        RectF rectF = new RectF(0, 0, resBitmap.getWidth(), resBitmap.getHeight());
        Paint paintRectF = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint paintBitmap = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintBitmap.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRoundRect(rectF, CommonUtils.dip2px(5), CommonUtils.dip2px(5), paintRectF);
        canvas.drawBitmap(resBitmap, null, rectF, paintBitmap);
        ivTest.setImageBitmap(roundedBitmap);
        setContentView(ivTest);
    }
}
