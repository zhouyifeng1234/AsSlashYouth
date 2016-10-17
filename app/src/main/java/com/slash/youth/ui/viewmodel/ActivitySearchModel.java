package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.busline.BusLineQuery;
import com.blankj.utilcode.utils.RegexUtils;
import com.blankj.utilcode.utils.ThreadPoolUtils;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.databinding.SearchActivityHotServiceBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.SearchAssociativeBean;
import com.slash.youth.domain.SearchNeedItem;
import com.slash.youth.domain.SearchPersonItem;
import com.slash.youth.domain.SearchServiceItem;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.PhoneLogin;
import com.slash.youth.http.protocol.SearchAllProtocol;
import com.slash.youth.http.protocol.SearchAssociativeProtocol;
import com.slash.youth.ui.adapter.PagerSearchItemAdapter;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.StringUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable {
    private ActivitySearchBinding mActivitySearchBinding;
    public static boolean isShowThreePage = false;
    private SearchHistoryListAdapter adapter;
    public static int searchType = 0;
    private int backType = 0;
    private int searchNeed = 1;
    private int searchService = 2;
    private int searchPerson = 3;
    private SearchActivityHotServiceBinding searchActivityHotServiceBinding;
    private SearchActivityHotServiceModel searchActivityHotServiceModel;
    private PagerSearchItemAdapter pagerSearchItemAdapter;
    private String searchContent1 = "";
    private ArrayList<String> search_contentlist = new ArrayList<>();
    private ArrayList<String> searchHistoryList;
    private File externalCacheDir = CommonUtils.getApplication().getExternalCacheDir();
    private File file = new File(externalCacheDir, "history.text");
    private ListView listView;
    private String tvSearchright;
    private SearchNeedResultTabBinding searchNeedResultTabBinding;
    private SearchNeedResultTabModel searchNeedResultTabModel;

    public ActivitySearchModel(ActivitySearchBinding activitySearchBinding) {
        this.mActivitySearchBinding = activitySearchBinding;
        //搜索框的监听，zss
        searchListener();
    }

    //zss,点击直接搜索
    public void etSearch(View v) {

     /*   if (mActivitySearchBinding.rlSearchMain != null) {
            mActivitySearchBinding.rlSearchMain.setVisibility(View.GONE);
        }*/
        if (searchActivityHotServiceBinding != null) {
            mActivitySearchBinding.flSearchFirst.removeView(searchActivityHotServiceBinding.getRoot());
        }

        //有内在的bug等待处理
        if (ActivitySearchModel.isShowThreePage){
            mActivitySearchBinding.flSearchFirst.removeView(searchActivityHotServiceModel.getSearchNeedResultTabModel().getmSearchNeedResultTabBinding().getRoot());
    }

        mActivitySearchBinding.llSearchHistory.setVisibility(View.VISIBLE);
        //加载搜索框历史的数据
        initSearchHistoryData();

    }

    //点击显示对话框
    public void showDialog(View v){
       // LogKit.d("显示对话框");
        //创建AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommonUtils.getCurrentActivity());
        //数据绑定填充视图
        DialogSearchCleanBinding dialogSearchCleanBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_search_clean, null, false);
        //创建数据模型
        DialogSearchCleanModel dialogSearchCleanModel = new DialogSearchCleanModel(dialogSearchCleanBinding,mActivitySearchBinding);
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
        switch (searchType){
            case 0:
                CommonUtils.getCurrentActivity().finish();
                break;
            case 1:
            case 2:
            case 3:
                if(searchActivityHotServiceBinding!=null){
                    mActivitySearchBinding.flSearchFirst.removeView(searchActivityHotServiceBinding.getRoot());
                }
                mActivitySearchBinding.llSearchHistory.setVisibility(View.GONE);
                mActivitySearchBinding.rlSearchMain.setVisibility(View.VISIBLE);
                searchType = 0;
                 backType = 1;
                break;
            case 4:
                SearchNeedResultTabBinding searchNeedResultTabBinding = searchActivityHotServiceModel.getSearchNeedResultTabBinding();
                mActivitySearchBinding.flSearchFirst.removeView(searchNeedResultTabBinding.getRoot());
                searchType =1;
                break;
            case 5:
                mActivitySearchBinding.rlSearchMain.setVisibility(View.VISIBLE);
                mActivitySearchBinding.llSearchHistory.setVisibility(View.GONE);
                searchType=0;
                break;
            case 6:
                SearchNeedResultTabBinding resultTabBinding = searchNeedResultTabModel.getmSearchNeedResultTabBinding();
                mActivitySearchBinding.flSearchFirst.removeView(resultTabBinding.getRoot());
                backType = 1;
                searchType =1;
                break;
            case 7:
                mActivitySearchBinding.flSearchFirst.removeView(listView);
                backType = 1;
                searchType = 0;
                break;
        }
        //只要按了返回键就会全部清空
        if(mActivitySearchBinding.etActivitySearchAssociation.getText()!=null){
            mActivitySearchBinding.etActivitySearchAssociation.setText(null);
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

                if(backType == 1){
                    mActivitySearchBinding.llSearchHistory.setVisibility(View.GONE);
                    mActivitySearchBinding.rlSearchMain.setVisibility(View.VISIBLE);
                    searchType = 0;
                    return;
                }else {
                    //下面的首页隐藏,显示搜索
                    mActivitySearchBinding.rlSearchMain.setVisibility(View.GONE);
                    mActivitySearchBinding.llSearchHistory.setVisibility(View.VISIBLE);
                }
            }

            //文本改变之后,输入的内容要保存到本地
            @Override
            public void afterTextChanged(Editable s) {
                //获取搜索内容
                //分三部分
                //长度为0：打开历史
                //长度为1并且非汉字；不动
                //长度大于2：搜索
                searchContent1 = s.toString();
                if(searchContent1.length()==0){
                    showHistory();
                }else if (searchContent1.length()==1){
                    if(RegexUtils.isChz(searchContent1)){

                        setSearchContentData();
                    }else {
                        //输入了一个非汉字的字符
                        ToastUtils.shortToast("输入信息太少，请重新输入");
                    }
                }else if(searchContent1.length()>=2){
                    setSearchContentData();
                }
                //当输入框里面不等于0就是有字，出现图片
                setDrawable(searchContent1);
            }
        });

        //设置搜索的数据，zss
        setSearchContentData();
        //点击搜索，显示搜索的结果 zss,二级页面是取消按钮
        setClickSearch();
        //叉叉图片点击事件
        mActivitySearchBinding.searchIvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            mActivitySearchBinding.etActivitySearchAssociation.setText(null);
            mActivitySearchBinding.searchIvDelete.setVisibility(View.INVISIBLE);
            }
        });
    }
    //设置搜索框的叉叉的图片
    private void setDrawable(String text) {
        if (text.length() < 1) {
            mActivitySearchBinding.searchIvDelete.setVisibility(View.INVISIBLE);
        }else {
            mActivitySearchBinding.searchIvDelete.setVisibility(View.VISIBLE);
        }
    }

    private void setClickSearch() {
        mActivitySearchBinding.tvSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            tvSearchright = mActivitySearchBinding.tvSearchResult.getText().toString();
                    //显示结果页面
                String text=mActivitySearchBinding.etActivitySearchAssociation.getText().toString();
                if(text.length()==0){
                    ToastUtils.shortToast("请您输入搜索内容");
                    mActivitySearchBinding.etActivitySearchAssociation.setFocusable(true);
                    mActivitySearchBinding.etActivitySearchAssociation.setFocusableInTouchMode(true);
                    mActivitySearchBinding.etActivitySearchAssociation.requestFocus();
                    InputMethodManager inputManager = (InputMethodManager) CommonUtils.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.showSoftInput(mActivitySearchBinding.etActivitySearchAssociation, 0);

                } else if (text.length()==1){
                    if(StringUtils.isSearchContent(text)) {
                        if (RegexUtils.isChz(searchContent1)) {
                            showSearchAllReasultView(text);
                        } else {
                            //输入了一个非汉字的字符
                            ToastUtils.shortToast("输入信息太少，请重新输入");
                        }
                    }
                } else if (text.length()>=2){
                    if(StringUtils.isSearchContent(text)) {
                        //联网搜索,并且根据字符搜索
                        saveHistory(text);
                        //TODO
                        // showSearchAllReasultView(text);
                    }else{
                        ToastUtils.shortToast("输入有效字符");
                    }
                }
            }
        });
    }

    private void saveHistory(String text) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file,true));
            bw.write(text);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showHistory() {
        initSearchHistoryData();
    }
    //zss 直接搜索结果
    private void showSearchAllReasultView(String text) {
     mActivitySearchBinding.llSearchHistory.setVisibility(View.INVISIBLE);
    listView = new ListView(CommonUtils.getContext());
    listView.setBackgroundColor(Color.parseColor("#e5e5e5"));
    SearchAllProtocol searchAllProtocol = new SearchAllProtocol(text);
    searchAllProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SearchAllBean>() {
        @Override
        public void execute(SearchAllBean dataBean) {
            SearchAllBean.DataBean data = dataBean.getData();
            ArrayList<SearchAllBean.DataBean.DemandListBean> demandList = data.getDemandList();
            ArrayList<?> serviceList = data.getServiceList();
            ArrayList<SearchAllBean.DataBean.UserListBean> userList = data.getUserList();
            pagerSearchItemAdapter = new PagerSearchItemAdapter(demandList, serviceList, userList, new PagerSearchItemAdapter.OnClickMoreListener() {
                @Override
                public void onClickMore(int btn) {
                    mActivitySearchBinding.flSearchFirst.removeView(listView);
                    switch (btn){
                        case needMoreBtn://点击更多按钮，跳转到需求页面
                        SpUtils.setString("searchType","热搜需求");
                            break;
                        case serviceMoreBtn:
                        SpUtils.setString("searchType","热搜服务");
                            break;
                        case persionMoreBtn:
                        SpUtils.setString("searchType","搜人");
                            break;
                    }
                    mActivitySearchBinding.etActivitySearchAssociation.setText(null);
                    showMoreSearch();
                }
            });
            listView.setAdapter(pagerSearchItemAdapter);
            mActivitySearchBinding.flSearchFirst.addView(listView);
            ActivitySearchModel.searchType = 7;
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("executeResultError"+result);
        }
    });
    }

    private void showMoreSearch() {
        searchNeedResultTabBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_need_result_tab, null, false);
        searchNeedResultTabModel = new SearchNeedResultTabModel(searchNeedResultTabBinding);
        searchNeedResultTabBinding.setSearchNeedResultTabModel(searchNeedResultTabModel);
        mActivitySearchBinding.flSearchFirst.addView(searchNeedResultTabBinding.getRoot());
        ActivitySearchModel.searchType = 6;
    }

    private   void initSearchHistoryData() {
        //存储集合
        searchHistoryList = new ArrayList<String>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line= br.readLine())!=null){
                if(TextUtils.isEmpty(line)){
                    continue;
                }
                searchHistoryList .add(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter= new SearchHistoryListAdapter(searchHistoryList,true,mActivitySearchBinding);
        mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);
    }

    private void setSearchContentData() {
        final SearchAssociativeProtocol searchAssociativeProtocol = new SearchAssociativeProtocol(searchContent1);
        search_contentlist.clear();
        searchAssociativeProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SearchAssociativeBean>() {
            @Override
            public void execute(SearchAssociativeBean dataBean) {

                ArrayList<SearchAssociativeBean.TagBean> list = dataBean.data.list;
                for (SearchAssociativeBean.TagBean tagBean : list) {
                    //遍历去重复
                    if(!search_contentlist.contains(tagBean.tag)){
                        search_contentlist.add(tagBean.tag);
                    }
                }
                adapter= new SearchHistoryListAdapter(search_contentlist,false,mActivitySearchBinding);
                mActivitySearchBinding.lvLvSearchcontent.setAdapter(adapter);
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("executeResultError:"+result);
            }
        });
    }

    //搜索需求
    public void searchNeed(View v) {
        showView("热搜需求");
        SpUtils.setString("searchType","热搜需求");
        searchType = searchNeed;
    }

    //搜索服务
    public void searchService(View v) {
        showView("热搜服务");
        SpUtils.setString("searchType","热搜服务");
        searchType = searchService;
    }

    //搜索人
    public void searchPerson(View v){
        showView("搜人");
        SpUtils.setString("searchType","搜人");
        searchType = searchPerson;
    }

    public void showView(String title) {
        searchActivityHotServiceBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_activity_hot_service, null, false);
        searchActivityHotServiceModel = new SearchActivityHotServiceModel(searchActivityHotServiceBinding,mActivitySearchBinding);
        searchActivityHotServiceBinding.setSearchActivityHotServiceModel(searchActivityHotServiceModel);
        mActivitySearchBinding.flSearchFirst.addView(searchActivityHotServiceBinding.getRoot());
        searchActivityHotServiceBinding.tvSearchTitle.setText(title);
        searchType = 1;
        ActivitySearchModel.isShowThreePage = false;
    }
}
