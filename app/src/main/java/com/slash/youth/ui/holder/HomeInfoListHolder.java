package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeInfoBinding;
import com.slash.youth.domain.ConversationListBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ItemHomeInfoModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;

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
        mItemHomeInfoModel.setUsername(data.name);
        BitmapKit.bindImage(mItemListviewHomeInfoBinding.ivInfoConversationAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + data.avatar);
        if (data.isAuth == 1) {
            mItemHomeInfoModel.setAddVVisibility(View.VISIBLE);
        } else {
            mItemHomeInfoModel.setAddVVisibility(View.GONE);
        }
        int unreadCount = RongIMClient.getInstance().getUnreadCount(Conversation.ConversationType.PRIVATE, data.uid + "");
        mItemListviewHomeInfoBinding.tvInfoUnreadMsgCount.setText(unreadCount + "");
        mItemHomeInfoModel.setCompanyAndPosition("(" + data.company + "," + data.position + ")");
        RongIMClient.getInstance().getLatestMessages(Conversation.ConversationType.PRIVATE, data.uid + "", 1, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages != null) {
                    if (messages.size() > 0) {
                        Message message = messages.get(0);
                        String objectName = message.getObjectName();
                        if (objectName.equals("RC:TxtMsg")) {
                            TextMessage textMessage = (TextMessage) message.getContent();
                            String extra = textMessage.getExtra();
                            if (TextUtils.isEmpty(extra)) {
                                mItemHomeInfoModel.setLastMsg(textMessage.getContent());
                            } else {

                            }
                        } else if (objectName.equals("RC:ImgMsg")) {
                            mItemHomeInfoModel.setLastMsg("有图片消息");
                        } else if (objectName.equals("RC:VcMsg")) {
                            mItemHomeInfoModel.setLastMsg("有语音消息");
                        }
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
        //相关任务的加载，通过本地文件存储,暂时路劲上有点BUG

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
}
