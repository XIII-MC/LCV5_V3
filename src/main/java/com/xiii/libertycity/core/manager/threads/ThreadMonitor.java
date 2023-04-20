package com.xiii.libertycity.core.manager.threads;

import com.xiii.libertycity.LibertyCity;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.management.*;
import java.util.HashMap;
import java.util.Map;

public final class ThreadMonitor extends BukkitRunnable {

    private static final ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    private static final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    private static final Map<Long, Long> threadInitialCPU = new HashMap<>();
    private static final Map<Long, Float> threadCPUUsage = new HashMap<>();
    private static final long initialUptime = runtimeMxBean.getUptime();
    private static final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
    private ThreadInfo[] threadInfo;

    @Override
    public void run() {

        threadInfo = threadMxBean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadInfo) {
            threadInitialCPU.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(LibertyCity.getInstance(), () -> {

            long upTime = runtimeMxBean.getUptime();

            Map<Long, Long> threadCurrentCPU = new HashMap<>();
            threadInfo = threadMxBean.dumpAllThreads(false, false);
            for (ThreadInfo info : threadInfo) {
                threadCurrentCPU.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
            }

            // CPU over all processes
            // int nrCPUs = osMxBean.getAvailableProcessors();
            // total CPU: CPU % can be more than 100% (divided over multiple cpus)
            long nrCPUs = osMxBean.getAvailableProcessors();
            // elapsedTime is in ms.
            long elapsedTime = (upTime - initialUptime);
            for (ThreadInfo info : threadInfo) {
                // elapsedCpu is in ns
                Long initialCPU = threadInitialCPU.get(info.getThreadId());
                if (initialCPU != null) {
                    long elapsedCpu = threadCurrentCPU.get(info.getThreadId()) - initialCPU;
                    float cpuUsage = elapsedCpu / (elapsedTime * 1000000F * nrCPUs);
                    threadCPUUsage.put(info.getThreadId(), cpuUsage);
                }
            }
        }, 10*20);

        // threadCPUUsage contains cpu % per thread
        // You can use osMxBean.getThreadInfo(threadID) to get information on every thread reported in threadCPUUsage and analyze the most CPU intensive threads
    }

    public static float getThreadUsage(final long threadID) {
        return threadCPUUsage.getOrDefault(threadID, -1f);
    }
}
