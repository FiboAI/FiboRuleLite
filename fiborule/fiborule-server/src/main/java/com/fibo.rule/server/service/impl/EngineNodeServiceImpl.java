package com.fibo.rule.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.server.dao.mapper.EngineNodeMapper;
import com.fibo.rule.server.dao.model.entity.EngineNode;
import com.fibo.rule.server.dao.model.param.*;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.nio.NioClientManager;
import com.fibo.rule.server.service.EngineNodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Slf4j
@Service
public class EngineNodeServiceImpl extends ServiceImpl<EngineNodeMapper, EngineNode> implements EngineNodeService {

    @Resource
    private EngineNodeMapper engineNodeMapper;

    @Resource
    private NioClientManager nioClientManager;


    @Override
    public List<FiboBeanDto> listNodesByType(NodesListParam param) {
        return nioClientManager.getNodesList(param.getAppId(), param.getScene(), param.getNodeType());
    }

    @Override
    public EngineNode nodeEdit(NodeEditParam param) {
        EngineNode node = new EngineNode();
        BeanUtils.copyProperties(param, node);
        if (null != param.getNodeId()) {
            node.setId(param.getNodeId());
            engineNodeMapper.updateById(node);
        } else {
            engineNodeMapper.insert(node);
        }
        node = engineNodeMapper.selectById(node.getId());
        return node;
    }

    @Override
    public void nodeDelete(SimpleCommonPrimaryKeyParam param) {
        EngineNode node = engineNodeMapper.selectById(param.getId());
        if (null != node) {
            node.setDelFlag(1);
            engineNodeMapper.updateById(node);
        }
    }

    @Override
    public void moveLocation(NodeMoveLocationParam param) {
        UpdateWrapper<EngineNode> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(EngineNode::getId, param.getNodeId());
        EngineNode node = new EngineNode();
        node.setNodeX(param.getNodeX());
        node.setNodeY(param.getNodeY());
        engineNodeMapper.update(node, updateWrapper);
    }

    @Override
    public void moveLocations(NodeMoveLocationParams param) {
        for (NodeMoveLocationParam nodeMoveLocationParam : param.getNodeMoveLocationParams()) {
            moveLocation(nodeMoveLocationParam);
        }
    }

    @Override
    public void connectLine(NodeConnectLineParam param) {
        for (NodeConnectLineDetailParam nodeConnect : param.getNodes()) {
            UpdateWrapper<EngineNode> updateWrapper = new UpdateWrapper<>();
            updateWrapper.lambda()
                    .set(EngineNode::getPreNodes, nodeConnect.getPreNodes())
                    .set(EngineNode::getNextNodes, nodeConnect.getNextNodes())
                    .eq(EngineNode::getId, nodeConnect.getNodeId());
            engineNodeMapper.update(null, updateWrapper);
        }
    }

    @Override
    public void reConnectLine(NodeConnectLineParam param) {
        connectLine(param);
    }

    @Override
    public void breakLine(NodeConnectLineParam param) {
        connectLine(param);
    }
}
