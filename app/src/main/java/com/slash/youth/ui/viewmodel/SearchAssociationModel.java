package com.slash.youth.ui.viewmodel;

import android.app.AlertDialog;
import android.app.Dialog;
import android.databinding.BaseObservable;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.slash.youth.R;
import com.slash.youth.databinding.DialogPasswordBinding;
import com.slash.youth.databinding.DialogSearchCleanBinding;
import com.slash.youth.databinding.SearchListviewAssociationBinding;
import com.slash.youth.gen.SearchHistoryEntityDao;
import com.slash.youth.ui.adapter.SearchHistoryListAdapter;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.DialogUtils;

/**
 * Created by zss on 2016/10/17.
 */
public class SearchAssociationModel extends BaseObservable {
    private SearchListviewAssociationBinding searchListviewAssociationBinding;
    private SearchHistoryEntityDao searchHistoryEntityDao;
    private SearchHistoryListAdapter adapter;

    public SearchAssociationModel(SearchListviewAssociationBinding searchListviewAssociationBinding, SearchHistoryEntityDao searchHistoryEntityDao,SearchHistoryListAdapter adapter) {
        this.searchListviewAssociationBinding = searchListviewAssociationBinding;
        this.searchHistoryEntityDao = searchHistoryEntityDao;
        this.adapter = adapter;
    }

    //显示对话框
    public void showDialog(View v) {
        //创建AlertDialog
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommonUtils.getCurrentActivity());
        //数据绑定填充视图
        DialogSearchCleanBinding dialogSearchCleanBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.dialog_search_clean, null, false);
        //创建数据模型
        DialogSearchCleanModel dialogSearchCleanModel = new DialogSearchCleanModel(dialogSearchCleanBinding,searchHistoryEntityDao,adapter);
        //绑定数据模型
        dialogSearchCleanBinding.setDialogSearchCleanModel(dialogSearchCleanModel);
        //设置布局
        dialogBuilder.setView(dialogSearchCleanBinding.getRoot());//getRoot返回根布局
        //创建dialogSearch
        AlertDialog dialogSearch = dialogBuilder.create();
        dialogSearch.show();
        dialogSearchCleanModel.currentDialog = dialogSearch;
        dialogSearch.setCanceledOnTouchOutside(false);//设置点击Dialog外部任意区域关闭Dialog
        Window dialogSubscribeWindow = dialogSearch.getWindow();
        WindowManager.LayoutParams dialogParams = dialogSubscribeWindow.getAttributes();
        //定义显示的dialog的宽和高
        dialogParams.width = CommonUtils.dip2px(280);//宽度
        dialogParams.height = CommonUtils.dip2px(200);//高度
        dialogSubscribeWindow.setAttributes(dialogParams);
//        dialogSubscribeWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialogSubscribeWindow.setDimAmount(0.1f);//dialog的灰度
//        dialogBuilder.show();
    }
}
