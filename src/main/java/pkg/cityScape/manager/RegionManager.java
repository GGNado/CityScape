package pkg.cityScape.manager;

import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegionManager {
    private YamlConfiguration regionConfig;
    private File regionFile;
    private CityScape cityScape;

    public RegionManager(CityScape cityScape) {
        this.cityScape = cityScape;
        regionFile = new File(cityScape.getDataFolder(), "regions.yml");
        if (regionFile.exists()){
            regionConfig = YamlConfiguration.loadConfiguration(regionFile);
        } else {
            System.out.println("File citizens.yml non trovato, sarà creato.");
            regionConfig = new YamlConfiguration();
        }
    }

    private void saveConfig() {
        try {
            regionConfig.save(regionFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel salvataggio del file regions.yml");
        }
    }

    public void addRegions(Region region){
        String path = "regions." + region.getId();

        regionConfig.set(path + ".fk_town", region.getFk_town());
        regionConfig.set(path + ".x", region.getX());
        regionConfig.set(path + ".y", region.getY());
        regionConfig.set(path + ".z", region.getZ());
        regionConfig.set(path + ".citizen_owner", region.getCitizenOwner().getUuid().toString());
        regionConfig.set(path + ".permission.break", region.getChunkPermission().get(ChunkPermission.BREAK));
        regionConfig.set(path + ".permission.build", region.getChunkPermission().get(ChunkPermission.BUILD));
        regionConfig.set(path + ".permission.open_chest", region.getChunkPermission().get(ChunkPermission.OPEN_CHEST));
        regionConfig.set(path + ".permission.use_furnace", region.getChunkPermission().get(ChunkPermission.USE_FURNACE));
        regionConfig.set(path + ".permission.use_crafting_table", region.getChunkPermission().get(ChunkPermission.USE_CRAFTING_TABLE));
        regionConfig.set(path + ".permission.attack_animals", region.getChunkPermission().get(ChunkPermission.ATTACK_ANIMALS));
        regionConfig.set(path + ".permission.attack_monsters", region.getChunkPermission().get(ChunkPermission.ATTACK_MONSTERS));
        regionConfig.set(path + ".permission.interact_door", region.getChunkPermission().get(ChunkPermission.INTERACT_DOOR));
        regionConfig.set(path + ".permission.interact_button", region.getChunkPermission().get(ChunkPermission.INTERACT_BUTTON));
        regionConfig.set(path + ".permission.interact_pressure_plate", region.getChunkPermission().get(ChunkPermission.INTERACT_PRESSURE_PLATE));
        regionConfig.set(path + ".permission.pvp", region.getChunkPermission().get(ChunkPermission.PVP));
        regionConfig.set(path + ".permission.farm", region.getChunkPermission().get(ChunkPermission.FARM));
        regionConfig.set(path + ".permission.ride_horse", region.getChunkPermission().get(ChunkPermission.RIDE_HORSE));

        saveConfig();
    }

    public boolean isRegionClaim(Chunk chunk){
        String key = chunk.getX() + ":" + chunk.getZ();
        Region region = cityScape.getRegions().get(key);
        return region != null;
        // APPENA PUOI CONTROLLA LE ADIACENZE DEI CHUNK!!!!!!!!!!!!
    }

    public Map<String, Region> getAllRegions() {
        Map<String, Region> regions = new HashMap<>();

        if (regionConfig.contains("regions")) {
            for (String regionId : regionConfig.getConfigurationSection("regions").getKeys(false)) {
                String path = "regions." + regionId;

                // Ottieni i dettagli della regione dalla configurazione
                int fkTown = regionConfig.getInt(path + ".fk_town");
                int x = regionConfig.getInt(path + ".x");
                int y = regionConfig.getInt(path + ".y");
                int z = regionConfig.getInt(path + ".z");
                String citizenOwnerUUID = regionConfig.getString(path + ".citizen_owner");

                // Creazione delle permissioni per il chunk
                Map<ChunkPermission, Boolean> permissions = new HashMap<>();
                permissions.put(ChunkPermission.BREAK, regionConfig.getBoolean(path + ".permission.break"));
                permissions.put(ChunkPermission.BUILD, regionConfig.getBoolean(path + ".permission.build"));
                permissions.put(ChunkPermission.OPEN_CHEST, regionConfig.getBoolean(path + ".permission.open_chest"));
                permissions.put(ChunkPermission.USE_FURNACE, regionConfig.getBoolean(path + ".permission.use_furnace"));
                permissions.put(ChunkPermission.USE_CRAFTING_TABLE, regionConfig.getBoolean(path + ".permission.use_crafting_table"));
                permissions.put(ChunkPermission.ATTACK_ANIMALS, regionConfig.getBoolean(path + ".permission.attack_animals"));
                permissions.put(ChunkPermission.ATTACK_MONSTERS, regionConfig.getBoolean(path + ".permission.attack_monsters"));
                permissions.put(ChunkPermission.INTERACT_DOOR, regionConfig.getBoolean(path + ".permission.interact_door"));
                permissions.put(ChunkPermission.INTERACT_BUTTON, regionConfig.getBoolean(path + ".permission.interact_button"));
                permissions.put(ChunkPermission.INTERACT_PRESSURE_PLATE, regionConfig.getBoolean(path + ".permission.interact_pressure_plate"));
                permissions.put(ChunkPermission.PVP, regionConfig.getBoolean(path + ".permission.pvp"));
                permissions.put(ChunkPermission.FARM, regionConfig.getBoolean(path + ".permission.farm"));
                permissions.put(ChunkPermission.RIDE_HORSE, regionConfig.getBoolean(path + ".permission.ride_horse"));

                // Creazione dell'oggetto Region
                Region region = new Region(regionId, fkTown, x, y, z, cityScape.getCitizens().get(UUID.fromString(citizenOwnerUUID)), permissions);

                // Aggiungi la regione alla mappa
                regions.put(regionId, region);
            }
        }

        return regions;
    }

    public Boolean isRegionAdjacent(Town town, Chunk chunk) {

        for (Region otherRegion : town.getRegions().values()) {
            
            int diffX = Math.abs(otherRegion.getX() - chunk.getX());
            int diffZ = Math.abs(otherRegion.getZ() - chunk.getZ());

            if ((diffX == 1 && diffZ == 0) || (diffX == 0 && diffZ == 1)) {
                return true;
            }
        }

        return false;
    }

    public void updateRegion(Region region, String path, boolean value) {
        String key = "regions." + region.getX() + ":" + region.getZ() + path;
        regionConfig.set(key, value);
        saveConfig();
    }
}
