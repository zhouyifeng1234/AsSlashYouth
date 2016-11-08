package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseSlashFriendHolder extends BaseHolder<SlashFriendBean> {

    private TextView friendName;
    private ImageView friendIcon;

    @Override
    public View initView() {
        View view = View.inflate(CommonUtils.getContext(), R.layout.item_choose_slash_friend, null);
        friendName = (TextView) view.findViewById(R.id.tv_SlashFriendName);
        friendIcon = (ImageView) view.findViewById(R.id.iv_friend_icon);

        return view;
    }

    @Override
    public void refreshView(SlashFriendBean data) {
        friendName.setText(data.getName());

    }
}
