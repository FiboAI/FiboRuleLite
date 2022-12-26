package com.fibo.rule.common.dto;

import com.fibo.rule.common.enums.NodeTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>节点定义信息传输对象</p>
 *
 * @author JPX
 * @since 2022-12-01 13:36
 */
@Data
public class FiboBeanDto {

    /**节点类型*/
    private NodeTypeEnum type;
    /**节点名称*/
    private String name;
    /**节点描述*/
    private String desc;
    /**节点类名，类名场景下不重复*/
    private String clazzName;
    /**节点类全路径*/
    private String nodeClazz;
    /**节点需要配置的属性*/
    private List<FiboFieldDto> fiboFieldDtoList;
    /**if和switch的分支  例如：key-value》Y-是*/
    private Map<String, String> branchMap;

}
