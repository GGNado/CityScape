package pkg.cityScape;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerSet;
import pkg.cityScape.command.TownCommand;
import pkg.cityScape.command.TownTab;
import pkg.cityScape.database.Database;
import pkg.cityScape.events.*;
import pkg.cityScape.manager.*;
import pkg.cityScape.model.Citizen;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;
import pkg.cityScape.util.PlaceHolderAPI;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.*;

public final class CityScape extends JavaPlugin {
    //MANAGERS
    private TownManager townManager;
    private CitizenManager citizenManager;
    private RegionManager regionManager;

    private Map<Integer, Town> towns;
    private Map<UUID, Citizen> citizens;
    private Map<String, Region> regions;

    private DynmapAPI dynmapAPI;
    private MarkerSet markerSet;
    private DynmapTerritoryManager dynmapTerritoryManager;

    @Override
    public void onEnable() {
        ConfigManager.setupConfig(this);
        reloadConfig();
        saveResource("towns.yml", false);
        saveResource("citizens.yml", false);
        saveResource("regions.yml", false);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlaceHolderAPI(this).register();
        }

        citizenManager = new CitizenManager(this);
        regionManager = new RegionManager(this);
        townManager = new TownManager(this);

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(citizenManager, this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        //Bisogna testare i listener

        getCommand("city").setExecutor(new TownCommand(this, townManager, citizenManager, regionManager));
        getCommand("city").setTabCompleter(new TownTab(this));


         // FAI FARE IL GET DELLE TOWN
        citizens = citizenManager.getAllCitizens();
        towns = townManager.getAllTowns();
        regions = regionManager.getAllRegions();

        for (Region claim : regions.values()){
            towns.get(claim.getFk_town()).addRegion(claim);
        }

        Plugin dynmap = Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        if (dynmap instanceof DynmapAPI) {
            dynmapAPI = (DynmapAPI) dynmap;
            getLogger().info("Dynmap API trovata!");
            DynmapTerritoryManager dynmapTerritoryManager = new DynmapTerritoryManager(dynmapAPI, regions, townManager);
            dynmapTerritoryManager.colorChunksOnDynmap();
            this.dynmapTerritoryManager = dynmapTerritoryManager;
            for (Town town : towns.values()) {
                dynmapTerritoryManager.addTownSpawnMarker(town);

                if (ConfigManager.isDatabaseEnabled()) {
                    try {
                        if (Database.getTownById(town.getId())) {
                            Database.updateTown(town);
                        } else {
                            Database.addTown(town);
                        }
                    } catch (SQLException e) {
                        System.err.println("Errore durante l'inserimento della città " + town.getTownName() + ": " + e.getMessage());
                    }

                }
            }
        } else {
            getLogger().warning("Dynmap non trovato!");
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

    public Town getTownByName(String name) {
        for (Town town : towns.values()) {
            if (town.getTownName().equalsIgnoreCase(name)) {
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

    public void setTownManager(TownManager townManager) {
        this.townManager = townManager;
    }

    public DynmapTerritoryManager getDynmapTerritoryManager() {
        return dynmapTerritoryManager;
    }
}
