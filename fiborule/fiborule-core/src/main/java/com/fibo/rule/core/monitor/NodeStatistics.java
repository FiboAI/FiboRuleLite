package com.fibo.rule.core.monitor;

/**
 *<p>统计类</p>
 *
 *@author JPX
 *@since 2022/11/29 17:41
 */
public class NodeStatistics implements Comparable<NodeStatistics>{

	private String componentClazzName;

	private long timeSpent;

	private long memorySpent;

	private long recordTime;
	
	public NodeStatistics(String componentClazzName, long timeSpent) {
		this.componentClazzName = componentClazzName;
		this.timeSpent = timeSpent;
		this.recordTime = System.currentTimeMillis();
	}
	public String getComponentClazzName() {
		return componentClazzName;
	}

	public void setComponentClazzName(String componentClazzName) {
		this.componentClazzName = componentClazzName;
	}

	public long getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(long timeSpent) {
		this.timeSpent = timeSpent;
	}

	public long getMemorySpent() {
		return memorySpent;
	}

	public void setMemorySpent(long memorySpent) {
		this.memorySpent = memorySpent;
	}
	
	public long getRecordTime() {
		return recordTime;
	}
	
	@Override
	public int compareTo(NodeStatistics o) {
		if(o != null) {
			return this.recordTime >= o.getRecordTime()  ? -1 : 1;
		}
		return 1;
	}
}
