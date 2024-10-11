package pkg.cityScape.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pkg.cityScape.CityScape;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.model.Town;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaceHolderAPI extends PlaceholderExpansion {

    private CityScape cityScape;

    public PlaceHolderAPI(CityScape cityScape) {
        this.cityScape = cityScape;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "CityScape";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nadoo";
    }

    @Override
    public @NotNull String getVersion() {
        return "0.2.2";
    }


    /*

     message = ChatColor.YELLOW + "%Metropoly_townBank%";
                parsedMessage = PlaceholderAPI.setPlaceholders(player, message);
                Score bancaTown = objective.getScore(ChatColor.GREEN + "Banca: " + parsedMessage)
    */

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        if (player == null)
            return "";

        List<Town> topTown = cityScape.getTowns().values().stream()
                .sorted(Comparator.comparing(Town::getGoldBank).reversed())
                .limit(3)
                .toList();

        List<Town> topClaim = cityScape.getTowns().values().stream()
                .sorted(Comparator.comparing(Town::getRegionsCount).reversed())
                .limit(3)
                .toList();

        List<Town> topCitizens = cityScape.getTowns().values().stream()
                .sorted(Comparator.comparing(Town::getCitizenCount).reversed())
                .limit(3)
                .toList();


        if (identifier.equals("town")){
            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null)
                return ConfigManager.getNotPartTownMessage();
            else
                return town.getTownName();

        } else if (identifier.equals("first_bank")) {
            return topTown.getFirst() != null ? topTown.getFirst().getTownName() + ": " +ChatColor.GOLD + topTown.getFirst().getGoldBank() + " gold" : "No info";

        } else if (identifier.equals("second_bank")) {
            return topTown.get(1) != null ? topTown.get(1).getTownName() + ": " +ChatColor.GOLD + topTown.get(1).getGoldBank() + " gold" : "No info";
        } else if (identifier.equals("third_bank")) {
            return topTown.get(2) != null ? topTown.get(2).getTownName() + ": " + ChatColor.BOLD + topTown.get(2).getGoldBank() + " gold" : "No info";
        } else if (identifier.equals("first_regions_claimed")) {
            return topClaim.getFirst() != null ? topClaim.getFirst().getTownName() + ": " +ChatColor.GOLD + topClaim.getFirst().getRegionsCount() + " regions" : "No info";
        } else if (identifier.equals("second_regions_claimed")) {
            return topClaim.get(1) != null ? topClaim.get(1).getTownName() + ": " +ChatColor.GOLD + topClaim.get(1).getRegionsCount() + " regions" : "No info";
        } else if (identifier.equals("third_regions_claimed")) {
            return topClaim.get(2) != null ? topClaim.get(2).getTownName() + ": " +ChatColor.GOLD + topClaim.get(2).getRegionsCount() + " regions" : "No info";
        } else if (identifier.equals("first_citizens")) {
            return topCitizens.getFirst() != null ? topCitizens.getFirst().getTownName() + ": " +ChatColor.GOLD + topCitizens.getFirst().getCitizenCount() + " citizens" : "No info";
        } else if (identifier.equals("second_citizens")) {
            return topCitizens.get(1) != null ? topCitizens.get(1).getTownName() + ": " +ChatColor.GOLD + topCitizens.get(1).getCitizenCount() + " citizens" : "No info";
        } else if (identifier.equals("third_citizens")) {
            return topCitizens.get(2) != null ? topCitizens.get(2).getTownName() + ": " +ChatColor.GOLD + topCitizens.get(2).getCitizenCount() + " citizens" : "No info";
        }

        return "PlaceHolderAPI error";
    }
}
