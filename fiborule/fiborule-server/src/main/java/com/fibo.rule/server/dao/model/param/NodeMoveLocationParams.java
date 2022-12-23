package com.fibo.rule.server.dao.model.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("批量移动engineNode参数对象")
public class NodeMoveLocationParams {

   private List<NodeMoveLocationParam> nodeMoveLocationParams;
    
    
}
