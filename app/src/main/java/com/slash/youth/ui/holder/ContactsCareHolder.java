package com.slash.youth.ui.holder;

import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.domain.MyFriendListBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.ContactsManager;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.adapter.ChooseFriendAdapter;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import org.xutils.x;

import java.util.List;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareHolder extends BaseHolder<ContactsBean.DataBean.ListBean> implements View.OnClickListener {
    private ImageView ivAddMeV;
    private ImageView ivAddMe;
    private ImageView iv;
    private TextView tvAddOk;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvDirection;
    public static CardView btn;
    private long uid;
    private int type;
    private int isfriend;
    private TextView title;
    private int status;
    private TextView tvCompany;

    public ContactsCareHolder(int type) {
        this.type = type;
    }

    @Override
    public View initView() {
        View itemView = View.inflate(CommonUtils.getContext(), R.layout.item_contacts_care_new, null);
        ivAddMe = (ImageView) itemView.findViewById(R.id.iv_addme_icon);
        ivAddMeV = (ImageView) itemView.findViewById(R.id.iv_addme_v);
        iv = (ImageView) itemView.findViewById(R.id.iv);
        tvName = (TextView) itemView.findViewById(R.id.tv_addme_name);
        tvPosition = (TextView) itemView.findViewById(R.id.tv_addme_position);
        tvAddOk = (TextView) itemView.findViewById(R.id.tv_add_ok);
        tvDirection = (TextView) itemView.findViewById(R.id.tv_addme_direction);
        title = (TextView) itemView.findViewById(R.id.tv_btn_title);
        btn = (CardView) itemView.findViewById(R.id.cd_btn);
        tvCompany = (TextView) itemView.findViewById(R.id.tv_addme_company);
        return itemView;
    }

    @Override
    public void refreshView(ContactsBean.DataBean.ListBean data) {
        String avatar = data.getAvatar();
        if(!TextUtils.isEmpty(avatar)){
            BitmapKit.bindImage(ivAddMe, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        int isauth = data.getIsauth();
        switch (isauth){
            case 1:
                ivAddMeV.setVisibility(View.VISIBLE);
                break;
            case 0:
                ivAddMeV.setVisibility(View.GONE);
                break;
            default:
                ivAddMeV.setVisibility(View.GONE);
                break;
        }

        String name = data.getName();
        tvName.setText(name);

        String direction = data.getDirection();
        String industry = data.getIndustry();
        String position = data.getPosition();
        String company = data.getCompany();
        uid = data.getUid();
        long uts = data.getUts();

        if(!TextUtils.isEmpty(direction)){
        tvDirection.setText(direction);
        }

        int careertype = data.getCareertype();
        switch (careertype){
            case 1:
                if(!TextUtils.isEmpty(company)&&!TextUtils.isEmpty(position)){
                    tvCompany.setText(company);
                    tvPosition.setText("-"+position);
                }
                break;
            case 2:
                tvCompany.setText("自雇者");
                break;
        }

        btn.setOnClickListener(this);
        iv.setVisibility(View.GONE);

       // isfriend = data.getIsfriend();
        status = data.getStatus();

        switch (type){
            case 1:
                btn.setVisibility(View.GONE);
                break;
            case 3:
                switch (status){
                    case 2://已是好友//     2是好友
                        tvAddOk.setVisibility(View.VISIBLE);
                        break;
                    default://显示同意
                        title.setVisibility(View.VISIBLE);
                        break;
                }
                break;
            case 2:
            case 4:
                btn.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        //埋点
        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.RELATIONSHIP_CLICK_ADD_ME_CLICK_AGREEN);

        ContactsManager.onAgreeFriendProtocol(new onAgreeFriendProtocol(),uid,"  ");
    }

    public class onAgreeFriendProtocol implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status){
                    case 1:
                        ToastUtils.shortCenterToast("已是好友");
                        title.setVisibility(View.GONE);
                        tvAddOk.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                        ToastUtils.shortCenterToast("添加好友未成功");
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
