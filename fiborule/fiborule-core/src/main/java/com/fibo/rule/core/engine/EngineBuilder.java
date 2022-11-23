package com.fibo.rule.core.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.NodeDto;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.engine.condition.FiboCondition;
import com.fibo.rule.core.engine.condition.FiboIfCondition;
import com.fibo.rule.core.engine.condition.FiboNormalCondition;
import com.fibo.rule.core.engine.element.FiboEngine;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import com.fibo.rule.core.node.FiboNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>引擎</p>
 *
 * @author JPX
 * @since 2022-11-20 14:42
 */
public class EngineBuilder {

    /**引擎*/
    private FiboEngine fiboEngine;
    /**根节点*/
    private FiboRunnable engineRoot;
    /**引擎下所有节点*/
    private List<NodeDto> nodeList;
    /**所有节点编码对应关系*/
    private Map<String, NodeDto> nodeDtoMap;
    /**所有节点编码对应关系*/
    private Map<String, FiboCondition> conditionMap;
    /**递归当前节点*/
    private NodeDto curNodeDto;
    /**节点前置节点数量*/
    private Map<String, Integer> nodePreNumMap;
    /**节点后置节点数量*/
    private Map<String, Integer> nodeNextNumMap;
    /**遍历元素栈*/
    private Stack<String> conditionStack;
    /**聚合元素栈*/
    private Stack<NodeDto> mergeStack;
    /**存储节点为合并节点*/
    private Map<String, Boolean> mergeNodeMap;

    public static EngineBuilder createEngine(EngineDto engineDto) {
        return new EngineBuilder(engineDto);
    }

    public EngineBuilder(EngineDto engineDto) {
        this.nodeList = engineDto.getNodeList();
        this.nodeDtoMap = nodeList.stream().collect(Collectors.toMap(NodeDto::getNodeCode, Function.identity()));
        List<NodeDto> startList = nodeList.stream().filter(item -> NodeTypeEnum.START.getType().equals(item.getNodeType())).collect(Collectors.toList());
        if(CollUtil.isEmpty(startList) || startList.size() > 1) {
            // TODO: 2022/11/18 没有开始节点或有多个开始节点抛出异常
            throw new RuntimeException();
        }
        this.curNodeDto = startList.get(0);
        this.fiboEngine = new FiboEngine();
        this.fiboEngine.setEngineId(engineDto.getId());
        this.nodeNextNumMap = new HashMap<>();
        this.nodePreNumMap = new HashMap<>();
        this.conditionMap = new HashMap<>();
        this.conditionStack = new Stack<>();
        this.mergeStack = new Stack<>();
        this.mergeNodeMap = new HashMap<>();
    }

    public void build() {
        recursionEngineNode();
        EngineManager.addEngine(fiboEngine);
    }

    private void recursionEngineNode() {
        if(ObjectUtil.isNull(curNodeDto)) {
            return;
        }
        NodeDto tempNodeDto = curNodeDto;
        //计算前置节点数量
        calcPreNodesNum(tempNodeDto);
        //前置节点有多个则将栈顶元素弹出
        if(nodePreNumMap.containsKey(tempNodeDto.getNodeCode())) {
            if(!conditionStack.empty()) {
                conditionStack.pop();
            }
            if(nodePreNumMap.get(tempNodeDto.getNodeCode()) == 0) {
                mergeStack.push(tempNodeDto);
            }
            return;
        }
        //创建condition
        FiboCondition fiboCondition = buildCondition();
        //将condition放入队列
        if(ObjectUtil.isNotNull(fiboCondition)) {
            conditionMap.put(tempNodeDto.getNodeCode(), fiboCondition);
            conditionStack.push(tempNodeDto.getNodeCode());
        }
        if(StrUtil.isEmpty(tempNodeDto.getNextNodes())) {
            return;
        }
        //后续节点
        List<String> nextNodes = StrUtil.split(tempNodeDto.getNextNodes(), StrUtil.COMMA);
        if(CollUtil.isEmpty(nextNodes)) {
            return;
        }
        //记录节点分支遍历数量
        if(nextNodes.size() > 1) {
            nodeNextNumMap.put(tempNodeDto.getNodeCode(), nextNodes.size());
        }
        for (String nextNode : nextNodes) {
            if(nodeNextNumMap.containsKey(tempNodeDto.getNodeCode())) {
                nodeNextNumMap.put(tempNodeDto.getNodeCode(), nodeNextNumMap.get(tempNodeDto.getNodeCode()) - 1);
            }
            NodeDto temp = nodeDtoMap.get(nextNode);
            curNodeDto = temp;
            recursionEngineNode();
            //if节点分支设置
            if(NodeTypeEnum.IF.getType().equals(tempNodeDto.getNodeType())) {
                String lineValue = JSONObject.parseObject(temp.getNodeConfig()).getString("lineValue");
                if(lineValue.equals("Y")) {
                    FiboIfCondition fiboIfCondition = (FiboIfCondition) fiboCondition;
                    fiboIfCondition.setTrueBranch(conditionMap.get(temp.getNextNodes()));
                } else if(lineValue.equals("N")) {
                    FiboIfCondition fiboIfCondition = (FiboIfCondition) fiboCondition;
                    fiboIfCondition.setFalseBranch(conditionMap.get(temp.getNextNodes()));
                }
            }
        }
        //当前节点有condition，则前置condition的分支为1，设置前置的后置condition
        if(ObjectUtil.isNotNull(fiboCondition)) {
            if(conditionStack.empty()) {
                engineRoot = fiboCondition;
                fiboEngine.setFiboRunnable(engineRoot);
                return;
            }
            //弹出前置节点
            String preCode = conditionStack.peek();
            if(preCode.equals(tempNodeDto.getNodeCode())) {
                conditionStack.pop();
                preCode = conditionStack.peek();
            }
            if(!nodeNextNumMap.containsKey(preCode)) {
                //分支数量为1个
                FiboCondition preCondition = conditionMap.get(preCode);
                preCondition.setNextRunnable(fiboCondition);
                conditionStack.pop();
            } else if(nodeNextNumMap.get(preCode) == 0) {
                if(!mergeStack.empty()) {
                    NodeDto mergeNode = mergeStack.pop();
                    if (ObjectUtil.isNotNull(mergeNode)) {
                        curNodeDto = mergeNode;
                        mergeNodeMap.put(mergeNode.getNodeCode(), true);
                        recursionEngineNode();
                    }
                } else {
                    //如果当前节点是合并节点
                    if(mergeNodeMap.getOrDefault(tempNodeDto.getNodeCode(), false)) {
                        FiboCondition preCondition = conditionMap.get(preCode);
                        preCondition.setNextRunnable(fiboCondition);
                    }
                    conditionStack.pop();
                }
            }
        }

    }

    /**
     * 计算前置节点数量
     * @param nodeDto
     * @return
     */
    private void calcPreNodesNum(NodeDto nodeDto) {
        if(nodePreNumMap.containsKey(nodeDto.getNodeCode()) && nodePreNumMap.get(nodeDto.getNodeCode()) == 0) {
            nodePreNumMap.remove(nodeDto.getNodeCode());
            return;
        }
        if(nodePreNumMap.containsKey(nodeDto.getNodeCode()) && nodePreNumMap.get(nodeDto.getNodeCode()) > 0) {
            nodePreNumMap.put(nodeDto.getNodeCode(), nodePreNumMap.get(nodeDto.getNodeCode()) - 1);
            return;
        }
        if(StrUtil.isEmpty(nodeDto.getPreNodes())) {
            return;
        }
        List<String> preNodes = StrUtil.split(nodeDto.getPreNodes(), StrUtil.COMMA);
        if(preNodes.size() > 1) {
            nodePreNumMap.put(nodeDto.getNodeCode(), preNodes.size() - 1);
        }
    }

    private FiboEngineNode buildEngineNode(NodeDto nodeDto) {
        try {
            FiboEngineNode fiboEngineNode = new FiboEngineNode();
            fiboEngineNode.setNodeId(nodeDto.getId());
            fiboEngineNode.setNodeName(nodeDto.getNodeName());
            fiboEngineNode.setNodeCode(nodeDto.getNodeCode());
            String nodeConfig = nodeDto.getNodeConfig();
            if(StrUtil.isEmpty(nodeConfig)) {
                nodeConfig = StrUtil.DELIM_START + StrUtil.DELIM_END;
            }
            FiboNode fiboNode = (FiboNode) JSONObject.parseObject(nodeConfig, Class.forName(nodeDto.getNodeClazz()));
            fiboNode.setNodeId(nodeDto.getId());
            fiboNode.setNodeName(nodeDto.getNodeName());
            fiboNode.setNodeCode(nodeDto.getNodeCode());
            fiboEngineNode.setFiboNode(fiboNode);
            return fiboEngineNode;
        } catch (ClassNotFoundException e) {
            // TODO: 2022/11/21 抛出异常
            throw new RuntimeException();
        }
    }

    private FiboCondition buildCondition() {
        // TODO: 2022/11/18 判断节点类型是否正确
        switch (NodeTypeEnum.getEnum(curNodeDto.getNodeType())) {
            case START:
                break;
            case END:
                break;
            case LINE:
                break;
            case NORMAL:
                //创建condition
                FiboNormalCondition fiboNormalCondition = new FiboNormalCondition();
                fiboNormalCondition.setFiboRunnable(buildEngineNode(curNodeDto));
                return fiboNormalCondition;
            case IF:
                //创建condition，并按后续两条线判断条件
                FiboIfCondition fiboIfCondition = new FiboIfCondition();
                fiboIfCondition.setFiboRunnable(buildEngineNode(curNodeDto));
                return fiboIfCondition;
            case SWITCH:
                //创建condition，并按后续线构造switch
                break;
            case ALL:
                //创建condition，获取其后续所有节点并创建
                break;
            default:
                // TODO: 2022/11/18 抛出异常
                throw new RuntimeException();
        }
        return null;
    }

}
