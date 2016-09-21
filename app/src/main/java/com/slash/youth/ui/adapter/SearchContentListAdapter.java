package com.slash.youth.ui.adapter;

import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.SearchContentHolder;

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
        setData( listData);
    }

    @Override
    public BaseHolder getHolder(int position) {
        return new SearchContentHolder(isShow);
    }
    //TODO zss 显示删除所有的item
  /*  public void cleanData(){
        ArrayList<ItemSearchBean> data = getData();
        data.clear();
    }*/

   /* private void getMoreHolder(){
        AddCleanHolder newHolder = new AddCleanHolder();
    }*/


}
