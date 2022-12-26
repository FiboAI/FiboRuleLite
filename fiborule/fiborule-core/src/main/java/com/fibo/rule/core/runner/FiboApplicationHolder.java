package com.fibo.rule.core.runner;

import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.exception.FiboApplicationNotInitException;
import com.fibo.rule.core.property.FiboRuleConfig;

public class FiboApplicationHolder {

    private static FiboApplication application;

    public static FiboApplication loadInstance(FiboRuleConfig config){
        if (ObjectUtil.isNull(application)){
            application = new FiboApplication(config);
        }
        return application;
    }

    public static FiboApplication loadInstance(){
        if (ObjectUtil.isNull(application)){
            throw new FiboApplicationNotInitException("执行器未初始化");
        }
        return application;
    }

    public static void setHolder(FiboApplication application){
        FiboApplicationHolder.application = application;
    }

    public static void clean(){
        application = null;
    }
}
