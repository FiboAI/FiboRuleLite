package com.fibo.rule.server.nio;

import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.common.dto.FiboFieldDto;
import com.fibo.rule.common.dto.FiboNioDto;
import com.fibo.rule.common.enums.NioOperationTypeEnum;
import com.fibo.rule.common.enums.NioTypeEnum;
import com.fibo.rule.common.model.ChannelInfo;
import com.fibo.rule.common.model.NodeInfo;
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

    
    private static Map<Integer, Map<String, Channel>> appAddressChannelMap = new ConcurrentHashMap<>();
    private static Map<Channel, ChannelInfo> channelInfoMap = new ConcurrentHashMap<>();
    private static Map<Integer, TreeMap<Long, Set<Channel>>> appChannelTimeTreeMap = new TreeMap<>();


    //app dto
    private static Map<Long, FiboNioDto> appNioMap = new ConcurrentHashMap<>();
    
    //app scenes
    private static Map<Long, List<String>> appScenesMap = new ConcurrentHashMap<>();
    
    //app scene nodes
    private static Map<Long, Map<String, List<FiboBeanDto>>> appSceneNodesMap = new ConcurrentHashMap<>();

    //remain app`s last client leaf info
    private static Map<Integer, Map<Byte, Map<String, NodeInfo>>> appNodeLeafClazzMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void initMap(){
        FiboNioDto fiboNioDto = new FiboNioDto();
        fiboNioDto.setId("通信id");
        fiboNioDto.setAddress("127.0.0.1:8080");
        fiboNioDto.setOperationType(NioOperationTypeEnum.INIT);
        fiboNioDto.setAppId(1);
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
        fiboBeanDtoIf.setNodeType(1);
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
        fiboBeanDto.setNodeType(0);
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
        fiboBeanDtoSwitch.setNodeType(2);
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

        appNioMap.put(1L,fiboNioDto);
        List<String> sceneList = new ArrayList<>();
        sceneList.add("mock场景");
        appScenesMap.put(1L,sceneList);
        appSceneNodesMap.put(1L,sceneBeansMap);
        
       
    }
    
    @Resource
    private ServerProperties properties;

    public static synchronized void unregister(Channel channel) {
        ChannelInfo info = channelInfoMap.get(channel);
        if (info != null) {
            String address = info.getAddress();
            Long originTime = info.getLastUpdateTime();
            int app = info.getApp();
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

            //remove client leaf class
//            Map<String, Map<String, NodeInfo>> addressNodeClassMap = appAddressLeafClazzMap.get(app);
            /*if (!CollectionUtils.isEmpty(addressNodeClassMap)) {
                addressNodeClassMap.remove(address);
                if (CollectionUtils.isEmpty(addressNodeClassMap)) {
                    //not have any available client, but remain last appNodeLeafClazzMap
                    appAddressLeafClazzMap.remove(app);
                } else {
                    //reorganize app leaf class map
                    Map<Byte, Map<String, NodeInfo>> nodeLeafClazzMapTmp = new ConcurrentHashMap<>();
                    for (Map<String, NodeInfo> leafTypeClassMap : addressNodeClassMap.values()) {
                        for (NodeInfo leafNodeInfo : leafTypeClassMap.values()) {
                            nodeLeafClazzMapTmp.computeIfAbsent(leafNodeInfo.getType(), k -> new ConcurrentHashMap<>()).put(leafNodeInfo.getClazz(), leafNodeInfo);
                        }
                    }
                    appNodeLeafClazzMap.put(app, nodeLeafClazzMapTmp);
                }
            }*/
            log.info("ice client app:{} client:{} offline", app, address);
        }
    }

    /**
     * clean client channel with expire time
     *
     * @param expireTime less time
     */
    public synchronized void cleanClientChannel(long expireTime) {
        for (Map.Entry<Integer, TreeMap<Long, Set<Channel>>> channelTimeTreeEntry : appChannelTimeTreeMap.entrySet()) {
            TreeMap<Long, Set<Channel>> treeMap = channelTimeTreeEntry.getValue();
            if (treeMap != null) {
                SortedMap<Long, Set<Channel>> cleanMap = treeMap.headMap(expireTime);
                if (!CollectionUtils.isEmpty(cleanMap)) {
                    Collection<Set<Channel>> cleanChannelSetList = cleanMap.values();
                    for (Set<Channel> cleanChannelSet : cleanChannelSetList) {
                        if (!CollectionUtils.isEmpty(cleanChannelSet)) {
                            for (Channel cleanChannel : cleanChannelSet) {
                                unregister(cleanChannel);
                            }
                        }
                    }
                }
            }
        }
    }

    public static synchronized void register(int app, Channel channel, String address) {
        //slap
        register(app, channel, address, null);
    }

    public static synchronized void register(int app, Channel channel, String address, List<NodeInfo> leafNodes) {
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
        if (!CollectionUtils.isEmpty(leafNodes)) {
            for (NodeInfo leafNodeInfo : leafNodes) {
//                appNioMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).computeIfAbsent(address, k -> new ConcurrentHashMap<>()).put(leafNodeInfo.getClazz(), leafNodeInfo);
                appNodeLeafClazzMap.computeIfAbsent(app, k -> new ConcurrentHashMap<>()).computeIfAbsent(leafNodeInfo.getType(), k -> new ConcurrentHashMap<>()).put(leafNodeInfo.getClazz(), leafNodeInfo);
            }
        }
    }

    public synchronized Set<String> getRegisterClients(int app) {
        Map<String, Channel> clientInfoMap = appAddressChannelMap.get(app);
        if (!CollectionUtils.isEmpty(clientInfoMap)) {
            return Collections.unmodifiableSet(clientInfoMap.keySet());
        }
        return null;
    }

    public synchronized Channel getClientSocketChannel(int app, String address) {
        if (address != null) {
            Map<String, Channel> clientMap = appAddressChannelMap.get(app);
            if (CollectionUtils.isEmpty(clientMap)) {
                return null;
            }
            return clientMap.get(address);
        }
        TreeMap<Long, Set<Channel>> treeMap = appChannelTimeTreeMap.get(app);
        if (CollectionUtils.isEmpty(treeMap)) {
            return null;
        }
        Set<Channel> socketChannels = treeMap.lastEntry().getValue();
        if (CollectionUtils.isEmpty(socketChannels)) {
            return null;
        }
        return socketChannels.iterator().next();
    }

    public synchronized Map<String, NodeInfo> getLeafTypeClasses(int app, byte type) {
        Map<Byte, Map<String, NodeInfo>> addressClazzInfoMap = appNodeLeafClazzMap.get(app);
        if (!CollectionUtils.isEmpty(addressClazzInfoMap)) {
            Map<String, NodeInfo> clazzInfoMap = addressClazzInfoMap.get(type);
            if (!CollectionUtils.isEmpty(clazzInfoMap)) {
                return clazzInfoMap;
            }
        }
        return null;
    }

    public synchronized void cleanChannelCache() {
        appNodeLeafClazzMap = new ConcurrentHashMap<>();
        appNioMap = new ConcurrentHashMap<>();
        appChannelTimeTreeMap = new ConcurrentHashMap<>();
        appAddressChannelMap = new ConcurrentHashMap<>();
        try {
            if (!CollectionUtils.isEmpty(channelInfoMap)) {
                for (Channel channel : channelInfoMap.keySet()) {
                    channel.close();
                }
            }
        } catch (Exception e) {
            //ignore
        }
        channelInfoMap = new ConcurrentHashMap<>();
    }

    public NodeInfo getNodeInfo(int app, String address, String clazz, Byte type) {
        if (address == null) {
            return getNodeInfoFromAllClient(app, clazz, type);
        }
//        Map<String, Map<String, NodeInfo>> addressLeafClazzMap = appNioMap.get(app);
        /*if (addressLeafClazzMap == null) {
            return getNodeInfoFromAllClient(app, clazz, type);
        }
        Map<String, NodeInfo> clazzMap = addressLeafClazzMap.get(address);
        if (clazzMap == null) {
            return getNodeInfoFromAllClient(app, clazz, type);
        }*/
//        return nodeInfoCopy(clazzMap.get(clazz));
        return null;
    }

    private NodeInfo getNodeInfoFromAllClient(int app, String clazz, Byte type) {
        Map<Byte, Map<String, NodeInfo>> nodeLeafClazzMap = appNodeLeafClazzMap.get(app);
        if (nodeLeafClazzMap == null) {
            return null;
        }
        Map<String, NodeInfo> clazzMap = nodeLeafClazzMap.get(type);
        if (clazzMap == null) {
            return null;
        }
        return nodeInfoCopy(clazzMap.get(clazz));
    }

    private static NodeInfo nodeInfoCopy(NodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return null;
        }
        NodeInfo result = new NodeInfo();
        result.setName(nodeInfo.getName());
        result.setClazz(nodeInfo.getClazz());
        result.setType(nodeInfo.getType());
        result.setDesc(nodeInfo.getDesc());
        result.setIceFields(fieldInfoListCopy(nodeInfo.getIceFields()));
        result.setHideFields(fieldInfoListCopy(nodeInfo.getHideFields()));
        return result;
    }

    private static List<NodeInfo.IceFieldInfo> fieldInfoListCopy(List<NodeInfo.IceFieldInfo> fieldInfoList) {
        if (fieldInfoList == null) {
            return null;
        }
        List<NodeInfo.IceFieldInfo> results = new ArrayList<>(fieldInfoList.size());
        for (NodeInfo.IceFieldInfo fieldInfo : fieldInfoList) {
            NodeInfo.IceFieldInfo result = new NodeInfo.IceFieldInfo();
            result.setField(fieldInfo.getField());
            result.setValue(fieldInfo.getValue());
            result.setType(fieldInfo.getType());
            result.setName(fieldInfo.getName());
            result.setDesc(fieldInfo.getDesc());
            results.add(result);
        }
        return results;
    }


    public synchronized List<String> getSceneList(Long appId) {
        return appScenesMap.get(appId);
    }

    public synchronized List<FiboBeanDto> getNodesList(Long appId, String scene, Integer nodeType) {
        List<FiboBeanDto> fiboBeanDtoList = appSceneNodesMap.get(appId).get(scene);
        return fiboBeanDtoList.stream().filter(fiboBeanDto -> fiboBeanDto.getNodeType().equals(nodeType)).collect(Collectors.toList());
    }
    
    
}
