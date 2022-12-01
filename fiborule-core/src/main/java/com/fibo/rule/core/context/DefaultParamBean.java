package com.fibo.rule.core.context;

import java.util.concurrent.ConcurrentHashMap;

/**
 *<p>引擎执行过程中默认参数</p>
 *
 *@author JPX
 *@since 2022/11/18 13:22
 */
public class DefaultParamBean {

    private final ConcurrentHashMap<String, Object> dataMap = new ConcurrentHashMap<>();

    private <T> void putDataMap(String key, T t) {
//        if (ObjectUtil.isNull(t)) {
//            throw new NullParamException("data can't accept null param");
//        }
        dataMap.put(key, t);
    }

    public boolean hasData(String key){
        return dataMap.containsKey(key);
    }

    public <T> T getData(String key){
        return (T) dataMap.get(key);
    }

    public <T> void setData(String key, T t){
        putDataMap(key, t);
    }
}
