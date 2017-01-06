package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.CreateTradePasswordProtocol;
import com.slash.youth.http.protocol.MyAccountProtocol;
import com.slash.youth.http.protocol.TradePasswordStatusProtocol;
import com.slash.youth.http.protocol.TransactionRecoreProtocol;

/**
 * Created by Administrator on 2016/8/31.
 */
public class AccountManager {

    /**
     * 一、[我的账户]-获取我的账户信息
     */
    public static void getMyAccountInfo(BaseProtocol.IResultExecutor onGetMyAccountInfoFinished) {
        MyAccountProtocol myAccountProtocol = new MyAccountProtocol();
        myAccountProtocol.getDataFromServer(onGetMyAccountInfoFinished);
    }

    /**
     * 六、[设置]-创建交易密码
     *
     * @param onCreateTradePasswordFinished
     * @param pass                          客户端密码明文密码经过MD5加密的字符串
     */
    public static void createTradePassword(BaseProtocol.IResultExecutor onCreateTradePasswordFinished, String pass) {
        CreateTradePasswordProtocol createTradePasswordProtocol = new CreateTradePasswordProtocol(pass);
        createTradePasswordProtocol.getDataFromServer(onCreateTradePasswordFinished);
    }


    /**
     * 七、[设置]-判断是否有交易密码
     *
     * @param onGetStatusFinished
     */
    public static void getTradePasswordStatus(BaseProtocol.IResultExecutor onGetStatusFinished) {
        TradePasswordStatusProtocol tradePasswordStatusProtocol = new TradePasswordStatusProtocol();
        tradePasswordStatusProtocol.getDataFromServer(onGetStatusFinished);
    }

    //获取我的交易流水
    public static void getTransactionRecore(BaseProtocol.IResultExecutor onGetTransactionRecore,int offset,int limit) {
        TransactionRecoreProtocol transactionRecoreProtocol = new TransactionRecoreProtocol(offset,limit);
        transactionRecoreProtocol.getDataFromServer(onGetTransactionRecore);
    }

}
