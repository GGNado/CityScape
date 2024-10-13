package pkg.cityScape.model;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pkg.cityScape.enums.Role;
import java.util.UUID;


public class Citizen {
    private UUID uuid;
    private OfflinePlayer player;
    private Role role;

    public Citizen(UUID uuid, OfflinePlayer player, Role role) {
        this.uuid = uuid;
        this.player = player;
        this.role = role;
    }

    public Citizen(UUID uuid, Player player, Role role) {
        this.uuid = uuid;
        this.player = player;
        this.role = role;
    }

    public Citizen() {

    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public OfflinePlayer getPlayer() {
        return player;
    }

    public void setPlayer(OfflinePlayer player) {
        this.player = player;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Integer goldInInventory(Player player) {
        Inventory inventory = player.getInventory();
        int goldCount = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.GOLD_INGOT) {
                goldCount += item.getAmount();
            }
        }
        return goldCount;
    }

    public void removeGold(Player player, int amount) {
        Inventory inventory = player.getInventory();

        int remainingToRemove = amount;

        for (ItemStack item : inventory.getContents()) {
            if (item != null && item.getType() == Material.GOLD_INGOT) {
                int itemAmount = item.getAmount();

                if (itemAmount <= remainingToRemove) {
                    remainingToRemove -= itemAmount;
                    inventory.remove(item);
                } else {
                    item.setAmount(itemAmount - remainingToRemove);
                    remainingToRemove = 0;
                    break;
                }
                if (remainingToRemove <= 0) {
                    break;
                }
            }
        }

        // Se non siamo riusciti a rimuovere tutta la quantità richiesta, restituire un messaggio o gestire il caso
        if (remainingToRemove > 0) {
            player.sendMessage("Non hai abbastanza lingotti d'oro nell'inventario!");
        }
    }

    public void addGold(Player player, int amount) {
        //Inventory inventory = player.getInventory();
        player.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, amount));
    }
}
