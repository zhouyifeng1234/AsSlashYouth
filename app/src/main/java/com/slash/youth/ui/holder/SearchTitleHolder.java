package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SearchAllAdapter;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.SpUtils;

/**
 * Created by acer on 2016/12/7.
 */
public class SearchTitleHolder extends SearchViewHolder<String> {

    private int type;
    public View mRootView;
    private TextView  tv_title;
    private LinearLayout linearLayout;


    public SearchTitleHolder(int type) {
        this.type = type;
    }

    @Override
    public View initView() {
        mRootView = View.inflate(CommonUtils.getContext(), R.layout.search_result_title, null);
        tv_title = (TextView) mRootView.findViewById(R.id.tv_title);
        linearLayout = (LinearLayout) mRootView.findViewById(R.id.ll_title_view);
        return mRootView;
    }

    @Override
    public void refreshView(String data,int position) {
        switch (type) {
            case SearchAllAdapter.searchTitle:
                tv_title.setText(SearchManager.SEARCH_ITEM_TITLE_DEMEND);
                break;
            case SearchAllAdapter.serviceTitle:
               tv_title.setText(SearchManager.SEARCH_ITEM_TITLE_SERVICE);
                break;
            case SearchAllAdapter.personTitle:
               tv_title.setText(SearchManager.SEARCH_ITEM_TITLE_PERSON);
                break;
        }
    }
}




