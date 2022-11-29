package com.fibo.rule.springboot;

import com.fibo.rule.core.property.FiboSceneConfig;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

/**
 *<p>参数配置</p>
 *
 *@author JPX
 *@since 2022/11/28 16:45
 */
@Data
@ConfigurationProperties(prefix = "fiborule", ignoreUnknownFields = true)
public class FiboRuleProperty {

    /**
     * appId
     */
    private Integer app;
    /**
     * 服务端地址host:port
     */
    private String server;
    /**
     * 是否打印fiborule banner
     */
    private boolean printBanner;
    /**
     * context数量
     */
    private int contextSize;
    /**
     * 场景列表
     */
    private List<FiboSceneConfig> sceneList;

}
