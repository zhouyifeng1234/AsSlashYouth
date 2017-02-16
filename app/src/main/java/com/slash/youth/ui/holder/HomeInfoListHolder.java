package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeInfoBinding;
import com.slash.youth.domain.ChatTaskInfoBean;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ItemHomeInfoModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by zhouyifeng on 2016/10/11.
 */
public class HomeInfoListHolder extends BaseHolder<ConversationListBean.ConversationInfo> {

    private ItemHomeInfoModel mItemHomeInfoModel;
    private ItemListviewHomeInfoBinding mItemListviewHomeInfoBinding;

    @Override
    public View initView() {
        mItemListviewHomeInfoBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_home_info, null, false);
        mItemHomeInfoModel = new ItemHomeInfoModel(mItemListviewHomeInfoBinding);
        mItemListviewHomeInfoBinding.setItemHomeInfoModel(mItemHomeInfoModel);
        return mItemListviewHomeInfoBinding.getRoot();
    }

    @Override
    public void refreshView(ConversationListBean.ConversationInfo data) {
        if (data.uid == 1000) {
            mItemHomeInfoModel.setUsername("消息小助手");
            mItemListviewHomeInfoBinding.ivInfoConversationAvatar.setImageResource(R.mipmap.message_icon);
            mItemHomeInfoModel.setAddVVisibility(View.GONE);
        } else if (MsgManager.customerServiceUid.equals(data.uid + "")) {
            mItemHomeInfoModel.setUsername("斜杠客服");
            mItemListviewHomeInfoBinding.ivInfoConversationAvatar.setImageResource(R.mipmap.customer_service_icon);
            mItemHomeInfoModel.setAddVVisibility(View.GONE);
        } else {
            mItemHomeInfoModel.setUsername(data.name);
            BitmapKit.bindImage(mItemListviewHomeInfoBinding.ivInfoConversationAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + data.avatar);
            if (data.isAuth == 1) {
                mItemHomeInfoModel.setAddVVisibility(View.VISIBLE);
            } else {
                mItemHomeInfoModel.setAddVVisibility(View.GONE);
            }
            String companyAndPosition = "";
            if (!TextUtils.isEmpty(data.company) && !TextUtils.isEmpty(data.position)) {//两个都不为空
                companyAndPosition = "(" + data.company + "," + data.position + ")";
            } else if (!TextUtils.isEmpty(data.company) || !TextUtils.isEmpty(data.position)) {//其中一个不为空,另一个为空
                companyAndPosition = "(" + data.company + data.position + ")";
            } else {//两个都为空,就不需要显示了

            }
            mItemHomeInfoModel.setCompanyAndPosition(companyAndPosition);
        }

        RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, data.uid + "", new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                int unreadCount = integer;
                if (unreadCount > 0) {
                    mItemListviewHomeInfoBinding.tvInfoUnreadMsgCount.setVisibility(View.VISIBLE);
                    mItemListviewHomeInfoBinding.tvInfoUnreadMsgCount.setText(unreadCount + "");
                } else {
                    mItemListviewHomeInfoBinding.tvInfoUnreadMsgCount.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

        RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, data.uid + "", 1, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages != null) {
                    if (messages.size() > 0) {
                        Message message = messages.get(0);
                        String objectName = message.getObjectName();
                        Message.MessageDirection messageDirection = message.getMessageDirection();
                        if (messageDirection == Message.MessageDirection.SEND) {
                            //自己发送的消息
                            //如果是向斜杠小助手发送消息，只能发送文本，所以和下面的发送文本消息的处理方式是一样的
                            if (objectName.equals("RC:TxtMsg")) {
                                TextMessage textMessage = (TextMessage) message.getContent();
                                String extra = textMessage.getExtra();
                                if (TextUtils.isEmpty(extra)) {
                                    mItemHomeInfoModel.setLastMsg(textMessage.getContent());
                                } else {
                                    String content = textMessage.getContent();
                                    if (content.contentEquals(MsgManager.CHAT_CMD_ADD_FRIEND)) {
                                        mItemHomeInfoModel.setLastMsg("您请求添加对方为好友");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_SHARE_TASK)) {
                                        mItemHomeInfoModel.setLastMsg("您向对方分享了一个任务");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_BUSINESS_CARD)) {
                                        mItemHomeInfoModel.setLastMsg("您向对方分享了个人名片");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_CHANGE_CONTACT)) {
                                        mItemHomeInfoModel.setLastMsg("您请求与对方交换联系方式");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_AGREE_ADD_FRIEND)) {
                                        mItemHomeInfoModel.setLastMsg("您同意添加对方为好友");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_REFUSE_ADD_FRIEND)) {
                                        mItemHomeInfoModel.setLastMsg("您拒绝添加对方为好友");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_AGREE_CHANGE_CONTACT)) {
                                        mItemHomeInfoModel.setLastMsg("您同意与对方交换联系方式");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_REFUSE_CHANGE_CONTACT)) {
                                        mItemHomeInfoModel.setLastMsg("您拒绝与对方交换联系方式");
                                    }
                                }
                            } else if (objectName.equals("RC:ImgMsg")) {
                                mItemHomeInfoModel.setLastMsg("有图片消息");
                            } else if (objectName.equals("RC:VcMsg")) {
                                mItemHomeInfoModel.setLastMsg("有语音消息");
                            }
                        } else if (messageDirection == Message.MessageDirection.RECEIVE) {
                            //接收到的消息
                            if (message.getSenderUserId().equals("1000")) {
                                //斜杠小助手向我发送的消息
//                                mItemHomeInfoModel.setLastMsg("斜杠小助手向您发送了一条消息");//后来确定斜杠消息助手也需要显示具体内容
                                TextMessage textMessage = (TextMessage) message.getContent();
                                mItemHomeInfoModel.setLastMsg(textMessage.getContent());
                                return;
                            }
                            if (objectName.equals("RC:TxtMsg")) {
                                TextMessage textMessage = (TextMessage) message.getContent();
                                String extra = textMessage.getExtra();
                                if (TextUtils.isEmpty(extra)) {
                                    mItemHomeInfoModel.setLastMsg(textMessage.getContent());
                                } else {
                                    String content = textMessage.getContent();
                                    if (content.contentEquals(MsgManager.CHAT_CMD_ADD_FRIEND)) {
                                        mItemHomeInfoModel.setLastMsg("对方请求添加您为好友");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_SHARE_TASK)) {
                                        mItemHomeInfoModel.setLastMsg("对方分享了一个任务");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_BUSINESS_CARD)) {
                                        mItemHomeInfoModel.setLastMsg("对方分享了个人名片");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_CHANGE_CONTACT)) {
                                        mItemHomeInfoModel.setLastMsg("对方请求交换联系方式");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_AGREE_ADD_FRIEND)) {
                                        mItemHomeInfoModel.setLastMsg("对方同意添加您为好友");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_REFUSE_ADD_FRIEND)) {
                                        mItemHomeInfoModel.setLastMsg("对方拒绝添加您为好友");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_AGREE_CHANGE_CONTACT)) {
                                        mItemHomeInfoModel.setLastMsg("对方同意交换联系方式");
                                    } else if (content.contentEquals(MsgManager.CHAT_CMD_REFUSE_CHANGE_CONTACT)) {
                                        mItemHomeInfoModel.setLastMsg("对方拒绝交换联系方式");
                                    }
                                }
                            } else if (objectName.equals("RC:ImgMsg")) {
                                mItemHomeInfoModel.setLastMsg("有图片消息");
                            } else if (objectName.equals("RC:VcMsg")) {
                                mItemHomeInfoModel.setLastMsg("有语音消息");
                            }
                        }
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        //相关任务的加载，通过本地文件存储,暂时路劲上有点BUG
        displayRelatedTask(data);
        //显示会话时间信息（例：6分钟前）
        mItemListviewHomeInfoBinding.tvConversationTime.setVisibility(View.VISIBLE);
        if (data.uts == 0) {
            //如果data.uts为0，代表没有消息记录时间，需要隐藏时间，这种情况应该只有消息助手和客服助手才会出现
            mItemListviewHomeInfoBinding.tvConversationTime.setVisibility(View.GONE);
        } else {
            long timeSpan = System.currentTimeMillis() - data.uts;
            long minutes = timeSpan / 1000 / 60;
            if (minutes <= 0) {
                if (data.uid == 1000 || MsgManager.customerServiceUid.equals(data.uid + "")) {
                    //没有和斜杠助手或客服助手收发消息，列表上显示“0 分钟前”-》应该不显示时间
                    mItemListviewHomeInfoBinding.tvConversationTime.setVisibility(View.GONE);
                } else {
                    mItemHomeInfoModel.setConversationTimeInfo("刚刚");
                }
            } else if (minutes < 60) {
                mItemHomeInfoModel.setConversationTimeInfo(minutes + "分钟前");
            } else {
                long hours = minutes / 60;
                if (hours < 24) {
                    mItemHomeInfoModel.setConversationTimeInfo(hours + "小时前");
                } else {
                    long days = hours / 24;
                    mItemHomeInfoModel.setConversationTimeInfo(days + "天前");
                }
            }
        }
//        if (data.isSlashLittleHelper) {
//            mItemHomeInfoModel.setUsername("斜杠小助手");
//            mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.INVISIBLE);
//            mItemHomeInfoModel.setAddVVisibility(View.GONE);
//            mItemHomeInfoModel.setUserLabelsInfoVisibility(View.GONE);
//        } else {
//            mItemHomeInfoModel.setUsername(data.username);
//            if (data.hasRelatedTasks) {
//                mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.VISIBLE);
//            } else {
//                mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.INVISIBLE);
//            }
//            if (data.isAddV) {
//                mItemHomeInfoModel.setAddVVisibility(View.VISIBLE);
//            } else {
//                mItemHomeInfoModel.setAddVVisibility(View.INVISIBLE);
//            }
//            mItemHomeInfoModel.setUserLabelsInfoVisibility(View.VISIBLE);
//        }
    }

    public void displayRelatedTask(ConversationListBean.ConversationInfo data) {
        File dataDir = CommonUtils.getContext().getFilesDir();
        File relatedTaskFiles = new File(dataDir,
                "relatedTaskDir/" + LoginManager.currentLoginUserId + "to" + data.uid);
        if (relatedTaskFiles.exists()) {
            FileInputStream fis = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                fis = new FileInputStream(relatedTaskFiles);
                isr = new InputStreamReader(fis);
                br = new BufferedReader(isr);
                String jsonData = br.readLine();
                Gson gson = new Gson();
                ChatTaskInfoBean chatTaskInfoBean = gson.fromJson(jsonData, ChatTaskInfoBean.class);
                mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.VISIBLE);
                mItemHomeInfoModel.setRelatedTaskTitle(chatTaskInfoBean.title);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(br);
                IOUtils.close(isr);
                IOUtils.close(fis);
            }
        } else {
            mItemHomeInfoModel.setRelatedTasksInfoVisibility(View.INVISIBLE);
        }
    }

}
