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
    private Long app;
    /**服务端地址host:port*/
    private String server;
    /**是否打印fiborule logo，默认true*/
    private Boolean printBanner;
    /**context数量，默认1024*/
    private Integer contextSize;
    /**场景列表，默认场景default，扫描全包*/
    private List<FiboSceneConfig> sceneList;
    /**并行节点线程池最大线程数，默认16*/
    private Integer allMaxWorkers;
    /**并行节点线程池最大队列数量，默认512*/
    private Integer allQueueLimit;
    /**是否打印执行过程中日志，默认true*/
    private Boolean printExecuteLog;
    /**是否打印监控log，默认false*/
    private Boolean enableLog;
    /**监控存储信息最大队列数量，默认200*/
    private Integer queueLimit;
    /**监控信息延迟多少秒打印，默认5分钟*/
    private Long delay;
    /**监控信息每隔多少秒打印，默认5分钟*/
    private Long period;
    /**默认16M，大小大于此值可能会损坏数据*/
    private Integer maxFrameLength;
    /**初始化重试次数，默认3次*/
    private Integer initRetryTimes;
    /**初始化重试延迟时间，默认2s*/
    private Integer initRetrySleepMs;

    public Long getApp() {
        return app;
    }

    public void setApp(Long app) {
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

    public Integer getMaxFrameLength() {
        if(ObjectUtil.isNull(maxFrameLength)) {
            return 16 * 1024 * 1024;
        }
        return maxFrameLength;
    }

    public void setMaxFrameLength(Integer maxFrameLength) {
        this.maxFrameLength = maxFrameLength;
    }

    public Integer getInitRetryTimes() {
        if(ObjectUtil.isNull(initRetryTimes)) {
            return 3;
        }
        return initRetryTimes;
    }

    public void setInitRetryTimes(Integer initRetryTimes) {
        this.initRetryTimes = initRetryTimes;
    }

    public Integer getInitRetrySleepMs() {
        if(ObjectUtil.isNull(initRetrySleepMs)) {
            return 2000;
        }
        return initRetrySleepMs;
    }

    public void setInitRetrySleepMs(Integer initRetrySleepMs) {
        this.initRetrySleepMs = initRetrySleepMs;
    }
}
