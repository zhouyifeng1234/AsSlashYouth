package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;

import java.util.ArrayList;

public abstract class SlashBaseAdapter<T> extends BaseAdapter {

    private ArrayList<T> mListData;
    public static final int HOLDER_TYPE_NORMAL = 0;
    public static final int HOLDER_TYPE_MORE = 1;
    private int loadMoreState;

    public SlashBaseAdapter(ArrayList<T> listData) {
        this.mListData = listData;
        loadMoreState = AddMoreHolder.STATE_MORE_EMPTY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mListData.size()) {
            return getInnerType(position);
        } else {
            return HOLDER_TYPE_MORE;
        }
    }

    public int getInnerType(int position) {
        return HOLDER_TYPE_NORMAL;
    }

    @Override
    public int getCount() {
        return mListData.size() + 1;
    }

    @Override
    public T getItem(int position) {
        if (getItemViewType(position) != HOLDER_TYPE_MORE) {
            return mListData.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder holder;
        if (convertView == null) {
            if (getItemViewType(position) == HOLDER_TYPE_MORE) {
                holder = getAddMoreHolder(position);
            } else {
                holder = getHolder(position);
            }
        } else {
            holder = (BaseHolder) convertView.getTag();
        }
        if (getItemViewType(position) != HOLDER_TYPE_MORE) {
            holder.setData(getItem(position), position);
        } else {
            // holder.setData(AddMoreHolder.STATE_MORE_EMPTY);
            AddMoreHolder addMoreHolder = (AddMoreHolder) holder;
            if (addMoreHolder.getData() == AddMoreHolder.STATE_MORE_MORE) {
                loadMore(addMoreHolder, position);
            }
        }

        View rootView = holder.getRootView();
        if (rootView != null) {
            return rootView;
        } else {
            TextView textViewNull = new TextView(CommonUtils.getContext());
            textViewNull.setText("Null");
            return textViewNull;
        }
    }

    boolean isLoadingMore = false;

    public void loadMore(final AddMoreHolder addMoreHolder, final int position) {
        if (!isLoadingMore) {
            isLoadingMore = true;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    final ArrayList<T> listMoreData = onLoadMore();
                    CommonUtils.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            if (listMoreData == null) {
                                addMoreHolder
                                        .setData(AddMoreHolder.STATE_MORE_ERROR, position);
                                setLoadMoreState(AddMoreHolder.STATE_MORE_ERROR);
                            } else {
                                if (listMoreData.size() < getPageSize()) {
                                    addMoreHolder
                                            .setData(AddMoreHolder.STATE_MORE_EMPTY, position);
                                    setLoadMoreState(AddMoreHolder.STATE_MORE_EMPTY);
                                    ToastUtils.shortToast("没有更多数据了�?��??");
                                } else {
                                    addMoreHolder
                                            .setData(AddMoreHolder.STATE_MORE_MORE, position);
                                    setLoadMoreState(AddMoreHolder.STATE_MORE_MORE);
                                }
                                mListData.addAll(listMoreData);
                                SlashBaseAdapter.this
                                        .notifyDataSetChanged();
                            }
                            isLoadingMore = false;
                        }
                    });
                }
            }).start();
        }
    }

    public abstract ArrayList<T> onLoadMore();

    public abstract BaseHolder getHolder(int position);

    public BaseHolder getAddMoreHolder(int position) {
        return new AddMoreHolder(getLoadMoreState(), position);
    }

    public int getLoadMoreState() {
        return loadMoreState;
    }

    public int getPageSize() {
        return 20;
    }

    public void setLoadMoreState(int loadMoreState) {
        this.loadMoreState = loadMoreState;
    }

    //TODO ZSS
    public void setData( ArrayList<T> ListData ) {
        this.mListData = ListData;
        notifyDataSetChanged();
    }

    public  ArrayList<T>  getData(){
        return this.mListData;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


}
