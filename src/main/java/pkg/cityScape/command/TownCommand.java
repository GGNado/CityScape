package pkg.cityScape.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
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

public class TownCommand implements CommandExecutor {
    private CityScape cityScape;
    private TownManager townManager;
    private CitizenManager citizenManager;
    private RegionManager regionManager;

    public TownCommand(CityScape cityScape, TownManager townManager, CitizenManager citizenManager, RegionManager regionManager) {
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
                System.out.println("Sto verificando");
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
            Town town = new Town(id, strings[1], 0, 1, cityScape.getCitizens().get(player.getUniqueId()), null, new ArrayList<>(), citizens, new HashMap<>());

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
            Integer price = ConfigManager.getClaimChunkPrice();
            if (citizen.goldInInventory(player) < price) {
                player.sendMessage(ChatColor.RED + "You need " + ChatColor.GOLD + price + ChatColor.RED + " to claim!");
                return false;
            }

            Town town = cityScape.getTownByPlayerUUID(player);
            if (town == null) {
                player.sendMessage(ChatColor.RED + ConfigManager.getNotPartTownMessage());
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
            citizen.removeGold(player, price);
            player.sendMessage(ChatColor.GREEN + "claim successfully");

        } else {
            commandList(player);
        }
        return false;
    }

    public void commandList(Player player) {
        player.sendMessage(ChatColor.RED + "- /town help " + ChatColor.GRAY + "-> Show this list");
        player.sendMessage(ChatColor.RED + "- /town create <name> " + ChatColor.GRAY + "-> Create a new Town ");
        player.sendMessage(ChatColor.RED + "- /town claim " + ChatColor.GRAY + "-> Claim chunk");
        player.sendMessage(ChatColor.RED + "- /town list " + ChatColor.GRAY + "-> Show all the towns");
    }

}
