package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivityUserinfoEditorBinding;
import com.slash.youth.databinding.ActivityUserinfoHeardListviewBinding;
import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.engine.UserInfoEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SetBaseProtocol;
import com.slash.youth.http.protocol.SetJsonArrayBaseProtocol;
import com.slash.youth.http.protocol.SetSaveUserInfoProtocol;
import com.slash.youth.ui.activity.CityLocationActivity;
import com.slash.youth.ui.activity.EditorIdentityActivity;
import com.slash.youth.ui.activity.ReplacePhoneActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.activity.UserinfoEditorActivity;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.PatternUtils;
import com.slash.youth.utils.ToastUtils;

import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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
    private String phone;
    private String[] skillLabels;
    public ArrayList<String> skillLabelList;
    public String avatar;
    private   boolean  isSetLocation  =false ;
    private String industry;
    private String identity;
    private int careertype;
    private String tag;
    private String slashIdentity;

    public ActivityUserInfoEditorModel(ActivityUserinfoEditorBinding activityUserinfoEditorBinding, long myUid, UserinfoEditorActivity userinfoEditorActivity) {
        this.activityUserinfoEditorBinding = activityUserinfoEditorBinding;
        this.myUid = myUid;
        this.userinfoEditorActivity = userinfoEditorActivity;
        initData();
        listener();
    }

    private void initData() {
        MyManager.getMyUserinfo(new OnGetMyUserinfo());
        MyManager.getMySelfPersonInfo(new OnGetSelfPersonInfo());
    }

    private void initView() {
        //头像的路径
        if(avatar!=null){
            BitmapKit.bindImage(activityUserinfoEditorBinding.ivHead, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }
        //姓名
        if(name!=null){
            activityUserinfoEditorBinding.etUsername.setText(name);
        }
        //手机号码
        if(phone!=null){
            activityUserinfoEditorBinding.tvUserphone.setText(phone);
        }
        //所在地
        if(province.equals(city) && city!=null && province!=null){
            activityUserinfoEditorBinding.tvLocation.setText(province);
        }else {
            activityUserinfoEditorBinding.tvLocation.setText(province +""+city);
        }
        //斜杠身份
        String replace = identity.replace(",", "/");
        slashIdentity = replace;
        activityUserinfoEditorBinding.tvIdentity.setText(replace);
        //职业类型
        switch (careertype){
            case 1://固定职业
                activityUserinfoEditorBinding.rbOfficeWorker.setChecked(true);
                activityUserinfoEditorBinding.rbSelfEmployed.setChecked(false);
                setCompanyAndPosition(false);
                break;
            case 2://自由职业
                activityUserinfoEditorBinding.rbOfficeWorker.setChecked(false);
                activityUserinfoEditorBinding.rbSelfEmployed.setChecked(true);
                setCompanyAndPosition(true);
                break;
        }
        //公司
        activityUserinfoEditorBinding.tvCompany.setText(company);
        //职位
        activityUserinfoEditorBinding.tvProfession.setText(position);

        //行业方向
        activityUserinfoEditorBinding.tvDirection.setText(industry +"|"+direction);
        //技能标签
        skillLabels = tag.split(" ");
        List<String> lists = Arrays.asList(skillLabels);
        skillLabelList = new ArrayList<>(lists);
        int length = skillLabels.length;

        if(tag!=""&&tag!=null){
            activityUserinfoEditorBinding.llSkilllabelContainer.removeAllViews();
            String[] split = tag.split(",");
            for (String textTag : split) {
                TextView skillTag = createSkillTag(textTag);
                activityUserinfoEditorBinding.llSkilllabelContainer.addView(skillTag);
            }
        }
    }

    //生成标签
    public TextView createSkillTag(String textTag) {
       TextView  textViewTag = new TextView(CommonUtils.getContext());
        textViewTag.setText(textTag);
        textViewTag.setTextColor(Color.parseColor("#31C5E4"));
        textViewTag.setTextSize(CommonUtils.dip2px(4));
        textViewTag.setPadding(CommonUtils.dip2px(8),CommonUtils.dip2px(6),CommonUtils.dip2px(8),CommonUtils.dip2px(6));
        textViewTag.setBackgroundColor(Color.parseColor("#d6f3fa"));
        return textViewTag;
    }

    //点击头像
    public void clickAvatar(View view) {
        //去相册里面调用图片
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image*//*");//相片类型
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

    //下载图片信息
    public void uploadPhoto(String filename){
        UserInfoEngine.uploadFile(new onUploadFile(),filename);
    }

    private String uploadFail = "上传照片失败";
    //上传图片
    public class onUploadFile implements BaseProtocol.IResultExecutor<UploadFileResultBean> {
        @Override
        public void execute(UploadFileResultBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                UploadFileResultBean.Data data = dataBean.data;
                String fileId = data.fileId;
                LogKit.d("图片路径"+fileId);
            }else {
                LogKit.d(uploadFail);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }


    //点击保存
    public void save(View view) {
        //保存图片路径
        if(avatar!=""&&avatar!=null){
            uploadPhoto(avatar);
        }

        //保存姓名，技能描述，职业类型
       name = activityUserinfoEditorBinding.etUsername.getText().toString();
        skilldescrib = activityUserinfoEditorBinding.etSkilldescribe.getText().toString();
        checked = activityUserinfoEditorBinding.rbOfficeWorker.isChecked();
        saveUserInfo(name,checked?1:2,skilldescrib);
      //保存公司和职位
       if(checked){
       paramsMap.put("company",company);
        paramsMap.put("position",position);
        SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_COMPANY_AND_POSITION,paramsMap);
      }

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
        String replace = industryAndDirection.replace("|", ",");
        if(industryAndDirection!=null && industryAndDirection!=""){
            String[] split = replace.split(",");
            industry =  split[0];
            direction = split[1];
            paramsMap.put("industry",industry);
            paramsMap.put("direction",direction);
            SetProtocol(GlobalConstants.HttpUrl.SET_SLASH_INDUSTRY,paramsMap);
        }

        //斜杠身份只能包含中文\英文\数字
        ArrayList<String> newSkillLabelList = EditorIdentityModel.newSkillLabelList;
        if(!newSkillLabelList.isEmpty()){
            UserInfoEngine.onSaveSlathYouth(new onSaveSlathYouth(),newSkillLabelList);
        }

        //技能标签
        ArrayList<String> listCheckedLabelName = SubscribeActivity.listCheckedLabelName;
        if(!listCheckedLabelName.isEmpty()){
            UserInfoEngine.onSaveListTag(new onSaveSlathYouth(),listCheckedLabelName);
        }
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
        activityUserinfoEditorBinding.rbOfficeWorker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.getId() == R.id.rb_office_worker){
                    checked  =  isChecked;
                    setCompanyAndPosition(!checked);
                }
            }
        });

        //监听公司和职位
        company = textChangeListener(activityUserinfoEditorBinding.tvCompany);
        position = textChangeListener(activityUserinfoEditorBinding.tvProfession);
        //设置所在地
        setLocation = textChangeListener(activityUserinfoEditorBinding.tvLocation);
        //行业 方向
        direction = textChangeListener(activityUserinfoEditorBinding.tvDirection);
        //监听shenf
        slashIdentity = activityUserinfoEditorBinding.tvIdentity.getText().toString();
    }

    private void setCompanyAndPosition(boolean isChecked) {
        if(isChecked){
            activityUserinfoEditorBinding.rlCompany.setVisibility(View.GONE);
            activityUserinfoEditorBinding.viewPosition.setVisibility(View.GONE);
            activityUserinfoEditorBinding.rlPosition.setVisibility(View.GONE);
            activityUserinfoEditorBinding.viewCompany.setVisibility(View.GONE);
        }else {
            activityUserinfoEditorBinding.rlCompany.setVisibility(View.VISIBLE);
            activityUserinfoEditorBinding.viewPosition.setVisibility(View.VISIBLE);
            activityUserinfoEditorBinding.rlPosition.setVisibility(View.VISIBLE);
            activityUserinfoEditorBinding.viewCompany.setVisibility(View.VISIBLE);
        }
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
                    SetBean.DataBean data = dataBean.getData();
                    int status = data.getStatus();
                    LogKit.d("status:"+status);
                }else {
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

    //保存斜杠身份
    public class onSaveSlathYouth implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
               LogKit.d("status:"+status);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }


    //获取个人信息的接口
    public class OnGetMyUserinfo implements BaseProtocol.IResultExecutor<MyFirstPageBean> {
        @Override
        public void execute(MyFirstPageBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                MyFirstPageBean.DataBean data = dataBean.getData();
                MyFirstPageBean.DataBean.MyinfoBean myinfo = data.getMyinfo();
                setUserInfoEditor(myinfo);
                initView();
            }else {
                LogKit.d("rescode="+rescode);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    //获取个人信息的接口
    public class OnGetSelfPersonInfo implements BaseProtocol.IResultExecutor<UserInfoItemBean> {
        @Override
        public void execute(UserInfoItemBean dataBean) {
            int rescode = dataBean.getRescode();
            if(rescode == 0){
                UserInfoItemBean.DataBean data = dataBean.getData();
                UserInfoItemBean.DataBean.UinfoBean uinfo = data.getUinfo();
                //技能描述
                 skilldescrib = uinfo.getDesc();
                //技能描述
            if(!skilldescrib.isEmpty()){
                activityUserinfoEditorBinding.etSkilldescribe.setText(skilldescrib);
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

    //获取数据
    private void setUserInfoEditor(MyFirstPageBean.DataBean.MyinfoBean myinfo) {
        if(avatar!=null&&avatar!=""&&!avatar.isEmpty()){
            avatar = myinfo.getAvatar();
        }
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
}