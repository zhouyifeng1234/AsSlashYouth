package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.DialogCustomSkilllabelBinding;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

/**
 * Created by zss on 2016/10/20.
 */
public class DialogCustomSkillLabelModel extends BaseObservable {
  private DialogCustomSkilllabelBinding dialogCustomSkilllabelBinding;
  public AlertDialog currentDialog;//当前对话框
 private SubscribeActivity subscribeActivity;

    public DialogCustomSkillLabelModel(DialogCustomSkilllabelBinding dialogCustomSkilllabelBinding, SubscribeActivity activity) {
        this.dialogCustomSkilllabelBinding = dialogCustomSkilllabelBinding;
        this.subscribeActivity = activity;
    }

    //取消搜索对话框(xml中取消按钮绑定方法)
    public void cancelDialog(View v) {
        //LogKit.d("cancel");
        currentDialog.dismiss();
    }

    //ok搜索对话框
    public void okDialog(View v) {
        String text = dialogCustomSkilllabelBinding.etSkillLabelName.getText().toString();
        char[] chars = text.toCharArray();
        for (char ch : chars) {
            if(ch == '-'){
                ToastUtils.shortToast("自定义技能名不能有"+"-"+"字符");
            }else {
                int length = chars.length;
                if(length!=10){
                    LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-2, -2);
                    TextView textview = new TextView(CommonUtils.getContext());
                    textview.setLayoutParams(ll);
                    textview.setMaxLines(1);
                    textview.setGravity(Gravity.CENTER);
                    textview.setTextColor(0xff333333);
                    textview.setTextSize(14);
                    textview.setText(text);
                    textview.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(3), CommonUtils.dip2px(11));

                    ImageView imageView = new ImageView(CommonUtils.getContext());
                    imageView.setImageResource(R.mipmap.delete_icon);
                    imageView.setPadding(CommonUtils.dip2px(3), CommonUtils.dip2px(13), CommonUtils.dip2px(13), CommonUtils.dip2px(13));
                    imageView.setLayoutParams(ll);

                    LinearLayout linearLayout = new LinearLayout(CommonUtils.getContext());
                    linearLayout.addView(textview);
                    linearLayout.addView(imageView);
                    linearLayout.setBackgroundResource(R.drawable.shape_rounded_rectangle_third_skilllabel);
                    linearLayout.setGravity(Gravity.CENTER);
                    subscribeActivity.customSkillLabel = linearLayout;

                    //在里面加上去
                  // subscribeActivity.mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.


                }else {
                    ToastUtils.shortToast("输入的字数不能超过5个");
                }
            }
        }
        currentDialog.dismiss();
    }
}
