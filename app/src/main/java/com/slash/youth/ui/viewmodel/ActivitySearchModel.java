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
import android.widget.ListView;
import android.widget.TextView;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.SearchNeedItem;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
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
    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;
    private SearchHistoryListAdapter adapter;
    private SearchNeedResultTabBinding searchNeedResultTabBinding;
    private View searchView;
    private int  searchType =0;
    private int searchType2;
    private int  searchNeed = 1;
    private int  searchService = 2;
    private int  searchPerson = 3;
    private int  searchResult = 4;
    private ArrayList<SearchNeedItem> needArrayList;

    public void setSearchNeedResultTabBinding(SearchNeedResultTabBinding searchNeedResultTabBinding) {
        this.searchNeedResultTabBinding = searchNeedResultTabBinding;
    }

    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding) {
        this.mActivitySearchBinding = activitySearchBinding;
        //搜索框的监听，zss
        searchListener();
    }

    //zss,点击直接搜索
    public void etSearch(View v){
       // LogKit.d("点击搜索框，出现历史纪录");
        mActivitySearchBinding.rlSearchMain.setVisibility(View.GONE);
        mActivitySearchBinding.llSearchHistory.setVisibility(View.VISIBLE);
        //加载搜索框历史的数据
        initSearchHistoryData();
        //删除单条和清除所有
        cleanAll(true);
    }

    //zss 判断显示清除所有历史
    private void cleanAll(boolean isShow) {
        mActivitySearchBinding.tvCleanAll.setVisibility(isShow?View.VISIBLE:View.GONE);
    }

    //点击显示对话框
    public void showDialog(View v){
        LogKit.d("显示对话框");
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
        dialogParams.height = CommonUtils.dip2px(200);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);//dialog的灰度
//        dialogBuilder.show();
    }

    //zss,退出当前的页面
    public void back(View v){
        // LogKit.d("back");
        switch (searchType){
            case 0:
                CommonUtils.getCurrentActivity().finish();
                break;
            case 1:
            case 2:
            case 3:
                mActivitySearchBinding.flSearchFirst.removeView(searchView);
                mActivitySearchBinding.rlSearchMain.setVisibility(View.VISIBLE);
                mActivitySearchBinding.tvSearchResult.setText("搜索");
                searchType = 0;
                break;
            case 4:
                //直接退出本页面
                mActivitySearchBinding.flSearchFirst.removeView(searchNeedResultTabBinding.getRoot());
                mActivitySearchBinding.flSearchFirst.addView(searchView);
                searchType = searchNeed;
                break;
        }
    }

      //zss.监听框的搜索
    private void searchListener() {
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

            //文本改变之后,输入的内容要保存到本地
            @Override
            public void afterTextChanged(Editable s) {
                //获取搜索内容
                if (s.length() != 0) {
                    String searchHistory = s.toString();
                    //searchHistoryList.add(searchHistory);
                   /*  try {
                        bw = new BufferedWriter(new FileWriter(file));
                        for(int i=0;i<searchHistoryList.size();i++){
                            bw.write(searchHistoryList.get(i));
                            bw.newLine();
                        }
                        bw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }*/
                }
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
        needArrayList = new ArrayList<>();

        //1
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        //2
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        //3
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        needArrayList.add(new SearchNeedItem());
        PagerSearchItemAdapter pagerSearchItemAdapter = new PagerSearchItemAdapter(needArrayList);
        listView.setAdapter(pagerSearchItemAdapter);
         mActivitySearchBinding.flSearchFirst.addView(listView);
    }

    private void initSearchHistoryData() {
         ArrayList<String> searchHistoryList = new ArrayList<String>();//存储集合
            searchHistoryList.add("独墅湖高教区");
            searchHistoryList.add("翰邻里中心2");
            searchHistoryList.add("克苏勒苏3");
            searchHistoryList.add("克苏勒苏4");
            searchHistoryList.add("克苏勒苏5");

      /*  try {
            br = new BufferedReader(new FileReader(file));
            String text = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        adapter= new SearchHistoryListAdapter(searchHistoryList,true);
        mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);
    }



    private void setSearchContentData(boolean isShow) {
        //TODO 联想搜索的内容，假的数据从服务端获取，zss
        ArrayList<String> search_contentlist = new ArrayList<>();
        search_contentlist.add("ui设计师");
        search_contentlist.add("uiux设计师");
        search_contentlist.add("uii设计师");
        search_contentlist.add("uii设计师");
        search_contentlist.add("uii设计师");
        adapter= new SearchHistoryListAdapter(search_contentlist,isShow);
        mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);
    }

    //搜索需求
    public void searchNeed(View v) {
    //LogKit.d("SearchNeed");
        showView("热搜需求");
      //  initView();
        searchType = searchNeed;
    }

    //搜索服务
    public void searchService(View v) {
       // LogKit.d("SearchService");
        showView("热搜服务");
      //  initView();
        searchType = searchService;
    }

    //搜索人
    public void searchPerson(View v){
      //  LogKit.d("SearchPerson");
        showView("搜人");
       // initView();
        searchType = searchPerson;
    }

    private void showView(String title) {
        SearchActivityHotServiceBinding searchActivityHotServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_activity_hot_service, null, false);
        SearchActivityHotServiceModel searchActivityHotServiceModel = new SearchActivityHotServiceModel(searchActivityHotServiceBinding);
        searchActivityHotServiceBinding.setSearchActivityHotServiceModel(searchActivityHotServiceModel);
        mActivitySearchBinding.flSearchFirst.addView(searchActivityHotServiceBinding.getRoot());
        searchActivityHotServiceBinding.tvSearchTitle.setText(title);
        mActivitySearchBinding.tvSearchResult.setText("取消");
    }

    /*//加载布局
    private void initView() {
        //zss  先展示首页
        searchView.findViewById(R.id.lv_activity_search_second_skilllable_list).setVerticalScrollBarEnabled(false);
        searchView.findViewById(R.id.sv_activity_search_third_skilllabel).setVerticalScrollBarEnabled(false);
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
        ((ListView)searchView.findViewById(R.id.lv_activity_search_second_skilllable_list)).setAdapter(new SubscribeSecondSkilllabelAdapter(listSkilllabel));
        SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
        searchView.findViewById(R.id.lv_activity_search_second_skilllable_list).post(new Runnable() {
            @Override
            public void run() {
                View lvActivitySubscribeSecondSkilllableListFirstChild = ((ListView) searchView.findViewById(R.id.lv_activity_search_second_skilllable_list)).getChildAt(0);
                LogKit.d(lvActivitySubscribeSecondSkilllableListFirstChild + "");
                SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();//获取他的tag
                lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;//tag建立数据模型
            }
        });
        setThirdSkilllabelData();
    }
    *//**
     * 根据选择的二级标签，显示对应的三级标签
     *//*
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
                int scrollViewWidth =searchView.findViewById(R.id.ll_activity_search_third_skilllabel).getMeasuredWidth();
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
                            ((LinearLayout) searchView.findViewById(R.id.ll_activity_search_third_skilllabel)).addView(llSkilllabelLine);
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
                            //展示搜索结果的页面
                            //  LogKit.d("展示搜索结果的页面");
                            showSearchReasult();
                            mActivitySearchBinding.tvSearchResult.setText("搜索");
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
        ((ListView)searchView.findViewById(R.id.lv_activity_search_second_skilllable_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //监听搜索首页
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
    }

    //zss 显示搜索页面
    private void showSearchReasult()  {
        mActivitySearchBinding.flSearchFirst.removeView(searchView);
       mActivitySearchBinding.rlSearchMain.setVisibility(View.GONE);
        //先把当前的页面隐藏,展示需要的布局
        switch (searchType){
            case 1:
            case 2:
                mActivitySearchBinding.flSearchFirst.addView(searchNeedResultTabBinding.getRoot());
                break;
            case 3:
                searchNeedResultTabBinding.llSearchTabHead.setVisibility(View.GONE);
                searchNeedResultTabBinding.lvShowSearchResult.setVisibility(View.GONE);
                searchNeedResultTabBinding.llSearchTab2.setVisibility(View.VISIBLE);
                searchNeedResultTabBinding.lvShowSearchPerson.setVisibility(View.VISIBLE);
                mActivitySearchBinding.flSearchFirst.addView(searchNeedResultTabBinding.getRoot());
                break;
        }
        searchType = searchResult;
    }*/

}
