package com.fibo.rule.common.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>节点定义信息传输对象</p>
 *
 * @author JPX
 * @since 2022-12-01 13:36
 */
@Data
public class FiboBeanDto {

    /**节点名称*/
    private String name;
    /**节点描述*/
    private String desc;
    /**节点类名*/
    private String clazzName;
    /**节点类全路径*/
    private String nodeClazz;
    /**节点需要配置的属性*/
    private List<FiboFieldDto> fiboFieldDtoList;
    
    //是否缺少节点类型
    private Integer nodeType;

}
