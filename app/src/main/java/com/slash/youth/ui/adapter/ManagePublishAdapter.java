package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ManagePublishHolder;
import com.slash.youth.ui.holder.MySkillManageHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

import io.rong.message.VoiceMessage;

/**
 * Created by acer on 2016/11/3.
 */
public class ManagePublishAdapter extends SlashBaseAdapter<ManagerMyPublishTaskBean.DataBean.ListBean> {

    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList;
    public ManagePublishHolder managePublishHolder;
    private int index = -1;

    public ManagePublishAdapter(ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> listData, MySkillManageActivity mySkillManageActivity,ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> managePublishList) {
        super(listData);
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
    }

    @Override
    public ArrayList<ManagerMyPublishTaskBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder( int position) {
        managePublishHolder = new ManagePublishHolder(mySkillManageActivity, managePublishList);
        deleteItem();
        return managePublishHolder;
    }

    //删除
    public void deleteItem() {
        managePublishHolder.setOnCBacklickListener(new ManagePublishHolder.OnDeleteClickListener() {
            @Override
            public void OnDeleteClick(int position) {
                 index = position;
                showDialog();
            }
        });
    }

     private void showDialog() {
        DialogUtils.showDialogFive(mySkillManageActivity, "是否删除该任务", "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                if(index!=-1){
                ManagerMyPublishTaskBean.DataBean.ListBean listBean = managePublishList.get(index);
                int type = listBean.getType();
                long tid = listBean.getTid();
                managePublishList.remove(index);
                notifyDataSetChanged();
                MyManager.onDeleteManagerMyPublishTaskItem(new onAddMyCollectionList(),type,tid);

                }
            }

            @Override
            public void CancleDown() {
                LogKit.d("取消删除");
            }
        });
    }

    public class onAddMyCollectionList implements BaseProtocol.IResultExecutor<SetBean> {
        @Override
        public void execute(SetBean dataBean) {
            int rescode = dataBean.rescode;
            if(rescode == 0){
                SetBean.DataBean data = dataBean.getData();
                int status = data.getStatus();
                LogKit.d("status:"+status);
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }


}
