package com.slash.youth.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by acer on 2016/11/6.
 */
public class RecordHolder extends BaseHolder<TransactionRecoreBean.DataBean.ListBean> {

    private View view;
    private TextView tv_type;
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_amount;

    @Override
    public View initView() {
        view = View.inflate(CommonUtils.getContext(), R.layout.layout_record, null);
        tv_type = (TextView) view.findViewById(R.id.tv_type);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_time = (TextView) view.findViewById(R.id.tv_time);
        tv_amount = (TextView) view.findViewById(R.id.tv_amount);
        return view;
    }

    @Override
    public void refreshView(TransactionRecoreBean.DataBean.ListBean data) {
        int type = data.getType();
        String title = data.getTitle();
        float amount = data.getAmount();
        long cts = data.getCts();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(new Date(cts));
        tv_time.setText(dateStr);
        tv_title.setText(title);
        switch (type){
            case 1://收入
                tv_type.setText("收入");
                tv_amount.setText("+"+amount+"元");
                tv_amount.setTextColor(Color.parseColor("#FF7333"));
                break;
            case 2://支出
                tv_type.setText("支出");
                tv_amount.setText("-"+amount+"元");
                tv_amount.setTextColor(Color.parseColor("#31C5E4"));
                break;
        }
    }
}
