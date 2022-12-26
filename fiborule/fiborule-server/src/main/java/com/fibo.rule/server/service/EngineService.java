package com.fibo.rule.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.*;
import com.fibo.rule.server.dao.model.vo.EngineDetailVO;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface EngineService extends IService<Engine> {

    PageInfo<Engine> engineList(EngineListParam param);

    List<String> getSceneList(SimpleCommonPrimaryKeyParam param);

    Engine engineEdit(EngineEditParam param);

    EngineDetailVO getEngineDetail(EngineDetailParam param);

    void engineRelease(EngineReleaseParam param);

    List<EngineDto> getEngineDtoList(Long appId, Long engineId);
    
    void engineDelete(SimpleCommonPrimaryKeyParam param);


    
}
