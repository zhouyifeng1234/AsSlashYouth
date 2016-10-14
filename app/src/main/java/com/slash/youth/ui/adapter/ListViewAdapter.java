package com.slash.youth.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ViewHolder;

import java.util.ArrayList;

/**
 * Created by zss on 2016/10/11.
 */
public class ListViewAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private int mPotion;
    public ListViewAdapter(ArrayList mData,int mPotion) {
        this.mData = mData;
        this.mPotion = mPotion;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
        }else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.setData(getItem(position),mPotion==position);

        return holder.rootView;
    }

    static class ViewHolder{
        View rootView;
        TextView item;
        LinearLayout ll;

        public ViewHolder() {
            rootView = View.inflate(CommonUtils.getContext(),R.layout.item_search_listview, null);
            item = (TextView) rootView.findViewById(R.id.tv_item);
            ll = (LinearLayout) rootView.findViewById(R.id.ll_bg);
            rootView.setTag(this);
        }
        public void setData(String itemText,boolean isChecked){
            if(isChecked){
                ll.setBackgroundResource(R.mipmap.select_bg);
                item.setText(itemText);
            }else {
                ll.setBackgroundColor(Color.WHITE);
                item.setText(itemText);
            }
        };
    }
}
