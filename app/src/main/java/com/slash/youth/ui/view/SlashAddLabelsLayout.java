package com.slash.youth.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.ui.activity.SubscribeActivity;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/25.
 */
public class SlashAddLabelsLayout extends LinearLayout {
    public SlashAddLabelsLayout(Context context) {
        super(context);
    }

    public SlashAddLabelsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlashAddLabelsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Activity mActivity;

    public ArrayList<String> listTotalAddedTagsNames = new ArrayList<String>();
    public ArrayList<String> listTotalAddedTags = new ArrayList<String>();

    public void addSkillLabels() {
        removeAllViews();
        int linesCount = listTotalAddedTagsNames.size() / 3;//完整的行数
        for (int i = 0; i < linesCount; i++) {
            LinearLayout skillLabelsLine = createSkillLabelsLine(false, i);
            addView(skillLabelsLine);
        }
        LinearLayout skillLabelsLastLine = createSkillLabelsLine(true, linesCount);
        addView(skillLabelsLastLine);
    }

    public void initSkillLabels() {
        addSkillLabels();
    }

    public LinearLayout createSkillLabelsLine(boolean isLastLine, int lineIndex) {
        LayoutParams paramsLine = new LayoutParams(-1, -2);
        LinearLayout llSkillLabelsLine = new LinearLayout(CommonUtils.getContext());
        llSkillLabelsLine.setOrientation(LinearLayout.HORIZONTAL);
        if (isLastLine) {
            //最后一行添加标签处理
            int lastLineLabelCount = listTotalAddedTagsNames.size() % 3;
            for (int i = 0; i < lastLineLabelCount; i++) {
                int labelIndex = lineIndex * 3 + i;
                String labelText = listTotalAddedTagsNames.get(labelIndex);
                RelativeLayout skillLabel = createSkillLabel(labelText, View.VISIBLE, View.VISIBLE, View.INVISIBLE, labelIndex);
                llSkillLabelsLine.addView(skillLabel);
                if (i < 2) {
                    View labelSpaceView = createLabelSpaceView();
                    llSkillLabelsLine.addView(labelSpaceView);
                }
            }
            RelativeLayout rlAddSkillLabel = createSkillLabel("", View.VISIBLE, View.INVISIBLE, View.VISIBLE, -1);
            llSkillLabelsLine.addView(rlAddSkillLabel);
            if (lastLineLabelCount < 2) {
                View labelSpaceView = createLabelSpaceView();
                llSkillLabelsLine.addView(labelSpaceView);
            }
            lastLineLabelCount++;
            for (int i = 0; i < 3 - lastLineLabelCount; i++) {
                RelativeLayout noSkillLabel = createSkillLabel("", View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, -1);
                llSkillLabelsLine.addView(noSkillLabel);
                if (i == 0 && lastLineLabelCount == 0) {
                    View labelSpaceView = createLabelSpaceView();
                    llSkillLabelsLine.addView(labelSpaceView);
                }
            }
        } else {
            for (int i = 0; i < 3; i++) {
                int labelIndex = lineIndex * 3 + i;
                String labelText = listTotalAddedTagsNames.get(labelIndex);
                RelativeLayout skillLabel = createSkillLabel(labelText, View.VISIBLE, View.VISIBLE, View.INVISIBLE, labelIndex);
                llSkillLabelsLine.addView(skillLabel);
                if (i < 2) {
                    View labelSpaceView = createLabelSpaceView();
                    llSkillLabelsLine.addView(labelSpaceView);
                }
            }
        }
        llSkillLabelsLine.setLayoutParams(paramsLine);
        return llSkillLabelsLine;
    }

    public RelativeLayout createSkillLabel(String labelText, int tvLabelTextVisibility, int ivbtnDelLabelVisibility, int ivAddLabelVisibility, int labelIndex) {
        LayoutParams paramsSkillLabel = new LayoutParams(0, CommonUtils.dip2px(55));
        if (TextUtils.isEmpty(labelText) || ivAddLabelVisibility == View.VISIBLE) {
            paramsSkillLabel.height = 0;
        }
        RelativeLayout rlSkillLabel = new RelativeLayout(CommonUtils.getContext());

        RelativeLayout.LayoutParams paramsTvLabelText = new RelativeLayout.LayoutParams(-1, -1);
        TextView tvLabelText = new TextView(CommonUtils.getContext());
        tvLabelText.setBackgroundResource(R.drawable.shape_publish_demand_label_bg_);
        tvLabelText.setGravity(Gravity.CENTER);
        tvLabelText.setText(labelText);
        tvLabelText.setTextColor(0xff999999);
        tvLabelText.setTextSize(14);
        tvLabelText.setVisibility(tvLabelTextVisibility);
        paramsTvLabelText.rightMargin = CommonUtils.dip2px(8);
        paramsTvLabelText.topMargin = CommonUtils.dip2px(16);
        tvLabelText.setLayoutParams(paramsTvLabelText);
        if (ivAddLabelVisibility == View.VISIBLE) {
            tvLabelText.setOnClickListener(new AddLabelsClickListener());
        }

        RelativeLayout.LayoutParams paramsIvbtnDelLabel = new RelativeLayout.LayoutParams(-2, -2);
        ImageButton ivbtnDelLabel = new ImageButton(CommonUtils.getContext());
        ivbtnDelLabel.setBackgroundColor(Color.TRANSPARENT);
        ivbtnDelLabel.setImageResource(R.mipmap.pluse);
        ivbtnDelLabel.setVisibility(ivbtnDelLabelVisibility);
        paramsIvbtnDelLabel.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        ivbtnDelLabel.setLayoutParams(paramsIvbtnDelLabel);
        if (ivAddLabelVisibility == View.INVISIBLE) {
            ivbtnDelLabel.setTag(labelIndex);
            ivbtnDelLabel.setOnClickListener(new DelLabelClickListener());
        }

        RelativeLayout.LayoutParams paramsIvAddLabel = new RelativeLayout.LayoutParams(CommonUtils.dip2px(22), CommonUtils.dip2px(22));
        ImageView ivAddLabel = new ImageView(CommonUtils.getContext());
        ivAddLabel.setImageResource(R.mipmap.jiajineng);
        ivAddLabel.setVisibility(ivAddLabelVisibility);
        paramsIvAddLabel.addRule(RelativeLayout.CENTER_IN_PARENT);
        ivAddLabel.setLayoutParams(paramsIvAddLabel);


        rlSkillLabel.addView(tvLabelText);
        rlSkillLabel.addView(ivbtnDelLabel);
        rlSkillLabel.addView(ivAddLabel);
        paramsSkillLabel.weight = 1;
        rlSkillLabel.setLayoutParams(paramsSkillLabel);
        return rlSkillLabel;
    }

    public View createLabelSpaceView() {
        LayoutParams paramsSpaceView = new LayoutParams(CommonUtils.dip2px(5), 0);
        View labelSpaceView = new View(CommonUtils.getContext());
        labelSpaceView.setLayoutParams(paramsSpaceView);
        return labelSpaceView;
    }

    public class AddLabelsClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intentSubscribeActivity = new Intent(CommonUtils.getContext(), SubscribeActivity.class);
            mActivity.startActivityForResult(intentSubscribeActivity, 10);
        }
    }

    public class DelLabelClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            int labelIndex = (int) v.getTag();
            if (labelIndex >= 0 && labelIndex < listTotalAddedTagsNames.size()) {
                listTotalAddedTagsNames.remove(labelIndex);
                listTotalAddedTags.remove(labelIndex);
                initSkillLabels();
            }
        }
    }

    public void getAddLabelsResult(Intent intentAddLabelsData) {
        Bundle bundleCheckedLabelsData = intentAddLabelsData.getBundleExtra("bundleCheckedLabelsData");
        if (bundleCheckedLabelsData != null) {
            ArrayList<String> listCheckedLabelName = bundleCheckedLabelsData.getStringArrayList("listCheckedLabelName");
            ArrayList<String> listCheckedLabelTag = bundleCheckedLabelsData.getStringArrayList("listCheckedLabelTag");
//            ToastUtils.shortToast("listCheckedLabelName.size()" + listCheckedLabelName.size());
//            listTotalAddedTagsNames.clear();//添加清空操作
            //限制一共就3个的操作
//            limitTotalLabelsCount(3);
            listTotalAddedTagsNames.addAll(listCheckedLabelName);
            listTotalAddedTags.addAll(listCheckedLabelTag);
            addSkillLabels();
        }
    }

    /**
     * 修改需求或者服务时，重新加载技能标签
     *
     * @param reloadTagsName
     * @param reloadTags
     */
    public void reloadSkillLabels(ArrayList<String> reloadTagsName, ArrayList<String> reloadTags) {
        listTotalAddedTagsNames.clear();
        listTotalAddedTags.clear();
        listTotalAddedTagsNames.addAll(reloadTagsName);
        listTotalAddedTags.addAll(reloadTags);
        addSkillLabels();
    }

//    private void limitTotalLabelsCount(int count) {
//        listTotalAddedTagsNames.addAll(listTotalAddedTagsNames.subList(0, 3));
//    }

    public void setActivity(Activity activity) {
        this.mActivity = activity;
    }

    public ArrayList<String> getAddedTagsName() {
        return listTotalAddedTagsNames;
    }

    public ArrayList<String> getAddedTags() {
        return listTotalAddedTags;
    }

    //zss
    public void setTag(ArrayList<String> tagArrayList) {
        listTotalAddedTagsNames.addAll(tagArrayList);
        addSkillLabels();
    }


}
