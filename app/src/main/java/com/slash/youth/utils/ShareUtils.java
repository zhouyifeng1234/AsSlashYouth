package com.slash.youth.utils;

/**
 * Created by zhouyifeng on 2017/1/12.
 */
public class ShareUtils {

    /**
     * 2.详情页面：param：1 需求2服务，oid：服务or需求id，favei：只限分享出去之后使用1,cid: 当前查看用户的id, share: visit分享使用（必须为visit）
     * http://web.slashyounger.com/#!/detail?param=1&share=2&oid=488&cid=10000&favei=1&share=visit
     */
    public static final String DETAIL_SHARE = "http://web.slashyounger.com/#!/detail";


    /**
     * 3.故事页面：需求或服务完成之后分享的页面，type: 1 需求 2 服务，tid: 需求或服务订单id，cid: 当前查看用户的id
     * http://web.slashyounger.com/#!/story?cid=10002&type=1&tid=156
     */
    public static final String STORY_SHARE = "http://web.slashyounger.com/#!/story";

}
