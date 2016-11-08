package com.slash.youth.ui.holder;

import android.view.View;

import com.slash.youth.R;
import com.slash.youth.domain.RecordBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by acer on 2016/11/6.
 */
public class RecordHolder extends BaseHolder<RecordBean> {

    private View view;

    @Override
    public View initView() {
        view = View.inflate(CommonUtils.getContext(), R.layout.layout_record, null);


        return view;
    }

    @Override
    public void refreshView(RecordBean data) {

    }
}
