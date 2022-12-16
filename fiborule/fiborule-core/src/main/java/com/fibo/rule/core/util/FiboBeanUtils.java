package com.fibo.rule.core.util;

/**
 *<p>fibo bean 生成工具类</p>
 *
 *@author JPX
 *@since 2022/12/16 10:31
 */
public final class FiboBeanUtils {

    private static FiboBeanFactory factory;

    private FiboBeanUtils() {
    }

    public static void autowireBean(Object existingBean) {
        if (factory == null) {
            return;
        }
        factory.autowireBean(existingBean);
    }

    public static boolean containsBean(String name) {
        if (factory == null) {
            return false;
        }
        return factory.containsBean(name);
    }

    public static Object getBean(String name) {
        if (factory == null) {
            return null;
        }
        return factory.getBean(name);
    }

    public static void setFactory(FiboBeanFactory factory) {
        FiboBeanUtils.factory = factory;
    }

    public interface FiboBeanFactory {

        void autowireBean(Object existingBean);

        boolean containsBean(String name);

        Object getBean(String name);
    }
}
