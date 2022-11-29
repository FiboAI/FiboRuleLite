package com.fibo.rule.core.engine.condition;

import cn.hutool.core.util.StrUtil;
import com.fibo.rule.core.context.Contextmanager;
import com.fibo.rule.core.context.FiboContext;
import com.fibo.rule.core.engine.element.FiboEngineNode;
import com.fibo.rule.core.engine.element.FiboRunnable;
import com.fibo.rule.core.exception.NodeAllExecuteException;
import com.fibo.rule.core.thread.ExecutorHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * <p>并行执行</p>
 *
 * @author JPX
 * @since 2022-11-18 11:00
 */
@Slf4j
public class FiboAllCondition extends FiboCondition {

    private List<FiboRunnable> runnableList = new ArrayList<>();

    @Override
    public void runnerBranch(Integer contextIndex) {
        FiboContext context = Contextmanager.getContext(contextIndex);
        ExecutorService executorService = ExecutorHelper.loadInstance().buildAllExecutor();
        List<CompletableFuture<Boolean>> futures = runnableList.stream()
                .map(item -> CompletableFuture.supplyAsync(() -> runnerOne(item, contextIndex), executorService))
                .collect(Collectors.toList());
        CompletableFuture<Void> resultCompletableFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]));
        AtomicBoolean result = new AtomicBoolean();
        resultCompletableFuture.whenComplete((v, t) -> {
            boolean match = futures.stream().allMatch(item -> {
                try {
                    return item.get();
                } catch (InterruptedException | ExecutionException e) {
                    log.error(StrUtil.format("[{}]:并行节点[{}-{}]执行异常", context.getRequestId(), this.getAllNode().getNodeId(), this.getAllNode().getBeanName()), e);
                    return false;
                }
            });
            result.set(match);
        });
        if(!result.get()) {
            throw new NodeAllExecuteException(StrUtil.format("[{}]:并行节点[{}-{}]执行失败", context.getRequestId(), this.getAllNode().getNodeId(), this.getAllNode().getBeanName()));
        }
    }

    private boolean runnerOne(FiboRunnable fiboRunnable, Integer contextIndex) {
        FiboContext context = Contextmanager.getContext(contextIndex);
        try {
            fiboRunnable.runner(contextIndex);
            return true;
        } catch (Exception e) {
            log.error(StrUtil.format("[{}]:并行节点[{}-{}]分支执行异常", context.getRequestId(), this.getAllNode().getNodeId(), this.getAllNode().getBeanName()), e);
            return false;
        }
    }

    public void addRunnable(FiboRunnable fiboRunnable) {
        runnableList.add(fiboRunnable);
    }

    private FiboEngineNode getAllNode() {
        return (FiboEngineNode) this.getFiboRunnable();
    }

}
