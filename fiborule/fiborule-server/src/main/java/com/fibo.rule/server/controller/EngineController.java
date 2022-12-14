package com.fibo.rule.server.controller;

import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.EngineDetailParam;
import com.fibo.rule.server.dao.model.param.EngineEditParam;
import com.fibo.rule.server.dao.model.param.EngineListParam;
import com.fibo.rule.server.dao.model.param.EngineReleaseParam;
import com.fibo.rule.server.dao.model.vo.EngineDetailVO;
import com.fibo.rule.server.model.ResponseEntityBuilder;
import com.fibo.rule.server.model.ResponseEntityDto;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.service.EngineService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
@RequestMapping("/engine")
public class EngineController {

    @Resource
    private EngineService engineService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntityDto<PageInfo<Engine>> engineList(@RequestBody EngineListParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineService.engineList(param));
    }

    @RequestMapping(value = "/getSceneList", method = RequestMethod.POST)
    public ResponseEntityDto<List<String>> getSceneList(@RequestBody SimpleCommonPrimaryKeyParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineService.getSceneList(param));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntityDto<Engine> engineEdit(@RequestBody EngineEditParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineService.engineEdit(param));
    }

    /**
     * 获取引擎详情
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/getEngineDetail", method = RequestMethod.POST)
    public ResponseEntityDto<EngineDetailVO> getEngineDetail(@RequestBody EngineDetailParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineService.getEngineDetail(param));
    }

    /**
     * 引擎发布、取消发布
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/engineRelease", method = RequestMethod.POST)
    public ResponseEntityDto engineRelease(@RequestBody EngineReleaseParam param) {
        engineService.engineRelease(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntityDto engineDelete(@RequestBody SimpleCommonPrimaryKeyParam param) {
        engineService.engineDelete(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }


}
