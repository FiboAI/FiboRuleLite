package com.fibo.rule.server.controller;

import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.AppEditParam;
import com.fibo.rule.server.dao.model.param.AppListParam;
import com.fibo.rule.server.dao.model.param.EngineEditParam;
import com.fibo.rule.server.dao.model.param.EngineListParam;
import com.fibo.rule.server.model.ResponseEntityBuilder;
import com.fibo.rule.server.model.ResponseEntityDto;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.service.AppService;
import com.fibo.rule.server.service.EngineService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@CrossOrigin
@RestController
@RequestMapping("/engine")
public class EngineController {
    
    @Resource
    private EngineService engineService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntityDto<PageInfo<Engine>> engineList(@RequestBody EngineListParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineService.engineList(param));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntityDto<Engine> engineEdit(@RequestBody EngineEditParam param) {
        return ResponseEntityBuilder.buildNormalResponse(engineService.engineEdit(param));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntityDto engineDelete(@RequestBody SimpleCommonPrimaryKeyParam param) {
        engineService.engineDelete(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    
}
