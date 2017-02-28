package com.slash.youth.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.ui.activity.ChooseSkillActivity;
import com.slash.youth.ui.activity.GuidActivity;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.activity.MessageActivity;
import com.slash.youth.ui.activity.PerfectInfoActivity;
import com.slash.youth.ui.activity.SplashActivity;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2017/2/25.
 */
public class BaseActivity extends Activity {
    View msgIconLayer;
    boolean isAddMsgIconLayer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //向没个Ativity都添加进入消息列表的icon
        msgIconLayer = View.inflate(CommonUtils.getContext(), R.layout.layer_every_msg_icon, null);
        ImageView ivMsgIcon = (ImageView) msgIconLayer.findViewById(R.id.iv_msg_icon);
        ivMsgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMessageActivity = new Intent(CommonUtils.getContext(), MessageActivity.class);
                startActivity(intentMessageActivity);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (msgIconLayer != null) {
            if (!isAddMsgIconLayer) {
                if (this instanceof SplashActivity || this instanceof LoginActivity || this instanceof PerfectInfoActivity || this instanceof ChooseSkillActivity || this instanceof MessageActivity || this instanceof GuidActivity) {

                } else {
                    this.addContentView(msgIconLayer, new ViewGroup.LayoutParams(-1, -1));
                    isAddMsgIconLayer = true;
                }
            }
        }
    }
}
