package com.fibo.rule.core.engine.element;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.exception.EngineSystemException;
import com.fibo.rule.core.node.FiboNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;

/**
 * <p>节点执行类</p>
 *
 * @author JPX
 * @since 2022-11-18 11:04
 */
@Slf4j
@Data
public class FiboEngineNode implements FiboRunnable {

    private Long nodeId;
    private String nodeName;
    private String beanName;
    private String nodeCode;
    private String nodeClazz;
    private NodeTypeEnum type;
    private FiboNode fiboNode;

    public FiboEngineNode(FiboNode fiboNode) {
        this.nodeId = fiboNode.getNodeId();
        this.nodeName = fiboNode.getNodeName();
        this.beanName = fiboNode.getBeanName();
        this.nodeCode = fiboNode.getNodeCode();
        this.nodeClazz = fiboNode.getNodeClazz();
        this.type = fiboNode.getType();
        this.fiboNode = fiboNode;
    }

    @Override
    public void runner(Integer contextIndex) {
        if(ObjectUtil.isNull(fiboNode)) {
            throw new EngineSystemException(StrUtil.format("节点[{}-{}]，没有节点实例", nodeId, beanName));
        }
        FiboContext context = Contextmanager.getContext(contextIndex);
        try {
            fiboNode.setContextIndex(contextIndex);
            fiboNode.runner();
        } catch (Exception e) {
            String errorMsg = StrUtil.format("[{}]:节点[{}-{}]，执行报错，错误信息:{}", context.getRequestId(), nodeId, beanName, e.getMessage());
            log.error(errorMsg);
            throw e;
        } finally {
            fiboNode.removeContextIndex();
        }
    }

    @Override
    public Long getRunnableId() {
        return nodeId;
    }

    @Override
    public String getRunnableName() {
        return beanName;
    }
}
