package pkg.cityScape.model;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Town {
    private int id;
    private String townName;
    private Integer goldBank;
    private Integer regionsCount;
    private Citizen mayor;
    private Citizen comayor;
    private Location spawnLocation;
    private boolean isSpawnOpen;
    private Integer spawnCost;
    private List<Citizen> builders;
    private List<Citizen> citizens;
    private Map<String, Region> regions; // <X:Y, Region>

    public Town(int id, String townName, Integer goldBank, Integer regionsCount, Citizen mayor, Citizen comayor, List<Citizen> builders, List<Citizen> citizens, Map<String, Region> regions, Location spawnLocation, boolean isSpawnOpen, Integer spawnCost) {
        this.id = id;
        this.townName = townName;
        this.goldBank = goldBank;
        this.mayor = mayor;
        this.comayor = comayor;
        this.builders = builders;
        this.citizens = citizens;
        this.regions = regions;
        this.regionsCount = regionsCount;
        this.spawnLocation = spawnLocation;
        this.isSpawnOpen = isSpawnOpen;
        this.spawnCost = spawnCost;
    }

    public Town(){
    }

    public int getId() {
        return id;
    }

    public String getTownName() {
        return townName;
    }

    public Integer getGoldBank() {
        return goldBank;
    }

    public Integer getRegionsCount() {
        return regions.size();
    }

    public Citizen getMayor() {
        return mayor;
    }

    public Citizen getComayor() {
        return comayor;
    }

    public List<Citizen> getBuilders() {
        return builders;
    }

    public List<Citizen> getCitizens() {
        return citizens;
    }

    public Map<String, Region> getRegions() {
        return regions;
    }

    public Citizen getCitizenByUUID(String uuid) {
        for (Citizen citizen : citizens) {
            if (citizen.getUuid().equals(uuid)) {
                return citizen;
            }
        }
        return null;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public void setGoldBank(Integer goldBank) {
        this.goldBank = goldBank;
    }

    public void setRegionsCount(Integer regionsCount) {
        this.regionsCount = regionsCount;
    }

    public void setMayor(Citizen mayor) {
        this.mayor = mayor;
    }

    public void setComayor(Citizen comayor) {
        this.comayor = comayor;
    }

    public void setBuilders(List<Citizen> builders) {
        this.builders = builders;
    }

    public void setCitizens(List<Citizen> citizens) {
        this.citizens = citizens;
    }

    public void setRegions(Map<String, Region> regions) {
        this.regions = regions;
    }

    public void addRegion(Region region){
        this.regions.put(region.getId(), region);
    }

    public void addGold(Integer gold){
        this.goldBank += gold;
    }

    public void removeGold(Integer gold) {
        this.goldBank -= gold;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public boolean isSpawnOpen() {
        return isSpawnOpen;
    }

    public void setSpawnOpen(boolean spawnOpen) {
        isSpawnOpen = spawnOpen;
    }

    public Integer getSpawnCost() {
        return spawnCost;
    }

    public void setSpawnCost(Integer spawnCost) {
        this.spawnCost = spawnCost;
    }

    public int getCitizenCount() {
        return citizens.size();
    }
}
