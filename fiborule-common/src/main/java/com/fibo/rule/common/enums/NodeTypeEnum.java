package com.fibo.rule.common.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *<p></p>
 *
 *@author JPX
 *@since 2022/11/18 14:21
 */
public enum NodeTypeEnum {

    /**开始节点*/
    START(1),
    /**结束节点*/
    END(2),
    /**普通节点*/
    NORMAL(3),
    /**IF节点*/
    IF(4),
    /**SWITCH节点*/
    SWITCH(5),
    /**ALL节点*/
    ALL(6);

    private static final Map<Integer, NodeTypeEnum> MAP = new HashMap<>(NodeTypeEnum.values().length);

    static {
        for (NodeTypeEnum enums : NodeTypeEnum.values()) {
            MAP.put(enums.getType(), enums);
        }
    }

    private final Integer type;

    NodeTypeEnum(Integer type) {
        this.type = type;
    }

    public static NodeTypeEnum getEnum(Integer type) {
        if (type == null) {
            return null;
        }
        return MAP.get(type);
    }

    public Integer getType() {
        return type;
    }

}
