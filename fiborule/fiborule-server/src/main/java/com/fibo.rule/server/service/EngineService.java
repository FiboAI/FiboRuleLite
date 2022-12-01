package com.fibo.rule.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.AppEditParam;
import com.fibo.rule.server.dao.model.param.AppListParam;
import com.fibo.rule.server.dao.model.param.EngineEditParam;
import com.fibo.rule.server.dao.model.param.EngineListParam;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.github.pagehelper.PageInfo;

public interface EngineService extends IService<Engine> {

    PageInfo<Engine> engineList(EngineListParam param);

    Engine engineEdit(EngineEditParam param);

    void engineDelete(SimpleCommonPrimaryKeyParam param);
}
