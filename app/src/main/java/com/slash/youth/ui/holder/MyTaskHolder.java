package com.slash.youth.ui.holder;

import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.slash.youth.R;
import com.slash.youth.databinding.ItemMyTaskBinding;
import com.slash.youth.domain.MyTaskBean;
import com.slash.youth.engine.MsgManager;
import com.slash.youth.global.GlobalConstants;
import com.slash.youth.ui.viewmodel.ItemMyTaskModel;
import com.slash.youth.utils.BitmapKit;
import com.slash.youth.utils.CommonUtils;
import com.slash.youth.utils.LogKit;

import java.text.SimpleDateFormat;

/**
 * Created by zhouyifeng on 2016/10/26.
 */
public class MyTaskHolder extends BaseHolder<MyTaskBean> {

    private ItemMyTaskModel mItemMyTaskModel;
    private ItemMyTaskBinding mItemMyTaskBinding;
    String[] optionalPriceUnit = new String[]{"次", "个", "幅", "份", "单", "小时", "分钟", "天", "其他"};

    @Override
    public View initView() {
        mItemMyTaskBinding = DataBindingUtil.inflate(LayoutInflater.from(CommonUtils.getContext()), R.layout.item_my_task, null, false);
        mItemMyTaskModel = new ItemMyTaskModel(mItemMyTaskBinding);
        mItemMyTaskBinding.setItemMyTaskModel(mItemMyTaskModel);
        return mItemMyTaskBinding.getRoot();
    }

    @Override
    public void refreshView(MyTaskBean data) {
        mItemMyTaskModel.tid = data.tid;
        mItemMyTaskModel.uid = data.uid;

        //加载头像
//        data.avatar = "group1/M00/00/00/eBtfY1gM2JmAa1SOAAJJOkiaAls.ac3597";//为了测试，设置的模拟数据
        if (TextUtils.isEmpty(data.avatar)) {
            //加载默认头像
            loadDefaultAvatar();
        } else {
//            DemandEngine.downloadFile(new BaseProtocol.IResultExecutor<byte[]>() {
//                @Override
//                public void execute(byte[] dataBean) {
//                    try {
//                        //字节转换成头像成功，显示下载的头像
//                        Bitmap bitmapAvatar = BitmapFactory.decodeByteArray(dataBean, 0, dataBean.length);
//                        mItemMyTaskBinding.ivMyTaskAvatar.setImageBitmap(bitmapAvatar);
//                    } catch (Exception ex) {
//                        //转换失败，显示默认头像
//                        loadDefaultAvatar();
//                    }
//                }
//
//                @Override
//                public void executeResultError(String result) {
//
//                }
//            }, data.avatar);

            BitmapKit.bindImage(mItemMyTaskBinding.ivMyTaskAvatar, GlobalConstants.HttpUrl.IMG_DOWNLOAD + "?fileId=" + data.avatar);
        }

        mItemMyTaskModel.setTaskTitle(data.title);

        String startTimeStr = convertStartTimeFormat(data.starttime, data.endtime, data.type, data.timetype);
        mItemMyTaskModel.setStartTime(startTimeStr);

        if (data.isauth == 1) {
            mItemMyTaskModel.setAddVvisibility(View.VISIBLE);
        } else {
            mItemMyTaskModel.setAddVvisibility(View.INVISIBLE);
        }

        mItemMyTaskModel.setUsername(data.name);


        //首先需要判断是否开启了分期，如果没有开启，下面两个字段不需要显示，暂时服务端没有返回判断是否开启分期的字段
//        if (data.instalment == 0) {
//            mItemMyTaskModel.setInstalmentText("分期到账");
//        } else {
//            mItemMyTaskModel.setInstalmentText("分期");
//        }

        if (TextUtils.isEmpty(data.instalmentratio)) {
            if (data.instalment == 1) {//分期，发布任务时开启的分期，这时候还没有人抢单，所以没有分期比例信息的
                mItemMyTaskModel.setInstalmentTextVisibility(View.VISIBLE);
                mItemMyTaskModel.setInstalmentratioStrVisibility(View.GONE);
                mItemMyTaskModel.setInstalmentText("分期到账");
            } else {// 0 不分期
                // 未开启分期
//            mItemMyTaskModel.setInstalmentText("");
//            mItemMyTaskModel.setInstalmentratioStr("");
//            mItemMyTaskModel.setInstalmentTextVisibility(View.GONE);
                mItemMyTaskModel.setInstalmentTextVisibility(View.VISIBLE);
                mItemMyTaskModel.setInstalmentText("一次性到账");
                mItemMyTaskModel.setInstalmentratioStrVisibility(View.GONE);
            }
        } else {
            //开启分期
            //显示每一期的分期比例
            String[] instalmentratioArray = data.instalmentratio.split(",");
            if (instalmentratioArray.length <= 1) {
                //只分了一期，相当于未开启分期
//                mItemMyTaskModel.setInstalmentText("");
//                mItemMyTaskModel.setInstalmentratioStr("");
                mItemMyTaskModel.setInstalmentTextVisibility(View.VISIBLE);
                mItemMyTaskModel.setInstalmentText("一次性到账");
                mItemMyTaskModel.setInstalmentratioStrVisibility(View.GONE);
            } else {
                mItemMyTaskModel.setInstalmentTextVisibility(View.VISIBLE);
                mItemMyTaskModel.setInstalmentratioStrVisibility(View.VISIBLE);

                mItemMyTaskModel.setInstalmentText("分期到账");
                String instalmentratioStr = "";
                for (int i = 0; i < instalmentratioArray.length; i++) {
                    String ratio = instalmentratioArray[i];
                    if (TextUtils.isEmpty(ratio)) {
                        continue;
                    }
                    LogKit.v("instalmentratioStr：" + ratio);
                    //ratio = (ratio.split("\\."))[1];
                    ratio = (int) (Double.parseDouble(ratio) * 100) + "";
                    if (i < instalmentratioArray.length - 1) {
                        instalmentratioStr += ratio + "%/";
                    } else {
                        instalmentratioStr += ratio + "%";
                    }
                }
                mItemMyTaskModel.setInstalmentratioStr(instalmentratioStr);
            }
        }

        //显示报价前应该先判断是否需要显示报价
        if (data.type == 1) {//需求
            if (data.quote <= 0) {
                mItemMyTaskModel.setQuote("服务方报价");
            } else {
                mItemMyTaskModel.setQuote((int) (data.quote) + "元");
            }
        } else if (data.type == 2) {//服务
            if (data.quoteunit == 9) {
                mItemMyTaskModel.setQuote((int) (data.quote) + "元");
            } else if (data.quoteunit < 9 && data.quoteunit > 0) {
                mItemMyTaskModel.setQuote((int) (data.quote) + "元/" + optionalPriceUnit[data.quoteunit - 1]);
            }
        }
        //抢单数量，服务端返回的是所有的抢单数量，这里需要新增的抢单数量
        mItemMyTaskModel.setBidnum(data.bidnum + "");

        mItemMyTaskModel.displayCurrentBigStatusAndButtons(data.status, data.type, data.roleid);

        if (data.type == 1) {//需求
            //隐藏需求方信息
            mItemMyTaskModel.setTaskDemandSideNameVisibility(View.INVISIBLE);

            //type 1 表示需求
//            if (data.roleid == 1) {
//                //需求发布者
//                mItemMyTaskModel.setPublishDemandStatusPointVisibility(View.VISIBLE);
//                mItemMyTaskModel.setBidDemandStatusPointVisibility(View.GONE);
//            } else if (data.roleid == 2) {
//                //抢需求
//                mItemMyTaskModel.setPublishDemandStatusPointVisibility(View.GONE);
//                mItemMyTaskModel.setBidDemandStatusPointVisibility(View.VISIBLE);
//            }

        } else if (data.type == 2) {//服务
            //显示需求方信息
            mItemMyTaskModel.setTaskDemandSideNameVisibility(View.VISIBLE);
            mItemMyTaskModel.setServiceDName("需求方:" + data.dname);

            //type 2 表示服务
//            if (data.roleid == 1) {
//                //服务发布者
//
//            } else if (data.roleid == 2) {
//                //抢服务
//
//            }
        }

        mItemMyTaskModel.setPublishDemandStatusPointVisibility(View.GONE);
        //显示小圆点
        if (MsgManager.everyTaskMessageCount != null) {//正常情况，这里应该不可能为null
            Integer integer = MsgManager.everyTaskMessageCount.get(data.id);
            int count;
            if (integer == null) {
                count = 0;
            } else {
                count = integer;
            }
            if (count > 0) {
                mItemMyTaskModel.setBidDemandStatusPointVisibility(View.VISIBLE);
            } else {
                mItemMyTaskModel.setBidDemandStatusPointVisibility(View.GONE);
            }
        }
    }

    public String convertStartTimeFormat(long startTimeMill, long endTimeMill, int type, int timetype) {
        if (type == 1) {//需求
            if (startTimeMill <= 0) {
                return "开始时间:随时";
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                return "开始时间:" + sdf.format(startTimeMill);
            }
        } else if (type == 2) {//服务
//            if (timetype == 0) {
            if (startTimeMill != 0 || endTimeMill != 0) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
                String starttimeStr = sdf.format(startTimeMill);
                String endtimeStr = sdf.format(endTimeMill);
//                return "闲置时间:" + starttimeStr + "-" + endtimeStr;
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
//                return "闲置时间:" + idleTimeName;
                return idleTimeName;
            }
        }
        return "";
    }

    public void loadDefaultAvatar() {

    }
}
