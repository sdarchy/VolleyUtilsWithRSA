package com.wangtian.network.utils;

import java.util.Random;

/**
 * Created by Archy on 16/4/25.
 */
public class AESKeyUtils  {

    public static String getKey(){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i=0;i<16;i++){
            int index = random.nextInt(str.length());
            sb.append(str.charAt(index));

        }
        return sb.toString();
    }
}
