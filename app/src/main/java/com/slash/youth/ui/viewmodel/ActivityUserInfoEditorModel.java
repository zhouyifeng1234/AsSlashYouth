package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.net.sip.SipAudioCall;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SetBaseProtocol;
import com.slash.youth.http.protocol.SetSaveUserInfoProtocol;
import com.slash.youth.ui.activity.EditorIdentityActivity;
import com.slash.youth.ui.activity.ReplacePhoneActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtil;
import com.slash.youth.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by acer on 2016/10/31.
 */
public class ActivityUserInfoEditorModel extends BaseObservable {
    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;
    private String name;
    private String skilldescrib;
    private boolean checked;
    private String setLocation;
    private  HashMap<String, String> paramsMap = new HashMap<>();
    private String textView;
    private String[] slashIdentitys;
    private String editext;
    private String company;
    private String position;
    private String direction;

    public ActivityUserInfoEditorModel(ActivityUserinfoEditorBinding activityUserinfoEditorBinding) {
        this.activityUserinfoEditorBinding = activityUserinfoEditorBinding;
        initView();

        listener();
    }


    private void initView() {
       // activityUserinfoEditorBinding.etUsername.setText("小玩");

        //初始化的时候获取一下，但是时刻监听变化
        name = activityUserinfoEditorBinding.etUsername.getText().toString();
        skilldescrib = activityUserinfoEditorBinding.tvSkilldescribe.getText().toString();
        checked = activityUserinfoEditorBinding.rbOfficeWorker.isChecked();

    }

    //设置斜杠身份
    public void editorIdentity(View view) {
        //先跳转到身份编辑页面，获取到身份后会显示这个页面
        Intent editorIdentityActivity = new Intent(CommonUtils.getContext(), EditorIdentityActivity.class);
        editorIdentityActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(editorIdentityActivity);
        // TODO 等到数据回来，可以传到后端

       // paramsMap.put("identity",slashIdentitys);
       // SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_IDENTITY,paramsMap);
    }

    public void setPhone(View view) {
        Intent replacePhoneActivity = new Intent(CommonUtils.getContext(), ReplacePhoneActivity.class);
        replacePhoneActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(replacePhoneActivity);
    }

    //skillLabel
    public void skillLabel(View view) {
        LogKit.d("点击技能标签");
        //去技能标签页面选标签

       // paramsMap.put("tag",tag);
      //   SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_TAG,paramsMap);


    }


    //点击头像
    public void clickAvatar(View view) {
        LogKit.d("点击头像");
        //去相册里面调用图片

        //图片路径
        String url = "";
        paramsMap.put("url",url);
       // SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_AVATAR,paramsMap);


    }

    //点击所在地
    public void setLocation(View view) {
      LogKit.d("点击所在地");
        // TODO 先跳转到选择地点的页面，获取省和市之后，回显到现在页面，并监听改变赋值

        paramsMap.put("province",setLocation);
        paramsMap.put("city",setLocation);
       // SetProtocol(GlobalConstants.HttpUrl.SET_LOCATION,paramsMap);
    }

    //点击保存
    public void save(View view) {
       // saveUserInfo(name,checked?1:2,skilldescrib);

      //保存的时候，我也保存一下公司和职位
      //  paramsMap.put("company",company);
       // paramsMap.put("position",position);
      //  SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_COMPANY_AND_POSITION,paramsMap);

       //保存行业方向
       // direction
        paramsMap.put("industry",company);
         paramsMap.put("direction",position);
       //  SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_INDUSTRY,paramsMap);

    }

    private void saveUserInfo(String name,int careertype,String desc) {
        if(name.length()<256&&desc.length()<4096){
            SetSaveUserInfoProtocol setSaveUserInfoProtocol = new SetSaveUserInfoProtocol(name, careertype,desc);
            setSaveUserInfoProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
                @Override
                public void execute(SetBean dataBean) {
                    int rescode = dataBean.rescode;
                    if(rescode!=0){
                        LogKit.d("保存用户信息错误");
                    }
                }

                @Override
                public void executeResultError(String result) {
                LogKit.d("result:"+result);
                }
            });

        }

        if(name.length()>256){
            ToastUtils.shortToast("姓名字数过多");
        }

        if(desc.length()>4096){
            ToastUtils.shortToast("技能描述字数过多");
        }
    }

    private void listener() {

        name = editxetChangeListener(activityUserinfoEditorBinding.etUsername);
        skilldescrib = editxetChangeListener(activityUserinfoEditorBinding.tvSkilldescribe);

        activityUserinfoEditorBinding.rbOfficeWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.getId() == R.id.rb_office_worker){
                    checked  =  isChecked;
                }
            }
        });

        setLocation = textChangeListener(activityUserinfoEditorBinding.tvLocation);
        //公司
        company = textChangeListener(activityUserinfoEditorBinding.tvCompany);
        //职位
        position = textChangeListener(activityUserinfoEditorBinding.tvProfession);
        //slashIdentity = textChangeListener(activityUserinfoEditorBinding.tvIdentity);
        //行业 方向
        direction = textChangeListener(activityUserinfoEditorBinding.tvDirection);


    }

    //Editext的监听
    private String editxetChangeListener(EditText et) {
       et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                editext = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                editext = s.toString();
            }
        });

        return editext;
    }

    //textview 的监听
    private String textChangeListener(TextView tv) {

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                 textView = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textView = s.toString();
            }
        });
        return textView;
    }


    //设置协议
    private void SetProtocol(String url,Map<String,String> paramsMap) {
        SetBaseProtocol setBaseProtocol = new SetBaseProtocol(url,paramsMap);
        setBaseProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                if(rescode == 0){
                    LogKit.d("SetBaseProtocol: 有错误");
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result:"+result);
            }
        });
        paramsMap.clear();
    }

}