package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemListviewHomeDemandBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by zhouyifeng on 2016/9/6.
 */
public class PagerHomeDemandHolder extends BaseHolder<DemandBean> {

    private ItemListviewHomeDemandBinding itemListviewHomeDemandBinding;

    @Override
    public View initView() {
        itemListviewHomeDemandBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_home_demand, null, false);

        return itemListviewHomeDemandBinding.getRoot();
    }

    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.ENGLISH);
    @Override
    public void refreshView(DemandBean data) {
        String dateStr = sdf.format(new Date(data.lasttime));
        itemListviewHomeDemandBinding.tvItemListviewHomeDemandTime.setText(dateStr);
    }

}
