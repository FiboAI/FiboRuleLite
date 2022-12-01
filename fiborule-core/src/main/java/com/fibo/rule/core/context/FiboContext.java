package com.fibo.rule.core.context;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>引擎执行上下文</p>
 *
 * @author JPX
 * @since 2022-11-18 13:18
 */
@Data
@Slf4j
public class FiboContext {

    private static final String REQUEST = "_request";

    private static final String EXCEPTION = "_exception";

    private static final String SUB_EXCEPTION_PREFIX = "_sub_exception_";

    private static final String IF_NODE_PREFIX = "_if_";

    private static final String SWITCH_NODE_PREFIX = "_switch_";

    protected ConcurrentHashMap<String, Object> metaDataMap = new ConcurrentHashMap<>();

    private List<Object> contextBeanList;

    public FiboContext() {

    }

    public FiboContext(List<Object> contextBeanList) {
        this.contextBeanList = contextBeanList;
    }

    public <T> void setRequestData(T t){
        putMetaDataMap(REQUEST, t);
    }

    public <T> T getRequestData() {
        return (T) this.metaDataMap.get(REQUEST);
    }

    private <T> void putMetaDataMap(String key, T t) {
        if (ObjectUtil.isNull(t)) {
            // TODO: 2022/11/18 抛出异常
            //data slot is a ConcurrentHashMap, so null value will trigger NullPointerException
//            throw new NullParamException("data slot can't accept null param");
            return;
        }
        metaDataMap.put(key, t);
    }

    public void setIfResult(String key, boolean result){
        putMetaDataMap(IF_NODE_PREFIX + key, result);
    }

    public boolean getIfResult(String key){
        return (boolean) metaDataMap.get(IF_NODE_PREFIX + key);
    }

    public <T> void setSwitchResult(String key, T t){
        putMetaDataMap(SWITCH_NODE_PREFIX + key, t);
    }

    public <T> T getSwitchResult(String key){
        return (T) metaDataMap.get(SWITCH_NODE_PREFIX + key);
    }

    public <T> T getContextBean(Class<T> contextBeanClazz) {
        T t = (T) this.contextBeanList.stream().filter((o) -> {
            return o.getClass().equals(contextBeanClazz);
        }).findFirst().orElse((Object)null);
        if (t == null) {
            throw new RuntimeException();
        } else {
            return t;
        }
    }

    public <T> T getFirstContextBean(){
        Class<T> firstContextBeanClazz = (Class<T>) this.getContextBeanList().get(0).getClass();
        return this.getContextBean(firstContextBeanClazz);
    }

    public Exception getException() {
        return (Exception) this.metaDataMap.get(EXCEPTION);
    }

    public void setException(Exception e) {
        putMetaDataMap(EXCEPTION, e);
    }

    public Exception getSubException(Long engineId) {
        return (Exception) this.metaDataMap.get(SUB_EXCEPTION_PREFIX + engineId);
    }

    public void setSubException(Long engineId, Exception e) {
        putMetaDataMap(SUB_EXCEPTION_PREFIX + engineId, e);
    }

}
