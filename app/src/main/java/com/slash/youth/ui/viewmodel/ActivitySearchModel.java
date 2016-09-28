package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.SearchAllItem;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.ui.adapter.SearchContentListAdapter;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable {
    ActivitySearchBinding mActivitySearchBinding;
    private boolean isShow;
    private  SearchContentListAdapter adapter;
    private SearchNeedResultTabBinding searchNeedResultTabBinding;
    private View searchView;
    private SearchActivityHotServiceBinding searchActivityHotServiceBinding;
    private int  SearchType;
    private int  SearchNeed = 1;
    private int  SearchService = 2;
    private int  SearchPerson = 3;


    public void setSearchNeedResultTabBinding(SearchNeedResultTabBinding searchNeedResultTabBinding) {
        this.searchNeedResultTabBinding = searchNeedResultTabBinding;
    }

    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding) {
        this.mActivitySearchBinding = activitySearchBinding;
        //1.搜索框的监听
        searchListener();

    }

    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;

    private void initView() {
        //zss  先展示首页
       searchView.findViewById(R.id.lv_activity_search_second_skilllable_list).setVerticalScrollBarEnabled(false);
        searchView.findViewById(R.id.sv_activity_search_third_skilllabel).setVerticalScrollBarEnabled(false);
        initData();
        initListener();
    }
      //2.监听框的搜索
    private void searchListener() {
        //点击一下搜索框出现,zss
        mActivitySearchBinding.etActivitySearchAssociation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  LogKit.d("首页隐藏，搜索历史出现");
                mActivitySearchBinding.rlSearchMain.setVisibility(View.GONE);
                mActivitySearchBinding.llSearchHistory.setVisibility(View.VISIBLE);
                //加载搜索框历史的数据
                initSearchHistoryData();
                //删除单条和清除所有
                cleanAll(true);
            }
        });

        // 搜索框里面文字变化，出现联想搜索
        mActivitySearchBinding.etActivitySearchAssociation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //下面的首页隐藏
                mActivitySearchBinding.rlSearchMain.setVisibility(View.GONE);
                //显示搜索
                mActivitySearchBinding.llSearchHistory.setVisibility(View.VISIBLE);

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
        //点击搜索，显示搜索的结果 zss
        mActivitySearchBinding.tvSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //显示结果页面
            LogKit.d("显示直接搜索结果页面");

          //直接搜索结果
           showSearchAllReasultView();

            }
        });
    }
    //zss 直接搜索结果
    private void showSearchAllReasultView() {
     mActivitySearchBinding.flSearchFirst.removeAllViews();
    ListView listView = new ListView(CommonUtils.getContext());
     //TODO 假数据 接口传入数据，zss
        ArrayList<SearchAllItem> arrayList = new ArrayList<>();
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        arrayList.add(new SearchAllItem());
        PagerSearchItemAdapter pagerSearchItemAdapter = new PagerSearchItemAdapter(arrayList);
        listView.setAdapter(pagerSearchItemAdapter);
         mActivitySearchBinding.flSearchFirst.addView(listView);
    }

    private void initSearchHistoryData() {
        //TODO 联想搜索的内容，假的数据从服务端获取，zss
        ArrayList<String> search_contentlist = new ArrayList<>();
        search_contentlist.add("独墅湖高教区");
        search_contentlist.add("翰邻里中心");
        search_contentlist.add("克苏勒苏");
        adapter= new SearchContentListAdapter(search_contentlist,true);
        mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);

    }

    //zss 显示搜索页面
    private void showSearchReasult()  {
        //先把当前的页面隐藏,展示需要的布局
        mActivitySearchBinding.flSearch.removeAllViews();
        switch (SearchType){
            case 1:
                mActivitySearchBinding.flSearch.addView(searchNeedResultTabBinding.getRoot());
                break;
            case 2:
                mActivitySearchBinding.flSearch.addView(searchNeedResultTabBinding.getRoot());
                break;
            case 3:
                searchNeedResultTabBinding.llSearchTabHead.setVisibility(View.GONE);
                searchNeedResultTabBinding.lvShowSearchResult.setVisibility(View.GONE);
                searchNeedResultTabBinding.llSearchTab2.setVisibility(View.VISIBLE);
                searchNeedResultTabBinding.lvShowSearchPerson.setVisibility(View.VISIBLE);
                mActivitySearchBinding.flSearch.addView(searchNeedResultTabBinding.getRoot());
                break;
        }
    }

    //zss 显示清除所有历史
    private void cleanAll(boolean isShow) {
        mActivitySearchBinding.tvCleanAll.setVisibility(isShow?View.VISIBLE:View.GONE);
        mActivitySearchBinding.tvCleanAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击出现对话框
            //     LogKit.d("显示对话框");
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
        //定义显示的dialog的宽和高
        dialogParams.width = CommonUtils.dip2px(280);//宽度
        dialogParams.height = CommonUtils.dip2px(174);//高度
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
        ((ListView)searchView.findViewById(R.id.lv_activity_search_second_skilllable_list)).setAdapter(new SubscribeSecondSkilllabelAdapter(listSkilllabel));
        SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
        searchView.findViewById(R.id.lv_activity_search_second_skilllable_list).post(new Runnable() {
            @Override
            public void run() {
                View lvActivitySubscribeSecondSkilllableListFirstChild = ((ListView) searchView.findViewById(R.id.lv_activity_search_second_skilllable_list)).getChildAt(0);
                LogKit.v(lvActivitySubscribeSecondSkilllableListFirstChild.getTag() + "");
                SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();//获取他的tag
                lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;//tag建立数据模型
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
        ((LinearLayout)searchView.findViewById(R.id.ll_activity_search_third_skilllabel)).removeAllViews();
        searchView.findViewById(R.id.ll_activity_search_third_skilllabel).post(new Runnable() {
            LinearLayout llSkilllabelLine;
            int lineCount = 0;
//            int lineLabelCount = 0;

            @Override
            public void run() {
                int scrollViewWidth = searchView.findViewById(R.id.ll_activity_search_third_skilllabel).getMeasuredWidth();
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
                            ((LinearLayout)searchView.findViewById(R.id.ll_activity_search_third_skilllabel)).addView(llSkilllabelLine);
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
                           ((LinearLayout)searchView.findViewById(R.id.ll_activity_search_third_skilllabel)).addView(llSkilllabelLine);
                            skillLabelLineWidth = 0;
                        } else {
                            skillLabelLineWidth = newSkillLabelLineWidth;
                        }
                    }

                    //不同的textview的点击事件
                    tvThirdSkilllabelName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                          //  LogKit.d("点击的textview"+((TextView)v).getText());
                            String skillName = (String) ((TextView) v).getText();
                            EditText et = (EditText) searchView.findViewById(R.id.et_activity_search_association);
                            et.setText(skillName);
                            et.setTextColor(Color.BLACK);
                        //展示搜索结果的页面
                          //  LogKit.d("展示搜索结果的页面");
                            showSearchReasult();
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

    private void initListener() {
        //监听搜索首页
        ((ListView)searchView.findViewById(R.id.lv_activity_search_second_skilllable_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        //三个对应搜索的推出按钮监听
        searchView.findViewById(R.id.ib_subSearch_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //清除当前布局，返回前一页的布局
               // LogKit.d("search_back");
            mActivitySearchBinding.flSearch.removeView(searchView);
            }
        });
    }
    //搜索需求
    public void searchNeed(View v) {
   // LogKit.d("SearchNeed");
        showView("热搜需求");
        initView();
        SearchType = SearchNeed;
    }

    //搜索服务
    public void searchService(View v) {
       // LogKit.d("SearchService");
        showView("热搜服务");
        initView();
        SearchType = SearchService;
    }

    //搜索人
    public void searchPerson(View v){
      //  LogKit.d("SearchPerson");
        showView("搜人");
        initView();
        SearchType = SearchPerson;
    }

    private void showView(String title) {
        searchView = View.inflate(CommonUtils.getContext(), R.layout.search_activity_hot_service, null);
        ((TextView) searchView.findViewById(R.id.tv_search_title)).setText(title);
        mActivitySearchBinding.flSearch.addView(searchView);
    }

    //退出当前的页面
    public void back(View v){
      //  LogKit.d("back");
        CommonUtils.getCurrentActivity().finish();
    }

}
