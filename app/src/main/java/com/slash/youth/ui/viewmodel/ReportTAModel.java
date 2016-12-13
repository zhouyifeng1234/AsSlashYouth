package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.slash.youth.databinding.ActivityReportTaBinding;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.ReportTAActivity;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SetDataUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by acer on 2016/11/2.
 */
public class ReportTAModel extends BaseObservable {

    private ActivityReportTaBinding activityReportTaBinding;
    private int num;
    private int totalNum = 300;
    private boolean checked;
    public ArrayList<String> list = new ArrayList<>();
    public String text; //举报的内容
    private int uid;
    private  HashMap<String, String> paramMap = new HashMap<>();

    public ReportTAModel(ActivityReportTaBinding activityReportTaBinding,int uid) {
        this.activityReportTaBinding = activityReportTaBinding;
        this.uid = uid;
        initView();

    }

    private void initView() {

        list.clear();
        getCheckable(activityReportTaBinding.cb1);
        getCheckable(activityReportTaBinding.cb2);
        getCheckable(activityReportTaBinding.cb3);

        activityReportTaBinding.etReportOther.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                text = s.toString();
                //我写的字数
                 num = s.length();
                activityReportTaBinding.tvReportNum.setText(num+"/"+totalNum);
               // selectionStart=activityReportTaBinding.etReportOther.getSelectionStart();
                selectionEnd = activityReportTaBinding.etReportOther.getSelectionEnd();
                if (wordNum.length() > totalNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    activityReportTaBinding.etReportOther.setText(s);
                    activityReportTaBinding.etReportOther.setSelection(tempSelection);//设置光标在最后
                }

               list.add("其他举报原因:"+text+" ");

            }
        });
    }

    private void getCheckable(CheckBox cb) {
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String text = buttonView.getText().toString();
                    list.add("固定举报原因:"+text+" ");

                }
            }
        });
    }

    //想服务器传递数据
    public  void  sendData() {
        String reason = list.toString();
        paramMap.put("QQ_uid",String.valueOf(uid));
        paramMap.put("reason",reason);
        SetDataUtils.setProtocol(GlobalConstants.HttpUrl.CLAIMS,paramMap);
        paramMap.clear();
    }

}
