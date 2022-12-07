package com.fibo.rule.server.controller;

import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.param.AppEditParam;
import com.fibo.rule.server.dao.model.param.AppListParam;
import com.fibo.rule.server.model.ResponseEntityBuilder;
import com.fibo.rule.server.model.ResponseEntityDto;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.service.AppService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


//@CrossOrigin
@RestController
@RequestMapping("/app")
public class AppController {
    
    @Resource
    private AppService appService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseEntityDto<List<App>> appList(@RequestBody AppListParam param) {
        return ResponseEntityBuilder.buildNormalResponse(appService.appList(param));
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ResponseEntityDto<App> appEdit(@RequestBody AppEditParam param) {
        return ResponseEntityBuilder.buildNormalResponse(appService.appEdit(param));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntityDto appDelete(@RequestBody SimpleCommonPrimaryKeyParam param) {
        appService.appDelete(param);
        return ResponseEntityBuilder.buildNormalResponse();
    }

    
}
