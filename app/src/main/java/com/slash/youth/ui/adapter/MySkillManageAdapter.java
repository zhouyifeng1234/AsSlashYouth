package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.domain.SkillManagerBean;
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
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class MySkillManageAdapter extends SlashBaseAdapter<SkillManagerBean.DataBean.ListBean> {

    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList;
    private MySkillManageHolder mySkillManageHolder;
    private int index = -1;

    public MySkillManageAdapter(ArrayList<SkillManagerBean.DataBean.ListBean> listData, MySkillManageActivity mySkillManageActivity,ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList) {
        super(listData);
        this.mySkillManageActivity = mySkillManageActivity;
        this.skillManageList = skillManageList;
    }

    @Override
    public ArrayList<SkillManagerBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        mySkillManageHolder = new MySkillManageHolder( mySkillManageActivity, skillManageList);
        deleteItem();
        return mySkillManageHolder;
    }

    //删除条目
    public void deleteItem() {
        mySkillManageHolder.setOnDeleteCklickListener(new MySkillManageHolder.OnDeleteClickListener() {
            @Override
            public void OnDeleteClick(int position) {
                index = position;
                showDialog();
            }
        });
    }

    private void showDialog() {
        DialogUtils.showDialogFive(mySkillManageActivity,"删除","确认删除该技能？",new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                if(index!=-1){
                    SkillManagerBean.DataBean.ListBean listBean = skillManageList.get(index);
                    long id = listBean.getId();
                    MyManager.onDeteleSkillManagerItem(new onAddMyCollectionList(),id);
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
                switch (status){
                    case 1://ok
                        skillManageList.remove(index);
                        notifyDataSetChanged();
                        break;
                    case 0:
                        ToastUtils.shortToast("删除失败");
                        break;
                }
            }
        }

        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }
}
