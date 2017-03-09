package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.FileUploadProtocol;
import com.slash.youth.http.protocol.LoginSetAvatarProtocol;
import com.slash.youth.http.protocol.LoginUserHomeInfoProtocol;
import com.slash.youth.http.protocol.LoginUserInfoProtocol;
import com.slash.youth.http.protocol.NewDemandAndServiceProtocol;
import com.slash.youth.http.protocol.OtherUserInfoProtocol;
import com.slash.youth.http.protocol.SaveListTagProtocol;
import com.slash.youth.http.protocol.SaveSlathYouthProtocol;
import com.slash.youth.http.protocol.UserAuthStatusProtocol;

import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/1.
 */
public class UserInfoEngine {
    public static final String ANY_TIME = "随时";
    public static final int  MY_USER_EDITOR = 123;
    public static final int  MY_USER_EDITOR_DIALOG = 1234;

    /**
     * 一、[用戶信息]-用户个人信息（获取别人的用户信息）
     *
     * @param onGetOtherUserInfoFinished
     * @param uid
     * @param isvisitor                  是否访客登记 0否 1是
     */
    public static void getOtherUserInfo(BaseProtocol.IResultExecutor onGetOtherUserInfoFinished, String uid, String isvisitor) {
        OtherUserInfoProtocol otherUserInfoProtocol = new OtherUserInfoProtocol(uid, isvisitor);
        otherUserInfoProtocol.getDataFromServer(onGetOtherUserInfoFinished);
    }

    /**
     * 二、[用戶信息]-获取个人资料(获取我的个人信息资料，自己的信息，当前登陆者的信息)
     */
    public static void getMyUserInfo(BaseProtocol.IResultExecutor onGetMyUserInfoFinished) {
        LoginUserInfoProtocol loginUserInfoProtocol = new LoginUserInfoProtocol();
        loginUserInfoProtocol.getDataFromServer(onGetMyUserInfoFinished);
    }

    /**
     * 十一、[用戶信息]-用户认证流程状态
     */
    public static void getUserAuthStatus(BaseProtocol.IResultExecutor onGetIsAuthFinished) {
        UserAuthStatusProtocol userAuthStatusProtocol = new UserAuthStatusProtocol();
        userAuthStatusProtocol.getDataFromServer(onGetIsAuthFinished);
    }

    /**
     * 十三、[用戶信息]-我的首页数据  (可以获得手机号)
     */
    public static void getMyHomeInfo(BaseProtocol.IResultExecutor onGetMyHomeInfoFinished) {
        LoginUserHomeInfoProtocol loginUserHomeInfoProtocol = new LoginUserHomeInfoProtocol();
        loginUserHomeInfoProtocol.getDataFromServer(onGetMyHomeInfoFinished);
    }

    //[最近列表]-查看用户最近发布的列表
    public static void getNewDemandAndServiceList(BaseProtocol.IResultExecutor onGetNewDemandAndServiceList, long uid, int offset, int limit,int anonymity) {
        NewDemandAndServiceProtocol newDemandAndServiceProtocol = new NewDemandAndServiceProtocol(uid, offset, limit,anonymity);
        newDemandAndServiceProtocol.getDataFromServer(onGetNewDemandAndServiceList);
    }

    //上传图片
//一、[文件]-图片上传
    public static void uploadFile(BaseProtocol.IResultExecutor onUploadFileFinished, String filePath) {
        FileUploadProtocol fileUploadProtocol = new FileUploadProtocol(filePath);
        fileUploadProtocol.getDataFromServer(onUploadFileFinished);
    }

    //设置头像
   // LoginSetAvatarProtocol
    public static void setAvater(BaseProtocol.IResultExecutor onUploadFileFinished, String filePath){
        LoginSetAvatarProtocol loginSetAvatarProtocol = new LoginSetAvatarProtocol(filePath);
        loginSetAvatarProtocol.getDataFromServer(onUploadFileFinished);
    }

    //保存斜杠身份
    public static void onSaveSlathYouth(BaseProtocol.IResultExecutor onSaveSlathYouth, ArrayList<String> list) {
        SaveSlathYouthProtocol saveSlathYouthProtocol = new SaveSlathYouthProtocol(list);
        saveSlathYouthProtocol.getDataFromServer(onSaveSlathYouth);
    }

    //技能标签
    public static void onSaveListTag(BaseProtocol.IResultExecutor onSaveListTag, ArrayList<String> list) {
        SaveListTagProtocol saveListTagProtocol = new SaveListTagProtocol(list);
        saveListTagProtocol.getDataFromServer(onSaveListTag);
    }

}
