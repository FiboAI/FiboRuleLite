package com.fibo.rule.core.engine.step;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.common.enums.NodeTypeEnum;
import lombok.Data;

/**
 *<p>节点执行步骤</p>
 *
 *@author JPX
 *@since 2022/11/29 14:08
 */
@Data
public class NodeStep {

    private Long nodeId;
    private String nodeName;
    private String beanName;
    private String nodeCode;
    private String nodeClazz;
    private NodeTypeEnum nodeType;

    //消耗的时间，毫秒为单位
    private Long timeSpent;

    //是否成功
    private boolean success;

    //有exception，success一定为false
    //但是success为false，不一定有exception，因为有可能没执行到，或者没执行结束(any)
    private Exception exception;

    public NodeStep(Long nodeId, String nodeName, String beanName, String nodeCode, String nodeClazz, NodeTypeEnum nodeType) {
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.beanName = beanName;
        this.nodeCode = nodeCode;
        this.nodeClazz = nodeClazz;
        this.nodeType = nodeType;
    }

    public String buildString() {
        return StrUtil.format("[{}]:[{}]", nodeId, nodeCode);
    }

    public String buildStringWithTime() {
        if (timeSpent != null){
            return StrUtil.format("[{}]:[{}]<{}>", nodeId, nodeCode, timeSpent);
        }else{
            return StrUtil.format("[{}]:[{}]", nodeId, nodeCode);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (ObjectUtil.isNull(obj)) {
            return false;
        } else {
            if (getClass() != obj.getClass()) {
                return false;
            } else {
                if (((NodeStep) obj).getNodeId().equals(this.getNodeId())) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

}
