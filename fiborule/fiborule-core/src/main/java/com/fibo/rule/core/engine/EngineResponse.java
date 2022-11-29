package com.fibo.rule.core.engine;

import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.step.NodeStep;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 *<p>执行结果封装</p>
 *
 *@author JPX
 *@since 2022/11/22 14:58
 */
@Data
public class EngineResponse implements Serializable {

    private static final long serialVersionUID = -2792556188993845048L;

    private boolean success;

    private String code;

    private String message;

    private Exception cause;

    private FiboContext fiboContext;

    public EngineResponse() {
    }

    public static EngineResponse newMainResponse(FiboContext fiboContext){
        return newResponse(fiboContext, fiboContext.getException());
    }

    private static EngineResponse newResponse(FiboContext fiboContext, Exception e){
        EngineResponse response = new EngineResponse();
        if (fiboContext != null && e != null) {
            response.setSuccess(false);
            response.setCause(e);
            response.setMessage(response.getCause().getMessage());
        } else {
            response.setSuccess(true);
        }
        response.setFiboContext(fiboContext);
        return response;
    }

    public <T> T getFirstContextBean(){
        return this.getFiboContext().getFirstContextBean();
    }

    public <T> T getContextBean(Class<T> contextBeanClazz){
        return this.getFiboContext().getContextBean(contextBeanClazz);
    }

    public Map<Long, NodeStep> getExecuteSteps(){
        Map<Long, NodeStep> map = new HashMap<>();
        this.getFiboContext().getExecuteSteps().forEach(cmpStep -> map.put(cmpStep.getNodeId(), cmpStep));
        return map;
    }

    public Queue<NodeStep> getExecuteStepQueue(){
        return this.getFiboContext().getExecuteSteps();
    }

    public String getExecuteStepStr(){
        return getExecuteStepStrWithoutTime();
    }

    public String getExecuteStepStrWithTime(){
        return this.getFiboContext().getExecuteStepStr(true);
    }

    public String getExecuteStepStrWithoutTime(){
        return this.getFiboContext().getExecuteStepStr(false);
    }

    public String getRequestId(){
        return this.getFiboContext().getRequestId();
    }
}
