package com.fibo.rule.core.engine;

import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.engine.element.FiboEngine;
import com.fibo.rule.core.node.FiboNode;
import com.fibo.rule.core.util.CopyOnWriteHashMap;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 *<p>引擎数据管理</p>
 *
 *@author JPX
 *@since 2022/11/18 13:44
 */
@Slf4j
public class EngineManager {

    private static final Map<Long, FiboEngine> engineMap = new CopyOnWriteHashMap<>();

    private static final Map<String, FiboNode> nodeMap = new CopyOnWriteHashMap<>();

    private EngineManager() {
    }

    public static void buildEngines(List<EngineDto> engineDtoList) {
        for (EngineDto engineDto : engineDtoList) {
            try {
                EngineBuilder.createEngine(engineDto).build();
            } catch (Exception e) {
                log.error("引擎[{}]构建失败", engineDto.getId(), e);
            }
        }
    }

    public static void buildEngine(EngineDto engineDto) {
        EngineBuilder.createEngine(engineDto).build();
    }

    public static FiboEngine getEngine(Long engineId){
        return engineMap.get(engineId);
    }

    public static void addEngine(FiboEngine engine) {
        engineMap.put(engine.getEngineId(), engine);
    }

    public static boolean containEngine(Long engineId) {
        return engineMap.containsKey(engineId);
    }

    public static Map<String, FiboNode> getNodeMap(){
        return nodeMap;
    }

    public static Map<Long, FiboEngine> getEngineMap(){
        return engineMap;
    }

    public static void cleanCache() {
        engineMap.clear();
        nodeMap.clear();
    }

    public static boolean removeEngine(Long engineId){
        if (containEngine(engineId)){
            engineMap.remove(engineId);
            return true;
        }else{
            return false;
        }
    }

    public static FiboNode getNode(String nodeClazz) {
        return nodeMap.get(nodeClazz);
    }

    public static void addNode(String beanName, Class<?> nodeClass, NodeTypeEnum type) throws Exception {
        try {
            FiboNode fiboNode = (FiboNode) nodeClass.newInstance();
            fiboNode.setBeanName(beanName);
            fiboNode.setNodeClazz(nodeClass.getName());
            fiboNode.setType(type);
            nodeMap.put(nodeClass.getName(), fiboNode);
        } catch (Exception e) {
            log.error("节点[{}]注册失败", beanName);
            throw e;
        }
    }

    public static boolean containNode(String nodeClazz) {
        return nodeMap.containsKey(nodeClazz);
    }
}
