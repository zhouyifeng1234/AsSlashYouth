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
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
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
public class PagerSearchPersonHolder extends BaseHolder<SearchUserItemBean.DataBean.ListBean>  {

    private ItemListviewSearchPersonBinding itemListviewSearchPersonBinding;
    private int isauth;

    @Override
    public View initView() {
        itemListviewSearchPersonBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_search_person, null, false);
      //  itemListviewSearchPersonBinding.tvContactsVisitorAddfriend.setOnClickListener(this);
        return itemListviewSearchPersonBinding.getRoot();
    }

    @Override
    public void refreshView(SearchUserItemBean.DataBean.ListBean data) {
        String direction = data.getDirection();
        itemListviewSearchPersonBinding.tvSearchPersonTag.setText(direction);

        String avatar = data.getAvatar();
        if (avatar != null) {
            BitmapKit.bindImage(itemListviewSearchPersonBinding.ivDefaultAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        String name = data.getName();
        itemListviewSearchPersonBinding.tvSearchPersonName.setText(name);

        int careertype = data.getCareertype();
        switch (careertype){
            case 1:
                String userPosition = data.getPosition();
                String company = data.getCompany();
                itemListviewSearchPersonBinding.tvSearchPersonPosition.setText(company+"-"+userPosition);
                break;
            case 2:
                itemListviewSearchPersonBinding.tvSearchPersonPosition.setVisibility(View.GONE);
                break;
        }


        int isauth = data.getIsauth();
        switch (isauth) {
            case 1:
                itemListviewSearchPersonBinding.ivIsAuth.setVisibility(View.VISIBLE);
                break;
            case 0:
                itemListviewSearchPersonBinding.ivIsAuth.setVisibility(View.GONE);
                break;
        }
    }
}
