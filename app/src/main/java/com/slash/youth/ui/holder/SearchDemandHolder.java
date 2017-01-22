package com.slash.youth.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.TimeUtils;

/**
 * Created by zss 2016/12/7.
 */
public class SearchDemandHolder  extends SearchViewHolder<SearchAllBean.DataBean.DemandListBean>{
    private View mRootView;
    TextView tv_item_listview_home_demand_username;
    TextView tv_item_listview_home_demand_title;
    TextView tv_item_listview_home_demand_date;
    ImageView iv_item_listview_home_demand_avatar;
    TextView tv_quote;
    TextView tv_pay;
    TextView tv_line;
    ImageView iv_authentication;

    @Override
    public View initView() {
        mRootView  = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_needandservice, null);
        iv_item_listview_home_demand_avatar = (ImageView) mRootView.findViewById(R.id.iv_item_listview_home_demand_avatar);
        iv_authentication = (ImageView)mRootView.findViewById(R.id.iv_authentication);
        tv_item_listview_home_demand_username = (TextView) mRootView.findViewById(R.id.tv_item_listview_home_demand_username);
        tv_item_listview_home_demand_title = (TextView) mRootView.findViewById(R.id.tv_item_listview_home_demand_title);
        tv_item_listview_home_demand_date = (TextView) mRootView.findViewById(R.id.tv_item_listview_home_demand_date);
        tv_quote= (TextView) mRootView.findViewById(R.id.tv_quote);
        tv_line= (TextView) mRootView.findViewById(R.id.tv_line);
        tv_pay= (TextView) mRootView.findViewById(R.id.tv_pay);
        return mRootView;
    }

    @Override
    public void refreshView(SearchAllBean.DataBean.DemandListBean demandListBean, final int position) {
        int anonymity = demandListBean.getAnonymity();
        String name = demandListBean.getName();
        String avatar = demandListBean.getAvatar();
        //匿名，实名
        switch (anonymity){
            case 1://实名
                BitmapKit.bindImage(iv_item_listview_home_demand_avatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + avatar);
                tv_item_listview_home_demand_username.setText(demandListBean.getName());
                break;
            case 0://匿名
                iv_item_listview_home_demand_avatar.setImageResource(R.mipmap.anonymity_avater);
                String firstName = name.substring(0, 1);
                String anonymityName = firstName + "xx";
                tv_item_listview_home_demand_username.setText(anonymityName);
                break;
        }

        if(demandListBean.getIsauth() == 1){
            iv_authentication.setVisibility(View.VISIBLE);
        }else if(demandListBean.getIsauth() == 0){
            iv_authentication.setVisibility(View.GONE);
        }

        tv_item_listview_home_demand_title.setText(demandListBean.getTitle());
        long starttime = demandListBean.getStarttime();
        String startData = TimeUtils.getTime(starttime);
      //  tv_item_listview_home_demand_date.setText(FirstPagerManager.START_TIME+startData);
        int quote = demandListBean.getQuote();
        if(quote<=0){
            tv_quote.setText(FirstPagerManager.DEMAND_QUOTE);
        }else {
            tv_quote.setText(FirstPagerManager.QUOTE+demandListBean.getQuote());
        }

        if(demandListBean.getPattern() == 0){
            tv_line.setText(SearchManager.SEARCH_FIELD_PATTERN_LINE_UP);
        }else if(demandListBean.getPattern() == 1){
            tv_line.setText(SearchManager.SEARCH_FIELD_PATTERN_LINE_DOWN);
        }

        //1开启 0关闭
        tv_pay.setText(FirstPagerManager.DEMAND_INSTALMENT);
        if(demandListBean.getInstalment() == 1){
            tv_pay.setVisibility(View.GONE);
        }else if(demandListBean.getInstalment() == 0){
            tv_pay.setVisibility(View.VISIBLE);
        }
    }
}
