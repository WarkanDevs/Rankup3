package sh.okx.rankup.blockmanager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;
import sh.okx.rankup.RankupPlugin;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BlockManager implements Listener {

    private final Set<CachedEntry> recentPlacedBlocks = new HashSet<>();

    public void lockBlock(Location location) {
        recentPlacedBlocks.add(new CachedEntry(location, System.currentTimeMillis()));
    }

    public boolean isLocked(Location location) {
        return recentPlacedBlocks.stream().anyMatch(entry -> entry.location().equals(location));
    }

    public void unlockBlock(Location location) {
        recentPlacedBlocks.removeIf(entry -> entry.location().equals(location));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent e) {
        lockBlock(e.getBlock().getLocation());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent e) {
        if (isLocked(e.getBlock().getLocation())) {
            // ¡Penalizado! Decrementar 1 en la estadística
            var currentStat = e.getPlayer().getStatistic(Statistic.MINE_BLOCK, e.getBlock().getType());
            if (currentStat > 0) {
                e.getPlayer().setStatistic(Statistic.MINE_BLOCK, e.getBlock().getType(), currentStat - 1);
            }
            unlockBlock(e.getBlock().getLocation());
        }
    }

    public void shutdown() {
        recentPlacedBlocks.clear();
    }

    public void start(RankupPlugin plugin) {
        new BukkitRunnable() {
            @Override
            public void run() {
                // Dejar las entradas por 1 minuto
                recentPlacedBlocks.removeIf(entry -> System.currentTimeMillis() - entry.time() > 600000);
            }
        }.runTaskTimer(plugin, 20, 20);
    }

    public record CachedEntry(Location location, long time) {

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            CachedEntry that = (CachedEntry) o;
            return Objects.equals(location, that.location);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(location);
        }
    }
}
