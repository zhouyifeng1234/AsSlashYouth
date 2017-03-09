package com.slash.youth.ui.adapter;

import android.content.Context;

import com.slash.youth.R;
import com.slash.youth.domain.Bean;
import com.slash.youth.utils.ViewHolder;

import java.util.List;

/**
 * Created by zss on 2015-10-10
 */

public class SearchAdapter extends CommonAdapter<Bean> {

    public SearchAdapter(Context context, List<Bean> data, int layoutId) {
        super(context, data, layoutId);
    }

    @Override
    public void convert(ViewHolder holder, int position) {
        /*holder.setImageResource(R.id.item_search_iv_icon,mData.get(position).getIconId())
                .setText(R.id.item_search_tv_title,mData.get(position).getTitle())
                .setText(R.id.item_search_tv_content,mData.get(position).getContent())
                .setText(R.id.item_search_tv_comments,mData.get(position).getComments());*/


        if(mData.size()-1 == position){
            //最后一行就新的holder
            holder.setText(R.id.tv_searchcontent,mData.get(position).getContent());
        }else {
            //不是最后一行
            holder.setImageResource(R.id.iv_remove,mData.get(position).getIconId())
                    .setText(R.id.tv_searchcontent,mData.get(position).getContent());
        }

    }
}
