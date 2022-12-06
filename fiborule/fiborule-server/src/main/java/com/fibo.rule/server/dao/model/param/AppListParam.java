package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询app列表参数对象")
public class AppListParam {

    @ApiModelProperty("名称")
    private String appName;

    @ApiModelProperty("code")
    private String appCode;

    @ApiModelProperty("描述")
    private String descriptions;
    
    @ApiModelProperty("状态：1有效，0无效")
    private Integer status;

    @ApiModelProperty("pageNum")
    private Integer pageNum;

    @ApiModelProperty("pageSize")
    private Integer pageSize = 0;
    
}
