package com.fibo.rule.core.property;

import cn.hutool.core.util.ObjectUtil;

/**
 *<p>Fiborule配置获取</p>
 *
 *@author JPX
 *@since 2022/11/28 17:17
 */
public class FiboRuleConfigGetter {

    private static FiboRuleConfig fiboRuleConfig;

    public static FiboRuleConfig get(){
        if (ObjectUtil.isNull(fiboRuleConfig)){
            fiboRuleConfig = new FiboRuleConfig();
        }
        return fiboRuleConfig;
    }

    public static void clean(){
        fiboRuleConfig = null;
    }

    public static void setFiboRuleConfig(FiboRuleConfig fiboRuleConfig){
        FiboRuleConfigGetter.fiboRuleConfig = fiboRuleConfig;
    }
}
