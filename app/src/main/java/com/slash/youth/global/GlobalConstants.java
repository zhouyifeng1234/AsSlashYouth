package com.slash.youth.global;

/**
 * Created by zhouyifeng on 2016/8/31.
 */
public class GlobalConstants {
    public static final String SP_NAME = "slash_sp.config";

    public static class SpConfigKey {
        // public static final String IS_GUID = "isGuid";
        // public static final String READ_NEWS_ID = "readNewsId";
    }


    /**
     * 项目中所用到的第三方API的APPID
     */
    public static class ThirdAppId {
        public static final String APPID_WECHAT = "";
        public static final String APPID_QQ = "";
        public static final String APPID_WEIBO = "";
        public static final String APPID_ALIPAY = "";
        public static final String APPID_RONG_CLOUD = "";


    }

    public static class HttpUrl {

//                一、测试机器地址和登陆方式
//                IP                  NAME                    密码/ssh
//                121.42.145.178      slash-youth-office-01
//
//                114.215.83.138      slash-youth-office-02
//
//                120.27.95.99        slash-youth-office-03


//                服务HTTP端口和RPC端口汇总
//
//                服务名称              HTTP        RPC        备注
//                slash.youth.auth	    8300
//                slash.youth.pay	    8200	    6200
//                slash.youth.ucenter	8100	    6100


//                    二、Mysql账号密码
//                    H:120.27.95.99
//                    U:slashyouth
//                    P:da#@I*O(


        //服务器主机地址
        public static final String SERVER_HOST = "http://121.42.145.178/";


        /**
         * 以下为服务端各接口的相对地址
         */


        //手机验证码发送
//        public static final String SEND_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/v1/phone";
        public static final String SEND_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/phone";

        //手机验证码验证
//        public static final String VERIFICATION_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/v1/pin";
        public static final String VERIFICATION_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/pin";



        //一、[用户认证]-真实用户认证接口
        public static final String REAL_USER_AUTH = SERVER_HOST + "uinfo/v1/api/real/auth";

        //二、[用户认证]-查询用户认证状态接口
        public static final String GET_USER_AUTH_STATE = SERVER_HOST + "uinfo/v1/api/real/status";

        //三、[用户信息]-获取用户名片接口
        public static final String GET_USER_VCARD = SERVER_HOST + "uinfo/v1/api/vcard/info";

        //四、[用户信息]-用户资料接口(用于编辑用户资料加载)
        public static final String USER_PERSON_INFO = SERVER_HOST + "uinfo/v1/api/vcard/person/info";

        //五、[用户信息]-获取用户技能标签
        public static final String GET_USER_SKILL_LABEL = SERVER_HOST + "uinfo/v1/api/vcard/skilllabel/get";

        //六、[用户信息]-设置用户技能标签
        public static final String SET_USER_SKILL_LABEL = SERVER_HOST + "uinfo/v1/api/vcard/skilllabel/set";

        //七、[用户信息]-系统技能标签库接口
        public static final String SYSTEM_SKILL_LABEL = SERVER_HOST + "uinfo/v1/api/vcard/default/skill";

        //八、[用户信息]-添加斜杠职业
        public static final String ADD_SLASH_JOB = SERVER_HOST + "uinfo/v1/api/vcard/profession/add";

        //九、[用户信息]-获取斜杠职业
        public static final String GET_SLASH_JOB = SERVER_HOST + "uinfo/v1/api/vcard/profession/get";

        //十、[用户信息]-设置用户地区
        public static final String SET_USER_LOCATION = SERVER_HOST + "uinfo/v1/api/vcard/location/set";

        //十一、[用户信息]-设置用户姓名
        public static final String SET_USER_NAME = SERVER_HOST + "uinfo/v1/api/vcard/name/set";

        //十二、[用户信息]-设置用户性别
        public static final String SET_USER_GENDER = SERVER_HOST + "uinfo/v1/api/vcard/sex/set";

        //十三、[用户信息]-设置用户技能描述
        public static final String SET_USER_SKILL_DESCRIPTION = SERVER_HOST + "uinfo/v1/api/vcard/desc/set";

        //十四、[用户教育信息]-用户一个教育信息
        public static final String GET_ONE_USER_DEU_INFO = SERVER_HOST + "uinfo/v1/api/vcard/edu/one";

        //十五、[用户教育信息]-删除一个教育信息接口
        public static final String DEL_ONE_USER_DEU_INFO = SERVER_HOST + "uinfo/v1/api/vcard/edu/del";

        //十六、[用户教育信息]-增加一个教育信息接口
        public static final String ADD_ONE_USER_DEU_INFO = SERVER_HOST + "uinfo/v1/api/vcard/edu/add";

        //十七、[用户工作经历信息]-获取用户全部教育信息
        public static final String GET_ALL_USER_EDU_INFO = SERVER_HOST + "uinfo/v1/api/vcard/edu/all";

        //十八、[用户工作经历信息]-用户一个工作经历信息
        public static final String GET_ONE_USER_WORK_INFO = SERVER_HOST + "uinfo/v1/api/vcard/cert/one";

        //十九、[用户工作经历信息]-删除一个工作经历接口
        public static final String DEL_ONE_USER_WORK_INFO = SERVER_HOST + "uinfo/v1/api/vcard/cert/del";

        //二十、[用户工作经历信息]-增加一个工作经历接口
        public static final String ADD_ONE_USER_WORK_INFO = SERVER_HOST + "uinfo/v1/api/vcard/cert/add";

        //二十一、[用户工作经历信息]-获取用户全部工作经历信息
        public static final String GET_ALL_USER_WORK_INFO = SERVER_HOST + "uinfo/v1/api/vcard/cert/all";

        //二十二、[用户信息]-上传用户头像
        public static final String UPLOAD_USER_AVATAR = SERVER_HOST + "uinfo/v1/api/vcard/avatar/upload";

        //二十三、[用户信息]-下载用户头像
        public static final String DOWNLOAD_USER_AVATAR = SERVER_HOST + "uinfo/v1/api/vcard/avatar/upload";

    }


    /**
     * rescode定义
     */
    public static final class Rescode {

        public static final int RES_SUCCESS = 0;
        public static final int RES_FAIL = 1;
        public static final int RES_INVALID_PARAMS = 2;
        public static final int RES_INVALID_TOKEN = 3;
        public static final int RES_TOKEN_TIMEOUT = 4;
        public static final int RES_INVALID_USERNAME_PASSWORD = 5;
        public static final int RES_USER_EXISTS = 6;

        public static final int RES_INVALID_PIN = 7;

        public static final int RES_INVALID_UID = 8;
    }

    /**
     * 第三方平台类型
     */
    public static final class LoginPlatformType {
        public static final byte WECHAT = 1;
        public static final byte QQ = 2;
        public static final byte WEIBO = 3;
    }
}
