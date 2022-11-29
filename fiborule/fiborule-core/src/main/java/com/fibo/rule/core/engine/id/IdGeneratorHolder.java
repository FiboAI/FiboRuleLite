package com.fibo.rule.core.engine.id;

import cn.hutool.core.util.IdUtil;

/**
 *<p>id生成器</p>
 *
 *@author JPX
 *@since 2022/11/29 10:40
 */
public class IdGeneratorHolder {

    private static IdGeneratorHolder INSTANCE;

    public static void init(){
        INSTANCE = new IdGeneratorHolder();
    }

    public static IdGeneratorHolder getInstance() {
        return INSTANCE;
    }

    public String generate() {
        return IdUtil.fastSimpleUUID();
    }
}
