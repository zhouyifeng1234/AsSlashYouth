package com.slash.youth.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.tool.reflection.Callable;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityResultAllBinding;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.databinding.SearchListviewAssociationBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.databinding.SearchPagerFirstBinding;
import com.slash.youth.ui.viewmodel.ActivitySearchModel;
import com.slash.youth.ui.viewmodel.SearchActivityHotServiceModel;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.ui.viewmodel.SearchPagerFirstModel;
import com.slash.youth.ui.viewmodel.SearchResultAllModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.game.UMGameAgent;
import com.umeng.analytics.social.UMPlatformData;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;

import java.util.Map;


/**
 * Created by zss on 2016/9/18.
 */
public class SearchActivity extends Activity {
    public SearchNeedResultTabBinding searchNeedResultTabBinding;
    public SearchActivityHotServiceBinding searchActivityHotServiceBinding;
    public ActivitySearchBinding activitySearchBinding;
    public ActivityResultAllBinding activityResultAllBinding;
    public SearchPagerFirstBinding searchPagerFirstBinding;
    public String checkedFirstLabel = "未选择";
    public int mPage = 0;
    private SearchPagerFirstModel searchPagerFirstModel;
    public SearchListviewAssociationBinding searchListviewAssociationBinding;
    // TODO  这个token是测试用的，一定要换成请求过来的，具体看文档
    private String Token = "d6bCQsXiupB/4OyGkh+TOrI6ZiT8q7s0UEaMPWY0lMxmHdi1v/AAJxOma4aYXyaivfPIJjNHdE+FMH9kV/Jrxg==";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.setCurrentActivity(this);
        //加载搜索框页面
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        ActivitySearchModel activitySearchModel = new ActivitySearchModel(activitySearchBinding);
        activitySearchBinding.setActivitySearchModel(activitySearchModel);
        CommonUtils.setCurrentActivity(SearchActivity.this);

        initView();

        removeView();

        UMengCount();

        RongYunPush();

        listener();

    }


    //加载页面
    private void initView() {
        //加载首页
        searchPagerFirstBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getApplication()), R.layout.search_pager_first, null, false);
        searchPagerFirstModel = new SearchPagerFirstModel(searchPagerFirstBinding);
        searchPagerFirstBinding.setSearchPagerFirstModel(searchPagerFirstModel);

        //加载搜索技能页面
        searchActivityHotServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_activity_hot_service, null, false);

        //加载搜索结果页面
        searchNeedResultTabBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_need_result_tab, null, false);

        //加载搜索所有结果页面
        activityResultAllBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.activity_result_all, null, false);

        //搜索历史和搜索记录的页面
        searchListviewAssociationBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_listview_association, null, false);

        //默认首页,page=0
        changeView(mPage);

    }

    //切换页面
    public void changeView(int page) {
        activitySearchBinding.flSearchFirst.removeAllViews();
        switch (page) {
            case 0:
                activitySearchBinding.flSearchFirst.addView(searchPagerFirstBinding.getRoot());
                break;
            case 1:
                activitySearchBinding.flSearchFirst.addView(searchActivityHotServiceBinding.getRoot());
                break;
            case 2:
                activitySearchBinding.flSearchFirst.addView(searchNeedResultTabBinding.getRoot());
                break;
            case 3:
                activitySearchBinding.flSearchFirst.addView(activityResultAllBinding.getRoot());
                break;
            case 4:
                activitySearchBinding.flSearchFirst.addView(searchListviewAssociationBinding.getRoot());
                break;
        }
        mPage = page;
    }

    //删除页面
    private void removeView() {
        activitySearchBinding.ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mPage) {
                    case 0:
                        CommonUtils.getCurrentActivity().finish();
                        break;
                    case 1:
                        changeView(0);
                        mPage = 0;
                        break;
                    case 2:
                        changeView(1);
                        mPage = 1;
                        //  listener.OnBackClick();
                        break;
                    case 3:
                        changeView(0);
                        mPage = 0;
                        break;
                    case 4:
                        changeView(0);
                        mPage = 0;
                        break;
                }
                if (activitySearchBinding.etActivitySearchAssociation.getText() != null) {
                    activitySearchBinding.etActivitySearchAssociation.setText(null);
                }

            }
        });
    }
    //监听回调返回键
  /*  public interface OnCBacklickListener{
        void OnBackClick();
    }

    private OnCBacklickListener listener;
    public void setOnCBacklickListener(OnCBacklickListener listener) {
        this.listener = listener;
    }*/


    //友盟分享SDK
    //如果使用的是qq或者新浪精简版jar，需要在您使用分享或授权的Activity（fragment不行）中添加如下回调代码
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    private void listener() {
        //在第三个页面搜索结果，点击条目进行第三方分享
        searchNeedResultTabBinding.lvSearchTab.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new ShareAction(SearchActivity.this).setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.MORE)
                        // .withTitle("友盟：新浪,QQ,微信")
                        .withText("——来自友盟分享面板")
                        .setCallback(umShareListener)
                        .open();
            }
        });
    }

    //友盟社会化分享的接口
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            LogKit.d("platform" + platform);
            Toast.makeText(SearchActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SearchActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if (t != null) {
                LogKit.d("throw:" + t.getMessage());
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SearchActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    private void UMengCount() {
        // 设置输出运行时日志
        //UMGameAgent.setDebugMode(true);
        // UMGameAgent.init(this);
        // Deprecated UMGameAgent.setPlayerLevel("LV.01");
        //UMGameAgent.setPlayerLevel(1);
        // UMGameAgent.setSessionContinueMillis(1000);
        //在代码中配置Appkey、Channel、Token（Dplus）等信息，样例： String appkey, String channelId（推广渠道名）, EScenarioType eType（场景类型）
        // UMGameAgent.startWithConfigure(
        // new UMAnalyticsConfig(mContext, "4f83c5d852701564c0000011", "Umeng",
        // EScenarioType.E_UM_GAME));
        MobclickAgent.setScenarioType(SearchActivity.this, MobclickAgent.EScenarioType.E_UM_NORMAL);

        //**********确认所有的Activity中都调用了onResume和onPause方法
    }

    //确保在所有的Activity中都调用 MobclickAgent.onResume() 和MobclickAgent.onPause()方法
    // ，这两个调用将不会阻塞应用程序的主线程，也不会影响应用程序的性能。
    //友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void onButtonClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_zhanghaoLogin:
                MobclickAgent.onProfileSignIn("userID");
                break;
            case R.id.btn_zhanghaoBack:
                MobclickAgent.onProfileSignOff();
                break;
            case R.id.btn_shejiao:
                UMPlatformData platform = new UMPlatformData(UMPlatformData.UMedia.SINA_WEIBO, "user_id");
                //  platform.setGender(GENDER.MALE); //optional
                platform.setWeiboId("weiboId");  //optional
                MobclickAgent.onSocialEvent(this, platform);
                break;
            case R.id.btn_text:
                MobclickAgent.setDebugMode(true);
                break;

        }
    }

    //融云推送
    private void RongYunPush() {
        //建立联系与服务器的连接
       // connectService();

        //通过点击推送
       // Intent intent = getIntent();
      //  getPushMessage(intent);
    }

   /* private void connectService() {
        RongIM.connect(Token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //Connect Token 失效的状态处理，需要重新获取 Token
            }

            @Override
            public void onSuccess(String userId) {
                Log.e("MainActivity", "“——onSuccess—-”" + userId);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("“MainActivity”", "“——onError—-”" + errorCode);
            }
        });
    }

    //这里是？
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getPushMessage(intent);
    }

    *//**
     * Android push 消息
     *//*
    private void getPushMessage(Intent intent) {

        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //该条推送消息的内容。
            String content = intent.getData().getQueryParameter("pushContent:"+"推送消息的内容");
            //标识该推送消息的唯一 Id。
            String id = intent.getData().getQueryParameter("pushId");
            //用户自定义参数 json 格式，解析后用户可根据自己定义的 Key 、Value 值进行业务处理。
            String extra = intent.getData().getQueryParameter("extra");
            //统计通知栏点击事件.
           // RongIMClient.recordNotificationEvent(id);
            Log.d("TestPushActivity", "--content:" + content + "--id:" + id + "---extra:" + extra);
        }
    }*/





}
