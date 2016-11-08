package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.net.sip.SipSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityEditorIdentityBinding;
import com.slash.youth.databinding.DialogCustomSkilllabelBinding;
import com.slash.youth.ui.activity.EditorIdentityActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by acer on 2016/11/1.
 */
public class EditorIdentityModel extends BaseObservable {

    private ActivityEditorIdentityBinding activityEditorIdentityBinding;
    private EditorIdentityActivity editorIdentityActivity;
    private DialogCustomSkillLabelModel  dialogCustomSkillLabelModel;

    public EditorIdentityModel(ActivityEditorIdentityBinding activityEditorIdentityBinding, EditorIdentityActivity editorIdentityActivity) {
        this.activityEditorIdentityBinding = activityEditorIdentityBinding;
        this.editorIdentityActivity = editorIdentityActivity;
        initData();
        initView();
        listener();

    }

    private void initData() {

    }

    private void initView() {

        //先做添加标签
        //View.inflate()

        //点击，动态穿件标签

        //新穿件的在添加在里面

    }

    private void listener() {
        activityEditorIdentityBinding.btnAddIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();

                dialogCustomSkillLabelModel.setOnOkDialogListener(new DialogCustomSkillLabelModel.OnOkDialogListener() {
                    @Override
                    public void OnOkDialogClick(String text) {

                        LogKit.d("标签名"+text);

                    }
                });
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(editorIdentityActivity);
        DialogCustomSkilllabelBinding dialogCustomSkilllabelBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_custom_skilllabel, null, false);
        dialogCustomSkillLabelModel = new DialogCustomSkillLabelModel(dialogCustomSkilllabelBinding);
        dialogCustomSkilllabelBinding.setDialogCustomSkillLabelModel(dialogCustomSkillLabelModel);
        dialogBuilder.setView(dialogCustomSkilllabelBinding.getRoot());
        AlertDialog dialogCustomSkillLabel = dialogBuilder.create();
        dialogCustomSkillLabel.show();
        dialogCustomSkillLabelModel.currentDialog = dialogCustomSkillLabel;
        dialogCustomSkillLabel.setCanceledOnTouchOutside(false);
        Window dialogSubscribeWindow = dialogCustomSkillLabel.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(300);//宽度
        dialogParams.height = CommonUtils.dip2px(190);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);//dialog的灰度
//        dialogBuilder.show();
    }
}
