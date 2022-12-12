package com.fibo.rule.server.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.EngineListParam;

import java.util.List;

public interface EngineMapper extends BaseMapper<Engine> {


    List<Engine> selectListByPage(EngineListParam param);
}
