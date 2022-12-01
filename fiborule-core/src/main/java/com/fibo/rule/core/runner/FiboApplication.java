package com.fibo.rule.core.runner;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.DefaultParamBean;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.EngineManager;
import com.fibo.rule.core.engine.EngineResponse;
import com.fibo.rule.core.engine.element.FiboEngine;
import lombok.extern.slf4j.Slf4j;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-18 13:13
 */
@Slf4j
public class FiboApplication {

    public FiboApplication() {
        Contextmanager.init();
    }

    public EngineResponse runner(Long engineId, Object param) {
        return this.runner(engineId, param, DefaultParamBean.class);
    }

    public EngineResponse runner(Long engineId, Object param, Class<?>... paramBeanClazzArray) {
        FiboContext fiboContext = this.execute(engineId, param, paramBeanClazzArray);
        return EngineResponse.newMainResponse(fiboContext);
    }

    private FiboContext execute(Long engineId, Object param, Class<?>... paramBeanClazzArray) {
        Integer contextIndex = Contextmanager.offerContextByClass(ListUtil.toList(paramBeanClazzArray));
        if(contextIndex == -1) {
            // TODO: 2022/11/18 抛出异常
            throw new RuntimeException();
        }
        FiboContext context = Contextmanager.getContext(contextIndex);
        if(ObjectUtil.isNull(context)) {
            // TODO: 2022/11/18 抛出异常
            throw new RuntimeException();
        }

        // TODO: 2022/11/18 生成context唯一id

        if(ObjectUtil.isNotNull(param)) {
            context.setRequestData(param);
        }

        try {
            FiboEngine engine = EngineManager.getEngine(engineId);
            if(ObjectUtil.isNull(engine)) {
                // TODO: 2022/11/18 抛出异常
                throw new RuntimeException();
            }
            engine.runner(contextIndex);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // TODO: 2022/11/18 打印执行日志
            Contextmanager.releaseContext(contextIndex);
        }
        return context;
    }

}
