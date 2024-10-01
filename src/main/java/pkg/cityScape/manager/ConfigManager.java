package pkg.cityScape.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import pkg.cityScape.CityScape;

public class ConfigManager {

    private static FileConfiguration config;

    public static void setupConfig(CityScape cityScape){
        ConfigManager.config = cityScape.getConfig();
        cityScape.saveDefaultConfig();
    }

    public static int getTownGold(){return config.getInt("create_gold_price");}
    public static int getClaimChunkPrice(){return config.getInt("claim_chunk_price");}
    public static int getNextID(){return config.getInt("next_id");}
    public static boolean isFirstJoinMessageEnabled(){return config.getBoolean("first_join_message_enabled");}
    public static String getFirstJoinMessage(){return config.getString("first_join_message");}
    public static boolean isJoinMessageEnabled(){return config.getBoolean("join_message_enabled");}
    public static String getJoinMessage(){return config.getString("join_message");}
    public static String getNewTownCreatedMessage(){return config.getString("new_town_created_message");}
    public static String getChunkAlreadyClaimMessage(){return config.getString("chunk_already_claim");}
    public static String getOnlyMayorMessage(){return config.getString("only_mayor_message");}
    public static String getNotPartTownMessage(){return config.getString("not_part_town");}
    public static String getClaimNotAdjacent(){return config.getString("claim_not_adjacent");}
    public static String getCantPlaceBlock(){return config.getString("cant_place_block");}
    public static String getCantKillAnimals(){return config.getString("cant_kill_animals");}
    public static String getCantInteractHere(){return config.getString("cant_interact_here");}
    public static String getCantKillMonster(){return config.getString("cant_kill_monster");}
    public static String getCantKillPlayer(){return config.getString("cant_kill_player");}
}
