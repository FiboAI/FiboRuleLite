package com.fibo.rule.core.context;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.engine.id.IdGeneratorHolder;
import com.fibo.rule.core.engine.step.NodeStep;
import com.fibo.rule.core.exception.NullParamException;
import com.fibo.rule.core.property.FiboRuleConfigGetter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * <p>引擎执行上下文</p>
 *
 * @author JPX
 * @since 2022-11-18 13:18
 */
@Data
@Slf4j
public class FiboContext {

    private static final String REQUEST_ID = "_request_id";

    private static final String REQUEST = "_request";

    private static final String ENGINE_ID = "_engine_id";

    private static final String EXCEPTION = "_exception";

    private static final String IF_NODE_PREFIX = "_if_";

    private static final String SWITCH_NODE_PREFIX = "_switch_";

    protected ConcurrentHashMap<String, Object> metaDataMap = new ConcurrentHashMap<>();

    private List<Object> contextBeanList;

    private final Deque<NodeStep> executeSteps = new ConcurrentLinkedDeque<>();

    private String executeStepsStr;

    public FiboContext() {

    }

    public FiboContext(List<Object> contextBeanList) {
        this.contextBeanList = contextBeanList;
    }

    private boolean hasMetaData(String key){
        return metaDataMap.containsKey(key);
    }

    private <T> void putMetaDataMap(String key, T t) {
        if (ObjectUtil.isNull(t)) {
            throw new NullParamException("context参数不能为空");
        }
        metaDataMap.put(key, t);
    }

    public <T> void setRequestData(T t){
        putMetaDataMap(REQUEST, t);
    }

    public <T> T getRequestData() {
        return (T) this.metaDataMap.get(REQUEST);
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

    public void setEngineId(Long engineId) {
        if (!hasMetaData(ENGINE_ID)){
            this.putMetaDataMap(ENGINE_ID, engineId);
        }
    }

    public Long getEngineId() {
        return (Long) metaDataMap.get(ENGINE_ID);
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

    public void generateRequestId() {
        metaDataMap.put(REQUEST_ID, IdGeneratorHolder.getInstance().generate());
    }

    public String getRequestId() {
        return (String) metaDataMap.get(REQUEST_ID);
    }

    public void addStep(NodeStep step){
        this.executeSteps.add(step);
    }

    public String getExecuteStepStr(boolean withTimeSpent){
        StringBuilder str = new StringBuilder();
        NodeStep nodeStep;
        for (Iterator<NodeStep> it = executeSteps.iterator(); it.hasNext();) {
            nodeStep = it.next();
            if (withTimeSpent){
                str.append(nodeStep.buildStringWithTime());
            }else{
                str.append(nodeStep.buildString());
            }
            if(it.hasNext()){
                str.append("==>");
            }
        }
        this.executeStepsStr = str.toString();
        return this.executeStepsStr;
    }

    public String getExecuteStepStr(){
        return getExecuteStepStr(false);
    }

    public void printStep(){
        if (ObjectUtil.isNull(this.executeStepsStr)){
            this.executeStepsStr = getExecuteStepStr(true);
        }
        if (FiboRuleConfigGetter.get().getPrintExecuteLog()){
            log.info("[{}]:引擎id[{}]\n{}",getRequestId(),this.getEngineId(), this.executeStepsStr);
        }
    }

}
