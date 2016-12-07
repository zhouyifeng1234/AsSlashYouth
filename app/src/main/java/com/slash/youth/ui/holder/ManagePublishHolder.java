package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemManagePublishHolderBinding;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.viewmodel.ItemManagePublishModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.TimeUtils;

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

    public ManagePublishHolder( MySkillManageActivity mySkillManageActivity,ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList) {
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
        myActivityTitle = SpUtils.getString("myActivityTitle", "");
    }

    @Override
    public View initView() {
        itemManagePublishHolderBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_manage_publish_holder, null, false);
        itemManagePublishModel = new ItemManagePublishModel(mySkillManageActivity,managePublishList,itemManagePublishHolderBinding);
        itemManagePublishHolderBinding.setItemManagePublishModel(itemManagePublishModel);
        return itemManagePublishHolderBinding.getRoot();
    }

    @Override
    public void refreshView(ManagerMyPublishTaskBean.DataBean.ListBean data) {
        setView(myActivityTitle,data);

        String avatar = data.getAvatar();
        if(avatar!=null){
            BitmapKit.bindImage(itemManagePublishHolderBinding.ivAvater,GlobalConstants.HttpUrl.IMG_DOWNLOAD+"?fileId="+avatar);
        }

        int isAuth = data.getIsAuth();
        switch (isAuth){
            case 1:
                itemManagePublishHolderBinding.ivManageMyPublishTaskV.setVisibility(View.VISIBLE);
                break;
            case 0:
                itemManagePublishHolderBinding.ivManageMyPublishTaskV.setVisibility(View.GONE);
                break;
        }

        String title = data.getTitle();
        itemManagePublishHolderBinding.tvManageMyPublishTaskTitle.setText(title);

        String name = data.getName();
        itemManagePublishHolderBinding.tvManageMyPublishName.setText(name);

        double quote = data.getQuote();
        itemManagePublishHolderBinding.tvManageMyPublishQuote.setText("报价:¥"+quote);

        int instalment = data.getInstalment();
        switch (instalment){
            case 1:
                itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.VISIBLE);
                itemManagePublishHolderBinding.tvManageMyPublishType.setText("分期到账");
                break;
            case 0:
                itemManagePublishHolderBinding.tvManageMyPublishType.setVisibility(View.GONE);
                break;
        }

        int type = data.getType();
        long starttime = data.getStarttime();
        long endtime = data.getEndtime();
        String startData = TimeUtils.getData(starttime);
        String endData = TimeUtils.getData(endtime);
        switch (type){
            case 1://需求
                if(starttime == 0){
                    itemManagePublishHolderBinding.tvManageMyPublishTime.setVisibility(View.GONE);
                }else {
                    itemManagePublishHolderBinding.tvManageMyPublishTime.setVisibility(View.VISIBLE);
                    itemManagePublishHolderBinding.tvManageMyPublishTime.setText("任务时间:"+startData);
                }
                break;
            case 2://服务
                itemManagePublishHolderBinding.tvManageMyPublishTime.setVisibility(View.VISIBLE);
                itemManagePublishHolderBinding.tvManageMyPublishTime.setText("任务时间:"+startData+"-"+endData);
                break;
        }
    }

    //设置界面
    private void setView(String myActivityTitle,ManagerMyPublishTaskBean.DataBean.ListBean data) {
           switch (myActivityTitle){
            case "技能管理":
                itemManagePublishHolderBinding.tvMyBtn.setText("发布");
              break;
            case "管理我发布的任务":
                int status = data.getStatus();
                switch (status){
                    case 0://不在架上
                        itemManagePublishHolderBinding.tvMyBtn.setText("下架");
                        itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#999999"));
                        break;
                    case 1:
                        itemManagePublishHolderBinding.tvMyBtn.setText("上架");
                        itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#31C6E4"));
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
                if(text.equals("上架")){
                    action = 0;
                    itemManagePublishHolderBinding.tvMyBtn.setText("下架");
                    itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#999999"));
                }else if(text.equals("下架")){
                    action = 1;
                    itemManagePublishHolderBinding.tvMyBtn.setText("上架");
                    itemManagePublishHolderBinding.tvMyBtn.setTextColor(Color.parseColor("#31C6E4"));
                }

                ManagerMyPublishTaskBean.DataBean.ListBean listBean = managePublishList.get(position);
                int id = listBean.getId();
                if (action!=-1) {
                    itemManagePublishModel.UpAndDown(id,action);
                }
            }
        });

    }


    public interface OnDeleteClickListener{
        void OnDeleteClick(int position);
    }

    private OnDeleteClickListener listener;
    public void setOnCBacklickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }


}
