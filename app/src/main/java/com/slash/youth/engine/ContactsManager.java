package com.slash.youth.engine;

import com.slash.youth.http.protocol.AddFrriendRlationProtocol;
import com.slash.youth.http.protocol.AddMeListProtocol;
import com.slash.youth.http.protocol.AgreeFriendProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CannelCareProtocol;
import com.slash.youth.http.protocol.ContactsMyVisitorProtocol;
import com.slash.youth.http.protocol.DeleteFriendRelationProtocol;
import com.slash.youth.http.protocol.MyFriendListProtocol;
import com.slash.youth.http.protocol.PersonRelationProtocol;
import com.slash.youth.http.protocol.RecommendFriendListProtovol;
import com.slash.youth.http.protocol.TestFriendStatueProcotol;
import com.slash.youth.http.protocol.TestIsFollowProtocol;

/**
 * Created by zss on 2016/11/21.
 */
public class ContactsManager {

    public static  final int FOLLOW_USER_NOT_EXIST_ERROR = 0;//用户不存在错误
    public static  final int FOLLOW_STATUS_SUCCESS = 1;//关注or取消关注成功
    public static  final int FOLLOW_STATUS_ALREADY_ERROR = 2;//已经关注过错误
    public static  final int FOLLOW_STATUS_UNKNOWN_ERROR = 3;//服务端错误
    public static  final int FOLLOW_STATUS_NOT_EXIST_ERROR = 4;//关注关系不存在错误
    public static  final String CARE_ME = "关注我的";
    public static  final String MY_CARE = "我关注";
    public static  final String ADD_ME = "加我的";
    public static  final String MY_ADD = "我加的";

    //[我的人脉]-人脉首页
    public static void getPersonRelationFirstPage(BaseProtocol.IResultExecutor onPersonRelationFirstPage) {
        PersonRelationProtocol personRelationProtocol = new PersonRelationProtocol();
        personRelationProtocol.getDataFromServer(onPersonRelationFirstPage);
    }

    //[我的访客]-访客列表
    public static void getMyVisitorList(BaseProtocol.IResultExecutor onGetMyVisitorList,int offset,int limit) {
        ContactsMyVisitorProtocol contactsMyVisitorProtocol = new ContactsMyVisitorProtocol(offset,limit);
        contactsMyVisitorProtocol.getDataFromServer(onGetMyVisitorList);
    }

    //我的好友列表
    public static void getMyFriendList(BaseProtocol.IResultExecutor onGetMyFriendList,int offset,int limit) {
        MyFriendListProtocol myFriendListProtocol = new MyFriendListProtocol(offset,limit);
        myFriendListProtocol.getDataFromServer(onGetMyFriendList);
    }

    //[推荐]-推荐好友
    public static void getMyRecommendFriendList(BaseProtocol.IResultExecutor onGetMyRecommendFriendList,int limit) {
        RecommendFriendListProtovol recommendFriendListProtovol = new RecommendFriendListProtovol(limit);
        recommendFriendListProtovol.getDataFromServer(onGetMyRecommendFriendList);
    }

    //[好友]-查看加我的列表
    public static void getAddMeList(BaseProtocol.IResultExecutor onGetAddMeList,int offset,int limit,String url) {
        AddMeListProtocol addMeListProtocol = new AddMeListProtocol(offset,limit,url);
        addMeListProtocol.getDataFromServer(onGetAddMeList);
    }

    //同意添加好友
    public static void onAgreeFriendProtocol(BaseProtocol.IResultExecutor onAgreeFriendProtocol,long uid,String extra) {
        AgreeFriendProtocol agreeFriendProtocol = new AgreeFriendProtocol(uid,extra);
        agreeFriendProtocol.getDataFromServer(onAgreeFriendProtocol);
    }

    //[好友]-发起好友申请
    public static void onAddFriendRelationProtocol(BaseProtocol.IResultExecutor onAddFriendRelationProtocol,long uid,String extra) {
        AddFrriendRlationProtocol addFrriendRlationProtocol = new AddFrriendRlationProtocol(uid,extra);
        addFrriendRlationProtocol.getDataFromServer(onAddFriendRelationProtocol);
    }

    //[好友]-验证好友状态
    public static void onTestFriendStatueProtocol(BaseProtocol.IResultExecutor onTestFriendStatueProtocol,long uid) {
        TestFriendStatueProcotol testFriendStatueProcotol = new TestFriendStatueProcotol(uid);
        testFriendStatueProcotol.getDataFromServer(onTestFriendStatueProtocol);
    }

    //[关注]-关注动作
    public static void onCareTAProtocol(BaseProtocol.IResultExecutor onCareTAProtocol,long uid) {
        TestFriendStatueProcotol testFriendStatueProcotol = new TestFriendStatueProcotol(uid);
        testFriendStatueProcotol.getDataFromServer(onCareTAProtocol);
    }

    //[关注]-我和某用户的关系
    public static void onTestIsFollow(BaseProtocol.IResultExecutor onCareTAProtocol,long uid) {
        TestIsFollowProtocol testIsFollowProtocol = new TestIsFollowProtocol(uid);
        testIsFollowProtocol.getDataFromServer(onCareTAProtocol);
    }

    //[好友]-解除（删除）好友关系（包括未建立完成的好友关系）
    public static void deleteFriendRelationProtocol(BaseProtocol.IResultExecutor onDeleteFriendRelationProtocol,long uid,String extra) {
        DeleteFriendRelationProtocol deleteFriendRelationProtocol = new DeleteFriendRelationProtocol(uid,extra);
        deleteFriendRelationProtocol.getDataFromServer(onDeleteFriendRelationProtocol);
    }

   //[关注]-取消关注动作
   public static void onCannelCareProtocol(BaseProtocol.IResultExecutor onCannelCareProtocol,long uid) {
       CannelCareProtocol cannelCareProtocol = new CannelCareProtocol(uid);
       cannelCareProtocol.getDataFromServer(onCannelCareProtocol);
   }













}
