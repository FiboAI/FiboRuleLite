package com.fibo.rule.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *<p>组件注解</p>
 *
 * name默认为实际类名，同一场景下不可重复
 *
 *@author JPX
 *@since 2022/11/22 15:25
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface FiboBean {

    String name() default "";

    String desc() default "";
}
