package com.fibo.rule.core.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fibo.rule.common.constant.EngineConstant;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.engine.condition.*;
import com.fibo.rule.core.engine.element.FiboEngine;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import com.fibo.rule.core.enums.NodeTypeClazzEnum;
import com.fibo.rule.core.exception.EngineBuildException;
import com.fibo.rule.core.node.FiboNode;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>引擎构建类</p>
 *
 * @author JPX
 * @since 2022-11-20 14:42
 */
public class EngineBuilder {

    /**引擎*/
    private FiboEngine fiboEngine;
    /**引擎下所有节点*/
    private List<EngineNodeDto> nodeList;
    /**所有节点编码对应关系*/
    private Map<String, EngineNodeDto> nodeDtoMap;
    /**所有节点编码对应关系*/
    private Map<String, FiboRunnable> runnableMap;
    /**递归当前节点*/
    private EngineNodeDto curNodeDto;
    /**聚合节点*/
    private EngineNodeDto aggNodeDto;
    /**串行节点栈*/
    private Stack<FiboSerialCondition> serialStack;
    /**已递归完成节点*/
    private Set<String> finishNodeSet;

    public static EngineBuilder createEngine(EngineDto engineDto) {
        return new EngineBuilder(engineDto);
    }

    public EngineBuilder(EngineDto engineDto) {
        this.nodeList = engineDto.getNodeList();
        this.nodeDtoMap = nodeList.stream().collect(Collectors.toMap(EngineNodeDto::getNodeCode, Function.identity()));
        List<EngineNodeDto> startList = nodeList.stream().filter(item -> NodeTypeEnum.START.getType().equals(item.getNodeType())).collect(Collectors.toList());
        if(CollUtil.isEmpty(startList) || startList.size() > 1) {
            throw new EngineBuildException(StrUtil.format("引擎[{}]开始节点异常", engineDto.getId()));
        }
        this.curNodeDto = startList.get(0);
        this.fiboEngine = new FiboEngine();
        this.fiboEngine.setEngineId(engineDto.getId());
        this.fiboEngine.setEngineName(engineDto.getEngineName());
        this.runnableMap = new HashMap<>();
        this.serialStack = new Stack<>();
        this.finishNodeSet = new HashSet<>();
    }

    public void build() {
        //创建FiboSerialCondition
        fiboEngine.setSerialCondition(buildSerialCondition());
        //递归解析引擎节点
        recursionEngineNode();
        EngineManager.addEngine(fiboEngine);
    }

    /**
     * 创建串行节点，并放到栈中
     * @return
     */
    private FiboSerialCondition buildSerialCondition() {
        FiboSerialCondition serialCondition = new FiboSerialCondition();
        serialStack.push(serialCondition);
        return serialCondition;
    }

    private void recursionEngineNode() {
        if(ObjectUtil.isNull(curNodeDto)) {
            return;
        }
        EngineNodeDto tempNodeDto = curNodeDto;
        //聚合节点，记录并跳出
        if(NodeTypeEnum.POLY.getType().equals(tempNodeDto.getNodeType())) {
            aggNodeDto = tempNodeDto;
            return;
        }
        //创建runnable
        FiboRunnable runnable = buildRunnable(tempNodeDto);
        if(ObjectUtil.isNotNull(runnable)) {
            //将节点加入到串行节点中
            FiboSerialCondition serialCondition = serialStack.peek();
            serialCondition.addRunnable(runnable);
        }
        if(finishNodeSet.contains(curNodeDto.getNodeCode())) {
            return;
        }
        //后续节点
        List<String> nextNodes = splitNextCodes(tempNodeDto.getNextNodes());
        //没有后续节点则跳出
        if(CollUtil.isEmpty(nextNodes)) {
            return;
        }

        for (String nextNode : nextNodes) {
            EngineNodeDto temp = nodeDtoMap.get(nextNode);
            curNodeDto = temp;
            //设置if、switch、all节点的分支
            boolean isHaveBranch = setConditionBranch(tempNodeDto, temp, runnable);
            recursionEngineNode();
            //弹出
            if(isHaveBranch) {
                serialStack.pop();
            }
        }

        if(NodeTypeEnum.ALL.getType().equals(tempNodeDto.getNodeType())) {
            curNodeDto = nodeDtoMap.get(aggNodeDto.getNextNodes());
            aggNodeDto = null;
            recursionEngineNode();
        }

        finishNodeSet.add(tempNodeDto.getNodeCode());
    }

    private FiboRunnable buildRunnable(EngineNodeDto nodeDto) {
        if(runnableMap.containsKey(nodeDto.getNodeCode())) {
            return runnableMap.get(nodeDto.getNodeCode());
        }
        NodeTypeEnum nodeTypeEnum = NodeTypeEnum.getEnum(nodeDto.getNodeType());
        if(ObjectUtil.isNull(nodeTypeEnum)) {
            throw new EngineBuildException(StrUtil.format("引擎[{}]节点[{}]类型错误", nodeDto.getEngineId(), nodeDto.getId()));
        }
        try {
            FiboCondition fiboCondition = NodeTypeClazzEnum.getFiboCondition(nodeTypeEnum);
            FiboEngineNode engineNode = buildEngineNode(nodeDto);
            //if/switch节点
            if(ObjectUtil.isNotNull(fiboCondition) && ObjectUtil.isNotNull(engineNode)) {
                fiboCondition.addRunnable(engineNode);
            }
            //all节点
            if(ObjectUtil.isNotNull(fiboCondition)) {
                fiboCondition.setId(nodeDto.getId());
                fiboCondition.setName(nodeDto.getBeanName());
                runnableMap.put(nodeDto.getNodeCode(), fiboCondition);
                return fiboCondition;
            }
            //普通节点
            if(ObjectUtil.isNotNull(engineNode)) {
                runnableMap.put(nodeDto.getNodeCode(), engineNode);
                return engineNode;
            }
            return null;
        } catch (Exception e) {
            throw new EngineBuildException(StrUtil.format("引擎[{}]节点[{}]创建condition失败，异常：{}", nodeDto.getEngineId(), nodeDto.getId(), e));
        }
    }

    /**
     * 设置if、switch、all节点的分支
     * @param tempNodeDto 当前节点
     * @param temp 分支节点
     * @param fiboRunnable 当前condition
     */
    private boolean setConditionBranch(EngineNodeDto tempNodeDto, EngineNodeDto temp, FiboRunnable fiboRunnable) {
        if(ObjectUtil.isNull(fiboRunnable)) {
            return false;
        }
        //if节点分支设置
        if(NodeTypeEnum.IF.getType().equals(tempNodeDto.getNodeType())) {
            String lineValue = JSONObject.parseObject(temp.getNodeConfig()).getString(EngineConstant.LINE_VALUE);
            //Y分支
            if(EngineConstant.STR_Y.equals(lineValue)) {
                FiboIfCondition fiboIfCondition = (FiboIfCondition) fiboRunnable;
                fiboIfCondition.setTrueBranch(buildSerialCondition());
                return true;
            }
            //N分支
            if(EngineConstant.STR_N.equals(lineValue)) {
                FiboIfCondition fiboIfCondition = (FiboIfCondition) fiboRunnable;
                fiboIfCondition.setFalseBranch(buildSerialCondition());
                return true;
            }
            //分支lineValue值不对
            throw new EngineBuildException(StrUtil.format("引擎[{}]节点[{}]IF分支创建失败，lineValue：[{}]", tempNodeDto.getEngineId(), tempNodeDto.getId(), lineValue));
        }
        //switch节点
        if(NodeTypeEnum.SWITCH.getType().equals(tempNodeDto.getNodeType())) {
            String lineValue = JSONObject.parseObject(temp.getNodeConfig()).getString(EngineConstant.LINE_VALUE);
            FiboSwitchCondition fiboSwitchCondition = (FiboSwitchCondition) fiboRunnable;
            fiboSwitchCondition.addSwitchRunnalbe(lineValue, buildSerialCondition());
            return true;
        }
        //all节点
        if(NodeTypeEnum.ALL.getType().equals(tempNodeDto.getNodeType())) {
            FiboAllCondition fiboAllCondition = (FiboAllCondition) fiboRunnable;
            fiboAllCondition.addRunnable(buildSerialCondition());
            return true;
        }
        return false;
    }

    /**
     * 创建节点
     * @param nodeDto
     * @return
     */
    private FiboEngineNode buildEngineNode(EngineNodeDto nodeDto) {
        if(StrUtil.isEmpty(nodeDto.getNodeClazz())) {
            return null;
        }
        FiboNode fiboNode = null;
        if(EngineManager.containNode(nodeDto.getNodeClazz())) {
            fiboNode = EngineManager.getNode(nodeDto.getNodeClazz());
        }
        if(ObjectUtil.isNull(fiboNode)) {
            try {
                String nodeConfig = nodeDto.getNodeConfig();
                if(StrUtil.isEmpty(nodeConfig)) {
                    nodeConfig = StrUtil.DELIM_START + StrUtil.DELIM_END;
                }
                fiboNode = (FiboNode) JSONObject.parseObject(nodeConfig, Class.forName(nodeDto.getNodeClazz()));
                fiboNode.setBeanName(nodeDto.getBeanName());
                fiboNode.setNodeClazz(nodeDto.getNodeClazz());
                fiboNode.setType(NodeTypeEnum.getEnum(nodeDto.getNodeType()));
            } catch (ClassNotFoundException e) {
                throw new EngineBuildException(StrUtil.format("引擎[{}]节点[{}]实例化失败，异常：{}", nodeDto.getEngineId(), nodeDto.getId(), e));
            }
        }
        FiboEngineNode fiboEngineNode = new FiboEngineNode(fiboNode, nodeDto);
        return fiboEngineNode;
    }

    private List<String> splitNextCodes(String str) {
        if(StrUtil.isEmpty(str)) {
            return new ArrayList<>();
        }
        return StrUtil.split(str, StrUtil.COMMA);
    }

}
