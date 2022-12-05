package com.fibo.rule.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.rule.common.enums.DelFlagEnum;
import com.fibo.rule.common.enums.StatusEnum;
import com.fibo.rule.server.dao.mapper.EngineMapper;
import com.fibo.rule.server.dao.mapper.EngineNodeMapper;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.entity.EngineNode;
import com.fibo.rule.server.dao.model.param.EngineDetailParam;
import com.fibo.rule.server.dao.model.param.EngineEditParam;
import com.fibo.rule.server.dao.model.param.EngineListParam;
import com.fibo.rule.server.dao.model.vo.EngineDetailVO;
import com.fibo.rule.server.dao.model.vo.EngineNodeDetailVO;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.nio.NioClientManager;
import com.fibo.rule.server.service.EngineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class EngineServiceImpl extends ServiceImpl<EngineMapper, Engine> implements EngineService {

    @Resource
    private EngineMapper engineMapper;

    @Resource
    private EngineNodeMapper engineNodeMapper;

    @Resource
    private NioClientManager nioClientManager;

    @Override
    public PageInfo<Engine> engineList(EngineListParam param) {
        return PageHelper.startPage(1, 10)
                .doSelectPageInfo(() -> engineMapper.selectListByPage(param));
    }

    @Override
    public List<String> getSceneList(SimpleCommonPrimaryKeyParam param) {
        return nioClientManager.getSceneList(param.getId());
    }

    @Override
    public Engine engineEdit(EngineEditParam param) {
        Engine engine = new Engine();
        BeanUtils.copyProperties(param, engine);
        if (null != param.getEngineId()) {
            engine.setId(param.getEngineId());
            engineMapper.updateById(engine);
        } else {
            engineMapper.insert(engine);
        }
        engine = engineMapper.selectById(engine.getId());
        return engine;
    }

    @Override
    public EngineDetailVO getEngineDetail(EngineDetailParam param) {
        EngineDetailVO vo = new EngineDetailVO();
        vo.setAppId(param.getAppId());
        Engine engine = engineMapper.selectById(param.getEngineId());
        vo.setEngine(engine);
        List<EngineNode> nodes = engineNodeMapper.selectList(new QueryWrapper<EngineNode>().lambda()
                .eq(EngineNode::getEngineId, param.getEngineId())
                .eq(EngineNode::getStatus, StatusEnum.VALID.status)
                .eq(EngineNode::getDelFlag, DelFlagEnum.DEL_NO.status));
        List<EngineNodeDetailVO> nodesDetail = new ArrayList<>();
        BeanUtils.copyProperties(nodes, nodesDetail);
        vo.setNodesDetail(nodesDetail);
        return vo;
    }

    @Override
    public void engineDelete(SimpleCommonPrimaryKeyParam param) {
        Engine engine = engineMapper.selectById(param.getId());
        if (null != engine) {
            engine.setDelFlag(1);
            engineMapper.updateById(engine);
        }
    }


}
