package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseSlashFriendHolder extends BaseHolder<MyFriendListBean.DataBean.ListBean> {

    private TextView friendName;
    private ImageView friendV;
    private TextView tvdirection;
    private ImageView ivavatar;

    @Override
    public View initView() {
        View view = View.inflate(CommonUtils.getContext(), R.layout.item_choose_slash_friend, null);
        friendName = (TextView) view.findViewById(R.id.tv_friend_name);
        friendV = (ImageView) view.findViewById(R.id.iv_friend_v);
        tvdirection = (TextView) view.findViewById(R.id.tv_direction);
        ivavatar = (ImageView) view.findViewById(R.id.iv_friend_icon);
        return view;
    }

    @Override
    public void refreshView(MyFriendListBean.DataBean.ListBean data) {
        friendName.setText(data.getName());

        String avatar = data.getAvatar();
        if(avatar!=null){
            BitmapKit.bindImage(ivavatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        int isauth = data.getIsauth();
        switch (isauth){
            case 1:
                friendV.setVisibility(View.VISIBLE);
                break;
            case 0:
                friendV.setVisibility(View.GONE);
                break;
        }

        String company = data.getCompany();
        String position = data.getPosition();
        tvdirection.setText(company+""+position);

        String direction = data.getDirection();
        String industry = data.getIndustry();
        int status = data.getStatus();
        int uid = data.getUid();
        long uts = data.getUts();

    }
}
