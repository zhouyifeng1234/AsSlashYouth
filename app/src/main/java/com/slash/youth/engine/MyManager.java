package com.slash.youth.engine;

import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.http.protocol.AddMyCollectionItemProtocol;
import com.slash.youth.http.protocol.AddSkillTempletProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckoutAuthProtocol;
import com.slash.youth.http.protocol.DeleteMyCollectionItemProtocol;
import com.slash.youth.http.protocol.DeleteMyPublishTaskItemProtocol;
import com.slash.youth.http.protocol.DeteleSkillManagerItemProtocol;
import com.slash.youth.http.protocol.GetSkillTempletProtocol;
import com.slash.youth.http.protocol.GetUserInfoProtocol;
import com.slash.youth.http.protocol.ManagerMyPublishUpAndDownProtocol;
import com.slash.youth.http.protocol.ManagerMyPulishTaskProtocol;
import com.slash.youth.http.protocol.MyCollectionListProtocol;
import com.slash.youth.http.protocol.MyFirstPageProtocol;
import com.slash.youth.http.protocol.MyUserInfoProtocol;
import com.slash.youth.http.protocol.SetMsgProtocol;
import com.slash.youth.http.protocol.SkillManagerProtocol;
import com.slash.youth.utils.LogKit;

import java.util.ArrayList;

/**
 * Created by zss on 2016/11/17.
 */
public class MyManager {

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
    public static void getOtherPersonInfo(BaseProtocol.IResultExecutor onGetOtherPersonInfo,long Uid) {
        MyUserInfoProtocol myUserInfoProtocol = new MyUserInfoProtocol(Uid);
        myUserInfoProtocol.getDataFromServer(onGetOtherPersonInfo);
    }

    //我的模块，用户认证
    public static void  checkoutAuth(BaseProtocol.IResultExecutor onCheckoutAuth,int type,int cardtype,String url) {
        CheckoutAuthProtocol checkoutAuthProtocol = new CheckoutAuthProtocol(type,cardtype,url);
        checkoutAuthProtocol.getDataFromServer(onCheckoutAuth);
    }

    //我的收藏
    public static void  getMyCollectionList(BaseProtocol.IResultExecutor onGetMyCollectionList,int offset,int limit) {
        MyCollectionListProtocol myCollectionListProtocol = new MyCollectionListProtocol(offset,limit);
        myCollectionListProtocol.getDataFromServer(onGetMyCollectionList);
    }

    //添加我的收藏
    public static void  addMyCollectionList(BaseProtocol.IResultExecutor onAddMyCollectionList,int type,int tid) {
        AddMyCollectionItemProtocol addMyCollectionItemProtocol = new AddMyCollectionItemProtocol(type,tid);
        addMyCollectionItemProtocol.getDataFromServer(onAddMyCollectionList);
    }

    //删除我的收藏
    public static void  onDeleteMyCollectionList(BaseProtocol.IResultExecutor onAddMyCollectionList,int type,int tid) {
        DeleteMyCollectionItemProtocol deleteMyCollectionItemProtocol = new DeleteMyCollectionItemProtocol(type,tid);
        deleteMyCollectionItemProtocol.getDataFromServer(onAddMyCollectionList);
    }

    //管理我发布的
    public static void  onManagerMyPublishTaskList(BaseProtocol.IResultExecutor onManagerMyPublishTaskList,int offset,int limit) {
        ManagerMyPulishTaskProtocol managerMyPulishTaskProtocol = new ManagerMyPulishTaskProtocol(offset,limit);
        managerMyPulishTaskProtocol.getDataFromServer(onManagerMyPublishTaskList);
    }

    //管理我发布的-删除管理项
    public static void  onDeleteManagerMyPublishTaskItem(BaseProtocol.IResultExecutor onDeleteManagerMyPublishTaskItem,int type,int tid) {
        DeleteMyPublishTaskItemProtocol deleteMyPublishTaskItemProtocol = new DeleteMyPublishTaskItemProtocol(type,tid);
        deleteMyPublishTaskItemProtocol.getDataFromServer(onDeleteManagerMyPublishTaskItem);
    }

    //管理我发布的-上架和下架
    public static void  onManagerMyPublishTaskItemUpAndDown(BaseProtocol.IResultExecutor onManagerMyPublishTaskItemUpAndDown,int id,int action) {
        ManagerMyPublishUpAndDownProtocol managerMyPublishUpAndDownProtocol = new ManagerMyPublishUpAndDownProtocol(id,action);
        managerMyPublishUpAndDownProtocol.getDataFromServer(onManagerMyPublishTaskItemUpAndDown);
    }

    //技能管理-查看我的技能模板列表
    public static void  onSkillManagerList(BaseProtocol.IResultExecutor onSkillManagerList,int offset,int limit) {
        SkillManagerProtocol skillManagerProtocol = new SkillManagerProtocol(offset,limit);
        skillManagerProtocol.getDataFromServer(onSkillManagerList);
    }

    //技能管理-删除一个技能模板
    public static void  onDeteleSkillManagerItem(BaseProtocol.IResultExecutor onDeteleSkillManagerItem,int id) {
        DeteleSkillManagerItemProtocol deteleSkillManagerItemProtocol = new DeteleSkillManagerItemProtocol(id);
        deteleSkillManagerItemProtocol.getDataFromServer(onDeteleSkillManagerItem);
    }

    //技能管理——添加技能模板
    public static void  onAddSkillTemplet(BaseProtocol.IResultExecutor onAddSkillTemplet, String title, ArrayList<String> listTag, long startime, long endtime, int anonymity, String desc, int timetype, ArrayList<String> listPic, int instalment, int bp, int pattern, String place, double lng, double lat, double quote, int quoteunit) {
        AddSkillTempletProtocol addSkillTempletProtocol = new AddSkillTempletProtocol(title, listTag, startime, endtime, anonymity, desc, timetype, listPic, instalment, bp, pattern, place, lng, lat, quote, quoteunit);
        addSkillTempletProtocol.getDataFromServer(onAddSkillTemplet);
    }

    //技能管理——添加技能模板
    public static void  onGetOneSkillTemplet(BaseProtocol.IResultExecutor onGetOneSkillTemplet,int id) {
        GetSkillTempletProtocol getSkillTempletProtocol = new GetSkillTempletProtocol(id);
        getSkillTempletProtocol.getDataFromServer(onGetOneSkillTemplet);
    }

















}
