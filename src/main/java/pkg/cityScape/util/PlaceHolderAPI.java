package pkg.cityScape.util;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pkg.cityScape.CityScape;

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
        return "0.2.1";
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

        if (identifier.equals("town"))
            return "Africano";

        return "AAAA";
    }
}
