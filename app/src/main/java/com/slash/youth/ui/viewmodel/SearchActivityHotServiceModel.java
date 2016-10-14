package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/28.
 */
public class SearchActivityHotServiceModel extends BaseObservable {
    private SearchActivityHotServiceBinding searchActivityHotServiceBinding;
    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;
    private  ActivitySearchBinding mActivitySearchBinding;
    private SearchNeedResultTabBinding searchNeedResultTabBinding;
    private int rlChooseMainLabelVisible = View.INVISIBLE;
    private NumberPicker mNpChooseMainLabels;
    String[] mainLabelsArr;
    private SearchNeedResultTabModel searchNeedResultTabModel;

    public SearchNeedResultTabBinding getSearchNeedResultTabBinding() {
        return searchNeedResultTabBinding;
    }

    public SearchActivityHotServiceBinding getSearchActivityHotServiceBinding() {
        return searchActivityHotServiceBinding;
    }

    public SearchNeedResultTabModel getSearchNeedResultTabModel() {
        return searchNeedResultTabModel;
    }

    public SearchActivityHotServiceModel(SearchActivityHotServiceBinding searchActivityHotServiceBinding, ActivitySearchBinding mActivitySearchBinding) {
        this.searchActivityHotServiceBinding = searchActivityHotServiceBinding;
        this.mActivitySearchBinding = mActivitySearchBinding;
        initView();
    }

    //加载布局
    private void initView() {
        mNpChooseMainLabels = searchActivityHotServiceBinding.npPublishServiceMainLabels;
        //zss  先展示首页
        searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.setVerticalScrollBarEnabled(false);
        searchActivityHotServiceBinding.svActivitySearchThirdSkilllabel.setVerticalScrollBarEnabled(false);
        initData();
        initListener();
    }

    //加载数据
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
        searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.setAdapter(new SubscribeSecondSkilllabelAdapter(listSkilllabel));
        SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
        searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.post(new Runnable() {
            @Override
            public void run() {
                View lvActivitySubscribeSecondSkilllableListFirstChild = searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.getChildAt(0);
              //  LogKit.d(lvActivitySubscribeSecondSkilllableListFirstChild + "");
                SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();//获取他的tag
                lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;//tag建立数据模型
            }
        });
        setThirdSkilllabelData();

        //openChoose的数据
        setOpenChoose();
    }

    private void setOpenChoose() {
        mainLabelsArr = new String[]{"金融", "IT", "农业", "水产", "文学"};//大类标签内容实际应该由服务端接口返回
        mNpChooseMainLabels.setDisplayedValues(mainLabelsArr);
        mNpChooseMainLabels.setMinValue(0);
        mNpChooseMainLabels.setMaxValue(mainLabelsArr.length - 1);
        mNpChooseMainLabels.setValue(1);
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
        searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.removeAllViews();
        searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.post(new Runnable() {
            LinearLayout llSkilllabelLine;
            int lineCount = 0;
//            int lineLabelCount = 0;
            @Override
            public void run() {
                int scrollViewWidth =searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.getMeasuredWidth();
                scrollViewWidth = scrollViewWidth - CommonUtils.dip2px(30);
                int labelRightMargin = CommonUtils.dip2px(10);
                int skillLabelLineWidth = 0;
                for (int i = 0; i < listThirdSkilllabelName.size(); i++) {
                    String thirdSkilllabelName = listThirdSkilllabelName.get(i);//这是第几个技能名字

                    //创建标签TextView
                    LinearLayout.LayoutParams llParamsForSkillLabel = new LinearLayout.LayoutParams(-2, -2); //-1 match -2 wrap
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
                            searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.addView(llSkilllabelLine);
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
                            searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.addView(llSkilllabelLine);
                            skillLabelLineWidth = 0;
                        } else {
                            skillLabelLineWidth = newSkillLabelLineWidth;
                        }
                    }
                    //不同的textview的点击事件
                    tvThirdSkilllabelName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                             //LogKit.d("点击的textview"+((TextView)v).getText());
                          /* String skillName = (String) ((TextView) v).getText();
                            mActivitySearchBinding.etActivitySearchAssociation.setText(skillName);
                            mActivitySearchBinding.etActivitySearchAssociation.setTextColor(Color.BLACK);*/
                            //展示搜索结果的页面
                             // LogKit.d("展示搜索结果的页面");
                            showSearchResult();
                        }
                    });
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

    //加载监听
    private void initListener() {
        //监听搜索首页
        searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // LogKit.d("首页监听");
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
        //点击事件
        searchActivityHotServiceBinding.tvOpenChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChoose();
            }
        });
    }

    //zss 显示搜索页面
    public void showSearchResult()  {
        searchNeedResultTabBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_need_result_tab, null, false);
        searchNeedResultTabModel = new SearchNeedResultTabModel(searchNeedResultTabBinding);
        searchNeedResultTabBinding.setSearchNeedResultTabModel(searchNeedResultTabModel);
        mActivitySearchBinding.flSearchFirst.addView(searchNeedResultTabBinding.getRoot());
        ActivitySearchModel.searchType = 4;
        ActivitySearchModel.isShowThreePage = true;
    }


    //选择行业
    public void openChoose() {
        setRlChooseMainLabelVisible(View.VISIBLE);
    }

    @Bindable
    public int getRlChooseMainLabelVisible() {
        return rlChooseMainLabelVisible;
    }

    public void setRlChooseMainLabelVisible(int rlChooseMainLabelVisible) {
       this.rlChooseMainLabelVisible = rlChooseMainLabelVisible;
        notifyPropertyChanged(BR.rlChooseMainLabelVisible);
    }

    public void okChooseMainLabel(View v) {
        setRlChooseMainLabelVisible(View.INVISIBLE);
        int value = mNpChooseMainLabels.getValue();
        SearchActivity searchActivity = (SearchActivity) CommonUtils.getCurrentActivity();
        searchActivity.checkedFirstLabel = mainLabelsArr[value];
    }

}
