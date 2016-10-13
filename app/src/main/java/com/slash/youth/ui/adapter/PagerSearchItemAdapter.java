package com.slash.youth.ui.adapter;

import android.icu.util.TimeZone;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.R;
import com.slash.youth.domain.SearchAllBean;
import com.slash.youth.domain.SearchNeedItem;
import com.slash.youth.domain.SearchPersonItem;
import com.slash.youth.domain.SearchServiceItem;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import org.xutils.x;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Objects;

/**
 * Created by zss on 2016/9/6.
 */
public  class PagerSearchItemAdapter<T> extends BaseAdapter {
    private ArrayList<SearchAllBean.DataBean.DemandListBean> mNeedData;
    private ArrayList<Objects> mServiceData;
    private ArrayList<SearchAllBean.DataBean.UserListBean> mPersonData;
    private static final int searchTitle = 0;
    private static final int serviceTitle = 1;
    private static final int personTitle = 2;
    private static final int searchResult = 3;
    private static final int serviceResult = 4;
    private static final int personResult = 5;
    private static final int searchMore = 6;
    private static final int personMore = 7;
    private static final int serviceMore = 8;

    public PagerSearchItemAdapter(ArrayList<SearchAllBean.DataBean.DemandListBean> demandList,
                                  ArrayList<Objects> serviceList,
                                  ArrayList<SearchAllBean.DataBean.UserListBean> userList ,
                                  OnClickMoreListener listener) {
        this.mNeedData = demandList;
        this.mServiceData = serviceList;
        this.mPersonData = userList;
       this. listener = listener;
    }

    private OnClickMoreListener listener;

    public interface OnClickMoreListener{
        int needMoreBtn=1;
        int serviceMoreBtn=2;
        int persionMoreBtn=3;
        void onClickMore(int btn);
    }

    @Override
    public int getCount() {
        return mNeedData.size()+mServiceData.size()+mPersonData.size()+6;
    }

    @Override
    public Object getItem(int position) {
        if(position ==0){
            return null;//title
        }else if(position>0||position<(mNeedData.size()+1)){
          return  mNeedData.get(position-1);
        }else if(position==(mNeedData.size()+1)){
            return null;//addMore
        }else if(position==(mNeedData.size()+2)){
            return null;//title
        }else if(position>(mNeedData.size()+2)||position<((mNeedData.size()+2)+mServiceData.size())){
            return mServiceData.get(position-(mNeedData.size()+2));
        }else if((mNeedData.size()+mServiceData.size()+3)==position){
            return null;//addMore
        }else if((mNeedData.size()+mServiceData.size()+4)==position){
            return null;//title
        }else if(position>(mNeedData.size()+mServiceData.size()+4)||position<((mNeedData.size()+mServiceData.size()+4)+mPersonData.size())){
            return mPersonData.get(position-(mNeedData.size()+mServiceData.size()+4));
        }else if(position == (mNeedData.size()+mServiceData.size()+mPersonData.size()+5)){
            return null;//addMore
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder holder = null;
        //设置holder
        final int type = getItemViewType(position);
        if (convertView == null) {
            holder = new viewHolder();
            switch (type) {
                case searchTitle://搜索标题
                case serviceTitle:
                case personTitle:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.search_result_title, null);
                   holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                    break;
                case searchResult://搜索结果
                case serviceResult:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search, null);
                    holder.iv_item_listview_home_demand_avatar = (ImageView) convertView.findViewById(R.id.iv_item_listview_home_demand_avatar);
                    holder.iv_authentication = (ImageView)convertView.findViewById(R.id.iv_authentication);
                    holder.tv_item_listview_home_demand_username = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_username);
                    holder.tv_item_listview_home_demand_title = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_title);
                    holder.tv_item_listview_home_demand_date = (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_date);
                    holder.tv_money= (TextView) convertView.findViewById(R.id.tv_money);
                    holder.tv_line= (TextView) convertView.findViewById(R.id.tv_line);
                    holder.tv_pay= (TextView) convertView.findViewById(R.id.tv_pay);
                    holder.tv_cityName= (TextView) convertView.findViewById(R.id.tv_cityName);
                    holder.iv_sandglass = (ImageView)convertView.findViewById(R.id.iv_sandglass);
                    holder.tv_item_listview_home_demand_time= (TextView) convertView.findViewById(R.id.tv_item_listview_home_demand_time);
                    break;
                case personResult:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.item_listview_search_person, null);
                    holder.tv_search_person_name = (TextView) convertView.findViewById(R.id.tv_search_person_name);
                    holder.iv_search_person = (ImageView) convertView.findViewById(R.id.iv_search_person);
                    holder.tv_search_person_zhiye = (TextView) convertView.findViewById(R.id.tv_search_person_zhiye);
                    holder.iv_search_v = (ImageView) convertView.findViewById(R.id.iv_search_v);
                    holder.iv_jiahao = (ImageView) convertView.findViewById(R.id.iv_jiahao);
                    holder.iv_star = (ImageView) convertView.findViewById(R.id.iv_star);
                    holder.tv_zhiye1 = (TextView) convertView.findViewById(R.id.tv_zhiye1);
                    holder.tv_zhiye2 = (TextView) convertView.findViewById(R.id.tv_zhiye2);
                    holder.tv_zhiye3 = (TextView) convertView.findViewById(R.id.tv_zhiye3);
                    holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                    break;
                case searchMore://搜索更多
                case serviceMore:
                case personMore:
                    convertView = View.inflate(CommonUtils.getContext(), R.layout.search_result_more, null);
                    holder.tv_search_more = (TextView) convertView.findViewById(R.id.tv_search_more);
                    holder.tv_search_more.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener!=null){
                                if (type==searchMore){
                                    listener.onClickMore(OnClickMoreListener.needMoreBtn);
                                }else if (type==serviceMore){
                                    listener.onClickMore(OnClickMoreListener.serviceMoreBtn);
                                }else if (type==personMore){
                                    listener.onClickMore(OnClickMoreListener.persionMoreBtn);
                                }
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
            convertView.setTag(holder);
        }else {
            holder = (viewHolder) convertView.getTag();
        }
        //判断，不同的数据就有不同的展示
        switch (type){
            case searchTitle:
                holder.tv_title.setText("需求");
            break;
            case serviceTitle:
                holder.tv_title.setText("服务");
                break;
            case personTitle:
                holder.tv_title.setText("找人");
                break;
            case searchMore:
                holder.tv_search_more.setText("查看更多需求");
                break;
            case serviceMore:
                holder.tv_search_more.setText("查看更多服务");
                break;
            case personMore:
                holder.tv_search_more.setText("查看更多人脉");
                break;
            case searchResult:
                for (SearchAllBean.DataBean.DemandListBean demandListBean : mNeedData) {
                    x.image().bind(holder.iv_item_listview_home_demand_avatar,demandListBean.getAvatar());
                    holder.tv_item_listview_home_demand_username.setText(demandListBean.getName());
                    holder.tv_item_listview_home_demand_title.setText(demandListBean.getTitle());
                    setTime(holder.tv_item_listview_home_demand_date,demandListBean.getStarttime(),demandListBean.getEndtime());
                   // holder.tv_search_value.setText("报价：￥");
                   // holder.tv_search_line.setText("");

                }
                break;
            case serviceResult:
                LogKit.d("暂时没有数据");
                break;
            case personResult:

                break;

        }


      return convertView;
    }
    //设置时间
    private void setTime(TextView tv_item_listview_home_demand_date, long starttime, long endtime) {
        tv_item_listview_home_demand_date.setText("任务时间：");
        //TODO
    }


    //获取种类的数量
    @Override
    public int getViewTypeCount() {
        return 9;
    }

    //条目的种类
    @Override
    public int getItemViewType(int position) {
        final int needTitlePosition=0;
        final int needMorePosition = needTitlePosition+mNeedData.size()+1;
        final int serviceTitlePosition=needMorePosition+1;
        final int serviceMroePosition=serviceTitlePosition+mServiceData.size()+1;
        final int userTitlePosition = serviceMroePosition+1;
        final int userMorePosition = userTitlePosition+mPersonData.size()+1;
        if (position==needTitlePosition){
            return searchTitle;
        }else if (position>needTitlePosition&&position<needMorePosition){
            return searchResult;
        }else if (position==needMorePosition){
            return searchMore;
        }else

        if (position==serviceTitlePosition){
            return serviceTitle;
        }else if (position>serviceTitlePosition&&position<serviceMroePosition){
            return serviceResult;
        }else if (position==serviceMroePosition){
            return serviceMore;
        }else

        if (position==userTitlePosition){
            return personTitle;
        }else if (position>userTitlePosition&&position<userMorePosition){
            return personResult;
        }else if (position==userMorePosition){
            return personMore;
        }

        return -1;
    }

    static class viewHolder{
        TextView tv_search_person_name;
        TextView tv_search_more;
        TextView tv_title;
        TextView tv_zhiye1;
        TextView tv_zhiye2;
        TextView tv_zhiye3;
        TextView tv_item_listview_home_demand_username;
        TextView tv_item_listview_home_demand_title;
        TextView tv_item_listview_home_demand_date;
        TextView tv_search_value;
        TextView tv_money;
        TextView tv_pay;
        TextView tv_time;
        TextView tv_search_person_zhiye;
        ImageView iv_search_v;
        ImageView iv_jiahao;
        ImageView iv_item_listview_home_demand_avatar;
        ImageView iv_star;
        ImageView iv_search_person;
        ImageView iv_authentication;
        TextView tv_line;
        TextView tv_cityName;
        ImageView iv_sandglass;
        TextView tv_item_listview_home_demand_time;

    }

}
