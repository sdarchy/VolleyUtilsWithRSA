package com.wangtian.network.utils;

import android.text.TextUtils;

/**
 * Created by Archy on 16/4/26.
 */
public class StringUtils {
    public static String substringBeforeLast(final String str, final String separator) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(separator)) {
            return str;
        }
        final int pos = str.lastIndexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }
}
