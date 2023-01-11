package com.fibo.rule.server.nio;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.common.dto.FiboFieldDto;
import com.fibo.rule.common.dto.FiboNioDto;
import com.fibo.rule.common.enums.FieldTypeEnum;
import com.fibo.rule.common.enums.NioOperationTypeEnum;
import com.fibo.rule.common.enums.NioTypeEnum;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.common.model.ChannelInfo;
import com.fibo.rule.common.utils.FiboNioUtils;
import com.fibo.rule.server.config.ServerProperties;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


@Slf4j
@Service
public final class NioClientManager {

    //app address channel
    private static Map<Long, Map<String, Channel>> appAddressChannelMap = new ConcurrentHashMap<>();
    //channel channelInfo
    private static Map<Channel, ChannelInfo> channelInfoMap = new ConcurrentHashMap<>();
    //app lastUpdateTime set<Channel>
    private static Map<Long, TreeMap<Long, Set<Channel>>> appChannelTimeTreeMap = new TreeMap<>();
    //app address scene nodeClazz
    private static Map<Long, Map<String, Map<String, Map<String, FiboBeanDto>>>> appAddressLeafClazzMap = new ConcurrentHashMap<>();
    //app scene type nodeClazz
    private static Map<Long, Map<String, Map<Integer, Map<String, FiboBeanDto>>>> appNodeLeafClazzMap = new ConcurrentHashMap<>();

    @Resource
    private ServerProperties properties;

    @PostConstruct
    private void initMap() {

        FiboNioDto fiboNioDto = new FiboNioDto();
        fiboNioDto.setId("通信id");
        fiboNioDto.setAddress("127.0.0.1:8080");
        fiboNioDto.setOperationType(NioOperationTypeEnum.INIT);
        fiboNioDto.setAppId(1L);
        fiboNioDto.setType(NioTypeEnum.REQ);
        fiboNioDto.setUnReleaseEngineId(1L);
        Map<String, List<FiboBeanDto>> sceneBeansMap = new ConcurrentHashMap<>();

        //mock节点信息
        List<FiboBeanDto> fiboBeanDtoList = new ArrayList<>();
        FiboBeanDto fiboBeanDtoIf = new FiboBeanDto();
        fiboBeanDtoIf.setName("自定义节点名称");
        fiboBeanDtoIf.setDesc("节点名称描述");
        fiboBeanDtoIf.setClazzName("TestA");
        fiboBeanDtoIf.setNodeClazz("com.fibo.rule.test.TestA");

        //节点类型 if
        fiboBeanDtoIf.setType(NodeTypeEnum.IF);
        List<FiboFieldDto> fiboFieldDtoListIf = new ArrayList<>();
        FiboFieldDto fiboFieldDtoIf = new FiboFieldDto();
        fiboFieldDtoIf.setName("自定义属性名称");
        fiboFieldDtoIf.setDesc("自定义属性名称描述");
        fiboFieldDtoIf.setFieldName("valueA");
        //数据类型
        fiboFieldDtoIf.setType(FieldTypeEnum.NUMBER);
        fiboFieldDtoListIf.add(fiboFieldDtoIf);
        fiboBeanDtoIf.setFiboFieldDtoList(fiboFieldDtoListIf);
        fiboBeanDtoList.add(fiboBeanDtoIf);

        FiboBeanDto fiboBeanDto = new FiboBeanDto();
        fiboBeanDto.setName("自定义节点名称");
        fiboBeanDto.setDesc("节点名称描述");
        fiboBeanDto.setClazzName("TestB");
        fiboBeanDto.setNodeClazz("com.fibo.rule.test.TestB");
        //normal
        fiboBeanDto.setType(NodeTypeEnum.NORMAL);
        List<FiboFieldDto> fiboFieldDtoList = new ArrayList<>();
        FiboFieldDto fiboFieldDto = new FiboFieldDto();
        fiboFieldDto.setName("自定义属性名称");
        fiboFieldDto.setDesc("自定义属性名称描述");
        fiboFieldDto.setFieldName("valueB");
        //数据类型
        fiboFieldDto.setType(FieldTypeEnum.STRING);
        fiboFieldDtoList.add(fiboFieldDto);
        fiboBeanDto.setFiboFieldDtoList(fiboFieldDtoList);
        fiboBeanDtoList.add(fiboBeanDto);

        FiboBeanDto fiboBeanDtoSwitch = new FiboBeanDto();
        fiboBeanDtoSwitch.setName("自定义节点c的名称");
        fiboBeanDtoSwitch.setDesc("自定义节点c的名称的描述");
        fiboBeanDtoSwitch.setClazzName("TestC");
        fiboBeanDtoSwitch.setNodeClazz("com.fibo.rule.test.TestC");
        //switch
        fiboBeanDtoSwitch.setType(NodeTypeEnum.SWITCH);
        List<FiboFieldDto> fiboFieldDtoListSwitch = new ArrayList<>();
        FiboFieldDto fiboFieldDtoSwitch = new FiboFieldDto();
        fiboFieldDtoSwitch.setName("自定义属性名称");
        fiboFieldDtoSwitch.setDesc("自定义属性名称描述");
        fiboFieldDtoSwitch.setFieldName("valueC");
        fiboFieldDtoSwitch.setType(FieldTypeEnum.STRING);
        fiboFieldDtoListSwitch.add(fiboFieldDtoSwitch);
        fiboBeanDtoSwitch.setFiboFieldDtoList(fiboFieldDtoListSwitch);
        Map<String, String> branchMap = new HashMap<>();
        branchMap.put("String", "字符串");
        branchMap.put("Number", "数字类型");
        branchMap.put("Date", "时间类型");
        fiboBeanDtoSwitch.setBranchMap(branchMap);
        fiboBeanDtoList.add(fiboBeanDtoSwitch);

        sceneBeansMap.put("mock场景", fiboBeanDtoList);
        fiboNioDto.setSceneBeansMap(sceneBeansMap);

        Set<String> sceneList = new HashSet<>();
        sceneList.add("mock场景");
//        appScenesMap.put(1L, sceneList);
//        appSceneNodesMap.put(1L, sceneBeansMap);

        if (CollectionUtil.isNotEmpty(sceneBeansMap)) {
            for (String scene : sceneBeansMap.keySet()) {
                List<FiboBeanDto> fiboBeanList = sceneBeansMap.get(scene);
                for (FiboBeanDto fiboBean : fiboBeanList) {
                    appNodeLeafClazzMap.computeIfAbsent(1L, k -> new ConcurrentHashMap<>()).computeIfAbsent(scene, k -> new ConcurrentHashMap<>()).computeIfAbsent(fiboBean.getType().getType(), k -> new ConcurrentHashMap<>()).put(fiboBean.getNodeClazz(), fiboBean);
                }
            }
        }
    }


    public static synchronized void unregister(Channel channel) {
        ChannelInfo info = channelInfoMap.get(channel);
        if (info != null) {
            String address = info.getAddress();
            Long originTime = info.getLastUpdateTime();
            Long app = info.getApp();
            channelInfoMap.remove(channel);
            Map<String, Channel> channelMap = appAddressChannelMap.get(app);
            if (!CollectionUtils.isEmpty(channelMap)) {
                channelMap.remove(address);
                if (CollectionUtils.isEmpty(channelMap)) {
                    appAddressChannelMap.remove(app);
                }
            }
            TreeMap<Long, Set<Channel>> treeMap = appChannelTimeTreeMap.get(app);
            if (!CollectionUtils.isEmpty(treeMap)) {
                Set<Channel> channels = treeMap.get(originTime);
                if (!CollectionUtils.isEmpty(channels)) {
                    channels.remove(channel);
                }
                if (CollectionUtils.isEmpty(channels)) {
                    treeMap.remove(originTime);
                }
                if (CollectionUtils.isEmpty(treeMap)) {
                    appChannelTimeTreeMap.remove(app);
                }
            }
            //remove client fobiBean class
            Map<String, Map<String, Map<String, FiboBeanDto>>> addressNodesMap = appAddressLeafClazzMap.get(app);
            if (!CollectionUtils.isEmpty(addressNodesMap)) {
                addressNodesMap.remove(address);
                if (CollectionUtils.isEmpty(addressNodesMap)) {
                    //not have any available client
                    appAddressLeafClazzMap.remove(app);
                    appNodeLeafClazzMap = new ConcurrentHashMap<>();
                } else {
                    //reorganize app leaf class map
                    Map<Long, Map<String, Map<Integer, Map<String, FiboBeanDto>>>> appNodeLeafClazzMapTemp = new ConcurrentHashMap<>();
                    for (Map<String, Map<String, FiboBeanDto>> leafTypeClassMap : addressNodesMap.values()) {
                        for (String scene : leafTypeClassMap.keySet()) {
                            Map<String, FiboBeanDto> fiboBeanMap = leafTypeClassMap.get(scene);
                            for (FiboBeanDto fiboBeanDto : fiboBeanMap.values()) {
                                fiboBeanDto.getType();
                                appNodeLeafClazzMapTemp.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).computeIfAbsent(scene, k -> new ConcurrentHashMap<>()).computeIfAbsent(fiboBeanDto.getType().getType(), k -> new ConcurrentHashMap<>()).put(fiboBeanDto.getNodeClazz(), fiboBeanDto);
                            }
                        }
                    }
                    appNodeLeafClazzMap = appNodeLeafClazzMapTemp;
                }
            }
            log.info("client app:{} client:{} offline", app, address);
        }
    }

    public static synchronized void register(Long app, Channel channel, String address) {
        //slap
        register(app, channel, address, null);
    }

    public static synchronized void register(Long app, Channel channel, String address, Map<String, List<FiboBeanDto>> sceneBeansMap) {
        long now = System.currentTimeMillis();
        ChannelInfo info = channelInfoMap.get(channel);
        if (info != null) {
            Long originTime = info.getLastUpdateTime();
            TreeMap<Long, Set<Channel>> socketTimeTreeMap = appChannelTimeTreeMap.get(app);
            if (socketTimeTreeMap != null) {
                Set<Channel> originTimeObject = socketTimeTreeMap.get(originTime);
                if (originTimeObject != null) {
                    originTimeObject.remove(channel);
                }
                if (CollectionUtils.isEmpty(originTimeObject)) {
                    socketTimeTreeMap.remove(originTime);
                }
            }
            info.setLastUpdateTime(now);
        } else {
            appAddressChannelMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).put(address, channel);
            channelInfoMap.put(channel, new ChannelInfo(app, address, now));
            log.info("client app:{} client:{} online", app, address);
        }
        appChannelTimeTreeMap.computeIfAbsent(app, k -> new TreeMap<>()).computeIfAbsent(now, k -> new HashSet<>()).add(channel);
        if (CollectionUtil.isNotEmpty(sceneBeansMap)) {
            for (String scene : sceneBeansMap.keySet()) {
                List<FiboBeanDto> fiboBeanDtoList = sceneBeansMap.get(scene);
                for (FiboBeanDto fiboBeanDto : fiboBeanDtoList) {
                    appAddressLeafClazzMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).computeIfAbsent(address, k -> new ConcurrentHashMap<>()).computeIfAbsent(scene, k -> new ConcurrentHashMap<>()).put(fiboBeanDto.getNodeClazz(), fiboBeanDto);
                    appNodeLeafClazzMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).computeIfAbsent(scene, k -> new ConcurrentHashMap<>()).computeIfAbsent(fiboBeanDto.getType().getType(), k -> new ConcurrentHashMap<>()).put(fiboBeanDto.getNodeClazz(), fiboBeanDto);
                }
            }
        }
    }

    public synchronized Set<String> getRegisterClients(Long app) {
        Map<String, Channel> clientInfoMap = appAddressChannelMap.get(app);
        if (!CollectionUtils.isEmpty(clientInfoMap)) {
            return Collections.unmodifiableSet(clientInfoMap.keySet());
        }
        return null;
    }

    /**
     * 引擎发布和取消发布
     */
    public void release(Long app, List<EngineDto> engineDtoList, Long engineId) {
        Map<String, Channel> clientMap = appAddressChannelMap.get(app);
        if (!CollectionUtils.isEmpty(clientMap)) {
            FiboNioDto updateModel = new FiboNioDto();
            updateModel.setEngineDtoList(engineDtoList);
            updateModel.setUnReleaseEngineId(engineId);
            updateModel.setAppId(app);
            updateModel.setType(NioTypeEnum.REQ);

            if (null == engineId) {
                updateModel.setOperationType(NioOperationTypeEnum.RELEASE_ENGINE);
            } else {
                updateModel.setOperationType(NioOperationTypeEnum.UNRELEASE_ENGINE);
            }
//            byte[] updateModelBytes = JSON.toJSONBytes(updateModel);
            for (Map.Entry<String, Channel> entry : clientMap.entrySet()) {
                submitRelease(entry.getValue(), updateModel);
            }
        }
    }

    /**
     * submit release to update client config
     *
     * @param channel     client socket channel
     * @param updateModel update data
     */
    private void submitRelease(Channel channel, FiboNioDto updateModel) {
        try {
            //synchronized with NioServerHandler client init
            synchronized (channel) {
                FiboNioUtils.writeNioModel(channel, updateModel);
            }
        } catch (Throwable t) {
            //write failed closed client, client will get update from reconnect
            channel.close();
        }
    }

    public synchronized List<String> getSceneList(Long appId) {
        Map<String, Map<Integer, Map<String, FiboBeanDto>>> sceneMap = appNodeLeafClazzMap.get(appId);
        if(CollectionUtil.isEmpty(sceneMap)) {
            return new ArrayList<>();
        }
        Set<String> sceneSet = sceneMap.keySet();
        return new ArrayList<>(sceneSet);
    }

    public synchronized List<FiboBeanDto> getNodesList(Long appId, String scene, Integer nodeType) {
        Map<String, Map<Integer, Map<String, FiboBeanDto>>> sceneMap = appNodeLeafClazzMap.get(appId);
        if (CollectionUtils.isEmpty(sceneMap)) {
            return new ArrayList<>();
        }
        Map<Integer, Map<String, FiboBeanDto>> beanTypeMap = sceneMap.get(scene);
        if (CollectionUtils.isEmpty(beanTypeMap)) {
            return new ArrayList<>();
        }
        Map<String, FiboBeanDto> fiboBeanMap = beanTypeMap.get(nodeType);
        if (CollectionUtils.isEmpty(fiboBeanMap)) {
            return new ArrayList<>();
        }
        List<FiboBeanDto> fiboBeanList = new ArrayList<>();
        for (FiboBeanDto fiboBean : fiboBeanMap.values()) {
            fiboBeanList.add(fiboBean);
        }
        return fiboBeanList;
    }

    public synchronized FiboBeanDto getNodeByClazz(Long appId, String scene, String clazzName) {
        Map<String, Map<Integer, Map<String, FiboBeanDto>>> sceneMap = appNodeLeafClazzMap.get(appId);
        if (CollectionUtils.isEmpty(sceneMap)) {
            return null;
        }
        Map<Integer, Map<String, FiboBeanDto>> beanTypeMap = sceneMap.get(scene);
        if (CollectionUtils.isEmpty(beanTypeMap)) {
            return null;
        }
        for (Map<String, FiboBeanDto> fiboBeanMap : beanTypeMap.values()) {
            if(ObjectUtil.isNotNull(fiboBeanMap.get(clazzName))) {
                return fiboBeanMap.get(clazzName);
            }
        }
        return null;
    }


}
