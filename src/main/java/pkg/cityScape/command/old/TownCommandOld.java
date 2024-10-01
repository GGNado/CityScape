package pkg.cityScape.command.old;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.enums.Role;
import pkg.cityScape.manager.CitizenManager;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.manager.RegionManager;
import pkg.cityScape.manager.TownManager;
import pkg.cityScape.model.Citizen;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

import java.util.*;
import java.util.stream.Collectors;

public class TownCommandOld implements CommandExecutor {
    private CityScape cityScape;
    private TownManager townManager;
    private CitizenManager citizenManager;
    private RegionManager regionManager;

    public TownCommandOld(CityScape cityScape, TownManager townManager, CitizenManager citizenManager, RegionManager regionManager) {
        this.cityScape = cityScape;
        this.townManager = townManager;
        this.citizenManager = citizenManager;
        this.regionManager = regionManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("You must be a player to use this command.");
            return false;
        }
        Player player = (Player) commandSender;
        if (strings.length == 0) {
            commandList(player);
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("help")) {
            commandList(player);
            //TOWN CREATE ITALY
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("create")) {
            Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
            Integer price = ConfigManager.getTownGold();

            if (citizen.goldInInventory(player) < price) {
                player.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + price + ChatColor.RED + " to create towns");
                return false;
            }

            List<Town> towns = new ArrayList<>(cityScape.getTowns().values());
            for (Town town : towns) {
                for (Citizen people : town.getCitizens())
                    if (people.getUuid().equals(player.getUniqueId())) {
                        player.sendMessage(ChatColor.RED + "You are already part of a town!");
                        return false;
                    }
            }

            if (regionManager.isRegionClaim(player.getChunk())){
                player.sendMessage(ChatColor.RED + ConfigManager.getChunkAlreadyClaimMessage());
                return false;
            }

            int id = townManager.getNextID(); //ConfigManager.getNextID();
            townManager.updateNextID(); //ConfigManager.updateNextID();
            //cityScape.saveConfig();
            List<Citizen> citizens = new ArrayList<>();
            citizens.add(cityScape.getCitizens().get(player.getUniqueId()));
            Town town = new Town(id, strings[1], 0, 1, cityScape.getCitizens().get(player.getUniqueId()), null, new ArrayList<>(), citizens, new HashMap<>(), player.getLocation(), false, 10);

            townManager.setNewTown(town);
            cityScape.getTowns().put(id, town);
            Bukkit.broadcastMessage(ChatColor.YELLOW + player.getName() + " " + ConfigManager.getNewTownCreatedMessage());
            cityScape.getCitizens().get(player.getUniqueId()).setRole(Role.MAYOR);
            citizenManager.updateRole(player.getUniqueId(), Role.MAYOR);

            Map<ChunkPermission, Boolean> permessi = new HashMap<>();
            permessi.put(ChunkPermission.ATTACK_ANIMALS, false);
            permessi.put(ChunkPermission.ATTACK_MONSTERS, false);
            permessi.put(ChunkPermission.BREAK, false);
            permessi.put(ChunkPermission.FARM, false);
            permessi.put(ChunkPermission.BUILD, false);
            permessi.put(ChunkPermission.OPEN_CHEST, false);
            permessi.put(ChunkPermission.USE_FURNACE, false);
            permessi.put(ChunkPermission.USE_CRAFTING_TABLE, false);
            permessi.put(ChunkPermission.INTERACT_DOOR, false);
            permessi.put(ChunkPermission.INTERACT_BUTTON, false);
            permessi.put(ChunkPermission.INTERACT_PRESSURE_PLATE, false);
            permessi.put(ChunkPermission.PVP, false);
            permessi.put(ChunkPermission.RIDE_HORSE, false);

            Region region = new Region(player.getChunk().getX() + ":" + player.getChunk().getZ(),
                    town.getId(),
                    player.getChunk().getX(),
                    0,
                    player.getChunk().getZ(),
                    cityScape.getCitizens().get(player.getUniqueId()),
                    permessi);

            cityScape.getTowns().get(id).addRegion(region);
            regionManager.addRegions(region);
            cityScape.getRegions().put(region.getId(), region);
            citizen.removeGold(player, price);

        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("claim")) {

            if (regionManager.isRegionClaim(player.getChunk())){
                player.sendMessage(ChatColor.RED + ConfigManager.getChunkAlreadyClaimMessage());
                return false;
            }

            Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
            if (citizen.getRole() != Role.MAYOR) {
                player.sendMessage(ChatColor.RED + ConfigManager.getOnlyMayorMessage());
                return false;
            }

            /*
            Integer price = ConfigManager.getClaimChunkPrice();
            if (citizen.goldInInventory(player) < price) {
                player.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + price + ChatColor.RED + " to claim!");
                return false;
            }
            */

            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
                return false;
            }

            if (town.getGoldBank() < ConfigManager.getClaimChunkPrice()){
                player.sendMessage(ChatColor.RED + "The town needs " + ChatColor.GOLD + ConfigManager.getClaimChunkPrice() + ChatColor.RED + " to claim!");
                return false;
            }

            if (!regionManager.isRegionAdjacent(town, player.getChunk())){
                player.sendMessage(ChatColor.RED + ConfigManager.getClaimNotAdjacent());
                return false;
            }

            Map<ChunkPermission, Boolean> permessi = new HashMap<>();
            permessi.put(ChunkPermission.ATTACK_ANIMALS, false);
            permessi.put(ChunkPermission.ATTACK_MONSTERS, false);
            permessi.put(ChunkPermission.BREAK, false);
            permessi.put(ChunkPermission.FARM, false);
            permessi.put(ChunkPermission.BUILD, false);
            permessi.put(ChunkPermission.OPEN_CHEST, false);
            permessi.put(ChunkPermission.USE_FURNACE, false);
            permessi.put(ChunkPermission.USE_CRAFTING_TABLE, false);
            permessi.put(ChunkPermission.INTERACT_DOOR, false);
            permessi.put(ChunkPermission.INTERACT_BUTTON, false);
            permessi.put(ChunkPermission.INTERACT_PRESSURE_PLATE, false);
            permessi.put(ChunkPermission.PVP, false);
            permessi.put(ChunkPermission.RIDE_HORSE, false);
            Region region = new Region(player.getChunk().getX() + ":" + player.getChunk().getZ(),
                    town.getId(),
                    player.getChunk().getX(),
                    0,
                    player.getChunk().getZ(),
                    citizen,
                    permessi);
            regionManager.addRegions(region);
            cityScape.getRegions().put(region.getId(), region);
            cityScape.getTowns().get(town.getId()).addRegion(region);
            cityScape.getTowns().get(town.getId()).removeGold(ConfigManager.getClaimChunkPrice());
            townManager.updateTown("towns." + town.getId() + ".goldBank", ConfigManager.getClaimChunkPrice());

            //citizen.removeGold(player, price);
            player.sendMessage(ChatColor.GREEN + "claim successfully");

        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("info")) {
            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
                return false;
            }
            sendCityInfo(player, town);
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("list")) {
            sendListCity(player);
        } else if (strings.length == 2 && strings[0].equalsIgnoreCase("deposit")) {
            Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());

            try{
                Integer.parseInt(strings[1]);
            }catch (NumberFormatException e){
                player.sendMessage(ChatColor.RED + "You have to put a number!");
                return false;
            }

            if (citizen.goldInInventory(player) < Integer.parseInt(strings[1])) {
                player.sendMessage(ChatColor.RED + "You haven't got enough gold!");
                return false;
            }

            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
                return false;
            }

            citizen.removeGold(player, Integer.parseInt(strings[1]));
            cityScape.getTowns().get(town.getId()).addGold(Integer.parseInt(strings[1]));
            townManager.updateTown("towns." + town.getId() + ".goldBank", Integer.parseInt(strings[1]));

        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("spawn")) {
            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
                return false;
            }
            player.teleport(town.getSpawnLocation());
            player.spawnParticle(Particle.FIREWORK, player.getLocation(), 100);

            //set
        } else if (strings[0].equalsIgnoreCase("set")) {
            Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
            if (citizen.getRole() != Role.MAYOR && citizen.getRole() != Role.COMAYOR){
                player.sendMessage(ChatColor.RED + ConfigManager.getOnlyMayorMessage());
                return false;
            }

            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
                return false;
            }

            if (strings.length == 1)
                return false;

            switch (strings[1].toLowerCase()){
                case "spawn":
                    setNewTownSpawn(town, player.getLocation());
                    player.sendMessage(ChatColor.GREEN + "Spawn set successfully");
                    break;
                case "public":
                    boolean isOpenInverse = !town.isSpawnOpen();
                    townManager.updateTown("towns." + town.getId() + ".spawn.is_open", isOpenInverse);
                    cityScape.getTowns().get(town.getId()).setSpawnOpen(isOpenInverse);
                    player.sendMessage(ChatColor.GREEN + "The public spawn is now " + (isOpenInverse ? "opened" : "closed"));
                    break;
                case "cost":
                    if (strings.length != 3)
                        return false;

                    int x = 0;
                    try {
                        x = Integer.parseInt(strings[2]);
                        townManager.updateTown("towns." + town.getId() + ".spawn.spawn_cost", x);
                        cityScape.getTowns().get(town.getId()).setSpawnCost(x);
                        player.sendMessage(ChatColor.GREEN + "The cost is now " + ChatColor.GOLD + x + " Gold");
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "You have to put a number!");
                    }
                    break;
                case "pvp":
                    //String key = player.getChunk().getX() + ":" + player.getChunk().getZ();
                    Region region = exsistRegion(player);
                    if (region == null) {
                        player.sendMessage(ChatColor.RED + "Not here...");
                        return false;
                    }

                    boolean isPvPEnable = region.getChunkPermission().get(ChunkPermission.PVP);
                    region.getChunkPermission().put(ChunkPermission.PVP, !isPvPEnable);
                    regionManager.updateRegion(region, ".permission.pvp", !isPvPEnable);
                    player.sendMessage(ChatColor.GREEN + "PVP change successfully");
                    break;
                case "build":
                    region = exsistRegion(player);
                    if (region == null) {
                        player.sendMessage(ChatColor.RED + "Not here...");
                        return false;
                    }

                    boolean isBuildEnable = region.getChunkPermission().get(ChunkPermission.BUILD);
                    region.getChunkPermission().put(ChunkPermission.BUILD, !isBuildEnable);
                    regionManager.updateRegion(region, ".permission.build", !isBuildEnable);
                    player.sendMessage(ChatColor.GREEN + "Build change successfully");
                    break;
                case "break":
                    region = exsistRegion(player);
                    if (region == null) {
                        player.sendMessage(ChatColor.RED + "Not here...");
                        return false;
                    }

                    boolean isBreakEnable = region.getChunkPermission().get(ChunkPermission.BREAK);
                    region.getChunkPermission().put(ChunkPermission.BREAK, !isBreakEnable);
                    regionManager.updateRegion(region, ".permission.break", !isBreakEnable);
                    player.sendMessage(ChatColor.GREEN + "Break change successfully");
                    break;

                default:
                    commandList(player);
            }
        } else if (strings.length == 1 && strings[0].equalsIgnoreCase("infoc")) {
            Region region = cityScape.getRegions().get(player.getChunk().getX() + ":" + player.getChunk().getZ());
            if (region == null) {
                regionInfo(new Region(), player);
                return true;
            }
            regionInfo(region, player);
        }
        return false;
    }

    private void setNewTownSpawn(Town town, @NotNull Location location) {
        cityScape.getTowns().get(town.getId()).setSpawnLocation(location);
        townManager.updateTown(location, town);

    }

    public void commandList(Player player) {
        player.sendMessage(ChatColor.RED + "- /town help " + ChatColor.GRAY + "-> Show this list");
        player.sendMessage(ChatColor.RED + "- /town create <name> " + ChatColor.GRAY + "-> Create a new Town ");
        player.sendMessage(ChatColor.RED + "- /town claim " + ChatColor.GRAY + "-> Claim chunk");
        player.sendMessage(ChatColor.RED + "- /town list " + ChatColor.GRAY + "-> Show all the towns");
    }

    public void sendCityInfo(Player player, Town town) {
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "           " + ChatColor.BOLD + town.getTownName() + " Info");
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");

        // Informazioni generali
        player.sendMessage(ChatColor.YELLOW + "Mayor: " + ChatColor.WHITE + town.getMayor().getPlayer().getName());
        player.sendMessage(ChatColor.YELLOW + "Co-Mayor: " + ChatColor.WHITE + (town.getComayor() != null ? town.getComayor().getPlayer().getName() : "None"));
        player.sendMessage(ChatColor.YELLOW + "Gold in Bank: " + ChatColor.WHITE + town.getGoldBank());

        // Informazioni sui costruttori e cittadini
        player.sendMessage(ChatColor.YELLOW + "Builders: " + ChatColor.WHITE + formatCitizenList(town.getBuilders()));
        player.sendMessage(ChatColor.YELLOW + "Citizens: " + ChatColor.WHITE + formatCitizenList(town.getCitizens()));

        // Informazioni sulle regioni
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
        for (ChunkPermission permesso : region.getChunkPermission().keySet()){
            player.sendMessage( ChatColor.YELLOW + permesso.toString() + ": " + ChatColor.WHITE + (region.getChunkPermission().get(permesso) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        }
        player.sendMessage(ChatColor.GREEN + "§m----------------------------------------");
    }

    private Region exsistRegion(Player player){
        String key = player.getChunk().getX() + ":" + player.getChunk().getZ();
        Region region = cityScape.getRegions().get(key);
        return region;
    }

}
