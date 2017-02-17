package com.slash.youth.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.slash.youth.R;

/**
 * Created by zhouyifeng on 2017/2/13.
 */
public class SlashProtocolActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slash_protocol);
        initData();
        initListener();
    }

    private void initData() {
        WebView webviewSlashProtocol = (WebView) findViewById(R.id.webview_slash_protocol);
        webviewSlashProtocol.getSettings().setJavaScriptEnabled(true);
        webviewSlashProtocol.loadUrl("http://web.slashyounger.com/#!/agreement?nav=1");
    }

    private void initListener() {
        ImageView ivGoBack = (ImageView) findViewById(R.id.iv_goback);
        ivGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
