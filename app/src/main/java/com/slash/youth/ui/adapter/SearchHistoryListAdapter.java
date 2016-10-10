package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.SearchContentHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Created by zss on 2016/9/19.搜索listview的适配器
 */
public class SearchHistoryListAdapter extends SlashBaseAdapter<ItemSearchBean> {
    private boolean isShow;
    public SearchHistoryListAdapter(ArrayList listData, boolean isShow) {

        super(buildData(listData,isShow));
        this.isShow = isShow;
    }

    private static ArrayList<ItemSearchBean> buildData(ArrayList<String> listData, boolean isShow ){
        ArrayList<ItemSearchBean> data = new ArrayList<ItemSearchBean>( );
        for(String str :listData ){
            ItemSearchBean bean =new ItemSearchBean();
            bean.item=str;
            LogKit.d("wwwwwwwwwwwwwwwwwww"+str);
            bean.isShowRemoveBtn=isShow;
            data.add( bean);
        }
        return data;
    }
    // zss,重写getview()方法
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            if (getItemViewType(position) == HOLDER_TYPE_MORE) {
                holder = getAddMoreHolder(position);
            } else {
                holder = getHolder(position);
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        if (getItemViewType(position) != HOLDER_TYPE_MORE) {
            //这不是最后一条
            holder.setData(getItem(position), position);
            //设置监听
            ( (SearchContentHolder)holder).setItemRemoveListener(new SearchContentHolder.onItemRemoveListener() {
                @Override
                public void onItemRemove(ItemSearchBean data, int index) {
                    getData().remove(index);
                    try{
                        File externalCacheDir = CommonUtils.getApplication().getExternalCacheDir();
                        File file = new File(externalCacheDir,"history.text");
                        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                        for (int i=0;i<getData().size();i++ ) {
                            bw.write(getData().get(i).item);
                            if (i==getData().size()-1){
                                break;
                            }
                            bw.newLine();
                        }
                        bw.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    SearchHistoryListAdapter.this.notifyDataSetChanged();
                }
            },position);
        } else {
            //最后一条数据
          /*  ClearHolder addMoreHolder = (ClearHolder) holder;
            addMoreHolder.show(isShow);*/
        //TODO

        }

        View rootView = holder.getRootView();
        if (rootView != null) {
            return rootView;
        } else {
            TextView textViewNull = new TextView(CommonUtils.getContext());
            textViewNull.setText("Null");
            return textViewNull;
        }
    }
    public SearchHistoryListAdapter(ArrayList listData) {
        super(listData);
    }

    @Override
    public ArrayList onLoadMore() {
        return null;
    }

    //TODO ZSS 显示每一个item删除
    public  void showRemoveBtn(boolean isShowRemoveBtn){
        ArrayList<ItemSearchBean> listData=getData();
        for(ItemSearchBean bean:listData){
            bean.isShowRemoveBtn=isShowRemoveBtn;
        }
        setData(listData);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SearchContentHolder(isShow);
    }

    //TODO zss 显示删除所有的item
    public void cleanData(){
        ArrayList<ItemSearchBean> data = getData();
        data.clear();
    }

}
