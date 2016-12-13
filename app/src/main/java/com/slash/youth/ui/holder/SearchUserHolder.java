package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/7.
 */
public class SearchUserHolder extends SearchViewHolder<SearchAllBean.DataBean.UserListBean> {
    private View mRootView;
    TextView tv_zhiye1;
    TextView tv_zhiye2;
    TextView tv_zhiye3;
    TextView tv_search_person_position;
    ImageView iv_search_v;
    TextView iv_jiahao;
    ImageView iv_star;
    ImageView iv_search_person;
    TextView tv_search_person_name;
    TextView tv_time;

    private ArrayList<SearchAllBean.DataBean.UserListBean> userList;

    public SearchUserHolder(ArrayList<SearchAllBean.DataBean.UserListBean> userList) {
        this.userList = userList;
    }

    @Override
    public View initView() {
        mRootView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_person, null);
        tv_search_person_name = (TextView) mRootView.findViewById(R.id.tv_search_person_name);
        iv_search_person = (ImageView) mRootView.findViewById(R.id.iv_avater);
        tv_search_person_position = (TextView) mRootView.findViewById(R.id.tv_search_person_position);
        iv_search_v = (ImageView) mRootView.findViewById(R.id.iv_search_v);
        iv_jiahao = (TextView) mRootView.findViewById(R.id.tv_contacts_visitor_addfriend);
        iv_star = (ImageView) mRootView.findViewById(R.id.iv_star);
        tv_zhiye1 = (TextView) mRootView.findViewById(R.id.tv_zhiye1);
        tv_zhiye2 = (TextView) mRootView.findViewById(R.id.tv_zhiye2);
        tv_zhiye3 = (TextView) mRootView.findViewById(R.id.tv_zhiye3);
        tv_time = (TextView) mRootView.findViewById(R.id.tv_time);
        return mRootView;
    }

    @Override
    public void refreshView(SearchAllBean.DataBean.UserListBean userListBean, final int position) {
        String avatar = userListBean.getAvatar();
        if(avatar!=null){
            BitmapKit.bindImage(iv_search_person, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        String name = userListBean.getName();
        tv_search_person_name.setText(name);

        String userPosition = userListBean.getPosition();
        tv_search_person_position.setText(userPosition);

        int isauth = userListBean.getIsauth();
        switch (isauth){
            case 1:
                iv_search_v.setVisibility(View.VISIBLE);
                break;
            case 0:
                iv_search_v.setVisibility(View.GONE);
                break;
        }

        //加好友
        iv_jiahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchAllBean.DataBean.UserListBean userBean = userList.get(position);
                int uid = userBean.getUid();
                //TODO  根据uid加好友

            }
        });
    }
}
