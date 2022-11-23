package com.fibo.rule.core.engine.condition;

import cn.hutool.core.util.ObjectUtil;

/**
 * <p>普通组件执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
public class FiboNormalCondition extends FiboCondition {

    @Override
    public void runner(Integer contextIndex) throws Exception {
        getFiboRunnable().runner(contextIndex);
        if(ObjectUtil.isNotNull(this.getNextRunnable())) {
            this.getNextRunnable().runner(contextIndex);
        }
    }
}
