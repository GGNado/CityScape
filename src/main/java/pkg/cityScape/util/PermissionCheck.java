package pkg.cityScape.util;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;
import pkg.cityScape.CityScape;
import pkg.cityScape.enums.ChunkPermission;
import pkg.cityScape.enums.Role;
import pkg.cityScape.manager.ConfigManager;
import pkg.cityScape.model.Citizen;
import pkg.cityScape.model.Region;
import pkg.cityScape.model.Town;

public class PermissionCheck {
    public static boolean isOutOfRules(CityScape cityScape, Player player, Chunk chunk, ChunkPermission permission) {
        Region region = cityScape.getRegions().get(chunk.getX() + ":" + chunk.getZ());

        if (region == null) return false;

        if (region.getChunkPermission().get(permission)) return false;

        Citizen citizen = cityScape.getCitizens().get(player.getUniqueId());
        // TOWN DA CLAIM
        Town town = cityScape.getTowns().get(region.getFk_town());
        //TOWN DA PLAYER
        Town townPlayer = cityScape.getTownByPlayerUUID(player);

        if (townPlayer == null){
            //event.setCancelled(true);
            //player.sendMessage(ChatColor.RED + ConfigManager.getCantPlaceBlock());
            return true;
        }

        if (townPlayer.getId() != town.getId()){
            //event.setCancelled(true);
            //player.sendMessage(ChatColor.RED + ConfigManager.getCantPlaceBlock());
            return true;
        }

        if (citizen.getRole() == Role.CITIZEN){
            //event.setCancelled(true);
            //player.sendMessage(ChatColor.RED + ConfigManager.getCantPlaceBlock());
            return true;
        }

        return false;
    }

    public static boolean isDoor(Material material) {
        return material == Material.OAK_DOOR ||
                material == Material.SPRUCE_DOOR ||
                material == Material.BIRCH_DOOR ||
                material == Material.JUNGLE_DOOR ||
                material == Material.ACACIA_DOOR ||
                material == Material.DARK_OAK_DOOR ||
                material == Material.CRIMSON_DOOR ||
                material == Material.WARPED_DOOR ||
                material == Material.IRON_DOOR;
    }

    public static boolean isTrapdoor(Material material) {
        return material == Material.OAK_TRAPDOOR ||
                material == Material.SPRUCE_TRAPDOOR ||
                material == Material.BIRCH_TRAPDOOR ||
                material == Material.JUNGLE_TRAPDOOR ||
                material == Material.ACACIA_TRAPDOOR ||
                material == Material.DARK_OAK_TRAPDOOR ||
                material == Material.CRIMSON_TRAPDOOR ||
                material == Material.WARPED_TRAPDOOR ||
                material == Material.IRON_TRAPDOOR ||
                material == Material.MANGROVE_TRAPDOOR ||
                material == Material.BAMBOO_TRAPDOOR ||
                material == Material.CHERRY_TRAPDOOR;
    }

    // Controlla se il materiale è un bottone
    public static boolean isButton(Material material) {
        return material == Material.OAK_BUTTON ||
                material == Material.SPRUCE_BUTTON ||
                material == Material.BIRCH_BUTTON ||
                material == Material.JUNGLE_BUTTON ||
                material == Material.ACACIA_BUTTON ||
                material == Material.DARK_OAK_BUTTON ||
                material == Material.CRIMSON_BUTTON ||
                material == Material.WARPED_BUTTON ||
                material == Material.MANGROVE_BUTTON ||
                material == Material.BAMBOO_BUTTON ||
                material == Material.CHERRY_BUTTON;
    }

    public static boolean isPressurePlate(Material material) {
        return material == Material.OAK_PRESSURE_PLATE ||
                material == Material.SPRUCE_PRESSURE_PLATE ||
                material == Material.BIRCH_PRESSURE_PLATE ||
                material == Material.JUNGLE_PRESSURE_PLATE ||
                material == Material.ACACIA_PRESSURE_PLATE ||
                material == Material.DARK_OAK_PRESSURE_PLATE ||
                material == Material.CRIMSON_PRESSURE_PLATE ||
                material == Material.WARPED_PRESSURE_PLATE ||
                material == Material.STONE_PRESSURE_PLATE ||
                material == Material.HEAVY_WEIGHTED_PRESSURE_PLATE ||
                material == Material.LIGHT_WEIGHTED_PRESSURE_PLATE ||
                material == Material.MANGROVE_PRESSURE_PLATE ||
                material == Material.BAMBOO_PRESSURE_PLATE ||
                material == Material.CHERRY_PRESSURE_PLATE;
    }

    public static boolean isAnimal(Entity entity){
        return entity instanceof Cow ||
                entity instanceof Sheep ||
                entity instanceof Pig ||
                entity instanceof Rabbit ||
                entity instanceof Chicken ||
                entity instanceof Squid ||
                entity instanceof Armadillo ||
                entity instanceof Axolotl ||
                entity instanceof Bee ||
                entity instanceof Camel ||
                entity instanceof Cat ||
                entity instanceof Donkey ||
                entity instanceof Fox ||
                entity instanceof Frog ||
                entity instanceof Goat || //CR7 SIIUUUUM
                entity instanceof Hoglin ||
                entity instanceof Horse ||
                entity instanceof Llama ||
                entity instanceof Mule ||
                entity instanceof Ocelot ||
                entity instanceof Panda ||
                entity instanceof Sniffer ||
                entity instanceof Turtle ||
                entity instanceof Wolf;

    }

    public static boolean isMonster( Entity entity) {
        return entity instanceof Zombie ||
                entity instanceof Skeleton ||
                entity instanceof Creeper ||
                entity instanceof Blaze ||
                entity instanceof CaveSpider ||
                entity instanceof Drowned ||
                entity instanceof Guardian ||
                entity instanceof Enderman ||
                entity instanceof Endermite ||
                entity instanceof Evoker;
    }

    // Verifica se il blocco è coinvolto nell'attività di farming
    public static boolean isFarmingBlock(Material material) {
        return material == Material.FARMLAND ||   // Terreno arato
                material == Material.WHEAT ||      // Grano
                material == Material.CARROTS ||    // Carote
                material == Material.POTATOES ||   // Patate
                material == Material.BEETROOTS ||  // Barbabietole
                material == Material.MELON_STEM || // Gambi di melone
                material == Material.PUMPKIN_STEM || // Gambi di zucca
                material == Material.SWEET_BERRY_BUSH || // Cespuglio di bacche dolci
                material == Material.COCOA ||      // Cacao
                material == Material.NETHER_WART || // Verruca del Nether
                material == Material.BAMBOO;       // Bambù
    }
}
