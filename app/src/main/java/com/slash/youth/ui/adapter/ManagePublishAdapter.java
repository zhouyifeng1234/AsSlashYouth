package com.slash.youth.ui.adapter;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.BR;
import com.slash.youth.R;
import com.slash.youth.databinding.DeleteItemDialogBinding;
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
import com.slash.youth.ui.viewmodel.DeleteItemDialogModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

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
    private int type;

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
         type = 2;
         AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mySkillManageActivity);
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
                     ManagerMyPublishTaskBean.DataBean.ListBean listBean = managePublishList.get(index);
                     long id = listBean.getId();
                     MyManager.onDeleteManagerMyPublishTaskItem(new onAddMyCollectionList(),id);
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
                switch (status){
                    case 0://删除失败
                        ToastUtils.shortToast("删除失败");
                        break;
                    case 1://删除成功
                        managePublishList.remove(index);
                        notifyDataSetChanged();
                        break;
                    case 2://在架上无法删除
                        ToastUtils.shortToast("在架上无法删除");
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
