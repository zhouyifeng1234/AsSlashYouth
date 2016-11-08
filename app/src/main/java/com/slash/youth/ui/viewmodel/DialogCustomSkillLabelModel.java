package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
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

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * Created by zss on 2016/10/20.
 */
public class DialogCustomSkillLabelModel extends BaseObservable {
  private DialogCustomSkilllabelBinding dialogCustomSkilllabelBinding;
  public AlertDialog currentDialog;//当前对话框
    private LinearLayout linearLayout;
    private String name;


    public DialogCustomSkillLabelModel(DialogCustomSkilllabelBinding dialogCustomSkilllabelBinding) {
        this.dialogCustomSkilllabelBinding = dialogCustomSkilllabelBinding;
        dialogCustomSkilllabelBinding.tvTitle.setText("请输入");

    }

    //取消搜索对话框(xml中取消按钮绑定方法)
    public void cancelDialog(View v) {
        //LogKit.d("cancel");
        currentDialog.dismiss();
    }

    //ok搜索对话框
    public void okDialog(View v) {
        String text = dialogCustomSkilllabelBinding.etSkillLabelName.getText().toString();
        boolean contains = text.contains("-");
        char[] chars = text.toCharArray();
        if(!text.isEmpty()){
            if(contains){
                ToastUtils.shortToast("自定义技能名不能有"+"-"+"字符");
            }else {
                int length = chars.length;
                if(length!=10){
                    listener.OnOkDialogClick(text);

                }else {
                    ToastUtils.shortToast("输入的字数不能超过5个");
                }
            }
        }else {
            ToastUtils.shortToast("不能输入空标签");
        }
        currentDialog.dismiss();
    }
    //接口回调传递数据
    public interface OnOkDialogListener{
        void OnOkDialogClick(String text);
    }

    private OnOkDialogListener listener;
    public void setOnOkDialogListener( OnOkDialogListener listener) {
        this.listener = listener;
    }
}
