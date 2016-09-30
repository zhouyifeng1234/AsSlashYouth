package com.slash.youth.ui.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import com.slash.youth.R;
import com.slash.youth.databinding.ActivitySearchBinding;
import com.slash.youth.databinding.SearchNeedResultTabBinding;
import com.slash.youth.ui.viewmodel.ActivitySearchModel;
import com.slash.youth.ui.viewmodel.SearchNeedResultTabModel;
import com.slash.youth.utils.CommonUtils;

/**
 * Created by zhouyifeng on 2016/9/18.
 */
public class SearchActivity extends Activity {

    private SearchNeedResultTabBinding searchNeedResultTabBinding;
    public String checkedFirstLabel = "未选择";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CommonUtils.setCurrentActivity(this);
        ActivitySearchBinding activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
        ActivitySearchModel activitySearchModel = new ActivitySearchModel(activitySearchBinding);
        activitySearchBinding.setActivitySearchModel(activitySearchModel);


        //创建数据绑定model
        searchNeedResultTabBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getCurrentActivity()), R.layout.search_need_result_tab, null, false);
        SearchNeedResultTabModel searchResultTabModel = new SearchNeedResultTabModel(searchNeedResultTabBinding);
        searchNeedResultTabBinding.setSearchNeedResultTabModel(searchResultTabModel);

    }
}
