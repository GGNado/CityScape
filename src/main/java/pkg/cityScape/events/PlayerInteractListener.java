package pkg.cityScape.events;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.util.PermissionCheck;

import java.util.List;

public class PlayerInteractListener implements Listener {
    private CityScape cityScape;
    public PlayerInteractListener(CityScape cityScape) {
        this.cityScape = cityScape;
    }
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction().isLeftClick()) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() == Material.AIR) return;

        boolean flag = false;
        if (event.getClickedBlock().getType().equals(Material.CHEST)){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.OPEN_CHEST);
            if (flag) event.setCancelled(true);

        } else if (event.getClickedBlock().getType().equals(Material.FURNACE)){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.USE_FURNACE);
            if (flag) event.setCancelled(true);
        } else if (event.getClickedBlock().getType().equals(Material.CRAFTING_TABLE)){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.USE_CRAFTING_TABLE);
            if (flag) event.setCancelled(true);
        } else if (PermissionCheck.isDoor(event.getClickedBlock().getType())){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.INTERACT_DOOR);
            if (flag) event.setCancelled(true);
        } else if (PermissionCheck.isTrapdoor(event.getClickedBlock().getType())){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.INTERACT_DOOR);
            if (flag) event.setCancelled(true);
        } else if (PermissionCheck.isButton(event.getClickedBlock().getType()) || event.getClickedBlock().getType().equals(Material.LEVER)){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.INTERACT_BUTTON);
            if (flag) event.setCancelled(true);
        } else if (PermissionCheck.isPressurePlate(event.getClickedBlock().getType())){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.INTERACT_PRESSURE_PLATE);
            if (flag) event.setCancelled(true);
        } else if (PermissionCheck.isFarmingBlock(event.getClickedBlock().getType())){
            flag = PermissionCheck.isOutOfRules(cityScape, player, event.getClickedBlock().getChunk(), ChunkPermission.FARM);
            if (flag) event.setCancelled(true);
        }

        if (flag) player.sendMessage(ChatColor.RED + ConfigManager.getCantInteractHere());
    }
}
