package com.slash.youth.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by zhouyifeng on 2017/2/13.
 */
public class SlashProtocolActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        webView.loadUrl("http://web.slashyounger.com/#!/agreement");
        setContentView(webView);
    }
}
