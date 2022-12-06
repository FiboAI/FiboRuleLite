package com.fibo.rule.core.annotation;

import com.fibo.rule.common.enums.FieldTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *<p>需配置属性注解</p>
 *
 *@author JPX
 *@since 2022/11/22 15:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface FiboField {

    String name() default "";

    String desc() default "";

    FieldTypeEnum type() default FieldTypeEnum.STRING;
}
