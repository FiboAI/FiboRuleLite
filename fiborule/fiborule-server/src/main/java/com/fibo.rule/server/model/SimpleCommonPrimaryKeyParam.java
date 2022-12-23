package com.fibo.rule.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "公共的主键参数对象")
public class SimpleCommonPrimaryKeyParam implements Serializable {

    @ApiModelProperty(value = "主键id")
    @NotNull(message = "id必传")
    private Long id;


}
