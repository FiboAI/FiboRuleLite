package com.fibo.rule.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.entity.EngineNode;
import com.fibo.rule.server.dao.model.param.*;
import com.fibo.rule.server.dao.model.vo.EngineNodeDetailVO;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;

import java.util.List;

public interface EngineNodeService extends IService<EngineNode> {

    List<FiboBeanDto> listNodesByType(NodesListParam param);

    EngineNode nodeEdit(NodeEditParam param);

    EngineNodeDetailVO nodeDetail(SimpleCommonPrimaryKeyParam param);

    void nodeDelete(SimpleCommonPrimaryKeyParam param);

    void moveLocation(NodeMoveLocationParam param);

    void moveLocations(NodeMoveLocationParams param);

    void connectLine(NodeConnectLineParam param);

    void reConnectLine(NodeConnectLineParam param);

    void breakLine(NodeConnectLineParam param);

    
}
