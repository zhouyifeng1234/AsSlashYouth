package com.slash.youth.ui.holder;

import android.speech.tts.Voice;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

/**
 * Created by zss on 2016/9/19.
 */
public class SearchContentHolder extends BaseHolder<ItemSearchBean> {

    private LinearLayout mSearchContent;
    private TextView tv_searchcount;
    private boolean isShowRemove;
    private ImageView iv_remove;
    private  ItemSearchBean data;

    public SearchContentHolder(boolean isShowRemove) {
        this.isShowRemove = isShowRemove;
    }


    @Override
    public View initView() {
        mSearchContent = (LinearLayout) View.inflate(CommonUtils.getContext(), R.layout.item_activity_search_listview_tv, null);
         tv_searchcount = (TextView) mSearchContent.findViewById(R.id.tv_searchcontent);
        iv_remove = (ImageView) mSearchContent.findViewById(R.id.iv_remove);
        if(isShowRemove){
            iv_remove.setVisibility(View.VISIBLE);
        }
        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogKit.d("删除一个条目");
             /*   new onItemRemoveListener() {
                    @Override
                    public void onItemRemove(ItemSearchBean data, int index) {

                    }
                };   */
            }
        });

        return mSearchContent;
    }

    @Override
    public void refreshView(ItemSearchBean data) {
        this.data = data;
        tv_searchcount.setText(data.item);
        iv_remove.setVisibility(data.isShowRemoveBtn?View.VISIBLE:View.GONE);

    }

    //接口回掉
    public interface onItemRemoveListener{
        void onItemRemove(ItemSearchBean data ,int index);
    }

    public void setItemRemoveListener (final onItemRemoveListener listener ,final int index) {
       iv_remove.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               listener.onItemRemove(data,index);
           }
       });
    }

}
