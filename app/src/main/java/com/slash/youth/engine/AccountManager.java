package com.slash.youth.engine;

import com.slash.youth.http.protocol.TradePasswordStatusProtocol;
import com.slash.youth.http.protocol.BaseProtocol;

/**
 * Created by Administrator on 2016/8/31.
 */
public class AccountManager {

    /**
     * 七、[设置]-判断是否有交易密码
     *
     * @param onGetStatusFinished
     */
    public static void getTradePasswordStatus(BaseProtocol.IResultExecutor onGetStatusFinished) {
        TradePasswordStatusProtocol tradePasswordStatusProtocol = new TradePasswordStatusProtocol();
        tradePasswordStatusProtocol.getDataFromServer(onGetStatusFinished);
    }
}
