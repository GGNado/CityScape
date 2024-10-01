package pkg.cityScape.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.util.PermissionCheck;

public class EntityDamageByEntityListener implements Listener {
    private CityScape cityScape;

    public EntityDamageByEntityListener(CityScape cityScape) {
        this.cityScape = cityScape;
    }
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {


            Player player = (Player) e.getDamager();
            if (PermissionCheck.isAnimal(e.getEntity())) {
                boolean flag = PermissionCheck.isOutOfRules(cityScape, player, e.getEntity().getChunk(), ChunkPermission.ATTACK_ANIMALS);
                if (flag) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + ConfigManager.getCantKillAnimals());
                    return;
                }
            } else if (PermissionCheck.isMonster(e.getEntity())){
                boolean flag = PermissionCheck.isOutOfRules(cityScape, player, e.getEntity().getChunk(), ChunkPermission.ATTACK_MONSTERS);
                if (flag){
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + ConfigManager.getCantKillMonster());
                }
            } else if (e.getEntity() instanceof Player) {
                boolean flag = PermissionCheck.isOutOfRules(cityScape, player, e.getEntity().getChunk(), ChunkPermission.PVP);
                if (flag) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + ConfigManager.getCantKillPlayer());
                }
            }
        }
    }
}
