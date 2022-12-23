package com.fibo.rule.server.controller;

import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.server.dao.model.entity.EngineNode;
import com.fibo.rule.server.dao.model.param.*;
import com.fibo.rule.server.dao.model.vo.EngineNodeDetailVO;
import com.fibo.rule.server.model.ResponseEntityBuilder;
import com.fibo.rule.server.model.ResponseEntityDto;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.service.EngineNodeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/engineNode")
public class EngineNodeController {

    @Resource
    private EngineNodeService engineNodeService;

    
    /**
     * 获取指定类型的节点实现信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/listNodesByType", method = RequestMethod.POST)
    public ResponseEntityDto<List<FiboBeanDto>> listNodesByType(@RequestBody NodesListParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineNodeService.listNodesByType(param));
    }

    /**
     * 编辑节点信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/nodeEdit", method = RequestMethod.POST)
    public ResponseEntityDto<EngineNode> nodeEdit(@RequestBody NodeEditParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineNodeService.nodeEdit(param));
    }

    /**
     * 查看节点信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/nodeDetail", method = RequestMethod.POST)
    public ResponseEntityDto<EngineNodeDetailVO> nodeDetail(@RequestBody SimpleCommonPrimaryKeyParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineNodeService.nodeDetail(param));
    }

    /**
     * 删除节点信息
     * @param param
     * @return
     */
    @RequestMapping(value = "/nodeDelete", method = RequestMethod.POST)
    public ResponseEntityDto nodeDelete(@RequestBody SimpleCommonPrimaryKeyParam param) {
        engineNodeService.nodeDelete(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    /**
     * 移动位置
     * @param param
     * @return
     */
    @RequestMapping(value = "/moveLocation", method = RequestMethod.POST)
    public ResponseEntityDto moveLocation(@RequestBody NodeMoveLocationParam param) {
        engineNodeService.moveLocation(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    /**
     * 批量移动位置
     * @param param
     * @return
     */
    @RequestMapping(value = "/moveLocations", method = RequestMethod.POST)
    public ResponseEntityDto moveLocations(@RequestBody NodeMoveLocationParams param) {
        engineNodeService.moveLocations(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    /**
     * 连线
     * @param param
     * @return
     */
    @RequestMapping(value = "/connectLine", method = RequestMethod.POST)
    public ResponseEntityDto connectLine(@RequestBody NodeConnectLineParam param) {
        engineNodeService.connectLine(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    /**
     * 重新连线
     * @param param
     * @return
     */
    @RequestMapping(value = "/reConnectLine", method = RequestMethod.POST)
    public ResponseEntityDto reConnectLine(@RequestBody NodeConnectLineParam param) {
        engineNodeService.reConnectLine(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    /**
     * 断线
     * @param param
     * @return
     */
    @RequestMapping(value = "/breakLine", method = RequestMethod.POST)
    public ResponseEntityDto breakLine(@RequestBody NodeConnectLineParam param) {
        engineNodeService.breakLine(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }


}
