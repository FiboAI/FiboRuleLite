package com.fibo.rule.core.property;

import lombok.Data;

/**
 * <p>场景配置信息</p>
 *
 * @author JPX
 * @since 2022-11-28 16:57
 */
@Data
public class FiboSceneConfig {

    /**
     * 场景名称
     */
    private String name;
    /**
     * 场景路径
     */
    private String url;

}
