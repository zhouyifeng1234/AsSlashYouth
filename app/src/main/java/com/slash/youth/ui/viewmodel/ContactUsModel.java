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

import java.lang.reflect.Method;

/**
 * Created by zss on 2016/11/4.
 */
public class ContactUsModel extends BaseObservable {

    private ActivityContactUsBinding activityContactUsBinding;
    private ContactUsActivity contactUsActivity;

    public ContactUsModel(ActivityContactUsBinding activityContactUsBinding, ContactUsActivity contactUsActivity) {
        this.activityContactUsBinding = activityContactUsBinding;
        this.contactUsActivity = contactUsActivity;
    }

    public void question(View view) {
        LogKit.d("问题");
    }

    public void contactUs(View view) {
        String phoneNumber = activityContactUsBinding.tvPhone.getText().toString();
        Uri uri = Uri.parse("tel:"+"400400400");
        Intent intent2 = new Intent(Intent.ACTION_DIAL, uri);
        contactUsActivity.startActivity(intent2);
    }

    public void updateVersion(View view){
        String url =  activityContactUsBinding.tvUrl.getText().toString();
        Uri uri = Uri.parse("http://"+"service@slashyanger.com");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        contactUsActivity.startActivity(intent);
    }

}
