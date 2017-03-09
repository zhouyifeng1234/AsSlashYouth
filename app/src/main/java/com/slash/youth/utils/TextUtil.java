package com.slash.youth.utils;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by acer on 2017/3/9.
 */
public class TextUtil {

    public static SpannableString matcherSearchTitle(int color, String text, List<String> keyword) {
        String string = text.toLowerCase();
        SpannableString ss = new SpannableString(text);
        for (String key : keyword) {
            String tempKey = key.toLowerCase();
            Pattern pattern = Pattern.compile(tempKey);
            Matcher matcher = pattern.matcher(string);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                ss.setSpan(new ForegroundColorSpan(color), start, end,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ss;
    }
}
