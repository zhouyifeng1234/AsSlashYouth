package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.SearchNeedItem;
import com.slash.youth.domain.SearchPersonItem;
import com.slash.youth.domain.SearchServiceItem;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public  class PagerSearchItemAdapter<T> extends BaseAdapter {
    private ArrayList<SearchNeedItem> mNeedData;
    private ArrayList<SearchServiceItem> mServiceData;
    private ArrayList<SearchPersonItem> mPersonData;
    private static final int searchTitle = 0;
    private static final int serviceTitle = 1;
    private static final int personTitle = 2;
    private static final int searchResult = 3;
    private static final int serviceResult = 4;
    private static final int personResult = 5;
    private static final int searchMore = 6;
    private static final int personMore = 7;
    private static final int serviceMore = 8;

    private int loadMoreState;
    private int searchType;

    public PagerSearchItemAdapter(ArrayList<SearchNeedItem> needData,
                                  ArrayList<SearchServiceItem> serviceData,
                                  ArrayList<SearchPersonItem> personData) {
        this.mNeedData = needData;
        this.mServiceData = serviceData;
        this.mPersonData = personData;
    }

    @Override
    public int getCount() {
        return mNeedData.size()+mServiceData.size()+mPersonData.size()+2;
    }

    @Override
    public Object getItem(int position) {
       /*if(getItemViewType(position)==searchResult){
            return mNeedData.get(position);
        }else {
            return null;
        }*/

        if(position ==0){
            return null;//title
        }else if(position>0||position<(mNeedData.size()+1)){
          return  mNeedData.get(position-1);
        }else if(position==(mNeedData.size()+1)){
            return null;//addMore
        }else if(position==(mNeedData.size()+2)){
            return null;//title
        }else if(position>(mNeedData.size()+2)||position<((mNeedData.size()+2)+mServiceData.size())){
            return mServiceData.get(position-(mNeedData.size()+2));
        }else if((mNeedData.size()+mServiceData.size()+3)==position){
            return null;//addMore
        }else if((mNeedData.size()+mServiceData.size()+4)==position){
            return null;//title
        }else if(position>(mNeedData.size()+mServiceData.size()+4)||position<((mNeedData.size()+mServiceData.size()+4)+mPersonData.size())){
            return mPersonData.get(position-(mNeedData.size()+mServiceData.size()+4));
        }else if(position == (mNeedData.size()+mServiceData.size()+mPersonData.size()+5)){
            return null;//addMore
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        //设置holder
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new viewHolder();
            switch (type) {
                case searchTitle://搜索标题
                case serviceTitle:
                case personTitle:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.search_result_title, null);
                   holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    break;
                case searchResult://搜索结果
                case serviceResult:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_needandservice, null);
                    holder.iv_item_listview_home_demand_avatar = (ImageView) convertView.findViewById(R.id.iv_item_listview_home_demand_avatar);
                    holder.tv_item_listview_home_demand_username = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_username);
                    holder.tv_item_listview_home_demand_title = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_title);
                    holder.tv_item_listview_home_demand_date = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_date);
                    holder.tv_search_value = (TextView) convertView.findViewById(R.id.tv_search_value);
                    holder.tv_search_line = (TextView) convertView.findViewById(R.id.tv_search_line);
                    holder.tv_search_consult = (TextView) convertView.findViewById(R.id.tv_search_consult);
                    break;
                case personResult:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_person, null);
                    holder.tv_search_person_name = (TextView) convertView.findViewById(R.id.tv_search_person_name);
                    holder.iv_search_person = (ImageView) convertView.findViewById(R.id.iv_search_person);
                    holder.tv_search_person_zhiye = (TextView) convertView.findViewById(R.id.tv_search_person_zhiye);
                    holder.iv_search_v = (ImageView) convertView.findViewById(R.id.iv_search_v);
                    holder.iv_jiahao = (ImageView) convertView.findViewById(R.id.iv_jiahao);
                    holder.iv_star = (ImageView) convertView.findViewById(R.id.iv_star);
                    holder.tv_zhiye1 = (TextView) convertView.findViewById(R.id.tv_zhiye1);
                    holder.tv_zhiye2 = (TextView) convertView.findViewById(R.id.tv_zhiye2);
                    holder.tv_zhiye3 = (TextView) convertView.findViewById(R.id.tv_zhiye3);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    break;
                case searchMore://搜索更多
                case serviceMore:
                case personMore:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.search_result_more, null);
                    holder.tv_search_more = (TextView) convertView.findViewById(R.id.tv_search_more);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        //判断，不同的数据就有不同的展示
        switch (searchType){
            case searchMore:
                holder.tv_title.setText("需求");
            break;
            case serviceTitle:
                holder.tv_title.setText("服务");
                break;
            case personTitle:
                holder.tv_title.setText("找人");
                break;
        }



      return convertView;
    }
    //获取种类的数量
    @Override
    public int getViewTypeCount() {
        return 6;
    }

    //条目的种类
    @Override
    public int getItemViewType(int position) {
        /*int p = position % 5;
        if (p == 0)
            return searchTitle;
        else if (p < 4)
            return searchResult;
        else if (p < 5)
            return searchMore;
        else
            return searchTitle;*/

        if (position == 0) {
            return searchTitle;
        }else if(position==(mNeedData.size()+2)) {
            return serviceTitle;
        }else if(position==(mNeedData.size()+mServiceData.size()+4)) {
            return personTitle;
        }else if (position>0||position<(mNeedData.size()+1)) {
            return searchResult;
        }else if (position>(mNeedData.size()+2)||position<((mNeedData.size()+2)+mServiceData.size())) {
            return serviceResult;
        }else if(position>(mNeedData.size()+mServiceData.size()+4)||position<((mNeedData.size()+mServiceData.size()+4)+mPersonData.size())) {
            return personResult;
        }else if(position==(mNeedData.size()+1)) {
            return searchMore;
        }else if((mNeedData.size()+mServiceData.size()+3)==position) {
            return serviceMore;
        }else if(position == (mNeedData.size()+mServiceData.size()+mPersonData.size()+5)) {
            return personMore;
        }
      return -1;
    }
   /* //搜索更多
    private viewHolder getSearchMoreHolder(int position) {
        return null;
    }
    //搜索结果
    private viewHolder getSearchResultHolder(int position) {
        return null;
    }
    //搜索标题
    private viewHolder getSearchTitleHolder(int position) {
        return null;
    }
*/
    static class viewHolder{
        TextView tv_search_person_name;
        TextView tv_search_more;
        TextView tv_title;
        TextView tv_zhiye1;
        TextView tv_zhiye2;
        TextView tv_zhiye3;
        TextView tv_item_listview_home_demand_username;
        TextView tv_item_listview_home_demand_title;
        TextView tv_item_listview_home_demand_date;
        TextView tv_search_value;
        TextView tv_search_line;
        TextView tv_search_consult;
        TextView tv_time;
        TextView tv_search_person_zhiye;
        ImageView iv_search_v;
        ImageView iv_jiahao;
        ImageView iv_item_listview_home_demand_avatar;
        ImageView iv_star;
        ImageView iv_search_person;
    }

}
