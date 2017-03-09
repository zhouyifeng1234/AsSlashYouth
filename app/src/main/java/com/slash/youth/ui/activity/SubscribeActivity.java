package com.slash.youth.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySubscribeBinding;
import com.slash.youth.databinding.DialogCustomSkilllabelBinding;
import com.slash.youth.domain.SkillLabelAllBean;
import com.slash.youth.domain.SkillLabelBean;
import com.slash.youth.domain.SkillLabelGetBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CreateSkillLabelProtocol;
import com.slash.youth.http.protocol.DeleteSkillLabelProtocol;
import com.slash.youth.http.protocol.SkillLabelAllProtocol;
import com.slash.youth.http.protocol.SkillLabelGetProtocol;
import com.slash.youth.ui.activity.base.BaseActivity;
import com.slash.youth.ui.adapter.SubscribeSecondSkilllabelAdapter;
import com.slash.youth.ui.holder.SubscribeSecondSkilllabelHolder;
import com.slash.youth.ui.viewmodel.ActivitySubscribeModel;
import com.slash.youth.ui.viewmodel.DialogCustomSkillLabelModel;
import com.slash.youth.ui.viewmodel.ItemSubscribeSecondSkilllabelModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.NetUtils;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zss on 2016/9/8.
 */
public class SubscribeActivity extends BaseActivity {
    public ActivitySubscribeBinding mActivitySubscribeBinding;
    private LinearLayout mLlCheckedLabels;
    public String checkedFirstLabel;
    public String checkedSecondLabel;
    private String titleText = "请选择";
    private String tabTitle = "已选项";
    private ArrayList<SkillLabelBean> listThirdSkilllabelName = new ArrayList();
    private ArrayList<SkillLabelBean> listFirstSkilllabelName = new ArrayList();
    private ArrayList<SkillLabelBean> listThirdUserCustomSkilllabelName = new ArrayList();
    private DialogCustomSkillLabelModel dialogCustomSkillLabelModel;
    public LinearLayout llSkilllabelLine;
    private int index;
    private int lineCount = 0;
    private int tvThirdSkilllabelWidth;
    private int newSkillLabelLineWidth;
    private int scrollViewWidth;
    private int skillLabelLineWidth = 0;
    private int labelRightMargin;
    private LinearLayout linearLayout;
    private LinearLayout layout;
    private String labelName;
    private ActivitySubscribeModel activitySubscribeModel;
    private ArrayList<SkillLabelBean> listSkilllabel = new ArrayList<SkillLabelBean>();
    private ArrayList<SkillLabelBean> commnSkilllabel = new ArrayList<SkillLabelBean>();
    private ArrayList<SkillLabelBean> commnThirdSkillLabel = new ArrayList<SkillLabelBean>();
    private SubscribeSecondSkilllabelAdapter subscribeSecondSkilllabelAdapter;
    private int secondId;
    private int uid;
    private SkillLabelBean skillLabelBean;
    private ImageView imageView;
    private ImageView imageViewAdd;
    public static int clickCount = 0;//默认是0
    private Intent intent;
    private boolean isEditor = false;
    private int no_custom_f1;
    private int no_custom_f2;
    private SkillLabelBean clickSecondSkillLabelBean;
    private String industry;
    private String direction;
    private SkillLabelBean secondSkillLabelBean;
    private SkillLabelBean industrySkillLabelBean;
    private boolean isAddSkill = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivitySubscribeBinding = DataBindingUtil.setContentView(this, R.layout.activity_subscribe);
        intent = getIntent();
        isEditor = intent.getBooleanExtra("isEditor", false);
        if (isEditor) {
            mActivitySubscribeBinding.tvSubscribeTitle.setText(titleText);
//            mActivitySubscribeBinding.tvTitleTwo.setText(tabTitle);//这里都显示“已选标签”，所以不需要判断设置
        }
        activitySubscribeModel = new ActivitySubscribeModel(mActivitySubscribeBinding, this, isEditor, isAddSkill);
        mActivitySubscribeBinding.setActivitySubscribeModel(activitySubscribeModel);

        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setVerticalScrollBarEnabled(false);
        mActivitySubscribeBinding.svActivitySubscribeThirdSkilllabel.setVerticalScrollBarEnabled(false);
        mLlCheckedLabels = mActivitySubscribeBinding.llActivitySubscribeCheckedLabels;
        mLlCheckedLabels.removeAllViews();

        int addSkillTemplte = intent.getIntExtra("addSkillTemplte", -1);
        if (addSkillTemplte == 0) {
            mActivitySubscribeBinding.tvSubscribeTitle.setText(titleText);
        }

        initData();
        initListener();
    }

    private void initData() {
        final ArrayList<String> addedTagsName = getIntent().getStringArrayListExtra("addedTagsName");
        final ArrayList<String> addedTags = getIntent().getStringArrayListExtra("addedTags");
        if (addedTagsName != null) {
            mLlCheckedLabels.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < addedTagsName.size(); i++) {
                        String labelTag = addedTags.get(i);
                        String labelName = addedTagsName.get(i);
                        addCheckedLabels(labelTag, labelName);
                    }
                }
            });
            //前面集合传递过来的技能标签数量
            int size = addedTagsName.size();
            clickCount = size;
        }

        //假设能够读取网络数据
        if (NetUtils.isNetworkAvailable(CommonUtils.getApplication())) {
            //从服务端借口获取非用户定义的接口
            SkillLabelAllProtocol skillLabelAllProtocol = new SkillLabelAllProtocol();
            skillLabelAllProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<ArrayList<SkillLabelAllBean>>() {
                @Override
                public void execute(ArrayList<SkillLabelAllBean> arrayList) {
                    //先保存在本地
                    saveDate2Local("skillLabel", arrayList);
                    //对集合分类
                    getSkillLabelAllArrayList(arrayList);
                    postThridSkillLabel();
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.d("result : " + result);
                }
            });
            //从服务端借口获取用户自己定义的标签
            SkillLabelGetProtocol skillLabelGetProtocol = new SkillLabelGetProtocol();
            skillLabelGetProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SkillLabelGetBean>() {
                @Override
                public void execute(SkillLabelGetBean dataBean) {
                    int rescode = dataBean.getRescode();
                    if (GlobalConstants.Rescode.RES_SUCCESS == rescode) {
                        ArrayList<SkillLabelGetBean.DataBean> data = (ArrayList<SkillLabelGetBean.DataBean>) dataBean.getData();
                        saveDate2Local("userSkillLabel", data);
                        getUserSkillLabelArrayList(data);
                        postThridSkillLabel();
                    } else {
                        LogKit.d("rescode :" + rescode);
                    }
                }

                @Override
                public void executeResultError(String result) {
                    LogKit.d("result :" + result);
                }
            });
        } else {
            ArrayList<SkillLabelAllBean> arrayList = getDateFromLocal("skillLabel");
            getSkillLabelAllArrayList(arrayList);
            ArrayList<SkillLabelGetBean.DataBean> data = getDateFromLocal("userSkillLabel");
            getUserSkillLabelArrayList(data);
            postThridSkillLabel();
        }
        //一级标签
        industry = getIntent().getStringExtra("industry");
        //二级标签
        direction = getIntent().getStringExtra("direction");

        checkedFirstLabel = industry;
        checkedSecondLabel = direction;
    }

    //获取用户自定义标签的集合
    private void getUserSkillLabelArrayList(ArrayList<SkillLabelGetBean.DataBean> data) {
        listThirdUserCustomSkilllabelName.clear();
        for (SkillLabelGetBean.DataBean bean : data) {
            //用户的id,不同用户不同id
            uid = bean.getUid();
            int userf1 = bean.getF1();
            int userf2 = bean.getF2();
            int userid = bean.getId();
            String userSkillLabel = bean.getTags();

            if (!TextUtils.isEmpty(userSkillLabel)) {
                if (userSkillLabel.contains("_")) {
                    String[] thirdSkillNames = userSkillLabel.split("_");
                    for (String thirdSkillName : thirdSkillNames) {
                        if (thirdSkillName != null) {
                            skillLabelBean = new SkillLabelBean(userf1, userf2, userid, thirdSkillName);
                            listThirdUserCustomSkilllabelName.add(skillLabelBean);
                        }
                    }
                } else {//不包含，就是一个标签
                    skillLabelBean = new SkillLabelBean(userf1, userf2, userid, userSkillLabel);
                    listThirdUserCustomSkilllabelName.add(skillLabelBean);
                }
            }
        }
    }

    //对集合分类
    private void getSkillLabelAllArrayList(ArrayList<SkillLabelAllBean> arrayList) {
        for (SkillLabelAllBean skillLabelAllBean : arrayList) {
            final int f1 = skillLabelAllBean.getF1();
            int f2 = skillLabelAllBean.getF2();
            int id = skillLabelAllBean.getId();
            String tag = skillLabelAllBean.getTag();

            //一级标签集合
            if (f1 == 0 && f2 == 0) {
                SkillLabelBean skillLabelBean = new SkillLabelBean(f1, f2, id, tag);
                listFirstSkilllabelName.add(skillLabelBean);
                listener.OnListener(listFirstSkilllabelName);
            }

            //二级标签集合
            if (f1 != 0 && f2 == 0) {
                listSkilllabel.add(new SkillLabelBean(f1, f2, id, tag));
            }

            //三级标签集合
            if (f1 != 0 && f2 != 0) {
                listThirdSkilllabelName.add(new SkillLabelBean(f1, f2, id, tag));
            }
        }
        //获取默认的一级id,展示二级
        for (int i = 0; i < listFirstSkilllabelName.size(); i++) {
            String tag = listFirstSkilllabelName.get(i).getTag();
            if (tag.equals(industry)) {
                industrySkillLabelBean = listFirstSkilllabelName.get(i);
                mActivitySubscribeBinding.tvFirstSkillLabelTitle.setText(industry);
                ActivitySubscribeModel.mNpChooseMainLabels.setValue(i);
            }
        }
        int firstId = 0;
        if (industrySkillLabelBean == null) {
            firstId = listFirstSkilllabelName.get(0).getId();
        } else {
            firstId = industrySkillLabelBean.getId();
        }

        //第一次展示页面，二级默认为设计页面
        getCommnSkillLabel(firstId);
        subscribeSecondSkilllabelAdapter = new SubscribeSecondSkilllabelAdapter(commnSkilllabel);
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setAdapter(subscribeSecondSkilllabelAdapter);
        // postSecondSkillLabel();

        //第一次展示页面，三级默认为品牌设计页面
        commnThirdSkillLabel.clear();
        for (int i = 0; i < commnSkilllabel.size(); i++) {
            String tag = commnSkilllabel.get(i).getTag();
            if (tag.equals(direction)) {
                secondSkillLabelBean = commnSkilllabel.get(i);
                SubscribeSecondSkilllabelHolder.clickItemPosition = i;
            }
        }

        postSecondSkillLabel();
        int f1;
        if (secondSkillLabelBean == null) {
            secondId = commnSkilllabel.get(0).getId();
            f1 = commnSkilllabel.get(0).getF1();
        } else {
            secondId = secondSkillLabelBean.getId();
            f1 = secondSkillLabelBean.getF1();
        }

        getCommnThirdSkillLabel(f1, secondId);
    }

    private void postSecondSkillLabel() {
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.post(new Runnable() {
            @Override
            public void run() {
                View lvActivitySubscribeSecondSkilllableListFirstChild = mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.getChildAt(SubscribeSecondSkilllabelHolder.clickItemPosition);
                SubscribeSecondSkilllabelHolder tag = (SubscribeSecondSkilllabelHolder) lvActivitySubscribeSecondSkilllableListFirstChild.getTag();
                lastClickItemModel = tag.mItemSubscribeSecondSkilllabelModel;
            }
        });
    }

    private ItemSubscribeSecondSkilllabelModel lastClickItemModel = null;

    private void initListener() {
        //二级条目点击事件
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int size = listCheckedLabelName.size();
                if (size != 0) {
                    clickSecondSkillLabelBean = commnSkilllabel.get(position);
                    checkedSecondLabel = clickSecondSkillLabelBean.getTag();
                    if (checkedSecondLabel.equals(direction)) {
                        chooseThridSkillLabelLiist(view, position);
                    } else {
                        ToastUtils.shortCenterToast("只能选择同一类型的标签");
                    }
                } else {//为0
                    chooseThridSkillLabelLiist(view, position);
                }
            }
        });

        //一级标签根据新的ID跳转到二级
        activitySubscribeModel.setOnOkChooseMainLabelListener(new ActivitySubscribeModel.OnOkChooseMainLabelListener() {
            @Override
            public void OnOkChooseMainLabelListener(int tagId) {
                int size = listCheckedLabelName.size();
                SkillLabelBean skillLabelBean = listFirstSkilllabelName.get(tagId);
                String tag = skillLabelBean.getTag();
                if (size != 0) {
                    if (tag.equals(industry)) {
                        //这里不需要进行操作
                        //一级显示
//                        mActivitySubscribeBinding.tvFirstSkillLabelTitle.setText(checkedFirstLabel);
                        //选择二级
//                        chooseSecondSkillLabelList(skillLabelBean);
                    } else {
                        ToastUtils.shortCenterToast("只能选择同一类型的标签");
                    }
                } else {
                    SubscribeSecondSkilllabelHolder.clickItemPosition = 0;
                    chooseSecondSkillLabelList(skillLabelBean);
                    mActivitySubscribeBinding.tvFirstSkillLabelTitle.setText(checkedFirstLabel);
                }
            }
        });
    }

    private void chooseSecondSkillLabelList(SkillLabelBean skillLabelBean) {
        int firstId = skillLabelBean.getId();
        //默认的一级标签的id,跳转到二级
        getCommnSkillLabel(firstId);
        //默认是0
        SkillLabelBean secondSkillLabelList = commnSkilllabel.get(0);
        checkedSecondLabel = secondSkillLabelList.getTag();
        subscribeSecondSkilllabelAdapter = new SubscribeSecondSkilllabelAdapter(commnSkilllabel);
        mActivitySubscribeBinding.lvActivitySubscribeSecondSkilllableList.setAdapter(subscribeSecondSkilllabelAdapter);
        postSecondSkillLabel();
        //并默认跳转到对应的三级
        SkillLabelBean secondSkillLabelBean = commnSkilllabel.get(0);
        int f1 = secondSkillLabelBean.getF1();
        int id = secondSkillLabelBean.getId();
        getCommnThirdSkillLabel(f1, id);
        postThridSkillLabel();
    }

    private void chooseThridSkillLabelLiist(View view, int position) {
        if (lastClickItemModel != null) {
            lastClickItemModel.setSecondSkilllabelColor(0xff333333);
        }

        SubscribeSecondSkilllabelHolder subscribeSecondSkilllabelHolder = (SubscribeSecondSkilllabelHolder) view.getTag();
        ItemSubscribeSecondSkilllabelModel itemSubscribeSecondSkilllabelModel = subscribeSecondSkilllabelHolder.mItemSubscribeSecondSkilllabelModel;

        itemSubscribeSecondSkilllabelModel.setSecondSkilllabelColor(0xff31c5e4);

        subscribeSecondSkilllabelHolder.clickItemPosition = position;

        lastClickItemModel = itemSubscribeSecondSkilllabelModel;
        checkedSecondLabel = itemSubscribeSecondSkilllabelModel.getSecondSkilllabelName();
        clickSecondSkillLabelBean = commnSkilllabel.get(position);
        int f1 = clickSecondSkillLabelBean.getF1();
        int secondId = clickSecondSkillLabelBean.getId();
        //根据条目点击事件跳转到三级条目
        getCommnThirdSkillLabel(f1, secondId);
        postThridSkillLabel();
    }

    //获取三级的集合
    private void getCommnThirdSkillLabel(int f1, int secondId) {
        commnThirdSkillLabel.clear();
        for (SkillLabelBean labelBean : listThirdSkilllabelName) {
            int F1 = labelBean.getF1();
            int f2 = labelBean.getF2();
            if (f1 == F1 && f2 == secondId) {
                commnThirdSkillLabel.add(labelBean);
            }
        }
    }

    //获取二级的集合
    private void getCommnSkillLabel(int firstId) {
        commnSkilllabel.clear();
        for (SkillLabelBean skillLabelBean : listSkilllabel) {
            int f11 = skillLabelBean.getF1();
            int f21 = skillLabelBean.getF2();
            int id1 = skillLabelBean.getId();
            String tag1 = skillLabelBean.getTag();
            if (firstId == f11 && f21 == 0) {
                commnSkilllabel.add(new SkillLabelBean(f11, f21, id1, tag1));
            }
        }
    }

    //加载三级标签
    private void postThridSkillLabel() {
        mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.removeAllViews();
        mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.post(new Runnable() {
            @Override
            public void run() {
                scrollViewWidth = mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.getMeasuredWidth();
                scrollViewWidth = scrollViewWidth - CommonUtils.dip2px(30);
//                labelRightMargin = CommonUtils.dip2px(10);
                labelRightMargin = 10;
                updateLableView();
            }
        });
    }

    //保存在本地
    private void saveDate2Local(String fileName, ArrayList data) {
        ObjectOutputStream oos = null;
        try {
            if (data == null || data.size() == 0) {
                return;
            }
            //对象流读取一个集合
            oos = new ObjectOutputStream(new FileOutputStream(getLocalFile(fileName)));
            oos.writeObject(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //从本地读取
    private ArrayList getDateFromLocal(String fileName) {
        ArrayList data = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(getLocalFile(fileName)));
            data = (ArrayList) ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    private HashMap<String, File> Files = new HashMap<String, File>();

    private File getLocalFile(String fileName) {
        File file = Files.get(fileName);
        if (file == null) {
            file = new File(CommonUtils.getApplication().getCacheDir(), fileName);
            //lable 存在本地 cache目录下 网络数据和用户自己加的数据存在不同目录下
            Files.put(fileName, file);
        }
        return file;
    }

    private void updateLableView() {
        skillLabelLineWidth = 0;//每次刷新数据就赋值为0
        mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.removeAllViews();
        //非自定义标签
        if (commnThirdSkillLabel.size() != 0) {
            for (int i = 0; i < commnThirdSkillLabel.size(); i++) {
                SkillLabelBean skillLabelBean = commnThirdSkillLabel.get(i);
                no_custom_f1 = skillLabelBean.getF1();
                no_custom_f2 = skillLabelBean.getF2();
                String thirdSkilllabelName = skillLabelBean.getTag();
                index = i;
                TextView tvThirdSkilllabelName = getTableView(i, thirdSkilllabelName, skillLabelBean);
                //测量标签TextView的宽度并判断是否换行
                addTableView(tvThirdSkilllabelName);
            }
        } else {//此时二级标签
            if (clickSecondSkillLabelBean != null) {
                no_custom_f1 = clickSecondSkillLabelBean.getF1();
                no_custom_f2 = clickSecondSkillLabelBean.getId();
            }
        }

        if (listThirdUserCustomSkilllabelName.size() != 0) {//自定义总的集合，再分类刷选
            //用户自定义标签
            for (int i = 0; i < listThirdUserCustomSkilllabelName.size(); i++) {
                SkillLabelBean skillLabelBean = listThirdUserCustomSkilllabelName.get(i);
                int f1 = skillLabelBean.getF1();
                int f2 = skillLabelBean.getF2();
                if (no_custom_f1 == f1 && no_custom_f2 == f2) {
                    addTableView(getUseCostomLableView(skillLabelBean.getTag(), skillLabelBean));
                }
            }
        }

        View addLableView = getAddLableView(no_custom_f1, no_custom_f2);
        if (addLableView != null) {
            addTableView(addLableView);
        }

        mActivitySubscribeBinding.llActivitySubscribeThirdSkilllabel.addView(llSkilllabelLine);
        //添加一行空格
        addLine();
    }

    private void addTableView(View tvThirdSkilllabelName) {
        tvThirdSkilllabelName.measure(0, 0);
        tvThirdSkilllabelWidth = tvThirdSkilllabelName.getMeasuredWidth() + labelRightMargin;
        newSkillLabelLineWidth = skillLabelLineWidth + tvThirdSkilllabelWidth;
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
    }

    @NonNull
    private TextView getTableView(int i, String thirdSkilllabelName, SkillLabelBean skillLabelBean) {
        //创建标签TextView
        LinearLayout.LayoutParams llParamsForSkillLabel = new LinearLayout.LayoutParams(-2, -2);
        llParamsForSkillLabel.rightMargin = CommonUtils.dip2px(labelRightMargin);//边距
        TextView tvThirdSkilllabelName = new TextView(CommonUtils.getContext());
        // tvThirdSkilllabelName.setTag("label_" + i);
        tvThirdSkilllabelName.setTag(skillLabelBean.getF1() + "-" + skillLabelBean.getF2() + "-" + skillLabelBean.getTag());
        tvThirdSkilllabelName.setOnClickListener(new CheckThirdLabelListener());
        tvThirdSkilllabelName.setLayoutParams(llParamsForSkillLabel);
        tvThirdSkilllabelName.setMaxLines(1);
        tvThirdSkilllabelName.setGravity(Gravity.CENTER);
        tvThirdSkilllabelName.setTextColor(0xff333333);
        tvThirdSkilllabelName.setTextSize(14);
        tvThirdSkilllabelName.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(16), CommonUtils.dip2px(11));
        tvThirdSkilllabelName.setBackgroundResource(R.drawable.shape_rounded_rectangle_third_skilllabel);
//        tvThirdSkilllabelName.setText(thirdSkilllabelName);
        tvThirdSkilllabelName.setText(skillLabelBean.getTag());
        return tvThirdSkilllabelName;
    }

    //最后创建一个空行
    private void addLine() {
        createLabelLine();
        LinearLayout.LayoutParams llParamsForLine = new LinearLayout.LayoutParams(-1, -2);
        llParamsForLine.height = CommonUtils.dip2px(55);
        LinearLayout lllayout = new LinearLayout(CommonUtils.getContext());
        lllayout.setLayoutParams(llParamsForLine);
        llSkilllabelLine.addView(lllayout);
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

    private View getUseCostomLableView(final String text, SkillLabelBean skillLabelBean) {
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-2, -2);
        TextView textview = new TextView(CommonUtils.getContext());
        textview.setLayoutParams(ll);
        textview.setMaxLines(1);
        textview.setGravity(Gravity.CENTER);
        textview.setTextColor(0xff333333);
        textview.setTextSize(14);
//        textview.setText(text);
        textview.setText(skillLabelBean.getTag());
        index += 1;
//        textview.setTag(("label_" + index));
        textview.setTag(skillLabelBean.getTag());//自定义标签不需要f1、f2
        textview.setOnClickListener(new CheckThirdLabelListener());
        textview.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(9), CommonUtils.dip2px(11));

        imageView = new ImageView(CommonUtils.getContext());
        imageView.setImageResource(R.mipmap.minus_icon);
        imageView.setPadding(CommonUtils.dip2px(5), CommonUtils.dip2px(10), CommonUtils.dip2px(5), CommonUtils.dip2px(10));
        imageView.setLayoutParams(ll);

        TextView lineTextView = new TextView(CommonUtils.getContext());
        lineTextView.setHeight(CommonUtils.dip2px(39));
        lineTextView.setWidth(CommonUtils.dip2px(1));
        lineTextView.setBackgroundColor(Color.parseColor("#e5e5e5"));
        lineTextView.setPadding(CommonUtils.dip2px(8), CommonUtils.dip2px(0), CommonUtils.dip2px(4), CommonUtils.dip2px(0));

        LinearLayout.LayoutParams ll2 = new LinearLayout.LayoutParams(-2, -2);
        ll2.rightMargin = CommonUtils.dip2px(labelRightMargin);
        linearLayout = new LinearLayout(CommonUtils.getContext());
        linearLayout.setLayoutParams(ll2);
        linearLayout.addView(textview);
        linearLayout.addView(lineTextView);
        linearLayout.addView(imageView);
        linearLayout.setBackgroundResource(R.drawable.shape_rounded_rectangle_third_skilllabel);
        linearLayout.setGravity(Gravity.CENTER);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listThirdUserCustomSkilllabelName.size(); i++) {
                    String skillLabelName = listThirdUserCustomSkilllabelName.get(i).getTag();
                    if (skillLabelName == text) {

                        String tags = listThirdUserCustomSkilllabelName.get(i).getTag();
                        int f1 = listThirdUserCustomSkilllabelName.get(i).getF1();
                        int f2 = listThirdUserCustomSkilllabelName.get(i).getF2();
                        listThirdUserCustomSkilllabelName.remove(listThirdUserCustomSkilllabelName.get(i));
                        //把删除的标签存在网络
                        saveMoveIntent(f1, f2, tags);
                    }
                }
                updateLableView();
            }
        });
        return linearLayout;
    }

    //删除标签在网络
    private void saveMoveIntent(int f1, int f2, String tags) {
        DeleteSkillLabelProtocol deleteSkillLabelProtocol = new DeleteSkillLabelProtocol(f1, f2, tags);
        deleteSkillLabelProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SkillLabelGetBean>() {
            @Override
            public void execute(SkillLabelGetBean dataBean) {
                int rescode = dataBean.getRescode();
                switch (rescode) {
                    case GlobalConstants.Rescode.RES_SUCCESS:
                        LogKit.d(MyManager.BACK_SUCCESS);
                        ToastUtils.shortToast("已删除");
                        break;
                    case GlobalConstants.Rescode.RES_FAIL:
                        LogKit.d(MyManager.RES_FAIL);
                        break;
                    case GlobalConstants.Rescode.RES_TAG_EXIST:
                        LogKit.d(MyManager.RES_TAG_EXIST);
                        break;
                    case GlobalConstants.Rescode.RES_INVALID_TOKEN:
                        LogKit.d(MyManager.RES_INVALID_TOKEN);
                        break;
                    case GlobalConstants.Rescode.RES_INVALID_PARAMS:
                        LogKit.d(MyManager.RES_INVALID_PARAMS);
                        break;
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result :" + result);
            }
        });
    }

    //    private String text = "新建标签";
    private String text = "自定义";
    private String logText = "创建的标签是空白";

    private View getAddLableView(final int f1, final int f2) {
        LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(-2, -2);
        TextView textview = new TextView(CommonUtils.getContext());
        textview.setLayoutParams(ll);
        textview.setMaxLines(1);
        textview.setGravity(Gravity.CENTER);
        textview.setTextColor(0xff333333);
        textview.setTextSize(14);
        textview.setText(text);
        textview.setPadding(CommonUtils.dip2px(16), CommonUtils.dip2px(11), CommonUtils.dip2px(9), CommonUtils.dip2px(11));
        imageViewAdd = new ImageView(CommonUtils.getContext());
        imageViewAdd.setImageResource(R.mipmap.pluse_icon);
        imageViewAdd.setPadding(CommonUtils.dip2px(5), CommonUtils.dip2px(12), CommonUtils.dip2px(5), CommonUtils.dip2px(10));
        imageViewAdd.setLayoutParams(ll);

        TextView linetextView = new TextView(CommonUtils.getContext());
        linetextView.setHeight(CommonUtils.dip2px(39));
        linetextView.setWidth(CommonUtils.dip2px(1));
        linetextView.setBackgroundColor(Color.parseColor("#e5e5e5"));
        linetextView.setPadding(CommonUtils.dip2px(8), CommonUtils.dip2px(0), CommonUtils.dip2px(4), CommonUtils.dip2px(0));

        LinearLayout.LayoutParams ll2 = new LinearLayout.LayoutParams(-2, -2);
        ll2.rightMargin = CommonUtils.dip2px(labelRightMargin);
        linearLayout = new LinearLayout(CommonUtils.getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(ll2);
        linearLayout.addView(textview);
        linearLayout.addView(linetextView);
        linearLayout.addView(imageViewAdd);
        linearLayout.setBackgroundResource(R.drawable.shape_rounded_rectangle_third_skilllabel);
        linearLayout.setGravity(Gravity.CENTER);

        //点击事件，弹出自定义标签的对话框
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
                //监听得到数据
                dialogCustomSkillLabelModel.setOnOkDialogListener(new DialogCustomSkillLabelModel.OnOkDialogListener() {
                    @Override
                    public void OnOkDialogClick(String text) {
                        if (text != null) {
                            SkillLabelBean skillLabelBean = new SkillLabelBean(f1, f2, -1, text);
                            listThirdUserCustomSkilllabelName.add(skillLabelBean);
                            saveDate2Local("userCostom", listThirdUserCustomSkilllabelName);
                            updateLableView();
                            //增加自定义标签保存在网络
                            saveAddInternet(f1, f2, text);
                        } else {
                            LogKit.d(logText);
                        }
                    }
                });
            }
        });
        return linearLayout;
    }

    //保存早网络
    private void saveAddInternet(int f1, int f2, String tags) {
        CreateSkillLabelProtocol addSkillLabelProtocol = new CreateSkillLabelProtocol(f1, f2, tags);
        addSkillLabelProtocol.getDataFromServer(new BaseProtocol.IResultExecutor<SkillLabelGetBean>() {
            @Override
            public void execute(SkillLabelGetBean dataBean) {
                int rescode = dataBean.getRescode();
                switch (rescode) {
                    case GlobalConstants.Rescode.RES_SUCCESS:
                        LogKit.d(MyManager.BACK_SUCCESS);
                        ToastUtils.shortToast("已添加");
                        break;
                    case GlobalConstants.Rescode.RES_FAIL:
                        LogKit.d(MyManager.RES_FAIL);
                        break;
                    case GlobalConstants.Rescode.RES_TAG_EXIST:
                        LogKit.d(MyManager.RES_TAG_EXIST);
                        break;
                    case GlobalConstants.Rescode.RES_INVALID_TOKEN:
                        LogKit.d(MyManager.RES_INVALID_TOKEN);
                        break;
                }
            }

            @Override
            public void executeResultError(String result) {
                LogKit.d("result :" + result);
            }
        });
    }

    private void showCustomDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SubscribeActivity.this);
        DialogCustomSkilllabelBinding dialogCustomSkilllabelBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_custom_skilllabel, null, false);
        dialogCustomSkillLabelModel = new DialogCustomSkillLabelModel(dialogCustomSkilllabelBinding);
        dialogCustomSkilllabelBinding.setDialogCustomSkillLabelModel(dialogCustomSkillLabelModel);
        dialogBuilder.setView(dialogCustomSkilllabelBinding.getRoot());

        AlertDialog dialogCustomSkillLabel = dialogBuilder.create();
        dialogCustomSkillLabel.show();
        dialogCustomSkillLabelModel.currentDialog = dialogCustomSkillLabel;
        dialogCustomSkillLabel.setCanceledOnTouchOutside(false);

        Window dialogSubscribeWindow = dialogCustomSkillLabel.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(300);//宽度
        dialogParams.height = CommonUtils.dip2px(190);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);//dialog的灰度
//        dialogBuilder.show();
    }

    private String lastLabelName = "";
    private String toastText = "此标签已被添加";
    private String toastTextString = "最多添加3个标签";

    public class CheckThirdLabelListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String labelTag = (String) v.getTag();
            labelName = ((TextView) v).getText().toString();
            if (clickCount >= 0 && clickCount < 3) {
                if (!lastLabelName.equals(labelName)) {
                    addCheckedLabels(labelTag, labelName);
                    clickCount += 1;
                    lastLabelName = labelName;
                } else {
                    ToastUtils.shortToast(toastText);
                }
            } else {
                ToastUtils.shortToast(toastTextString);
            }
        }
    }

    LinearLayout llCheckedLabelsLine = null;
    int checkedLabelLeftMargin = CommonUtils.dip2px(11);
    int totalCheckedLabelsWidth = 0;
    int currentLabelsLineWidth = 0;
    public ArrayList<String> listCheckedLabelTag = new ArrayList<String>();
    public ArrayList<String> listCheckedLabelName = new ArrayList<String>();
    public static ArrayList<String> saveListCheckedLabelName = new ArrayList<String>();

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
        tvParams.topMargin = CommonUtils.dip2px(12);
        tvLabelName.setBackgroundResource(R.drawable.shape_rounded_rectangle_skilllabel_gray);
        tvLabelName.setText(labelName);
        tvLabelName.setTextColor(Color.parseColor("#333333"));
        tvLabelName.setPadding(CommonUtils.dip2px(15), CommonUtils.dip2px(12), CommonUtils.dip2px(15), CommonUtils.dip2px(12));
        tvLabelName.setLayoutParams(tvParams);

        LinearLayout.LayoutParams ivbtnParams = new LinearLayout.LayoutParams(-2, -2);
        ImageButton ivbtnUnCheckedLabel = new ImageButton(CommonUtils.getContext());
        ivbtnUnCheckedLabel.setBackground(new ColorDrawable(Color.TRANSPARENT));
        ivbtnUnCheckedLabel.setImageResource(R.mipmap.delete_icon);
        ivbtnParams.leftMargin = CommonUtils.dip2px(-7);
        ivbtnUnCheckedLabel.setLayoutParams(ivbtnParams);

        //最多出现三个标签
        llCheckedLabel.addView(tvLabelName);
        llCheckedLabel.addView(ivbtnUnCheckedLabel);

//        ivbtnUnCheckedLabel.setOnClickListener(new deleteCheckedLabelListener(llCheckedLabel));//现在要把删除时间添加到整个标签上
        llCheckedLabel.setOnClickListener(new deleteCheckedLabelListener(llCheckedLabel));
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

            clickCount -= 1;
            if (clickCount == 0) {
                lastLabelName = "";
            }
        }
    }

    //接口回调传递数据
    public interface OnListener {
        void OnListener(ArrayList<SkillLabelBean> firstList);
    }

    private OnListener listener;

    public void setOnListener(OnListener listener) {
        this.listener = listener;
    }
}
