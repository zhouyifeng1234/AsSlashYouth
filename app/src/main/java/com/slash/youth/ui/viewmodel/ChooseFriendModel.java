package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;
import android.net.sip.SipAudioCall;
import android.view.View;
import android.widget.AdapterView;

import com.slash.youth.databinding.ActivityChooseFriendBinding;
import com.slash.youth.domain.SlashFriendBean;
import com.slash.youth.ui.activity.ChooseFriendActivtiy;
import com.slash.youth.ui.adapter.ChooseFriendAdapter;
import com.slash.youth.ui.adapter.LocationCityFirstLetterAdapter;
import com.slash.youth.utils.DialogUtils;
import com.slash.youth.utils.LogKit;

import java.io.FileReader;
import java.util.ArrayList;

/**
 * Created by zss on 2016/11/2.
 */
public class ChooseFriendModel  extends BaseObservable{
    private ActivityChooseFriendBinding activityChooseFriendBinding;
    private  ArrayList<SlashFriendBean> friendArrayList = new ArrayList<>();
    private ArrayList<Character> letterList = new ArrayList<>();
    private LocationCityFirstLetterAdapter locationCityFirstLetterAdapter;
    private ChooseFriendAdapter chooseFriendAdapter;
    private ChooseFriendActivtiy chooseFriendActivtiy;

    public ChooseFriendModel(ActivityChooseFriendBinding activityChooseFriendBinding,ChooseFriendActivtiy chooseFriendActivtiy) {
        this.activityChooseFriendBinding = activityChooseFriendBinding;
        this.chooseFriendActivtiy = chooseFriendActivtiy;
        initData();
        initView();
        listener();
    }

    private void initData() {
        friendArrayList.add(new SlashFriendBean("朋友"));
        friendArrayList.add(new SlashFriendBean("朋友"));
        friendArrayList.add(new SlashFriendBean("朋友"));
        friendArrayList.add(new SlashFriendBean("朋友"));
        friendArrayList.add(new SlashFriendBean("朋友"));

        for (char cha = 'A'; cha <= 'Z'; cha++) {
            letterList.add(cha);
        }

    }

    private void initView() {
        chooseFriendAdapter = new ChooseFriendAdapter(friendArrayList);
        activityChooseFriendBinding.lvFriend.setAdapter(chooseFriendAdapter);

        locationCityFirstLetterAdapter = new LocationCityFirstLetterAdapter(letterList);
        activityChooseFriendBinding.lvLetter.setAdapter(locationCityFirstLetterAdapter);
    }

    private void listener() {
        activityChooseFriendBinding.lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showSendDialog(position);

            }
        });


    }

    private void showSendDialog(int position) {

        DialogUtils.showDialogFive(chooseFriendActivtiy, "发给王鹏", "", new DialogUtils.DialogCallBack() {
            @Override
            public void OkDown() {
                LogKit.d("OK");
            }

            @Override
            public void CancleDown() {

                LogKit.d("cannel");
            }
        });
    }


}
