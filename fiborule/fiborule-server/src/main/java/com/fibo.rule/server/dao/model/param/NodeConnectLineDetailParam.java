package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("连线engineNode详细参数对象")
public class NodeConnectLineDetailParam {

    @ApiModelProperty("id")
    private Long id;
    
    @ApiModelProperty("前置节点，多个节点以逗号分隔")
    private String preNodes;

    @ApiModelProperty("后置节点，多个节点以逗号分隔")
    private String nextNodes;
    
}
