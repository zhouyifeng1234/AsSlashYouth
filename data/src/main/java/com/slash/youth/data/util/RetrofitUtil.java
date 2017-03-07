package com.slash.youth.data.util;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by acer on 2017/3/6.
 */

public class RetrofitUtil {

    public static RequestBody toRequestBody(String body) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body);
    }
}
