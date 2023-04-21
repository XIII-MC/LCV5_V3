package com.xiii.libertycity.core.manager.threads;

import com.xiii.libertycity.LibertyCity;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.management.*;
import java.util.HashMap;
import java.util.Map;

public final class ThreadMonitor extends BukkitRunnable {

    private final ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
    private final RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
    private final Map<Long, Long> threadInitialCPU = new HashMap<>();
    private static final Map<Long, Float> threadCPUUsage = new HashMap<>();
    private final long initialUptime = runtimeMxBean.getUptime();
    private final OperatingSystemMXBean osMxBean = ManagementFactory.getOperatingSystemMXBean();
    private ThreadInfo[] threadInfo;

    @Override
    public void run() {

        threadInfo = threadMxBean.dumpAllThreads(false, false);
        for (ThreadInfo info : threadInfo) {
            threadInitialCPU.put(info.getThreadId(), threadMxBean.getThreadCpuTime(info.getThreadId()));
        }

        Bukkit.getScheduler().runTaskLaterAsynchronously(LibertyCity.getInstance(), () -> {

            /*

            long upTime = runtimeMxBean.getUptime();
            List<Long> threadCpuTime = new ArrayList<>();
            for (long i = 0L; i < threadInitialCPU.size(); i++) {
                long threadId = threadInitialCPU.get(i);
                if (threadId != -1) {
                    threadCpuTime.add(threadMxBean.getThreadCpuTime(threadId));
                } else {
                    threadCpuTime.add(0L);
                }
            }
            int nCPUs = osMxBean.getAvailableProcessors();
            if (initialUptime > 0L && upTime > initialUptime) {
                cpuUsageList.clear();
                // elapsedTime is in ms
                long elapsedTime = upTime - initialUptime;
                for (long i = 0L; i < threadInitialCPU.size(); i++) {
                    // elapsedCpu is in ns
                    long elapsedCpu = threadCpuTime.get((int) i) - threadInitialCPU.get(i);
                    // cpuUsage could go higher than 100% because elapsedTime
                    // and elapsedCpu are not fetched simultaneously. Limit to
                    // 99% to avoid Chart showing a scale from 0% to 200%.
                    float cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * 1000000F * nCPUs));
                    cpuUsageList.set(Math.toIntExact(threadInitialCPU.get(i)), cpuUsage);
                }
            }

            */

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
                    float cpuUsage = Math.min(99F, elapsedCpu / (elapsedTime * nrCPUs));
                    threadCPUUsage.put(info.getThreadId(), cpuUsage);
                }
            }
        }, 20*2);

        // threadCPUUsage contains cpu % per thread
        // You can use osMxBean.getThreadInfo(threadID) to get information on every thread reported in threadCPUUsage and analyze the most CPU intensive threads
    }

    public static float getThreadUsage(final long threadID) {
        return threadCPUUsage.get(threadID);
    }

    public static long getThreadUpTime(final long threadID) {
        final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        return threadMXBean.getThreadCpuTime(threadID);
    }
}
