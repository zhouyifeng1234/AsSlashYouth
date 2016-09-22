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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.DialogHomeSubscribeBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.domain.DemandBean;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.ui.adapter.PagerHomeDemandtAdapter;
import com.slash.youth.ui.adapter.SearchContentListAdapter;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class ActivitySearchModel extends BaseObservable implements View.OnClickListener {
    ActivitySearchBinding mActivitySearchBinding;
    private boolean isShow;
    private  SearchContentListAdapter adapter;
    private ListView lv_showSearchResult;


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
        //点击搜索，显示搜索的结果 zss
        mActivitySearchBinding.tvSearchResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //显示结果页面
          //  LogKit.d("显示搜索结果页面");
          showSearchReasult();
            }
        });
    }
    //zss 显示搜索页面
    private void showSearchReasult() {
        //先把当前的页面隐藏
        mActivitySearchBinding.llActivityHotService.setVisibility(View.GONE);
        //添加一个布局，获取布局的view
        View view = getView(R.layout.include_search_result);
        lv_showSearchResult = (ListView) view.findViewById(R.id.lv_showSearchResult);
        //获取listview要展示的数据
        setSearchResultData();
        mActivitySearchBinding.flSearchResult.addView(view);
        mActivitySearchBinding.flSearchResult.setVisibility(View.VISIBLE);
        //设置指示器的点击事件,三个不同的指示器的监听事件
        view.findViewById(R.id.rl_tab_line).setOnClickListener(this);
        view.findViewById(R.id.rl_tab_user).setOnClickListener(this);
        view.findViewById(R.id.rl_tab_time).setOnClickListener(this);

    }
    //获取view
    private View getView(int layout) {
        mActivitySearchBinding.flSearchResult.removeAllViews();
        return View.inflate(CommonUtils.getContext(),layout,null);
    }

    private View getView(boolean isremove,int layout) {
        if (isremove) {
            mActivitySearchBinding.flSearchResult.removeAllViews();
        }
        return View.inflate(CommonUtils.getContext(),layout,null);
    }



    //zss 设置搜索结果数据
    private void setSearchResultData() {

        //假数据，真实数据从服务端接口获取
        ArrayList<DemandBean> listDemand = new ArrayList<DemandBean>();
        //集合里对象信息也要重新设置
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        listDemand.add(new DemandBean());
        lv_showSearchResult.setAdapter(new PagerHomeDemandtAdapter(listDemand));
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
    //zss 指示器的点击事件
    private  boolean isClick ;
    @Override
    public void onClick(View v) {
    switch (v.getId()){
        //线上用户
        case R.id.rl_tab_line:
            setImageView(v,R.id.iv_line_icon,R.mipmap.free_play,R.mipmap.free_pay_jihuo);
            //展示线上的选择页面
           // getView(false,);


            break;
        //认证用户
        case R.id.rl_tab_user:
            setImageView(v,R.id.iv_user_icon,R.mipmap.free_play,R.mipmap.free_pay_jihuo);
            break;
        //时间
        case R.id.rl_tab_time:
            setImageView(v,R.id.iv_time_icon,R.mipmap.xia,R.mipmap.shang_icon);
            break;

        }
    }
    //指示器设置图片,zss
    private void setImageView(View v,int iv1,int iv2,int iv3) {
        if (isClick){
            ((ImageView)v.findViewById(iv1)).setImageResource(iv2);
        }else {
            ((ImageView)v.findViewById(iv1)).setImageResource(iv3);
        }
        isClick = !isClick;
    }



}
