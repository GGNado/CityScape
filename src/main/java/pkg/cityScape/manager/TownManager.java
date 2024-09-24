package pkg.cityScape.manager;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import pkg.cityScape.CityScape;
import pkg.cityScape.model.Citizen;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TownManager {
    private YamlConfiguration townConfig;
    private File townFile;
    private CityScape cityScape;

    public TownManager(CityScape cityScape) {
        this.cityScape = cityScape;
        townFile = new File(cityScape.getDataFolder(), "towns.yml");
        if (townFile.exists()){
            townConfig = YamlConfiguration.loadConfiguration(townFile);
        } else {
            System.out.println("File towns.yml non trovato, sarà creato.");
            townConfig = new YamlConfiguration();
        }
    }


    public void setNewTown(Town town) {
        // La chiave sarà l'ID della città
        String townKey = "towns." + town.getId();

        // Inserire i dati della città nel config
        townConfig.set(townKey + ".townName", town.getTownName());
        townConfig.set(townKey + ".goldBank", town.getGoldBank());
        townConfig.set(townKey + ".regionsCount", town.getRegionsCount());

        // Inserire UUID del sindaco e vice-sindaco
        System.out.println("F");
        if (town.getMayor() != null) {
            System.out.println("ENTRO?");
            System.out.println(town.getMayor().getPlayer().getName());
            townConfig.set(townKey + ".mayorUUID", town.getMayor().getPlayer().getUniqueId().toString()); // Salva l'UUID come stringa
        }

        System.out.println("G");
        if (town.getComayor() != null) {
            townConfig.set(townKey + ".comayorUUID", town.getComayor().getPlayer().getUniqueId().toString());
        }
        System.out.println("H");
        // Inserire la lista dei costruttori (builder) con UUID
        List<Citizen> builders = town.getBuilders();
        if (builders != null && !builders.isEmpty()) {
            for (int i = 0; i < builders.size(); i++) {
                townConfig.set(townKey + ".builders." + i, builders.get(i).getPlayer().getUniqueId().toString());
            }
        }

        // Inserire la lista dei cittadini (citizens) con UUID
        List<Citizen> citizens = town.getCitizens();
        if (citizens != null && !citizens.isEmpty()) {
            for (int i = 0; i < citizens.size(); i++) {
                townConfig.set(townKey + ".citizens." + i, citizens.get(i).getPlayer().getUniqueId().toString());
            }
        }

        saveConfig();
    }

    public HashMap<Integer, Town> getAllTowns() {
        HashMap<Integer, Town> towns = new HashMap<>();

        // Verificare se la configurazione contiene delle città
        if (townConfig.contains("towns")) {
            // Ottenere tutte le chiavi delle città
            for (String key : townConfig.getConfigurationSection("towns").getKeys(false)) {
                try {
                    // Convertire la chiave in un intero per l'ID della città
                    int townID = Integer.parseInt(key);

                    // Creare un nuovo oggetto Town
                    String townName = townConfig.getString("towns." + key + ".townName");
                    int goldBank = townConfig.getInt("towns." + key + ".goldBank");
                    int regionsCount = townConfig.getInt("towns." + key + ".regionsCount");

                    // Recuperare il sindaco e il vice-sindaco
                    Citizen mayor = null;
                    if (townConfig.contains("towns." + key + ".mayorUUID")) {
                        String mayorUUID = townConfig.getString("towns." + key + ".mayorUUID");
                        mayor = cityScape.getCitizens().get(mayorUUID);
                    }

                    Citizen comayor = null;
                    if (townConfig.contains("towns." + key + ".comayorUUID")) {
                        String comayorUUID = townConfig.getString("towns." + key + ".comayorUUID");
                        comayor = cityScape.getCitizens().get(comayorUUID);
                    }

                    // Recuperare la lista dei costruttori (builders)
                    List<Citizen> builders = new ArrayList<>();
                    if (townConfig.contains("towns." + key + ".builders")) {
                        for (String builderKey : townConfig.getConfigurationSection("towns." + key + ".builders").getKeys(false)) {
                            String builderUUID = townConfig.getString("towns." + key + ".builders." + builderKey);
                            Citizen builder = cityScape.getCitizens().get(builderUUID);
                            builders.add(builder);
                        }
                    }

                    // Recuperare la lista dei cittadini (citizens)
                    System.out.println("DOVREI AGGIUNGERE!");
                    List<Citizen> citizens = new ArrayList<>();
                    if (townConfig.contains("towns." + key + ".citizens")) {
                        ConfigurationSection citizensSection = townConfig.getConfigurationSection("towns." + key + ".citizens");
                        System.out.println("A");
                        if (citizensSection != null) {
                            System.out.println("B");
                            // Ciclo attraverso i cittadini
                            for (String citizenKey : citizensSection.getKeys(false)) {
                                System.out.println("C");
                                String citizenUUID = citizensSection.getString(citizenKey); // Ottieni l'UUID del cittadino
                                //System.out.println("CONFORNTO: " + citizenUUID + " con " + cit);
                                Citizen citizen = cityScape.getCitizens().get(UUID.fromString(citizenUUID)); // Recupera il cittadino dalla mappa

                                if (citizen != null) {
                                    citizens.add(citizen); // Aggiungi il cittadino alla lista
                                    System.out.println("Aggiunto: " + citizen.getUuid() + ": " + citizen.getPlayer().getName());
                                } else {
                                    System.out.println("Cittadino con UUID " + citizenUUID + " non trovato.");
                                }
                            }
                        } else {
                            System.out.println("Sezione citizens non trovata per la città con chiave: " + key);
                        }
                    } else {
                        System.out.println("La chiave 'citizens' non esiste per la città con chiave: " + key);
                    }

                    // Creare un oggetto Town con le informazioni recuperate
                    HashMap<String, Region> regions = new HashMap<>();
                    Town town = new Town(townID, townName, goldBank, regionsCount, mayor, comayor, builders, citizens, regions);

                    // Aggiungere la città alla mappa
                    towns.put(townID, town);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Errore nel caricamento della città con ID: " + key);
                }
            }
        }

        return towns;
    }

    public int getNextID(){return townConfig.getInt("next_id");}
    public void updateNextID(){
        townConfig.set("next_id", getNextID() + 1);
        saveConfig();
    }

    private void saveConfig() {
        try {
            townConfig.save(townFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Errore nel salvataggio del file towns.yml");
        }
    }
}
