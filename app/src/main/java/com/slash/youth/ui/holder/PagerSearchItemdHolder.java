package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.TextView;
import com.slash.youth.domain.SearchNeedItem;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zss on 2016/9/6.
 */
public class PagerSearchItemdHolder extends BaseHolder<SearchNeedItem> {
    @Override
    public View initView() {
        //加载布局
       // ItemListviewSearchItemBinding mItemListviewSearchItemBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_listview_search_item, null, false);

        //return mItemListviewSearchItemBinding.getRoot();
        return  new TextView(CommonUtils.getContext());
    }

    @Override
    public void refreshView(SearchNeedItem data) {


    }
}
