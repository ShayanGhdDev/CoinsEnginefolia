package su.nightexpress.excellenteconomy.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

public final class SchedulerUtil {

    private static final boolean FOLIA;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        }
        catch (ClassNotFoundException e) {
            folia = false;
        }
        FOLIA = folia;
    }

    private SchedulerUtil() {}

    public static boolean isFolia() {
        return FOLIA;
    }

    public static void runSync(Plugin plugin, Runnable task) {
        if (FOLIA) {
            Bukkit.getGlobalRegionScheduler().run(plugin, scheduledTask -> task.run());
        }
        else {
            Bukkit.getScheduler().runTask(plugin, task);
        }
    }

    public static void runAsync(Plugin plugin, Runnable task) {
        if (FOLIA) {
            Bukkit.getAsyncScheduler().runNow(plugin, scheduledTask -> task.run());
        }
        else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, task);
        }
    }

    public static void runAsyncRepeating(Plugin plugin, Runnable task, long intervalTicks) {
        if (FOLIA) {
            long intervalMs = intervalTicks * 50L;
            Bukkit.getAsyncScheduler().runAtFixedRate(plugin, scheduledTask -> task.run(),
                intervalMs, intervalMs, TimeUnit.MILLISECONDS);
        }
        else {
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, intervalTicks, intervalTicks);
        }
    }
}
