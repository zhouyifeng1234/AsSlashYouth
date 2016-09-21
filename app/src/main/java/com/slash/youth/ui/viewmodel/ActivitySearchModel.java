package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogHomeSubscribeBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.adapter.SearchContentListAdapter;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable  {
    ActivitySearchBinding mActivitySearchBinding;
    private boolean isShow;
    private  SearchContentListAdapter adapter;


    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding) {
        this.mActivitySearchBinding = activitySearchBinding;

        initView();
    }

    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;

    private void initView() {
        mActivitySearchBinding.lvActivitySearchSecondSkilllableList.setVerticalScrollBarEnabled(false);
        mActivitySearchBinding.svActivitySearchThirdSkilllabel.setVerticalScrollBarEnabled(false);

        initData();
        initListener();
        //TODO 监听搜索框的搜索,zss
        searchListener();

    }

    private void searchListener() {
        //TODO 添加联想搜索,zss
        mActivitySearchBinding.etActivitySearchAssociation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mActivitySearchBinding.llActivityHotService.setVisibility(View.GONE);
                mActivitySearchBinding.lvLvSearchcontent.setVisibility(View.VISIBLE);
                //文字监听不为0，就显示搜索内容
                if (s.length()!=0) {
                   isShow = false;

                }else if(s.length() == 0){
                    isShow =true;

                }
                adapter.showRemoveBtn(isShow);
                //显示cleanAll
                cleanAll(isShow);


            }

            //文本改变之后
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //设置搜索的数据，zss
        setSearchContentData(isShow);
    }
    //zss 显示清除所有历史
    private void cleanAll(boolean isShow) {
        mActivitySearchBinding.tvCleanAll.setVisibility(isShow?View.VISIBLE:View.GONE);
        mActivitySearchBinding.tvCleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击出现对话框
                 LogKit.d("显示对话框");
                showCleanAllDialog();
            }
        });
    }

    //TODO zss 显示对话框
    private void showCleanAllDialog() {
        //创建AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommonUtils.getCurrentActivity());
        //数据绑定填充视图
        DialogSearchCleanBinding dialogSearchCleanBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_search_clean, null, false);
        //创建数据模型
        DialogSearchCleanModel dialogSearchCleanModel = new DialogSearchCleanModel(dialogSearchCleanBinding);
        //绑定数据模型
        dialogSearchCleanBinding.setDialogSearchCleanModel(dialogSearchCleanModel);
        //设置布局
        dialogBuilder.setView(dialogSearchCleanBinding.getRoot());//getRoot返回根布局
        //创建dialogSearch
        AlertDialog dialogSearch = dialogBuilder.create();
        dialogSearch.show();
        dialogSearchCleanModel.currentDialog = dialogSearch;
        dialogSearch.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        Window dialogSubscribeWindow = dialogSearch.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        //TODO 高度和宽度要重新设定
        dialogParams.width = CommonUtils.dip2px(350);//宽度
        dialogParams.height = CommonUtils.dip2px(280);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);//dialog的灰度
//        dialogBuilder.show();

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
        mActivitySearchBinding.lvActivitySearchSecondSkilllableList.setAdapter(new SubscribeSecondSkilllabelAdapter(listSkilllabel));
        SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
        mActivitySearchBinding.lvActivitySearchSecondSkilllableList.post(new Runnable() {
            @Override
            public void run() {
                View lvActivitySubscribeSecondSkilllableListFirstChild = mActivitySearchBinding.lvActivitySearchSecondSkilllableList.getChildAt(0);
                LogKit.v(lvActivitySubscribeSecondSkilllableListFirstChild.getTag() + "");
                SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();
                lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;
            }
        });
        setThirdSkilllabelData();
    }

    private void setSearchContentData(boolean isShow) {
        //TODO 联想搜索的内容，假的数据从服务端获取，zss
        ArrayList<String> search_contentlist = new ArrayList<>();
        search_contentlist.add("ui设计师");
        search_contentlist.add("uiux设计师");
        search_contentlist.add("uii设计师");
        adapter= new SearchContentListAdapter(search_contentlist,isShow);
        mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);
    }

    /**
     * 根据选择的二级标签，显示对应的三级标签
     */
    public void setThirdSkilllabelData() {
        //TODO 目前只是测试数据，到时候该方法可能需要传入二级标签的ID等信息，以方便查询对应的三级标签
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
        mActivitySearchBinding.llActivitySearchThirdSkilllabel.removeAllViews();
        mActivitySearchBinding.llActivitySearchThirdSkilllabel.post(new Runnable() {
            LinearLayout llSkilllabelLine;
            int lineCount = 0;
//            int lineLabelCount = 0;

            @Override
            public void run() {
                int scrollViewWidth = mActivitySearchBinding.llActivitySearchThirdSkilllabel.getMeasuredWidth();
                scrollViewWidth = scrollViewWidth - CommonUtils.dip2px(30);

                int labelRightMargin = CommonUtils.dip2px(10);
                int skillLabelLineWidth = 0;


                for (int i = 0; i < listThirdSkilllabelName.size(); i++) {
                    String thirdSkilllabelName = listThirdSkilllabelName.get(i);

                    //创建标签TextView
                    LinearLayout.LayoutParams llParamsForSkillLabel = new LinearLayout.LayoutParams(-2, -2);
                    llParamsForSkillLabel.rightMargin = CommonUtils.dip2px(labelRightMargin);
                    TextView tvThirdSkilllabelName = new TextView(CommonUtils.getContext());
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
                            mActivitySearchBinding.llActivitySearchThirdSkilllabel.addView(llSkilllabelLine);
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
                            mActivitySearchBinding.llActivitySearchThirdSkilllabel.addView(llSkilllabelLine);
                            skillLabelLineWidth = 0;
                        } else {
                            skillLabelLineWidth = newSkillLabelLineWidth;
                        }
                    }
                }
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

    private void initListener() {
        mActivitySearchBinding.lvActivitySearchSecondSkilllableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

}
