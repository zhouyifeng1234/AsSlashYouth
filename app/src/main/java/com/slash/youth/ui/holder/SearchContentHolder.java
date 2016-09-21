package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.ItemSearchBean;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/9/19.
 */
public class SearchContentHolder extends BaseHolder<ItemSearchBean> {

    private LinearLayout mSearchContent;
    private TextView tv_searchcount;
    private boolean isShowRemove;
    private ImageView iv_remove;

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
        return mSearchContent;
    }

    @Override
    public void refreshView(ItemSearchBean data) {
        tv_searchcount.setText(data.item);

        iv_remove.setVisibility(data.isShowRemoveBtn?View.VISIBLE:View.GONE);

    }

}
