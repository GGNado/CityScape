package pkg.cityScape.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.util.PermissionCheck;

public class BlockBreakListener implements Listener {
    private CityScape cityScape;
    public BlockBreakListener(CityScape cityScape) {
        this.cityScape = cityScape;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        //if (e.getBlock().getType().equals(Material.CARROT))

        boolean flag = PermissionCheck.isOutOfRules(cityScape, player, e.getBlock().getChunk(), ChunkPermission.BREAK);

        if (flag)
            e.setCancelled(true);
        else
            e.setCancelled(false);

    }
}
