package com.slash.youth.global;

/**
 * Created by zhouyifeng on 2016/8/31.
 */
public class GlobalConstants {
    public static final String SP_NAME = "slash_sp.config";

    public static class SpConfigKey {
        // public static final String IS_GUID = "isGuid";
        // public static final String READ_NEWS_ID = "readNewsId";
        public static final String HOME_IS_DISPLAY_DEMAND_LIST = "homeIsDisplayDemandList";//如果存true，表示展示需求列表，false为展示服务列表
    }


//    各平台的appkey和secret
//    Wechat.appid=wx34aecd5b12c34dcb
//    Wechat.secret=5c309ec7287c5d23ead0a275fa91ee03
//
//    QQ.appid=1105561277
//    QQ.secret=dTjSTFTETKfrFEtl
//
//    Weibo.appid=2605297956
//    Weibo.secret=368e66f115dd4e8912ea647b701aee4b

    /**
     * 项目中所用到的第三方API的APPID
     */
    public static class ThirdAppId {
        public static final String APPID_WECHAT = "wx34aecd5b12c34dcb";
        public static final String APPID_QQ = "1105561277";
        public static final String APPID_WEIBO = "2605297956";
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
//                slash.youth.customer	8500	    6500
//                slash.youth.feed	    8400        6400
//                slash.youth.auth	    8300        6300
//                slash.youth.pay	    8200	    6200
//                slash.youth.ucenter	8100	    6100


//                    二、Mysql账号密码
//                    H:120.27.95.99
//                    U:slashyouth
//                    P:da#@I*O(


        //服务器主机地址
//        public static final String SERVER_HOST = "http://121.42.145.178:8400/";
        public static final String SERVER_HOST = "http://121.42.145.178/feed/v1/api/demand/";
        //ZSS
        public static final String SERVER_HOST_BAR = "http://121.42.145.178/search/v1/api";
        //zss
        public static final String SERVER_HOST_SKILLLABEL = "http://121.42.145.178/";

        /**
         * 以下为服务端各接口的相对地址
         */


        //手机验证码发送
        public static final String SEND_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/v1/phone";
//        public static final String SEND_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/phone";

        //手机验证码验证
        public static final String VERIFICATION_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/v1/pin";
//        public static final String VERIFICATION_PHONE_VERIFICATION_CODE = SERVER_HOST + "auth/pin";

        //手机号登录
        public static final String PHONE_NUMBER_LOGIN = SERVER_HOST + "auth/v1/login/phone";


        //一、[需求]-发布需求
        public static final String PUBLISH_DEMAND = SERVER_HOST + "publish";
        //二、[需求]-需求方取消需求
        public static final String CANCEL_DEMAND = SERVER_HOST + "cancel";
        //三、[需求]-我发布的需求列表
        public static final String MY_PUBLISH_DEMAND_LIST = SERVER_HOST + "mylist";
        //四、[需求]-服务方竞标需求
        public static final String SERVICE_PARTY_BID_DEMAND = SERVER_HOST + "bid";
        //五、[需求]-需求方选择服务方
        public static final String DEMAND_PARTY_SELECT_SERVICE_PARTY = SERVER_HOST + "select";
        //六、[需求]-服务方确认一个服务者
        public static final String SERVICE_PARTY_CONFIRM_SERVANT = SERVER_HOST + "confirm";
        //七、[需求]-查看需求流程日志
        public static final String GET_DEMAND_FLOW_LOG = SERVER_HOST + "log";
        //八、[需求]-服务方拒绝
        public static final String SERVICE_PARTY_REJECT = SERVER_HOST + "reject";
        //九、[需求]-需求方预支付
        public static final String DEMAND_PARTY_PRE_PAYMENT = SERVER_HOST + "payment";
        //十、[需求]-我发布的历史需求列表
        public static final String MY_PUBLISH_HOSTORY_DEMAND_LIST = SERVER_HOST + "myhislist";
        //十一、[需求]-服务方完成任务
        public static final String SERVICE_PARTY_COMPLETE = SERVER_HOST + "complete";
        //十二、[需求]-服务方完成任务(应该是 需求方确认完成 ？？？)
        public static final String DEMAND_PARTY_CONFIRM_COMPLETE = SERVER_HOST + "confirmComplete";
        //十三、[需求]-需求方查看竞标（抢需求服务者）列表
        public static final String DEMAND_PARTY_GET_BIDLIST = SERVER_HOST + "bidlist";
        //十四、[需求]-加载需求描述信息
        public static final String GET_DEMAND_DESC = SERVER_HOST + "descget";
        //十五、[需求]-设置需求描述信息
        public static final String SET_DEMAND_DESC = SERVER_HOST + "descset";
        //十六、[需求]-技能标签(一 二 三 级的技能标签)
        public static final String SKILLLABEL = SERVER_HOST_SKILLLABEL + "static/tag/sys_tag.json";




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

        //二十四、[搜索]-联想词搜索
        public static final String SEARCH_ASSOCIATIVE = SERVER_HOST_BAR + "/common/tag";

        //二十五、[搜索]-[用户&需求&服务]搜索
        public static final String SEARCH_ALL = SERVER_HOST_BAR + "/common/all";

        //二十六、[搜索]-用户搜索
        public static final String SEARCH_USER = SERVER_HOST_BAR + "/user";

        //二十七、[搜索]-需求搜索
        public static final String SEARCH_DEMAND = SERVER_HOST_BAR + "/demand";

        //二十八、[搜索]-服务搜索
        public static final String SEARCH_SERVICE = SERVER_HOST_BAR + "/service";
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
