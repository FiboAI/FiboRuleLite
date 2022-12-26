package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("移动engineNode参数对象")
public class NodeMoveLocationParam {

    @ApiModelProperty("nodeId")
    private Long nodeId;

    @ApiModelProperty("nodeX")
    private String nodeX;
    
    @ApiModelProperty("nodeY")
    private String nodeY;
    
    
}
