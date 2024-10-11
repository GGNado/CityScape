package pkg.cityScape.manager;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerSet;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

import java.util.Map;

public class DynmapTerritoryManager {
    private DynmapAPI dynmapAPI;
    private MarkerSet markerSet;
    private TownManager townManager;
    private Map<String, Region> territorioMap;

    public DynmapTerritoryManager(DynmapAPI dynmapAPI, Map<String, Region> territorioMap, TownManager townManager) {
        this.dynmapAPI = dynmapAPI;
        this.territorioMap = territorioMap;
        this.townManager = townManager;

        // Crea un MarkerSet per i territori
        MarkerAPI markerAPI = dynmapAPI.getMarkerAPI();
        markerSet = markerAPI.getMarkerSet("territories");
        if (markerSet == null) {
            markerSet = markerAPI.createMarkerSet("territories", "Territori", null, false);
        }
    }

    public void colorChunksOnDynmap() {
        // Itera attraverso la mappa dei territori
        for (Region territorio : territorioMap.values()) {
            // Ottieni il mondo e le coordinate del chunk
            World world = Bukkit.getWorld("world");
            int chunkX = territorio.getX();
            int chunkZ = territorio.getZ();
            String color = "#FFFF00";  // Colore in formato esadecimale

            // Ottieni i vertici del chunk (converti chunkX e chunkZ in coordinate di blocco)
            double[] xPoints = {chunkX * 16, (chunkX + 1) * 16};
            double[] zPoints = {chunkZ * 16, (chunkZ + 1) * 16};

            // Crea un'area marker per il chunk
            String markerId = territorio.getFk_town() + "_" + chunkX + "_" + chunkZ;
            AreaMarker areaMarker = markerSet.createAreaMarker(markerId, "" + territorio.getFk_town(), false, world.getName(), xPoints, zPoints, false);

            if (areaMarker != null) {
                // Imposta il colore e lo stile dell'area
                areaMarker.setLineStyle(1, 0.5, 0x000000);  // Bordo nero
                areaMarker.setFillStyle(0.5, Integer.parseInt(color.replace("#", ""), 16));  // Colore di riempimento del territorio
            }
        }
    }

    public void colorChunkLive(Region territorio){
        World world = Bukkit.getWorld("world");
        int chunkX = territorio.getX();
        int chunkZ = territorio.getZ();
        String color = "#FFFF00";  // Colore in formato esadecimale

        double[] xPoints = {chunkX * 16, (chunkX + 1) * 16};
        double[] zPoints = {chunkZ * 16, (chunkZ + 1) * 16};

        String markerId = territorio.getId() + "_" + chunkX + "_" + chunkZ;
        AreaMarker areaMarker = markerSet.createAreaMarker(markerId, territorio.getId(), false, world.getName(), xPoints, zPoints, false);

        if (areaMarker != null) {
            // Imposta il colore e lo stile dell'area
            areaMarker.setLineStyle(1, 0.5, 0x000000);  // Bordo nero
            areaMarker.setFillStyle(0.5, Integer.parseInt(color.replace("#", ""), 16));  // Colore di riempimento del territorio
        }
    }

    public void addTownSpawnMarker(Town town) {
        Location spawnLocation = town.getSpawnLocation();
        if (spawnLocation != null) {
            // Ottieni il mondo e le coordinate dello spawn
            World world = Bukkit.getWorld("world");
            double x = spawnLocation.getX();
            double y = spawnLocation.getY();
            double z = spawnLocation.getZ();
            // Crea un marker con un'icona a forma di casa
            String markerId = "townSpawn_" + town.getTownName();
            Marker marker = markerSet.findMarker(markerId);

            if (marker != null) {
                marker.deleteMarker(); // Rimuovi il vecchio marker se esiste
            }

            markerSet.createMarker(markerId, "Spawn di " + town.getTownName(), "world", x, y, z, dynmapAPI.getMarkerAPI().getMarkerIcon("house"), false);
        }
    }
}