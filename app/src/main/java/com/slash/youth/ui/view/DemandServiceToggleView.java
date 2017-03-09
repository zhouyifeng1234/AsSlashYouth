package com.slash.youth.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.slash.youth.R;

/**
 * Created by zhouyifeng on 2017/2/26.
 */
public class DemandServiceToggleView extends FrameLayout {

    private Context context;

    public DemandServiceToggleView(Context context) {
        super(context);
        this.context = context;
    }

    public DemandServiceToggleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public DemandServiceToggleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public void initView(boolean isCheckedService) {
        initView();
        if (isCheckedService) {
            checkedService();
        } else {
            checkedDemand();
        }
    }

    TextView tvService;
    TextView tvDemand;

    private void initView() {
        View view = View.inflate(context, R.layout.view_service_demand_toggle, null);
        tvService = (TextView) view.findViewById(R.id.tv_service_toggle);
        tvDemand = (TextView) view.findViewById(R.id.tv_demand_toggle);
        tvService.setOnClickListener(new ServiceOnClickListener());
        tvDemand.setOnClickListener(new DemandOnClickListener());
        this.addView(view);
    }

    private class ServiceOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            checkedService();
            if (mServiceDemandToggle != null) {
                mServiceDemandToggle.toggleServiceDemand(true);
            }
        }
    }

    private class DemandOnClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            checkedDemand();
            if (mServiceDemandToggle != null) {
                mServiceDemandToggle.toggleServiceDemand(false);
            }
        }
    }

    private void checkedService() {
        tvService.setBackgroundResource(R.drawable.shape_service_toggle_select);
        tvService.setTextColor(0xff333333);
        tvDemand.setBackgroundResource(R.drawable.shape_demand_toggle_unselect);
        tvDemand.setTextColor(0xffffffff);
    }

    private void checkedDemand() {
        tvService.setBackgroundResource(R.drawable.shape_service_toggle_unselect);
        tvService.setTextColor(0xffffffff);
        tvDemand.setBackgroundResource(R.drawable.shape_demand_toggle_select);
        tvDemand.setTextColor(0xff333333);
    }

    private IServiceDemandToggle mServiceDemandToggle;

    public interface IServiceDemandToggle {
        void toggleServiceDemand(boolean isCheckedService);
    }

    public void setServiceDemandToggle(IServiceDemandToggle iServiceDemandToggle) {
        this.mServiceDemandToggle = iServiceDemandToggle;
    }
}
