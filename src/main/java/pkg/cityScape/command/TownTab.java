package pkg.cityScape.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pkg.cityScape.CityScape;
import pkg.cityScape.model.Town;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TownTab implements TabCompleter {
    private final CityScape cityScape;

    public TownTab(CityScape cityScape) {
        this.cityScape = cityScape;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> completions = new ArrayList<>();

        // Gestione del primo argomento: lista dei comandi principali
        if (args.length == 1) {
            List<String> commands = Arrays.asList("create", "claim", "list", "info", "deposit","withdraw", "spawn", "set", "infoc", "help");
            return StringUtil.copyPartialMatches(args[0], commands, completions);
        }

        // Gestione del secondo argomento, specifico per certi comandi
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("create")) {
                completions.add("<town_name>");
            } else if (args[0].equalsIgnoreCase("deposit") || args[0].equalsIgnoreCase("withdraw")) {
                completions.add("<amount>");
            } else if (args[0].equalsIgnoreCase("set")) {
                List<String> setOptions = Arrays.asList("spawn", "public", "cost", "pvp", "build", "break", "interact_pressure_plate", "open_chest", "use_furnace", "use_crafting_table", "attack_animals", "attack_monsters", "interact_door", "interact_button", "farm", "ride_horse");
                return StringUtil.copyPartialMatches(args[1], setOptions, completions);
            } else if (args[0].equalsIgnoreCase("info")) {
                List<String> infoOptions = new ArrayList<>();
                for (Town town : cityScape.getTowns().values()) {
                    infoOptions.add(town.getTownName());
                }
                return StringUtil.copyPartialMatches(args[1], infoOptions, completions);
            } else if (args[0].equalsIgnoreCase("spawn")) {
                List<String> spawnOption = new ArrayList<>();
                for (Town town : cityScape.getTowns().values()) {
                    spawnOption.add(town.getTownName());
                }
                return StringUtil.copyPartialMatches(args[1], spawnOption, completions);
            }
        }

        return completions;
    }
}