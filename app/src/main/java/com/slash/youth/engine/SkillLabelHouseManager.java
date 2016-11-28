package com.slash.youth.engine;

import com.slash.youth.http.protocol.BaseProtocol;
import com.slash.youth.http.protocol.PersonRelationProtocol;
import com.slash.youth.http.protocol.SkillLabelAllProtocol;

/**
 * Created by zss on 2016/11/22.
 */
public class SkillLabelHouseManager {

    //拉取系统标签接口
    public static void getSkillLabelHouseData(BaseProtocol.IResultExecutor onGetSkillLabelHouseData) {
        SkillLabelAllProtocol skillLabelAllProtocol = new SkillLabelAllProtocol();
        skillLabelAllProtocol.getDataFromServer(onGetSkillLabelHouseData);
    }




}
