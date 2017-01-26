package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.view.View;
import android.widget.Toast;

import com.slash.youth.databinding.ActivityUserinfoBinding;
import com.slash.youth.databinding.DialogRecommendBinding;
import com.slash.youth.domain.ChatCmdBusinesssCardBean;
import com.slash.youth.ui.activity.MyFriendActivtiy;
import com.slash.youth.ui.activity.UserInfoActivity;
import com.slash.youth.utils.CommonUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/23.
 */
public class DialogRecommendModel extends BaseObservable {
    private   DialogRecommendBinding dialogRecommendBinding;
 private UMImage image ;
 private UserInfoActivity userInfoActivity;
private ChatCmdBusinesssCardBean chatCmdBusinesssCardBean;
private ActivityUserinfoBinding activityUserinfoBinding;
 private SHARE_MEDIA share_media = SHARE_MEDIA.ALIPAY;
 public ArrayList<SnsPlatform> platforms = new ArrayList<SnsPlatform>();

    public DialogRecommendModel(DialogRecommendBinding dialogRecommendBinding,UserInfoActivity userInfoActivity,ActivityUserinfoBinding activityUserinfoBinding,ChatCmdBusinesssCardBean chatCmdBusinesssCardBean) {
        this.dialogRecommendBinding = dialogRecommendBinding;
     this.userInfoActivity = userInfoActivity;
     this.activityUserinfoBinding = activityUserinfoBinding;
      this.chatCmdBusinesssCardBean = chatCmdBusinesssCardBean;
        //添加平台
        initPlatforms();
    }

    //微信
    public void weixin(View view){
        share_media = platforms.get(7).mPlatform;
        ShareAction shareAction = new ShareAction(userInfoActivity);
        shareAction.setPlatform(share_media).setCallback(umShareListener).share();

    }

    //朋友空间
    public void friendPlace(View view){
        share_media = platforms.get(8).mPlatform;
        ShareAction shareAction = new ShareAction(userInfoActivity);
        shareAction.setPlatform(share_media).setCallback(umShareListener).share();

    }

    //qq
    public  void qq(View view){
        share_media = platforms.get(5).mPlatform;
        ShareAction shareAction = new ShareAction(userInfoActivity);
        shareAction.setPlatform(share_media).setCallback(umShareListener).share();
    }

    //空间
    public void qqSpace(View view){
        //好友列表

        Intent intentChooseFriendActivtiy = new Intent(CommonUtils.getContext(), MyFriendActivtiy.class);
        intentChooseFriendActivtiy.putExtra("sendFriend",true);
        intentChooseFriendActivtiy.putExtra("ChatCmdBusinesssCardBean",chatCmdBusinesssCardBean);
        intentChooseFriendActivtiy.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentChooseFriendActivtiy);

      /* share_media = platforms.get(4).mPlatform;
        ShareAction shareAction = new ShareAction(userInfoActivity);
        shareAction.setPlatform(share_media).setCallback(umShareListener).share();*/
    }

    //取消
    public void cannel(View view){
        activityUserinfoBinding.flDialogContainer.removeView(dialogRecommendBinding.getRoot());
    }


    //加载平台
    private void initPlatforms(){
        platforms.clear();
        for (SHARE_MEDIA e : SHARE_MEDIA.values()) {
            if (!e.toString().equals(SHARE_MEDIA.GENERIC.toString())){
                platforms.add(e.toSnsPlatform());
            }
        }
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(userInfoActivity,platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(userInfoActivity,platform + " 分享失败啦"+t.getMessage(), Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(userInfoActivity,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };
}
