package com.fibo.rule.springboot;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.common.utils.LOGOPrinter;
import com.fibo.rule.core.client.FiboNioClient;
import com.fibo.rule.core.property.FiboRuleConfig;
import com.fibo.rule.core.property.FiboSceneConfig;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *<p>客户端初始化</p>
 *
 *@author JPX
 *@since 2022/12/5 17:43
 */
@Component
public class FiboNioClientInit implements InitializingBean, DisposableBean {

    @Resource
    private FiboRuleConfig properties;

    private FiboNioClient fiboNioClient;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(properties.getPrintBanner()) {
            LOGOPrinter.print();
        }
        Map<String, Set<String>> scenePackages;
        if(CollUtil.isNotEmpty(properties.getSceneList())) {
            scenePackages = properties.getSceneList().stream().collect(Collectors.toMap(FiboSceneConfig::getName, item -> {
                if (StrUtil.isEmpty(item.getPath())) {
                    return new HashSet<>();
                }
                return new HashSet<>(StrUtil.split(item.getPath(), StrUtil.C_COMMA));
            }));
        } else {
            scenePackages = new HashMap<>();
        }
        fiboNioClient = new FiboNioClient(properties.getApp(), properties.getServer(), properties.getMaxFrameLength(), scenePackages, properties.getInitRetryTimes(), properties.getInitRetrySleepMs());
        fiboNioClient.start();
    }

    public void destroy() {
        if (fiboNioClient != null) {
            fiboNioClient.destroy();
        }
    }
}
 