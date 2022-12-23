package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询engine详情参数对象")
public class EngineDetailParam {

    @ApiModelProperty("appId")
    private Long appId;

    @ApiModelProperty("engineId")
    private Long engineId;

}
