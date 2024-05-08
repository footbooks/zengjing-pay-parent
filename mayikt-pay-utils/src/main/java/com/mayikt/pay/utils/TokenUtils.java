package com.mayikt.pay.utils;

import java.util.UUID;

public class TokenUtils {
    public static String getToken(){
        return UUID.randomUUID().toString().replace("-","");
    }
    public static String getToken(String prefix){
        return prefix+getToken();
    }
}
