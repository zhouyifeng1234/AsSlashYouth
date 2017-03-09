package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyCollectionBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ItemMyCollectionModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/4.
 */
public class MyCollectionHolder extends BaseHolder<MyCollectionBean.DataBean.ListBean> {

    public ItemMyCollectionBinding itemMyCollectionBinding;
    private ArrayList<MyCollectionBean.DataBean.ListBean> listData;
    private MyCollectionBean mdata;

    public MyCollectionHolder(ArrayList<MyCollectionBean.DataBean.ListBean> listData) {
        this.listData = listData;
    }

    @Override
    public View initView() {
        itemMyCollectionBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_collection, null, false);
        ItemMyCollectionModel itemMyCollectionModel = new ItemMyCollectionModel(itemMyCollectionBinding, listData);
        itemMyCollectionBinding.setItemMyCollectionModel(itemMyCollectionModel);
        return itemMyCollectionBinding.getRoot();
    }

    @Override
    public void refreshView(MyCollectionBean.DataBean.ListBean data) {

        String avatar = data.getAvatar();
        if (avatar != null) {
            if (data.anonymity == 0) {//匿名
                itemMyCollectionBinding.ivAvater.setImageResource(R.mipmap.anonymity_avater);
            } else {//实名
                BitmapKit.bindImage(itemMyCollectionBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
            }
        }

        int instalment = data.getInstalment();//1表示支持分期 0表示不支持分期

        int quote = data.getQuote();//0表示对方报价

        int type = data.getType();//1需求 2服务

        long cts = data.getCts();//收藏时间
        long starttime = data.getStarttime();
        String startTime = TimeUtils.getTime(starttime);

        switch (type) {
            case 1:
                itemMyCollectionBinding.tvFenqi.setText(FirstPagerManager.DEMAND_INSTALMENT);
                switch (instalment) {
                    case 1:
                        itemMyCollectionBinding.tvFenqi.setVisibility(View.GONE);
                        break;
                    case 0:
                        itemMyCollectionBinding.tvFenqi.setVisibility(View.VISIBLE);
                        break;
                }

                if (quote <= 0) {
                    itemMyCollectionBinding.tvMyCollectionQuote.setText(FirstPagerManager.DEMAND_QUOTE);
                } else {
                    itemMyCollectionBinding.tvMyCollectionQuote.setText(MyManager.QOUNT + quote);
                }

               /* if(starttime<=0){
                    itemMyCollectionBinding.tvTime.setText(FirstPagerManager.ANY_TIME);
                }else {
                    itemMyCollectionBinding.tvTime.setText(MyManager.START_TIME+startTime);
                }*/
                break;
            case 2:
                itemMyCollectionBinding.tvFenqi.setText(FirstPagerManager.SERVICE_INSTALMENT);
                switch (instalment) {
                    case 1:
                        itemMyCollectionBinding.tvFenqi.setVisibility(View.VISIBLE);
                        break;
                    case 0:
                        itemMyCollectionBinding.tvFenqi.setVisibility(View.GONE);
                        break;
                }

                int quoteunit = data.getQuoteunit();
                if (quoteunit >= 1 && quoteunit <= 8) {
                    itemMyCollectionBinding.tvMyCollectionQuote.setText(MyManager.QOUNT + quote + "元/" + FirstPagerManager.QUOTEUNITS[quoteunit - 1]);
                } else {
                    itemMyCollectionBinding.tvMyCollectionQuote.setText(MyManager.QOUNT + quote + "元");
                }

                itemMyCollectionBinding.ivTime.setVisibility(View.VISIBLE);
                int timetype = data.getTimetype();
                if (timetype == 0) {
                    // itemMyCollectionBinding.tvTime.setText(startTime);
                } else {
                    itemMyCollectionBinding.tvTime.setText(FirstPagerManager.TIMETYPES[timetype - 1]);
                }
                break;
        }

        int isAuth = data.getIsAuth();//0表示未认证，1表示已经认证
        switch (isAuth) {
            case 1:
                itemMyCollectionBinding.ivV.setVisibility(View.VISIBLE);
                break;
            case 0:
                itemMyCollectionBinding.ivV.setVisibility(View.GONE);
                break;
        }

        String name = data.getName();
        if (data.anonymity == 0) {//匿名
            if (TextUtils.isEmpty(name)) {
                name = "XXX";
            } else {
                name = name.substring(0, 1) + "XX";
            }
        }
        itemMyCollectionBinding.tvName.setText(name);

        String title = data.getTitle();//需求或者服务标题
        itemMyCollectionBinding.tvTitle.setText(title);

        int status = data.getStatus();  //1表示可以预约，0表示不可以
        switch (status) {
            case 1:
                itemMyCollectionBinding.tvMyBtn.setVisibility(View.GONE);
                break;
            case 0:
                itemMyCollectionBinding.tvMyBtn.setVisibility(View.VISIBLE);
                break;
        }

        long tid = data.getTid(); //需求或者服务ID

        long uid = data.getUid();
    }

    @Override
    public void setData(MyCollectionBean.DataBean.ListBean data, final int position) {
        super.setData(data, position);
        //点击删除
        itemMyCollectionBinding.ivDeleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDeleteClick(position);
            }
        });
    }

    public interface OnDeleteClickListener {
        void OnDeleteClick(int position);
    }

    private OnDeleteClickListener listener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

}
