package com.fibo.rule.core.engine.element;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-18 10:47
 */
@Data
public class FiboEngine implements FiboRunnable {

    private Long engineId;

    private FiboRunnable fiboRunnable;

    @Override
    public void runner(Integer contextIndex) throws Exception {
        if(ObjectUtil.isNull(fiboRunnable)) {
            // TODO: 2022/11/18 抛出异常
            return;
        }
        fiboRunnable.runner(contextIndex);
    }
}
