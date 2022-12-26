package com.fibo.rule.core.monitor;

import java.util.TimerTask;

/**
 *<p>监控器线程</p>
 *
 *@author JPX
 *@since 2022/11/29 17:42
 */
public class MonitorTimerTask extends TimerTask {

    private MonitorManager monitorManager;

    public MonitorTimerTask(MonitorManager monitorManager) {
        this.monitorManager = monitorManager;
    }

    @Override
    public void run() {
        monitorManager.printStatistics();
    }
}
