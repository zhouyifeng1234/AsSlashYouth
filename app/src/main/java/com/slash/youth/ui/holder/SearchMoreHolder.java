package com.slash.youth.ui.holder;

import android.net.sip.SipSession;
import android.view.View;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.adapter.SearchAllAdapter;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

/**
 * Created by zss on 2016/12/7.
 */
public class SearchMoreHolder extends SearchViewHolder<String> {

    private int type;
    private View mRootView;
    TextView tv_search_more;

    public SearchMoreHolder(int type) {
        this.type = type;
    }

    @Override
    public View initView() {
        mRootView = View.inflate(CommonUtils.getContext(), R.layout.search_result_more, null);
        tv_search_more = (TextView) mRootView.findViewById(R.id.tv_search_more);
        return mRootView;
    }

    @Override
    public void refreshView(String data, final int position) {
        switch (type){
            case SearchAllAdapter.searchMore:
                tv_search_more.setText(SearchManager.SEARCH_ITEM_BOTTOM_DEMEND);
                break;
            case SearchAllAdapter.serviceMore:
                tv_search_more.setText(SearchManager.SEARCH_ITEM_BOTTOM_SERVICE);
                break;
            case SearchAllAdapter.personMore:
                tv_search_more.setText(SearchManager.SEARCH_ITEM_BOTTOM_PERSON);
                break;
        }

        tv_search_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnMoreClick(position);
            }
        });
    }

    //监听回调返回键
    public interface OnMoreClickListener{
        void OnMoreClick(int position);
    }

    private OnMoreClickListener listener;
    public void setOnMoreClickListener(OnMoreClickListener listener) {
        this.listener = listener;
    }
}
