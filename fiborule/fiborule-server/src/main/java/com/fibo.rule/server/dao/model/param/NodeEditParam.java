package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("编辑engineNode参数对象")
public class NodeEditParam {

    @ApiModelProperty("nodeId")
    private Long nodeId;
    
    @ApiModelProperty("engineId")
    private Long engineId;
    
    @ApiModelProperty("名称")
    private String nodeName;

    @ApiModelProperty("节点类型：开始节点、结束节点、普通节点、IF节点、switch节点、并行节点、聚合节点")
    private Integer nodeType;

    @ApiModelProperty("前置节点，多个节点以逗号分隔")
    private String preNodes;

    @ApiModelProperty("后置节点，多个节点以逗号分隔")
    private String nextNodes;

    @ApiModelProperty("节点配置信息（json类型，if/switch有lineValue，并行网关有isAny(先不做)，其他节点需设置阈值）")
    private String nodeConfig;

    @ApiModelProperty("nodeX")
    private String nodeX;
    
    @ApiModelProperty("nodeY")
    private String nodeY;

    @ApiModelProperty("类全名称，包名+类名")
    private String nodeClazz;

    @ApiModelProperty("类名称")
    private String clazzName;

    @ApiModelProperty("node组")
    private String nodeGroup;

    @ApiModelProperty("状态：1有效，0无效")
    private Integer status;
    
}
