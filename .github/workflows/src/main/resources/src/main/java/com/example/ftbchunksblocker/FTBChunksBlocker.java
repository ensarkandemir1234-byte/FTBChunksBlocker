package com.example.ftbchunksblocker;

import org.bukkit.plugin.java.JavaPlugin;

import com.feed_the_beast.mods.ftbchunks.events.ClaimedChunkEvent;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionContainer;
import com.sk89q.worldguard.protection.managers.RegionManager;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import org.bukkit.entity.Player;

/**
 * Main plugin class
 */
@Mod.EventBusSubscriber
public final class FTBChunksBlocker extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("FTBChunksBlocker aktif!");
    }

    @Override
    public void onDisable() {
        getLogger().info("FTBChunksBlocker kapatıldı!");
    }

    /**
     * FTB Chunks claim eventini yakalar
     */
    @SubscribeEvent
    public static void onChunkClaim(ClaimedChunkEvent event) {
        if (!(event.getPlayer() instanceof Player player)) {
            return;
        }

        // WorldGuard bölge kontrolü
        RegionContainer container = WorldGuardPlugin.inst().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(player.getWorld()));

        if (regions == null) {
            return;
        }

        ApplicableRegionSet set = regions.getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation()));

        if (!set.getRegions().isEmpty()) {
            // Eğer oyuncu WorldGuard bölgesindeyse claim iptal edilir
            player.sendMessage("§cBu bölgede claim atamazsın!");
            event.setCanceled(true);
        }
    }
}
