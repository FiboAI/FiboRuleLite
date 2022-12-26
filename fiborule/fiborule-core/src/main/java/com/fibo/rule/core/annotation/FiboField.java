package com.fibo.rule.core.annotation;

import com.fibo.rule.common.enums.FieldTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *<p>需配置属性注解</p>
 *
 * name默认为实际字段名，同一类下不可重复<br/>
 * type默认为default，按照字段实际类型进行处理<br/>
 *  默认支持:<br/>
 *  String-FieldTypeEnum.STRING<br/>
 *  java.math.BigDecimal、Integer、Long、int、long、double、float、Double、Float-FieldTypeEnum.NUMBER<br/>
 *  java.util.Date-FieldTypeEnum.DATE<br/>
 *
 *@author JPX
 *@since 2022/11/22 15:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FiboField {

    String name() default "";

    String desc() default "";

    FieldTypeEnum type() default FieldTypeEnum.DEFAULT;
}
