package com.slash.youth.ui.holder;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewSearchPersonBinding;
import com.slash.youth.domain.SearchUserBean;
import com.slash.youth.domain.SearchUserItemBean;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import org.xutils.HttpManager;
import org.xutils.ImageManager;
import org.xutils.x;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerSearchPersonHolder extends BaseHolder<SearchUserItemBean.DataBean.ListBean> implements View.OnClickListener {

    private ItemListviewSearchPersonBinding itemListviewSearchPersonBinding;
    private int isauth;

    @Override
    public View initView() {
        itemListviewSearchPersonBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_search_person, null, false);
        itemListviewSearchPersonBinding.tvContactsVisitorAddfriend.setOnClickListener(this);

        return itemListviewSearchPersonBinding.getRoot();
    }

    @Override
    public void refreshView(SearchUserItemBean.DataBean.ListBean data) {
        //头像图片
      /*  x.image().bind(itemListviewSearchPersonBinding.ivSearchPerson, data.getAvatar());
        String company = data.getCompany();
        int id = data.getId();
        String identity = data.getIdentity();
        itemListviewSearchPersonBinding.tvZhiye1.setText(identity);

        isauth = data.getIsauth();
        setAddFriendBtn(itemListviewSearchPersonBinding,isauth);

        int isfriend = data.getIsfriend();
        if(isfriend == 1){
            //好友关系
            itemListviewSearchPersonBinding.tvContactsVisitorAddfriend.setVisibility(View.GONE);
        }else if(isfriend == 0){
            //非好友
            itemListviewSearchPersonBinding.tvContactsVisitorAddfriend.setVisibility(View.INVISIBLE);
        }
        long lasttime = data.getLasttime();
        //设置时间
        setTime(lasttime);

        String name = data.getName();
        itemListviewSearchPersonBinding.tvSearchPersonName.setText(name);

        String profession = data.getProfession();
        itemListviewSearchPersonBinding.tvSearchPersonPosition.setText(profession);
        int star = data.getStar();
        for (int i = 0; i <= star; i++) {
            ImageView imageView = new ImageView(CommonUtils.getContext());
            imageView.setImageResource(R.mipmap.star);
            itemListviewSearchPersonBinding.llStar.addView(imageView);
        }
    }

    private static void setAddFriendBtn(ItemListviewSearchPersonBinding itemListviewSearchPersonBinding,int isauth) {
        if(isauth == 0){
            itemListviewSearchPersonBinding.ivSearchV.setVisibility(View.VISIBLE);
        }else if(isauth == 1){
            itemListviewSearchPersonBinding.ivSearchV.setVisibility(View.GONE);
        }else {
            LogKit.d("输入信息不正确，isauth="+ isauth);
        }
    }

    //点击事件
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_contacts_visitor_time:
                itemListviewSearchPersonBinding.ivSearchV.setVisibility(View.GONE);
                isauth =1;
                //最好能传到后端，保存一下
            break;
        }
    }

    //设置时间
    private void setTime( long lasttime) {
        //开始的时间，从登陆的时间开始算
        long startTime = (long) 148123123120.0;
        //先对毫秒进行判断，
        if(lasttime>(60*60*60*12)){
        LogKit.d("超过12个小时");
        }else {
            long time = lasttime - startTime;
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
            String hour = formatter.format(time);
            itemListviewSearchPersonBinding.tvTime.setText(hour+"时"+"在线");
        }*/
    }

    @Override
    public void onClick(View v) {

    }
}
