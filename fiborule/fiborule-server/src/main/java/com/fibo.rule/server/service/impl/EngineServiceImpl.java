package com.fibo.rule.server.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.EngineNodeDto;
import com.fibo.rule.common.enums.DelFlagEnum;
import com.fibo.rule.common.enums.StatusEnum;
import com.fibo.rule.server.dao.mapper.EngineMapper;
import com.fibo.rule.server.dao.mapper.EngineNodeMapper;
import com.fibo.rule.server.dao.model.entity.Engine;
import com.fibo.rule.server.dao.model.entity.EngineNode;
import com.fibo.rule.server.dao.model.param.EngineDetailParam;
import com.fibo.rule.server.dao.model.param.EngineEditParam;
import com.fibo.rule.server.dao.model.param.EngineListParam;
import com.fibo.rule.server.dao.model.param.EngineReleaseParam;
import com.fibo.rule.server.dao.model.vo.EngineDetailVO;
import com.fibo.rule.server.dao.model.vo.EngineNodeDetailVO;
import com.fibo.rule.server.enums.BootStatusEnum;
import com.fibo.rule.server.enums.ErrorCodeEnum;
import com.fibo.rule.server.exception.ApiException;
import com.fibo.rule.server.model.SimpleCommonPrimaryKeyParam;
import com.fibo.rule.server.nio.NioClientManager;
import com.fibo.rule.server.service.EngineService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
        return PageHelper.startPage(param.getPageNum(), param.getPageSize())
                .doSelectPageInfo(() -> engineMapper.selectListByPage(param));
    }

    @Override
    public List<String> getSceneList(SimpleCommonPrimaryKeyParam param) {
        return nioClientManager.getSceneList(param.getId());
    }

    @Override
    public Engine engineEdit(EngineEditParam param) {
        this.verifyParam(param);
        Engine engine = new Engine();
        BeanUtils.copyProperties(param, engine);
        if (null != param.getId()) {
            engine.setEngineCode(null);
            engineMapper.updateById(engine);
        } else {
            engineMapper.insert(engine);
        }
        engine = engineMapper.selectById(engine.getId());
        return engine;
    }

    private void verifyParam(EngineEditParam param) {
        if (null == param.getId()) {
            int count = engineMapper.selectCount(new QueryWrapper<Engine>().lambda()
                    .eq(Engine::getAppId, param.getAppId())
                    .eq(Engine::getDelFlag, DelFlagEnum.DEL_NO.status)
                    .eq(Engine::getEngineCode, param.getEngineCode()));
            if (count > 0) {
                throw new ApiException(ErrorCodeEnum.ENGINE_CODE_REDO_EXCEPTION);
            }
        } else {
            Engine engine = engineMapper.selectById(param.getId());
            if (null != engine && engine.getBootStatus().equals(BootStatusEnum.BOOT.status)) {
                throw new ApiException(ErrorCodeEnum.ENGINE_BOOT_EDIT_EXCEPTION);
            }
        }
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
        for (EngineNode node : nodes) {
            EngineNodeDetailVO detailVO = new EngineNodeDetailVO();
            BeanUtils.copyProperties(node, detailVO);
            nodesDetail.add(detailVO);
        }
        vo.setNodesDetail(nodesDetail);
        return vo;
    }

    @Override
    public void engineRelease(EngineReleaseParam param) {
        if (param.getBootStatus().equals(BootStatusEnum.BOOT.status)) {
            List<EngineDto> engineDtoList = this.getEngineDtoList(param.getAppId(), param.getEngineId());
            nioClientManager.release(param.getAppId(), engineDtoList, null);
        } else {
            nioClientManager.release(param.getAppId(), null, param.getEngineId());
        }

    }

    @Override
    public List<EngineDto> getEngineDtoList(Long appId, Long engineId) {
        List<EngineDto> engineDtoList = new ArrayList<>();
        if (null != engineId) {
            Engine engine = engineMapper.selectById(engineId);
            this.getEngineDtoList(engine, engineDtoList);
        } else {
            List<Engine> engineList = engineMapper.selectList(new QueryWrapper<Engine>().lambda()
                    .eq(Engine::getAppId, appId)
                    .eq(Engine::getDelFlag, DelFlagEnum.DEL_NO.status)
                    .eq(Engine::getStatus, StatusEnum.VALID.status)
                    .eq(Engine::getBootStatus, BootStatusEnum.BOOT.status));
            if (!CollectionUtils.isEmpty(engineList)) {
                for (Engine engine : engineList) {
                    this.getEngineDtoList(engine, engineDtoList);
                }
            }
        }
        return engineDtoList;
    }

    private void getEngineDtoList(Engine engine, List<EngineDto> engineDtoList) {
        EngineDto engineDto = new EngineDto();
        engineDto.setId(engine.getId());
        engineDto.setEngineName(engine.getEngineName());
        List<EngineNode> engineNodeList = engineNodeMapper.selectList(new QueryWrapper<EngineNode>()
                .lambda()
                .eq(EngineNode::getEngineId, engine.getId())
                .eq(EngineNode::getDelFlag, DelFlagEnum.DEL_NO.status)
                .eq(EngineNode::getStatus, StatusEnum.VALID.status));
        List<EngineNodeDto> nodeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(engineNodeList)) {
            for (EngineNode engineNode : engineNodeList) {
                EngineNodeDto engineNodeDto = new EngineNodeDto();
                BeanUtils.copyProperties(engineNode, engineNodeDto);
                nodeList.add(engineNodeDto);
            }
            
        }
        engineDto.setNodeList(nodeList);
        engineDtoList.add(engineDto);
    }

    @Override
    public void engineDelete(SimpleCommonPrimaryKeyParam param) {
        Engine engine = engineMapper.selectById(param.getId());
        if (null != engine && engine.getBootStatus().equals(BootStatusEnum.BOOT.status)) {
            throw new ApiException(ErrorCodeEnum.ENGINE_BOOT_DELETE_EXCEPTION);
        }
        if (null != engine) {
            engine.setDelFlag(1);
            engineMapper.updateById(engine);
        }
    }


}
