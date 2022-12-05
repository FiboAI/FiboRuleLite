package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("编辑app列表参数对象")
public class AppEditParam {

    @ApiModelProperty("id")
    private Long appId;

    @ApiModelProperty("名称")
    private String appName;

    @ApiModelProperty("code")
    private String appCode;

    @ApiModelProperty("描述")
    private String descriptions;
    
    @ApiModelProperty("状态：1有效，0无效")
    private Integer status;
    
}
