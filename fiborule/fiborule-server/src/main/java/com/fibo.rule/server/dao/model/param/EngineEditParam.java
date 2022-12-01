package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("编辑engine列表参数对象")
public class EngineEditParam {

    @ApiModelProperty("engineId")
    private Integer engineId;

    @ApiModelProperty("appId")
    private Integer appId;
    
    @ApiModelProperty("名称")
    private String engineName;

    @ApiModelProperty("code")
    private String engineCode;

    @ApiModelProperty("描述")
    private String descriptions;

    @ApiModelProperty("发布状态:0创建，1发布，2取消发布")
    private Integer bootStatus;
    
    @ApiModelProperty("状态：1有效，0无效")
    private Integer status;
    
}
