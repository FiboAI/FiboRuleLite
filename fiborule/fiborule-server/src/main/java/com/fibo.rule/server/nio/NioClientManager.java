package com.fibo.rule.server.nio;

import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.common.dto.FiboFieldDto;
import com.fibo.rule.common.dto.FiboNioDto;
import com.fibo.rule.common.enums.NioOperationTypeEnum;
import com.fibo.rule.common.enums.NioTypeEnum;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.common.model.ChannelInfo;
import com.fibo.rule.common.model.NodeInfo;
import com.fibo.rule.server.config.ServerProperties;
import com.fibo.rule.server.utils.JacksonUtils;
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


    //app scenes
    private static Map<Long, Set<String>> appScenesMap = new ConcurrentHashMap<>();

    //app scene nodes
    private static Map<Long, Map<String, List<FiboBeanDto>>> appSceneNodesMap = new ConcurrentHashMap<>();

    //remain app`s last client leaf info
    private static Map<Integer, Map<Byte, Map<String, NodeInfo>>> appNodeLeafClazzMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void initMap() {
        FiboNioDto fiboNioDto = new FiboNioDto();
        fiboNioDto.setId("通信id");
        fiboNioDto.setAddress("127.0.0.1:8080");
        fiboNioDto.setOperationType(NioOperationTypeEnum.INIT);
        fiboNioDto.setAppId(1L);
        fiboNioDto.setType(NioTypeEnum.REQ);
        fiboNioDto.setReleaseEngineId(1L);
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
        fiboFieldDtoIf.setType(1);
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
        fiboFieldDto.setType(2);
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
        fiboFieldDtoSwitch.setType(3);
        fiboFieldDtoListSwitch.add(fiboFieldDtoSwitch);
        fiboBeanDtoSwitch.setFiboFieldDtoList(fiboFieldDtoListSwitch);
        fiboBeanDtoList.add(fiboBeanDtoSwitch);

        sceneBeansMap.put("mock场景", fiboBeanDtoList);
        fiboNioDto.setSceneBeansMap(sceneBeansMap);

        Set<String> sceneList = new HashSet<>();
        sceneList.add("mock场景");
        appScenesMap.put(1L, sceneList);
        appSceneNodesMap.put(1L, sceneBeansMap);


    }

    @Resource
    private ServerProperties properties;

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
            Set<String> scenes = appScenesMap.get(app);
            Map<String, List<FiboBeanDto>> sceneNodesMap = appSceneNodesMap.get(app);
            if (!CollectionUtils.isEmpty(scenes) && !CollectionUtils.isEmpty(sceneNodesMap)) {
                appScenesMap.remove(app);
                for (String scene : scenes) {
                    List<FiboBeanDto> fiboBeanDtos = sceneNodesMap.get(scene);
                    if (!CollectionUtils.isEmpty(fiboBeanDtos)) {
                        sceneNodesMap.remove(scene);
                    }
                }
                appSceneNodesMap.remove(app);
            }
            log.info("ice client app:{} client:{} offline", app, address);
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
            log.info("ice client app:{} client:{} online", app, address);
        }
        appChannelTimeTreeMap.computeIfAbsent(app, k -> new TreeMap<>()).computeIfAbsent(now, k -> new HashSet<>()).add(channel);
        if (null != sceneBeansMap) {
            appScenesMap.put(app, sceneBeansMap.keySet());
            for (String scene : sceneBeansMap.keySet()) {
                appSceneNodesMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).put(scene, sceneBeansMap.get(scene));
//                    appNodeLeafClazzMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).computeIfAbsent(leafNodeInfo.getType(), k -> new ConcurrentHashMap<>()).put(leafNodeInfo.getClazz(), leafNodeInfo);
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
            updateModel.setReleaseEngineId(engineId);
            updateModel.setAppId(app);
            updateModel.setOperationType(NioOperationTypeEnum.UPDATE);
            updateModel.setType(NioTypeEnum.REQ);
            byte[] updateModelBytes = JacksonUtils.toJsonBytes(updateModel);
            for (Map.Entry<String, Channel> entry : clientMap.entrySet()) {
                submitRelease(entry.getValue(), updateModelBytes);
            }
        }
    }

    /**
     * submit release to update client config
     *
     * @param channel    client socket channel
     * @param modelBytes update data
     */
    private void submitRelease(Channel channel, byte[] modelBytes) {
        /*executor.submit(() -> {
            try {
                //synchronized with IceNioServerHandler client init
                synchronized (channel) {
                    NioUtils.writeModel(channel, modelBytes);
                }
            } catch (Throwable t) {
                //write failed closed client, client will get update from reconnect
                channel.close();
            }
        });*/
    }

    public synchronized List<String> getSceneList(Long appId) {
        return new ArrayList<>(appScenesMap.get(appId));
    }

    public synchronized List<FiboBeanDto> getNodesList(Long appId, String scene, Integer nodeType) {
        List<FiboBeanDto> fiboBeanDtoList = appSceneNodesMap.get(appId).get(scene);
        return fiboBeanDtoList.stream().filter(fiboBeanDto -> fiboBeanDto.getType().getType().equals(nodeType)).collect(Collectors.toList());
    }


}
