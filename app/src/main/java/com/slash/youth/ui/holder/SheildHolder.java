package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.domain.SheildPersonBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/11/3.
 */
public class SheildHolder extends BaseHolder<SheildPersonBean> {

    private View itemSheild;

    @Override
    public View initView() {
        itemSheild = View.inflate(CommonUtils.getContext(), R.layout.item_choose_slash_friend, null);
        itemSheild.findViewById(R.id.cb_sheld).setVisibility(View.VISIBLE);
        return itemSheild;
    }

    @Override
    public void refreshView(SheildPersonBean data) {



    }
}
