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
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));

        if (ActivityCompat.checkSelfPermission(CommonUtils.getApplication(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        contactUsActivity.startActivity(intent);


      /*  try {
            final Class<?> phoneFactoryClass = Class.forName("com.android.internal.telephony.PhoneFactory");
            Method makeDefaultPhonesMethod = phoneFactoryClass.getDeclaredMethod("makeDefaultPhones", Context.class);
            makeDefaultPhonesMethod.invoke(null, CommonUtils.getApplication());

            Method getDefaultPhone = phoneFactoryClass.getDeclaredMethod("getDefaultPhone");
            Object phone = getDefaultPhone.invoke(getDefaultPhone);

            final Class<?> callManagerClass = Class.forName("com.android.internal.telephony.CallManager");
            Method getInstanceMethod = callManagerClass.getDeclaredMethod("getInstance");
            Object callManager = getInstanceMethod.invoke(getInstanceMethod);

            final Class<?> phoneClass = Class.forName("com.android.internal.telephony.Phone");
            Method registerPhoneMethod = callManagerClass.getDeclaredMethod("registerPhone", phoneClass);
            registerPhoneMethod.invoke(callManager, phone);

            Method maybeGetMethod = callManagerClass.getDeclaredMethod("getDefaultPhone");
            Object phone1 = maybeGetMethod.invoke(callManager);

            Method dial = phoneClass.getDeclaredMethod("dial", String.class);
            dial.invoke(phone1, phoneNumber);
        } catch (ClassNotFoundException e) {
            String string = e.getCause().toString();
           // Log.e("CallTest" ,string);
        } catch (Exception e) {
            String string = e.getCause().toString();
           // Log.e("CallTest" ,string);
        }*/
    }

    public void updateVersion(View view){
        LogKit.d("邮箱");
        TextView tv = (TextView) view;
        String e_mail = tv.getText().toString();
        Intent data=new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse("mailto:"+e_mail));
     //   data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
     //   data.putExtra(Intent.EXTRA_TEXT, "这是内容");
        contactUsActivity.startActivity(data);
    }

}
