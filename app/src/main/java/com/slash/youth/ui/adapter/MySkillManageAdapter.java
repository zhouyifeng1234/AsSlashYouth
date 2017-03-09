package com.slash.youth.ui.adapter;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.tool.reflection.Callable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.databinding.DeleteItemDialogBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
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
import com.slash.youth.ui.viewmodel.DeleteItemDialogModel;
import com.slash.youth.ui.viewmodel.DialogSearchCleanModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.CustomEventAnalyticsUtils;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class MySkillManageAdapter extends SlashBaseAdapter<SkillManagerBean.DataBean.ListBean> {

    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList;
    private MySkillManageHolder mySkillManageHolder;
    private int index = -1;
    private String avater;
    private String name;
    private int type;

    public MySkillManageAdapter(ArrayList<SkillManagerBean.DataBean.ListBean> listData, MySkillManageActivity mySkillManageActivity,ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList,String avater,String name) {
        super(listData);
        this.mySkillManageActivity = mySkillManageActivity;
        this.skillManageList = skillManageList;
        this.avater = avater;
        this.name = name;
    }

    @Override
    public ArrayList<SkillManagerBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        mySkillManageHolder = new MySkillManageHolder( mySkillManageActivity, skillManageList,avater,name);
        deleteItem();
        return mySkillManageHolder;
    }

    //删除条目
    public void deleteItem() {
        mySkillManageHolder.setOnDeleteCklickListener(new MySkillManageHolder.OnDeleteClickListener() {
            @Override
            public void OnDeleteClick(int position) {
                index = position;
                showDialog(index);
            }
        });
    }

    private void showDialog(final int index) {
        type = 1;
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
                    SkillManagerBean.DataBean.ListBean listBean = skillManageList.get(index);
                    long id = listBean.getId();
                    MyManager.onDeteleSkillManagerItem(new onAddMyCollectionList(),id);
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
