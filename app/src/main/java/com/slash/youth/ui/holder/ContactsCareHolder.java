package com.slash.youth.ui.holder;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.utils.CommonUtils;

import org.xutils.x;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareHolder extends BaseHolder<ContactsBean.DataBean.ListBean> implements View.OnClickListener {

    private ImageView ivAddMeV;
    private ImageView ivAddMe;
    private ImageView iv;
    private TextView tvName;
    private TextView tvPosition;
    private TextView tvDirection;
    private CardView btn;
    private int uid;

    @Override
    public View initView() {
        View itemView = View.inflate(CommonUtils.getContext(), R.layout.item_contacts_care, null);
        ivAddMe = (ImageView) itemView.findViewById(R.id.iv_addme_icon);
        ivAddMeV = (ImageView) itemView.findViewById(R.id.iv_addme_v);
        iv = (ImageView) itemView.findViewById(R.id.iv);
        tvName = (TextView) itemView.findViewById(R.id.tv_addme_name);
        tvPosition = (TextView) itemView.findViewById(R.id.tv_addme_position);
        tvDirection = (TextView) itemView.findViewById(R.id.tv_addme_direction);
        btn = (CardView) itemView.findViewById(R.id.cd_btn);
        return itemView;
    }

    @Override
    public void refreshView(ContactsBean.DataBean.ListBean data) {
        String avatar = data.getAvatar();
        if(avatar!=null){
            x.image().bind(ivAddMe,avatar);
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

        tvPosition.setText(company+" "+position);
        tvDirection.setText(industry+"|"+direction);

        iv.setVisibility(View.GONE);

        btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {





    }
}
