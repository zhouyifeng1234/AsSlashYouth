package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemDemandLayoutBinding;
import com.slash.youth.databinding.ItemManagePublishHolderBinding;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.viewmodel.ItemManagePublishModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.TimeUtils;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class ManagePublishHolder extends BaseHolder<ManagerMyPublishTaskBean.DataBean.ListBean> {
    private MySkillManageActivity mySkillManageActivity;
    private final String myActivityTitle;
    private ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList;
    public ItemManagePublishHolderBinding itemManagePublishHolderBinding;
    private ItemManagePublishModel itemManagePublishModel;
    private int action = -1;

    public ManagePublishHolder(MySkillManageActivity mySkillManageActivity, ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList) {
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
        myActivityTitle = SpUtils.getString("myActivityTitle", "");
    }

    @Override
    public View initView() {
        itemManagePublishHolderBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_manage_publish_holder, null, false);
        itemManagePublishModel = new ItemManagePublishModel(mySkillManageActivity, managePublishList, itemManagePublishHolderBinding);
        itemManagePublishHolderBinding.setItemManagePublishModel(itemManagePublishModel);
        return itemManagePublishHolderBinding.getRoot();
    }

    @Override
    public void refreshView(ManagerMyPublishTaskBean.DataBean.ListBean data) {
        setView(myActivityTitle, data);

        int instalment = data.getInstalment();

        String title = data.getTitle();
        itemManagePublishHolderBinding.tvManageMyPublishTaskTitle.setText(title);

        int isAuth = data.getIsAuth();
        switch (isAuth) {
            case 1:
                itemManagePublishHolderBinding.ivManageMyPublishTaskV.setVisibility(View.VISIBLE);
                break;
            case 0:
                itemManagePublishHolderBinding.ivManageMyPublishTaskV.setVisibility(View.GONE);
                break;
        }

        String name = data.getName();
        itemManagePublishHolderBinding.tvManageMyPublishName.setText(name);

        double quote = data.getQuote();
        //单位
        int quoteUnit = data.getQuoteUnit();

        long starttime = data.getStarttime();
        long endtime = data.getEndtime();
        String startData = TimeUtils.getData(starttime);
        String endData = TimeUtils.getData(endtime);

        String avatar = data.getAvatar();
        if (!TextUtils.isEmpty(avatar)) {
            BitmapKit.bindImage(itemManagePublishHolderBinding.ivAvater, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
        }

        int type = data.getType();
        switch (type){
            case 1://需求

                if(instalment == 0){
                    itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.DEMAND_INSTALMENT);
                }

                itemManagePublishHolderBinding.ivTime.setVisibility(View.GONE);

                switch (myActivityTitle){
                    case MyManager.SKILL_MANAGER:
                        itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.DEMAND_INSTALMENT);
                        switch (instalment) {
                            case 1:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.GONE);
                                break;
                            case 0:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.VISIBLE);
                                break;
                        }
                        break;
                    case MyManager.MAANAGER_MY_PUBLISH_TASK:
                        itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.VISIBLE);
                        switch (instalment) {
                            case 1:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.SERVICE_INSTALMENT);
                                break;
                            case 0:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.DEMAND_INSTALMENT);
                                break;
                        }
                        break;
                }

                if(quote<=0){
                    itemManagePublishHolderBinding.tvManageMyPublishQuote.setText(FirstPagerManager.DEMAND_QUOTE);
                }else {
                    itemManagePublishHolderBinding.tvManageMyPublishQuote.setText(MyManager.QOUNT + (int) quote+"元");
                }

               /* if(starttime <=0){
                    itemManagePublishHolderBinding.tvManageMyPublishTime.setText(FirstPagerManager.ANY_TIME);
                }else {
                    itemManagePublishHolderBinding.tvManageMyPublishTime.setText(MyManager.START_TIME + startData);
                }*/

                break;
            case 2://服务
                if(instalment == 1){
                    itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager. SERVICE_INSTALMENT);
                }

                itemManagePublishHolderBinding.ivTime.setVisibility(View.VISIBLE);

                switch (myActivityTitle){
                    case MyManager.SKILL_MANAGER:
                        itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.SERVICE_INSTALMENT);
                        switch (instalment) {
                            case 1:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.VISIBLE);
                                break;
                            case 0:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.GONE);
                                break;
                        }
                        break;
                    case MyManager.MAANAGER_MY_PUBLISH_TASK:
                        itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.VISIBLE);
                        switch (instalment) {
                            case 1:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.SERVICE_INSTALMENT);
                                break;
                            case 0:
                                itemManagePublishHolderBinding.tvManageMyPublishType.setText(FirstPagerManager.DEMAND_INSTALMENT);
                                break;
                        }
                        break;
                }

                if (quoteUnit == 9) {
                    itemManagePublishHolderBinding.tvManageMyPublishQuote.setText(MyManager.QOUNT + (int) quote+"元");
                } else if (quoteUnit > 0 && quoteUnit < 9) {
                    itemManagePublishHolderBinding.tvManageMyPublishQuote.setText(MyManager.QOUNT + (int) quote + "元/" + MyManager.unitArr[quoteUnit - 1]);
                } else {//这种情况应该不存在
                    itemManagePublishHolderBinding.tvManageMyPublishQuote.setText(MyManager.QOUNT + (int) quote+"元");
                }

                int timetype = data.getTimetype();
                if(timetype == 0){
                   // itemManagePublishHolderBinding.tvManageMyPublishTime.setText( startData + "-" + endData);
                }else {
                    itemManagePublishHolderBinding.tvManageMyPublishTime.setText(FirstPagerManager.TIMETYPES[timetype-1]);
                }
                break;
        }
    }

    //设置界面
    private void setView(String myActivityTitle, ManagerMyPublishTaskBean.DataBean.ListBean data) {
        switch (myActivityTitle) {
            case MyManager.SKILL_MANAGER:
                itemManagePublishHolderBinding.tvMyBtn.setText(MyManager.PUBLISH);
                break;
            case MyManager.MAANAGER_MY_PUBLISH_TASK:
                int status = data.getStatus();
                switch (status) {
                    case 0://不在架上,那我就需要把它上架
                        itemManagePublishHolderBinding.tvMyBtn.setText(MyManager.UP);
                        itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#31C6E4"));
                        break;
                    case 1://在架上，那我显示下架
                        itemManagePublishHolderBinding.tvMyBtn.setText(MyManager.DOWN);
                        itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#999999"));
                        break;
                }
                break;
        }
    }

    @Override
    public void setData(ManagerMyPublishTaskBean.DataBean.ListBean data, final int position) {
        super.setData(data, position);
        //点击删除
        itemManagePublishHolderBinding.ivDeleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDeleteClick(position);
            }
        });

        //上下架
        itemManagePublishHolderBinding.tvMyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = itemManagePublishHolderBinding.tvMyBtn.getText().toString();
                if (text.equals(MyManager.UP)) {
                    //下架埋点
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_RELEASE_UNSHELVE);
                    action = 1;

                } else if (text.equals(MyManager.DOWN)) {
                    //上架埋点
                    MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_RELEASE_TASK_RESHELF);
                    action = 0;
                }

                ManagerMyPublishTaskBean.DataBean.ListBean listBean = managePublishList.get(position);
                long id = listBean.getId();
                if (action != -1) {
                    itemManagePublishModel.UpAndDown(id, action);
                }
            }
        });
    }

    public interface OnDeleteClickListener {
        void OnDeleteClick(int position);
    }

    private OnDeleteClickListener listener;

    public void setOnCBacklickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }
}
