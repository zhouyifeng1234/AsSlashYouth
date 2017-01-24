package com.slash.youth.ui.adapter;

import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.MyCollectionHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

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
                //埋点
                MobclickAgent.onEvent(CommonUtils.getContext(), CustomEventAnalyticsUtils.EventID.MINE_CLICK_MY_COLLECT_DELETE);

                index = position;
                showDialog();
            }
        });
    }

    private void showDialog() {
        DialogUtils.showDialogFive(myCollectionActivity, "删除", "是否删除该任务", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                if(index!=-1){
                    MyCollectionBean.DataBean.ListBean listBean = listData.get(index);
                    int type = listBean.getType();
                    long tid = listBean.getTid();
                    MyManager.onDeleteMyCollectionList(new onAddMyCollectionList(),type,tid);
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
                        ToastUtils.shortToast("删除成功");
                        listData.remove(index);
                        notifyDataSetChanged();
                        break;
                    case 0:
                        ToastUtils.shortToast("删除失败");
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
