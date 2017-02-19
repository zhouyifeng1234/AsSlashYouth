package com.slash.youth.domain;


import java.util.ArrayList;

/**
 * Created by zhouyifeng on 2016/12/23.
 */
public class ThirdPayChargeBean {
    public int rescode;
    public Data data;

    public class Data {
        public Charge charge;
    }

    public class Charge {
        public long amount;
        public long amountRefunded;
        public long amountSettle;
        public String app;
        public String body;
        public String channel;
        public String clientIp;
        public long created;
        public Credential credential;
        public String currency;
        public String description;
        public Extra extra;
        public String failureCode;
        public String failureMsg;
        public String id;
        public boolean livemode;
        public Metadata metadata;
        public String object;
        public String orderNo;
        public boolean paid;
        public boolean refunded;
        public Refunds refunds;
        public String subject;
        public long timeExpire;
        public String timePaid;
        public String timeSettle;
        public String transactionNo;
    }

    public class Credential {
        public Alipay alipay;
        public WX wx;
        public String object;
    }

    public class Extra {

    }

    public class Metadata {

    }

    public class Refunds {
        public ArrayList data;
        public boolean hasMore;
        public String object;
        public String uRL;
    }

    public class Alipay {
        public String orderInfo;
    }

    public class WX {
        public String appId;
        public String nonceStr;
        public String packageValue;
        public String partnerId;
        public String prepayId;
        public String sign;
        public String timeStamp;
    }

}
