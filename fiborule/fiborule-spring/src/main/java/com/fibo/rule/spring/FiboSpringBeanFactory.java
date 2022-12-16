package com.fibo.rule.spring;

import com.fibo.rule.core.util.FiboBeanUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 *<p>spring bean 工厂</p>
 *
 *@author JPX
 *@since 2022/12/16 10:31
 */
public class FiboSpringBeanFactory implements FiboBeanUtils.FiboBeanFactory, ApplicationContextAware {

    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void autowireBean(Object existingBean) {
        this.beanFactory.autowireBean(existingBean);
    }

    @Override
    public boolean containsBean(String name) {
        return this.beanFactory.containsBean(name);
    }

    @Override
    public Object getBean(String name) {
        return beanFactory.getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.beanFactory = applicationContext.getAutowireCapableBeanFactory();
        FiboBeanUtils.setFactory(this);
    }
}
