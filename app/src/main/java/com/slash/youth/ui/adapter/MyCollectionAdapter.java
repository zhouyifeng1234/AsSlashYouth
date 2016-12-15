package com.slash.youth.ui.adapter;

import android.graphics.Color;
import android.support.v4.view.PagerTitleStrip;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.domain.ManagerMyPublishTaskBean;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.domain.TransactionRecoreBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ManagePublishHolder;
import com.slash.youth.ui.holder.MyCollectionHolder;
import com.slash.youth.ui.holder.MySkillManageHolder;
import com.slash.youth.ui.holder.SearchContentHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ViewHolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/3.
 */
public class MyCollectionAdapter extends SlashBaseAdapter<MyCollectionBean.DataBean.ListBean>{

    private ArrayList<MyCollectionBean.DataBean.ListBean> listData;
    private MyCollectionHolder myCollectionHolder;
    private  int index=-1;
    private MyCollectionActivity myCollectionActivity;
    private boolean isSuccessful;

    public MyCollectionAdapter(ArrayList<MyCollectionBean.DataBean.ListBean> listData,MyCollectionActivity myCollectionActivity) {
        super(listData);
        this.listData = listData;
        this.myCollectionActivity = myCollectionActivity;
    }

    @Override
    public ArrayList<MyCollectionBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        myCollectionHolder = new MyCollectionHolder(listData);
        deleteItem();
        return myCollectionHolder;
    }

    //删除
    public void deleteItem() {
        myCollectionHolder.setOnDeleteClickListener(new MyCollectionHolder.OnDeleteClickListener() {
            @Override
            public void OnDeleteClick(int position) {
                index = position;
                showDialog();
            }
        });
    }

    private void showDialog() {
        DialogUtils.showDialogFive(myCollectionActivity, "是否删除该任务", "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                if(index!=-1){
                    MyCollectionBean.DataBean.ListBean listBean = listData.get(index);
                    int type = listBean.getType();
                    long tid = listBean.getTid();
                    MyManager.onDeleteMyColl,ectionList(new onAddMyCollectionList(),type,tid);
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
                switch (status) {
                    case 1:
                        listData.remove(index);
                        notifyDataSetChanged();
                        break;
                    case 0:
                        LogKit.d("删除失败");
                        break;
                }
                LogKit.d("status:"+status);
            }
        }
        @Override
        public void executeResultError(String result) {
            LogKit.d("result:"+result);
        }
    }

}
