package com.fibo.rule.server.dao.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("节点详细信息")
public class EngineNodeDetailVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("engineId")
    private Long engineId;

    @ApiModelProperty("nodeName")
    private String nodeName;

    @ApiModelProperty("beanName")
    private String beanName;

    @ApiModelProperty("nodeCode")
    private String nodeCode;

    @ApiModelProperty("节点类型：开始节点、结束节点、普通节点、IF节点、switch节点、并行节点、聚合节点")
    private Integer nodeType;

    @ApiModelProperty("前置节点，多个节点以逗号分隔")
    private String preNodes;
    
    @ApiModelProperty("后置节点，多个节点以逗号分隔")
    private String nextNodes;

    @ApiModelProperty("节点配置信息（json类型，if/switch有lineValue，其他节点需设置阈值）")
    private String nodeConfig;

    @ApiModelProperty("nodeX")
    private String nodeX;

    @ApiModelProperty("nodeY")
    private String nodeY;

    @ApiModelProperty("nodeClazz")
    private String nodeClazz;

    @ApiModelProperty("clazzName")
    private String clazzName;

    @ApiModelProperty("nodeGroup")
    private String nodeGroup;

    //后续节点对应的分支值-[{"key":"Y",value:"node1"}]
    private String nextNodeValue;
    

}
