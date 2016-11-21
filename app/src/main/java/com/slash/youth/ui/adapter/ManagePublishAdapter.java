package com.slash.youth.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.slash.youth.domain.MyCollectionBean;
import com.slash.youth.domain.SkillManageBean;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.holder.AddMoreHolder;
import com.slash.youth.ui.holder.BaseHolder;
import com.slash.youth.ui.holder.ManagePublishHolder;
import com.slash.youth.ui.holder.MySkillManageHolder;
import com.slash.youth.utils.CommonUtils;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class ManagePublishAdapter extends SlashBaseAdapter<MyCollectionBean> {

    private MySkillManageActivity mySkillManageActivity;
    private ArrayList<MyCollectionBean> managePublishList;
    private ManagePublishHolder managePublishHolder;

    public ManagePublishAdapter(ArrayList<MyCollectionBean> listData, MySkillManageActivity mySkillManageActivity,ArrayList<MyCollectionBean> managePublishList) {
        super(listData);
        this.mySkillManageActivity = mySkillManageActivity;
        this.managePublishList = managePublishList;
    }

    @Override
    public ArrayList<MyCollectionBean> onLoadMore() {
        return null;
    }

    @Override
    public BaseHolder getHolder(int position) {
        managePublishHolder = new ManagePublishHolder(position, mySkillManageActivity, managePublishList);
        return managePublishHolder;
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
            ImageView ivDeleteSkill = managePublishHolder.itemManagePublishHolderBinding.ivDeleteSkill;
            ivDeleteSkill.setOnClickListener(new lvButtonListener(position));

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

}
