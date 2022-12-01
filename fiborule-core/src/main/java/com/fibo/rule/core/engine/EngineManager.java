package com.fibo.rule.core.engine;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.engine.element.FiboEngine;
import com.fibo.rule.core.util.CopyOnWriteHashMap;

import java.util.Map;

/**
 *<p>引擎数据管理</p>
 *
 *@author JPX
 *@since 2022/11/18 13:44
 */
public class EngineManager {

    private static final Map<Long, FiboEngine> engineMap = new CopyOnWriteHashMap<>();

    private EngineManager() {
    }

    public static FiboEngine getEngine(Long engineId){
        return engineMap.get(engineId);
    }

    //这个方法主要用于第二阶段的替换chain
    public static void addEngine(FiboEngine engine) {
        engineMap.put(engine.getEngineId(), engine);
    }

    public static boolean containEngine(Long engineId) {
        return engineMap.containsKey(engineId);
    }

    public static Map<Long, FiboEngine> getEngineMap(){
        return engineMap;
    }

    public static void cleanCache() {
        engineMap.clear();
    }

    public static boolean removeEngine(Long engineId){
        if (containEngine(engineId)){
            engineMap.remove(engineId);
            return true;
        }else{
            String errMsg = StrUtil.format("cannot find the chain[{}]", engineId);
//            LOG.error(errMsg);
            return false;
        }
    }
}
