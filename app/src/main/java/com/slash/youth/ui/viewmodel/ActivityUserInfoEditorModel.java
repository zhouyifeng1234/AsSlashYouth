package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SetBaseProtocol;
import com.slash.youth.http.protocol.SetSaveUserInfoProtocol;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.EditorIdentityActivity;
import com.slash.youth.ui.activity.ReplacePhoneActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zss on 2016/10/31.
 */
public class ActivityUserInfoEditorModel extends BaseObservable {
    private ActivityUserinfoEditorBinding activityUserinfoEditorBinding;
    private String name;
    private String skilldescrib;
    private boolean checked;
    private String setLocation;
    private  HashMap<String, String> paramsMap = new HashMap<>();
    private  HashMap<String, String[]> paramsMaps = new HashMap<>();
    private String textView;
    private String[] slashIdentitys;
    private String editext;
    private String company;
    private String position;
    private String direction;
    private long myUid;//我自己的id
    private UserinfoEditorActivity userinfoEditorActivity;
    private  StringBuffer sb = new StringBuffer();
    public String city;
    public String province;
    private UserInfoItemBean.DataBean.UinfoBean uifo;
    private String phone;
    private String[] skillLabels;
    public ArrayList<String> skillLabelList;
    private String avatar;
    private   boolean  isSetLocation  =false ;
    private String industry;
    private String identity;
    private int careertype;
    private String tag;
    private MyFirstPageBean.DataBean.MyinfoBean myinfo;

    public ActivityUserInfoEditorModel(ActivityUserinfoEditorBinding activityUserinfoEditorBinding, long myUid,
                                       UserinfoEditorActivity userinfoEditorActivity, MyFirstPageBean.DataBean.MyinfoBean myinfo) {
        this.activityUserinfoEditorBinding = activityUserinfoEditorBinding;
        this.myUid = myUid;
        this.myinfo = myinfo;
        this.userinfoEditorActivity = userinfoEditorActivity;
        initData();
        initView();
        listener();
    }

    public ActivityUserInfoEditorModel(ActivityUserinfoEditorBinding activityUserinfoEditorBinding, long myUid,
                                       UserinfoEditorActivity userinfoEditorActivity,   UserInfoItemBean.DataBean.UinfoBean uifo
                                        ,String phone) {
        this.activityUserinfoEditorBinding = activityUserinfoEditorBinding;
        this.myUid = myUid;
        this.userinfoEditorActivity = userinfoEditorActivity;
        this.uifo = uifo;
        this.phone = phone;
        initData();
        initView();
        listener();
    }

    private void initData() {
        if(myUid == -1){//我的
            getMyData();
            MyManager.getMyUserinfo(new OnGetMyUserinfo());
        }else {//个人信息
            getPersonData();
        }
    }

    private void getMyData() {
        avatar = myinfo.getAvatar();
        name = myinfo.getName();
        phone = myinfo.getPhone();
        province =  myinfo.getProvince();
        city = myinfo.getCity();
        identity = myinfo.getIdentity();
        careertype = myinfo.getCareertype();
        company = myinfo.getCompany();
        position = myinfo.getPosition();
        industry = myinfo.getIndustry();
        direction = myinfo.getDirection();
        tag = myinfo.getTag();
    }

    private void getPersonData() {
        //头像的路径
        avatar = uifo.getAvatar();
        name = uifo.getName();
        //所在地
        province =  uifo.getProvince();
        city = uifo.getCity();
        //斜杠身份
        identity = uifo.getIdentity();
        //职业类型
        careertype = uifo.getCareertype();
        //公司
        company = uifo.getCompany();
        //职位
        position = uifo.getPosition();
        //技能描述
        skilldescrib = uifo.getDesc();
        //行业方向
        industry = uifo.getIndustry();
        direction = uifo.getDirection();
        //技能标签
        tag = uifo.getTag();
    }

    private void initView() {
        //头像的路径
        if(avatar!=null){
            x.image().bind(activityUserinfoEditorBinding.ivHead, avatar);
        }
        //姓名
        activityUserinfoEditorBinding.etUsername.setText(name);
        //手机号码
        activityUserinfoEditorBinding.tvUserphone.setText(phone);
        //所在地
        if(province.equals(city)){
            activityUserinfoEditorBinding.tvLocation.setText(province);
        }else {
            activityUserinfoEditorBinding.tvLocation.setText(province +""+city);
        }
        //斜杠身份
        String replace = identity.replace(",", "/");
        activityUserinfoEditorBinding.tvIdentity.setText(replace);
        //职业类型
        switch (careertype){
            case 1://固定职业
                activityUserinfoEditorBinding.rbOfficeWorker.setChecked(true);
                activityUserinfoEditorBinding.rbSelfEmployed.setChecked(false);
                break;
            case 2://自由职业
                activityUserinfoEditorBinding.rbOfficeWorker.setChecked(false);
                activityUserinfoEditorBinding.rbSelfEmployed.setChecked(true);
                break;
        }
        //公司
        activityUserinfoEditorBinding.tvCompany.setText(company);
        //职位
        activityUserinfoEditorBinding.tvProfession.setText(position);
        //技能描述
       /* if(!skilldescrib.isEmpty()){
            activityUserinfoEditorBinding.etSkilldescribe.setText(skilldescrib);
        }*/
        //行业方向
        activityUserinfoEditorBinding.tvDirection.setText(industry +"|"+direction);
        //技能标签
        skillLabels = tag.split(" ");
        List<String> lists = Arrays.asList(skillLabels);
        skillLabelList = new ArrayList<>(lists);
        int length = skillLabels.length;
        switch (length){
            case 1:
                activityUserinfoEditorBinding.tvLine.setVisibility(View.VISIBLE);
                activityUserinfoEditorBinding.tvLine.setText(skillLabels[0]);
                break;
            case 2:
                activityUserinfoEditorBinding.tvLine.setVisibility(View.VISIBLE);
                activityUserinfoEditorBinding.tvLine.setText(skillLabels[0]);
                activityUserinfoEditorBinding.tvLine2.setVisibility(View.VISIBLE);
                activityUserinfoEditorBinding.tvLine2.setText(skillLabels[1]);
                break;
            case 3:
                activityUserinfoEditorBinding.tvLine.setVisibility(View.VISIBLE);
                activityUserinfoEditorBinding.tvLine.setText(skillLabels[0]);
                activityUserinfoEditorBinding.tvLine2.setVisibility(View.VISIBLE);
                activityUserinfoEditorBinding.tvLine2.setText(skillLabels[1]);
                activityUserinfoEditorBinding.tvLine3.setVisibility(View.VISIBLE);
                activityUserinfoEditorBinding.tvLine3.setText(skillLabels[2]);
                break;
        }
    }

    //点击头像
    public void clickAvatar(View view) {
        //去相册里面调用图片
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");//相片类型
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX",1);
        intent.putExtra("aspectY",1);
        intent.putExtra("outputX", 48);
        intent.putExtra("outputY", 48);
        userinfoEditorActivity.startActivityForResult(intent, Constants.USERINFO_IMAGVIEW);
    }

    //设置斜杠身份
    public void editorIdentity(View view) {
        Intent editorIdentityActivity = new Intent(CommonUtils.getContext(), EditorIdentityActivity.class);
        userinfoEditorActivity.startActivityForResult(editorIdentityActivity, Constants.USERINFO_IDENTITY);
    }
    //设置手机号码
    public void setPhone(View view) {
        Intent replacePhoneActivity = new Intent(CommonUtils.getContext(), ReplacePhoneActivity.class);
        userinfoEditorActivity.startActivityForResult(replacePhoneActivity, Constants.USERINFO_PHONE);
    }

    //点击所在地
    public void setLocation(View view) {
       isSetLocation = true;
        Intent intentCityLocationActivity = new Intent(CommonUtils.getContext(), CityLocationActivity.class);
        userinfoEditorActivity.startActivityForResult(intentCityLocationActivity, Constants.USERINFO_LOCATION);
    }

    //技能标签
    public void skillLabel(View view) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        intentSubscribeActivity.putExtra("isEditor",true);
        intentSubscribeActivity.putStringArrayListExtra("addedSkillLabels",skillLabelList);
        userinfoEditorActivity.startActivityForResult(intentSubscribeActivity, Constants.USERINFO_SKILLLABEL);
    }

    //点击保存
    public void save(View view) {
        //保存图片路径
         paramsMap.put("url",avatar);
         SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_AVATAR,paramsMap);
        //保存姓名，技能描述，职业类型
       name = activityUserinfoEditorBinding.etUsername.getText().toString();
        skilldescrib = activityUserinfoEditorBinding.etSkilldescribe.getText().toString();
        checked = activityUserinfoEditorBinding.rbOfficeWorker.isChecked();
        saveUserInfo(name,checked?1:2,skilldescrib);

      //保存公司和职位
        paramsMap.put("company",company);
        paramsMap.put("position",position);
        SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_COMPANY_AND_POSITION,paramsMap);

        //保存所在地
        if(isSetLocation){
           city = CityLocationActivity.map.get("city");
            province = CityLocationActivity.map.get("province");
        }
        if(city!=null&&province!=null){
             paramsMap.put("province",province);
             paramsMap.put("city",city);
             SetProtocol(GlobalConstants.HttpUrl.SET_LOCATION,paramsMap);
        }
        //保存行业方向
        String industryAndDirection = activityUserinfoEditorBinding.tvDirection.getText().toString();
        String[] split = industryAndDirection.split("|");
        industry =  split[0];
        direction = split[1];
        paramsMap.put("industry",industry);
         paramsMap.put("direction",direction);
          SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_INDUSTRY,paramsMap);

        //斜杠身份只能包含中文\英文\数字
   /*     String regex="^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);*/
      /*  ArrayList<String> newSkillLabelList = EditorIdentityModel.newSkillLabelList;
        for (int i = 0; i < newSkillLabelList.size()-1; i++) {
            if(i>=0&&i<newSkillLabelList.size()-1){
                sb.append(newSkillLabelList.get(i));
                sb.append("/");
            }else if(i==newSkillLabelList.size()-1){
                sb.append(newSkillLabelList.get(newSkillLabelList.size()-1));
            }
        }
        paramsMap.put("identity",sb.toString());
        SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_IDENTITY,paramsMap);*/


      /*  String[] s = {"1","2","3"};
        paramsMaps.put("tag",s);
        SetJsonArrayBaseProtocol setJsonArrayBaseProtocol =
                new SetJsonArrayBaseProtocol(GlobalConstants.HttpUrl.SET_SLASH_TAG,paramsMaps);

        setJsonArrayBaseProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SetBean>() {
            @Override
            public void execute(SetBean dataBean) {
                int rescode = dataBean.rescode;
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                LogKit.d("rescode=="+rescode+"======"+status);
            }

            @Override
            public void executeResultError(String result) {
            
            }
        });*/
        //保存技能标签页面
       //  paramsMap.put("tag",tag);
         //  SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_TAG,paramsMap);
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
        //监听名字和描述信息和职业类型的变化
        name = editxetChangeListener(activityUserinfoEditorBinding.etUsername);
        skilldescrib = editxetChangeListener(activityUserinfoEditorBinding.etSkilldescribe);
       /* activityUserinfoEditorBinding.rbOfficeWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.getId() == R.id.rb_office_worker){
                    checked  =  isChecked;
                }
            }
        });*/

        //监听公司和职位
        company = textChangeListener(activityUserinfoEditorBinding.tvCompany);
        position = textChangeListener(activityUserinfoEditorBinding.tvProfession);
        //设置所在地
        setLocation = textChangeListener(activityUserinfoEditorBinding.tvLocation);
        //行业 方向
        direction = textChangeListener(activityUserinfoEditorBinding.tvDirection);
        //监听shenf
        //slashIdentity = textChangeListener(activityUserinfoEditorBinding.tvIdentity);
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


    //设置协议 参数String
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

    //获取个人信息的接口
    public class OnGetMyUserinfo implements BaseProtocol.IResultExecutor<UserInfoItemBean> {
        @Override
        public void execute(UserInfoItemBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                UserInfoItemBean.DataBean data = dataBean.getData();
                UserInfoItemBean.DataBean.UinfoBean uinfo = data.getUinfo();
                String desc = uinfo.getDesc();
                if(!desc.isEmpty()){
                    //技能描述
                    activityUserinfoEditorBinding.etSkilldescribe.setText(desc);
                }

            }else {
                LogKit.d("rescode="+rescode);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}