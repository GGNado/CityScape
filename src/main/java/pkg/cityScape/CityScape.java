package pkg.cityScape;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pkg.cityScape.command.TownCommand;
import pkg.cityScape.events.PlayerJoinListener;
import pkg.cityScape.manager.CitizenManager;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.manager.RegionManager;
import pkg.cityScape.manager.TownManager;
import pkg.cityScape.model.Citizen;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

import java.util.*;

public final class CityScape extends JavaPlugin {
    //MANAGERS
    private TownManager townManager;
    private CitizenManager citizenManager;
    private RegionManager regionManager;

    private Map<Integer, Town> towns;
    private Map<UUID, Citizen> citizens;
    private Map<String, Region> regions;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
        reloadConfig();
        saveResource("towns.yml", false);
        saveResource("citizens.yml", false);
        saveResource("regions.yml", false);

        citizenManager = new CitizenManager(this);
        regionManager = new RegionManager(this);
        townManager = new TownManager(this);


        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(citizenManager, this), this);

        getCommand("city").setExecutor(new TownCommand(this, townManager, citizenManager, regionManager));


         // FAI FARE IL GET DELLE TOWN
        citizens = citizenManager.getAllCitizens();
        towns = townManager.getAllTowns();
        regions = regionManager.getAllRegions();
        System.out.println(citizens);
        System.out.println(towns);

        for (Region claim : regions.values()){
            towns.get(claim.getFk_town()).addRegion(claim);
            System.out.println(towns.get(claim.getFk_town()).getRegions());
        }



    }

    public Town getTownByPlayerUUID(Player player){
        for (Town town : towns.values()) {
            for (Citizen people : town.getCitizens())
                if (people.getUuid().equals(player.getUniqueId())) {
                    return town;
                }
        }

        return null;
    }

    public TownManager getTownManager() {
        return townManager;
    }

    public CitizenManager getCitizenManager() {
        return citizenManager;
    }

    public RegionManager getRegionManager() {
        return regionManager;
    }

    public Map<Integer, Town> getTowns() {
        return towns;
    }

    public Map<UUID, Citizen> getCitizens() {
        return citizens;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public void addTowns(Map<Integer, Town> towns) {
        this.towns = towns;
    }
}
