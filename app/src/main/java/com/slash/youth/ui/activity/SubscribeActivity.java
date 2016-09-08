package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySubscribeBinding;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.ui.view.SlashSkilllabelFlowLayout;
import com.slash.youth.ui.viewmodel.ActivitySubscribeModel;
import com.slash.youth.ui.viewmodel.ItemSubscribeSecondSkilllabelModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/8.
 */
public class SubscribeActivity extends Activity {

    private ActivitySubscribeBinding mActivitySubscribeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySubscribeBinding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe);
        ActivitySubscribeModel activitySubscribeModel = new ActivitySubscribeModel(mActivitySubscribeBinding);
        mActivitySubscribeBinding.setActivitySubscribeModel(activitySubscribeModel);

        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setVerticalScrollBarEnabled(false);
//        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setDivider(new ColorDrawable(0xedf1f2));
        initData();
        initListener();
    }


    private void initData() {
        //假数据，实际应该从服务端借口获取
        ArrayList<SkillLabelBean> listSkilllabel = new ArrayList<SkillLabelBean>();
        listSkilllabel.add(new SkillLabelBean("设计"));
        listSkilllabel.add(new SkillLabelBean("研发"));
        listSkilllabel.add(new SkillLabelBean("销售"));
        listSkilllabel.add(new SkillLabelBean("运营"));
        listSkilllabel.add(new SkillLabelBean("产品"));
        listSkilllabel.add(new SkillLabelBean("市场上午"));
        listSkilllabel.add(new SkillLabelBean("高管"));
        listSkilllabel.add(new SkillLabelBean("运营"));
        listSkilllabel.add(new SkillLabelBean("销售"));
        listSkilllabel.add(new SkillLabelBean("设计"));
        listSkilllabel.add(new SkillLabelBean("研发"));
        listSkilllabel.add(new SkillLabelBean("销售"));
        listSkilllabel.add(new SkillLabelBean("销售"));
        listSkilllabel.add(new SkillLabelBean("运营"));
        listSkilllabel.add(new SkillLabelBean("产品"));
        listSkilllabel.add(new SkillLabelBean("市场上午"));
        listSkilllabel.add(new SkillLabelBean("高管"));
        listSkilllabel.add(new SkillLabelBean("运营"));
        listSkilllabel.add(new SkillLabelBean("销售"));
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setAdapter(new SubscribeSecondSkilllabelAdapter(listSkilllabel));
        SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.post(new Runnable() {
            @Override
            public void run() {
                View lvActivitySubscribeSecondSkilllableListFirstChild = mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.getChildAt(0);
                LogKit.v(lvActivitySubscribeSecondSkilllableListFirstChild.getTag() + "");
                SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();
                lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;
            }
        });
        setThirdSkilllabelData();
    }

    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;

    private void initListener() {
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (lastClickItemModel != null) {
                    lastClickItemModel.setSecondSkilllabelColor(0xff333333);
                }
                SubscribeSecondSkilllabelHolder subscribeSecondSkilllabelHolder = (SubscribeSecondSkilllabelHolder) view.getTag();
                ItemSubscribeSecondSkilllabelModel itemSubscribeSecondSkilllabelModel = subscribeSecondSkilllabelHolder.mItemSubscribeSecondSkilllabelModel;
                itemSubscribeSecondSkilllabelModel.setSecondSkilllabelColor(0xff31c5e4);
                subscribeSecondSkilllabelHolder.clickItemPosition = position;
                lastClickItemModel = itemSubscribeSecondSkilllabelModel;
                setThirdSkilllabelData();
            }
        });
    }


    /**
     * 根据选择的二级标签，显示对应的三级标签
     */
    public void setThirdSkilllabelData() {

        //TODO 目前只是测试数据，到时候该方法可能需要传入二级标签的ID等信息，以方便查询对应的三级标签
//        TextView tv = new TextView(CommonUtils.getContext());
//        tv.setText("APP开发");
//        mActivitySubscribeBinding.tsgActivitySubscribeThirdSkilllabel.addView(tv);
        ScrollView.LayoutParams params = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
        SlashSkilllabelFlowLayout flowThirdSkilllabel = new SlashSkilllabelFlowLayout(CommonUtils.getContext());
        flowThirdSkilllabel.setLayoutParams(params);
        flowThirdSkilllabel.setBackgroundColor(0xffff0000);
        for (int i = 0; i <= 1; i++) {
            TextView tv = new TextView(CommonUtils.getContext());
            tv.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(16), CommonUtils.dip2px(11));
            tv.setBackgroundColor(0xffffffff);
            tv.setText("APP开发");
            tv.setTextColor(0xff333333);
            tv.setTextSize(14);
            flowThirdSkilllabel.addView(tv);
        }
//        mActivitySubscribeBinding.svActivitySubscribeThirdSkilllabel.setVerticalScrollBarEnabled(false);
        mActivitySubscribeBinding.svActivitySubscribeThirdSkilllabel.addView(flowThirdSkilllabel);
    }
}
