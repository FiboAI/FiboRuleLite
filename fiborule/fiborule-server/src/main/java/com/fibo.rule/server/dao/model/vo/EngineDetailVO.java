package com.fibo.rule.server.dao.model.vo;

import com.fibo.rule.server.dao.model.entity.Engine;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("引擎详情参数对象")
public class EngineDetailVO {

    @ApiModelProperty("appId")
    private Long appId;

    @ApiModelProperty("engineId")
    private Engine engine;

    @ApiModelProperty("节点详细信息")
    private List<EngineNodeDetailVO> nodesDetail;


}
