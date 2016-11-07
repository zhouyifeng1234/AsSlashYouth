package com.slash.youth.ui.viewmodel;

import android.databinding.BaseObservable;

import com.slash.youth.databinding.ActivityReportTaBinding;

/**
 * Created by acer on 2016/11/2.
 */
public class ReportTAModel extends BaseObservable {

    private ActivityReportTaBinding activityReportTaBinding;

    public ReportTAModel(ActivityReportTaBinding activityReportTaBinding) {
        this.activityReportTaBinding = activityReportTaBinding;
    }
}
