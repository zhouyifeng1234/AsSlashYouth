package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.SearchContentHolder;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/19.搜索listview的适配器
 */
public class SearchContentListAdapter extends SlashBaseAdapter<ItemSearchBean> {
    private boolean isShow;
    public SearchContentListAdapter(ArrayList listData,boolean isShow) {

        super(buildData(listData,isShow));
        this.isShow = isShow;
    }

    private static ArrayList<ItemSearchBean> buildData(ArrayList<String> listData, boolean isShow ){
        ArrayList<ItemSearchBean> data = new ArrayList<ItemSearchBean>( );
        for(String str :listData ){
            ItemSearchBean bean =new ItemSearchBean();
            bean.item=str;
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
                    SearchContentListAdapter.this.notifyDataSetChanged();
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
    public SearchContentListAdapter(ArrayList listData) {
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

  /*  private void getMoreHolder(){
         NewHolder newHolder = new NewHolder();
    }*/


}
