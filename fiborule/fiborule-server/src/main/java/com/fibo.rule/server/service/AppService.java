package com.fibo.rule.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.param.AppEditParam;
import com.fibo.rule.server.dao.model.param.AppListParam;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.github.pagehelper.PageInfo;

public interface AppService extends IService<App> {

    PageInfo<App> appList(AppListParam param);

    App appEdit(AppEditParam param);

    void appDelete(SimpleCommonPrimaryKeyParam param);
}
