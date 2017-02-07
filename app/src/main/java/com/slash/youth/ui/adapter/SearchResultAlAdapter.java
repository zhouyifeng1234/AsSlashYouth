package com.slash.youth.ui.adapter;

import android.view.View;

import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.engine.SearchManager;
import com.slash.youth.gen.CityHistoryEntityDao;
import com.slash.youth.ui.activity.SearchActivity;
import com.slash.youth.ui.holder.SearchDemandHolder;
import com.slash.youth.ui.holder.SearchMoreHolder;
import com.slash.youth.ui.holder.SearchServiceHolder;
import com.slash.youth.ui.holder.SearchTitleHolder;
import com.slash.youth.ui.holder.SearchUserHolder;
import com.slash.youth.ui.holder.SearchViewHolder;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;

import java.util.ArrayList;

/**
 * Created by zss on 2016/12/7.
 */
public class SearchResultAlAdapter extends  SearchAllAdapter{
    private ArrayList<SearchAllBean.DataBean.UserListBean> userListBeen;
    private ArrayList<SearchAllBean.DataBean.ServiceListBean> serviceListBeen;
    private ArrayList<SearchAllBean.DataBean.DemandListBean> demandListBeen;
    private SearchMoreHolder searchMoreHolder;
    private SearchNeedResultTabModel searchNeedResultTabModel;
    private SearchActivity currentActivity = (SearchActivity) CommonUtils.getCurrentActivity();
    private String tag;
    private CityHistoryEntityDao cityHistoryEntityDao;

    public SearchResultAlAdapter(ArrayList<SearchAllBean.DataBean.DemandListBean> demandListBeen, ArrayList<SearchAllBean.DataBean.ServiceListBean> serviceListBeen, ArrayList<SearchAllBean.DataBean.UserListBean> userListBeen,String tag,CityHistoryEntityDao cityHistoryEntityDao) {
        super(demandListBeen, serviceListBeen, userListBeen);
        this.userListBeen = userListBeen;
        this.demandListBeen = demandListBeen;
        this.serviceListBeen =serviceListBeen;
        this.tag = tag;
        this.cityHistoryEntityDao = cityHistoryEntityDao;
    }

    @Override
    public SearchViewHolder getTitleHolder(int position, int type) {

        return new SearchTitleHolder(type);
    }

    @Override
    public SearchViewHolder getDemandHolder(int position, int type) {
        return new SearchDemandHolder();
    }

    @Override
    public SearchViewHolder getServiceHolder(int position, int type) {
        return new SearchServiceHolder();
    }

    @Override
    public SearchViewHolder getUserHolder(int position, int type) {
        return new SearchUserHolder(userListBeen);
    }

    @Override
    public SearchViewHolder getMoreHolder(int position, int type) {
        searchMoreHolder = new SearchMoreHolder(type,userListBeen,serviceListBeen,demandListBeen);
        clickMore();
        return searchMoreHolder;
    }

    private void clickMore() {
        searchMoreHolder.setOnMoreClickListener(new SearchMoreHolder.OnMoreClickListener() {
            @Override
            public void OnMoreClick(int position) {
                int itemViewType = getItemViewType(position);
              switch (itemViewType){
                    case SearchAllAdapter.searchMore:
                         SpUtils.setString("searchType", SearchManager.HOT_SEARCH_DEMEND);
                        break;
                    case SearchAllAdapter.serviceMore:
                        SpUtils.setString("searchType",SearchManager.HOT_SEARCH_SERVICE);
                        break;
                    case SearchAllAdapter.personMore:
                         SpUtils.setString("searchType",SearchManager.HOT_SEARCH_PERSON);
                        break;
                }
                  showMoreSearch();
            }
        });
    }
    private void showMoreSearch() {
        currentActivity.changeView(5);
        searchNeedResultTabModel = new SearchNeedResultTabModel(currentActivity.searchNeedResultTabBinding,tag,cityHistoryEntityDao);
        currentActivity.searchNeedResultTabBinding.setSearchNeedResultTabModel(searchNeedResultTabModel);
    }
}
