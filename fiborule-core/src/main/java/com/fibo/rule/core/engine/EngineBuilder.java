package com.fibo.rule.core.engine;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.NodeDto;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.engine.condition.FiboAllCondition;
import com.fibo.rule.core.engine.condition.FiboCondition;
import com.fibo.rule.core.engine.condition.FiboIfCondition;
import com.fibo.rule.core.engine.condition.FiboSwitchCondition;
import com.fibo.rule.core.engine.element.FiboEngine;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import com.fibo.rule.core.enums.NodeTypeClazzEnum;
import com.fibo.rule.core.node.FiboNode;

import java.util.*;
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
    /**遍历元素栈*/
    private Stack<String> conditionStack;
    /**并行节点栈*/
    private Stack<NodeDto> parallelStack;
    /**聚合节点*/
    private NodeDto aggNodeDto;
    /**并行节点记录*/
    private Set<String> parallelSet;

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
        this.conditionMap = new HashMap<>();
        this.conditionStack = new Stack<>();
        this.parallelStack = new Stack<>();
        this.parallelSet = new HashSet<>();
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

        //后续节点
        List<String> nextNodes = splitNextCodes(tempNodeDto.getNextNodes());
        //并行节点开始节点和结束节点判断
        if(NodeTypeEnum.ALL.getType().equals(tempNodeDto.getNodeType())) {
            //后续节点不为空
            if(CollUtil.isNotEmpty(nextNodes)) {
                //后续节点为1，且没有并行开始节点，则该节点为并行结束节点
                if(nextNodes.size() == 1 && !parallelStack.isEmpty()) {
                    aggNodeDto = tempNodeDto;
                    return;
                }
                //否则节点为并行开始节点
                parallelStack.push(tempNodeDto);
            }
        }
        //创建condition
        FiboCondition fiboCondition = buildCondition(tempNodeDto);
        //没有后续节点则跳出
        if(CollUtil.isEmpty(nextNodes)) {
            return;
        }
        //将condition放入栈
        if(ObjectUtil.isNotNull(fiboCondition)) {
            conditionStack.push(tempNodeDto.getNodeCode());
        }

        for (String nextNode : nextNodes) {
            NodeDto temp = nodeDtoMap.get(nextNode);
            curNodeDto = temp;
            recursionEngineNode();
            //设置if、switch、all节点的分支
            setConditionBranch(tempNodeDto, temp, fiboCondition);
        }

        if(ObjectUtil.isNull(fiboCondition)) {
            return;
        }

        if(NodeTypeEnum.ALL.getType().equals(tempNodeDto.getNodeType())) {
            parallelStack.pop();
            curNodeDto = nodeDtoMap.get(aggNodeDto.getNextNodes());
            aggNodeDto = null;
            parallelSet.add(tempNodeDto.getNodeCode());
            recursionEngineNode();
        }

        //如果节点栈为空了，则当前节点为引擎根节点
        if(conditionStack.empty()) {
            engineRoot = fiboCondition;
            fiboEngine.setFiboRunnable(engineRoot);
            return;
        }
        setNextRunnable(tempNodeDto, fiboCondition);
    }

    /**
     * 设置后续节点
     * @param tempNodeDto
     * @param fiboCondition
     */
    public void setNextRunnable(NodeDto tempNodeDto, FiboCondition fiboCondition) {
        //弹出前置节点
        String preCode = conditionStack.peek();
        if(preCode.equals(tempNodeDto.getNodeCode())) {
            conditionStack.pop();
            preCode = conditionStack.peek();
        }
        NodeDto preNodeDto = nodeDtoMap.get(preCode);
        //if和switch不需要设置后续节点
        if(NodeTypeEnum.IF.getType().equals(preNodeDto.getNodeType())
                || NodeTypeEnum.SWITCH.getType().equals(preNodeDto.getNodeType())) {
            return;
        }

        //聚合节点如果出口未遍历完成
        if(NodeTypeEnum.ALL.getType().equals(preNodeDto.getNodeType())
                && !parallelSet.contains(preNodeDto.getNodeCode())) {
            return;
        }

        FiboCondition preCondition = conditionMap.get(preCode);
        preCondition.setNextRunnable(fiboCondition);
        conditionStack.pop();

    }

    /**
     * 设置if、switch、all节点的分支
     * @param tempNodeDto 当前节点
     * @param temp 分支节点
     * @param fiboCondition 当前condition
     */
    private void setConditionBranch(NodeDto tempNodeDto, NodeDto temp, FiboCondition fiboCondition) {
        if(ObjectUtil.isNull(fiboCondition)) {
            return;
        }
        //if节点分支设置
        if(NodeTypeEnum.IF.getType().equals(tempNodeDto.getNodeType())) {
            String lineValue = JSONObject.parseObject(temp.getNodeConfig()).getString("lineValue");
            if(lineValue.equals("Y")) {
                FiboIfCondition fiboIfCondition = (FiboIfCondition) fiboCondition;
                fiboIfCondition.setTrueBranch(conditionMap.get(temp.getNodeCode()));
            } else if(lineValue.equals("N")) {
                FiboIfCondition fiboIfCondition = (FiboIfCondition) fiboCondition;
                fiboIfCondition.setFalseBranch(conditionMap.get(temp.getNodeCode()));
            }
        } else if(NodeTypeEnum.SWITCH.getType().equals(tempNodeDto.getNodeType())) {
            String lineValue = JSONObject.parseObject(temp.getNodeConfig()).getString("lineValue");
            FiboSwitchCondition fiboSwitchCondition = (FiboSwitchCondition) fiboCondition;
            fiboSwitchCondition.addSwitchFiboRunnalbe(lineValue, conditionMap.get(temp.getNodeCode()));
        } else if(NodeTypeEnum.ALL.getType().equals(tempNodeDto.getNodeType())) {
            FiboAllCondition fiboAllCondition = (FiboAllCondition) fiboCondition;
            fiboAllCondition.addRunnable(conditionMap.get(temp.getNodeCode()));
        }
    }

    /**
     * 创建condition
     * @return
     */
    private FiboCondition buildCondition(NodeDto nodeDto) {
        if(conditionMap.containsKey(nodeDto.getNodeCode())) {
            return conditionMap.get(nodeDto.getNodeCode());
        }
        NodeTypeEnum nodeTypeEnum = NodeTypeEnum.getEnum(nodeDto.getNodeType());
        if(ObjectUtil.isNull(nodeTypeEnum)) {
            throw new RuntimeException();
        }
        try {
            FiboCondition fiboCondition = NodeTypeClazzEnum.getFiboCondition(nodeTypeEnum);
            if(ObjectUtil.isNotNull(fiboCondition)) {
                fiboCondition.setFiboRunnable(buildEngineNode(nodeDto));
                conditionMap.put(nodeDto.getNodeCode(), fiboCondition);
            }
            return fiboCondition;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 创建节点
     * @param nodeDto
     * @return
     */
    private FiboEngineNode buildEngineNode(NodeDto nodeDto) {
        if(StrUtil.isEmpty(nodeDto.getNodeClazz())) {
            return null;
        }
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

    private List<String> splitNextCodes(String str) {
        if(StrUtil.isEmpty(str)) {
            return new ArrayList<>();
        }
        return StrUtil.split(str, StrUtil.COMMA);
    }

}
