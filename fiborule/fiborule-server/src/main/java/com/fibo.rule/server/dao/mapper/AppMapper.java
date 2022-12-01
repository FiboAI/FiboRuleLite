package com.fibo.rule.server.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.param.AppListParam;

import java.util.List;

public interface AppMapper extends BaseMapper<App> {


    List<App> selectListByPage(AppListParam param);
}
