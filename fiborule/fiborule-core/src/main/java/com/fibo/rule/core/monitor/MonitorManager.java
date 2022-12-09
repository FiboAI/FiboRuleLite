package com.fibo.rule.core.monitor;

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.property.FiboRuleConfig;
import com.fibo.rule.core.util.BoundedPriorityBlockingQueue;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *<p>监控类元数据，打印执行器类</p>
 *
 *@author JPX
 *@since 2022/11/29 18:05
 */
@Slf4j
public class MonitorManager {

	private static MonitorManager instance;

	public static MonitorManager loadInstance(FiboRuleConfig config){
		if (ObjectUtil.isNull(instance)){
			instance = new MonitorManager(config);
		}
		return instance;
	}

	public static MonitorManager loadInstance(){
		return instance;
	}

	private FiboRuleConfig config;

	private final ConcurrentHashMap<String, BoundedPriorityBlockingQueue<NodeStatistics>> statisticsMap = new ConcurrentHashMap<>();

	private final ScheduledExecutorService printLogScheduler = Executors.newScheduledThreadPool(1);

	public MonitorManager(FiboRuleConfig config) {
		this.config = config;

		if(BooleanUtil.isTrue(config.getEnableLog())){
			this.printLogScheduler.scheduleAtFixedRate(new MonitorTimerTask(this), config.getDelay(), config.getPeriod(), TimeUnit.MILLISECONDS);
		}
	}

	public void addStatistics(NodeStatistics statistics){
		if(statisticsMap.containsKey(statistics.getNodeClazzName())){
			statisticsMap.get(statistics.getNodeClazzName()).offer(statistics);
		}else{
			BoundedPriorityBlockingQueue<NodeStatistics> queue = new BoundedPriorityBlockingQueue<>(config.getQueueLimit());
			queue.offer(statistics);
			statisticsMap.put(statistics.getNodeClazzName(), queue);
		}
	}

	public void printStatistics(){
		try{
			Map<String, BigDecimal> compAverageTimeSpent = new HashMap<String, BigDecimal>();
			
			for(Entry<String, BoundedPriorityBlockingQueue<NodeStatistics>> entry : statisticsMap.entrySet()){
				long totalTimeSpent = 0;
				for(NodeStatistics statistics : entry.getValue()){
					totalTimeSpent += statistics.getTimeSpent();
				}
				compAverageTimeSpent.put(entry.getKey(), new BigDecimal(totalTimeSpent).divide(new BigDecimal(entry.getValue().size()), 2, RoundingMode.HALF_UP));
			}

			List<Entry<String, BigDecimal>> compAverageTimeSpentEntryList = new ArrayList<>(compAverageTimeSpent.entrySet());

			Collections.sort(compAverageTimeSpentEntryList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

			StringBuilder logStr = new StringBuilder();
			logStr.append("以下为FiboRule中间件统计信息：\n");
			logStr.append("======================================================================================\n");
			logStr.append("===================================SLOT INFO==========================================\n");
			logStr.append(MessageFormat.format("CONTEXT总大小 : {0}\n", config.getContextSize()));
			logStr.append(MessageFormat.format("CONTEXT占用数量 : {0}\n", Contextmanager.OCCUPY_COUNT));
			logStr.append("===============================TIME AVERAGE SPENT=====================================\n");
			for(Entry<String, BigDecimal> entry : compAverageTimeSpentEntryList){
				logStr.append(MessageFormat.format("节点[{0}]平均消耗时间 : {1}\n", entry.getKey(), entry.getValue()));
			}
			logStr.append("======================================================================================\n");
			log.info(logStr.toString());
		}catch(Exception e){
			log.error("打印统计信息报错",e);
		}
	}

	public FiboRuleConfig getConfig() {
		return config;
	}

	public void setConfig(FiboRuleConfig config) {
		this.config = config;
	}

	public void closeScheduler(){
		this.printLogScheduler.shutdown();
	}
}
