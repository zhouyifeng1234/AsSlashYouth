package com.slash.youth.ui.viewmodel;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityMyAddSkillBinding;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillMamagerOneTempletBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.domain.UploadFileResultBean;
import com.slash.youth.engine.DemandEngine;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.FinishPhoneActivity;
import com.slash.youth.ui.activity.MyAddSkillActivity;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.ui.view.SlashAddLabelsLayout;
import com.slash.youth.ui.view.SlashAddPicLayout;
import com.slash.youth.ui.view.SlashNumberPicker;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    private int quote = -1;
    private ArrayList<String> thridlistTag = new ArrayList<String>();
    public ArrayList<String> listTag = new ArrayList<String>();
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
    private String tag;
    private String pic;
    private long startime;
    private long endtime;
    private String place = "";
    private String toastText = "请把信息填写完整";
    private int skillTemplteType;
    private SkillManagerBean.DataBean.ListBean listBean;
    private int position;
    private int f1;
    private int f2;
    private String substring;
    private String[] picFileIds;
    private String picCachePath;
    private int pathSize;
    private String quoteString;

    public MyAddSkillModel(ActivityMyAddSkillBinding activityMyAddSkillBinding, MyAddSkillActivity myAddSkillActivity, long id,  int skillTemplteType,int position) {
        this.activityMyAddSkillBinding = activityMyAddSkillBinding;
        this.myAddSkillActivity = myAddSkillActivity;
        this.skillTemplteType = skillTemplteType;
        this.id = id;
        this.position = position;
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
        title = activityMyAddSkillBinding.etTitle.getText().toString();
        desc = activityMyAddSkillBinding.etSkillManageDesc.getText().toString();
        quoteString = activityMyAddSkillBinding.etMoney.getText().toString();

        if (title.length() < 5 || title.length() > 20) {
            ToastUtils.shortToast("标题必须为5-20字之间");
            return;
        }

        if (desc.length() <= 0) {
            ToastUtils.shortToast("请输入服务描述");
            return;
        } else if (desc.length() < 5) {
            ToastUtils.shortToast("服务描述必须是5-300字之间");
            return;
        } else if (desc.length() > 300) {
            ToastUtils.shortToast("服务描述不能超过300字");
            return;
        }

        if(!TextUtils.isEmpty(title)){
            if(!TextUtils.isEmpty(desc)){
                if(!TextUtils.isEmpty(quoteString)){
                    if(quoteunit>=0&&quoteunit<=8){
                        if(sallAddedSkilllabels.getAddedTags().size()!=0){
                            getSumbitData();
                        }else {
                           ToastUtils.shortCenterToast("请选择技能标签");
                        }
                    }else {
                    ToastUtils.shortCenterToast("请选择价格单位");
                    }

                }else {
                    ToastUtils.shortCenterToast("请填写报价");
                }

            }else {
                ToastUtils.shortCenterToast("请填写描述信息");
            }
        }else {
            ToastUtils.shortCenterToast("请填写服务标题");
        }

       /* LogKit.d(" title = "+title+" listTag = "+listTag+" startime = "+startime+" endtime = "+endtime+" anonymity = "+anonymity+ " desc = "+desc+" timetype ="+timetype+" listPic = "+listPic+" instalment = "+instalment+" bp = "+bp
                +" pattern = "+pattern+" place ="+place+" lng = "+lng+" lat = "+lat+" quote = "+quote+" quoteunit = "+ quoteunit);
*/

    }

    private void sumbitSucceful() {
        listBean = new SkillManagerBean.DataBean.ListBean();
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
    }

    //获取要提交的内容
    public void getSumbitData() {
        if (!TextUtils.isEmpty(quoteString)) {
            //quote = Double.parseDouble(quoteString);
            quote = Integer.parseInt(quoteString);
        }
        quoteunit = value + 1;
        anonymity = 1;
        timetype = 0;//时间类型
        instalment = 0;
        bp = 1;//取值只能1或者2 1平台 2协商
        pattern = 1;//取值只能1或者0 （1线下 0线上）
        //获取标签
        listTag = sallAddedSkilllabels.getAddedTags();
        //获取图片

        listPic.clear();
        ArrayList<String> addedPicTempPath = addPic.getAddedPicTempPath();
        pathSize = addedPicTempPath.size();
        if(pathSize ==0){
            switch (skillTemplteType){
                case Constants.ADD_ONE_SKILL_MANAGER://添加一个技能标签模板
                    MyManager.onAddSkillTemplet(new onAddSkillTemplet(), title, listTag, startime, endtime, anonymity, desc,  listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
                    break;
                case Constants.UPDATE_SKILL_MANAGER_ONE://修改一个技能标签模板
                    MyManager.onUpdateSkillTemplet(new onAddSkillTemplet(),id,title, listTag, startime, endtime, anonymity, desc,  listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
                    break;
            }
        }

        final int[] uploadCount = {0};
        for (final String filePath : addedPicTempPath) {
            DemandEngine.uploadFile(new BaseProtocol.IResultExecutor<UploadFileResultBean>() {
                @Override
                public void execute(UploadFileResultBean dataBean) {
                    LogKit.v(filePath + ":上传成功");
                    uploadCount[0]++;
                    LogKit.v("uploadCount:" + uploadCount[0]);
                    LogKit.v(dataBean + "");
                    listPic.add(dataBean.data.fileId);

                    switch (skillTemplteType){
                        case Constants.ADD_ONE_SKILL_MANAGER://添加一个技能标签模板
                            MyManager.onAddSkillTemplet(new onAddSkillTemplet(), title, listTag, startime, endtime, anonymity, desc,  listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
                            break;
                        case Constants.UPDATE_SKILL_MANAGER_ONE://修改一个技能标签模板
                            MyManager.onUpdateSkillTemplet(new onAddSkillTemplet(),id,title, listTag, startime, endtime, anonymity, desc,  listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
                            break;
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.v(filePath + ":上传失败");
                    uploadCount[0]++;

                }
            }, filePath);
        }
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
                        ToastUtils.shortToast("已提交");
                        sumbitSucceful();
                        Intent intent = new Intent();
                        switch (skillTemplteType){
                            case Constants.ADD_ONE_SKILL_MANAGER://添加一个技能标签模板
                                intent.putExtra("sumbitNewTemplet", listBean);
                                myAddSkillActivity.setResult(Constants.SUMBIT_ONE_SKILL_MANAGER, intent);
                                break;
                            case Constants.UPDATE_SKILL_MANAGER_ONE://修改一个技能标签模板
                                intent.putExtra("sumbitNewTemplet", listBean);
                                intent.putExtra("position",position);
                                myAddSkillActivity.setResult(Constants.UPDATE_SKILL_MANAGER_ONE, intent);
                                break;
                        }
                        myAddSkillActivity.finish();
                        break;
                    case 0:
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
        listTag.clear();
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
        for (String s : tags) {
            listTag.add(s);
        }
        for (String tag : tags) {
            String[] split = tag.split("-");
            String F1 = split[0];
            f1= Integer.parseInt(F1);
            String F2 = split[1];
            f2= Integer.parseInt(F2);
            substring = split[2];
            thridlistTag.add(substring);
        }
        activityMyAddSkillBinding.sallAddedSkilllabels.reloadSkillLabels(thridlistTag, listTag);

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
        desc = service.getDesc();
        activityMyAddSkillBinding.etSkillManageDesc.setText(desc);
        if(quoteunit>=0&&quoteunit<=7){
            activityMyAddSkillBinding.tvUnit.setText(FirstPagerManager.QUOTEUNITS[quoteunit-1]);
        }

        //回显图片
        String[] picFileIds = service.getPic().split(",");
        final String picCachePath = myAddSkillActivity.getCacheDir().getAbsoluteFile() + "/addpicache/";
        File cacheDir = new File(picCachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        for (String fileId : picFileIds) {
            DemandEngine.downloadFile(new BaseProtocol.IResultExecutor<byte[]>() {
                @Override
                public void execute(byte[] dataBean) {

                    Bitmap bitmap = BitmapFactory.decodeByteArray(dataBean, 0, dataBean.length);
                    if (bitmap != null) {
                        File tempFile = new File(picCachePath + System.currentTimeMillis() + ".jpeg");
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(tempFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                            addPic.reloadPic(tempFile.getAbsolutePath());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            IOUtils.close(fos);
                        }
                    }
                }

                @Override
                public void executeResultError(String result) {

                }
            }, fileId);
        }
    }
}
