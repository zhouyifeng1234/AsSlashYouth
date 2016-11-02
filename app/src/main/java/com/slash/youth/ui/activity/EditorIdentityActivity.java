package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityEditorIdentityBinding;
import com.slash.youth.ui.viewmodel.ActivityUserInfoEditorModel;
import com.slash.youth.ui.viewmodel.EditorIdentityModel;

/**
 * Created by zss on 2016/11/1.
 */
public class EditorIdentityActivity extends Activity implements View.OnClickListener {

    private ActivityEditorIdentityBinding activityEditorIdentityBinding;
    private View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEditorIdentityBinding = DataBindingUtil.setContentView(this, R.layout.activity_editor_identity);
        EditorIdentityModel editorIdentityModel = new EditorIdentityModel(activityEditorIdentityBinding);
        activityEditorIdentityBinding.setEditorIdentityModel(editorIdentityModel);

        initView();
        back();

    }

    private void initView() {
        TextView title = (TextView) findViewById(R.id.tv_userinfo_title);
        title.setText("编辑斜杠职业");
        TextView tvfinish = (TextView) findViewById(R.id. tv_userinfo_save);
        tvfinish.setVisibility(View.VISIBLE);
        tvfinish.setText("完成");
        tvfinish.setOnClickListener(this);
        ImageView ivmenu = (ImageView) findViewById(R.id.iv_userinfo_menu);
        ivmenu.setVisibility(View.GONE);

    }

    //返回键
    private void back() {
        findViewById(R.id.iv_userinfo_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //点击事件
    @Override
    public void onClick(View v) {
        //完成返回，是保存数据的，携带数据返回到上一个页面
        finish();
    }
}
