package pkg.cityScape.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.util.PermissionCheck;

public class BlockPlaceListener implements Listener {
    private CityScape cityScape;

    public BlockPlaceListener(CityScape cityScape) {
        this.cityScape = cityScape;
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        boolean flag = PermissionCheck.isOutOfRules(cityScape, player, event.getBlock().getChunk(), ChunkPermission.BUILD);

        if (flag) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + ConfigManager.getCantPlaceBlock());
        } else {
            event.setCancelled(false);
        }
    }
}
