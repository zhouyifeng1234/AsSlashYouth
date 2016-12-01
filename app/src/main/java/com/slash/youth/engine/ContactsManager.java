package com.slash.youth.engine;

import com.slash.youth.http.protocol.AddMeListProtocol;
import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.ContactsMyVisitorProtocol;
import com.slash.youth.http.protocol.MyFriendListProtocol;
import com.slash.youth.http.protocol.PersonRelationProtocol;
import com.slash.youth.http.protocol.RecommendFriendListProtovol;

/**
 * Created by zss on 2016/11/21.
 */
public class ContactsManager {

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







}
