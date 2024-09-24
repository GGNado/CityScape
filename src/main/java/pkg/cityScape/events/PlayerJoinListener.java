package pkg.cityScape.events;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.Role;
import pkg.cityScape.manager.CitizenManager;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.model.Citizen;

public class PlayerJoinListener implements Listener {
    private CitizenManager citizenManager;
    private CityScape cityScape;

    public PlayerJoinListener(CitizenManager citizenManager, CityScape cityScape) {
        this.citizenManager = citizenManager;
        this.cityScape = cityScape;
    }
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!player.hasPlayedBefore() && ConfigManager.isFirstJoinMessageEnabled()){
            event.setJoinMessage(ChatColor.GREEN + " " + ConfigManager.getFirstJoinMessage());
        } else if (ConfigManager.isJoinMessageEnabled()){
            event.setJoinMessage(ChatColor.GREEN + " " + ConfigManager.getJoinMessage());
        }

        //Se Player gia salvato nel file citizens.yml return
        if (citizenManager.isCitizenInFile(player.getUniqueId())) return;

        //Se non salvato
        Citizen citizen = new Citizen(player.getUniqueId(), player, Role.CITIZEN);
        citizenManager.setNewCitizen(citizen);
        cityScape.getCitizens().put(player.getUniqueId(), citizen);

    }
}
