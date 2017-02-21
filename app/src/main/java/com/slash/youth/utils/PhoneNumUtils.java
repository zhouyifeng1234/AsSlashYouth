package com.slash.youth.utils;

import android.text.TextUtils;

/**
 * Created by zhouyifeng on 2017/1/6.
 */
public class PhoneNumUtils {

    /**
     * 检查输入的手机号是否合法
     */
    public static boolean checkPhoneNum(String phoneNum) {
        //不能为空
        if (TextUtils.isEmpty(phoneNum)) {
            return false;
        }
        //不能大于11位，或者小于11位
        if (phoneNum.length() < 11 || phoneNum.length() > 11) {
            return false;
        }


//        //前几位位必须是运营商号码段
//        String[] startNumArr = "177,134,135,136,137,138,139,150,151,157,158,159,182,187,188,130,131,132,152,155,156,185,186,133,1349,153,180,189,170".split(",");
//        boolean hasStartNum = false;
//        //判断手机号的前几位数必须为以上的号码之一
//        for (int i = 0; i < startNumArr.length; i++) {
//            String startNum = startNumArr[i];
//            if (phoneNum.startsWith(startNum)) {
//                hasStartNum = true;
//                break;
//            }
//        }
//        if (!hasStartNum) {
//            return false;
//        }


        //不能有非数字字符
        for (int i = 0; i < phoneNum.length(); i++) {
            char chaNum = phoneNum.charAt(i);
            if (chaNum > '9' || chaNum < '0') {
                return false;
            }
        }
        return true;
    }
}
