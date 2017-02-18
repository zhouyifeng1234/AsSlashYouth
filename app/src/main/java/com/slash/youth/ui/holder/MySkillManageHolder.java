package com.slash.youth.ui.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.ServiceDetailBean;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.viewmodel.ItemMySkillManageModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.SpUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class MySkillManageHolder extends BaseHolder<SkillManagerBean.DataBean.ListBean> {

    public ItemMySkillManageBinding itemMySkillManageBinding;
    private MySkillManageActivity mySkillManageActivity;
    private final String myActivityTitle;
    private ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList;
    private String[] split;
    private String avater;
    private String name;

    public MySkillManageHolder(MySkillManageActivity mySkillManageActivity, ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList, String avater, String name) {
        this.mySkillManageActivity = mySkillManageActivity;
        this.skillManageList = skillManageList;
        this.avater = avater;
        this.name = name;
        myActivityTitle = SpUtils.getString("myActivityTitle", "");
    }

    @Override
    public View initView() {
        itemMySkillManageBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_skill_manage, null, false);
        ItemMySkillManageModel itemMySkillManageModel = new ItemMySkillManageModel(itemMySkillManageBinding, skillManageList, mySkillManageActivity);
        itemMySkillManageBinding.setItemMySkillManageModel(itemMySkillManageModel);
        return itemMySkillManageBinding.getRoot();
    }

    @Override
    public void refreshView(SkillManagerBean.DataBean.ListBean data) {
        setView(myActivityTitle);

        if (!TextUtils.isEmpty(avater)) {
            BitmapKit.bindImage(itemMySkillManageBinding.ivPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avater);
        }

        String title = data.getTitle();
        itemMySkillManageBinding.tvSkillManagerTitle.setText(title);

        double quote = data.getQuote();
        int quoteunit = data.getQuoteunit();
        itemMySkillManageBinding.tvSkillManagerQuote.setText(MyManager.QOUNT + quote + "元" + "/" + FirstPagerManager.QUOTEUNITS[quoteunit - 1]);

        int anonymity = data.getAnonymity();//1实名0匿名
        String desc = data.getDesc();//长度小于4096字节 服务端默认取值为""
        String tag = data.getTag();
        int count = data.getCount();
        long id = data.getId();
        int bp = data.getBp();
        long cts = data.getCts();
        double lat = data.getLat();
        double lng = data.getLng();
        int loop = data.getLoop();
        int pattern = data.getPattern();
        String place = data.getPlace();
        String remark = data.getRemark();
        long uid = data.getUid();
        long uts = data.getUts();

        itemMySkillManageBinding.tvName.setText(name);
    }

    //设置界面
    private void setView(String myActivityTitle) {
        switch (myActivityTitle) {
            case MyManager.SKILL_MANAGER:
                itemMySkillManageBinding.tvMyBtn.setText(MyManager.PUBLISH);
                break;
        }

    }

    @Override
    public void setData(final SkillManagerBean.DataBean.ListBean data, final int position) {
        super.setData(data, position);
        //点击删除
        itemMySkillManageBinding.ivDeleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (myActivityTitle) {
                    case MyManager.SKILL_MANAGER:
                        //埋点
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SKILL_AGREEMENT_DELETE);
                        break;
                    case MyManager.PUBLISH:
                        //埋点
                        MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_RELEASE_TASK_DELETE);
                        break;
                }
                listener.OnDeleteClick(position);
            }
        });

        //发布  技能列表发布按钮
        itemMySkillManageBinding.tvMyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_SKILL_AGREEMENT_ISSUE);

                SkillManagerBean.DataBean.ListBean listBean = skillManageList.get(position);
                ServiceDetailBean serviceDetailBean = new ServiceDetailBean(1);
                ServiceDetailBean.Service service = serviceDetailBean.data.service;

                setBean(listBean, service);
                Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
                intentPublishServiceBaseInfoActivity.putExtra("serviceDetailBean", serviceDetailBean);
                intentPublishServiceBaseInfoActivity.putExtra("isFromSkillManager", true);
                mySkillManageActivity.startActivityForResult(intentPublishServiceBaseInfoActivity, Constants.SKILL_MANAGER_PUBLISH_SERVICE);
            }
        });
    }

    public interface OnDeleteClickListener {
        void OnDeleteClick(int position);
    }

    private OnDeleteClickListener listener;

    public void setOnDeleteCklickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    private void setBean(SkillManagerBean.DataBean.ListBean listBean, ServiceDetailBean.Service service) {
        //技能管理里面，这有这6项是需要填写的，其余的都使用默认值
        service.title = listBean.getTitle();
        service.desc = listBean.getDesc();
        service.quote = listBean.getQuote();
        service.quoteunit = listBean.getQuoteunit();
        service.pic = listBean.getPic();
        service.tag = listBean.getTag();
        //下面的都使用默认值
        service.anonymity = listBean.getAnonymity();
//        service.bp = listBean.getBp();
        service.bp = 0;//默认的纠纷处理方式是平台处理
        service.cts = listBean.getCts();
//        service.endtime = listBean.getEndtime();
        service.endtime = 0;
        service.id = listBean.getId();
//        service.instalment = listBean.getInstalment();
        service.instalment = 0;//分期默认是关闭的
        service.iscomment = listBean.getIscomment();
        service.isonline = listBean.getIsonline();
//        service.lat = listBean.getLat();
//        service.lng = listBean.getLng();
        service.lat = 0;
        service.lng = 0;
        service.loop = listBean.getLoop();
//        service.pattern = listBean.getPattern();
        service.pattern = 0;//默认服务方式是线上
//        service.place = listBean.getPlace();
        service.place = "";
//        service.starttime = listBean.getStarttime();
        service.starttime = 0;
        service.status = listBean.getStatus();
        service.uid = listBean.getUid();
        service.timetype = -1;
    }
}
