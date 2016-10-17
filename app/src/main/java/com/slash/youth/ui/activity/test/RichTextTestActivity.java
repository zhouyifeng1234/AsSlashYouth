package com.slash.youth.ui.activity.test;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.EditText;

import com.slash.youth.R;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zhouyifeng on 2016/10/16.
 */
public class RichTextTestActivity extends Activity {

    private EditText mEtRichText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rich_text_test);
        mEtRichText = (EditText) findViewById(R.id.et_richtext);
    }

    //文本和图片一起显示
    public void disTextAndImg(View v) {
        SpannableString ss = new SpannableString("今天我们一起出去玩吧！！！");
        Drawable drawable = getResources().getDrawable(R.mipmap.task_icon);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        ImageSpan imgSpan = new ImageSpan(drawable);

        ss.setSpan(imgSpan, 4, 9, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        mEtRichText.setText(ss);
    }

    //文本中某段文字变色
    public void changePartTextColor(View v) {
        SpannableString ss = new SpannableString("Hello,Good morning,Good Afternoon");
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(0xffff0000);
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(0xff00ff00);
        ss.setSpan(foregroundColorSpan, 0, 4, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(backgroundColorSpan, 10, 15, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        mEtRichText.setText(ss);
    }

    //给某段文字添加超链接
    public void setHyperlinkOnPartText(View v) {
        String text = "详情请点击<a href='http://www.baidu.com'>百度</a>lll";
        Spanned spanned = Html.fromHtml(text);
        mEtRichText.setText(spanned);
        mEtRichText.setMovementMethod(LinkMovementMethod.getInstance());

        //经测试，使用这种方式，图片和超链接似乎无法同时使用（可能有其它的实现方式）
//        SpannableString ss = new SpannableString("Hello,Good morning,Good Afternoon");
//        Drawable drawable = getResources().getDrawable(R.mipmap.task_icon);
//        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
//        ImageSpan imgSpan = new ImageSpan(drawable);
//        String hyperlinkText = "详情请点击<a href='http://www.baidu.com'>百度</a>";
//        Spanned hyperlinkSpanned = Html.fromHtml(hyperlinkText);
//
//        ss.setSpan(imgSpan, 4, 9, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//        ss.setSpan(hyperlinkSpanned, 10, 15, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
//        mEtRichText.setText(ss);
//        mEtRichText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    //让某段文字可以被点击并自定义点击的逻辑操作
    public void setOnClickOnPartText(View v) {
        String text = "one,two,three,four";
        SpannableString ss = new SpannableString(text);
        SlashUrlSpan slashUrlSpan = new SlashUrlSpan("http://www.baidu.com");
        ss.setSpan(slashUrlSpan, 4, 7, SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        mEtRichText.setText(ss);
        mEtRichText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public class SlashUrlSpan extends URLSpan {

        public SlashUrlSpan(String url) {
            super(url);
        }

        @Override
        public void onClick(View widget) {
//            super.onClick(widget);
            ToastUtils.shortToast(getURL());
            widget.clearFocus();
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(ds.linkColor);
//            ds.setUnderlineText(true);
        }
    }

    public void disText(View v) {
        ToastUtils.shortToast(mEtRichText.getText().toString());
    }

    public void insertContentInCursor(View v) {


        int selectionStart = mEtRichText.getSelectionStart();
        int selectionEnd = mEtRichText.getSelectionEnd();
        LogKit.v("selectionStart:" + selectionStart);
        LogKit.v("selectionEnd:" + selectionEnd);

        String text = mEtRichText.getText().toString();
        LogKit.v(text);


        Object[] spans = mEtRichText.getText().getSpans(0, text.length(), Object.class);
        LogKit.v("spans:" + spans.length);
        int spanStart = mEtRichText.getText().getSpanStart(spans[4]);
        LogKit.v("spanStart:" + spanStart);
    }
}
