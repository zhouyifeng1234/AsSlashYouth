package com.slash.youth.ui.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.domain.ContactsBean;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ContactsCareHolder;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/20.
 */
public class ContactsCareAdapter extends SlashBaseAdapter<ContactsBean.DataBean.ListBean> {

    private ContactsCareHolder contactsCareHolder;
    private int type;

    public ContactsCareAdapter(ArrayList<ContactsBean.DataBean.ListBean> listData, int type) {
        super(listData);
        this.type = type;
    }

    @Override
    public ArrayList<ContactsBean.DataBean.ListBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        contactsCareHolder = new ContactsCareHolder(type);
        return contactsCareHolder;
    }


   /* @Override
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
            CardView btn = ContactsCareHolder.btn;
            btn.setOnClickListener(new lvButtonListener(position));

            return rootView;
        } else {
            TextView textViewNull = new TextView(CommonUtils.getContext());
            textViewNull.setText("Null");
            return textViewNull;
        }
    }


    class lvButtonListener implements View.OnClickListener {
        private int position;
        lvButtonListener(int pos) {
            position = pos;
        }

        @Override
        public void onClick(View v) {
            listener.onItemRemove(position);
        }
    }

    public interface onItemRemoveListener{
        void onItemRemove( int index);
    }

    private onItemRemoveListener listener;
    public void setItemRemoveListener ( onItemRemoveListener listener ) {
        this.listener = listener;
    }
*/


}
