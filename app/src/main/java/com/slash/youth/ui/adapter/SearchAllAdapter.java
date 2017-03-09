package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.holder.SearchViewHolder;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by acer on 2016/12/6.
 */
public abstract class SearchAllAdapter extends BaseAdapter  {
    private  ArrayList<SearchAllBean.DataBean.DemandListBean> demandListBeen;
    private  ArrayList<SearchAllBean.DataBean.ServiceListBean> serviceListBeen;
    private ArrayList<SearchAllBean.DataBean.UserListBean> userListBeen;
    public static final int searchTitle = 0;
    public static final int serviceTitle = 1;
    public static final int personTitle = 2;
    private static final int searchResult = 3;
    private static final int serviceResult = 4;
    private static final int personResult = 5;
    public static final int searchMore = 6;
    public static final int personMore = 7;
    public static final int serviceMore = 8;

    public SearchAllAdapter(ArrayList<SearchAllBean.DataBean.DemandListBean> demandListBeen, ArrayList<SearchAllBean.DataBean.ServiceListBean> serviceListBeen, ArrayList<SearchAllBean.DataBean.UserListBean> userListBeen) {
        this.demandListBeen = demandListBeen;
        this.serviceListBeen = serviceListBeen;
        this.userListBeen = userListBeen;
    }

    @Override
    public int getCount() {
        int totalCount = demandListBeen.size() + serviceListBeen.size() + userListBeen.size() + 6;
        return totalCount;
    }

    @Override
    public Object getItem(int position) {
        if(position ==0){
            return null;//title
        }else if(position>0&&position<=(demandListBeen.size())){
            return  demandListBeen.get(position-1);
        }else if(position==(demandListBeen.size()+1)){
            return null;//addMore
        }else if(position==(demandListBeen.size()+2)){
            return null;//title
        }else if(position>(demandListBeen.size()+2)&&position<=((demandListBeen.size()+2)+serviceListBeen.size())){
            return serviceListBeen.get((position-(demandListBeen.size()+2)-1));
        }else if((demandListBeen.size()+serviceListBeen.size()+3)==position){
            return null;//addMore
        }else if((demandListBeen.size()+serviceListBeen.size()+4)==position){
            return null;//title
        }else if(position>(demandListBeen.size()+serviceListBeen.size()+4)&&position<=((demandListBeen.size()+serviceListBeen.size()+4)+userListBeen.size())){
            return userListBeen.get(position-(demandListBeen.size()+serviceListBeen.size()+4)-1);
        }else if(position == (demandListBeen.size()+serviceListBeen.size()+userListBeen.size()+5)){
            return null;//addMore
        }
        return null;
    }

    //获取种类的数量
    @Override
    public int getViewTypeCount() {
        return 9;
    }

    //条目的种类
    @Override
    public int getItemViewType(int position) {
        final int needTitlePosition=0;
        final int needMorePosition = needTitlePosition+demandListBeen.size()+1;
        final int serviceTitlePosition=needMorePosition+1;
        final int serviceMroePosition=serviceTitlePosition+serviceListBeen.size()+1;
        final int userTitlePosition = serviceMroePosition+1;
        final int userMorePosition = userTitlePosition+userListBeen.size()+1;
        if (position==needTitlePosition){
            return searchTitle;
        }else if (position>needTitlePosition&&position<needMorePosition){
            return searchResult;
        }else if (position==needMorePosition){
            return searchMore;
        }else

        if (position==serviceTitlePosition){
            return serviceTitle;
        }else if (position>serviceTitlePosition&&position<serviceMroePosition){
            return serviceResult;
        }else if (position==serviceMroePosition){
            return serviceMore;
        }else

        if (position==userTitlePosition){
            return personTitle;
        }else if (position>userTitlePosition&&position<userMorePosition){
            return personResult;
        }else if (position==userMorePosition){
            return personMore;
        }
        return -1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        SearchViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case searchTitle://搜索标题
                case serviceTitle:
                case personTitle:
                     holder= getTitleHolder(position,type);
                    break;
                case searchResult://搜索结果
                    holder=   getDemandHolder(position,type);
                    break;
                case serviceResult:
                    holder=  getServiceHolder(position,type);
                    break;
                case personResult:
                    holder= getUserHolder(position,type);
                    break;
                case searchMore://搜索更多
                case serviceMore:
                case personMore:
                    holder= getMoreHolder(position,type);
                    break;
            }
        }else {
            holder = (SearchViewHolder) convertView.getTag();
        }

        holder.setData(getItem(position), position);

        return holder.getRootView();
    }

    public abstract  SearchViewHolder getTitleHolder(int position,int type);

    public abstract  SearchViewHolder getDemandHolder(int position,int type);

    public abstract  SearchViewHolder getServiceHolder(int position,int type);

    public abstract  SearchViewHolder getUserHolder(int position,int type);

    public abstract  SearchViewHolder getMoreHolder(int position,int type);
}
