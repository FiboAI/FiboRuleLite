package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询engineNodes列表参数对象")
public class NodesListParam {

    @ApiModelProperty("appId")
    private Long appId;
    
    @ApiModelProperty("引擎id")
    private Long engineId;

    @ApiModelProperty("场景名称")
    private String scene;

    @ApiModelProperty("节点类型")
    private Integer nodeType;

}
