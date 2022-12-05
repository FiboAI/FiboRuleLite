package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("重新连线engineNode参数对象")
public class NodeReConnectLineParam {

    @ApiModelProperty("出节点")
    private NodeConnectLineDetailParam outNode;

    @ApiModelProperty("旧入节点")
    private NodeConnectLineDetailParam oldInNode;

    @ApiModelProperty("新入节点")
    private NodeConnectLineDetailParam newInNode;
    
}
