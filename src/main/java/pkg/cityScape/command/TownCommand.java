package pkg.cityScape.command;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.enums.Role;
import pkg.cityScape.manager.*;
import pkg.cityScape.model.Citizen;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

import java.util.*;
import java.util.stream.Collectors;

public class TownCommand implements CommandExecutor {
    private final CityScape cityScape;
    private final TownManager townManager;
    private final CitizenManager citizenManager;
    private final RegionManager regionManager;

    private HashMap<UUID, List<String>> playerInvitedTowns;

    public TownCommand(CityScape cityScape, TownManager townManager, CitizenManager citizenManager, RegionManager regionManager) {
        this.cityScape = cityScape;
        this.townManager = townManager;
        this.citizenManager = citizenManager;
        this.regionManager = regionManager;
        playerInvitedTowns = new HashMap<>();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return false;
        }

        Player player = (Player) commandSender;

        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            commandList(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length == 2) {
                    handleCreateTown(player, args[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /c create <name>");
                }
                break;

            case "claim":
                handleClaim(player);
                break;

            case "info":
                handleTownInfo(player);
                break;

            case "list":
                sendListCity(player);
                break;

            case "deposit":
                if (args.length == 2) {
                    handleDeposit(player, args[1]);
                } else {
                    player.sendMessage(ChatColor.RED + "Usage: /c deposit <amount>");
                }
                break;

            case "spawn":
                handleSpawn(player);
                break;

            case "set":
                handleSetCommand(player, args);
                break;

            case "infoc":
                handleRegionInfo(player);
                break;
            case "invite":
                if (args.length == 2)
                    handleInvite(player, args);
                else
                    player.sendMessage(ChatColor.RED + "Usage: /c invite <name>");
                break;
            case "accept":
                if (args.length == 2)
                    handleAcceptInvite(player, args);
                else
                    player.sendMessage(ChatColor.RED + "Usage: /c accept <Town name>");
                break;

            default:
                commandList(player);
        }

        return true;
    }

    // --------- Handle individual commands ---------

    private void handleCreateTown(Player player, String townName) {
        Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
        Integer price = ConfigManager.getTownGold();

        if (citizen.goldInInventory(player) < price) {
            player.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + price + ChatColor.RED + " gold to create a town.");
            return;
        }

        // Check if player is already in a town
        for (Town town : cityScape.getTowns().values()) {
            for (Citizen citizen2 : town.getCitizens()) {
                if (citizen2.getUuid().equals(player.getUniqueId())) {
                    player.sendMessage(ChatColor.RED + "You are already part of a town!");
                    return;
                }
            }
        }

        if (regionManager.isRegionClaim(player.getChunk())) {
            player.sendMessage(ChatColor.RED + ConfigManager.getChunkAlreadyClaimMessage());
            return;
        }

        int id = townManager.getNextID();
        townManager.updateNextID();
        List<Citizen> citizens = new ArrayList<>();
        citizens.add(citizen);

        Town town = new Town(id, townName, 0, 1, citizen, null, new ArrayList<>(), citizens, new HashMap<>(), player.getLocation(), false, 10);
        townManager.setNewTown(town);
        cityScape.getTowns().put(id, town);
        Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " " + ConfigManager.getNewTownCreatedMessage());

        citizen.setRole(Role.MAYOR);
        citizenManager.updateRole(player.getUniqueId(), Role.MAYOR);
        createInitialRegion(player, town);
        citizen.removeGold(player, price);
    }

    private void handleClaim(Player player) {
        if (regionManager.isRegionClaim(player.getChunk())) {
            player.sendMessage(ChatColor.RED + ConfigManager.getChunkAlreadyClaimMessage());
            return;
        }

        Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
        if (citizen.getRole() != Role.MAYOR) {
            player.sendMessage(ChatColor.RED + ConfigManager.getOnlyMayorMessage());
            return;
        }

        Town town = cityScape.getTownByPlayerUUID(player);
        if (town == null) {
            player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
            return;
        }

        if (town.getGoldBank() < ConfigManager.getClaimChunkPrice()) {
            player.sendMessage(ChatColor.RED + "The town needs " + ChatColor.GOLD + ConfigManager.getClaimChunkPrice() + ChatColor.RED + " to claim!");
            return;
        }

        if (!regionManager.isRegionAdjacent(town, player.getChunk())) {
            player.sendMessage(ChatColor.RED + ConfigManager.getClaimNotAdjacent());
            return;
        }

        createRegion(player, town);
        town.removeGold(ConfigManager.getClaimChunkPrice());
        player.sendMessage(ChatColor.GREEN + "Chunk claimed successfully.");
    }

    private void handleTownInfo(Player player) {
        Town town = cityScape.getTownByPlayerUUID(player);
        if (town == null) {
            player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
            return;
        }
        sendCityInfo(player, town);
    }

    private void handleDeposit(Player player, String amountStr) {
        Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());

        try {
            int amount = Integer.parseInt(amountStr);
            if (citizen.goldInInventory(player) < amount) {
                player.sendMessage(ChatColor.RED + "You haven't got enough gold!");
                return;
            }

            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
                return;
            }

            citizen.removeGold(player, amount);
            town.addGold(amount);
            townManager.updateTown("towns." + town.getId() + ".goldBank", amount);

        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "You have to enter a valid number!");
        }
    }

    private void handleSpawn(Player player) {
        Town town = cityScape.getTownByPlayerUUID(player);
        if (town == null) {
            player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
            return;
        }
        player.teleport(town.getSpawnLocation());
        player.spawnParticle(Particle.FIREWORK, player.getLocation(), 100);
    }

    private void handleSetCommand(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(ChatColor.RED + "Usage: /town set <option>");
            return;
        }

        Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
        if (citizen.getRole() != Role.MAYOR && citizen.getRole() != Role.COMAYOR) {
            player.sendMessage(ChatColor.RED + ConfigManager.getOnlyMayorMessage());
            return;
        }

        Town town = cityScape.getTownByPlayerUUID(player);
        if (town == null) {
            player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
            return;
        }

        switch (args[1].toLowerCase()) {
            case "spawn":
                setNewTownSpawn(town, player.getLocation());
                player.sendMessage(ChatColor.GREEN + "Spawn set successfully");
                break;
            case "public":
                togglePublicSpawn(town, player);
                break;
            case "cost":
                if (args.length == 3) {
                    updateSpawnCost(town, args[2], player);
                }
                break;
            case "pvp":
            case "build":
            case "break":
            case "interact_pressure_plate":
            case "open_chest":
            case "use_furnace":
            case "use_crafting_table":
            case "attack_animals":
            case "attack_monsters":
            case "interact_door":
            case "interact_button":
            case "farm":
            case "ride_horse":
                toggleRegionPermission(player, args[1]);
                break;
            default:
                player.sendMessage(ChatColor.RED + "Unknown option for set command.");
        }
    }

    private void handleRegionInfo(Player player) {
        Region region = cityScape.getRegions().get(player.getChunk().getX() + ":" + player.getChunk().getZ());
        if (region == null) {
            regionInfo(new Region(), player);
            return;
        }
        regionInfo(region, player);
    }

    private void handleInvite(Player sindaco, String[] args) {
        Town town = cityScape.getTownByPlayerUUID(sindaco);
        if (town == null) {
            sindaco.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
            return;
        }
        Player cittadino = Bukkit.getPlayer(args[1]);

        if (cittadino == null) { return;}

        if (!cittadino.isOnline()) {
            sindaco.sendMessage(ChatColor.RED + ConfigManager.getPlayerNotOnline());
            return;
        }

        if (cittadino.getUniqueId() == sindaco.getUniqueId()) return;

        Town townCittadino = cityScape.getTownByPlayerUUID(cittadino);
        if (townCittadino != null) {
            sindaco.sendMessage(ChatColor.RED + ConfigManager.getPartTownMessage());
            return;
        }

        sindaco.sendMessage(ChatColor.GREEN + "Invite send to: " + cittadino.getPlayer().getName());
        cittadino.sendMessage(ChatColor.GREEN + "The town " + ChatColor.GOLD + town.getTownName() + ChatColor.GREEN + " invite you to join the town!" + ChatColor.GOLD + "/c accept " + town.getTownName());

        if (!playerInvitedTowns.containsKey(cittadino.getUniqueId())){
            List<String> citta = new ArrayList<>();
            citta.add(town.getTownName());
            playerInvitedTowns.put(cittadino.getUniqueId(), citta);
        } else {
            List<String> citta = playerInvitedTowns.get(cittadino.getUniqueId());
            citta.add(town.getTownName());
            playerInvitedTowns.put(cittadino.getUniqueId(), citta);
        }
    }

    private void handleAcceptInvite(Player cittadino, String[] args) {
        Town town = cityScape.getTownByPlayerUUID(cittadino);
        if (town != null) {
            cittadino.sendMessage(ChatColor.RED + ConfigManager.getPartTownMessage());
            return;
        }

        if (!playerInvitedTowns.containsKey(cittadino.getUniqueId())) {
            cittadino.sendMessage(ChatColor.RED + ConfigManager.getNotInvited());
            return;
        }

        List<String> citta = playerInvitedTowns.get(cittadino.getUniqueId());
        for (Town city : cityScape.getTowns().values()){
            if (city.getTownName().equalsIgnoreCase(args[1]) && citta.contains(args[1])){
                cittadino.sendMessage(ConfigManager.getPlayerJoinTown() + citta);
                cityScape.getTowns().get(city.getId()).setCitizens(townManager.addCitizen(city, cityScape.getCitizens().get(cittadino.getUniqueId())));
                playerInvitedTowns.remove(cittadino.getUniqueId());
                return;
            }
        }

        cittadino.sendMessage(ChatColor.RED + ConfigManager.getNotInvited());

    }
    // -------- Helper methods --------

    private void createInitialRegion(Player player, Town town) {
        Map<ChunkPermission, Boolean> permessi = initializeDefaultPermissions();
        Region region = new Region(player.getChunk().getX() + ":" + player.getChunk().getZ(),
                town.getId(), player.getChunk().getX(), 0, player.getChunk().getZ(),
                cityScape.getCitizens().get(player.getUniqueId()), permessi);

        town.addRegion(region);
        regionManager.addRegions(region);
        cityScape.getRegions().put(region.getId(), region);
    }

    private void createRegion(Player player, Town town) {
        Map<ChunkPermission, Boolean> permessi = initializeDefaultPermissions();
        Region region = new Region(player.getChunk().getX() + ":" + player.getChunk().getZ(),
                town.getId(), player.getChunk().getX(), 0, player.getChunk().getZ(),
                cityScape.getCitizens().get(player.getUniqueId()), permessi);

        regionManager.addRegions(region);
        cityScape.getRegions().put(region.getId(), region);
        town.addRegion(region);
        DynmapTerritoryManager dynmapTerritoryManager = cityScape.getDynmapTerritoryManager();
        if (dynmapTerritoryManager != null){
            dynmapTerritoryManager.colorChunkLive(region);
        }
    }

    private Map<ChunkPermission, Boolean> initializeDefaultPermissions() {
        Map<ChunkPermission, Boolean> permessi = new HashMap<>();
        for (ChunkPermission perm : ChunkPermission.values()) {
            permessi.put(perm, false);
        }
        return permessi;
    }

    private void setNewTownSpawn(Town town, @NotNull Location location) {
        town.setSpawnLocation(location);
        townManager.updateTown(location, town);
    }

    private void togglePublicSpawn(Town town, Player player) {
        boolean isOpenInverse = !town.isSpawnOpen();
        townManager.updateTown("towns." + town.getId() + ".spawn.is_open", isOpenInverse);
        town.setSpawnOpen(isOpenInverse);
        player.sendMessage(ChatColor.GREEN + "The public spawn is now " + (isOpenInverse ? "opened" : "closed"));
    }

    private void updateSpawnCost(Town town, String costStr, Player player) {
        try {
            int cost = Integer.parseInt(costStr);
            townManager.updateTown("towns." + town.getId() + ".spawn.spawn_cost", cost);
            town.setSpawnCost(cost);
            player.sendMessage(ChatColor.GREEN + "The cost is now " + ChatColor.GOLD + cost + " Gold");
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "You have to enter a valid number!");
        }
    }

    private void toggleRegionPermission(Player player, String permission) {
        Region region = exsistRegion(player);
        if (region == null) {
            player.sendMessage(ChatColor.RED + "Region not found here...");
            return;
        }

        ChunkPermission chunkPermission;
        try {
            chunkPermission = ChunkPermission.valueOf(permission.toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ChatColor.RED + "Invalid permission: " + permission);
            return;
        }

        boolean isEnabled = region.getChunkPermission().get(chunkPermission);
        region.getChunkPermission().put(chunkPermission, !isEnabled);
        regionManager.updateRegion(region, ".permission." + chunkPermission.name().toLowerCase(), !isEnabled);
        player.sendMessage(ChatColor.GREEN + chunkPermission.name() + " permission toggled successfully.");
    }

    // -------- Helper methods for displaying info --------

    public void commandList(Player player) {
        player.sendMessage(ChatColor.RED + "- /c help " + ChatColor.GRAY + "-> Show this list");
        player.sendMessage(ChatColor.RED + "- /c create <name> " + ChatColor.GRAY + "-> Create a new Town");
        player.sendMessage(ChatColor.RED + "- /c claim " + ChatColor.GRAY + "-> Claim the current chunk for your town");
        player.sendMessage(ChatColor.RED + "- /c list " + ChatColor.GRAY + "-> Show all towns on the server");
        player.sendMessage(ChatColor.RED + "- /c info " + ChatColor.GRAY + "-> Show information about your current town");
        player.sendMessage(ChatColor.RED + "- /c deposit <amount> " + ChatColor.GRAY + "-> Deposit gold into the town bank");
        player.sendMessage(ChatColor.RED + "- /c spawn " + ChatColor.GRAY + "-> Teleport to your town's spawn location");
        player.sendMessage(ChatColor.RED + "- /c set spawn " + ChatColor.GRAY + "-> Set the spawn point for your town");
        player.sendMessage(ChatColor.RED + "- /c set public " + ChatColor.GRAY + "-> Toggle public access to your town's spawn");
        player.sendMessage(ChatColor.RED + "- /c set cost <amount> " + ChatColor.GRAY + "-> Set the teleport cost to your town's spawn");
        player.sendMessage(ChatColor.RED + "- /c set pvp " + ChatColor.GRAY + "-> Toggle PvP in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set build " + ChatColor.GRAY + "-> Toggle build permissions in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set break " + ChatColor.GRAY + "-> Toggle block-breaking permissions in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set open_chest " + ChatColor.GRAY + "-> Toggle chest access in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set use_furnace " + ChatColor.GRAY + "-> Toggle furnace use in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set use_crafting_table " + ChatColor.GRAY + "-> Toggle crafting table use in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set farm " + ChatColor.GRAY + "-> Toggle farming in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set interact_door " + ChatColor.GRAY + "-> Toggle door interaction in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c set interact_button " + ChatColor.GRAY + "-> Toggle button and lever interaction in the current chunk");
        player.sendMessage(ChatColor.RED + "- /c infoc " + ChatColor.GRAY + "-> Show information about the current region");
    }

    public void sendCityInfo(Player player, Town town) {
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "           " + ChatColor.BOLD + town.getTownName() + " Info");
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");

        player.sendMessage(ChatColor.YELLOW + "Mayor: " + ChatColor.WHITE + town.getMayor().getPlayer().getName());
        player.sendMessage(ChatColor.YELLOW + "Co-Mayor: " + ChatColor.WHITE + (town.getComayor() != null ? town.getComayor().getPlayer().getName() : "None"));
        player.sendMessage(ChatColor.YELLOW + "Gold in Bank: " + ChatColor.WHITE + town.getGoldBank());
        player.sendMessage(ChatColor.YELLOW + "Builders: " + ChatColor.WHITE + formatCitizenList(town.getBuilders()));
        player.sendMessage(ChatColor.YELLOW + "Citizens: " + ChatColor.WHITE + formatCitizenList(town.getCitizens()));
        player.sendMessage(ChatColor.YELLOW + "Regions Claimed: " + ChatColor.WHITE + town.getRegions().size());
        player.sendMessage(ChatColor.YELLOW + "Spawn Cost: " + ChatColor.WHITE + town.getSpawnCost());
        player.sendMessage(ChatColor.YELLOW + "Public Spawn: " + ChatColor.WHITE + (town.isSpawnOpen() ? "Open" : "Close"));

        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
    }

    public void sendListCity(Player player) {
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "           " + ChatColor.BOLD + "City List");
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");

        for (Town town : cityScape.getTowns().values()) {
            player.sendMessage(ChatColor.YELLOW + town.getTownName() + ChatColor.WHITE + " - " + town.getRegions().size() + " Regions");
        }

        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
    }

    private String formatCitizenList(List<Citizen> citizens) {
        if (citizens == null || citizens.isEmpty()) {
            return ChatColor.GRAY + "None";
        }
        return citizens.stream()
                .map(citizen -> citizen.getPlayer() != null ? citizen.getPlayer().getName() : ChatColor.RED + "Offline")
                .collect(Collectors.joining(", "));
    }

    public void regionInfo(Region region, Player player) {
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "           " + ChatColor.BOLD + "Region Info");
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");

        if (region.getCitizenOwner() != null) {
            player.sendMessage(ChatColor.YELLOW + "Region Claimed by: " + ChatColor.WHITE + region.getCitizenOwner().getPlayer().getName());
        } else {
            player.sendMessage(ChatColor.YELLOW + "Region is not claimed");
        }

        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "           " + ChatColor.BOLD + "Permission Info");
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");

        for (ChunkPermission perm : region.getChunkPermission().keySet()) {
            player.sendMessage(ChatColor.YELLOW + perm.toString() + ": " + ChatColor.WHITE +
                    (region.getChunkPermission().get(perm) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        }

        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
    }

    private Region exsistRegion(Player player) {
        String key = player.getChunk().getX() + ":" + player.getChunk().getZ();
        return cityScape.getRegions().get(key);
    }
}