package com.fibo.rule.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.rule.common.enums.DelFlagEnum;
import com.fibo.rule.server.dao.mapper.AppMapper;
import com.fibo.rule.server.dao.mapper.EngineMapper;
import com.fibo.rule.server.dao.model.entity.App;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.param.AppEditParam;
import com.fibo.rule.server.dao.model.param.AppListParam;
import com.fibo.rule.server.enums.ErrorCodeEnum;
import com.fibo.rule.server.exception.ApiException;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.nio.NioClientManager;
import com.fibo.rule.server.service.AppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Slf4j
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Resource
    private AppMapper appMapper;

    @Resource
    private EngineMapper engineMapper;

    @Resource
    private NioClientManager nioClientManager;

    public List<App> appList(AppListParam param) {
        List<App> info = appMapper.selectList(param);
        for (App app : info) {
            Set<String> clients = nioClientManager.getRegisterClients(app.getId());
            if (null != clients) {
                app.setClients(new ArrayList<>(clients));
            } else {
                app.setClients(new ArrayList<>());
            }
        }
        return info;
    }

    @Override
    public App appEdit(AppEditParam param) {
        this.verifyParam(param);
        App app = new App();
        BeanUtils.copyProperties(param, app);
        if (null != param.getId()) {
            app.setAppCode(null);
            appMapper.updateById(app);
        } else {
            appMapper.insert(app);
        }
        app = appMapper.selectById(app.getId());
        return app;
    }

    /**
     * 规则：appName唯一可编辑
     * appCode唯一不可编辑
     *
     * @param param
     */
    private void verifyParam(AppEditParam param) {
        if (null == param.getId()) {
            int count = appMapper.selectCount(new QueryWrapper<App>().lambda()
                    .eq(App::getDelFlag, DelFlagEnum.DEL_NO.status)
                    .eq(App::getAppCode, param.getAppCode()));
            if (count > 0) {
                throw new ApiException(ErrorCodeEnum.APP_CODE_REDO_EXCEPTION);
            }
            count = appMapper.selectCount(new QueryWrapper<App>().lambda()
                    .eq(App::getDelFlag, DelFlagEnum.DEL_NO.status)
                    .eq(App::getAppName, param.getAppName()));
            if (count > 0) {
                throw new ApiException(ErrorCodeEnum.APP_NAME_REDO_EXCEPTION);
            }
        } else {
            int count = appMapper.selectCount(new QueryWrapper<App>().lambda()
                    .eq(App::getAppName, param.getAppName())
                    .eq(App::getDelFlag, DelFlagEnum.DEL_NO.status)
                    .ne(App::getId, param.getId()));
            if (count > 0) {
                throw new ApiException(ErrorCodeEnum.APP_NAME_REDO_EXCEPTION);
            }
        }

    }

    /**
     * 有未删除的引擎则不能删除
     *
     * @param param
     */
    @Override
    public void appDelete(SimpleCommonPrimaryKeyParam param) {
        int count = engineMapper.selectCount(new QueryWrapper<Engine>().lambda()
                .eq(Engine::getAppId, param.getId())
                .eq(Engine::getDelFlag, DelFlagEnum.DEL_NO.status));
        if (count > 0) {
            throw new ApiException(ErrorCodeEnum.APP_NOT_ALLOWED_DELETE_EXCEPTION);
        }
        App app = appMapper.selectById(param.getId());
        if (null != app) {
            app.setDelFlag(1);
            appMapper.updateById(app);
        }
    }


}
