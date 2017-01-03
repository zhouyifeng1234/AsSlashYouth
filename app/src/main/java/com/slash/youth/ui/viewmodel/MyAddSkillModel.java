package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyAddSkillActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashNumberPicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by zss on 2016/11/3.
 */
public class MyAddSkillModel extends BaseObservable {
    public SlashAddPicLayout addPic;
    private int totalNum = 300;
    private int num;
    private ActivityMyAddSkillBinding activityMyAddSkillBinding;
    private MyAddSkillActivity myAddSkillActivity;
    private SlashNumberPicker npUnit;
    private String[] unitArr = {"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};
    private int value;
    public SlashAddLabelsLayout sallAddedSkilllabels;
    private long id;
    private String title;
    private String desc;
    private double quote = 0;
    private ArrayList<String> listTag = new ArrayList<String>();
    private int anonymity = 1;
    private int bp = 1;
    private int count;
    private int cts;
    private int instalment = 0;
    private double lat = 0.00000000D;
    private int iscomment;
    private double lng = 0.00000000D;
    private int loop;
    private int pattern = 0;
    private int quoteunit;
    private String remark;
    private int starttime;
    private int timetype = 0;
    private int uid;
    private int uts;
    private ArrayList<String> listPic = new ArrayList<String>();
    ;
    private String tag;
    private String pic;
    private long startime;
    private long endtime;
    private String place = "";
    private boolean isSucceful;
    private String toastText = "请把信息填写完整";

    public MyAddSkillModel(ActivityMyAddSkillBinding activityMyAddSkillBinding, MyAddSkillActivity myAddSkillActivity, long id) {
        this.activityMyAddSkillBinding = activityMyAddSkillBinding;
        this.myAddSkillActivity = myAddSkillActivity;
        this.id = id;
        initView();
        initData();
        listener();
    }

    private void initView() {
        //初始化单位选择
        addPic = activityMyAddSkillBinding.addPic;
        addPic.setActivity(myAddSkillActivity);
        addPic.initPic();
        npUnit = activityMyAddSkillBinding.npUnit;

        //初始化添加技能标签
        sallAddedSkilllabels = activityMyAddSkillBinding.sallAddedSkilllabels;
        sallAddedSkilllabels.setActivity(myAddSkillActivity);
        sallAddedSkilllabels.initSkillLabels();

        //设置默认单位
        npUnit.setDisplayedValues(unitArr);
        npUnit.setMinValue(0);
        npUnit.setMaxValue(unitArr.length - 1);
        npUnit.setValue(1);
    }

    //加载数据
    private void initData() {
        if (id != -1) {
            MyManager.onGetOneSkillTemplet(new onGetOneSkillTemplet(), id);
        }
    }

    private int rlChooseMainLabelVisible = View.INVISIBLE;

    @Bindable
    public int getRlChooseMainLabelVisible() {
        return rlChooseMainLabelVisible;
    }

    public void setRlChooseMainLabelVisible(int rlChooseMainLabelVisible) {
        this.rlChooseMainLabelVisible = rlChooseMainLabelVisible;
        notifyPropertyChanged(BR.rlChooseMainLabelVisible);
    }

    //点击选择单位
    public void chooseUnit(View view) {
        setRlChooseMainLabelVisible(View.VISIBLE);
    }

    //确定单位
    public void sure(View view) {
        setRlChooseMainLabelVisible(View.INVISIBLE);
        value = npUnit.getValue();
        String unit = unitArr[value];
        if (!unit.isEmpty()) {
            activityMyAddSkillBinding.tvUnit.setText(unit);
        }
    }

    //监听事件
    private void listener() {
        activityMyAddSkillBinding.etSkillManageDesc.addTextChangedListener(new TextWatcher() {
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
                String text = s.toString();
                num = s.length();
                selectionEnd = activityMyAddSkillBinding.etSkillManageDesc.getSelectionEnd();
                activityMyAddSkillBinding.tvDescNum.setText(num + "/" + totalNum);
                // selectionStart=activityReportTaBinding.etReportOther.getSelectionStart();
                if (wordNum.length() > totalNum) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    activityMyAddSkillBinding.etSkillManageDesc.setText(s);
                    activityMyAddSkillBinding.etSkillManageDesc.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    //跳转到标签页面。关联标签
    public void jumpSkillLabel(View view) {
        Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
        ArrayList<String> addedTagsName = sallAddedSkilllabels.getAddedTagsName();
        ArrayList<String> addedTags = sallAddedSkilllabels.getAddedTags();
        intentSubscribeActivity.putStringArrayListExtra("addedTagsName", addedTagsName);
        intentSubscribeActivity.putStringArrayListExtra("addedTags", addedTags);
        intentSubscribeActivity.putExtra("addSkillTemplte", 0);
        myAddSkillActivity.startActivityForResult(intentSubscribeActivity, Constants.SKILL_MANAGER_ADD_LABEL);
    }

    //提交
    public void sumbit(View view) {
        getSumbitData();
       /* LogKit.d(" title = "+title+" listTag = "+listTag+" startime = "+startime+" endtime = "+endtime+" anonymity = "+anonymity+ " desc = "+desc+" timetype ="+timetype+" listPic = "+listPic+" instalment = "+instalment+" bp = "+bp
                +" pattern = "+pattern+" place ="+place+" lng = "+lng+" lat = "+lat+" quote = "+quote+" quoteunit = "+ quoteunit);
 */
        MyManager.onAddSkillTemplet(new onAddSkillTemplet(), title, listTag, startime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);

        if (isSucceful) {
            SkillManagerBean.DataBean.ListBean listBean = new SkillManagerBean.DataBean.ListBean();
            listBean.setTitle(title);
            listBean.setTag(tag);
            listBean.setStarttime(starttime);
            listBean.setEndtime(endtime);
            listBean.setAnonymity(anonymity);//服务端默认取值为1
            listBean.setDesc(desc);
            listBean.setTimetype(timetype);//默认时间的类型
            listBean.setPic(pic);
            listBean.setInstalment(instalment);
            listBean.setBp(bp);
            listBean.setPattern(pattern);
            listBean.setPlace(place);
            listBean.setLng(lng);
            listBean.setLat(lat);
            listBean.setQuote(quote);
            listBean.setQuoteunit(quoteunit);
            listBean.setCount(count);
            listBean.setCts(cts);
            listBean.setId(id);
            Intent intent = new Intent();
            intent.putExtra("sumbitNewTemplet", listBean);
            myAddSkillActivity.setResult(Constants.SUMBIT_ONE_SKILL_MANAGER, intent);
            myAddSkillActivity.finish();
        }
    }

    //获取要提交的内容
    public void getSumbitData() {
        title = activityMyAddSkillBinding.etTitle.getText().toString();
        desc = activityMyAddSkillBinding.etSkillManageDesc.getText().toString();
        String quoteString = activityMyAddSkillBinding.etMoney.getText().toString();
        if (quoteString != "" && !quoteString.isEmpty() && quoteString != " ") {
            quote = Double.parseDouble(quoteString);
        }
        quoteunit = value + 1;
        anonymity = 1;
        timetype = 0;//时间类型
        instalment = 1;
        bp = 1;//取值只能1或者2 1平台 2协商
        pattern = 1;//取值只能1或者0 （1线下 0线上）
        //TODO传递图片
        //获取图片
        //获取标签
        listTag = sallAddedSkilllabels.getAddedTagsName();
    }

    //获取一个技能模板
    public class onGetOneSkillTemplet implements BaseProtocol.IResultExecutor<SkillMamagerOneTempletBean> {
        @Override
        public void execute(SkillMamagerOneTempletBean dataBean) {
            int rescode = dataBean.getRescode();
            if (rescode == 0) {
                SkillMamagerOneTempletBean.DataBean data = dataBean.getData();
                SkillMamagerOneTempletBean.DataBean.ServiceBean service = data.getService();
                initOneTemplet(service);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    //添加一个技能模板
    public class onAddSkillTemplet implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if (rescode == 0) {
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                switch (status) {
                    case 1:
                        isSucceful = true;
                        break;
                    case 0:
                        isSucceful = false;
                        ToastUtils.shortToast(toastText);
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:" + result);
        }
    }

    private void initOneTemplet(SkillMamagerOneTempletBean.DataBean.ServiceBean service) {
        String serviceTitle = service.getTitle();
        activityMyAddSkillBinding.etTitle.setText(serviceTitle);
        String serviceDesc = service.getDesc();
        activityMyAddSkillBinding.etSkillManageDesc.setText(serviceDesc);
        int serviceQuote = service.getQuote();
        activityMyAddSkillBinding.etMoney.setText(String.valueOf(serviceQuote));
        int serviceQuoteunit = service.getQuoteunit();
        int serviceValue = serviceQuoteunit - 1;
        npUnit.setValue(serviceValue);
        tag = service.getTag();
        String[] tags = tag.split(",");
        listTag.clear();
        for (String tag : tags) {
            int index = tag.lastIndexOf("-");
            String substring = tag.substring(index + 1);
            listTag.add(substring);
        }
        activityMyAddSkillBinding.sallAddedSkilllabels.reloadSkillLabels(listTag, listTag);

        pic = service.getPic();
        if (pic != null) {
            String[] pics = pic.split(",");
            Collections.addAll(listPic, pics);
        }
        anonymity = service.getAnonymity();
        bp = service.getBp();
        count = service.getCount();
        cts = service.getCts();
        endtime = service.getEndtime();
        instalment = service.getInstalment();
        lat = service.getLat();
        iscomment = service.getIscomment();
        lng = service.getLng();
        loop = service.getLoop();
        pattern = service.getPattern();
        quote = service.getQuote();
        quoteunit = service.getQuoteunit();
        remark = service.getRemark();
        starttime = service.getStarttime();
        timetype = service.getTimetype();
        uid = service.getUid();
        uts = service.getUts();
        place = service.getPlace();
    }

}
