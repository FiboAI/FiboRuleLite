package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("连线engineNode参数对象")
public class NodeConnectLineParam {

    @ApiModelProperty("出入节点")
    private List<NodeConnectLineDetailParam> nodes;

    
}
