package com.fibo.rule.common.dto;

import lombok.Data;

/**
 * <p>节点传输对象</p>
 *
 * @author JPX
 * @since 2022-11-18 14:14
 */
@Data
public class EngineNodeDto {

    private Long id;
    private String nodeName;
    private String nodeCode;
    private Long engineId;
    private Integer nodeType;
    private String preNodes;
    private String nextNodes;
    private String nodeConfig;
    private String nodeClazz;

}
