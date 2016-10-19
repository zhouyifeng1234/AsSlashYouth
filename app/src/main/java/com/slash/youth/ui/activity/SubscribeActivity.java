package com.slash.youth.ui.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySubscribeBinding;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
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
    private LinearLayout mLlCheckedLabels;
    public String checkedFirstLabel = "未选择";
    public String checkedSecondLabel = "未选择";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySubscribeBinding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe);
        ActivitySubscribeModel activitySubscribeModel = new ActivitySubscribeModel(mActivitySubscribeBinding, this);
        mActivitySubscribeBinding.setActivitySubscribeModel(activitySubscribeModel);

        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setVerticalScrollBarEnabled(false);
//        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setDivider(new ColorDrawable(0xedf1f2));
        mActivitySubscribeBinding.svActivitySubscribeThirdSkilllabel.setVerticalScrollBarEnabled(false);
        mLlCheckedLabels = mActivitySubscribeBinding.llActivitySubscribeCheckedLabels;
        mLlCheckedLabels.removeAllViews();
        initData();
        initListener();
    }


    private void initData() {
        final ArrayList<String> addedSkillLabels = getIntent().getStringArrayListExtra("addedSkillLabels");
        mLlCheckedLabels.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < addedSkillLabels.size(); i++) {
                    String labelTag = "label_" + (-i);
                    String labelName = addedSkillLabels.get(i);
                    addCheckedLabels(labelTag, labelName);
                }
            }
        });

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
                checkedSecondLabel = itemSubscribeSecondSkilllabelModel.getSecondSkilllabelName();
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


//        ScrollView.LayoutParams params = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT, ScrollView.LayoutParams.WRAP_CONTENT);
//        SlashSkilllabelFlowLayout flowThirdSkilllabel = new SlashSkilllabelFlowLayout(CommonUtils.getContext());
//        flowThirdSkilllabel.setLayoutParams(params);
//        flowThirdSkilllabel.setBackgroundColor(0xffff0000);
//        for (int i = 0; i <= 1; i++) {
//            TextView tv = new TextView(CommonUtils.getContext());
//            tv.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(16), CommonUtils.dip2px(11));
//            tv.setBackgroundColor(0xffffffff);
//            tv.setText("APP开发");
//            tv.setTextColor(0xff333333);
//            tv.setTextSize(14);
//            flowThirdSkilllabel.addView(tv);
//        }
//        mActivitySubscribeBinding.svActivitySubscribeThirdSkilllabel.setVerticalScrollBarEnabled(false);
//        mActivitySubscribeBinding.svActivitySubscribeThirdSkilllabel.addView(flowThirdSkilllabel);

        final ArrayList<String> listThirdSkilllabelName = new ArrayList<String>();
        listThirdSkilllabelName.add("设计");
        listThirdSkilllabelName.add("A设计");
        listThirdSkilllabelName.add("A");
        listThirdSkilllabelName.add("APP开发设计");
        listThirdSkilllabelName.add("APP开发");
        listThirdSkilllabelName.add(".NET");
        listThirdSkilllabelName.add("java");
        listThirdSkilllabelName.add("android");
        listThirdSkilllabelName.add("hadoop");
        listThirdSkilllabelName.add("设计");
        listThirdSkilllabelName.add("APP设计");
        listThirdSkilllabelName.add("APP开发设计");
        listThirdSkilllabelName.add("APP开发");
        listThirdSkilllabelName.add("设计");
        listThirdSkilllabelName.add(".NET");
        listThirdSkilllabelName.add("java");
        listThirdSkilllabelName.add("android");
        listThirdSkilllabelName.add("hadoop");
        listThirdSkilllabelName.add("APP设计");
        listThirdSkilllabelName.add("APP开发设计");
        listThirdSkilllabelName.add("APP开发");
        listThirdSkilllabelName.add("设计");
        listThirdSkilllabelName.add("APP设计");
        listThirdSkilllabelName.add("APP开发设计");
        listThirdSkilllabelName.add("APP开发");

        mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.removeAllViews();
        mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.post(new Runnable() {
            LinearLayout llSkilllabelLine;
            int lineCount = 0;
//            int lineLabelCount = 0;

            @Override
            public void run() {
                int scrollViewWidth = mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.getMeasuredWidth();
//                ToastUtils.shortToast("getMeasuredWidth:" + scrollViewWidth + "   getMeasuredHeight:" + mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.getMeasuredHeight());
                scrollViewWidth = scrollViewWidth - CommonUtils.dip2px(30);

                int labelRightMargin = CommonUtils.dip2px(10);
                int skillLabelLineWidth = 0;


                for (int i = 0; i < listThirdSkilllabelName.size(); i++) {
                    String thirdSkilllabelName = listThirdSkilllabelName.get(i);

                    //创建标签TextView
                    LinearLayout.LayoutParams llParamsForSkillLabel = new LinearLayout.LayoutParams(-2, -2);
                    llParamsForSkillLabel.rightMargin = CommonUtils.dip2px(labelRightMargin);
                    TextView tvThirdSkilllabelName = new TextView(CommonUtils.getContext());
                    tvThirdSkilllabelName.setTag("label_" + i);
                    tvThirdSkilllabelName.setOnClickListener(new CheckThirdLabelListener());
                    tvThirdSkilllabelName.setLayoutParams(llParamsForSkillLabel);
                    tvThirdSkilllabelName.setMaxLines(1);
                    tvThirdSkilllabelName.setGravity(Gravity.CENTER);
                    tvThirdSkilllabelName.setTextColor(0xff333333);
                    tvThirdSkilllabelName.setTextSize(14);
                    tvThirdSkilllabelName.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(16), CommonUtils.dip2px(11));
//                    tvThirdSkilllabelName.setBackgroundColor(0xffffffff);
                    tvThirdSkilllabelName.setBackgroundResource(R.drawable.shape_rounded_rectangle_third_skilllabel);
                    tvThirdSkilllabelName.setText(thirdSkilllabelName);
                    //测量标签TextView的宽度并判断是否换行
                    tvThirdSkilllabelName.measure(0, 0);
                    int tvThirdSkilllabelWidth = tvThirdSkilllabelName.getMeasuredWidth() + labelRightMargin;
                    int newSkillLabelLineWidth = skillLabelLineWidth + tvThirdSkilllabelWidth;
//                    lineLabelCount++;
                    if (skillLabelLineWidth != 0) {
                        if (newSkillLabelLineWidth >= scrollViewWidth) {
                            mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.addView(llSkilllabelLine);
                            createLabelLine();
                            llSkilllabelLine.addView(tvThirdSkilllabelName);
                            skillLabelLineWidth = tvThirdSkilllabelWidth;
                        } else {
                            llSkilllabelLine.addView(tvThirdSkilllabelName);
                            skillLabelLineWidth = newSkillLabelLineWidth;
                        }
                    } else {
                        //防范一个标签就超过总宽度的情况
                        createLabelLine();
                        llSkilllabelLine.addView(tvThirdSkilllabelName);

                        if (newSkillLabelLineWidth >= scrollViewWidth) {
                            mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.addView(llSkilllabelLine);
                            skillLabelLineWidth = 0;
                        } else {
                            skillLabelLineWidth = newSkillLabelLineWidth;
                        }
                    }


                    //将标签TextView添加到Line里面
//                    llSkilllabelLine.addView(tvThirdSkilllabelName);


                }
                mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.addView(llSkilllabelLine);
            }

            public void createLabelLine() {
                //创建Line
                LinearLayout.LayoutParams llParamsForLine = new LinearLayout.LayoutParams(-1, -2);
                if (lineCount % 2 == 0) {
                    llParamsForLine.topMargin = CommonUtils.dip2px(20);
                } else {
                    llParamsForLine.topMargin = CommonUtils.dip2px(10);
                }
                llSkilllabelLine = new LinearLayout(CommonUtils.getContext());
                llSkilllabelLine.setLayoutParams(llParamsForLine);
                llSkilllabelLine.setOrientation(LinearLayout.HORIZONTAL);
                lineCount++;
//                lineLabelCount = 0;
            }
        });
    }

    public class CheckThirdLabelListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            String labelTag = (String) v.getTag();
//            ToastUtils.shortToast(tvLabel);
            String labelName = ((TextView) v).getText().toString();
            addCheckedLabels(labelTag, labelName);
        }
    }


    LinearLayout llCheckedLabelsLine = null;
    int checkedLabelLeftMargin = CommonUtils.dip2px(11);
    int totalCheckedLabelsWidth = 0;
    int currentLabelsLineWidth = 0;
    public ArrayList<String> listCheckedLabelTag = new ArrayList<String>();
    public ArrayList<String> listCheckedLabelName = new ArrayList<String>();


    private void addCheckedLabels(String labelTag, String labelName) {
        if (listCheckedLabelTag.contains(labelTag)) {
            return;
        }
        listCheckedLabelTag.add(labelTag);
        listCheckedLabelName.add(labelName);
        totalCheckedLabelsWidth = mLlCheckedLabels.getMeasuredWidth() - 2 * checkedLabelLeftMargin;
        if (llCheckedLabelsLine == null) {
            createCheckedLabelsLine();
            mLlCheckedLabels.addView(llCheckedLabelsLine);
        }
        LinearLayout checkedLabel = createCheckedLabel(labelTag, labelName);
        checkedLabel.measure(0, 0);
        int checkedLabelWidth = checkedLabel.getMeasuredWidth() + checkedLabelLeftMargin;
        int newLabelsLineWidth = currentLabelsLineWidth + checkedLabelWidth;
        if (newLabelsLineWidth > totalCheckedLabelsWidth) {
            if (currentLabelsLineWidth > 0) {
                createCheckedLabelsLine();
                llCheckedLabelsLine.addView(checkedLabel);
                currentLabelsLineWidth = checkedLabelWidth;
                mLlCheckedLabels.addView(llCheckedLabelsLine);
            } else {
                llCheckedLabelsLine.addView(checkedLabel);
                createCheckedLabelsLine();
                currentLabelsLineWidth = 0;
                mLlCheckedLabels.addView(llCheckedLabelsLine);
            }
        } else {
            llCheckedLabelsLine.addView(checkedLabel);
            currentLabelsLineWidth = newLabelsLineWidth;
        }
        mActivitySubscribeBinding.svActivitySubscribeCheckedLabels.post(new Runnable() {
            @Override
            public void run() {
                mActivitySubscribeBinding.svActivitySubscribeCheckedLabels.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public void createCheckedLabelsLine() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        llCheckedLabelsLine = new LinearLayout(CommonUtils.getContext());
        llCheckedLabelsLine.setOrientation(LinearLayout.HORIZONTAL);
        params.topMargin = CommonUtils.dip2px(10);
        llCheckedLabelsLine.setLayoutParams(params);
    }

    public LinearLayout createCheckedLabel(String labelTag, String labelName) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
        LinearLayout llCheckedLabel = new LinearLayout(CommonUtils.getContext());
        llCheckedLabel.setOrientation(LinearLayout.HORIZONTAL);
        params.leftMargin = CommonUtils.dip2px(11);
        llCheckedLabel.setLayoutParams(params);
        llCheckedLabel.setTag(labelTag);

        LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(-2, -2);
        TextView tvLabelName = new TextView(CommonUtils.getContext());
        tvParams.topMargin = CommonUtils.dip2px(5);
        tvLabelName.setBackgroundResource(R.drawable.shape_rounded_rectangle_skilllabel_gray);
        tvLabelName.setText(labelName);
        tvLabelName.setPadding(CommonUtils.dip2px(15), CommonUtils.dip2px(12), CommonUtils.dip2px(15), CommonUtils.dip2px(12));
        tvLabelName.setLayoutParams(tvParams);

        LinearLayout.LayoutParams ivbtnParams = new LinearLayout.LayoutParams(-2, -2);
        ImageButton ivbtnUnCheckedLabel = new ImageButton(CommonUtils.getContext());
        ivbtnUnCheckedLabel.setBackground(new ColorDrawable(Color.TRANSPARENT));
        ivbtnUnCheckedLabel.setImageResource(R.mipmap.delete_icon);
        ivbtnParams.leftMargin = CommonUtils.dip2px(-7);
        ivbtnUnCheckedLabel.setLayoutParams(ivbtnParams);

        llCheckedLabel.addView(tvLabelName);
        llCheckedLabel.addView(ivbtnUnCheckedLabel);
//        llCheckedLabel.setOnClickListener(new deleteCheckedLabelListener());
        ivbtnUnCheckedLabel.setOnClickListener(new deleteCheckedLabelListener(llCheckedLabel));

        return llCheckedLabel;
    }

    public class deleteCheckedLabelListener implements View.OnClickListener {
        LinearLayout mLlCheckedLabel;

        public deleteCheckedLabelListener(LinearLayout llCheckedLabel) {
            this.mLlCheckedLabel = llCheckedLabel;
        }

        @Override
        public void onClick(View v) {
            String labelTag = (String) mLlCheckedLabel.getTag();
            int deleteIndex = listCheckedLabelTag.indexOf(labelTag);
            listCheckedLabelTag.remove(deleteIndex);
            listCheckedLabelName.remove(deleteIndex);
            LinearLayout parentLabelsLine = (LinearLayout) mLlCheckedLabel.getParent();
            parentLabelsLine.removeView(mLlCheckedLabel);
            if (parentLabelsLine.getChildCount() <= 0) {
                LinearLayout parentCheckedLabels = (LinearLayout) parentLabelsLine.getParent();
                int parentLabelsLineIndex = parentCheckedLabels.indexOfChild(parentLabelsLine);
                if (parentLabelsLineIndex < parentCheckedLabels.getChildCount() - 1) {
                    parentCheckedLabels.removeView(parentLabelsLine);
                }
            }
//            ToastUtils.shortToast(listCheckedLabelName.get(deleteIndex));
        }
    }
}
