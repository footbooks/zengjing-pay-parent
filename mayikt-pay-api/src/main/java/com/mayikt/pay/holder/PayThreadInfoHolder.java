package com.mayikt.pay.holder;

import java.util.Map;

public class PayThreadInfoHolder {
    private static ThreadLocal<Map<String,String>> paramsThreadLocal =  new ThreadLocal<Map<String,String>>();
    public static void add(Map<String,String> params){
        paramsThreadLocal.set(params);
    }
    public static Map<String,String> get(){
        return paramsThreadLocal.get();
    }
}
