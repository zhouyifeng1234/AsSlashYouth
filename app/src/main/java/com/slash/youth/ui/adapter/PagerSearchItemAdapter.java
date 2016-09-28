package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
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
                   holder.textView = (TextView) convertView.findViewById(R.id.tv_title);
                    break;
                case searchResult://搜索结果
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_needandservice, null);
                    holder.imageView = (ImageView) convertView.findViewById(R.id.iv_item_listview_home_demand_avatar);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_username);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_title);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_date);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_search_value);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_search_line);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_search_consult);
                    break;
                case searchMore://搜索更多
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.search_result_more, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_search_more);
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }

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
        TextView textView;
        ImageView imageView;

    }

}
