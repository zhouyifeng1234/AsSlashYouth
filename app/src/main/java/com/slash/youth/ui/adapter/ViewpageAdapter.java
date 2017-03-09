package com.slash.youth.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.slash.youth.R;
import com.slash.youth.databinding.ApprovalCertificatesBinding;
import com.slash.youth.ui.activity.ApprovalActivity;
import com.slash.youth.ui.viewmodel.ApprovalCertificatesModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/11/5.
 */
public class ViewpageAdapter extends PagerAdapter {
    private int[] images;
    private String[] names;
    public ApprovalCertificatesBinding approvalCertificatesBinding;
    private ApprovalActivity approvalActivity;
    private ApprovalCertificatesModel approvalCertificatesModel;
    private ImageView imageView;

    public ViewpageAdapter(int[] images,String[] names,ApprovalActivity approvalActivity) {
        this.images = images;
        this.names = names;
        this.approvalActivity = approvalActivity;
    }

    @Override
    public int getCount() {
        return images.length+2;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    approvalCertificatesBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.approval_certificates, null, false);
    int realPosition = (position - 1 + images.length)%images.length;
    approvalCertificatesModel = new ApprovalCertificatesModel(approvalCertificatesBinding,approvalActivity,realPosition);
    approvalCertificatesBinding.setApprovalCertificatesModel(approvalCertificatesModel);
    View itemView = getView(realPosition);
    container.addView(itemView);
    return itemView;
    }

    //获取图片
    private View getView(int realPosition) {
        imageView = new ImageView(CommonUtils.getContext());
        imageView.setImageResource(images[realPosition]);
        approvalCertificatesBinding.flCertificates.addView(imageView);
        approvalCertificatesBinding.tvCertificatesName.setText(names[realPosition]);
        return approvalCertificatesBinding.getRoot();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
