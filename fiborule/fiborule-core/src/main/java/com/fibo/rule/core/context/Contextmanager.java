package com.fibo.rule.core.context;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *<p>引擎执行上下文管理，用来管理FiboContext，进行分配和回收</p>
 *
 *@author JPX
 *@since 2022/11/18 13:23
 */
public class Contextmanager {

	public static AtomicInteger OCCUPY_COUNT = new AtomicInteger(0);

	//这里为什么采用ConcurrentHashMap作为FiboContext存放的容器？
	//因为ConcurrentHashMap的随机取值复杂度也和数组一样为O(1)，并且没有并发问题，还有自动扩容的功能
	//用数组的话，扩容涉及copy，线程安全问题还要自己处理
	private static ConcurrentHashMap<Integer, FiboContext> FIBOCONTEXTS;

	private static ConcurrentLinkedQueue<Integer> QUEUE;

	//当前FiboContext的下标index的最大值
	private static Integer currentIndexMaxValue;

	//这里原先版本中是static块，现在改成init静态方法，由FlowExecutor中的init去调用
	//这样的改动对项目来说没有什么实际意义，但是在单元测试中，却有意义。
	//因为单元测试中所有的一起跑，jvm是不退出的，所以如果是static块的话，跑多个testsuite只会执行一次。
	//而由FlowExecutor中的init去调用，是会被执行多次的。保证了每个单元测试都能初始化一遍
	public static void init() {
		if (MapUtil.isEmpty(FIBOCONTEXTS)){
			// TODO: 2022/11/18 从配置中获取contxt池最大值
//			LiteflowConfig liteflowConfig = LiteflowConfigGetter.get();
//			currentIndexMaxValue = liteflowConfig.getSlotSize();
			currentIndexMaxValue = 10;

			FIBOCONTEXTS = new ConcurrentHashMap<>();
			QUEUE = IntStream.range(0, currentIndexMaxValue).boxed().collect(Collectors.toCollection(ConcurrentLinkedQueue::new));
		}
	}

	public static int offerContextByClass(List<Class<?>> paramBeanClazzArray) {
		//把classList通过反射初始化成对象列表
		//这里用newInstanceIfPossible这个方法，是为了兼容当没有无参构造方法所报的错
		List<Object> paramBeanList = paramBeanClazzArray.stream()
				.map((Function<Class<?>, Object>) ReflectUtil::newInstanceIfPossible).collect(Collectors.toList());

		FiboContext context = new FiboContext(paramBeanList);

		return offerIndex(context);
	}

	public static int offerSlotByBean(List<Object> paramBeanList){
		FiboContext context = new FiboContext(paramBeanList);
		return offerIndex(context);
	}

	private static int offerIndex(FiboContext context){
		try {
			//这里有没有并发问题？
			//没有，因为QUEUE的类型为ConcurrentLinkedQueue，并发情况下，每次取到的index不会相同
			//当然前提是QUEUE里面的值不会重复，但是这个是由其他机制来保证的
			Integer contextIndex = QUEUE.poll();

			if (ObjectUtil.isNull(contextIndex)){
				//只有在扩容的时候需要用到synchronized重量级锁
				//扩一次容，增强原来size的0.75，因为初始slot容量为1024，从某种层面来说，即便并发很大。但是扩容的次数不会很多。
				//因为单个机器的tps上限总归是有一个极限的，不可能无限制的增长。
				synchronized (Contextmanager.class){
					//在扩容的一刹那，去竞争这个锁的线程还是有一些，所以获得这个锁的线程这里要再次取一次。如果为null，再真正扩容
					contextIndex = QUEUE.poll();
					if (ObjectUtil.isNull(contextIndex)){
						int nextMaxIndex = (int) Math.round(currentIndexMaxValue * 1.75);
						QUEUE.addAll(IntStream.range(currentIndexMaxValue, nextMaxIndex).boxed().collect(Collectors.toCollection(ConcurrentLinkedQueue::new)));
						currentIndexMaxValue = nextMaxIndex;
						//扩容好，从队列里再取出扩容好的index
						contextIndex = QUEUE.poll();
					}
				}
			}

			if (ObjectUtil.isNotNull(contextIndex)) {
				FIBOCONTEXTS.put(contextIndex, context);
				OCCUPY_COUNT.incrementAndGet();
				return contextIndex;
			}
		} catch (Exception e) {
//			LOG.error("offer slot error", e);
			return -1;
		}
		return -1;
	}

	public static FiboContext getContext(int contextIndex){
		return FIBOCONTEXTS.get(contextIndex);
	}

	public static List<Object> getContextBeanList(int contextIndex){
		FiboContext context = getContext(contextIndex);
		return context.getContextBeanList();
	}

	public static void releaseContext(int slotIndex){
//		LiteflowConfig liteflowConfig = LiteflowConfigGetter.get();
		if(ObjectUtil.isNotNull(FIBOCONTEXTS.get(slotIndex))){
//			if (BooleanUtil.isTrue(liteflowConfig.getPrintExecutionLog())){
//				LOG.info("[{}]:slot[{}] released",SLOTS.get(slotIndex).getRequestId(),slotIndex);
//			}
			FIBOCONTEXTS.remove(slotIndex);
			QUEUE.add(slotIndex);
			OCCUPY_COUNT.decrementAndGet();
		}else{
//			LOG.warn("slot[{}] already has been released",slotIndex);
		}
	}
}
