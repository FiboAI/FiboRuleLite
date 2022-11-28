package com.fibo.rule.common.dto;

import lombok.Data;

import java.util.List;

/**
 * <p>引擎配置传输对象</p>
 *
 * @author JPX
 * @since 2022-11-18 14:14
 */
@Data
public class EngineDto {

    private Long id;
    private String engineName;
    private List<EngineNodeDto> nodeList;

}
