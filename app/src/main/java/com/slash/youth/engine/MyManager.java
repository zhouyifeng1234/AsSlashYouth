package com.slash.youth.engine;

import com.slash.youth.domain.MyFirstPageBean;
import com.slash.youth.domain.UserInfoItemBean;
import com.slash.youth.http.protocol.AddMyCollectionItemProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CheckoutAuthProtocol;
import com.slash.youth.http.protocol.GetUserInfoProtocol;
import com.slash.youth.http.protocol.MyCollectionListProtocol;
import com.slash.youth.http.protocol.MyFirstPageProtocol;
import com.slash.youth.http.protocol.MyUserInfoProtocol;
import com.slash.youth.http.protocol.SetMsgProtocol;
import com.slash.youth.utils.LogKit;

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
    public static void  getMyCollectionList(BaseProtocol.IResultExecutor onGetMyCollectionList,int offset,int limit,String url) {
        MyCollectionListProtocol myCollectionListProtocol = new MyCollectionListProtocol(offset,limit,url);
        myCollectionListProtocol.getDataFromServer(onGetMyCollectionList);
    }

    //添加我的收藏
    public static void  addMyCollectionList(BaseProtocol.IResultExecutor onAddMyCollectionList,int type,int tid,String url) {
        AddMyCollectionItemProtocol addMyCollectionItemProtocol = new AddMyCollectionItemProtocol(type,tid,url);
        addMyCollectionItemProtocol.getDataFromServer(onAddMyCollectionList);
    }









}
