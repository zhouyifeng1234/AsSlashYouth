package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityContactsCareBinding;
import com.slash.youth.domain.ContactsBean;
import com.slash.youth.ui.adapter.ContactsCareAdapter;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/20.
 */
public class ActivityContactsCareModel extends BaseObservable {
    private ActivityContactsCareBinding activityContactsCareBinding;
    private String title;
    private ArrayList<ContactsBean> contactsLists = new ArrayList<>();
    private ContactsCareAdapter contactsCareAdapter;


    public ActivityContactsCareModel(ActivityContactsCareBinding activityContactsCareBinding,String title) {
        this.activityContactsCareBinding = activityContactsCareBinding;
        this.title = title;
        initData();
        initView();


    }



    private void initData() {
        contactsLists.clear();
        switch (title){
            case "关注我的":
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsLists.add(new ContactsBean());
                contactsCareAdapter = new ContactsCareAdapter(contactsLists);
                 activityContactsCareBinding.lv.setAdapter(contactsCareAdapter);
                 break;
            case "我关注":

                break;

            case "加我的":

                break;

            case "我加的":

                break;

        }

    }

    private void initView() {
        activityContactsCareBinding.tvContactsTitle.setText(title);




    }

}
