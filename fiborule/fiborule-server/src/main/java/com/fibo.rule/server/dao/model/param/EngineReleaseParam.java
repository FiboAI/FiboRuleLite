package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("engine发布参数对象")
public class EngineReleaseParam {

    @ApiModelProperty("appId")
    private Long appId;

    @ApiModelProperty("engineId")
    private Long engineId;

    @ApiModelProperty("发布状态:0创建，1发布，2取消发布")
    private Integer bootStatus;

}
