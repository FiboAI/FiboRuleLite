package com.fibo.rule.core.property;

import cn.hutool.core.util.ObjectUtil;

import java.util.List;

/**
 * <p>配置实体类</p>
 *
 * @author JPX
 * @since 2022-11-28 16:07
 */
public class FiboRuleConfig {

    /**appId*/
    private Integer app;
    /**服务端地址host:port*/
    private String server;
    /**是否打印fiborule banner*/
    private Boolean printBanner;
    /**context数量*/
    private Integer contextSize;
    /**场景列表*/
    private List<FiboSceneConfig> sceneList;
    /**并行节点线程池最大线程数*/
    private Integer allMaxWorkers;
    /**并行节点线程池最大队列数量*/
    private Integer allQueueLimit;
    /**是否打印执行过程中日志*/
    private Boolean printExecuteLog;
    //是否打印监控log
    private Boolean enableLog;
    //监控存储信息最大队列数量
    private Integer queueLimit;
    //延迟多少秒打印
    private Long delay;
    //每隔多少秒打印
    private Long period;

    public Integer getApp() {
        return app;
    }

    public void setApp(Integer app) {
        this.app = app;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Boolean getPrintBanner() {
        if(ObjectUtil.isNull(printBanner)) {
            return Boolean.TRUE;
        }
        return printBanner;
    }

    public void setPrintBanner(Boolean printBanner) {
        this.printBanner = printBanner;
    }

    public Integer getContextSize() {
        if(ObjectUtil.isNull(contextSize)) {
            return 1024;
        }
        return contextSize;
    }

    public void setContextSize(Integer contextSize) {
        this.contextSize = contextSize;
    }

    public List<FiboSceneConfig> getSceneList() {
        return sceneList;
    }

    public void setSceneList(List<FiboSceneConfig> sceneList) {
        this.sceneList = sceneList;
    }

    public Integer getAllMaxWorkers() {
        if(ObjectUtil.isNull(allMaxWorkers)) {
            return 16;
        }
        return allMaxWorkers;
    }

    public void setAllMaxWorkers(Integer allMaxWorkers) {
        this.allMaxWorkers = allMaxWorkers;
    }

    public Integer getAllQueueLimit() {
        if(ObjectUtil.isNull(allQueueLimit)) {
            return 512;
        }
        return allQueueLimit;
    }

    public void setAllQueueLimit(Integer allQueueLimit) {
        this.allQueueLimit = allQueueLimit;
    }

    public Boolean getPrintExecuteLog() {
        if (ObjectUtil.isNull(printExecuteLog)){
            return Boolean.TRUE;
        }else{
            return printExecuteLog;
        }
    }

    public void setPrintExecuteLog(Boolean printExecuteLog) {
        this.printExecuteLog = printExecuteLog;
    }

    public Integer getQueueLimit() {
        if (ObjectUtil.isNull(queueLimit)) {
            return 200;
        } else {
            return queueLimit;
        }
    }

    public void setQueueLimit(Integer queueLimit) {
        this.queueLimit = queueLimit;
    }

    public Long getDelay() {
        if (ObjectUtil.isNull(delay)) {
            return 300000L;
        } else {
            return delay;
        }
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getPeriod() {
        if (ObjectUtil.isNull(period)) {
            return 300000L;
        } else {
            return period;
        }
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Boolean getEnableLog() {
        if (ObjectUtil.isNull(enableLog)) {
            return Boolean.FALSE;
        } else {
            return enableLog;
        }
    }

    public void setEnableLog(Boolean enableLog) {
        this.enableLog = enableLog;
    }
}
