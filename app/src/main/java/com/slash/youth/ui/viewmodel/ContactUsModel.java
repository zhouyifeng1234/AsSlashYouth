package com.slash.youth.ui.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.databinding.ActivityContactUsBinding;
import com.slash.youth.ui.activity.ContactUsActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.lang.reflect.Method;

/**
 * Created by zss on 2016/11/4.
 */
public class ContactUsModel extends BaseObservable {

    private ActivityContactUsBinding activityContactUsBinding;
    private ContactUsActivity contactUsActivity;
    private String phoneNumber = "400400400";
    private String address = "service@slashyanger.com";

    public ContactUsModel(ActivityContactUsBinding activityContactUsBinding, ContactUsActivity contactUsActivity) {
        this.activityContactUsBinding = activityContactUsBinding;
        this.contactUsActivity = contactUsActivity;
    }

    public void hepler(View view) {

        ToastUtils.shortCenterToast("斜杠小助手");

        LogKit.d("斜杠小助手");
    }

    public void phone(View view) {
        String phoneNumber = activityContactUsBinding.tvPhone.getText().toString();
        Uri uri = Uri.parse("tel:"+phoneNumber);
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, uri);
        contactUsActivity.startActivity(phoneIntent);
    }

    public void email(View view){
        String url =  activityContactUsBinding.tvUrl.getText().toString();
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:"+url));
        contactUsActivity.startActivity(data);
    }

}
