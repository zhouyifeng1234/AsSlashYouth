package com.slash.youth.ui.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.AgreeRefundBean;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

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
    private TextView over;
    private TextView tv_tag;

    public SearchUserHolder(ArrayList<SearchAllBean.DataBean.UserListBean> userList) {
        this.userList = userList;
    }

    @Override
    public View initView() {
        mRootView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_person, null);
        tv_search_person_name = (TextView) mRootView.findViewById(R.id.tv_search_person_name);
        iv_search_person = (ImageView) mRootView.findViewById(R.id.iv_default_avater);
        tv_search_person_position = (TextView) mRootView.findViewById(R.id.tv_search_person_position);
        iv_search_v = (ImageView) mRootView.findViewById(R.id.iv_isAuth);
        iv_jiahao = (TextView) mRootView.findViewById(R.id.tv_contacts_visitor_addfriend);
        iv_star = (ImageView) mRootView.findViewById(R.id.iv_star);
        tv_zhiye1 = (TextView) mRootView.findViewById(R.id.tv_zhiye1);
        tv_zhiye2 = (TextView) mRootView.findViewById(R.id.tv_zhiye2);
        tv_zhiye3 = (TextView) mRootView.findViewById(R.id.tv_zhiye3);
        tv_time = (TextView) mRootView.findViewById(R.id.tv_time);
        over = (TextView) mRootView.findViewById(R.id.tv_over);
        tv_tag = (TextView) mRootView.findViewById(R.id.tv_search_person_tag);
        return mRootView;
    }

    @Override
    public void refreshView(SearchAllBean.DataBean.UserListBean userListBean, final int position) {
        String name = userListBean.getName();
        String avatar = userListBean.getAvatar();

        if(!TextUtils.isEmpty(avatar)){
            BitmapKit.bindImage(iv_search_person, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        tv_search_person_name.setText(name);

        int isauth = userListBean.getIsauth();
        switch (isauth){
            case 1:
                iv_search_v.setVisibility(View.VISIBLE);
                break;
            case 0:
                iv_search_v.setVisibility(View.GONE);
                break;
        }

        int careertype = userListBean.getCareertype();
        switch (careertype){
            case 1://上班
                String company = userListBean.getCompany();
                String mPosition = userListBean.getPosition();
                if(!TextUtils.isEmpty(company)&&!TextUtils.isEmpty(mPosition)){
                    tv_search_person_position.setText(company+"-"+mPosition);
                }
                break;
            case 2:
                tv_search_person_position.setVisibility(View.GONE);
                break;
        }

        String direction = userListBean.getDirection();
        tv_tag.setText(direction);



        //加好友
       /* iv_jiahao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchAllBean.DataBean.UserListBean userBean = userList.get(position);
                long uid = userBean.getUid();
                ContactsManager.onAddFriendRelationProtocol(new  onAddFriendRelationProtocol(),uid,"   ");
            }
        });*/
    }

    //加好友关系
    public class onAddFriendRelationProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        iv_jiahao.setVisibility(View.GONE);
                        over.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                        ToastUtils.shortToast("申请失败");
                        break;
                }
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
