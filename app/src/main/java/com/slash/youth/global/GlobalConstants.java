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
        //public static final String SERVER_HOST = "http://121.42.145.178:8400/";
        public static final String SERVER_HOST = "http://121.42.145.178/";

        //需求流程相关接口地址前缀
        public static final String SERVER_HOST_DEMAND = "http://121.42.145.178/feed/v1/api/demand/";
        //图片上传下载接口地址前缀
        public static final String SERVER_HOST_IMG_UPLOAD_DOWNLOAD = "http://121.42.145.178/file/v1/api/";
        //我的任务列表接口 地址前缀
        public static final String SERVER_HOST_MY_TASK_LIST = "http://121.42.145.178/feed/v1/api/mytask/";
        //用户设置接口 地址前缀
        public static final String SERVER_HOST_USER_SETTING = "http://121.42.145.178/uinfo/v1/api/config/";
        //评价和分享上报接口 地址前缀
        public static final String SERVER_HOST_COMMENT_SHARE = "http://121.42.145.178/feed/v1/api/evaluation/";
        //服务流程接口地址前缀
        public static final String SERVER_HOST_SERVICE = "http://121.42.145.178/feed/v1/api/service/";
        //ZSS
        public static final String SERVER_HOST_SKILLLABEL = "http://121.42.145.178/";
        public static final String SERVER_HOST_BAR = "http://121.42.145.178/search/v1/api";

        //zss [我的模块]
        public static final String SERVER_HOST_MY_USERINFO = "http://121.42.145.178/uinfo/v1/api";
        //ZSS 我的账户
        public static final String SERVER_HOST_MY_ACCOUNT = "http://121.42.145.178/uinfo/v1/api/wallet";

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

        //一、[需求]-我的任务列表
        public static final String GET_MY_TASK_LIST = SERVER_HOST_MY_TASK_LIST + "list";
        //二、[需求]-我的任务ITEM
        public static final String GET_MY_TASK_ITEM = SERVER_HOST_MY_TASK_LIST + "one";


        //一、[文件]-图片上传
        public static final String IMG_UPLOAD = SERVER_HOST_IMG_UPLOAD_DOWNLOAD + "upload";

        //二、[文件]-图片下载
        public static final String IMG_DOWNLOAD = SERVER_HOST_IMG_UPLOAD_DOWNLOAD + "download";


        //一、[需求]-发布需求
        public static final String PUBLISH_DEMAND = SERVER_HOST_DEMAND + "publish";
        //二、[需求]-需求方取消需求
        public static final String CANCEL_DEMAND = SERVER_HOST_DEMAND + "cancel";
        //三、[需求]-我发布的需求列表
        public static final String MY_PUBLISH_DEMAND_LIST = SERVER_HOST_DEMAND + "mylist";
        //四、[需求]-服务方竞标需求
        public static final String SERVICE_PARTY_BID_DEMAND = SERVER_HOST_DEMAND + "bid";
        //五、[需求]-需求方选择服务方
        public static final String DEMAND_PARTY_SELECT_SERVICE_PARTY = SERVER_HOST_DEMAND + "select";
        //六、[需求]-服务方确认一个服务者
        public static final String SERVICE_PARTY_CONFIRM_SERVANT = SERVER_HOST_DEMAND + "confirm";
        //七、[需求]-查看需求流程日志
        public static final String GET_DEMAND_FLOW_LOG = SERVER_HOST_DEMAND + "log";
        //八、[需求]-服务方拒绝
        public static final String SERVICE_PARTY_REJECT = SERVER_HOST_DEMAND + "reject";
        //九、[需求]-需求方预支付
        public static final String DEMAND_PARTY_PRE_PAYMENT = SERVER_HOST_DEMAND + "payment";
        //十、[需求]-我发布的历史需求列表
        public static final String MY_PUBLISH_HOSTORY_DEMAND_LIST = SERVER_HOST_DEMAND + "myhislist";
        //十一、[需求]-服务方完成任务
        public static final String SERVICE_PARTY_COMPLETE = SERVER_HOST_DEMAND + "complete";
        //十二、[需求]-服务方完成任务(应该是 需求方确认完成 ？？？)
        public static final String DEMAND_PARTY_CONFIRM_COMPLETE = SERVER_HOST_DEMAND + "confirmComplete";
        //十三、[需求]-需求方查看竞标（抢需求服务者）列表
        public static final String DEMAND_PARTY_GET_BIDLIST = SERVER_HOST_DEMAND + "bidlist";
        //十四、[需求]-加载需求描述信息
        public static final String GET_DEMAND_DESC = SERVER_HOST_DEMAND + "descget";
        //十五、[需求]-设置需求描述信息
        public static final String SET_DEMAND_DESC = SERVER_HOST_DEMAND + "descset";
        //十六、[需求]-需求方获取意向单列表
        public static final String GET_DEMAND_PURPOSE_LIST = SERVER_HOST_DEMAND + "purposelist";
        //十八、[需求]-服务方确认同意退款
        public static final String SERVICE_PARTY_AGREE_REFUND = SERVER_HOST_DEMAND + "agreeRefund";
        //十九、[需求]-服务方不同意退款并申请平台介入
        public static final String SERVICE_PARTY_INTERVENTION = SERVER_HOST_DEMAND + "intervention";
        //[需求]-需求方要求延期付款接口
        public static final String DEMAND_PARTY_DELAY_PAY = SERVER_HOST_DEMAND + "rollback";
        //二、[需求]-需求方淘汰服务者
        public static final String DEMAND_ELIMINATE_SERVICE = SERVER_HOST_DEMAND + "eliminate";
        //二、[需求]-查看需求详情
        public static final String GET_DEMAND_DETAIL = SERVER_HOST_DEMAND + "queryone";
        //七、[设置]-判断是否有交易密码
        public static final String GET_TRADE_PASSWORD_STATUS = SERVER_HOST_USER_SETTING + "tradepassword/status";
        //一、[需求]-需求方评价接口
        public static final String DEMAND_PARTY_COMMENT = SERVER_HOST_COMMENT_SHARE + "publish";

        //一、[服务]-发布服务
        public static final String PUBLISH_SERVICE = SERVER_HOST_SERVICE + "publish";

        //十六、[需求]-技能标签(一 二 三 级的技能标签)
        public static final String SKILLLABEL = SERVER_HOST_SKILLLABEL + "static/tag/sys_tag.json";
        //十七、[需求]-技能标签(所有自定义技能标签)
        public static final String SKILLLABEL_GET = SERVER_HOST_SKILLLABEL + "recommend/v1/api/tag/get";
        //十八、[需求]-技能标签(增加自定义技能标签)
        public static final String SKILLLABEL_CREATE = SERVER_HOST_SKILLLABEL + "recommend/v1/api/tag/create";
        //十九、[需求]-技能标签(删除自定义技能标签)
        public static final String SKILLLABEL_DELETE = SERVER_HOST_SKILLLABEL + "recommend/v1/api/tag/delete";


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


        //一、[我的]-[用户中心]-用户个人信息
        public static final String MY_USERINFO = SERVER_HOST_MY_USERINFO + "/vcard/info/get";
        //二、[我的]-[用户中心]-获取个人资料
        public static final String GET_USERINFO = SERVER_HOST_MY_USERINFO + "/vcard/basic/info/get";
        //三、[我的]-[用户中心]-保存个人资料
        public static final String SAVE_USERINFO = SERVER_HOST_MY_USERINFO + "/vcard/basic/info/set";
        //四、[我的]-[用户中心]-设置所在地
        public static final String SET_LOCATION = "/vcard/location/set";
        //五、[我的]-[用户中心]-设置斜杠身份
        public static final String SET_SLASH_IDENTITY = "/vcard/identity/set";
        //六、[我的]-[用户中心]-设置公司和职位
        public static final String SET_SLASH_COMPANY_AND_POSITION = "/vcard/company/set";
        //七、[我的]-[用户中心]-设置设置行业和方向
        public static final String SET_SLASH_INDUSTRY = "/vcard/industry/set";
        //八、[我的]-[用户中心]-设置设置头像
        public static final String SET_SLASH_AVATAR = "/vcard/avatar/set";
        //九、[我的]-[用户中心]-设置用户标签
        public static final String SET_SLASH_TAG = "/vcard/tag/set";
        //十、[我的]-[用户中心]-设置用户认证
        public static final String SET_SLASH_AUTH = "/real/auth";
        //十一、[我的]-[用户中心]-用户认证流程状态
        public static final String SLASH_STATUS = "/real/status";
        //十二、[我的]-[用户中心]-举报用户
        public static final String CLAIMS = "/claims";
        //十三、[我的]-[用户中心]-我的首页数据
        //登出
        public static final String MY_INFO = SERVER_HOST_MY_USERINFO +"/my/info/get";
        //十四，[我的]-[用户中心]-认证
        public static final String MY_CHECKOUT_AUTH = SERVER_HOST +"/real/auth";
        //十五，[我的]-[用户中心]-用户认证流程状态
       // public static final String MY_CHECKOUT_AUTH_SRATUS = SERVER_HOST +"/real/status";
        //十六，[我的]-[用户中心]-登出
        public static final String MY_LOGOUT = SERVER_HOST +"auth/v1/logout";

        //一、[我的账户]-获取我的账户信息
        public static final String MY_ACCOUNT = SERVER_HOST_MY_ACCOUNT + "/info/get";
        //二、[我的账户]-获取我的交易流水
        public static final String MY_TRANSACTIONRECORE = SERVER_HOST_MY_ACCOUNT + "/trade/stream";

        //一、[设置]-查询时间免打扰设置
        public static final String SERVER_HOST_SETTING = "http://121.42.145.178/uinfo/v1/api/config";
        public static final String SET_TIME_GET = SERVER_HOST_SETTING + "/dnd/time/get";
        //二、[设置]-时间免打扰设置
        //public static final String SET_TIME_SET = SERVER_HOST_SETTING +"/dnd/time/set";
        public static final String SET_TIME_SET = "/dnd/time/set";
        //三、[设置]-获取消息免打扰设置
        public static final String SET_MSG_GET = SERVER_HOST_SETTING + "/dnd/msg/get";
        //四、[设置]-消息免打扰设置
        public static final String SET_MSG_SET = "/dnd/msg/set";
        //五，上传图片
        public static final String UPLOAD_PHOTO = "http://121.42.145.178/file/v1/api" + "/upload";
        // 五、[设置]-设置新的交易密码
        public static final String SET_PASSWORD = SERVER_HOST_SETTING + "/tradepassword/set";
        //六、[设置]-创建交易密码
        public static final String CREATE_PASSWORD = SERVER_HOST_SETTING + "/tradepassword/create";

        //第三方，获得第三方平台列表
        public static final String GET_BINDING = SERVER_HOST + "auth/v1/login/getBind";
        //绑定第三方账号
        public static final String LOGIN_BINDING = SERVER_HOST + "auth/v1/login/bind";
        //解绑第三方账号
        public static final String UNBINDING = SERVER_HOST + "auth/v1/login/unbind";

        //我的收藏
        public static final String COLLECTION_HOST = "http://121.42.145.178/feed/v1/api/myfavorite";
        public static final String MY_COLLECTION_LIST = COLLECTION_HOST+"/list";
        public static final String MY_ADD_COLLECTION_ITEM = COLLECTION_HOST+"/add";
        public static final String MY_DELETE_COLLECTION_ITEM = COLLECTION_HOST+"/del";

        //管理我发布的
        public static final String MANAGE_PUBLISH_HOST ="http://121.42.145.178/feed/v1/api/mytaskmanagement";
        public static final String MANAGE_PUBLISH_LIST =MANAGE_PUBLISH_HOST+"/list";
        public static final String MANAGE_PUBLISH_DELETE =MANAGE_PUBLISH_HOST+"/del";
        public static final String MANAGE_PUBLISH_UP_AND_DOWN =MANAGE_PUBLISH_HOST+"/manage";

        //技能管理
        public static final String SKILL_MANAGE_HOST ="http://121.42.145.178/feed/v1/api/myservicemanagement";
        public static final String SKILL_MANAGE_LIST =SKILL_MANAGE_HOST+"/list";
        public static final String SKILL_MANAGE_DELETE =SKILL_MANAGE_HOST+"/del";
        public static final String ADD_SKILL_TEMPLET =SKILL_MANAGE_HOST+"/add";
        public static final String GET_SKILL_TEMPLET =SKILL_MANAGE_HOST+"/one";

        //[我的人脉]-人脉首页
        public static final String PERSON_RELATION_HOST ="http://121.42.145.178/uinfo/v1/api/relation";
       //人脉首页
       public static final String PERSON_RELATION_FIRST_PAGER =PERSON_RELATION_HOST+"/facade";
       //[我的访客]-访客列表
       public static final String MY_VISITOR_HOST ="http://121.42.145.178/uinfo/v1/api/relation/visitor";
        public static final String MY_VISITOR_LIST = MY_VISITOR_HOST+"/list";

        //一、[好友]-查看我的好友列表
        public static final String MY_FRIEND_LIST_HOST = "http://121.42.145.178/uinfo/v1/api/relation/friend";
        public static final String MY_FRIEND_LIST = MY_FRIEND_LIST_HOST+"/list";
        //[好友]-查看加我的列表
        public static final String MY_FRIEND_LIST_ADD_ME_LIST =MY_FRIEND_LIST_HOST+"/addmelist";
        //同意添加为好友
        public static final String  AGREE_FRIEND_RELATION = MY_FRIEND_LIST_HOST+"/approval";
        //[好友]-发起好友申请
        public static final String  ADD_FRIEND_RELATION = MY_FRIEND_LIST_HOST+"/add";
        //[好友]-验证好友状态
        public static final String  TEST_FRIEND_STATUE = MY_FRIEND_LIST_HOST+"/isfriend";
        //[好友]-解除（删除）好友关系（包括未建立完成的好友关系）
        public static final String DELETE_FRIEND_RELATION = MY_FRIEND_LIST_HOST+"/del";

        //三、[关注]-查询我关注的人
        public static final String  RELATION_HOST = "http://121.42.145.178/uinfo/v1/api/relation/follow";
        public static final String  MY_CARE_PERSON = RELATION_HOST+"/myfollow";
        //[关注]-查询我的粉丝
        public static final String  CARE_ME_PERSON = RELATION_HOST+"/myfans";
       //[关注]-关注动作
       public static final String  CARE_TA = RELATION_HOST+"/add";
        //[关注]-我和某用户的关系
        public static final String IS_FOLLOW = RELATION_HOST+"/isfollow";
        //[关注]-取消关注动作
        public static final String CANNEL_CARE = RELATION_HOST+"/cancel";

        //[推荐]-推荐好友
        public static final String RECOMMONEND_FRIEND_LIST = "http://121.42.145.178/uinfo/v1/api/friend/recommend"+"/list";






    }


    /**
     * rescode定义
     */
    public static final class Rescode {

        public static final int RES_SUCCESS = 0;
        public static final int RES_FAIL = 1;
        public static final int RES_INVALID_PARAMS = 2;//参数错误
        public static final int RES_INVALID_TOKEN = 3;
        public static final int RES_TOKEN_TIMEOUT = 4;
        public static final int RES_INVALID_USERNAME_PASSWORD = 5;
        public static final int RES_USER_EXISTS = 6;
        //zss 新加的，根据后端返回的值
        public static final int RES_TAG_EXIST = 50;

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
