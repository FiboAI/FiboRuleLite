package com.fibo.rule.core.runner;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.DefaultParamBean;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.EngineManager;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.engine.element.FiboEngine;
import com.fibo.rule.core.engine.id.IdGeneratorHolder;
import com.fibo.rule.core.exception.EngineNotFoundException;
import com.fibo.rule.core.exception.NoAvailableContextException;
import com.fibo.rule.core.property.FiboRuleConfig;
import com.fibo.rule.core.property.FiboRuleConfigGetter;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>执行类</p>
 *
 * @author JPX
 * @since 2022-11-18 13:13
 */
@Slf4j
public class FiboApplication {

    private FiboRuleConfig fiboRuleConfig;

    public FiboApplication() {
        Contextmanager.init();
        IdGeneratorHolder.init();
    }

    public FiboApplication(FiboRuleConfig config) {
        this.fiboRuleConfig = config;
        FiboRuleConfigGetter.setFiboRuleConfig(config);
        FiboApplicationHolder.setHolder(this);
        Contextmanager.init();
        IdGeneratorHolder.init();
//        MonitorManager.loadInstance(config);
    }

    public EngineResponse runner(Long engineId, Object param) {
        return this.runner(engineId, param, DefaultParamBean.class);
    }

    public EngineResponse runner(Long engineId, Object param, Class<?>... paramBeanClazzArray) {
        FiboContext fiboContext = this.execute(engineId, param, paramBeanClazzArray);
        return EngineResponse.newMainResponse(fiboContext);
    }

    private FiboContext execute(Long engineId, Object param, Class<?>... paramBeanClazzArray) {
        //分配context
        Integer contextIndex = Contextmanager.offerContextByClass(ListUtil.toList(paramBeanClazzArray));
        if(contextIndex == -1) {
            throw new NoAvailableContextException("没有可用的context");
        }
        //获取context
        FiboContext context = Contextmanager.getContext(contextIndex);
        if(ObjectUtil.isNull(context)) {
            throw new NoAvailableContextException(StrUtil.format("context[{}]不存在", contextIndex));
        }
        //生成requestId
        context.generateRequestId();
        if (BooleanUtil.isTrue(fiboRuleConfig.getPrintExecuteLog())) {
            log.info("requestId已生成：[{}]", context.getRequestId());
        }
        //设置参数
        if(ObjectUtil.isNotNull(param)) {
            context.setRequestData(param);
        }
        FiboEngine engine = null;
        try {
            //获取引擎
            engine = EngineManager.getEngine(engineId);
            if(ObjectUtil.isNull(engine)) {
                String errorMsg = StrUtil.format("[{}]:找不到id为[{}]的引擎", context.getRequestId(), engineId);
                throw new EngineNotFoundException(errorMsg);
            }
            //执行引擎
            engine.runner(contextIndex);
        } catch (Exception e) {
            if (ObjectUtil.isNotNull(engine)) {
                String errMsg = StrUtil.format("[{}]:引擎[{}]在context[{}]上执行失败", context.getRequestId(), engine.getEngineId(), contextIndex);
                if (BooleanUtil.isTrue(fiboRuleConfig.getPrintExecuteLog())){
                    log.error(errMsg, e);
                }else{
                    log.error(errMsg);
                }
            }else{
                if (BooleanUtil.isTrue(fiboRuleConfig.getPrintExecuteLog())){
                    log.error(e.getMessage(), e);
                }else{
                    log.error(e.getMessage());
                }
            }
            context.setException(e);
        } finally {
            context.printStep();
            Contextmanager.releaseContext(contextIndex);
        }
        return context;
    }

    public FiboRuleConfig getFiboRuleConfig() {
        return fiboRuleConfig;
    }

    public void setFiboRuleConfig(FiboRuleConfig fiboRuleConfig) {
        this.fiboRuleConfig = fiboRuleConfig;
        FiboRuleConfigGetter.setFiboRuleConfig(fiboRuleConfig);
//        MonitorManager.loadInstance(fiboRuleConfig);
    }
}
