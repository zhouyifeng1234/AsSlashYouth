package com.slash.youth.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;

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
                RelativeLayout llPicView = createPicView(bitmap, fileIndex);
                llPicLine.addView(llPicView);
            }
            if (listFilePath.size() < maxPicCount) {
                RelativeLayout llAddPicView = createPicView(null, -1);
                llPicLine.addView(llAddPicView);
            }
        } else {
            for (int i = 0; i < 3; i++) {
                int fileIndex = lineIndex * 3 + i;
                String filePath = listFilePath.get(fileIndex);
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                RelativeLayout llPicView = createPicView(bitmap, fileIndex);
                llPicLine.addView(llPicView);
            }
        }

        llPicLine.setLayoutParams(paramsPicLine);
        return llPicLine;
    }

    public RelativeLayout createPicView(Bitmap bitmap, int fileIndex) {
        LayoutParams paramsPicView = new LayoutParams(-2, -2);
        RelativeLayout rlPicView = new RelativeLayout(CommonUtils.getContext());
        paramsPicView.leftMargin = CommonUtils.dip2px(7);

        RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(CommonUtils.dip2px(91), CommonUtils.dip2px(91));
        ImageView imageView = new ImageView(CommonUtils.getContext());
        imageView.setLayoutParams(paramsImageView);
        paramsImageView.leftMargin = CommonUtils.dip2px(10);
        paramsImageView.topMargin = CommonUtils.dip2px(10);

        RelativeLayout.LayoutParams paramsIvBtnDelPic = new RelativeLayout.LayoutParams(-2, -2);
        ImageButton ivbtnDelPic = new ImageButton(CommonUtils.getContext());
        ivbtnDelPic.setBackgroundColor(Color.TRANSPARENT);
        ivbtnDelPic.setImageResource(R.mipmap.guanbi_icon);
        ivbtnDelPic.setLayoutParams(paramsIvBtnDelPic);

        if (bitmap == null) {
            ivbtnDelPic.setVisibility(View.INVISIBLE);
            imageView.setImageResource(R.mipmap.jiatu_icon);
            imageView.setOnClickListener(new AddPicClickListener());
        } else {
            imageView.setImageBitmap(bitmap);
            ivbtnDelPic.setOnClickListener(new DelPicClickListener());
            ivbtnDelPic.setTag(fileIndex);
        }

        rlPicView.setLayoutParams(paramsPicView);
        rlPicView.addView(imageView);
        rlPicView.addView(ivbtnDelPic);
        return rlPicView;
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
                    try {
                        Bitmap bitmap = data.getParcelableExtra("data");
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
                    }
                }
            }
        }

    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public ArrayList<String> getAddedPicTempPath() {
        return listFilePath;
    }

}
