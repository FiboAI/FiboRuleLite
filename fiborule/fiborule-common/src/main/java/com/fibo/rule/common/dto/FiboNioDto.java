package com.fibo.rule.common.dto;

import com.fibo.rule.common.enums.NioOperationTypeEnum;
import com.fibo.rule.common.enums.NioTypeEnum;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>nio数据传输</p>
 *
 * @author JPX
 * @since 2022-12-01 13:52
 */
@Data
public class FiboNioDto {

    /**请求唯一标识：发出request，返回的response的id需相同*/
    private String id;
    /**Nio请求类型：request/response*/
    private NioTypeEnum type;
    /**Nio操作类型：初始化/更新信息等*/
    private NioOperationTypeEnum operationType;
    /**appId*/
    private Long appId;
    /**app地址*/
    private String address;
    /**场景对应的节点信息*/
    private Map<String, List<FiboBeanDto>> sceneBeansMap;
    /**引擎定义传输*/
    private List<EngineDto> engineDtoList;
    /**取消发布的引擎id*/
    private Long releaseEngineId;

}
