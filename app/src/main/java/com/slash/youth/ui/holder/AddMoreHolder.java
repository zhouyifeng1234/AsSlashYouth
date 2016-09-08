package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.LinearLayout;

import com.slash.youth.R;
import com.slash.youth.utils.CommonUtils;

public class AddMoreHolder extends BaseHolder<Integer> {

    public static final int STATE_MORE_MORE = 1001;
    public static final int STATE_MORE_ERROR = 1002;
    public static final int STATE_MORE_EMPTY = 1003;
    private LinearLayout mLlAddMoreMore;
    private LinearLayout mLlAddMoreError;

    public AddMoreHolder(int loadMoreState, int position) {
        setData(loadMoreState, position);
    }

    @Override
    public View initView() {
        View view = CommonUtils
                .inflate(R.layout.item_listview_addmore);
        mLlAddMoreMore = (LinearLayout) view
                .findViewById(R.id.ll_item_listview_addmore_more);
        mLlAddMoreError = (LinearLayout) view
                .findViewById(R.id.ll_item_listview_addmore_error);

        return view;
    }

    @Override
    public void refreshView(Integer data) {
        switch (data) {
            case STATE_MORE_MORE:
                mLlAddMoreMore.setVisibility(View.VISIBLE);
                mLlAddMoreError.setVisibility(View.INVISIBLE);
                break;
            case STATE_MORE_ERROR:
                mLlAddMoreMore.setVisibility(View.INVISIBLE);
                mLlAddMoreError.setVisibility(View.VISIBLE);
                break;
            case STATE_MORE_EMPTY:
                mLlAddMoreMore.setVisibility(View.GONE);
                mLlAddMoreError.setVisibility(View.GONE);
                break;

        }
    }

}
