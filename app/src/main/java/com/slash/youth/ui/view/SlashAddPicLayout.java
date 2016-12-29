package com.slash.youth.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/26.
 */
public class SlashAddPicLayout extends LinearLayout {
    public SlashAddPicLayout(Context context) {
        super(context);
    }

    public SlashAddPicLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlashAddPicLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    ArrayList<String> listFilePath = new ArrayList<String>();
    int maxPicCount = 5;
    Activity mActivity;

    public void addPic() {
        removeAllViews();
        int lineCount = listFilePath.size() / 3;
        for (int i = 0; i < lineCount; i++) {
            LinearLayout picLine = createPicLine(false, i);
            addView(picLine);
        }
        LinearLayout lastPicLine = createPicLine(true, lineCount);
        addView(lastPicLine);
    }

    public void initPic() {
        addPic();
    }

    public LinearLayout createPicLine(boolean isLastLine, int lineIndex) {
        LayoutParams paramsPicLine = new LayoutParams(-1, -2);
        LinearLayout llPicLine = new LinearLayout(CommonUtils.getContext());
        llPicLine.setOrientation(LinearLayout.HORIZONTAL);
        if (isLastLine) {
            int lastLinePicCount = listFilePath.size() % 3;
            for (int i = 0; i < lastLinePicCount; i++) {
                int fileIndex = lineIndex * 3 + i;
                String filePath = listFilePath.get(fileIndex);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                FrameLayout flPicView = createPicView(bitmap, fileIndex);
                llPicLine.addView(flPicView);
            }
            if (listFilePath.size() < maxPicCount) {
                FrameLayout flAddPicView = createPicView(null, -1);
                llPicLine.addView(flAddPicView);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                int fileIndex = lineIndex * 3 + i;
                String filePath = listFilePath.get(fileIndex);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                FrameLayout flPicView = createPicView(bitmap, fileIndex);
                llPicLine.addView(flPicView);
            }
        }

        llPicLine.setLayoutParams(paramsPicLine);
        return llPicLine;
    }


    public FrameLayout createPicView(Bitmap bitmap, int fileIndex) {
        LinearLayout.LayoutParams paramsPicView = new LinearLayout.LayoutParams(-2, -2);
        FrameLayout flPicView = new FrameLayout(CommonUtils.getContext());
        paramsPicView.leftMargin = CommonUtils.dip2px(7);

        FrameLayout.LayoutParams paramsImageView = new FrameLayout.LayoutParams(CommonUtils.dip2px(91), CommonUtils.dip2px(91));
        ImageView imageView = new ImageView(CommonUtils.getContext());
        imageView.setLayoutParams(paramsImageView);
        paramsImageView.leftMargin = CommonUtils.dip2px(10);
        paramsImageView.topMargin = CommonUtils.dip2px(16);

        FrameLayout.LayoutParams paramsIvBtnDelPic = new FrameLayout.LayoutParams(-2, -2);
        ImageButton ivbtnDelPic = new ImageButton(CommonUtils.getContext());
        ivbtnDelPic.setBackgroundColor(Color.TRANSPARENT);
        ivbtnDelPic.setImageResource(R.mipmap.guanbi_icon);
        ivbtnDelPic.setLayoutParams(paramsIvBtnDelPic);

        FrameLayout.LayoutParams paramsTvPicOrder = new FrameLayout.LayoutParams(CommonUtils.dip2px(30), CommonUtils.dip2px(13));
        TextView tvPicOrder = new TextView(CommonUtils.getContext());
        tvPicOrder.setBackgroundColor(0xff31C5E4);
        tvPicOrder.setTextColor(0xffFFFFFF);
        tvPicOrder.setTextSize(9);
        tvPicOrder.setGravity(Gravity.CENTER);
        paramsTvPicOrder.topMargin = CommonUtils.dip2px(16);
//        paramsTvPicOrder.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsTvPicOrder.gravity = Gravity.RIGHT;
        tvPicOrder.setLayoutParams(paramsTvPicOrder);

        if (bitmap == null) {
            ivbtnDelPic.setVisibility(View.INVISIBLE);
            tvPicOrder.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.mipmap.upload_pictures_icon);
            imageView.setOnClickListener(new AddPicClickListener());
        } else {
            imageView.setImageBitmap(bitmap);
            ivbtnDelPic.setOnClickListener(new DelPicClickListener());
            ivbtnDelPic.setTag(fileIndex);
            tvPicOrder.setText("图片" + (fileIndex + 1));
        }

        flPicView.setLayoutParams(paramsPicView);
        flPicView.addView(imageView);
        flPicView.addView(ivbtnDelPic);
        flPicView.addView(tvPicOrder);
        return flPicView;
    }

    public class DelPicClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int fileIndex = (int) v.getTag();
            String tempFilePath = listFilePath.get(fileIndex);
            File tempFile = new File(tempFilePath);
            tempFile.delete();
            listFilePath.remove(fileIndex);
            initPic();
        }
    }

    public class AddPicClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intentAddPicture = new Intent();
            intentAddPicture.setType("image/*");
            intentAddPicture.setAction(Intent.ACTION_GET_CONTENT);
            intentAddPicture.putExtra("crop", "true");
            intentAddPicture.putExtra("outputX", CommonUtils.dip2px(91));
            intentAddPicture.putExtra("outputY", CommonUtils.dip2px(91));
            intentAddPicture.putExtra("outputFormat", "JPEG");
            intentAddPicture.putExtra("aspectX", 1);
            intentAddPicture.putExtra("aspectY", 1);
            intentAddPicture.putExtra("return-data", true);
            mActivity.startActivityForResult(intentAddPicture, 10);
        }
    }

    public void addPicResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == mActivity.RESULT_OK) {
            if (requestCode == 10) {
                if (data != null) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = data.getParcelableExtra("data");
                        if (bitmap == null) {
                            Uri uri = data.getData();
                            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                            Rect rect = new Rect(0, 0, CommonUtils.dip2px(91), CommonUtils.dip2px(91));
                            bitmap = BitmapFactory.decodeStream(CommonUtils.getContext().getContentResolver().openInputStream(uri), rect, bitmapOptions);
                        }
                        LogKit.v("bitmap:" + bitmap);
                        String picCachePath = mActivity.getCacheDir().getAbsoluteFile() + "/picache/";
                        File cacheDir = new File(picCachePath);
                        if (!cacheDir.exists()) {
                            cacheDir.mkdir();
                        }
                        File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(tempFile));
                        listFilePath.add(tempFile.getAbsolutePath());
                        addPic();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
                        if (bitmap != null) {
                            bitmap.recycle();
                            bitmap = null;
                        }
                    }
                }
            }
        }

    }

    /**
     * 这个方法主要是用在修改需求或者服务时候，回填之前上传的图片
     *
     * @param picPath
     */
    public void reloadPic(String picPath) {
        listFilePath.add(picPath);
        addPic();
    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public ArrayList<String> getAddedPicTempPath() {
        return listFilePath;
    }

}
