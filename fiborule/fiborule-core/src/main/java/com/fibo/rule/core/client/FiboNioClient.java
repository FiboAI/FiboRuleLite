package com.fibo.rule.core.client;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.fibo.rule.common.constant.EngineConstant;
import com.fibo.rule.common.dto.EngineDto;
import com.fibo.rule.common.dto.FiboBeanDto;
import com.fibo.rule.common.dto.FiboFieldDto;
import com.fibo.rule.common.enums.FieldTypeEnum;
import com.fibo.rule.common.enums.NodeTypeEnum;
import com.fibo.rule.core.annotation.FiboBean;
import com.fibo.rule.core.annotation.FiboField;
import com.fibo.rule.core.engine.EngineManager;
import com.fibo.rule.core.exception.FiboBeanNameRepeatException;
import com.fibo.rule.core.exception.FiboFieldNameRepeatException;
import com.fibo.rule.core.node.FiboIfNode;
import com.fibo.rule.core.node.FiboSwitchNode;
import com.fibo.rule.core.util.FiboAddressUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 *<p>client类</p>
 *
 *@author JPX
 *@since 2022/12/5 14:04
 */
@Slf4j
public final class FiboNioClient {
    /*base config*/
    private final Long app;
    private String server;
    /*derive*/
    private String host;
    private int port;
    private Bootstrap bootstrap;
    private EventLoopGroup worker;
    private final String fiboAddress;
    /**场景对应的节点定义信息*/
    private Map<String, List<FiboBeanDto>> fiboBeanMap;

    //start error cause
    private volatile Throwable startCause;
    //destroy sign
    private volatile boolean destroy = false;
    //start data ready from ice server
    private volatile boolean startDataReady = false;
    //ice client started
    private volatile boolean started = false;
    private final Object startDataLock = new Object();
    private final Object startedLock = new Object();
    //start data
    private volatile List<EngineDto> startData;
    /*with default*/
    private final int maxFrameLength;
    private final int initRetryTimes;
    private final int initRetrySleepMs;
    /*default*/
    private static final int DEFAULT_MAX_FRAME_LENGTH = 16 * 1024 * 1024; //16M
    private static final int DEFAULT_INIT_RETRY_TIMES = 3;
    private static final int DEFAULT_INIT_RETRY_SLEEP_MS = 2000;

    public FiboNioClient(Long app, String server, int maxFrameLength, Map<String, Set<String>> scenePackages, int initRetryTimes, int initRetrySleepMs) throws Exception {
        this.initRetryTimes = initRetryTimes < 0 ? DEFAULT_INIT_RETRY_TIMES : initRetryTimes;
        this.initRetrySleepMs = initRetrySleepMs < 0 ? DEFAULT_INIT_RETRY_SLEEP_MS : initRetrySleepMs;
        this.app = app;
        this.maxFrameLength = maxFrameLength;
        this.fiboAddress = FiboAddressUtils.getAddress(app);
        this.setServer(server);
        scanSceneNodes(scenePackages);
        prepare();
    }

    public FiboNioClient(Long app, String server, Map<String, Set<String>> scenePackages) throws Exception {
        this(app, server, DEFAULT_MAX_FRAME_LENGTH, scenePackages, DEFAULT_INIT_RETRY_TIMES, DEFAULT_INIT_RETRY_SLEEP_MS);
    }

    public FiboNioClient(Long app, String server) throws Exception {
        this(app, server, Collections.emptyMap());
    }

    private void setServer(String server) {
        this.server = server;
        setServerHostPort(server);
    }

    private void setServerHostPort(String hostPort) {
        String[] serverHostPort = hostPort.split(":");
        try {
            this.host = serverHostPort[0];
            this.port = Integer.parseInt(serverHostPort[1]);
        } catch (Exception e) {
            throw new RuntimeException("服务器地址配置错误:" + hostPort);
        }
    }

    private void prepare() {
        worker = new NioEventLoopGroup();
        bootstrap = new Bootstrap().group(worker)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ch.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
                        ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(maxFrameLength, 0, 4, 0, 4));
                        ch.pipeline().addLast(new FiboNioClientHandler(app, FiboNioClient.this));
                    }
                });
    }

    public void destroy() {
        startCause = null;
        startDataReady = false;
        started = false;
        destroy = true;
        if (worker != null) {
            worker.shutdownGracefully();
        }
    }

    public boolean isDestroy() {
        return destroy;
    }

    /**
     * wait for started
     */
    public void waitStarted() {
        if (!started) {
            synchronized (startedLock) {
                if (!started) {
                    try {
                        startedLock.wait();
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
            }
        }
    }

    /**
     * 启动fibo客户端
     * 1.连接服务端
     * 2.从服务端获取数据
     * 3.初始化启动数据
     * @throws Exception
     */
    public void start() throws Exception {
        destroy = false;
        long start = System.currentTimeMillis();
        for (int i = 0; i < initRetryTimes; i++) {
            try {
                new Thread(() -> {
                    try {
                        bootstrap.connect(host, port).sync();
                    } catch (Throwable t) {
                        if (!this.startDataReady) {
                            //ice client not started, just shutdown it
                            if (worker != null) {
                                worker.shutdownGracefully();
                            }
                            startCause = t;
                            synchronized (startDataLock) {
                                startDataLock.notifyAll();
                            }
                        }
                    }
                }).start();
                //等待客户端启动数据获取成功
                synchronized (startDataLock) {
                    if (!startDataReady && startCause == null) {
                        startDataLock.wait();
                    }
                }
                if (startDataReady) {
                    //准备初始化
                    break;
                }
                if (i < initRetryTimes - 1) {
                    Thread.sleep(initRetrySleepMs);
                }
            } catch (Exception e) {
                log.error("客户端初始化失败重试:{}, 延迟:{}ms", i, initRetrySleepMs, e);
                startCause = e;
                Thread.sleep(initRetrySleepMs);
            }
        }
        if (!this.startDataReady) {
            this.destroy();
            throw new RuntimeException("连接服务端失败:" + server, startCause);
        }
        //start data ready, starting cache
        EngineManager.buildEngines(startData);
        startData = null;
        synchronized (startedLock) {
            //started
            started = true;
            startedLock.notifyAll();
        }
        log.info("客户端启动成功 app:{} 地址:{} 耗时:{}ms", app, fiboAddress, System.currentTimeMillis() - start);
    }

    /**
     * 从服务端获取初始化数据完成
     * @param initData 初始数据
     */
    public void initDataReady(List<EngineDto> initData) {
        if (started) {
            //已启动，构建引擎数据
            EngineManager.buildEngines(initData);
        } else {
            synchronized (startedLock) {
                if (started) {
                    EngineManager.buildEngines(initData);
                    return;
                }
                if (startDataReady) {
                    try {
                        //启动数据准备完成，待启动
                        startedLock.wait();
                    } catch (InterruptedException e) {
                        //忽略
                    }
                    //已启动，构建引擎数据
                    EngineManager.buildEngines(initData);
                } else {
                    synchronized (startDataLock) {
                        //首次启动数据准备完成
                        this.startData = initData;
                        startDataReady = true;
                        startDataLock.notifyAll();
                    }
                }
            }
        }
    }

    /**
     * 重新连接服务器
     * 2秒一次
     * @throws InterruptedException e
     */
    public void reconnect() throws InterruptedException {
        if (!destroy) {
            ChannelFuture cf = bootstrap.connect(host, port);
            cf.addListener((ChannelFutureListener) future -> {
                if (!future.isSuccess()) {
                    //the reconnection handed over by backend thread
                    future.channel().eventLoop().schedule(() -> {
                        try {
                            reconnect();
                        } catch (Exception e) {
                            log.warn("客户端重新连接失败", e);
                        }
                    }, 2, TimeUnit.SECONDS);
                } else {
                    log.info("客户端已重新连接");
                }
            });
            cf.channel().closeFuture().sync();
        }
    }

    /**
     * 扫描场景下包中的节点
     * @param scenePackages 需要扫描的场景和包路径
     * @throws IOException 异常
     */
    private void scanSceneNodes(Map<String, Set<String>> scenePackages) throws Exception {
        fiboBeanMap = new HashMap<>();
        if (scenePackages == null || scenePackages.isEmpty()) {
            //默认场景
            List<FiboBeanDto> fiboBeanDtoList = scanScenePackages(EngineConstant.DEFAULT, null);
            fiboBeanMap.put(EngineConstant.DEFAULT, fiboBeanDtoList);
        } else {
            for (String scene : scenePackages.keySet()) {
                List<FiboBeanDto> fiboBeanDtoList = scanScenePackages(scene, scenePackages.get(scene));
                fiboBeanMap.put(scene, fiboBeanDtoList);
            }
        }
        log.info("节点信息扫描完成");
    }

    private List<FiboBeanDto> scanScenePackages(String scene, Set<String> packages) throws Exception {
        long start = System.currentTimeMillis();
        Set<Class<?>> nodeClasses;
        if (EngineConstant.DEFAULT.equals(scene)) {
            nodeClasses = FiboNodeScanner.scanPackage(null);
        } else {
            nodeClasses = new HashSet<>();
            for (String packageName : packages) {
                nodeClasses.addAll(FiboNodeScanner.scanPackage(packageName));
            }
        }
        log.info("节点信息扫描, 场景:[{}-{}] {}ms 数量:{}", scene, packages, System.currentTimeMillis() - start, nodeClasses.size());
        List<FiboBeanDto> fiboBeanList = new ArrayList<>();
        if (nodeClasses.isEmpty()) {
            return fiboBeanList;
        }
        Set<String> beanNameSet = new HashSet<>();
        Set<String> fieldNameSet = new HashSet<>();
        for (Class<?> nodeClass : nodeClasses) {
            FiboBeanDto fiboBean = new FiboBeanDto();
            fiboBean.setClazzName(nodeClass.getSimpleName());
            fiboBean.setNodeClazz(nodeClass.getName());
            //获取@FiboBean注解
            FiboBean beanAnnotation = nodeClass.getAnnotation(FiboBean.class);
            if (ObjectUtil.isNotNull(beanAnnotation)) {
                fiboBean.setName(beanAnnotation.name());
                fiboBean.setDesc(beanAnnotation.desc());
            }
            //注解name为空，则设置为类名
            if(StrUtil.isEmpty(fiboBean.getName())) {
                fiboBean.setName(fiboBean.getClazzName());
            }
            //判断名称重复
            if(beanNameSet.contains(fiboBean.getName())) {
                throw new FiboBeanNameRepeatException(StrUtil.format("场景[{}], FiboBean[{}]name重复", scene, fiboBean.getName()));
            }
            beanNameSet.add(fiboBean.getName());
            //获取所有属性
            Field[] nodeFields = nodeClass.getDeclaredFields();
            List<FiboFieldDto> fiboFieldList = new ArrayList<>();
            fieldNameSet.clear();
            for (Field field : nodeFields) {
                FiboField fieldAnnotation = field.getAnnotation(FiboField.class);
                if(ObjectUtil.isNull(fieldAnnotation)) {
                    continue;
                }
                String name = StrUtil.isEmpty(fieldAnnotation.name()) ? field.getName() : fieldAnnotation.name();
                if(fieldNameSet.contains(name)) {
                    throw new FiboFieldNameRepeatException(StrUtil.format("场景[{}], FiboBean[{}], FiboField[{}]name重复", scene, fiboBean.getName(), name));
                }
                FiboFieldDto fiboField = new FiboFieldDto();
                fiboField.setName(name);
                fiboField.setFieldName(field.getName());
                fiboField.setDesc(fieldAnnotation.desc());
                fiboField.setType(fieldAnnotation.type());
                if(FieldTypeEnum.DEFAULT.equals(fiboField.getType())) {
                    fiboField.setType(FieldTypeEnum.getFieldTypeByClazz(field.getType()));
                }
                fiboFieldList.add(fiboField);
            }
            fiboBean.setFiboFieldDtoList(fiboFieldList);
            //设置节点类型
            if(FiboIfNode.class.isAssignableFrom(nodeClass)) {
                fiboBean.setType(NodeTypeEnum.IF);
            } else if(FiboSwitchNode.class.isAssignableFrom(nodeClass)) {
                fiboBean.setType(NodeTypeEnum.SWITCH);
                FiboSwitchNode fiboSwitchNode = (FiboSwitchNode) nodeClass.newInstance();
                fiboBean.setBranchMap(fiboSwitchNode.switchBranchs());
            } else {
                fiboBean.setType(NodeTypeEnum.NORMAL);
            }
            if(nodeFields.length == 0) {
                EngineManager.addNode(fiboBean.getName(), nodeClass, fiboBean.getType());
            }
            fiboBeanList.add(fiboBean);
        }
        return fiboBeanList;
    }

    public String getFiboAddress() {
        return fiboAddress;
    }

    public Map<String, List<FiboBeanDto>> getFiboBeanMap() {
        return fiboBeanMap;
    }
}