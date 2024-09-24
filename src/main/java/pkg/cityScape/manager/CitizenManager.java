package pkg.cityScape.manager;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.Role;
import pkg.cityScape.model.Citizen;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CitizenManager {
    private YamlConfiguration citizenConfig;
    private File citizenFile;
    private CityScape cityScape;

    public CitizenManager(CityScape cityScape) {
        this.cityScape = cityScape;
        citizenFile = new File(cityScape.getDataFolder(), "citizens.yml");
        if (citizenFile.exists()){
            citizenConfig = YamlConfiguration.loadConfiguration(citizenFile);
        } else {
            System.out.println("File citizens.yml non trovato, sarà creato.");
            citizenConfig = new YamlConfiguration();
        }
    }

    private void saveConfig() {
        try {
            citizenConfig.save(citizenFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel salvataggio del file citizens.yml");
        }
    }


    public void setNewCitizen(Citizen citizen) {
        String citizenKey = "citizens." + citizen.getUuid().toString();
        citizenConfig.set(citizenKey + ".uuid", citizen.getUuid().toString());
        citizenConfig.set(citizenKey + ".name", citizen.getPlayer().getName());
        citizenConfig.set(citizenKey + ".role", citizen.getRole().toString());

        saveConfig();
    }

    public boolean isCitizenInFile(Citizen citizen){
        return citizenConfig.contains("citizens." + citizen.getUuid().toString());
    }
    public boolean isCitizenInFile(UUID uuid){
        return citizenConfig.contains("citizens." + uuid.toString());
    }

    public Map<UUID, Citizen> getAllCitizens() {
        Map<UUID, Citizen> citizens = new HashMap<UUID, Citizen>();
        ConfigurationSection citizensSection = citizenConfig.getConfigurationSection("citizens");
        if (citizensSection == null) return citizens;

        for (String chiave : citizenConfig.getConfigurationSection("citizens").getKeys(false)) {
            UUID uuid = UUID.fromString(chiave);
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                Citizen citizen = new Citizen(uuid, player, Role.valueOf(citizenConfig.getString("citizens." + chiave + ".role")));
                citizens.put(citizen.getUuid(), citizen);
            } else {
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
                Citizen citizen = new Citizen(uuid, offlinePlayer, Role.valueOf(citizenConfig.getString("citizens." + chiave + ".role")));
                citizens.put(citizen.getUuid(), citizen);
            }
        }

        return citizens;
    }

    public void updateRole(UUID uniqueId, Role role) {
        citizenConfig.set("citizens." + uniqueId.toString() + ".role", role.toString());
        saveConfig();
    }
}
