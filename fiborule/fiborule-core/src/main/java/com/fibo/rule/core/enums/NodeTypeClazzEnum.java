package com.fibo.rule.core.enums;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.engine.condition.*;

import java.util.HashMap;
import java.util.Map;

/**
 *<p>节点类型对应的condition</p>
 *
 *@author JPX
 *@since 2022/11/18 14:21
 */
public enum NodeTypeClazzEnum {

    /**开始节点*/
    START(NodeTypeEnum.START, null),
    /**结束节点*/
    END(NodeTypeEnum.END, null),
    /**普通节点*/
    NORMAL(NodeTypeEnum.NORMAL, null),
    /**IF节点*/
    IF(NodeTypeEnum.IF, FiboIfCondition.class),
    /**SWITCH节点*/
    SWITCH(NodeTypeEnum.SWITCH, FiboSwitchCondition.class),
    /**ALL节点*/
    ALL(NodeTypeEnum.ALL, FiboAllCondition.class);

    private static final Map<NodeTypeEnum, NodeTypeClazzEnum> MAP = new HashMap<>(NodeTypeClazzEnum.values().length);

    static {
        for (NodeTypeClazzEnum enums : NodeTypeClazzEnum.values()) {
            MAP.put(enums.getNodeTypeEnum(), enums);
        }
    }

    private final NodeTypeEnum nodeTypeEnum;
    private final Class<? extends FiboCondition> mappingClazz;

    NodeTypeClazzEnum(NodeTypeEnum nodeTypeEnum, Class<? extends FiboCondition> mappingClazz) {
        this.nodeTypeEnum = nodeTypeEnum;
        this.mappingClazz = mappingClazz;
    }

    public Class<? extends FiboCondition> getMappingClazz() {
        return mappingClazz;
    }

    public static FiboCondition getFiboCondition(NodeTypeEnum nodeTypeEnum) throws InstantiationException, IllegalAccessException {
        if (nodeTypeEnum == null) {
            return null;
        }
        Class<? extends FiboCondition> tempClazz = MAP.get(nodeTypeEnum).getMappingClazz();
        if(ObjectUtil.isNull(tempClazz)) {
            return null;
        }

        return tempClazz.newInstance();
    }

    public NodeTypeEnum getNodeTypeEnum() {
        return nodeTypeEnum;
    }

}
