package com.fibo.rule.core.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.core.engine.EngineManager;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 *<p>客户端操作类</p>
 *
 *@author JPX
 *@since 2022/12/6 10:46
 */
@Slf4j
public final class FiboNioClientService {

    /**
     * 发布引擎
     * @param engineDtoList
     * @return
     */
    public static String releaseEngine(List<EngineDto> engineDtoList) {
        if(CollUtil.isEmpty(engineDtoList)) {
            return "引擎数据为空";
        }
        try {
            EngineManager.buildEngine(engineDtoList.get(0));
        } catch (Exception e) {
            return StrUtil.format("引擎发布失败，错误信息：{}", e.getMessage());
        }
        return null;
    }

    /**
     * 取消发布引擎
     * @param engineId
     */
    public static void unReleaseEngine(Long engineId) {
        EngineManager.removeEngine(engineId);
    }

}
