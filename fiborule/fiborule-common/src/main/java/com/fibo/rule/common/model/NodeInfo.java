package com.fibo.rule.common.model;

import lombok.Data;

import java.util.List;


@Data
public final class NodeInfo {
    private Byte type;
    private String clazz;
    private String name;
    private String desc;
    private List<IceFieldInfo> iceFields;
    private List<IceFieldInfo> hideFields;

    @Data
    public static class IceFieldInfo {
        private String field;
        private String name;
        private String desc;
        private String type;
        private Object value;
        private Boolean valueNull;
    }
}
