package com.slash.youth.ui.adapter;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import com.slash.youth.R;
import com.slash.youth.databinding.DeleteItemDialogBinding;
import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SetBean;
import com.slash.youth.engine.MyManager;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.ui.activity.MyCollectionActivity;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.MyCollectionHolder;
import com.slash.youth.ui.viewmodel.DeleteItemDialogModel;
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
    private int type;

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
        type = 3;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(myCollectionActivity);
        DeleteItemDialogBinding deleteItemDialogBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.delete_item_dialog, null, false);
        DeleteItemDialogModel deleteItemDialogModel = new DeleteItemDialogModel(deleteItemDialogBinding,type);
        deleteItemDialogBinding.setDeleteItemDialogModel(deleteItemDialogModel);
        dialogBuilder.setView(deleteItemDialogBinding.getRoot());
        AlertDialog dialogSearch = dialogBuilder.create();
        dialogSearch.show();
        deleteItemDialogModel.currentDialog = dialogSearch;
        dialogSearch.setCanceledOnTouchOutside(false);
        Window dialogSubscribeWindow = dialogSearch.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        dialogParams.width = CommonUtils.dip2px(276);//宽度
        dialogParams.height = CommonUtils.dip2px(130);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
        deleteItemDialogModel.setOnDeleteCklickListener(new DeleteItemDialogModel.OnDeleteClickListener() {
            @Override
            public void OnDeleteClick() {
                if(index!=-1){
                    MyCollectionBean.DataBean.ListBean listBean = listData.get(index);
                    int type = listBean.getType();
                    long tid = listBean.getTid();
                    MyManager.onDeleteMyCollectionList(new onAddMyCollectionList(),type,tid);
                }
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
