package com.fibo.rule.core.engine.element;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.node.FiboNode;
import lombok.Data;

/**
 * <p></p>
 *
 * @author JPX
 * @since 2022-11-18 11:04
 */
@Data
public class FiboEngineNode implements FiboRunnable {

    private Long nodeId;
    private String nodeName;
    private String nodeCode;
    private FiboNode fiboNode;

    @Override
    public void runner(Integer contextIndex) {
        if(ObjectUtil.isNull(fiboNode)) {
            // TODO: 2022/11/23 异常处理
            throw new RuntimeException();
        }
        try {
            fiboNode.setContextIndex(contextIndex);
            fiboNode.runner(contextIndex);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fiboNode.removeContextIndex();
        }
    }
}
