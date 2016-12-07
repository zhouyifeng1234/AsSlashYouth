package com.slash.youth.ui.holder;

import android.speech.tts.Voice;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/9/19.
 */
public class SearchContentHolder extends BaseHolder<ItemSearchBean> {

    private LinearLayout mSearchContent;
    private TextView tv_searchcount;
    private RelativeLayout iv_remove;
    private ArrayList<ItemSearchBean> arrayList;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();

    public SearchContentHolder(ArrayList<ItemSearchBean> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public View initView() {
        mSearchContent = (LinearLayout) View.inflate(CommonUtils.getContext(), R.layout.item_activity_search_listview_tv, null);
         tv_searchcount = (TextView) mSearchContent.findViewById(R.id.tv_searchcontent);
        iv_remove = (RelativeLayout) mSearchContent.findViewById(R.id.iv_remove);
        return mSearchContent;
    }

    @Override
    public void refreshView(ItemSearchBean data) {
        String item = data.getItem();
        boolean showRemoveBtn = data.isShowRemoveBtn();
         tv_searchcount.setText(item);
        iv_remove.setVisibility(showRemoveBtn?View.VISIBLE:View.GONE);
        currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(showRemoveBtn?View.VISIBLE:View.GONE);
        if(arrayList.size() == 0){
            currentActivity.searchListviewAssociationBinding.tvCleanAll.setVisibility(View.GONE);
        }
    }

    //删除
    @Override
    public void setData(ItemSearchBean data, final int position) {
        super.setData(data, position);
        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemRemove(position);
            }
        });
    }

    //接口回掉
    public interface onItemRemoveListener{
        void onItemRemove(int index);
    }

    private onItemRemoveListener listener;
    public void setItemRemoveListener (onItemRemoveListener listener) {
        this.listener = listener;
    }

}
