package com.slash.youth.ui.holder;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMySkillManageBinding;
import com.slash.youth.domain.SkillManagerBean;
import com.slash.youth.engine.FirstPagerManager;
import com.slash.youth.engine.MyManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.activity.MySkillManageActivity;
import com.slash.youth.ui.activity.PublishServiceBaseInfoActivity;
import com.slash.youth.ui.viewmodel.ItemMySkillManageModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.Constants;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.SpUtils;
import com.slash.youth.utils.TimeUtils;

import java.util.ArrayList;

/**
 * Created by acer on 2016/11/3.
 */
public class MySkillManageHolder extends BaseHolder<SkillManagerBean.DataBean.ListBean> {

    public ItemMySkillManageBinding itemMySkillManageBinding;
    private MySkillManageActivity mySkillManageActivity;
    private final String myActivityTitle;
    private ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList;
    private String[] split;

    public MySkillManageHolder( MySkillManageActivity mySkillManageActivity,ArrayList<SkillManagerBean.DataBean.ListBean> skillManageList) {
        this.mySkillManageActivity = mySkillManageActivity;
        this.skillManageList = skillManageList;
        myActivityTitle = SpUtils.getString("myActivityTitle", "");
    }

    @Override
    public View initView() {
        itemMySkillManageBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_skill_manage, null, false);
        ItemMySkillManageModel itemMySkillManageModel = new ItemMySkillManageModel(itemMySkillManageBinding,skillManageList,mySkillManageActivity);
        itemMySkillManageBinding.setItemMySkillManageModel(itemMySkillManageModel);
            return itemMySkillManageBinding.getRoot();
        }

        @Override
        public void refreshView(SkillManagerBean.DataBean.ListBean data) {
            setView(myActivityTitle);
            String pic = data.getPic();
            if(!TextUtils.isEmpty(pic)){
                if(pic.contains(",")){
                    split = pic.split(",");
                    BitmapKit.bindImage(itemMySkillManageBinding.ivPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + split[0]);
                }else {
                    BitmapKit.bindImage(itemMySkillManageBinding.ivPic, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + pic);
                }
            }

        String title = data.getTitle();
        itemMySkillManageBinding.tvSkillManagerTitle.setText(title);

        double quote = data.getQuote();
        int quoteunit = data.getQuoteunit();
        itemMySkillManageBinding.tvSkillManagerQuote.setText(MyManager.QOUNT+quote+"元"+"/"+FirstPagerManager.QUOTEUNITS[quoteunit-1]);

       /* int timetype = data.getTimetype();//闲时类型
            itemMySkillManageBinding.ivTime.setVisibility(View.VISIBLE);*/
       /* if(timetype == 0){
           // long starttime = data.getStarttime();
            long  timeMillis = System.currentTimeMillis();
            long endtime = data.getEndtime();
            String startData = TimeUtils.getData(starttime);
            String endData = TimeUtils.getData(endtime);
            itemMySkillManageBinding.tvSkillMamagerTime.setText(MyManager.TASK_TIME+startData+"-"+endData);
        } else {
            itemMySkillManageBinding.tvSkillMamagerTime.setText(FirstPagerManager.TIMETYPES[timetype-1]);
        }*/

        int anonymity = data.getAnonymity();//1实名0匿名
        String desc = data.getDesc();//长度小于4096字节 服务端默认取值为""

        int instalment = data.getInstalment();
        switch (instalment){
            case 1:
                itemMySkillManageBinding.tvFenqi.setVisibility(View.VISIBLE);
                break;
            case 0:
                itemMySkillManageBinding.tvFenqi.setVisibility(View.GONE);
                break;
        }

        String tag = data.getTag();
        int count = data.getCount();
        long id = data.getId();
        int bp = data.getBp();
        long cts = data.getCts();
        double lat = data.getLat();
        double lng = data.getLng();
        int loop = data.getLoop();
        int pattern = data.getPattern();
        String place = data.getPlace();
        String remark = data.getRemark();
        long uid = data.getUid();
        long uts = data.getUts();
    }

    //设置界面
    private void setView(String myActivityTitle) {
           switch (myActivityTitle){
            case MyManager.SKILL_MANAGER:
                itemMySkillManageBinding.tvMyBtn.setText(MyManager.PUBLISH);
              break;
        }
    }

    @Override
    public void setData(SkillManagerBean.DataBean.ListBean data, final int position) {
        super.setData(data, position);
        //点击删除
        itemMySkillManageBinding.ivDeleteSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDeleteClick(position);
            }
        });

        //发布
        itemMySkillManageBinding.tvMyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            SkillManagerBean.DataBean.ListBean listBean = skillManageList.get(position);
            Intent intentPublishServiceBaseInfoActivity = new Intent(CommonUtils.getContext(), PublishServiceBaseInfoActivity.class);
            intentPublishServiceBaseInfoActivity.putExtra("skillManagerItemBean",listBean);
            mySkillManageActivity.startActivityForResult(intentPublishServiceBaseInfoActivity, Constants.SKILL_MANAGER_PUBLISH_SERVICE);
            }
        });
    }

    public interface OnDeleteClickListener{
        void OnDeleteClick(int position);
    }

    private OnDeleteClickListener listener;
    public void setOnDeleteCklickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

}
