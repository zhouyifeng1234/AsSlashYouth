package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.domain.SkillLabelAllBean;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.engine.SkillLabelHouseManager;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/28.
 */
public class SearchActivityHotServiceModel extends BaseObservable {
    private SearchActivityHotServiceBinding searchActivityHotServiceBinding;
    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;
    private  ActivitySearchBinding mActivitySearchBinding;
    private int rlChooseMainLabelVisible = View.INVISIBLE;
    private NumberPicker mNpChooseMainLabels;
    private String[] mainLabelsArr ;
    private SearchNeedResultTabModel searchNeedResultTabModel;
    private ArrayList<SkillLabelBean> listFirstSkilllabel = new ArrayList<>();
    private ArrayList<SkillLabelBean> listSecondSkilllabel = new ArrayList<>();
    private ArrayList<SkillLabelBean> listThirdSkilllabel = new ArrayList();
    private ArrayList<SkillLabelBean> listSkilllabel = new ArrayList<SkillLabelBean>();
    private  ArrayList<SkillLabelBean> listThirdSkilllabelName = new ArrayList();
    private int secondId;
    private int firstId;
    private String text = "请点击";
    private CityHistoryEntityDao cityHistoryEntityDao;

    public SearchActivityHotServiceModel(SearchActivityHotServiceBinding searchActivityHotServiceBinding,
                                         ActivitySearchBinding mActivitySearchBinding,CityHistoryEntityDao cityHistoryEntityDao) {
        this.searchActivityHotServiceBinding = searchActivityHotServiceBinding;
        this.mActivitySearchBinding = mActivitySearchBinding;
        this.cityHistoryEntityDao = cityHistoryEntityDao;
        initData();
        initView();
        initListener();
    }

    //加载布局
    private void initView() {
        searchActivityHotServiceBinding.tvOpenChoose.setText(text);
        mNpChooseMainLabels = searchActivityHotServiceBinding.npPublishServiceMainLabels;
        searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.setVerticalScrollBarEnabled(false);
        searchActivityHotServiceBinding.svActivitySearchThirdSkilllabel.setVerticalScrollBarEnabled(false);
    }

    //获取所有的标签
    private void initData() {
        SkillLabelHouseManager.getSkillLabelHouseData(new onGetSkillLabelHouseData());
    }

    @Bindable
    public int getRlChooseMainLabelVisible() {
        return rlChooseMainLabelVisible;
    }

    public void setRlChooseMainLabelVisible(int rlChooseMainLabelVisible) {
        this.rlChooseMainLabelVisible = rlChooseMainLabelVisible;
        notifyPropertyChanged(BR.rlChooseMainLabelVisible);
    }

    public void cannelChooseMainLabel(View view){
        setRlChooseMainLabelVisible(View.GONE);
    }

    //获取从后台所有的技能标签数据
    public class onGetSkillLabelHouseData implements BaseProtocol.IResultExecutor<ArrayList<SkillLabelAllBean>> {
        @Override
        public void execute(ArrayList<SkillLabelAllBean> arrayList) {
            //对集合进行分类
            getSkillLabelAllArrayList(arrayList);
            //默认一级的标签
            showFirstLabel();
            //默认选取二级标签的第一个是一级标签的第一个
            SkillLabelBean firstSkillLabelBean = listFirstSkilllabel.get(0);
            int firstId = firstSkillLabelBean.getId();//默认id
            showSecondLabel(firstId);
            //三级标签的默认，f1=一级标签的id,f2= 对应二级标签的id
            SkillLabelBean secondSkillLabelBean = listSecondSkilllabel.get(0);
            int f1 = secondSkillLabelBean.getF1();
            if(f1 == firstId){
                int secondId = secondSkillLabelBean.getId();
                showThridLabel(firstId,secondId);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

    private void getSkillLabelAllArrayList(ArrayList<SkillLabelAllBean> arrayList) {
        for (SkillLabelAllBean skillLabelAllBean : arrayList) {
            final int f1 = skillLabelAllBean.getF1();
            int f2 = skillLabelAllBean.getF2();
            int id = skillLabelAllBean.getId();
            String tag = skillLabelAllBean.getTag();

            //一级标签集合
            if (f1 == 0 && f2 == 0) {
                SkillLabelBean skillLabelBean = new SkillLabelBean( f1, f2, id,tag);
                listFirstSkilllabel.add(skillLabelBean);
            }

            //二级标签集合
            if (f1 != 0 && f2 == 0) {
                listSecondSkilllabel.add(new SkillLabelBean( f1, f2, id,tag));
            }

            //三级标签集合
            if (f1 != 0 && f2 != 0) {
                listThirdSkilllabel.add(new SkillLabelBean(f1, f2, id,tag));
            }
        }
    }

    //展示一级标签
    private void showFirstLabel() {
        int size = listFirstSkilllabel.size();
         mainLabelsArr = new String[size];
        for (int i = 0; i < listFirstSkilllabel.size(); i++) {
            mainLabelsArr[i] = listFirstSkilllabel.get(i).getTag();
        }
        mNpChooseMainLabels.setDisplayedValues(mainLabelsArr);
        mNpChooseMainLabels.setMinValue(0);
        mNpChooseMainLabels.setMaxValue(mainLabelsArr.length - 1);
        mNpChooseMainLabels.setValue(0);
    }

    //点击一级标签确定按钮
    public void okChooseMainLabel(View v) {
        setRlChooseMainLabelVisible(View.INVISIBLE);
        int value = mNpChooseMainLabels.getValue();
        SearchActivity searchActivity = (SearchActivity) CommonUtils.getCurrentActivity();
        searchActivity.checkedFirstLabel = mainLabelsArr[value];
        searchActivityHotServiceBinding.tvOpenChoose.setText(mainLabelsArr[value]);
        //一级标签的id，展示二级
        SkillLabelBean skillLabelBean = listFirstSkilllabel.get(value);
        int firstId = skillLabelBean.getId();
        showSecondLabel(firstId);
        //二级的id,展示三级
        SkillLabelBean labelBean = listSkilllabel.get(0);
        int secondId = labelBean.getId();
        showThridLabel(firstId,secondId);
    }

    //初始化展示二级标签
    private void showSecondLabel(int firstId) {
    listSkilllabel.clear();

    //初始化的时候展示的是id为对应的的二级标签
    for (SkillLabelBean skillLabelBean : listSecondSkilllabel) {
        int f1 = skillLabelBean.getF1();
        int f2 = skillLabelBean.getF2();
        if(f1 == firstId&&f2==0){
            listSkilllabel.add(skillLabelBean);
        }
    }

    searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.setAdapter(new SubscribeSecondSkilllabelAdapter(listSkilllabel));
    SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
    searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.post(new Runnable() {
        @Override
        public void run() {
            View lvActivitySubscribeSecondSkilllableListFirstChild = searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.getChildAt(0);
             LogKit.d(lvActivitySubscribeSecondSkilllableListFirstChild + " ");
            SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();//获取他的tag
            lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;
        }
    });
    }

    //初始化展示三级标签，默认是id为1的对应的三级标签
    private void showThridLabel(int firstId,int secondId) {
        listThirdSkilllabelName.clear();
        for (SkillLabelBean skillLabelBean : listThirdSkilllabel) {
            int f1 = skillLabelBean.getF1();
            int f2 = skillLabelBean.getF2();
            int thridId = skillLabelBean.getId();
            String tag = skillLabelBean.getTag();
            if(f2!=0){
                if(f1 == firstId&&f2 == secondId){
                    listThirdSkilllabelName.add(new SkillLabelBean(f1,f2,thridId,tag));
                }
            }
        }
        //TODO,这里隐藏的bug,但不影响逻辑和使用
        listThirdSkilllabelName.add(new SkillLabelBean(0,0,0,""));
        listThirdSkilllabelName.add(new SkillLabelBean(0,0,0,""));

        searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.removeAllViews();
        searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.post(new Runnable() {
            LinearLayout llSkilllabelLine;
            int lineCount = 0;
            @Override
            public void run() {
                int scrollViewWidth =searchActivityHotServiceBinding.llActivitySearchThirdSkilllabel.getMeasuredWidth();
                scrollViewWidth = scrollViewWidth - CommonUtils.dip2px(30);
                int labelRightMargin = CommonUtils.dip2px(10);
                int skillLabelLineWidth = 0;
                for (int i = 0; i < listThirdSkilllabelName.size(); i++) {
                    SkillLabelBean skillLabelBean = listThirdSkilllabelName.get(i);
                    String thirdSkilllabelName = skillLabelBean.getTag();
                    int thridId = skillLabelBean.getId();
                    // String thirdSkilllabelName = listThirdSkilllabelName.get(i);//这是第几个技能名字
                    //创建标签TextView
                    LinearLayout.LayoutParams llParamsForSkillLabel = new LinearLayout.LayoutParams(-2, -2); //-1 match -2 wrap
                    llParamsForSkillLabel.rightMargin = CommonUtils.dip2px(labelRightMargin);
                    TextView tvThirdSkilllabelName = new TextView(CommonUtils.getContext());
                    tvThirdSkilllabelName.setLayoutParams(llParamsForSkillLabel);
                    tvThirdSkilllabelName.setMaxLines(1);
                    tvThirdSkilllabelName.setTag(thirdSkilllabelName);
                    tvThirdSkilllabelName.setGravity(Gravity.CENTER);
                    tvThirdSkilllabelName.setTextColor(0xff333333);
                    tvThirdSkilllabelName.setTextSize(14);
                    tvThirdSkilllabelName.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(16), CommonUtils.dip2px(11));
                    tvThirdSkilllabelName.setBackgroundResource(R.drawable.shape_rounded_rectangle_third_skilllabel);
                    tvThirdSkilllabelName.setText(thirdSkilllabelName);

                    //TODO,这里隐藏的bug,但不影响逻辑和使用
                    if(thirdSkilllabelName == ""){
                        tvThirdSkilllabelName.setVisibility(View.GONE);
                    }

                    //不同的textview的点击事件
                    tvThirdSkilllabelName.setOnClickListener(new CheckThirdLabelListener());

                    //测量标签TextView的宽度并判断是否换行
                    tvThirdSkilllabelName.measure(0, 0);
                    int tvThirdSkilllabelWidth = tvThirdSkilllabelName.getMeasuredWidth() + labelRightMargin;
                    int newSkillLabelLineWidth = skillLabelLineWidth + tvThirdSkilllabelWidth;
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
            }
        });
    }

    public class CheckThirdLabelListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String labelTag = (String) v.getTag();
            showSearchResult(labelTag);
        }
    }

    //加载监听
    private void initListener() {
        //监听搜索首页
        searchActivityHotServiceBinding.lvActivitySearchSecondSkilllableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

                SkillLabelBean skillLabelBean = listSkilllabel.get(position);
                int f1 = skillLabelBean.getF1();
                int secondId = skillLabelBean.getId();
                showThridLabel(f1,secondId);
            }
        });
        //点击事件
        searchActivityHotServiceBinding.tvOpenChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRlChooseMainLabelVisible(View.VISIBLE);
            }
        });
        searchActivityHotServiceBinding.ivbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRlChooseMainLabelVisible(View.VISIBLE);
            }
        });
    }

    // 显示搜索页面
    public void showSearchResult(String tag)  {
        SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
        currentActivity.changeView(2);
        searchNeedResultTabModel = new SearchNeedResultTabModel(currentActivity.searchNeedResultTabBinding,tag,cityHistoryEntityDao);
        currentActivity.searchNeedResultTabBinding.setSearchNeedResultTabModel(searchNeedResultTabModel);
    }
}
