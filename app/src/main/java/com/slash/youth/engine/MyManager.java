package com.slash.youth.engine;

import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.http.protocol.AddMyCollectionItemProtocol;
import com.slash.youth.http.protocol.AddSkillTempletProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckoutAuthProcessProtocol;
import com.slash.youth.http.protocol.CheckoutAuthProtocol;
import com.slash.youth.http.protocol.DeleteMyCollectionItemProtocol;
import com.slash.youth.http.protocol.DeleteMyPublishTaskItemProtocol;
import com.slash.youth.http.protocol.DeteleSkillManagerItemProtocol;
import com.slash.youth.http.protocol.EnchashmentApplicationProtocol;
import com.slash.youth.http.protocol.FileUploadProtocol;
import com.slash.youth.http.protocol.GetSkillTempletProtocol;
import com.slash.youth.http.protocol.GetUserInfoProtocol;
import com.slash.youth.http.protocol.ManagerMyPublishUpAndDownProtocol;
import com.slash.youth.http.protocol.ManagerMyPulishTaskProtocol;
import com.slash.youth.http.protocol.MyCollectionListProtocol;
import com.slash.youth.http.protocol.MyFirstPageProtocol;
import com.slash.youth.http.protocol.MyUserInfoProtocol;
import com.slash.youth.http.protocol.SetMsgProtocol;
import com.slash.youth.http.protocol.SkillManagerProtocol;
import com.slash.youth.http.protocol.TestFindPasswordStatusProtocol;
import com.slash.youth.http.protocol.UpdateSkillTempletProtocol;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/17.
 */
public class MyManager {
    public static final String  UP ="上架";
    public static final String  DOWN ="下架";
    public static final String  PUBLISH ="发布";
    public static final String  SKILL_MANAGER ="技能管理";
    public static final String  MAANAGER_MY_PUBLISH_TASK ="发布管理";
    public static final String   INSTALMENT="分期到账";
    public static final String   QOUNT ="报价:¥";
    public static final String   TASK_TIME ="任务时间:";
    public static final String   START_TIME ="开始时间:";
    public static final String   MY_COLLECTION ="收藏";
    public static final String   BACK_SUCCESS ="返回成功";
    public static final String   RES_FAIL="返回失败";
    public static final String   RES_TAG_EXIST="标签已存在";
    public static final String   RES_INVALID_TOKEN="用户uid不存在";
    public static final String   RES_INVALID_PARAMS="参数错误";
    public static String[] unitArr = {"次","个","幅","份","单","小时","分钟","天","其他"};

    //我的模块,首页获取我的个人信息
    public static void getMyUserinfo(BaseProtocol.IResultExecutor onGetMyUserinfo) {
        MyFirstPageProtocol myFirstPageProtocol = new MyFirstPageProtocol();
        myFirstPageProtocol.getDataFromServer(onGetMyUserinfo);
    }


    //我的模块,获取个人信息（自己看自己）
    public static void getMySelfPersonInfo(BaseProtocol.IResultExecutor onGetMySelfPersonInfo) {
        GetUserInfoProtocol getUserInfoProtocol = new GetUserInfoProtocol();
        getUserInfoProtocol.getDataFromServer(onGetMySelfPersonInfo);
    }

    //我的模块,获取用户个人信息（自己看其他人）
    public static void getOtherPersonInfo(BaseProtocol.IResultExecutor onGetOtherPersonInfo,long Uid,int anonymity) {
        MyUserInfoProtocol myUserInfoProtocol = new MyUserInfoProtocol(Uid,anonymity);
        myUserInfoProtocol.getDataFromServer(onGetOtherPersonInfo);
    }

    //我的模块，用户认证
    public static void  checkoutAuth(BaseProtocol.IResultExecutor onCheckoutAuth,int type,int cardtype,String url) {
        CheckoutAuthProtocol checkoutAuthProtocol = new CheckoutAuthProtocol(type,cardtype,url);
        checkoutAuthProtocol.getDataFromServer(onCheckoutAuth);
    }

    //认证流程
    public static void  checkoutAuthProcess(BaseProtocol.IResultExecutor onCheckoutAuthProcess) {
        CheckoutAuthProcessProtocol checkoutAuthProcessProtocol = new CheckoutAuthProcessProtocol();
        checkoutAuthProcessProtocol.getDataFromServer(onCheckoutAuthProcess);
    }

    //我的收藏
    public static void  getMyCollectionList(BaseProtocol.IResultExecutor onGetMyCollectionList,int offset,int limit) {
        MyCollectionListProtocol myCollectionListProtocol = new MyCollectionListProtocol(offset,limit);
        myCollectionListProtocol.getDataFromServer(onGetMyCollectionList);
    }

    //添加我的收藏
    public static void  addMyCollectionList(BaseProtocol.IResultExecutor onAddMyCollectionList,int type,long tid) {
        AddMyCollectionItemProtocol addMyCollectionItemProtocol = new AddMyCollectionItemProtocol(type,tid);
        addMyCollectionItemProtocol.getDataFromServer(onAddMyCollectionList);
    }

    //删除我的收藏
    public static void  onDeleteMyCollectionList(BaseProtocol.IResultExecutor onAddMyCollectionList,int type,long tid) {
        DeleteMyCollectionItemProtocol deleteMyCollectionItemProtocol = new DeleteMyCollectionItemProtocol(type,tid);
        deleteMyCollectionItemProtocol.getDataFromServer(onAddMyCollectionList);
    }

    //管理我发布的
    public static void  onManagerMyPublishTaskList(BaseProtocol.IResultExecutor onManagerMyPublishTaskList,int offset,int limit) {
        ManagerMyPulishTaskProtocol managerMyPulishTaskProtocol = new ManagerMyPulishTaskProtocol(offset,limit);
        managerMyPulishTaskProtocol.getDataFromServer(onManagerMyPublishTaskList);
    }

    //管理我发布的-删除管理项
    public static void  onDeleteManagerMyPublishTaskItem(BaseProtocol.IResultExecutor onDeleteManagerMyPublishTaskItem,long tid) {
        DeleteMyPublishTaskItemProtocol deleteMyPublishTaskItemProtocol = new DeleteMyPublishTaskItemProtocol(tid);
        deleteMyPublishTaskItemProtocol.getDataFromServer(onDeleteManagerMyPublishTaskItem);
    }

    //管理我发布的-上架和下架
    public static void  onManagerMyPublishTaskItemUpAndDown(BaseProtocol.IResultExecutor onManagerMyPublishTaskItemUpAndDown,long id,int action) {
        ManagerMyPublishUpAndDownProtocol managerMyPublishUpAndDownProtocol = new ManagerMyPublishUpAndDownProtocol(id,action);
        managerMyPublishUpAndDownProtocol.getDataFromServer(onManagerMyPublishTaskItemUpAndDown);
    }

    //技能管理-查看我的技能模板列表
    public static void  onSkillManagerList(BaseProtocol.IResultExecutor onSkillManagerList,int offset,int limit) {
        SkillManagerProtocol skillManagerProtocol = new SkillManagerProtocol(offset,limit);
        skillManagerProtocol.getDataFromServer(onSkillManagerList);
    }

    //技能管理-删除一个技能模板
    public static void  onDeteleSkillManagerItem(BaseProtocol.IResultExecutor onDeteleSkillManagerItem,long id) {
        DeteleSkillManagerItemProtocol deteleSkillManagerItemProtocol = new DeteleSkillManagerItemProtocol(id);
        deteleSkillManagerItemProtocol.getDataFromServer(onDeteleSkillManagerItem);
    }

    //技能管理——添加技能模板
    public static void  onAddSkillTemplet(BaseProtocol.IResultExecutor onAddSkillTemplet, String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc,  ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        AddSkillTempletProtocol addSkillTempletProtocol = new AddSkillTempletProtocol(title, listTag, startime, endtime, anonymity, desc, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        addSkillTempletProtocol.getDataFromServer(onAddSkillTemplet);
    }

    //技能管理——添加技能模板
    public static void  onGetOneSkillTemplet(BaseProtocol.IResultExecutor onGetOneSkillTemplet,long id) {
        GetSkillTempletProtocol getSkillTempletProtocol = new GetSkillTempletProtocol(id);
        getSkillTempletProtocol.getDataFromServer(onGetOneSkillTemplet);
    }

    //技能管理——修改技能模板
    public static void  onUpdateSkillTemplet(BaseProtocol.IResultExecutor onUpdateSkillTemplet,long id ,String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc, ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        UpdateSkillTempletProtocol updateSkillTempletProtocol = new UpdateSkillTempletProtocol(id,title, listTag, startime, endtime, anonymity, desc, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        updateSkillTempletProtocol.getDataFromServer(onUpdateSkillTemplet);
    }

    public static final String WITHDRAWALS_SUCCESS="提现申请成功，请以最终银行交易为准";
    public static final String WITHDRAWALS_FAIL_BALANCE_LEASE="钱包可提现余额不足";
    public static final String WITHDRAWALS_FAIL_PASSWORD_ERROR="交易密码错误";
    public static final String WITHDRAWALS_SERVICE_ERROR="服务端未知错误";

    //[我的账户]-提现申请（由于要做到不是实时提现，所以提现改成提现申请）
    public static void  enchashmentApplication(BaseProtocol.IResultExecutor onEnchashmentApplication,double amount,String address,int type,String password) {
        EnchashmentApplicationProtocol enchashmentApplicationProtocol = new EnchashmentApplicationProtocol(amount,address,type,password);
        enchashmentApplicationProtocol.getDataFromServer(onEnchashmentApplication);
    }

    //一、[文件]-图片上传
    public static void uploadFile(BaseProtocol.IResultExecutor onUploadFileFinished, String filePath) {
        FileUploadProtocol fileUploadProtocol = new FileUploadProtocol(filePath);
        fileUploadProtocol.getDataFromServer(onUploadFileFinished);
    }

    //验证找回密码的审核状态
    public static void testFindPassWord(BaseProtocol.IResultExecutor onTestFindPassWord) {
        TestFindPasswordStatusProtocol testFindPasswordStatusProtocol = new TestFindPasswordStatusProtocol();
        testFindPasswordStatusProtocol.getDataFromServer(onTestFindPassWord);
    }

}
