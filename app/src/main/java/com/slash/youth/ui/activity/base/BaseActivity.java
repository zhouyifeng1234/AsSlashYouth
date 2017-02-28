package com.slash.youth.ui.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.ui.activity.ChooseSkillActivity;
import com.slash.youth.ui.activity.GuidActivity;
import com.slash.youth.ui.activity.LoginActivity;
import com.slash.youth.ui.activity.MessageActivity;
import com.slash.youth.ui.activity.PerfectInfoActivity;
import com.slash.youth.ui.activity.SplashActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by zhouyifeng on 2017/2/25.
 */
public class BaseActivity extends Activity {
    View msgIconLayer;
    ImageView ivMsgIcon;
    boolean isAddMsgIconLayer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //向没个Ativity都添加进入消息列表的icon
        msgIconLayer = View.inflate(CommonUtils.getContext(), R.layout.layer_every_msg_icon, null);
        ivMsgIcon = (ImageView) msgIconLayer.findViewById(R.id.iv_msg_icon);
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

        if (ivMsgIcon != null) {
            setIvMsgIconState();
            setMsgChangeListener();
        }
    }

    /**
     * 刚进入消息页的时候，或者是回退到消息页的时候(这两种情况都会调用onStart方法)，通过融云的API获取总的未读消息数，消息Icon的颜色
     */
    private void setIvMsgIconState() {
        RongIMClient.getInstance().getUnreadCount(new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                int totalUnreadCount = integer;
                LogKit.v("HomeActivity unReadCount:" + totalUnreadCount);
                if (totalUnreadCount <= 0) {//没有聊天消息，显示灰色的Icon
                    ivMsgIcon.setImageResource(R.mipmap.news_default);
                } else {//有聊天消息，显示红色的Icon
                    ivMsgIcon.setImageResource(R.mipmap.news_activation);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        }, Conversation.ConversationType.PRIVATE);
    }

    /**
     * 注册未读消息的监听器，这样每次来新的聊天消息都能根据未读数量来更新icon颜色
     */
    private void setMsgChangeListener() {
        MsgManager.setTotalUnReadCountListener(new MsgManager.TotalUnReadCountListener() {

            @Override
            public void displayTotalUnReadCount(int count) {
                LogKit.v("HomeActivity unReadCount:" + count);
                if (count <= 0) {//没有聊天消息，显示灰色的Icon
                    ivMsgIcon.setImageResource(R.mipmap.news_default);
                } else {//有聊天消息，显示红色的Icon
                    ivMsgIcon.setImageResource(R.mipmap.news_activation);
                }
            }
        });
    }

}
