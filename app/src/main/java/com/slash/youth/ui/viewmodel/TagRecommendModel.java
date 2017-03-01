package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slash.youth.BR;
import com.slash.youth.databinding.ActivityTagRecommendBinding;
import com.slash.youth.domain.AllSkillLablesBean;
import com.slash.youth.domain.LoginTagBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by zhouyifeng on 2017/2/27.
 */
public class TagRecommendModel extends BaseObservable {

    ActivityTagRecommendBinding mActivityTagRecommendBinding;
    Activity mActivity;
    long tagId;
    String tagName;

    public TagRecommendModel(ActivityTagRecommendBinding activityTagRecommendBinding, Activity activity) {
        this.mActivityTagRecommendBinding = activityTagRecommendBinding;
        this.mActivity = activity;
        initData();
        initView();
    }

    private void initData() {
        tagId = mActivity.getIntent().getLongExtra("tagId", -1);//这里应该是一级标签的ID
        tagName = mActivity.getIntent().getStringExtra("tagName");
        LogKit.v("tagId:" + tagId + "    tagName:" + tagName);
        getDataFromServer();
    }

    private void initView() {
        setFirstTagName(tagName);
    }

    private ArrayList<Long> listTag2Id = new ArrayList<Long>();
    private ArrayList<String> listTag2Name = new ArrayList<String>();

    private void getDataFromServer() {
        initTagsInfo();
        if (allSkillLablesBean != null) {
            HashMap<Long, AllSkillLablesBean.Tag_1> mapTag_1 = allSkillLablesBean.mapTag_1;
            AllSkillLablesBean.Tag_1 tag_1 = mapTag_1.get(tagId);//根据一级标签ID获取一级标签对象
            //获取到这个一级标签下所有二级标签的ID和名字信息
            for (long tag_2_id : tag_1.mapTag_2.keySet()) {
                AllSkillLablesBean.Tag_2 tag_2 = tag_1.mapTag_2.get(tag_2_id);
                String tag_2_name = tag_2.tag;
                LogKit.v("tag_2_id:" + tag_2_id + "  tag_2_name:" + tag_2_name);
                listTag2Id.add(tag_2_id);
                listTag2Name.add(tag_2_name);
            }
            if (listTag2Id.size() > 0) {
                setTag2Tab();
                //默认显示集合中的第一个二级标签的推荐列表
                loadTag2RecommendList(listTag2Id.get(0));
            } else {
                LogKit.v("没有对应二级标签的数据");
                mActivityTagRecommendBinding.llTag2Tab.setVisibility(View.GONE);
            }
        } else {
            LogKit.v("加载标签数据失败,allSkillLablesBean:" + allSkillLablesBean);
        }
    }

    private ArrayList<TextView> listTabTextView = new ArrayList<TextView>();

    /**
     * 显示二级标签的tab
     */
    private void setTag2Tab() {
        if (listTag2Name.size() <= 1) {//只有一个二级标签，“其它”就是如此，直接隐藏二级标签tab栏
            mActivityTagRecommendBinding.llTag2Tab.setVisibility(View.GONE);
        } else {//目前二级标签的数量只有3个或者4个,如果以后会出现跟多的数量，再做判断
            for (int i = 0; i < listTag2Name.size(); i++) {
                String tag2Name = listTag2Name.get(i);
                long tag2Id = listTag2Id.get(i);

                LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, -2);
                tvParams.gravity = Gravity.CENTER_VERTICAL;
                tvParams.weight = 1;
                TextView tvTab = new TextView(CommonUtils.getContext());
                tvTab.setLayoutParams(tvParams);
                tvTab.setGravity(Gravity.CENTER);
                tvTab.setText(tag2Name);
                tvTab.setTextSize(15);
                //这里默认选中的是第一个，所以默认第一个字体颜色是31C5E4
                if (i == 0) {
                    tvTab.setTextColor(0xff31C5E4);
                } else {
                    tvTab.setTextColor(0xff333333);
                }
                tvTab.setOnClickListener(new Tag2TabOnClickListener(i));
                mActivityTagRecommendBinding.llTag2Tab.addView(tvTab);
                listTabTextView.add(tvTab);

                if (i < listTag2Name.size() - 1) {//如果不是最后一个tab，需要加入分割竖线
                    LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(CommonUtils.dip2px(1), CommonUtils.dip2px(20));
                    viewParams.gravity = Gravity.CENTER_VERTICAL;
                    View viewLine = new View(CommonUtils.getContext());
                    viewLine.setBackgroundColor(0xffE5E5E5);
                    viewLine.setLayoutParams(viewParams);
                    mActivityTagRecommendBinding.llTag2Tab.addView(viewLine);
                }
            }
        }
    }

    private int currentCheckedTabIndex = 0;//默认选中的是第一个tab

    private class Tag2TabOnClickListener implements View.OnClickListener {

        private int tabIndex;

        public Tag2TabOnClickListener(int tabIndex) {
            this.tabIndex = tabIndex;
        }

        @Override
        public void onClick(View v) {
            if (tabIndex != currentCheckedTabIndex) {
                currentCheckedTabIndex = tabIndex;
                for (int i = 0; i < listTabTextView.size(); i++) {
                    TextView tvTab = listTabTextView.get(i);
                    if (i == tabIndex) {
                        tvTab.setTextColor(0xff31C5E4);
                    } else {
                        tvTab.setTextColor(0xff333333);
                    }
                }
                Long tag2Id = listTag2Id.get(tabIndex);
                loadTag2RecommendList(tag2Id);
            }
        }
    }

    /**
     * 根据二级标签的ID,从接口获取推荐的需求服务列表
     */
    private void loadTag2RecommendList(long tag2Id) {
        LogKit.v("load tag2 recommend list,tag2Id:" + tag2Id);
    }

    AllSkillLablesBean allSkillLablesBean;

    private void initTagsInfo() {
        //先获取本地的标签缓存
        allSkillLablesBean = getTagCache();
        if (allSkillLablesBean != null) {

        } else {
            LoginManager.loginGetTag(new BaseProtocol.IResultExecutor<String>() {
                @Override
                public void execute(String dataBean) {
                    setTagCache(dataBean);
                    allSkillLablesBean = getTagCache();
                }

                @Override
                public void executeResultError(String result) {
                    //这里不会执行
                }
            });
        }
    }

    private AllSkillLablesBean getTagCache() {
        File fileCache = CommonUtils.getContext().getCacheDir();
        File fileTagJson = new File(fileCache, "sys_tag.json");
        if (!fileTagJson.exists()) {
            return null;
        } else {
            FileInputStream fisTagJson = null;
            ObjectInputStream oisTagJson = null;
            try {
                fisTagJson = new FileInputStream(fileTagJson);
                oisTagJson = new ObjectInputStream(fisTagJson);
                AllSkillLablesBean allSkillLablesBean = (AllSkillLablesBean) oisTagJson.readObject();
                return allSkillLablesBean;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                IOUtils.close(oisTagJson);
                IOUtils.close(fisTagJson);
            }
        }
        return null;
    }

    private void setTagCache(String tagJson) {
        File fileCache = CommonUtils.getContext().getCacheDir();
        if (!fileCache.exists()) {
            fileCache.mkdirs();
        }
        File fileTagJson = new File(fileCache, "sys_tag.json");
        FileOutputStream fosTagJson = null;
        ObjectOutputStream oosTagJson = null;
        try {
            fosTagJson = new FileOutputStream(fileTagJson);
            oosTagJson = new ObjectOutputStream(fosTagJson);
            Gson gson = new Gson();

            Type listTagsType = new TypeToken<ArrayList<LoginTagBean>>() {
            }.getType();
            ArrayList<LoginTagBean> listTags = gson.fromJson(tagJson, listTagsType);


            AllSkillLablesBean allSkillLablesBean = new AllSkillLablesBean();
            allSkillLablesBean.addListTags(listTags);
            oosTagJson.writeObject(allSkillLablesBean);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oosTagJson);
            IOUtils.close(fosTagJson);
        }
    }

    public void goBack(View v) {
        mActivity.finish();
    }

    private String firstTagName;

    @Bindable
    public String getFirstTagName() {
        return firstTagName;
    }

    public void setFirstTagName(String firstTagName) {
        this.firstTagName = firstTagName;
        notifyPropertyChanged(BR.firstTagName);
    }
}
