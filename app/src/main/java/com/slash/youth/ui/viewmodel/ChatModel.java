package com.slash.youth.ui.viewmodel;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChatBinding;
import com.slash.youth.databinding.ItemChatChangeContactWayInfoBinding;
import com.slash.youth.databinding.ItemChatDatetimeBinding;
import com.slash.youth.databinding.ItemChatFriendPicBinding;
import com.slash.youth.databinding.ItemChatFriendTextBinding;
import com.slash.youth.databinding.ItemChatInfoBinding;
import com.slash.youth.databinding.ItemChatMyPicBinding;
import com.slash.youth.databinding.ItemChatMySendBusinessCardBinding;
import com.slash.youth.databinding.ItemChatMySendVoiceBinding;
import com.slash.youth.databinding.ItemChatMyShareTaskBinding;
import com.slash.youth.databinding.ItemChatMyTextBinding;
import com.slash.youth.databinding.ItemChatOtherChangeContactWayBinding;
import com.slash.youth.databinding.ItemChatOtherSendAddFriendBinding;
import com.slash.youth.databinding.ItemChatOtherSendBusinessCardBinding;
import com.slash.youth.databinding.ItemChatOtherSendVoiceBinding;
import com.slash.youth.databinding.ItemChatOtherShareTaskBinding;
import com.slash.youth.domain.ChatCmdAddFriendBean;
import com.slash.youth.domain.ChatCmdBusinesssCardBean;
import com.slash.youth.domain.ChatCmdChangeContactBean;
import com.slash.youth.domain.ChatCmdShareTaskBean;
import com.slash.youth.domain.SendMessageBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;
import io.rong.message.ImageMessage;
import io.rong.message.ReadReceiptMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * Created by zhouyifeng on 2016/11/16.
 */
public class ChatModel extends BaseObservable {

    ActivityChatBinding mActivityChatBinding;
    Activity mActivity;
    private TextView mTvChatFriendName;
    private LinearLayout mLlChatContent;//聊天内容容器
    private ScrollView mSvChatContent;
    private String targetId = "10002";
    //    private String targetId = "10000";
    ArrayList<SendMessageBean> listSendMsg = new ArrayList<SendMessageBean>();

    public ChatModel(ActivityChatBinding activityChatBinding, Activity activity) {
        this.mActivityChatBinding = activityChatBinding;
        this.mActivity = activity;
        initData();
        initView();
        initListener();
    }


    private void initData() {
        MsgManager.targetId = targetId;//设置聊天界面只显示当前聊天UserId发来的消息

        MsgManager.setHistoryListener(new ChatHistoryListener());
        MsgManager.loadHistoryChatRecord();

        String chatCmdName = mActivity.getIntent().getStringExtra("chatCmdName");
        if (chatCmdName.equals("sendBusinessCard")) {
            sendBusinessCard();
        } else if (chatCmdName.equals("sendShareTask")) {
            sendShareTask();
        }
    }


    private void initView() {
        //使底部的输入框失去焦点，隐藏软键盘
        mTvChatFriendName = mActivityChatBinding.tvChatFriendName;
        mTvChatFriendName.setFocusable(true);
        mTvChatFriendName.setFocusableInTouchMode(true);
        mTvChatFriendName.requestFocus();

        mLlChatContent = mActivityChatBinding.llChatContent;

        //测试添加各种消息条目
//        mLlChatContent.addView(createDateTimeView());
//        mLlChatContent.addView(createFriendTextView());
//        mLlChatContent.addView(createMyTextView());
//        mLlChatContent.addView(createDateTimeView());
//        mLlChatContent.addView(createFriendPicView());
//        mLlChatContent.addView(createMyPicView());
//        mLlChatContent.addView(createOtherSendAddFriendView());
//        mLlChatContent.addView(createInfoView("您已接受对方的加好友请求"));
//        mLlChatContent.addView(createInfoView("您向对方分享了一个服务"));
//        mLlChatContent.addView(createOtherShareTaskView());
//        mLlChatContent.addView(createMyShareTaskView());
//        mLlChatContent.addView(createOtherSendBusinessCardView());
//        mLlChatContent.addView(createMySendBusinessCardView());
//        mLlChatContent.addView(createOtherChangeContactWayView());
//        mLlChatContent.addView(createChangeContactWayInfoView());//因为背景切图还没有，所以暂未实现
//        mLlChatContent.addView(createOtherSendVoiceView());
//        mLlChatContent.addView(createMySendVoiceView());

        //自动滚动到底部
        mSvChatContent = mActivityChatBinding.svChatContent;
        mSvChatContent.post(new Runnable() {
            @Override
            public void run() {
                mSvChatContent.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    long startRecorderTime = 0;
    long endRecorderTime = 0;

    private void initListener() {
        setMessageListener();

        mActivityChatBinding.etChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 0) {
                    setUploadPicBtnVisibility(View.VISIBLE);
                    setSendTextBtnVisibility(View.GONE);
                } else {
                    setUploadPicBtnVisibility(View.GONE);
                    setSendTextBtnVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mActivityChatBinding.etChatInput.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                mSvChatContent.fullScroll(View.FOCUS_DOWN);
            }
        });

        mActivityChatBinding.tvChatInputVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int width = v.getWidth();
                int[] locationOnScreen = new int[2];
                v.getLocationOnScreen(locationOnScreen);
                //确定是指滑动的区域，超过这个区域就会取消发送语音
                int left = locationOnScreen[0];
                int top = locationOnScreen[1] - CommonUtils.dip2px(75);
                int right = left + width;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        v.setBackgroundResource(R.drawable.shape_chat_input_voice_bg);
                        setSendVoiceCmdLayerVisibility(View.VISIBLE);
                        setUpCancelSendVoiceVisibility(View.VISIBLE);
                        setRelaseCancelSendVoiceVisibility(View.GONE);
                        startRecorderTime = SystemClock.currentThreadTimeMillis();
                        startSoundRecording();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float rawX = event.getRawX();
                        float rawY = event.getRawY();
                        if (rawX >= left && rawX <= right && rawY >= top) {
                            setUpCancelSendVoiceVisibility(View.VISIBLE);
                            setRelaseCancelSendVoiceVisibility(View.GONE);
                            isCancelRecord = false;
                        } else {
                            setUpCancelSendVoiceVisibility(View.GONE);
                            setRelaseCancelSendVoiceVisibility(View.VISIBLE);
                            isCancelRecord = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        v.setBackgroundResource(R.drawable.shape_chat_input_voice_untouch_bg);
                        setSendVoiceCmdLayerVisibility(View.GONE);
                        endRecorderTime = SystemClock.currentThreadTimeMillis();
                        long timeSpan = endRecorderTime - startRecorderTime;
                        if (timeSpan > 500) {
                            stopSoundRecording();
                        } else {//这个判断只是为了防止不崩溃
                            CommonUtils.getHandler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    stopSoundRecording();
                                }
                            }, (500 - timeSpan));
                        }
                        break;
                }
                return true;
            }
        });
    }

    //接受消息用的监听
    private void setMessageListener() {
        MsgManager.setChatTextListener(new MsgManager.ChatTextListener() {
            @Override
            public void displayText(Message message, int left) {
                TextMessage textMessage = (TextMessage) message.getContent();
                String content = textMessage.getContent();
                View friendTextView = createFriendTextView(content);
                mLlChatContent.addView(friendTextView);

                //发送已经阅读的回执
                sendReadReceipt(message.getSentTime());
            }
        });

        MsgManager.setChatPicListener(new MsgManager.ChatPicListener() {

            @Override
            public void dispayPic(Message message, int left) {
                ImageMessage imageMessage = (ImageMessage) message.getContent();
                Uri thumUri = imageMessage.getThumUri();
//                Uri localUri = imageMessage.getLocalUri();
                Uri remoteUri = imageMessage.getRemoteUri();
                LogKit.v("remoteUri:" + remoteUri.toString());
                View friendPicView = createFriendPicView(thumUri);
                mLlChatContent.addView(friendPicView);

                //发送已经阅读的回执
                sendReadReceipt(message.getSentTime());
            }
        });
        MsgManager.setChatVoiceListener(new MsgManager.ChatVoiceListener() {
            @Override
            public void loadVoice(Message message, int left) {
                VoiceMessage voiceMessage = (VoiceMessage) message.getContent();
                int duration = voiceMessage.getDuration();
                Uri voiceUri = voiceMessage.getUri();
                mLlChatContent.addView(createOtherSendVoiceView(voiceUri, duration));

                //发送已经阅读的回执
                sendReadReceipt(message.getSentTime());
            }
        });
        MsgManager.setChatOtherCmdListener(new MsgManager.ChatOtherCmdListener() {
            @Override
            public void doOtherCmd(Message message, int left) {
                CommandMessage commandMessage = (CommandMessage) message.getContent();
                String name = commandMessage.getName();
                String data = commandMessage.getData();
                Gson gson = new Gson();
                if (name.contentEquals(MsgManager.CHAT_CMD_ADD_FRIEND)) {
                    ChatCmdAddFriendBean chatCmdAddFriendBean = gson.fromJson(data, ChatCmdAddFriendBean.class);
                    mLlChatContent.addView(createOtherSendAddFriendView(chatCmdAddFriendBean));
                } else if (name.contentEquals(MsgManager.CHAT_CMD_SHARE_TASK)) {
                    ChatCmdShareTaskBean chatCmdShareTaskBean = gson.fromJson(data, ChatCmdShareTaskBean.class);
                    mLlChatContent.addView(createOtherShareTaskView(chatCmdShareTaskBean));
                } else if (name.contentEquals(MsgManager.CHAT_CMD_BUSINESS_CARD)) {
                    ChatCmdBusinesssCardBean chatCmdBusinesssCardBean = gson.fromJson(data, ChatCmdBusinesssCardBean.class);
                    mLlChatContent.addView(createOtherSendBusinessCardView(chatCmdBusinesssCardBean));

                } else if (name.contentEquals(MsgManager.CHAT_CMD_CHANGE_CONTACT)) {
                    ChatCmdChangeContactBean chatCmdChangeContactBean = gson.fromJson(data, ChatCmdChangeContactBean.class);
                    mLlChatContent.addView(createOtherChangeContactWayView(chatCmdChangeContactBean));
                }

                //发送已经阅读的回执
                sendReadReceipt(message.getSentTime());
            }
        });
        //接受消息回执的监听
        //在 onReadReceiptReceived() 回调里，请先判断 message.getConversationType() 和
        // message.getTargetId() 和当前会话一致，
        // 然后在UI里把该会话中发送时间戳之前的所有已发送消息状态置为已读（底层数据库消息状态已经改为已读）。
        RongIMClient.setReadReceiptListener(new RongIMClient.ReadReceiptListener() {
            @Override
            public void onReadReceiptReceived(Message message) {
                ReadReceiptMessage content = (ReadReceiptMessage) message.getContent();
                long lastMessageSendTime = content.getLastMessageSendTime();
                //自行进行UI处理，把会话中发送时间戳之前的所有已发送消息状态置为已读
                ArrayList<SendMessageBean> listNeedRemove = new ArrayList<SendMessageBean>();
                for (SendMessageBean sendMessageBean : listSendMsg) {
                    if (sendMessageBean.sendTime < lastMessageSendTime) {
                        final TextView tvReadStatus = (TextView) sendMessageBean.vReadStatus;
                        CommonUtils.getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                tvReadStatus.setText("已读");
                            }
                        });
                        listNeedRemove.add(sendMessageBean);
                    }
                }
                listSendMsg.removeAll(listNeedRemove);
            }

            @Override
            public void onMessageReceiptRequest(Conversation.ConversationType conversationType, String s, String s1) {

            }

            @Override
            public void onMessageReceiptResponse(Conversation.ConversationType conversationType, String s, String s1, HashMap<String, Long> hashMap) {

            }
        });
    }

    /**
     * 发送阅读huizhi
     *
     * @param sentTime
     */
    private void sendReadReceipt(long sentTime) {
        RongIMClient.getInstance().sendReadReceiptMessage(Conversation.ConversationType.PRIVATE, targetId, sentTime);
    }

    boolean isCancelRecord = false;
    MediaRecorder mediaRecorder;
    File tmpVoiceFile;

    /**
     * 开始录音
     */
    public void startSoundRecording() {

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(CommonUtils.getContext(), Manifest.permission.RECORD_AUDIO);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
                return;
            }
        }
        soundRecord();
    }

    public void soundRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                tmpVoiceFile = new File(CommonUtils.getContext().getCacheDir(), "tmpVoice" + SystemClock.currentThreadTimeMillis());
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
                mediaRecorder.setOutputFile(tmpVoiceFile.getAbsolutePath());
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                try {
                    mediaRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mediaRecorder.start();
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaRecorder != null) {
                    int maxAmplitude = 0;
                    try {
                        maxAmplitude = mediaRecorder.getMaxAmplitude();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    LogKit.v("maxAmplitude:" + maxAmplitude);
                    int volumeLevel;
                    if (maxAmplitude >= 0 && maxAmplitude < 2000) {
                        volumeLevel = 1;
                    } else if (maxAmplitude >= 1000 && maxAmplitude < 4000) {
                        volumeLevel = 2;
                    } else if (maxAmplitude >= 2000 && maxAmplitude < 6000) {
                        volumeLevel = 3;
                    } else if (maxAmplitude >= 3000 && maxAmplitude < 8000) {
                        volumeLevel = 4;
                    } else {
                        volumeLevel = 5;
                    }

                    setRecorderVolumeLevelIcon(volumeLevel);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void setRecorderVolumeLevelIcon(final int volumeLevel) {
        CommonUtils.getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (volumeLevel == 1) {
                    mActivityChatBinding.ivChatRecorderVolume.setImageResource(R.mipmap.column1);
                } else if (volumeLevel == 2) {
                    mActivityChatBinding.ivChatRecorderVolume.setImageResource(R.mipmap.column2);
                } else if (volumeLevel == 3) {
                    mActivityChatBinding.ivChatRecorderVolume.setImageResource(R.mipmap.column3);
                } else if (volumeLevel == 4) {
                    mActivityChatBinding.ivChatRecorderVolume.setImageResource(R.mipmap.column4);
                } else if (volumeLevel == 5) {
                    mActivityChatBinding.ivChatRecorderVolume.setImageResource(R.mipmap.column5);
                }
            }
        });
    }


    /**
     * 停止录音
     */
    public void stopSoundRecording() {
        if (mediaRecorder == null) {
            return;
        }
//            mediaRecorder.reset();
        try {
            mediaRecorder.stop();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        mediaRecorder.release();
        mediaRecorder = null;

        int duration = 0;
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(tmpVoiceFile.getAbsolutePath());
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration() / 1000;
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (duration < 1) {
            ToastUtils.shortToast("录音时间过短");
            deleteTmpRecordingFile();
            return;
        }
        if (!isCancelRecord) {
            sendVoice(tmpVoiceFile.getAbsolutePath(), duration);
        } else {
            deleteTmpRecordingFile();
        }
    }

    /**
     * 删除录音保存的临时文件
     */
    public void deleteTmpRecordingFile() {
        if (tmpVoiceFile != null && tmpVoiceFile.exists()) {
            tmpVoiceFile.delete();
        }
    }

    /**
     * 发送语音
     */
    public void sendVoice(String voiceFilePath, final int duration) {
        final File voiceFile = new File(voiceFilePath);
        final VoiceMessage vocMsg = VoiceMessage.obtain(Uri.fromFile(voiceFile), duration);
        final long sendTime = SystemClock.currentThreadTimeMillis();
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, vocMsg, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onError(Integer messageId, RongIMClient.ErrorCode e) {
                deleteTmpRecordingFile();
            }

            @Override
            public void onSuccess(Integer integer) {
//                mLlChatContent.addView(createMySendVoiceView(Uri.fromFile(voiceFile), duration));
//                mLlChatContent.addView(createMySendVoiceView(vocMsg.getUri(), duration));
                deleteTmpRecordingFile();
            }
        }, new RongIMClient.ResultCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                VoiceMessage savedVoiceMessage = (VoiceMessage) message.getContent();
                View mySendVoiceView = createMySendVoiceView(savedVoiceMessage.getUri(), duration);

                View vReadStatus = mySendVoiceView.findViewById(R.id.tv_chat_msg_read_status);
                SendMessageBean sendMessageBean = new SendMessageBean(sendTime, vReadStatus);
                listSendMsg.add(sendMessageBean);

                mLlChatContent.addView(mySendVoiceView);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

    }

    /**
     * 发送文本
     */
    public void sendText() {
        final String inputText = mActivityChatBinding.etChatInput.getText().toString();
        TextMessage textMessage = TextMessage.obtain(inputText);
        final long sendTime = SystemClock.currentThreadTimeMillis();
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, textMessage, null, null, new RongIMClient.SendMessageCallback() {
            //发送消息的回调
            @Override
            public void onSuccess(Integer integer) {
                View myTextView = createMyTextView(inputText);

                View vReadStatus = myTextView.findViewById(R.id.tv_chat_msg_read_status);
                SendMessageBean sendMessageBean = new SendMessageBean(sendTime, vReadStatus);
                listSendMsg.add(sendMessageBean);

                mLlChatContent.addView(myTextView);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }
        }, new RongIMClient.ResultCallback<Message>() {
            //消息存库的回调，可用于获取消息实体
            @Override
            public void onSuccess(Message message) {

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 发送图片
     */
    public void sendPic(String imgPath) {
        File imageSource = new File(imgPath);
        File imageThumb = new File(CommonUtils.getContext().getCacheDir(), "thumb" + SystemClock.currentThreadTimeMillis());

        try {
            Bitmap bmpSource = BitmapFactory.decodeFile(imgPath);

            // 创建缩略图变换矩阵。
            Matrix m = new Matrix();
            m.setRectToRect(new RectF(0, 0, bmpSource.getWidth(), bmpSource.getHeight()), new RectF(0, 0, 160, 160), Matrix.ScaleToFit.CENTER);

            // 生成缩略图。
            Bitmap bmpThumb = Bitmap.createBitmap(bmpSource, 0, 0, bmpSource.getWidth(), bmpSource.getHeight(), m, true);

            imageThumb.createNewFile();

            FileOutputStream fosThumb = new FileOutputStream(imageThumb);

            // 保存缩略图。
            bmpThumb.compress(Bitmap.CompressFormat.JPEG, 60, fosThumb);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageMessage imageMessage = ImageMessage.obtain(Uri.fromFile(imageThumb), Uri.fromFile(imageSource));
        long sendTime = SystemClock.currentThreadTimeMillis();
        RongIMClient.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE, targetId, imageMessage, null, null, new RongIMClient.SendImageMessageCallback() {

            @Override
            public void onAttached(Message message) {
                //保存数据库成功
                LogKit.v("保存数据库成功");
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode code) {
                //发送失败
                LogKit.v("发送失败");
            }

            @Override
            public void onSuccess(Message message) {
                //发送成功
                LogKit.v("发送成功");
                ImageMessage imageMessage1 = (ImageMessage) message.getContent();
                LogKit.v(imageMessage1.getRemoteUri().toString());
            }

            @Override
            public void onProgress(Message message, int progress) {
                //发送进度
                LogKit.v("发送进度:" + progress);
            }
        });

        View myPicView = createMyPicView(Uri.fromFile(imageThumb));

        View vReadStatus = myPicView.findViewById(R.id.tv_chat_msg_read_status);
        SendMessageBean sendMessageBean = new SendMessageBean(sendTime, vReadStatus);
        listSendMsg.add(sendMessageBean);

        mLlChatContent.addView(myPicView);
    }


    /**
     * 添加好友
     */
    public void sendAddFriend() {
        ChatCmdAddFriendBean chatCmdAddFriendBean = new ChatCmdAddFriendBean();
        chatCmdAddFriendBean.uid = LoginManager.currentLoginUserId;
        Gson gson = new Gson();
        String jsonData = gson.toJson(chatCmdAddFriendBean);
        CommandMessage commandMessage = CommandMessage.obtain(MsgManager.CHAT_CMD_ADD_FRIEND, jsonData);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, commandMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
//                ToastUtils.shortToast("send add friend success");
                View infoView = createInfoView("您已发送添加好友请求");
                mLlChatContent.addView(infoView);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }
        });
    }


    /**
     * 交换联系方式
     */
    public void sendChangeContact() {
        ChatCmdChangeContactBean chatCmdChangeContactBean = new ChatCmdChangeContactBean();
        chatCmdChangeContactBean.uid = LoginManager.currentLoginUserId;
        chatCmdChangeContactBean.phone = "18888888888";
        Gson gson = new Gson();
        String jsonData = gson.toJson(chatCmdChangeContactBean);
        CommandMessage commandMessage = CommandMessage.obtain(MsgManager.CHAT_CMD_SHARE_TASK, jsonData);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, commandMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                View infoView = createInfoView("您已发送交换手机号请求");
                mLlChatContent.addView(infoView);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 发送任务分享
     */
    public void sendShareTask() {
        ChatCmdShareTaskBean chatCmdShareTaskBean = new ChatCmdShareTaskBean();
        chatCmdShareTaskBean.uid = LoginManager.currentLoginUserId;
        chatCmdShareTaskBean.avatar = "";
        chatCmdShareTaskBean.title = "";
        chatCmdShareTaskBean.quote = 10;
        chatCmdShareTaskBean.type = 1;//服务或者需求
        Gson gson = new Gson();
        String jsonData = gson.toJson(chatCmdShareTaskBean);
        CommandMessage commandMessage = CommandMessage.obtain(MsgManager.CHAT_CMD_SHARE_TASK, jsonData);
        final long sendTime = SystemClock.currentThreadTimeMillis();
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, commandMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                View myShareTaskView = createMyShareTaskView();


                View vReadStatus = myShareTaskView.findViewById(R.id.tv_chat_msg_read_status);
                SendMessageBean sendMessageBean = new SendMessageBean(sendTime, vReadStatus);
                listSendMsg.add(sendMessageBean);

                mLlChatContent.addView(myShareTaskView);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    /**
     * 发送个人名片
     */
    public void sendBusinessCard() {
        ChatCmdBusinesssCardBean chatCmdBusinesssCardBean = new ChatCmdBusinesssCardBean();
        chatCmdBusinesssCardBean.uid = LoginManager.currentLoginUserId;
        chatCmdBusinesssCardBean.avatar = "";
        chatCmdBusinesssCardBean.name = "tom";
        chatCmdBusinesssCardBean.industry = "";
        chatCmdBusinesssCardBean.profession = "";
        Gson gson = new Gson();
        String jsonData = gson.toJson(chatCmdBusinesssCardBean);
        CommandMessage commandMessage = CommandMessage.obtain(MsgManager.CHAT_CMD_BUSINESS_CARD, jsonData);
        final long sendTime = SystemClock.currentThreadTimeMillis();
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, commandMessage, null, null, new RongIMClient.SendMessageCallback() {
            @Override
            public void onSuccess(Integer integer) {
                View mySendBusinessCardView = createMySendBusinessCardView();

                View vReadStatus = mySendBusinessCardView.findViewById(R.id.tv_chat_msg_read_status);
                SendMessageBean sendMessageBean = new SendMessageBean(sendTime, vReadStatus);
                listSendMsg.add(sendMessageBean);

                mLlChatContent.addView(mySendBusinessCardView);
            }

            @Override
            public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    //创建我发的文本消息View
    private View createMyTextView(String inputText) {
        ItemChatMyTextBinding itemChatMyTextBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_my_text, null, false);
        ChatMyTextModel chatMyTextModel = new ChatMyTextModel(itemChatMyTextBinding, mActivity, inputText);
        itemChatMyTextBinding.setChatMyTextModel(chatMyTextModel);
        return itemChatMyTextBinding.getRoot();
    }

    //创建好友发的文本消息View
    private View createFriendTextView(String content) {
        ItemChatFriendTextBinding itemChatFriendTextBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_friend_text, null, false);
        ChatFriendTextModel chatFriendTextModel = new ChatFriendTextModel(itemChatFriendTextBinding, mActivity);
        itemChatFriendTextBinding.setChatFriendTextModel(chatFriendTextModel);
        chatFriendTextModel.setTextContent(content);
        return itemChatFriendTextBinding.getRoot();
    }

    //创建聊天记录显示时间 View
    private View createDateTimeView() {
        ItemChatDatetimeBinding itemChatDatetimeBinding =
                DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_datetime, null, false);
        ChatDatetimeModel chatDatetimeModel = new ChatDatetimeModel(itemChatDatetimeBinding, mActivity);
        itemChatDatetimeBinding.setChatDatetimeModel(chatDatetimeModel);
        return itemChatDatetimeBinding.getRoot();
    }

    //创建我发的图片View
    private View createMyPicView(Uri thumUri) {
        ItemChatMyPicBinding itemChatMyPicBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_my_pic, null, false);
        ChatMyPicModel chatMyPicModel = new ChatMyPicModel(itemChatMyPicBinding, mActivity, thumUri);
        itemChatMyPicBinding.setChatMyPicModel(chatMyPicModel);
        return itemChatMyPicBinding.getRoot();
    }

    //创建好友发的图片View
    private View createFriendPicView(Uri thumUri) {
        ItemChatFriendPicBinding itemChatFriendPicBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_friend_pic, null, false);
        ChatFriendPicModel chatFriendPicModel = new ChatFriendPicModel(itemChatFriendPicBinding, mActivity, thumUri);
        itemChatFriendPicBinding.setChatFriendPicModel(chatFriendPicModel);
        return itemChatFriendPicBinding.getRoot();
    }

    private View createOtherSendAddFriendView(ChatCmdAddFriendBean chatCmdAddFriendBean) {
        ItemChatOtherSendAddFriendBinding itemChatOtherSendAddFriendBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_other_send_add_friend, null, false);
        ChatOtherSendAddFriendModel chatOtherSendAddFriendModel = new ChatOtherSendAddFriendModel(itemChatOtherSendAddFriendBinding, mActivity);
        itemChatOtherSendAddFriendBinding.setChatOtherSendAddFriendModel(chatOtherSendAddFriendModel);
        return itemChatOtherSendAddFriendBinding.getRoot();
    }

    //这种情况应该不存在，如果自己点击添加好友，应该是只提示一条消息
    private void createMySendAddFriendView() {

    }

    //参数可能只是为了方便测试
    private View createInfoView(String info) {
        ItemChatInfoBinding itemChatInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_info, null, false);
        ChatInfoModel chatInfoModel = new ChatInfoModel(itemChatInfoBinding, mActivity);
        itemChatInfoBinding.setChatInfoModel(chatInfoModel);
        chatInfoModel.setInfo(info);
        return itemChatInfoBinding.getRoot();
    }

    private View createOtherShareTaskView(ChatCmdShareTaskBean chatCmdShareTaskBean) {
        ItemChatOtherShareTaskBinding itemChatOtherShareTaskBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_other_share_task, null, false);
        ChatOtherShareTaskModel chatOtherShareTaskModel = new ChatOtherShareTaskModel(itemChatOtherShareTaskBinding, mActivity);
        itemChatOtherShareTaskBinding.setChatOtherShareTaskModel(chatOtherShareTaskModel);
        return itemChatOtherShareTaskBinding.getRoot();
    }

    private View createMyShareTaskView() {
        ItemChatMyShareTaskBinding itemChatMyShareTaskBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_my_share_task, null, false);
        ChatMyShareTaskModel chatMyShareTaskModel = new ChatMyShareTaskModel(itemChatMyShareTaskBinding, mActivity);
        itemChatMyShareTaskBinding.setChatMyShareTaskModel(chatMyShareTaskModel);
        return itemChatMyShareTaskBinding.getRoot();
    }

    private View createOtherSendBusinessCardView(ChatCmdBusinesssCardBean chatCmdBusinesssCardBean) {
        ItemChatOtherSendBusinessCardBinding itemChatOtherSendBusinessCardBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_other_send_business_card, null, false);
        ChatOtherSendBusinessCardModel chatOtherSendBusinessCardModel = new ChatOtherSendBusinessCardModel(itemChatOtherSendBusinessCardBinding, mActivity);
        itemChatOtherSendBusinessCardBinding.setChatOtherSendBusinessCardModel(chatOtherSendBusinessCardModel);
        return itemChatOtherSendBusinessCardBinding.getRoot();
    }

    private View createMySendBusinessCardView() {
        ItemChatMySendBusinessCardBinding itemChatMySendBusinessCardBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_my_send_business_card, null, false);
        ChatMySendBusinessCardModel chatMySendBusinessCardModel = new ChatMySendBusinessCardModel(itemChatMySendBusinessCardBinding, mActivity);
        itemChatMySendBusinessCardBinding.setChatMySendBusinessCardModel(chatMySendBusinessCardModel);
        return itemChatMySendBusinessCardBinding.getRoot();
    }

    private View createOtherChangeContactWayView(ChatCmdChangeContactBean chatCmdChangeContactBean) {
        ItemChatOtherChangeContactWayBinding itemChatOtherChangeContactWayBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_other_change_contact_way, null, false);
        ChatOtherChangeContactWayModel chatOtherChangeContactWayModel = new ChatOtherChangeContactWayModel(itemChatOtherChangeContactWayBinding, mActivity);
        itemChatOtherChangeContactWayBinding.setChatOtherChangeContactWayModel(chatOtherChangeContactWayModel);
        return itemChatOtherChangeContactWayBinding.getRoot();
    }

    private View createChangeContactWayInfoView() {
        ItemChatChangeContactWayInfoBinding itemChatChangeContactWayInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_change_contact_way_info, null, false);
        ChatChangeContactWayInfoModel chatChangeContactWayInfoModel = new ChatChangeContactWayInfoModel(itemChatChangeContactWayInfoBinding, mActivity);
        itemChatChangeContactWayInfoBinding.setChatChangeContactWayInfoModel(chatChangeContactWayInfoModel);
        return itemChatChangeContactWayInfoBinding.getRoot();
    }

    private View createOtherSendVoiceView(Uri voiceUri, int duration) {
        ItemChatOtherSendVoiceBinding itemChatOtherSendVoiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_other_send_voice, null, false);
        ChatOtherSendVoiceModel chatOtherSendVoiceModel = new ChatOtherSendVoiceModel(itemChatOtherSendVoiceBinding, mActivity, voiceUri, duration);
        itemChatOtherSendVoiceBinding.setChatOtherSendVoiceModel(chatOtherSendVoiceModel);
        return itemChatOtherSendVoiceBinding.getRoot();
    }

    private View createMySendVoiceView(Uri voiceUri, int duration) {
        ItemChatMySendVoiceBinding itemChatMySendVoiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_chat_my_send_voice, null, false);
        ChatMySendVoiceModel chatMySendVoiceModel = new ChatMySendVoiceModel(itemChatMySendVoiceBinding, mActivity, voiceUri, duration);
        itemChatMySendVoiceBinding.setChatMySendVoiceModel(chatMySendVoiceModel);
        return itemChatMySendVoiceBinding.getRoot();
    }

    public void openUploadPic(View v) {
        setUploadPicLayerVisibility(View.VISIBLE);
    }

    public void closeUploadPic(View v) {
        setUploadPicLayerVisibility(View.GONE);
    }

    public void sendText(View v) {
        sendText();
    }

    //拍照发送图片
    public void photoGraph(View v) {
//        sendPic("/storage/sdcard1/4.jpg");
    }

    //选择相册图片发送
    public void getAlbumPic(View v) {
        sendPic("/storage/sdcard1/4.jpg");
    }

    //交换联系方式
    public void sendChangeContact(View v) {
        sendChangeContact();
    }

    //添加好友
    public void sendAddFriend(View v) {
        sendAddFriend();
    }

    public void switchVoiceInput(View v) {
        switchVoiceInput();
    }

    public void switchTextInput(View v) {
        switchTextInput();
    }

    //切换到语音输入
    public void switchVoiceInput() {
        //当切换到语音输入状态时，语音输入按钮需要隐藏，文本输入按钮需要显示
        setVoiceInputIconVisibility(View.GONE);
        setTextInputIconVisibility(View.VISIBLE);
        setInputVoiceTvVisibility(View.VISIBLE);
        setInputTextEtVisibility(View.GONE);
    }

    //切换到文本输入
    public void switchTextInput() {
        //当切换到文本输入状态时，语音输入按钮需要显示，文本输入按钮需要隐藏
        setVoiceInputIconVisibility(View.VISIBLE);
        setTextInputIconVisibility(View.GONE);
        setInputVoiceTvVisibility(View.GONE);
        setInputTextEtVisibility(View.VISIBLE);
    }

    public class ChatHistoryListener implements MsgManager.HistoryListener {

        @Override
        public void displayHistory(List<Message> messages) {

            sendReadReceipt(SystemClock.currentThreadTimeMillis());
        }
    }

    private int voiceInputIconVisibility = View.VISIBLE;
    private int textInputIconVisibility = View.GONE;
    private int inputTextEtVisibility = View.VISIBLE;
    private int inputVoiceTvVisibility = View.GONE;
    private int uploadPicLayerVisibility = View.GONE;
    private int uploadPicBtnVisibility = View.VISIBLE;
    private int sendTextBtnVisibility = View.GONE;
    private int sendVoiceCmdLayerVisibility = View.GONE;
    private int upCancelSendVoiceVisibility;
    private int relaseCancelSendVoiceVisibility;

    @Bindable
    public int getTextInputIconVisibility() {
        return textInputIconVisibility;
    }

    public void setTextInputIconVisibility(int textInputIconVisibility) {
        this.textInputIconVisibility = textInputIconVisibility;
        notifyPropertyChanged(BR.textInputIconVisibility);
    }

    @Bindable
    public int getVoiceInputIconVisibility() {
        return voiceInputIconVisibility;
    }

    public void setVoiceInputIconVisibility(int voiceInputIconVisibility) {
        this.voiceInputIconVisibility = voiceInputIconVisibility;
        notifyPropertyChanged(BR.voiceInputIconVisibility);
    }

    @Bindable
    public int getInputVoiceTvVisibility() {
        return inputVoiceTvVisibility;
    }

    public void setInputVoiceTvVisibility(int inputVoiceTvVisibility) {
        this.inputVoiceTvVisibility = inputVoiceTvVisibility;
        notifyPropertyChanged(BR.inputVoiceTvVisibility);
    }

    @Bindable
    public int getInputTextEtVisibility() {
        return inputTextEtVisibility;
    }

    public void setInputTextEtVisibility(int inputTextEtVisibility) {
        this.inputTextEtVisibility = inputTextEtVisibility;
        notifyPropertyChanged(BR.inputTextEtVisibility);
    }

    @Bindable
    public int getUploadPicLayerVisibility() {
        return uploadPicLayerVisibility;
    }

    public void setUploadPicLayerVisibility(int uploadPicLayerVisibility) {
        this.uploadPicLayerVisibility = uploadPicLayerVisibility;
        notifyPropertyChanged(BR.uploadPicLayerVisibility);
    }

    @Bindable
    public int getUploadPicBtnVisibility() {
        return uploadPicBtnVisibility;
    }

    public void setUploadPicBtnVisibility(int uploadPicBtnVisibility) {
        this.uploadPicBtnVisibility = uploadPicBtnVisibility;
        notifyPropertyChanged(BR.uploadPicBtnVisibility);
    }

    @Bindable
    public int getSendTextBtnVisibility() {
        return sendTextBtnVisibility;
    }

    public void setSendTextBtnVisibility(int sendTextBtnVisibility) {
        this.sendTextBtnVisibility = sendTextBtnVisibility;
        notifyPropertyChanged(BR.sendTextBtnVisibility);
    }

    @Bindable
    public int getSendVoiceCmdLayerVisibility() {
        return sendVoiceCmdLayerVisibility;
    }

    public void setSendVoiceCmdLayerVisibility(int sendVoiceCmdLayerVisibility) {
        this.sendVoiceCmdLayerVisibility = sendVoiceCmdLayerVisibility;
        notifyPropertyChanged(BR.sendVoiceCmdLayerVisibility);
    }

    @Bindable
    public int getUpCancelSendVoiceVisibility() {
        return upCancelSendVoiceVisibility;
    }

    public void setUpCancelSendVoiceVisibility(int upCancelSendVoiceVisibility) {
        this.upCancelSendVoiceVisibility = upCancelSendVoiceVisibility;
        notifyPropertyChanged(BR.upCancelSendVoiceVisibility);
    }

    @Bindable
    public int getRelaseCancelSendVoiceVisibility() {
        return relaseCancelSendVoiceVisibility;
    }

    public void setRelaseCancelSendVoiceVisibility(int relaseCancelSendVoiceVisibility) {
        this.relaseCancelSendVoiceVisibility = relaseCancelSendVoiceVisibility;
        notifyPropertyChanged(BR.relaseCancelSendVoiceVisibility);
    }
}
