package com.fibo.rule.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.rule.server.dao.mapper.AppMapper;
import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.param.AppEditParam;
import com.fibo.rule.server.dao.model.param.AppListParam;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.service.AppService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private AppMapper appMapper;

    public PageInfo<App> appList(AppListParam param) {
        return PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> appMapper.selectListByPage(param));
    }

    @Override
    public App appEdit(AppEditParam param) {
        App app = new App();
        BeanUtils.copyProperties(param, app);
        if (null != param.getAppId()) {
            app.setId(param.getAppId());
            appMapper.updateById(app);
        } else {
            appMapper.insert(app);
        }
        app = appMapper.selectById(app.getId());
        return app;
    }

    @Override
    public void appDelete(SimpleCommonPrimaryKeyParam param) {
        App app = appMapper.selectById(param.getId());
        if (null != app) {
            app.setDelFlag(1);
            appMapper.updateById(app);
        }
    }


}
