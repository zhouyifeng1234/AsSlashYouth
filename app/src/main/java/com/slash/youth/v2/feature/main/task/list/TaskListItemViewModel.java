package com.slash.youth.v2.feature.main.task.list;

import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;

import com.core.op.lib.base.BViewModel;
import com.slash.youth.R;
import com.slash.youth.domain.CommentStatusBean;
import com.slash.youth.domain.bean.TaskList;
import com.slash.youth.engine.MyTaskEngine;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.LogKit;
import com.slash.youth.utils.ToastUtils;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.text.SimpleDateFormat;

import rx.Observable;

import static android.R.attr.data;

/**
 * Created by acer on 2017/3/9.
 */

public class TaskListItemViewModel extends BViewModel {

    public final ObservableField<Integer> vipVisible = new ObservableField<>(View.VISIBLE);

    public TaskList.TaskBean taskBean;

    public String uri;
    public String date;

    public String quote;

    public String instalmentration;

    public String instalment;
    public String dname;
    public String status;
    public Drawable statusBg;

    public ObservableField<Integer> instalmentVisible = new ObservableField<>(View.GONE);
    public ObservableField<Integer> instalmentrationVisible = new ObservableField<>(View.GONE);

    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

    public TaskListItemViewModel() {
    }

    public TaskListItemViewModel(RxAppCompatActivity activity, TaskList.TaskBean data) {
        super(activity);
        this.taskBean = data;
        date = convertStartTimeFormat(taskBean.starttime, taskBean.endtime, taskBean.type, taskBean.timetype);

        uri = GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + data.avatar;
        //显示报价前应该先判断是否需要显示报价
        if (taskBean.type == 1) {//需求
            if (taskBean.quote <= 0) {
                this.quote = "服务方报价";
            } else {
                this.quote = (int) (taskBean.quote) + "元";
            }
        } else if (taskBean.type == 2) {//服务
            if (taskBean.quoteunit == 9) {
                this.quote = (int) (taskBean.quote) + "元";
            } else if (taskBean.quoteunit < 9 && taskBean.quoteunit > 0) {
                this.quote = (int) (taskBean.quote) + "元/" + optionalPriceUnit[taskBean.quoteunit - 1];
            }
        }

        if (TextUtils.isEmpty(data.instalmentratio)) {
            if (data.instalment == 1) {//分期，发布任务时开启的分期，这时候还没有人抢单，所以没有分期比例信息的
                instalmentVisible.set(View.VISIBLE);
                instalmentrationVisible.set(View.GONE);
                instalment = "分期到账";
            } else {// 0 不分期
                // 未开启分期
                instalmentVisible.set(View.VISIBLE);
                instalment = "一次性到账";
                instalmentrationVisible.set(View.GONE);
            }
        } else {
            //开启分期
            //显示每一期的分期比例
            String[] instalmentratioArray = data.instalmentratio.split(",");
            if (instalmentratioArray.length <= 1) {
                instalmentVisible.set(View.VISIBLE);
                instalment = "一次性到账";
                instalmentrationVisible.set(View.GONE);
            } else {
                instalmentVisible.set(View.VISIBLE);
                instalmentrationVisible.set(View.VISIBLE);

                instalment = "分期到账";
                for (int i = 0; i < instalmentratioArray.length; i++) {
                    String ratio = instalmentratioArray[i];
                    if (TextUtils.isEmpty(ratio)) {
                        continue;
                    }
                    ratio = (int) (Double.parseDouble(ratio) * 100) + "";
                    if (i < instalmentratioArray.length - 1) {
                        instalmentration += ratio + "%/";
                    } else {
                        instalmentration += ratio + "%";
                    }
                }
            }
        }
        if (data.type == 2) {//服务
            this.dname = "需求方:" + data.dname;
        }

        if (data.type == 1) {//需求

            switch (data.status) {
                case 1:
                case 4:
                case 5:
                    status = "待抢单";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 2:
                case 3:
                case 10:
                case 11:
                    status = "已过期";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_huise);
                    break;
                case 6:
                    status = "待支付";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 7:
                case 9:
                    status = "服务中";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 8:
                    status = "已完成";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
            }
        } else if (data.type == 2) {//服务
            switch (data.status) {
                case 1:/*初始化订单*/
                    //预约中 大状态
                    status = "已预约";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 2:/*服务者确认*/
                case 3:/*需求方支付中*/
                    //预支付 大状态
                    status = "待支付";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 5:/*订单进行中*/
                case 6:/*订单完成*/
                case 8:/*申请退款*/
                case 9:/*同意退款*/
                case 10:/*平台申诉处理*/
                    //服务中 大状态
                    status = "服务中";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 7:/*订单确认完成*/
                    //评价中 大状态
                    status = "待评价";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_bg);
                    break;
                case 4:/*订单已经取消*/
                case 11:/*服务方拒绝*/
                default:
                    //失效 过期 状态 四个圈全都是灰色
                    status = "已过期";
                    statusBg = activity.getResources().getDrawable(R.mipmap.state_huise);
                    break;
            }
        }
    }

    private String convertStartTimeFormat(long startTimeMill, long endTimeMill, int type, int timetype) {
        if (type == 1) {//需求
            if (startTimeMill <= 0) {
                return "开始时间:随时";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                return "开始时间:" + sdf.format(startTimeMill);
            }
        } else if (type == 2) {//服务
            if (startTimeMill != 0 || endTimeMill != 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                String starttimeStr = sdf.format(startTimeMill);
                String endtimeStr = sdf.format(endTimeMill);
                return starttimeStr + "-" + endtimeStr;
            } else {
                String idleTimeName = "";
                if (timetype == 1) {
                    idleTimeName = "下班后";
                } else if (timetype == 2) {
                    idleTimeName = "周末";
                } else if (timetype == 3) {
                    idleTimeName = "下班后及周末";
                } else if (timetype == 4) {
                    idleTimeName = "随时";
                }
                return idleTimeName;
            }
        }
        return "";
    }
}
