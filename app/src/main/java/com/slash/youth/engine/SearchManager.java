package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.SearchAllProtocol;
import com.slash.youth.http.protocol.SearchAssociativeProtocol;
import com.slash.youth.http.protocol.SearchDemandListProtocol;
import com.slash.youth.http.protocol.SearchServiceListProtocol;
import com.slash.youth.http.protocol.SearchUserListProtocol;

/**
 * Created by zss on 2016/12/5.
 */
public class SearchManager {
    public static final String HOT_SEARCH_DEMEND = "热搜需求";
    public static final String HOT_SEARCH_SERVICE = "热搜服务";
    public static final String HOT_SEARCH_PERSON = "搜人";
    public static final String SEARCH_FIELD_PATTERN_LINE_UP = "线上";
    public static final String SEARCH_FIELD_PATTERN_LINE_DOWN = "线下";
    public static final String SEARCH_ITEM_TITLE_DEMEND = "需求";
    public static final String SEARCH_ITEM_BOTTOM_DEMEND = "查看更多需求";
    public static final String SEARCH_ITEM_TITLE_SERVICE = "服务";
    public static final String SEARCH_ITEM_BOTTOM_SERVICE = "查看更多服务";
    public static final String SEARCH_ITEM_TITLE_PERSON = "找人";
    public static final String SEARCH_ITEM_BOTTOM_PERSON = "查看更多人脉";
    public static final String NOTHING = "暂无结果";

    //[搜索]-联想词搜索
    public static void getSearchAssociativeTag(BaseProtocol.IResultExecutor getSearchAssociativeTag, String searchTag, int offset, int limit) {
        SearchAssociativeProtocol searchAssociativeProtocol = new SearchAssociativeProtocol(searchTag, offset, limit);
        searchAssociativeProtocol.getDataFromServer(getSearchAssociativeTag);
    }

    //[搜索]-[用户&需求&服务]搜索
    public static void getSearchAll(BaseProtocol.IResultExecutor onGetSearchAll, String searchTag) {
        SearchAllProtocol searchAllProtocol = new SearchAllProtocol(searchTag);
        searchAllProtocol.getDataFromServer(onGetSearchAll);
    }

    //[搜索]-需求搜索
    public static void getSearchDemandList(BaseProtocol.IResultExecutor onGetSearchAll,String tag, int pattern, int isauth, String city, int sort, double lat, double lng, int offset, int limit) {
        SearchDemandListProtocol searchDemandListProtocol = new SearchDemandListProtocol(tag,  pattern,  isauth,  city, sort,  lat,  lng,  offset,  limit);
        searchDemandListProtocol.getDataFromServer(onGetSearchAll);
    }

    //[搜索]-服务搜索
    public static void getSearchServiceList(BaseProtocol.IResultExecutor onGetSearchAll,String tag, int pattern, int isauth, String city, int sort, double lat, double lng, int offset, int limit) {
        SearchServiceListProtocol searchServiceItemProtocol = new SearchServiceListProtocol(tag,  pattern,  isauth,  city, sort,  lat,  lng,  offset,  limit);
        searchServiceItemProtocol.getDataFromServer(onGetSearchAll);
    }

    //[搜索]-用户搜索
    public static void getSearchUserList(BaseProtocol.IResultExecutor onGetSearchAll,String tag,  int isauth,  int sort,  int offset, int limit) {
        SearchUserListProtocol searchUserListProtocol = new SearchUserListProtocol(tag,isauth,sort, offset,limit);
        searchUserListProtocol.getDataFromServer(onGetSearchAll);
    }


}