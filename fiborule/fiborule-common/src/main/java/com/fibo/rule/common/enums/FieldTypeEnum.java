package com.fibo.rule.common.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 *<p>节点字段类型枚举类</p>
 *
 *@author JPX
 *@since 2022/11/18 14:21
 */
public enum FieldTypeEnum {

    /**默认类型*/
    DEFAULT,
    /**数值型*/
    NUMBER,
    /**字符型*/
    STRING,
    /**时间型*/
    DATE;

    private static Set<Class> numberSet = new HashSet<>(Arrays.asList(
            BigDecimal.class, Integer.class, int.class, Long.class, long.class,
            Double.class, double.class, Float.class, float.class));

    public static <T> FieldTypeEnum getFieldTypeByClazz(Class<T> clazz) {
        if(String.class.equals(clazz)) {
            return STRING;
        } else if(Date.class.equals(clazz)) {
            return DATE;
        } else if(numberSet.contains(clazz)) {
            return NUMBER;
        } else {
            return STRING;
        }
    }

}
