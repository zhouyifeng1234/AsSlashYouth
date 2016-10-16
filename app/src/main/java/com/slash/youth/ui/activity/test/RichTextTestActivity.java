package com.slash.youth.ui.activity.test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.slash.youth.R;

/**
 * Created by zhouyifeng on 2016/10/16.
 */
public class RichTextTestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rich_text_test);
        TextView tvRichText = (TextView) findViewById(R.id.tv_richtext);
    }
}
