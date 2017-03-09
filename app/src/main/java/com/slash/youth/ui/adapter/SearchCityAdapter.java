package com.slash.youth.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;
import java.util.ArrayList;

/**
 * Created by zss on 2016/10/12.
 */
public class SearchCityAdapter extends BaseAdapter {
    private ArrayList<String> mData;
    private int mPotion;
    public SearchCityAdapter(ArrayList<String> mData,int mPotion ) {
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
        TextView itemChecked;
        public ViewHolder() {
            rootView = View.inflate(CommonUtils.getContext(), R.layout.item_gridview_city, null);
            item = (TextView) rootView.findViewById(R.id.tv_city);
            itemChecked = (TextView) rootView.findViewById(R.id.tv_city_checked);
            rootView.setTag(this);
        }
        public void setData(String city,boolean isChecked){
            if(isChecked){
                itemChecked.setVisibility(View.VISIBLE);
                itemChecked.setText(city);
                item.setVisibility(View.GONE);
            }else {
                item.setVisibility(View.VISIBLE);
                item.setText(city);
                itemChecked.setVisibility(View.GONE);

            }
        };
    }
}
