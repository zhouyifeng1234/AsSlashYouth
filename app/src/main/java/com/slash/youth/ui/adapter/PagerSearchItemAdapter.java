package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.SearchNeedItem;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/6.
 */
public  class PagerSearchItemAdapter<T> extends BaseAdapter {
    private ArrayList<T> mListData;
    private static final int searchTitle = 0;
    private static final int searchResult = 1;
    private static final int searchMore = 2;
    private int loadMoreState;
    private int searchType;

    public PagerSearchItemAdapter(ArrayList<T> listData) {
        this.mListData = listData;
    }

    @Override
    public int getCount() {
        return mListData.size()+2;
    }

    @Override
    public T getItem(int position) {
        if(getItemViewType(position)==searchResult){
            return mListData.get(position);
        }else {
            return null;
        }

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
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.search_result_title, null);
                   holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    break;
                case searchResult://搜索结果
                    if(searchType == 3){
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
                    }else {
                        convertView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_needandservice, null);
                        holder.iv_item_listview_home_demand_avatar = (ImageView) convertView.findViewById(R.id.iv_item_listview_home_demand_avatar);
                        holder.tv_item_listview_home_demand_username = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_username);
                        holder.tv_item_listview_home_demand_title = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_title);
                        holder.tv_item_listview_home_demand_date = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_date);
                        holder.tv_search_value = (TextView) convertView.findViewById(R.id.tv_search_value);
                        holder.tv_search_line = (TextView) convertView.findViewById(R.id.tv_search_line);
                        holder.tv_search_consult = (TextView) convertView.findViewById(R.id.tv_search_consult);
                    }
                    break;
                case searchMore://搜索更多
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
      /*  switch (searchType){
            case 1:
                holder.tv_title.setText("需求");
            break;
            case 2:
                holder.tv_title.setText("服务");
                break;
            case 3:
                holder.tv_title.setText("找人");
                break;
        }*/

      return convertView;
    }
    //获取种类的数量
    @Override
    public int getViewTypeCount() {
        return 3;
    }

    //条目的种类
    @Override
    public int getItemViewType(int position) {
        int p = position % 5;
        if (p == 0)
            return searchTitle;
        else if (p < 4)
            return searchResult;
        else if (p < 5)
            return searchMore;
        else
            return searchTitle;
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
