package com.slash.youth.ui.viewmodel;

import android.app.Activity;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivityChooseSkillBinding;
import com.slash.youth.domain.AllSkillLablesBean;
import com.slash.youth.domain.LoginTagBean;
import com.slash.youth.engine.LoginManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.HomeActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.IOUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/9/12.
 */
public class ActivityChooseSkillModel extends BaseObservable {

    ActivityChooseSkillBinding mActivityChooseSkillBinding;
    Activity mActivity;
    private int chooseSkillLayerVisibility = View.INVISIBLE;//选择行业（一级标签）或岗位（二级标签）的浮层是否显示，默认为隐藏
    boolean isChooseMainLabel;//true为选择添加一级标签，false为选择添加二级标签
    String[] optionalMainLabels;
    String[] optionalSecondLabels;
    private NumberPicker mNpChooseSkillLabel;
    private String choosedMainLabel;
    private String choosedSecondLabel;
    ArrayList<String> choosedThirdLabels = new ArrayList<String>();

    public ActivityChooseSkillModel(ActivityChooseSkillBinding activityChooseSkillBinding, Activity activity) {
        this.mActivityChooseSkillBinding = activityChooseSkillBinding;
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        mNpChooseSkillLabel = mActivityChooseSkillBinding.npChooseSkillLabel;
        initData();
    }

    private void initData() {
        getDataFromServer();

//        optionalMainLabels = new String[]{"金融", "IT", "医学", "手工业", "文学"};//加载可选的一级标签（行业），实际应该从服务端接口获取
//        optionalSecondLabels = new String[]{"研发", "设计", "开发", "装修", "运营"};//加载可选的二级标签（岗位），实际应该从服务端接口获取
    }

    AllSkillLablesBean allSkillLablesBean;

    private void getDataFromServer() {
        //先获取本地的标签缓存
        allSkillLablesBean = getTagCache();
        if (allSkillLablesBean != null) {

        } else {
            LoginManager.loginSetTag(new BaseProtocol.IResultExecutor<String>() {
                @Override
                public void execute(String dataBean) {
                    LogKit.v("setTagCache 11111");
                    setTagCache(dataBean);
                    LogKit.v("setTagCache 22222");
                    allSkillLablesBean = getTagCache();
                    LogKit.v("setTagCache 33333");
                    if (allSkillLablesBean == null) {
                        ToastUtils.shortToast("allSkillLablesBean null");
                    }
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
            oosTagJson.writeObject(oosTagJson);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(oosTagJson);
            IOUtils.close(fosTagJson);
        }
    }

    LinearLayout llSkillLabelsLine = null;
    int lineLeftRightMargin = CommonUtils.dip2px(11);
    int labelRightMargin = CommonUtils.dip2px(20);
    int currentLabelsWidth = 0;

    /**
     * 根据选择一级标签和二级标签，展示对应的三级标签
     */
    private void setThirdSkillLabels() {
        if (TextUtils.isEmpty(choosedMainLabel) || TextUtils.isEmpty(choosedSecondLabel)) {
            return;
        }
        choosedThirdLabels.clear();
        //TODO 首先获取技能标签数据的集合，实际应该从服务端接口获取，这里暂时写死
        final ArrayList<String> listSkillLabels = new ArrayList<String>();
        listSkillLabels.add("APP开发设计");
        listSkillLabels.add("APP");
        listSkillLabels.add("U3D");
        listSkillLabels.add(".NET");
        listSkillLabels.add("java");
        listSkillLabels.add("服务端开发");
        listSkillLabels.add("前端开发");
        listSkillLabels.add("APP开发设计");
        listSkillLabels.add("APP");
        listSkillLabels.add("U3D");
        listSkillLabels.add(".NET");
        listSkillLabels.add("java");
        listSkillLabels.add("服务端开发");
        listSkillLabels.add("前端开发");
        listSkillLabels.add("APP开发设计");
        listSkillLabels.add("APP");
        listSkillLabels.add("U3D");
        listSkillLabels.add(".NET");
        listSkillLabels.add("java");
        listSkillLabels.add("服务端开发");
        listSkillLabels.add("前端开发");
        listSkillLabels.add("APP开发设计");
        listSkillLabels.add("APP");
        listSkillLabels.add("U3D");
        listSkillLabels.add(".NET");
        listSkillLabels.add("java");
        listSkillLabels.add("服务端开发");
        listSkillLabels.add("前端开发");
        listSkillLabels.add("APP开发设计");
        listSkillLabels.add("APP");
        listSkillLabels.add("U3D");
        listSkillLabels.add(".NET");
        listSkillLabels.add("java");
        listSkillLabels.add("服务端开发");
        listSkillLabels.add("前端开发");

        mActivityChooseSkillBinding.llActivityChooseSkillLabels.removeAllViews();
        mActivityChooseSkillBinding.llActivityChooseSkillLabels.post(new Runnable() {
            @Override
            public void run() {
                int labelsTotalWidth = mActivityChooseSkillBinding.llActivityChooseSkillLabels.getMeasuredWidth() - 2 * lineLeftRightMargin;
//                ToastUtils.shortToast(labelsTotalWidth + "");
                for (int i = 0; i < listSkillLabels.size(); i++) {
                    String labelName = listSkillLabels.get(i);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, CommonUtils.dip2px(60));
                    params.rightMargin = labelRightMargin;
                    TextView tvSkillLabel = new TextView(CommonUtils.getContext());
                    tvSkillLabel.setTag(false);
                    tvSkillLabel.setText(labelName);
                    tvSkillLabel.setPadding(CommonUtils.dip2px(21), CommonUtils.dip2px(10), CommonUtils.dip2px(21), CommonUtils.dip2px(10));
                    tvSkillLabel.setBackgroundResource(R.mipmap.unchoose_skill_label_bg);
                    tvSkillLabel.setGravity(Gravity.CENTER);
                    tvSkillLabel.setTextColor(0xff31c5e4);
                    tvSkillLabel.setTextSize(13.5f);
                    tvSkillLabel.setLayoutParams(params);
                    tvSkillLabel.measure(0, 0);
                    setSkillLabelSelectedListener(tvSkillLabel);
                    int labelWidth = tvSkillLabel.getMeasuredWidth() + labelRightMargin;

                    int newCurrentLablesWidth = currentLabelsWidth + labelWidth;
                    if (llSkillLabelsLine == null) {
                        createSkillLabelsLine();
                    }
                    if (newCurrentLablesWidth >= labelsTotalWidth) {
                        if (currentLabelsWidth == 0) {
                            llSkillLabelsLine.addView(tvSkillLabel);
                            mActivityChooseSkillBinding.llActivityChooseSkillLabels.addView(llSkillLabelsLine);
                            createSkillLabelsLine();
                        } else {
                            mActivityChooseSkillBinding.llActivityChooseSkillLabels.addView(llSkillLabelsLine);
                            createSkillLabelsLine();
                            llSkillLabelsLine.addView(tvSkillLabel);
                            currentLabelsWidth = labelWidth;
                        }
                    } else {
                        llSkillLabelsLine.addView(tvSkillLabel);
                        currentLabelsWidth = newCurrentLablesWidth;
                    }
                }
            }
        });

    }

    private void createSkillLabelsLine() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
        params.setMargins(lineLeftRightMargin, CommonUtils.dip2px(24), lineLeftRightMargin, 0);

        llSkillLabelsLine = new LinearLayout(CommonUtils.getContext());
        llSkillLabelsLine.setLayoutParams(params);
    }

    private void setSkillLabelSelectedListener(final TextView tvSkillLabel) {
        tvSkillLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelected = (boolean) tvSkillLabel.getTag();
                if (isSelected) {
                    tvSkillLabel.setTextColor(0xff31c5e4);
                    tvSkillLabel.setBackgroundResource(R.mipmap.unchoose_skill_label_bg);
                    choosedThirdLabels.remove(tvSkillLabel.getText().toString());
                } else {
                    tvSkillLabel.setTextColor(0xffffffff);
                    tvSkillLabel.setBackgroundResource(R.mipmap.choose_skill_label_bg);
                    choosedThirdLabels.add(tvSkillLabel.getText().toString());
                }
                tvSkillLabel.setTag(!isSelected);
            }
        });
    }

    public void finishChooseSkill(View v) {
        Intent intentHomeActivity = new Intent(CommonUtils.getContext(), HomeActivity.class);
        intentHomeActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        CommonUtils.getContext().startActivity(intentHomeActivity);
    }

    /**
     * 选择一级标签（选择行业）
     *
     * @param v
     */
    public void chooseMainSkillLabel(View v) {
        isChooseMainLabel = true;
        setChooseSkillLayerVisibility(View.VISIBLE);

        AllSkillLablesBean.Tag_1[] tag1Arr = (AllSkillLablesBean.Tag_1[]) allSkillLablesBean.mapTag_1.values().toArray();
        optionalMainLabels = new String[tag1Arr.length];
        for (int i = 0; i < tag1Arr.length; i++) {
            optionalMainLabels[i] = tag1Arr[i].tag;
        }

        mNpChooseSkillLabel.setDisplayedValues(optionalMainLabels);
        mNpChooseSkillLabel.setMaxValue(optionalMainLabels.length - 1);
        mNpChooseSkillLabel.setMinValue(0);
        mNpChooseSkillLabel.setValue(1);
    }


    /**
     * 选择二级标签（选择岗位）
     *
     * @param v
     */
    public void chooseSecondSkillLabel(View v) {
        if (TextUtils.isEmpty(choosedMainLabel)) {
            return;
        }
        optionalSecondLabels = new String[]{"研发", "设计", "开发", "装修", "运营"};//加载可选的二级标签（岗位），实际应该从服务端接口获取
        isChooseMainLabel = false;
        setChooseSkillLayerVisibility(View.VISIBLE);
        mNpChooseSkillLabel.setDisplayedValues(optionalSecondLabels);
        mNpChooseSkillLabel.setMaxValue(optionalSecondLabels.length - 1);
        mNpChooseSkillLabel.setMinValue(0);
        mNpChooseSkillLabel.setValue(1);
        setThirdSkillLabels();
    }

    /**
     * 确定选择的标签
     *
     * @param v
     */
    public void okChooseLabel(View v) {
        setChooseSkillLayerVisibility(View.INVISIBLE);
        int value = mNpChooseSkillLabel.getValue();
        if (isChooseMainLabel) {
            if (optionalMainLabels[value] != choosedMainLabel) {
                setChoosedMainLabel(optionalMainLabels[value]);
                setChoosedSecondLabel(null);
                mActivityChooseSkillBinding.llActivityChooseSkillLabels.removeAllViews();
            }
        } else {
            if (optionalSecondLabels[value] != choosedSecondLabel) {
                setChoosedSecondLabel(optionalSecondLabels[value]);
                setThirdSkillLabels();
            }
        }
    }

    @Bindable
    public int getChooseSkillLayerVisibility() {
        return chooseSkillLayerVisibility;
    }

    public void setChooseSkillLayerVisibility(int chooseSkillLayerVisibility) {
        this.chooseSkillLayerVisibility = chooseSkillLayerVisibility;
        notifyPropertyChanged(BR.chooseSkillLayerVisibility);
    }

    @Bindable
    public String getChoosedMainLabel() {
        return choosedMainLabel;
    }

    public void setChoosedMainLabel(String choosedMainLabel) {
        this.choosedMainLabel = choosedMainLabel;
        notifyPropertyChanged(BR.choosedMainLabel);
    }

    @Bindable
    public String getChoosedSecondLabel() {
        return choosedSecondLabel;
    }

    public void setChoosedSecondLabel(String choosedSecondLabel) {
        this.choosedSecondLabel = choosedSecondLabel;
        notifyPropertyChanged(BR.choosedSecondLabel);
    }
}
