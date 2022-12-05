package com.fibo.rule.common.dto;

import lombok.Data;

/**
 * <p>节点配置属性</p>
 *
 * @author JPX
 * @since 2022-12-01 13:40
 */
@Data
public class FiboFieldDto {

    /**展示*/
    private String name;
    /**存储*/
    private String fieldName;
    /**描述*/
    private String desc;
    /**类型-枚举*/
    private Integer type;

}
