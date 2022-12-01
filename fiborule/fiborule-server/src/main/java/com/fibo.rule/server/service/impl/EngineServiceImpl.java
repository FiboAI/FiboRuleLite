package com.fibo.rule.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.rule.server.dao.mapper.EngineMapper;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.EngineEditParam;
import com.fibo.rule.server.dao.model.param.EngineListParam;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.service.EngineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class EngineServiceImpl extends ServiceImpl<EngineMapper, Engine> implements EngineService {

    @Resource
    private EngineMapper engineMapper;

    @Override
    public PageInfo<Engine> engineList(EngineListParam param) {
        return PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> engineMapper.selectListByPage(param));
    }

    @Override
    public Engine engineEdit(EngineEditParam param) {
        Engine engine = new Engine();
        BeanUtils.copyProperties(param, engine);
        if (null != param.getEngineId()) {
            engine.setId(param.getEngineId());
            engineMapper.updateById(engine);
        } else {
            engineMapper.insert(engine);
        }
        engine = engineMapper.selectById(engine.getId());
        return engine;
    }

    @Override
    public void engineDelete(SimpleCommonPrimaryKeyParam param) {
        Engine engine = engineMapper.selectById(param.getId());
        if (null != engine) {
            engine.setDelFlag(1);
            engineMapper.updateById(engine);
        }
    }


}
